package com.example.misson2.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

// JWT 자체와 관련된 기능을 만드는 곳
@Slf4j
@Component
public class JwtTokenUtils {
    // JWT를 만드는 용도의 암호키
    private final Key signingKey;
    // JWT를 해석하는 용도의 객체
    private final JwtParser jwtParser;

    public JwtTokenUtils(
            @Value("${jwt.secret}")
            String jwtSecret
    ) {
        this.signingKey
                = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtParser = Jwts
                .parserBuilder()
                .setSigningKey(this.signingKey)
                .build();
    }

    // UserDetails를 받아서 JWT로 변환하는 메서드
    public String generateToken(UserDetails userDetails) {
        // JWT에 담고싶은 추가 정보를 여기에 추가한다.

        // 현재 호출되었을 때 epoch time
        Instant now = Instant.now();
        Claims jwtClaims = Jwts.claims()
                // sub: 누구인지
                .setSubject(userDetails.getUsername())
                // iat: 언제 발급 되었는지
                .setIssuedAt(Date.from(now))
                // exp: 언제 만료 예정인지
                .setExpiration(Date.from(now.plusSeconds(60 * 30)));

        // 최종적으로 JWT를 발급한다.
        // JWT 빌더
        return Jwts.builder()
                .setClaims(jwtClaims)
                .signWith(signingKey)
                // build()의 역할
                .compact();
    }

    // 정삭적인 JWT인지 판단하는 메서드
    public boolean validate(String token) {
        try {
            // 정삭적이지 않은 JWT라면 예외(Exception)가 발생한다.
            jwtParser.parseClaimsJws(token).getBody();
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("malformed jwt");
        } catch (ExpiredJwtException e) {
            log.warn("expired jwt presented");
        } catch (UnsupportedJwtException e) {
            log.warn("unsupported jwt");
        } catch (IllegalArgumentException e) {
            log.warn("illegal argument");
        }
        return false;
    }

    // 실제 데이터를(Payload)를 반환하는 메소드
    public Claims parseClaims(String token) {
        return jwtParser
                .parseClaimsJws(token)
                .getBody();
    }
}
