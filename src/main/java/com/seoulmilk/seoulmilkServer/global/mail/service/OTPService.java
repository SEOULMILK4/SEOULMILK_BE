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
    public String generateOtp(String employeeNum) {

        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        otpStorage.put(employeeNum, otp);
        otpExpiry.put(employeeNum, LocalDateTime.now().plusMinutes(EXPIRY_TIME_MINUTES));
        return otp;
    }

    // OTP 검증
    public boolean validateOtp(String employeeNum, String otp) {

        if (!otpStorage.containsKey(employeeNum) || otpExpiry.get(employeeNum)
            .isBefore(LocalDateTime.now())) {
            return false;
        }
        boolean isValid = otpStorage.get(employeeNum).equals(otp);
        if (isValid) {
            otpStorage.remove(employeeNum);
            otpExpiry.remove(employeeNum);
        }
        return isValid;
    }
}

