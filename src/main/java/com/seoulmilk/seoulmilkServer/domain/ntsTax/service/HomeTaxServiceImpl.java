package com.seoulmilk.seoulmilkServer.domain.ntsTax.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.OcrTaxInvoiceRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.OcrTaxInvoiceResponseDTO;
import com.seoulmilk.seoulmilkServer.global.codef.properties.CodefProperties;
import com.seoulmilk.seoulmilkServer.global.codef.service.CodefAuthService;
import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class HomeTaxServiceImpl implements HomeTaxService {

    private final CodefAuthService codefAuthService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    private final CodefProperties codefProperties;
    private static final String CODEF_API_URL = "https://development.codef.io/v1/kr/public/nt/third-party/tax-invoice-issue";


    public OcrTaxInvoiceResponseDTO verifyTaxInvoice(OcrTaxInvoiceRequestDTO request) {

        String accessToken = codefAuthService.getCodefToken();
        return processTaxInvoiceVerification(request, accessToken);
    }

    public List<OcrTaxInvoiceResponseDTO> verifyMultipleTaxInvoice(
        List<OcrTaxInvoiceRequestDTO> requests) {
        String accessToken = codefAuthService.getCodefToken();

        return requests.parallelStream()
            .map(request -> processTaxInvoiceVerification(request, accessToken))
            .collect(Collectors.toList());
    }

    public OcrTaxInvoiceResponseDTO processTaxInvoiceVerification(OcrTaxInvoiceRequestDTO request,
        String accessToken) {
        try {
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("organization", "0004");
            requestData.put("loginType", "0");
            requestData.put("certType", "1");
            requestData.put("certFile", codefProperties.getCertDer());
            requestData.put("keyFile", codefProperties.getCertKey());
            requestData.put("certPassword", codefProperties.getPassword());
            requestData.put("supplierRegNumber", request.getSupplierRegNumber());
            requestData.put("contractorRegNumber", request.getContractorRegNumber());
            requestData.put("approvalNo", request.getApprovalNo());
            requestData.put("reportingDate", request.getReportingDate());
            requestData.put("supplyValue", request.getSupplyValue());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestData, headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                CODEF_API_URL, HttpMethod.POST, requestEntity, String.class
            );

            String responseBody = responseEntity.getBody();
            responseBody = URLDecoder.decode(responseBody, StandardCharsets.UTF_8);

            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode resultNode = rootNode.get("result");

            String resultCode = resultNode.get("code").asText();
            String resultMessage = resultNode.get("message").asText();

            if (!"CF-00000".equals(resultCode)) {
                throw new RuntimeException(
                    "CODEF API 요청 실패 (에러 코드: " + resultCode + ", 메시지: " + resultMessage + ")");
            }

            JsonNode dataNode = rootNode.get("data");
            return OcrTaxInvoiceResponseDTO.of(
                dataNode.get("resAuthenticity").asText(),
                dataNode.get("resAuthenticityDesc").asText()
            );

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.VERIFY_TAX_INVOICE_FALIED);
        }
    }
}

