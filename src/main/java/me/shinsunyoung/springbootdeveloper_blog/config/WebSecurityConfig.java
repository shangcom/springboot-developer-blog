package me.shinsunyoung.springbootdeveloper_blog.config;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper_blog.service.UserDetailService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailService userDetailService;

    /*
    스프링 시큐리티 기능 비활성화
    스프링 시큐리티 필터에서 H2 콘솔과 static 경로 요청을 무시.
    즉, 위 요청들은 필터에서 제외한다.
     */
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console())
                .requestMatchers(new AntPathRequestMatcher("/static/**"));
    }

    /*
    특정 HTTP 요청에 대한 웹 기반 보안 구성.
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
                // CSRF 보호를 비활성화.
                .csrf(AbstractHttpConfigurer::disable)
                // 설정된 HttpSecurity를 기반으로 SecurityFilterChain 객체를 생성하여 반환.
                .build();
    }

}
