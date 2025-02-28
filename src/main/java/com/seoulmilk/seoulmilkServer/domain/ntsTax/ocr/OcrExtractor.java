package com.seoulmilk.seoulmilkServer.domain.ntsTax.ocr;

import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.ARAP;
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
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OcrExtractor {

    public static NtsTax parseOcrResponse(String jsonResponse) {
        Map<String, String> extractedData = new HashMap<>();
        List<String> registrationNumbers = new ArrayList<>();
        String lastLabel = "";

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

                        // 승인번호
                        if (!extractedData.containsKey("승인번호") && inferText.matches("\\d{8}-\\d{8}-\\d{8}")) {
                            extractedData.put("승인번호", inferText);
                        }

                        // 공급자, 공급 받는 자
                        if (inferText.matches("\\d{3}-\\d{2}-\\d{5}")) {
                            registrationNumbers.add(inferText);
                        }

                        if (registrationNumbers.size() >= 2) {
                            extractedData.put("공급자 등록번호", registrationNumbers.get(0));
                            extractedData.put("공급받는자 등록번호", registrationNumbers.get(1));
                        }

                        // 발행일자
                        if (inferText.matches("\\d{4}-\\d{2}-\\d{2}")){
                            extractedData.put("발행일자", inferText);
                        }

                        // 발행일자
                        if (inferText.matches("\\d{4}-\\d{2}-\\d{2}")) {
                            extractedData.put("발행일자", inferText);
                        }

                        // 금액 라벨 감지
                        if (inferText.contains("공급가액")) {
                            lastLabel = "공급가액";
                        } else if (inferText.contains("세액")) {
                            lastLabel = "세액";
                        } else if (inferText.contains("합계금액")) {
                            lastLabel = "합계금액";
                        }

                        // 숫자 감지
                        if (inferText.matches("\\d{1,3}(,\\d{3})+")) {
                            if (!lastLabel.isEmpty() && !extractedData.containsValue(lastLabel)) {
                                extractedData.put(lastLabel, inferText.replaceAll("[^0-9,]", ""));
                                lastLabel = ""; // 저장 후 초기화
                            }
                        }
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
                .ARAP(ARAP.AR)
                .grandTotal(String.valueOf(extractedData.containsKey("합계금액") ? parseLong(extractedData.get("합계금액")) : null))
                .chargeTotal(String.valueOf(extractedData.containsKey("공급가액") ? parseLong(extractedData.get("공급가액")) : null))
                .taxTotal(String.valueOf(extractedData.containsKey("세액") ? parseLong(extractedData.get("세액")) : null))
                .createdTime(LocalTime.now())
                .build();
    }

    private static Long parseLong(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return Long.parseLong(value.replaceAll("[^0-9]", "")); // 숫자만 추출 후, 변환
    }
}