package com.seoulmilk.seoulmilkServer.domain.ntsTax.ocr;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.ARAP;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.Status;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.OcrResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OcrParser {

    public static NtsTax parseOcrResponse(Agency agency, String jsonResponse, String imageUrl) {
        try {
            OcrResponseDTO response = new ObjectMapper().readValue(jsonResponse, OcrResponseDTO.class);

            if (response.getImages() == null || response.getImages().isEmpty()) {
                return null;
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

            return NtsTax.builder()
                    .ARAP(ARAP.AR)
                    .status(Status.WAITING)
                    .agency(agency)
                    .member(agency.getMember())
                    .issueId(extractedData.getOrDefault("승인번호", null))
                    .issueDate(extractedData.containsKey("발행일자") ? LocalDate.parse(extractedData.get("발행일자")) : null)
                    .suId(extractedData.getOrDefault("공급자 등록번호", null))
                    .suName(extractedData.getOrDefault("공급자 사업체명", null))
                    .ipId(extractedData.getOrDefault("공급받는자 등록번호", null))
                    .ipName(extractedData.getOrDefault("공급받는자 사업체명", null))
                    .chargeTotal(extractedData.getOrDefault("공급가액", null))
                    .taxTotal(extractedData.getOrDefault("세액", null))
                    .grandTotal(extractedData.getOrDefault("합계금액", null))
                    .imageUrl(imageUrl)
                    .build();

        } catch (Exception e) {
            return null;
        }
    }
}