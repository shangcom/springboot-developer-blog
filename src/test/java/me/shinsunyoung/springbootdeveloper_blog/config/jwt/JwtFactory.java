package me.shinsunyoung.springbootdeveloper_blog.config.jwt;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static java.util.Collections.emptyMap;

/*
JWT(JSON Web Token)를 생성하기 위한 테스트용 유틸리티 클래스
이 클래스는 JWT 생성의 기본값을 제공하고, 필요에 따라 커스터마이징된 토큰을 생성
 */
@Getter
public class JwtFactory {

    private String subject = "test@email.com"; // JWT의 소유자(주제) 정보
    private Date issuedAt = new Date(); //  JWT 발급 시간. 현재 시간을 기본값으로 설정.
    private Date expiration = new Date(new Date().getTime() + Duration.ofDays(14).toMillis()); // JWT 만료 시간. 현재 시간에서 14일 뒤로 기본 설정.
    private Map<String, Object> claims = emptyMap(); // 추가 데이터(클레임). 기본값으로 빈 맵(emptyMap()) 설정.

    /*
     빌더 패턴을 사용하여 필드를 유연하게 초기화.
     값이 null인 경우 기본값을 유지하도록 설계되어, 선택적으로 필요한 값만 설정할 수 있음
     */
    @Builder
    public JwtFactory(String subject, Date issuedAt, Date expiration, Map<String, Object> claims) {
        this.subject = subject != null ? subject : this.subject;
        this.issuedAt = issuedAt != null ? issuedAt : this.issuedAt;
        this.expiration = expiration != null ? expiration : this.expiration;
        this.claims = claims != null ? claims : this.claims;
    }

    /*
    기본값 팩토리 메서드.
    기본값(subject, issuedAt, expiration, claims)이 설정된 JwtFactory 인스턴스를 생성.
     */
    public static JwtFactory withDefaultValues() {
        return JwtFactory.builder().build();
    }

    /*
    JwtProperties로부터 서명 키(secretKey)와 발급자(issuer) 정보를 받아 JWT를 생성.
    설정된 필드(subject, issuedAt, expiration, claims)를 JWT에 반영.
    io.jsonwebtoken 라이브러리를 사용해 HMAC-SHA256 알고리즘으로 JWT를 서명.
     */
    public String createToken(JwtProperties jwtProperties) {
        return Jwts.builder()
                .setSubject(subject)
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setExpiration(expiration)
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }
}
