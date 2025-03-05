package com.seoulmilk.seoulmilkServer.global.codef.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@NoArgsConstructor
@RedisHash(value = "codefAccessToken")
public class CodefAccessToken {

    @Id
    private Long id =1L;

    private String accessToken;

    @TimeToLive
    private Long ttl = 604800L; //1주일로 설정

    public CodefAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}