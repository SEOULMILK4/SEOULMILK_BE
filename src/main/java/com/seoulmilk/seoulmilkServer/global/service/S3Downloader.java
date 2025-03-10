package com.seoulmilk.seoulmilkServer.global.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import java.io.IOException;
import java.net.URLEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class S3Downloader {

    private final AmazonS3 amazonS3;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${spring.cloud.aws.s3.url}")
    private String bucketUrl;

    public ResponseEntity<byte[]> downloadImage(String fileUrl) {
        try {
            String objectKey = extractS3ObjectKey(fileUrl); // S3 키만 추출

            S3Object s3Object = amazonS3.getObject(new GetObjectRequest(bucket, objectKey));
            S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
            byte[] bytes = IOUtils.toByteArray(objectInputStream);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(contentType(fileUrl));
            httpHeaders.setContentLength(bytes.length);

            String[] arr = fileUrl.split("/");
            String type = arr[arr.length - 1];
            String fileName = URLEncoder.encode(type, "UTF-8").replaceAll("\\+", "%20");
            httpHeaders.setContentDispositionFormData("attachment", fileName);

            return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(("파일 다운로드 중 오류 발생: " + e.getMessage()).getBytes());
        }
    }


    private MediaType contentType(String keyname) {
        String[] arr = keyname.split("\\.");
        String type = arr[arr.length - 1];
        switch (type) {
            case "txt":
                return MediaType.TEXT_PLAIN;
            case "png":
                return MediaType.IMAGE_PNG;
            case "jpg":
                return MediaType.IMAGE_JPEG;
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }


    public String extractS3ObjectKey(String fileUrl) {
        return fileUrl.replace(bucketUrl,"");
    }

}
