package com.seoulmilk.seoulmilkServer.global.auth.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@NoArgsConstructor
@RedisHash(value = "verifiedMember")
public class AuthVerifiedMember {

    @Id
    private String userId;

    private String otpCode;

    private boolean verified = false;

    @TimeToLive
    private Long ttl = 300L;

    public AuthVerifiedMember(String userId, String otpCode) {
        this.userId = userId;
        this.otpCode = otpCode;
    }

    public void updateVerified() {
        this.verified = true;
    }
}
