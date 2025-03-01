package com.seoulmilk.seoulmilkServer.domain.ntsTax.ocr;

import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.ARAP;
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
        boolean expectingChargeTotal = false; // 공급가액 다음 줄을 읽음
        boolean expectingTaxTotal = false; // 세액 다음 줄을 읽음
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

                        // '.' -> ','로 변환
                        String normalizedText = inferText.replaceAll("\\.", ",");

                        // 승인번호 감지
                        if (!extractedData.containsKey("승인번호") && inferText.matches("\\d{8}-\\d{8}-\\d{8}")) {
                            extractedData.put("승인번호", inferText);
                        }

                        // 공급자, 공급받는자 등록번호 감지
                        if (inferText.matches("\\d{3}-\\d{2}-\\d{5}")) {
                            registrationNumbers.add(inferText);
                        }

                        if (registrationNumbers.size() >= 2) {
                            extractedData.put("공급자 등록번호", registrationNumbers.get(0));
                            extractedData.put("공급받는자 등록번호", registrationNumbers.get(1));
                        }

                        // 발행일자 감지
                        if (inferText.matches("\\d{4}-\\d{2}-\\d{2}")) {
                            extractedData.put("발행일자", inferText);
                        }

                        // 공급가액 감지
                        if (inferText.contains("공급가액")) {
                            expectingChargeTotal = true;
                            continue;
                        }
                        // 세액 감지
                        if (inferText.contains("세액")) {
                            expectingTaxTotal = true;
                            continue;
                        }

                        // 숫자 감지 및 저장
                        if (normalizedText.matches("\\d{1,3}(,\\d{3})+")) {
                            // 공급가액 저장
                            if (expectingChargeTotal && !extractedData.containsKey("공급가액")) {
                                extractedData.put("공급가액", normalizedText.replaceAll("[^0-9,]", ""));
                                expectingChargeTotal = false;
                                continue;
                            }
                            // 세액 저장
                            if (expectingTaxTotal && !extractedData.containsKey("세액")) {
                                extractedData.put("세액", normalizedText.replaceAll("[^0-9,]", ""));
                                expectingTaxTotal = false;
                                continue;
                            }
                        }

                        // 합계금액 감지
                        if (inferText.contains("합계금액")) {
                            lastLabel = "합계금액";
                        }
                        // 숫자 감지 -> 합계금액 저장
                        if (normalizedText.matches("\\d{1,3}(,\\d{3})+")) {
                            if (!lastLabel.isEmpty() && !extractedData.containsKey("합계금액")) {
                                extractedData.put("합계금액", normalizedText.replaceAll("[^0-9,]", ""));
                                lastLabel = "";
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OCR_PARSE_FAILED);
        }

        NtsTax ntsTax = NtsTax.builder()
                .issueId(extractedData.getOrDefault("승인번호", null))
                .issueDate(extractedData.containsKey("발행일자") ? LocalDate.parse(extractedData.get("발행일자")) : null)
                .suId(extractedData.getOrDefault("공급자 등록번호", null))
                .suName(extractedData.getOrDefault("공급자 사업체명", null))
                .ipId(extractedData.getOrDefault("공급받는자 등록번호", null))
                .ipName(extractedData.getOrDefault("공급받는자 사업체명", null))
                .ARAP(ARAP.AR)
                .grandTotal(extractedData.getOrDefault("합계금액", null))
                .chargeTotal(extractedData.getOrDefault("공급가액", null))
                .taxTotal(extractedData.getOrDefault("세액", null))
                .build();

        return ntsTax;
    }
}