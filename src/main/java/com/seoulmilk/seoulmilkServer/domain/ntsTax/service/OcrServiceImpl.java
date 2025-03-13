package com.seoulmilk.seoulmilkServer.domain.ntsTax.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.agency.service.AgencyAuthService;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.ARAP;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.IsSuccess;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.Status;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.OcrImageRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.OcrRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetOcrNtsTaxListResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetOcrNtsTaxResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.OcrResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.ocr.ClovaOcrClient;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.ocr.OcrParser;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.repository.NtsTaxRepository;
import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import com.seoulmilk.seoulmilkServer.global.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OcrServiceImpl implements OcrService{

    private final NtsTaxRepository ntsTaxRepository;
    private final ClovaOcrClient clovaOcrClient;
    private final ObjectMapper objectMapper;
    private final S3Service s3Service;
    private final AgencyAuthService agencyAuthService;

    @Value("${ocr.api.secret-key}")
    private String secretKey;

    @Value("${ocr.api.template-ids}")
    private String templateIds;

    @Override
    @Transactional
    public GetOcrNtsTaxListResponseDTO getOcrResponse(List<MultipartFile> files) {
        List<GetOcrNtsTaxResponseDTO> responseList = new ArrayList<>();
        Agency agency = agencyAuthService.getCurrentAgency();

        // OCR 전체 시작 시간
        LocalDateTime totalStartTime = LocalDateTime.now();

        for (MultipartFile file : files) {
            try {
                // 원본 파일명
                String fileName = file.getOriginalFilename();

                // S3에 업로드할 고유한 파일명 생성
                String fileExtension = getFileExtension(file.getOriginalFilename());
                String keyName = "ocr-uploads/" + UUID.randomUUID() + "." + fileExtension; // 고유한 keyName 생성

                // 개별 파일 업로드 후 URL 획득
                String imageUrl = s3Service.uploadFile(keyName, file);

                LocalDateTime startTime = LocalDateTime.now();
                log.info("OCR 요청 시작 시간: {}", startTime);

                NtsTax ntsTax = null;

                // 템플릿 ID 리스트 변환
                List<Integer> templateIdList = Arrays.stream(templateIds.split(","))
                        .map(String::trim)
                        .map(Integer::parseInt)
                        .toList();

                // 여러 개의 템플릿을 순차적으로 시도
                for (Integer templateId : templateIdList) {
                    OcrRequestDTO request = OcrRequestDTO.builder()
                            .images(Collections.singletonList(
                                    OcrImageRequestDTO.builder()
                                            .format(fileExtension)
                                            .name(file.getOriginalFilename())
                                            .url(imageUrl)
                                            .templateIds(Collections.singletonList(templateId))
                                            .build()
                            ))
                            .build();

                    // JSON 문자열로 변환
                    String jsonMessage = objectMapper.writeValueAsString(request);
                    log.info("OCR 요청 (템플릿 {} 적용): {}", templateId, jsonMessage);

                    // OpenFeign을 사용하여 OCR API 호출
                    OcrResponseDTO getOcrResponse = clovaOcrClient.callApi(secretKey, request);
                    log.info("OCR API 응답 (템플릿 {} 적용): {}", templateId, objectMapper.writeValueAsString(getOcrResponse));

                    // OCR 파싱
                    ntsTax = OcrParser.parseOcrResponse(agency, objectMapper.writeValueAsString(getOcrResponse), imageUrl, fileName);

                    // 유효한 데이터 & OCR 신뢰도 90% 이상 -> 성공 처리
                    if (ntsTax != null && isHighConfidence(getOcrResponse)) {
                        break;
                    }
                }
                if (ntsTax == null) {
                    ntsTax = NtsTax.builder()
                            .isSuccess(IsSuccess.FAILED)
                            .imageUrl(imageUrl)
                            .fileName(fileName)
                            .issueId(" ")
                            .issueDate(LocalDate.of(1, 1, 1))
                            .suId(" ")
                            .suName(" ")
                            .ipId(" ")
                            .ipName(" ")
                            .grandTotal(" ")
                            .chargeTotal(" ")
                            .taxTotal(" ")
                            .ARAP(ARAP.AR)
                            .status(Status.WAITING)
                            .member(agency.getMember())
                            .agency(agency)
                            .build();
                } else {
                    ntsTax = NtsTax.builder()
                            .isSuccess(isInvalidNtsTax(ntsTax) ? IsSuccess.FAILED : IsSuccess.SUCCESS)
                            .imageUrl(imageUrl)
                            .fileName(fileName)
                            .issueId(!isEmpty(ntsTax.getIssueId()) ? ntsTax.getIssueId() : " ")
                            .issueDate(ntsTax.getIssueDate() != null ? ntsTax.getIssueDate() : LocalDate.of(1, 1, 1))
                            .suId(!isEmpty(ntsTax.getSuId()) ? ntsTax.getSuId() : " ")
                            .suName(!isEmpty(ntsTax.getSuName()) ? ntsTax.getSuName() : " ")
                            .ipId(!isEmpty(ntsTax.getIpId()) ? ntsTax.getIpId() : " ")
                            .ipName(!isEmpty(ntsTax.getIpName()) ? ntsTax.getIpName() : " ")
                            .grandTotal(!isEmpty(ntsTax.getGrandTotal()) ? ntsTax.getGrandTotal() : " ")
                            .chargeTotal(!isEmpty(ntsTax.getChargeTotal()) ? ntsTax.getChargeTotal() : " ")
                            .taxTotal(!isEmpty(ntsTax.getTaxTotal()) ? ntsTax.getTaxTotal() : " ")
                            .ARAP(ARAP.AR)
                            .status(Status.WAITING)
                            .member(agency.getMember())
                            .agency(agency)
                            .build();
                }
                ntsTax = ntsTaxRepository.save(ntsTax);

                // 성공 여부 판별 후 DTO 변환
                responseList.add(GetOcrNtsTaxResponseDTO.from(agency, ntsTax));

                LocalDateTime endTime = LocalDateTime.now();
                Duration duration = Duration.between(startTime, endTime);
                log.info("OCR 요청 종료 시간: {}, 소요 시간: {}ms", endTime, duration.toMillis());

            } catch (IOException e) {
                throw new BusinessException(ErrorCode.OCR_REQUEST_FAILED);
            }
        }
        // OCR 전체 종료 시간
        LocalDateTime totalEndTime = LocalDateTime.now();
        Duration totalDuration = Duration.between(totalStartTime, totalEndTime);
        long totalTimes = totalDuration.toMillis();

        return GetOcrNtsTaxListResponseDTO.from(responseList, totalTimes);
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            throw new BusinessException(ErrorCode.NTS_TAX_INVALID_FILE);
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private boolean isInvalidNtsTax(NtsTax ntsTax) {
        return isEmpty(ntsTax.getIssueId()) ||
                ntsTax.getIssueDate() == null ||
                isEmpty(ntsTax.getSuId()) ||
                isEmpty(ntsTax.getSuName()) ||
                isEmpty(ntsTax.getIpId()) ||
                isEmpty(ntsTax.getIpName()) ||
                isEmpty(ntsTax.getGrandTotal()) ||
                isEmpty(ntsTax.getChargeTotal()) ||
                isEmpty(ntsTax.getTaxTotal());
    }

    private boolean isEmpty(String value) {
        return value == null || value.isBlank() || value.isEmpty();
    }

    private boolean isHighConfidence(OcrResponseDTO response) {
        if (response.getImages() == null || response.getImages().isEmpty()) {
            return false;
        }

        OcrResponseDTO.ImageResult imageResult = response.getImages().get(0);
        if (imageResult.getFields() == null || imageResult.getFields().isEmpty()) {
            return false;
        }

        // 모든 필드의 신뢰도가 90% 이상인지 확인
        for (OcrResponseDTO.Field field : imageResult.getFields()) {
            if (field.getInferConfidence() < 0.9) {
                return false;
            }
        }
        return true;
    }
}