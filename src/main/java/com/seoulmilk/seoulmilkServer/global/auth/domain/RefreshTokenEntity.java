package com.seoulmilk.seoulmilkServer.global.auth.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@NoArgsConstructor
@RedisHash(value = "refreshToken")
public class RefreshTokenEntity {

    @Id
    private String userId;

    private String refreshToken;

    @TimeToLive
    private Long ttl = 604800L;

    public RefreshTokenEntity(String userId, String role, String refreshToken) {
        this.userId = userId+":"+role;
        this.refreshToken = refreshToken;
    }


}
