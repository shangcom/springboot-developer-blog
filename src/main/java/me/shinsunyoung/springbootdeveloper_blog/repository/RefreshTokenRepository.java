package me.shinsunyoung.springbootdeveloper_blog.repository;

import me.shinsunyoung.springbootdeveloper_blog.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// JpaRepository 상속받았으므로 @Repository 붙이지 않아도 Spring Data JPA가 자동으로 스프링 빈으로 등록.
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserId(Long userId);

    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
