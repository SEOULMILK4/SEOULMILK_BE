package com.seoulmilk.seoulmilkServer.domain.ntsTax.ocr;

import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Long.parseLong;

@Slf4j
@Service
@RequiredArgsConstructor
public class OcrExtractor {

    public static NtsTax parseOcrResponse(String jsonResponse) {
        Map<String, String> extractedData = new HashMap<>();

        try {
            JSONParser parser = new JSONParser();
            JSONObject responseJson = (JSONObject) parser.parse(jsonResponse);
            JSONArray images = (JSONArray) responseJson.get("images");

            if (images != null && !images.isEmpty()) {
                JSONObject imageResult = (JSONObject) images.get(0);
                JSONArray fields = (JSONArray) imageResult.get("fields");

                if (fields != null) {
                    for (Object fieldObj : fields) {
                        JSONObject field = (JSONObject) fieldObj;
                        String inferText = (String) field.get("inferText"); // OCR 추출 값
                        String boundingText = getBoundingText(field); // OCR 박스 주변 텍스트

                        // 승인번호
                        if (!extractedData.containsKey("승인번호") && inferText.matches("\\d{8}-\\d{8}-\\d{8}")) {
                            extractedData.put("승인번호", inferText);
                        }

                        // 등록번호 (공급자 & 공급받는자)
                        if (inferText.matches("\\d{3}-\\d{2}-\\d{5}")) {
                            extractedData.put("등록번호", inferText);
                        }

                        String normalizedText = boundingText.replaceAll("\\s", ""); // 공백 제거
                        if (normalizedText.contains("공급가액")) {
                            String numericValue = inferText.replaceAll("[^0-9,]", ""); // 숫자만 추출
                            extractedData.put("공급가액", numericValue);
                        } else if (normalizedText.contains("세액")) {
                            extractedData.put("세액", inferText.replaceAll("[^0-9,]", ""));
                        } else if (normalizedText.contains("합계금액")) {
                            extractedData.put("합계금액", inferText.replaceAll("[^0-9,]", ""));
                        } else if (normalizedText.contains("발행일자")) {
                            extractedData.put("발행일자", inferText);
                        }

                        log.info("OCR 추출 데이터: {}", extractedData);
                    }
                }
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OCR_PARSE_FAILED);
        }
        return NtsTax.builder()
                .issueId(extractedData.getOrDefault("승인번호", null))
                .issueDate(extractedData.containsKey("발행일자") ? LocalDate.parse(extractedData.get("발행일자")) : null)
                .suId(extractedData.getOrDefault("공급자 등록번호", null))
                .ipId(extractedData.getOrDefault("공급받는자 등록번호", null))
                .grandTotal(extractedData.containsKey("합계금액") ? parseLong(extractedData.get("합계금액")) : null)
                .chargeTotal(extractedData.containsKey("공급가액") ? parseLong(extractedData.get("공급가액")) : null)
                .taxTotal(extractedData.containsKey("세액") ? parseLong(extractedData.get("세액")) : null)
                .createdTime(LocalTime.now())
                .build();
    }

    /**
     * OCR 필드 주변의 텍스트를 가져오는 메서드
     */
    private static String getBoundingText(JSONObject field) {
        JSONArray boundingWords = (JSONArray) field.get("boundingWords");
        StringBuilder sb = new StringBuilder();

        if (boundingWords != null) {
            for (Object obj : boundingWords) {
                JSONObject word = (JSONObject) obj;
                sb.append((String) word.get("inferText")).append(" ");
            }
        }

        return sb.toString().trim();
    }
}