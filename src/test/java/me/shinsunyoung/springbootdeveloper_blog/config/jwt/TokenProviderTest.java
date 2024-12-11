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

@SpringBootTest
public class TokenProviderTest {

    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProperties jwtProperties;

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

    @DisplayName("validToken(): 만료된 토큰일 경우 유효성 검증에 실패한다.")
    @Test
    void validToken_invalidToken() {
        /*
        given : jjwt 라이브러리를 사용해 토큰 생성. 시간 설정 과거로 해서 이미 만료된 토큰 생성.
         */
        String token = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                .build().createToken(jwtProperties);

        //when: 토큰 제공자의 validToken() 메서드를 호출해 유효한 토큰인지 검증한 뒤 결괏값을 반환받음.
        boolean result = tokenProvider.validToken(token);

        //then: 반환값이 false(유효한 토큰이 아님)인 것을 확인.
        assertThat(result).isFalse();
    }

    @DisplayName("getAuthentication(): 토큰 기반으로 인증 정보를 가져올 수 있다.")
    @Test
    void getAuthentication() {
        /*
        given: jjwt 라이버리 사용해 토큰 생성. 토큰 제목(subject)는 userEmail으로 등록.
         */
        String userEmail = "user@gmail.com";
        String token = JwtFactory.builder()
                .subject(userEmail)
                .build()
                .createToken(jwtProperties);

        //when: tokenProvider의 getAutentication()으로 인증 객체 반환받음.
        Authentication autentication = tokenProvider.getAutentication(token);

        //then: 반환받은 인증 객체의 유저 이름을 가져와 given절에서 설정한 subject 값인 "user@gmail.com"과 같은지 확인.
        assertThat(((UserDetails) autentication.getPrincipal()).getUsername()).isEqualTo(userEmail);
    }

    @DisplayName("getUserId(): 토큰으로 유저 ID를 가져올 수 있다.")
    @Test
    void getUserId() {
        /*
        given: jjwt 라이브러리 사용해 토큰 생성.
               + 클레임 추가 : key = "id", value = userId (값 1L)
         */
        Long userId = 1L;
        String token = JwtFactory.builder()
                .claims(Map.of("id", userId))
                .build().createToken(jwtProperties);

        //when: tokenProvider의 getUserId() 메서드로 유저 ID 반환받음.
        Long userIdByToken = tokenProvider.getUserId(token);

        //then: 반환받은 유저ID가 given절에서 설정한 userId와 일치하는지 확인.
        assertThat(userIdByToken).isEqualTo(userId);

    }
}
