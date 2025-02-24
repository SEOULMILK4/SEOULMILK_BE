package com.seoulmilk.seoulmilkServer.domain.auth.domain;

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
    private String employeeNum;

    private String otpCode;

    private boolean verified = false;

    @TimeToLive
    private Long ttl = 300L;

    public AuthVerifiedMember(String employeeNum, String otpCode) {
        this.employeeNum = employeeNum;
        this.otpCode = otpCode;
    }

    public void updateVerified() {
        this.verified = true;
    }
}
