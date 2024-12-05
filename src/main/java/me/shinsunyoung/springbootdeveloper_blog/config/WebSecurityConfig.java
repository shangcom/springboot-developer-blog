package me.shinsunyoung.springbootdeveloper_blog.config;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper_blog.service.UserDetailService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailService userDetailService;

    /**
     * 스프링 시큐리티의 보안 필터 체인에서 특정 요청을 제외(ignoring)하는 설정을 정의.
     * 보안이 필요하지 않은 요청(예: 정적 리소스, H2 콘솔)에 대해 스프링 시큐리티 필터가 적용되지 않도록 한다.
     * @return WebSecurityCustomizer 객체로 보안 필터 체인에서 제외할 요청을 정의.
     */
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console())
                .requestMatchers(new AntPathRequestMatcher("/static/**"));
    }

    /*
    특정 HTTP 요청에 대한 웹 기반 보안 구성.
    SecurityFilterChain을 빈으로 정의하여 보안 필터 체인을 명시적으로 구성.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // 요청 url별로 접근 권한 정의
                .authorizeRequests(auth -> auth
                        // 특정 경로에 대한 요청 필터링
                        .requestMatchers(
                                new AntPathRequestMatcher("/login"),
                                new AntPathRequestMatcher("/signup"),
                                new AntPathRequestMatcher("/user"))
                        // 위 세 경로는 인증 없이 접근 가능.
                        .permitAll()
                        // 그 외 모든 요청은 인증 필요.
                        .anyRequest().authenticated())
                // form 기반 로그인 설정.
                .formLogin(formLogin -> formLogin
                        .loginPage("/login") // 커스텀 로그인 페이지 경로 지정.
                        .defaultSuccessUrl("/articles") // 로그인 성공 시 리다이렉트될 기본 경로.
                )
                // 로그아웃 설정.
                .logout(logout -> logout
                        .logoutSuccessUrl("/login") // 로그아웃 성공 후 이동할 경로 지정.
                        .invalidateHttpSession(true)) // 로그아웃 시 세션을 무효화하여 보안성을 강화.
                // CSRF 보호를 비활성화. 기본값이 활성화이므로 아래 코드 지우면 활성화됨.
                .csrf(AbstractHttpConfigurer::disable) // http.csrf().disable()과 같음.
                // 설정된 HttpSecurity를 기반으로 SecurityFilterChain 객체를 생성하여 반환.
                .build();
    }

    /**
     * 인증(Authentication)을 처리하는 AuthenticationManager 빈 생성.
     * - DaoAuthenticationProvider : 데이터베이스에서 사용자 정보를 가져오고, 비밀번호를 검증.
     * - BCryptPasswordEncoder : 암호화된 비밀번호를 비교.
     *
     * @param http                  HttpSecurity 객체, 시큐리티 설정과 연결. (아직 사용 안하고 있음)
     * @param bCryptPasswordEncoder 비밀번호 암호화를 위한 인코더 빈.
     * @param userDetailService     사용자 정보를 로드하는 서비스.
     * @return 인증 매니저(AuthenticationManager) 빈.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            UserDetailService userDetailService) {
        // 데이터베이스에서 사용자 정보와 비밀번호를 검증하는 Provider 생성
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService); // 사용자 정보 로드
        authProvider.setPasswordEncoder(bCryptPasswordEncoder); // 비밀번호 검증
        // ProviderManager로 DaoAuthenticationProvider를 관리하며 AuthenticationManager 반환
        return new ProviderManager(authProvider);
    }

    /**
     * 비밀번호를 암호화하거나 검증할 때 사용할 BCryptPasswordEncoder 빈 생성.
     * - 스프링 시큐리티에서 권장하는 비밀번호 암호화 방식 중 하나.
     * - 암호화된 비밀번호와 입력 비밀번호를 비교해 검증.
     *
     * @return BCryptPasswordEncoder 인스턴스.
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        // BCrypt 알고리즘을 사용하는 암호화 인코더 반환
        return new BCryptPasswordEncoder();
    }
}
