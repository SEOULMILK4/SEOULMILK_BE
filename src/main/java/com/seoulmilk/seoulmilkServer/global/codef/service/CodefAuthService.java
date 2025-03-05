package com.seoulmilk.seoulmilkServer.global.codef.service;

import com.seoulmilk.seoulmilkServer.global.codef.domain.CodefAccessToken;
import com.seoulmilk.seoulmilkServer.global.codef.properties.CodefProperties;
import com.seoulmilk.seoulmilkServer.global.codef.repository.CodefTokenRepository;
import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import io.codef.api.EasyCodef;
import io.codef.api.EasyCodefServiceType;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CodefAuthService {

    private final CodefProperties codefProperties;
    private final CodefTokenRepository codefTokenRepository;

    public String getCodefToken() {

        Optional<CodefAccessToken> existingToken = codefTokenRepository.findById(1L);
        if (existingToken.isPresent()) {
            return existingToken.get().getAccessToken();
        }

        EasyCodef codef = new EasyCodef();
        codef.setClientInfoForDemo(codefProperties.getClientId(),
            codefProperties.getClientSecret());
        codef.setPublicKey(codefProperties.getPublicKey());

        try {
            String accessToken = codef.requestToken(EasyCodefServiceType.DEMO);
            CodefAccessToken tokenEntity = new CodefAccessToken(accessToken);
            codefTokenRepository.save(tokenEntity);
            return accessToken;

        } catch (IOException e) {
            throw new BusinessException(ErrorCode.CREATE_ACCESSTOKEN_CODEF_FAILED);
        }
    }


}

