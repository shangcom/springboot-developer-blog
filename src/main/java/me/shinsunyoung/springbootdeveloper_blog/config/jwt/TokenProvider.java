package me.shinsunyoung.springbootdeveloper_blog.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper_blog.domain.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final JwtProperties jwtProperties;

    /**
     * JWT 생성 메서드
     *
     * @param user      유저 객체. 사용자 정보를 얻기 위해 필요. email, id 사용됨.
     * @param expiredAt 토큰의 수명 지정. ex) 1시간짜리 토큰 : Duration.ofHours(1)을 인자로 전달.
     * @return makeToken 메서드를 통해 생성된 String을 반환
     * 이 메서드는 User 정보와 토큰 수명을 지정하여 makeToken를 호출하고 그 결과를 반환함.
     */
    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    /**
     * JWT의 세부 정보를 구성하고 서명(Signature)을 추가하여 최종 토큰(JWT 문자열)을 생성.
     * generateToken 메서드의 내부 동작으로 사용됨.     * @param expiry 토큰 수명
     *
     * @param user 유저 객체
     * @return Jwts.builder() 통해 생성된 jwt를 반환.
     */
    private String makeToken(Date expiry, User user) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // JWT의 타입(JWT) 지정
                .setIssuer(jwtProperties.getIssuer()) // 발급자 정보
                .setIssuedAt(now) // 발급 시간
                .setExpiration(expiry) // 만료 시간
                .setSubject(user.getEmail()) // 사용자의 식별자
                .claim("id", user.getId()) // 추가 클레임
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey()) // 서명
                .compact(); // 최종적으로 JWT 문자열 생성
    }

    /*
     *전달된 토큰이 유효한지 검증
     * 토큰의 유효성을 확인하지 않으면, 만료되었거나 변조된 토큰이 허용될 위험.
     * 토큰을 파싱하고 비밀키를 사용해 서명 검증
     */
    public boolean validToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token);
            return true; // 유효하면 true 반환
        } catch (Exception e) {
            return false; // 예외 발생 시 false 반환
        }
    }

    /**
     * JWT를 파싱하여 사용자의 인증 정보를 추출.
     * 스프링 시큐리티의 Authentication 객체를 반환하여 인증 상태를 설정.
     * @param token
     * @return
     */
    public Authentication getAutentication(String token) {
        Claims claims = getClaims(token); // JWT의 페이로드 추출
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")); // 사용자 권한 설정

        return new UsernamePasswordAuthenticationToken(
                new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities),// 사용자 정보
                token, authorities);// 인증 토큰 반환
    }

    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
}
