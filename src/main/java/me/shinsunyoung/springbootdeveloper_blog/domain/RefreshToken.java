package me.shinsunyoung.springbootdeveloper_blog.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
리프레시 토큰 데이터를 저장하는 JPA 엔티티.
사용자의 ID와 리프레시 토큰을 저장하며, 사용자당 하나의 리프레시 토큰을 관리.
 */
@NoArgsConstructor
@Getter
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    /*
    리프레시 토큰을 소유한 사용자의 ID.
    unique = true**로 설정되어 사용자당 하나의 리프레시 토큰만 허용.
     */
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    // 새로운 리프레시 토큰 엔티티를 생성.
    public RefreshToken(Long userId, String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
    }

    // 리프레시 토큰 값을 갱신하고, 업데이트된 엔티티를 반환.
    public RefreshToken update(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
        return this;
    }
}
