package com.seoulmilk.seoulmilkServer.global.jwt.provider;

import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import com.seoulmilk.seoulmilkServer.global.jwt.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtProvider {

    private final JwtProperties jwtDTO;

    public String generateAccessToken(Long id,String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtDTO.getAccessExp());

        return Jwts.builder()
            .setSubject(id.toString())
            .claim("role", role)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(Keys.hmacShaKeyFor(jwtDTO.getSecretKey().getBytes(StandardCharsets.UTF_8)),
                SignatureAlgorithm.HS256)
            .compact();

    }

    public String generateRefreshToken(Long id,String role) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtDTO.getRefreshExp());

        return Jwts.builder()
            .setSubject(id.toString())
            .claim("role", role)
            .setIssuedAt(new Date())
            .setExpiration(expiryDate)
            .signWith(Keys.hmacShaKeyFor(jwtDTO.getSecretKey().getBytes(StandardCharsets.UTF_8)),
                SignatureAlgorithm.HS256)
            .compact();

    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtDTO.getSecretKey().getBytes(
                    StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token);

            Date expiration = claimsJws.getBody().getExpiration();
            if (expiration != null && expiration.before(new Date())) {
                throw new BusinessException(ErrorCode.TOKEN_EXPIRED);
            }
            return true;

        } catch (JwtException | IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.TOKEN_INVALID);
        }
    }


    public String getUserIdFromToken(String token) {

        return Jwts.parserBuilder()
            .setSigningKey(
                Keys.hmacShaKeyFor(jwtDTO.getSecretKey().getBytes(StandardCharsets.UTF_8)))
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    public String getUserRoleFromToken(String token) {
        return (String) Jwts.parserBuilder()
            .setSigningKey(
                    Keys.hmacShaKeyFor(jwtDTO.getSecretKey().getBytes(StandardCharsets.UTF_8)))
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("role");
    }


    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtDTO.getSecretKey().getBytes())) // 서명 키 설정
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (JwtException e) {
            throw new RuntimeException("유효하지 않은 JWT 토큰입니다.");
        }
    }


}
