package me.shinsunyoung.springbootdeveloper_blog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    public User(String email, String password, String auth) {
        this.email = email;
        this.password = password;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    // 사용자의 id를 반환(고유한 값)
    @Override
    public String getUsername() {
        return email;
    }

    // 사용자의 패스워드 반환
    @Override
    public String getPassword() {
        return password;
    }

    // 계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        /*
        만료됐는지 확인하는 로직 추가
         */
        return true; // 만료 안됨.
    }

    // 계정 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        /*
        계정 잠금 확인 로직 추가
         */
        return true; // 안잠김
    }

    // 패스워드 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        /*
        패스워드 만료 확인 로직 추가
         */
        return true; //만료 안됨
    }

    // 계정 사용 가능 여부 반환
    @Override
    public boolean isEnabled() {
        /*
        계정 사용 가능 여부 확인 로직 추가
         */
        return true; // 사용 가능
    }

}