package me.shinsunyoung.springbootdeveloper_blog.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties("jwt") // application.yml의 jwt 항목 아래에 있는 설정 값을 매핑
public class JwtProperties {
    // issuer와 secretKey는 jwt.issuer, jwt.secret_key의 값을 자동으로 주입받음.
    private String issuer;
    private String secretKey;
}
