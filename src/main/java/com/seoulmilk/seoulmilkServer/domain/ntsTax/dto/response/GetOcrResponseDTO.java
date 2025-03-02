package com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetOcrResponseDTO {
    private String version;
    private String requestId;
    private long timestamp;
    private List<ImageResult> images;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageResult {
        private String uid;
        private String name;
        private String inferResult;
        private String message;
        private MatchedTemplate matchedTemplate;
        private List<Field> fields;
        private Title title;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MatchedTemplate {
        private int id;
        private String name;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Field {
        private String name;
        private String inferText;
        private double inferConfidence;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Title {
        private String name;
        private String inferText;
        private double inferConfidence;
    }
}
