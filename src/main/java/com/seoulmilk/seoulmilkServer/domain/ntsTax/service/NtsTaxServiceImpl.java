package com.seoulmilk.seoulmilkServer.domain.ntsTax.service;

import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetOcrTestResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.ocr.ClovaOcr;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.ocr.OcrExtractor;
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
    private final OcrExtractor ocrExtractor;
    private final NtsTaxRepository ntsTaxRepository;

    @Value("${ocr.api.secret-key}")
    private static String secretKey;

    @Override
    @Transactional
    public List<GetOcrTestResponseDTO> ocrTestResponse (List<MultipartFile> files) {
        List<GetOcrTestResponseDTO> responseList = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                // 파일 확장자 추출
                String fileExtension = getFileExtension(file.getOriginalFilename());

                LocalDateTime startTime = LocalDateTime.now();
                log.info("OCR 요청 시작 시간: {}", startTime);

                // OCR API 호출
                List<String> extractedOcr = clovaOcr.callApi("POST", file, secretKey, fileExtension);

                // DTO 변환 후 리스트에 추가
                responseList.add(new GetOcrTestResponseDTO(extractedOcr));

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
}
