package com.seoulmilk.seoulmilkServer.global.codef.properties;

import com.seoulmilk.seoulmilkServer.global.codef.service.CodefAuthService;
import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "codef")
public class CodefProperties {

    private String publicKey;

    private String clientId;

    private String clientSecret;

    private String certDer;

    private String certKey;

    private String password;

    @PostConstruct
    public void encodeCertKey(){
        this.password= encryptCertPassword();
    }

    public String encryptCertPassword() {
        try {
            return encryptRSA(password,publicKey);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.ENCODING_PASSWORD_FAILED);
        }
    }

    public static String encryptRSA(String plainText, String publicKeyStr) throws Exception {

          byte[] publicBytes = Base64.getDecoder().decode(publicKeyStr);
          X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
          KeyFactory keyFactory = KeyFactory.getInstance("RSA");
          PublicKey publicKey = keyFactory.generatePublic(keySpec);

          Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
          cipher.init(Cipher.ENCRYPT_MODE, publicKey);

          byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

          return Base64.getEncoder().encodeToString(encryptedBytes);
      }


}