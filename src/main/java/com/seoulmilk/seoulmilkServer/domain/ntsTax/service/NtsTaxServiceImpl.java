package com.seoulmilk.seoulmilkServer.domain.ntsTax.service;

import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetOcrResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.ocr.ClovaOcr;
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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NtsTaxServiceImpl implements NtsTaxService {

    private final ClovaOcr clovaOcr;
    private final NtsTaxRepository ntsTaxRepository;

    @Value("${ocr.api.secret-key}")
    private static String secretKey;

    @Override
    @Transactional
    public List<GetOcrResponseDTO> ocrTestResponse (List<MultipartFile> files) { // 세금계산서 업로드 + OCR
        List<GetOcrResponseDTO> responseList = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                // 파일 확장자 추출
                String fileExtension = getFileExtension(file.getOriginalFilename());

                LocalDateTime startTime = LocalDateTime.now();
                log.info("OCR 요청 시작 시간: {}", startTime);

                // OCR API 호출 및 추출 후, NtsTax로 변환
                NtsTax ntsTax = clovaOcr.callApi("POST", file, secretKey, fileExtension);

                // 필수 값 X -> 저장 X, 실패 응답 추가
                if (ntsTax == null || isInvalidNtsTax(ntsTax)) {
                    responseList.add(GetOcrResponseDTO.from(ntsTax, false, "OCR 실패"));
                    continue; // 다음 파일 처리
                }
                ntsTaxRepository.save(ntsTax);

                // DTO 변환 후 리스트에 추가
                responseList.add(GetOcrResponseDTO.from(ntsTax, true, "OCR 성공"));

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
                ntsTax.getSuId() == null ||
                ntsTax.getIpId() == null ||
                ntsTax.getGrandTotal() == null ||
                ntsTax.getChargeTotal() == null ||
                ntsTax.getTaxTotal() == null;
    }
}
