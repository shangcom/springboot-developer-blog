package me.shinsunyoung.springbootdeveloper_blog.config.jwt;

import io.jsonwebtoken.Jwts;
import me.shinsunyoung.springbootdeveloper_blog.domain.User;
import me.shinsunyoung.springbootdeveloper_blog.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/*
TokenProvider 클래스의 주요 메서드(generateToken, validToken, getAuthentication, getUserId)가 정상적으로 동작하는지 테스트.
테스트는 JWT 라이브러리(JJWT)를 사용해 토큰을 생성, 파싱, 검증하며 결과를 비교.
 */
@SpringBootTest
public class TokenProviderTest {

    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 사용자 정보를 기반으로 JWT를 생성할 수 있는지 확인.
     * 생성된 토큰에 사용자 ID가 올바르게 포함되어 있는지 검증.
     */
    @DisplayName("generateToken() : 유저 정보와 만료 기간을 전달해 토큰을 만들 수 있다.")
    @Test
    void generateToken() {
        //given : 토큰에 유저 정보를 추가하기 위한 테스트 유저 생성
        User testUser = userRepository.save(User.builder().email("user@gmail.com").password("test").build());

        //when : tokeProvider의 generateToken() 메서드를 호출해 토근 생성.
        String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));

        /*
        then : jjwt 라이브러리를 사용해 토큰 복호화.
        토큰 만들 때 클레임으로 넣어둔 id값이 given절에서 만든 유저 ID와 동일한지 확인.
         */
        Long userId = Jwts.parser().setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);

        assertThat(userId).isEqualTo(testUser.getId());
    }

    /**
     * 만료된 토큰에 대해 유효성 검증이 실패하는지 확인.
     */
    @DisplayName("validToken(): 만료된 토큰일 경우 유효성 검증에 실패한다.")
    @Test
    void validToken_invalidToken() {
        /*
        given : jjwt 라이브러리를 사용해 토큰 생성. 시간 설정 과거로 해서 이미 만료된 토큰 생성.
         */
        String token = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                .build().createToken(jwtProperties);

        //when: validToken 메서드를 호출하여 토큰 유효성 검사.
        boolean result = tokenProvider.validToken(token);

        //then: 반환값이 false(유효한 토큰이 아님)인 것을 확인.
        assertThat(result).isFalse();
    }

    /**
     * 토큰에서 인증 객체(Authentication)를 반환받을 수 있는지 확인.
     * 반환된 인증 객체가 예상한 사용자 정보(이메일)를 포함하는지 확인.
     */
    @DisplayName("getAuthentication(): 토큰 기반으로 인증 정보를 가져올 수 있다.")
    @Test
    void getAuthentication() {
        /*
        given: JwtFactory를 사용해 subject를 userEmail로 설정한 JWT 생성.
         */
        String userEmail = "user@gmail.com";
        String token = JwtFactory.builder()
                .subject(userEmail)
                .build()
                .createToken(jwtProperties);

        //when: tokenProvider의 getAutentication()으로 인증 객체 반환받음.
        Authentication autentication = tokenProvider.getAutentication(token);

        //then: 반환된 인증 객체의 사용자 이름이 토큰의 subject 값(userEmail)과 동일한지 확인.
        assertThat(((UserDetails) autentication.getPrincipal()).getUsername()).isEqualTo(userEmail);
    }

    /**
     * JWT의 클레임에 포함된 사용자 ID를 올바르게 추출할 수 있는지 확인.
     */
    @DisplayName("getUserId(): 토큰으로 유저 ID를 가져올 수 있다.")
    @Test
    void getUserId() {
        /*
        given: JwtFactory를 사용해 클레임("id")에 사용자 ID(1L)를 포함한 JWT 생성.
         */
        Long userId = 1L;
        String token = JwtFactory.builder()
                .claims(Map.of("id", userId))
                .build().createToken(jwtProperties);

        //when: tokenProvider의 getUserId() 메서드로 유저 ID 반환받음.
        Long userIdByToken = tokenProvider.getUserId(token);

        //then: 반환된 ID가 설정된 사용자 ID(1L)와 같은지 확인.
        assertThat(userIdByToken).isEqualTo(userId);

    }
}
