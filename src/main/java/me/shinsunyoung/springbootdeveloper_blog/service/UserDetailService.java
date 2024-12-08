package me.shinsunyoung.springbootdeveloper_blog.service;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper_blog.domain.User;
import me.shinsunyoung.springbootdeveloper_blog.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
JPA, MyBatis 등을 통해 데이터베이스에서 사용자 정보를 인증할 때 UserDetailsService 구현해서 사용.
사용자 이름이 기본값.
사용자 이름 대신 이메일, 정화번호 등을 통해 인증해야하는 경우 커스텀이 필요함으로 직접 구현해야 한다.
메모리 인증, LDAP, OAuth2 등 외부 인증 방식에서는 UserDetailsService 필요 없음.
AuthenticationManager에서 검증을 위해 사용자 정보를 조회할 때 loadUserByUsername() 메서드가 사용됨.
 */
@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException(email));
        return user;
    }
}
