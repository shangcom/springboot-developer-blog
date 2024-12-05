package me.shinsunyoung.springbootdeveloper_blog.service;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper_blog.domain.User;
import me.shinsunyoung.springbootdeveloper_blog.dto.AddUserRequest;
import me.shinsunyoung.springbootdeveloper_blog.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /*
    유저 정보 저장한 뒤 id 반환.
    bCryptPasswordEncoder.encode() 통해 패스워드 암호화.
     */
    public Long save(AddUserRequest dto) {
        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .build())
                .getId();
    }

}

