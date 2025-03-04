package com.seoulmilk.seoulmilkServer.domain.ntsTax.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.ARAP;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.Status;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.GetOcrImageRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.GetOcrRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetNtsTaxResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetOcrResponseDTO;
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

    @Value("${ocr.api.secret-key}")
    private String secretKey;

    @Value("${ocr.api.template-ids}")
    private String templateIds;

    @Override
    @Transactional
    public List<GetNtsTaxResponseDTO> getOcrResponse(Member member, List<MultipartFile> files) {
        List<GetNtsTaxResponseDTO> responseList = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
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
                    GetOcrRequestDTO request = GetOcrRequestDTO.builder()
                            .images(Collections.singletonList(
                                    GetOcrImageRequestDTO.builder()
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
                    GetOcrResponseDTO getOcrResponse = clovaOcrClient.callApi(secretKey, request);
                    log.info("OCR API 응답 (템플릿 {} 적용): {}", templateId, objectMapper.writeValueAsString(getOcrResponse));

                    // OCR 파싱
                    ntsTax = OcrParser.parseOcrResponse(objectMapper.writeValueAsString(getOcrResponse), imageUrl);

                    // 유효한 데이터 -> 종료
                    if (ntsTax != null && !isInvalidNtsTax(ntsTax)) {
                        break;
                    }
                }
                if (ntsTax == null || isInvalidNtsTax(ntsTax)) {
                    ntsTax = NtsTax.builder()
                            .imageUrl(imageUrl)
                            .issueId("OCR 실패")
                            .issueDate(LocalDate.now())
                            .suId("OCR 실패")
                            .suName("OCR 실패")
                            .ipId("OCR 실패")
                            .ipName("OCR 실패")
                            .grandTotal("OCR 실패")
                            .chargeTotal("OCR 실패")
                            .taxTotal("OCR 실패")
                            .ARAP(ARAP.AR)
                            .status(Status.WAITING)
                            .build();
                }
                ntsTax = ntsTaxRepository.save(ntsTax);

                // 성공 여부 판별 후 DTO 변환
                boolean success = !(ntsTax.getIssueId() == null || "OCR 실패".equals(ntsTax.getIssueId()));
                responseList.add(GetNtsTaxResponseDTO.from(ntsTax, success));

                LocalDateTime endTime = LocalDateTime.now();
                Duration duration = Duration.between(startTime, endTime);
                log.info("OCR 요청 종료 시간: {}, 소요 시간: {}ms", endTime, duration.toMillis());

            } catch (IOException e) {
                throw new BusinessException(ErrorCode.OCR_REQUEST_FAILED);
            }
        }
        return responseList;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            throw new BusinessException(ErrorCode.NTS_TAX_INVALID_FILE);
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private boolean isInvalidNtsTax(NtsTax ntsTax) {
        return ntsTax.getIssueId() == null ||
                ntsTax.getIssueDate() == null ||
                ntsTax.getSuId() == null ||
                ntsTax.getSuName() == null ||
                ntsTax.getIpId() == null ||
                ntsTax.getIpName() == null ||
                ntsTax.getGrandTotal() == null ||
                ntsTax.getChargeTotal() == null ||
                ntsTax.getTaxTotal() == null;
    }
}