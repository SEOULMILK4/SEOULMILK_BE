package com.seoulmilk.seoulmilkServer.domain.auth.domain;

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

    private String role;

    @TimeToLive
    private Long ttl = 604800L;

    public RefreshTokenEntity(String userId, String refreshToken, String role) {
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.role = role;
    }


}
