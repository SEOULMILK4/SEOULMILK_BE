package com.seoulmilk.seoulmilkServer.global.mail.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class OTPService {

    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> otpExpiry = new ConcurrentHashMap<>();
    private static final int EXPIRY_TIME_MINUTES = 5;

    // OTP 생성 및 저장
    public String generateOtp(String userId) {

        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        otpStorage.put(userId, otp);
        otpExpiry.put(userId, LocalDateTime.now().plusMinutes(EXPIRY_TIME_MINUTES));
        return otp;
    }

    // OTP 검증
    public boolean validateOtp(String userId, String otp) {

        if (!otpStorage.containsKey(userId) || otpExpiry.get(userId)
            .isBefore(LocalDateTime.now())) {
            return false;
        }
        boolean isValid = otpStorage.get(userId).equals(otp);
        if (isValid) {
            otpStorage.remove(userId);
            otpExpiry.remove(userId);
        }
        return isValid;
    }
}

