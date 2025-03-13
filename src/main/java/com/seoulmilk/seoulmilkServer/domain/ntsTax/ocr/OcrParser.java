package com.seoulmilk.seoulmilkServer.domain.ntsTax.ocr;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.ARAP;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.IsSuccess;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.Status;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.OcrResponseDTO;
import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class OcrParser {

    public static NtsTax parseOcrResponse(Agency agency, String jsonResponse, String imageUrl, String fileName) {
        try {
            OcrResponseDTO response = new ObjectMapper().readValue(jsonResponse, OcrResponseDTO.class);

            if (response.getImages() == null || response.getImages().isEmpty()) {
                throw new BusinessException(ErrorCode.NTS_TAX_NOT_UPLOAD);
            }

            OcrResponseDTO.ImageResult imageResult = response.getImages().get(0);
            List<OcrResponseDTO.Field> fields = imageResult.getFields();

            if (fields == null || fields.isEmpty()) {
                return null;
            }

            Map<String, String> extractedData = new HashMap<>();
            for (OcrResponseDTO.Field field : fields) {

                if (field.getInferText() == null) {
                    continue;
                }

                String normalizedText = field.getInferText().trim();

                // '.' -> ',' 변환
                normalizedText = normalizedText.replace(".", ",");

                // 숫자, ','만 유지
                if (normalizedText.matches("\\d{1,3}(,\\d{3})+")) {
                    normalizedText = normalizedText.replaceAll("[^0-9,]", "");
                }

                // 필드 이름을 OCR 응답에 맞게 수정
                switch (field.getName()) {
                    case "승인번호" -> extractedData.put("승인번호", normalizedText);
                    case "작성일자" -> extractedData.put("발행일자", normalizedText);
                    case "공급자 등록번호" -> extractedData.put("공급자 등록번호", normalizedText);
                    case "공급자 상호 - 법인명" -> extractedData.put("공급자 사업체명", normalizedText);
                    case "공급 받는 자 등록번호" -> extractedData.put("공급받는자 등록번호", normalizedText);
                    case "공급 받는자 상호 - 법인명" -> extractedData.put("공급받는자 사업체명", normalizedText);
                    case "공급가액" -> extractedData.put("공급가액", normalizedText);
                    case "세액" -> extractedData.put("세액", normalizedText);
                    case "합계금액" -> extractedData.put("합계금액", normalizedText);
                }
            }
            String issueIdPattern= "^\\d{8}-\\d{8}-\\d{8}$";
            String registeredPattern = "^\\d{3}-\\d{2}-\\d{5}$";
            String amountPattern = "^[0-9,]+$";

            // ocr 추출 데이터 전처리
            String issueId = isValid(extractedData.get("승인번호"), issueIdPattern)
                    ? extractedData.get("승인번호") : "";

            String suId = isValid(extractedData.get("공급자 등록번호"), registeredPattern)
                    ? extractedData.get("공급자 등록번호").replaceAll("[^0-9-]", "") : "";

            String ipId = isValid(extractedData.get("공급받는자 등록번호"), registeredPattern)
                    ? extractedData.get("공급받는자 등록번호").replaceAll("[^0-9-]", "") : "";

            String chargeTotal = isValid(extractedData.get("공급가액"), amountPattern)
                    ? extractedData.get("공급가액") : "";

            LocalDate issueDate;
            try {
                String rawDate = extractedData.get("발행일자");
                String formattedDate = rawDate != null ? rawDate.replaceAll("\\s+", "-") : "";

                issueDate = LocalDate.parse(formattedDate);
            } catch (DateTimeParseException e) {
                issueDate = LocalDate.of(1, 1, 1);
            }

            return NtsTax.builder()
                    .isSuccess(IsSuccess.SUCCESS)
                    .ARAP(ARAP.AR)
                    .status(Status.WAITING)
                    .agency(agency)
                    .member(agency.getMember())
                    .issueId(issueId)
                    .issueDate(issueDate)
                    .suId(suId)
                    .suName(extractedData.getOrDefault("공급자 사업체명", ""))
                    .ipId(ipId)
                    .ipName(extractedData.getOrDefault("공급받는자 사업체명", ""))
                    .chargeTotal(chargeTotal)
                    .taxTotal(extractedData.getOrDefault("세액", ""))
                    .grandTotal(extractedData.getOrDefault("합계금액", ""))
                    .imageUrl(imageUrl)
                    .fileName(fileName)
                    .build();

        } catch (Exception e) {
            return null;
        }
    }

    private static boolean isValid(String value, String pattern) {
        return value != null && Pattern.matches(pattern, value);
    }
}