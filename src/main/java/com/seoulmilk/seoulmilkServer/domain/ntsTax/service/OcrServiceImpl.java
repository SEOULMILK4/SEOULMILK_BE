package com.seoulmilk.seoulmilkServer.domain.ntsTax.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.GetOcrImageRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.GetOcrRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetNtsTaxResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetOcrResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.ocr.ClovaOcrClient;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.ocr.OcrParser;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.repository.NtsTaxRepository;
import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OcrServiceImpl implements OcrService{

    private final NtsTaxRepository ntsTaxRepository;
    private final ClovaOcrClient clovaOcrClient;
    private final ObjectMapper objectMapper;

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
                // 파일 확장자 추출
                String fileExtension = getFileExtension(file.getOriginalFilename());

                LocalDateTime startTime = LocalDateTime.now();
                log.info("OCR 요청 시작 시간: {}", startTime);

                // 파일 -> Base64로 변환
                String base64Data = Base64.getEncoder().encodeToString(file.getBytes());

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
                                            .data(base64Data)
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
                    ntsTax = OcrParser.parseOcrResponse(objectMapper.writeValueAsString(getOcrResponse));

                    // 유효한 데이터 -> 종료
                    if (ntsTax != null && !isInvalidNtsTax(ntsTax)) {
                        break;
                    }
                }
                // 필수 값 X -> 저장 X, 실패 응답 추가
                if (ntsTax == null || isInvalidNtsTax(ntsTax)) {
                    responseList.add(GetNtsTaxResponseDTO.from(ntsTax,false));
                    continue; // 다음 파일 처리
                }
                ntsTaxRepository.save(ntsTax);

                // DTO 변환 후 응답 리스트에 추가
                responseList.add(GetNtsTaxResponseDTO.from(ntsTax, true));

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