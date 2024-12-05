package me.shinsunyoung.springbootdeveloper_blog.controller;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper_blog.dto.AddUserRequest;
import me.shinsunyoung.springbootdeveloper_blog.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserApiController {

    private UserService userService;

    @PostMapping("/user")
    public String signUp(AddUserRequest request) {
        userService.save(request); // 회원 갑입 메서드 호출
        return "redirect:/login"; // 회원 가입이 완료된 이후에 로그인 페이지로 이동
    }

}
