package com.seoulmilk.seoulmilkServer.domain.ntsTax.ocr;

import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class ClovaOcr {

    private final String clovaUrl;
    private final String secretKey;

    public ClovaOcr(@Value("${ocr.api.url}") String clovaUrl,
                    @Value("${ocr.api.secret-key}") String secretKey) {
        this.clovaUrl = clovaUrl;
        this.secretKey = secretKey;
    }

    public List<String> callApi(String type, MultipartFile file, String key, String ext) throws IOException {
        List<String> parseData;

        // HTTP 연결
        URL url = new URL(clovaUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setReadTimeout(5000);
        connection.setRequestMethod(type);

        String boundary = "----" + UUID.randomUUID().toString().replaceAll("-", "");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        connection.setRequestProperty("X-OCR-SECRET", secretKey);

        // JSON 메시지 생성
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("version", "V2");
        jsonObject.put("requestId", UUID.randomUUID().toString());
        jsonObject.put("timestamp", System.currentTimeMillis());

        JSONObject image = new JSONObject();
        image.put("format", ext);
        image.put("name", file.getOriginalFilename());

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(image);
        jsonObject.put("images", jsonArray);
        String postParams = jsonObject.toString();

        // MultipartFile 전송
        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            writeMultiPart(wr, postParams, file, boundary);
        }

        // 응답 처리
        int responseCode = connection.getResponseCode();
        BufferedReader br;

        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new BusinessException(ErrorCode.OCR_REQUEST_FAILED);
        } else {
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        }

        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = br.readLine()) != null) {
            response.append(inputLine);
        }
        br.close();

        parseData = parseOcrResponse(response.toString());

        return parseData;
    }

    private static void writeMultiPart(OutputStream out, String jsonMessage, MultipartFile file, String boundary) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition:form-data; name=\"message\"\r\n\r\n");
        sb.append(jsonMessage);
        sb.append("\r\n");

        out.write(sb.toString().getBytes("UTF-8"));
        out.flush();

        if (!file.isEmpty()) {
            out.write(("--" + boundary + "\r\n").getBytes("UTF-8"));
            StringBuilder fileString = new StringBuilder();
            fileString.append("Content-Disposition:form-data; name=\"file\"; filename=");
            fileString.append("\"" + file.getName() + "\"\r\n");
            fileString.append("Content-Type: application/octet-stream\r\n\r\n");
            out.write(fileString.toString().getBytes("UTF-8"));
            out.flush();

            try (InputStream fis = file.getInputStream()) {
                byte[] buffer = new byte[8192];
                int count;
                while ((count = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, count);
                }
                out.write("\r\n".getBytes());
            }

            out.write(("--" + boundary + "--\r\n").getBytes("UTF-8"));
        }
        out.flush();
    }

    // OCR 응답 -> JSON 파싱
    private List<String> parseOcrResponse(String jsonResponse) {
        List<String> extractedTexts = new ArrayList<>();

        try {
            JSONParser parser = new JSONParser();
            JSONObject responseJson = (JSONObject) parser.parse(jsonResponse);
            JSONArray imagesArray = (JSONArray) responseJson.get("images");

            if (imagesArray != null && !imagesArray.isEmpty()) {
                JSONObject imageResult = (JSONObject) imagesArray.get(0);
                JSONArray fieldsArray = (JSONArray) imageResult.get("fields");

                if (fieldsArray != null) {
                    for (Object fieldObj : fieldsArray) {
                        JSONObject field = (JSONObject) fieldObj;
                        String inferText = (String) field.get("inferText");
                        extractedTexts.add(inferText);
                    }
                }
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OCR_PARSE_FAILED);
        }

        return extractedTexts;
    }
}