package me.shinsunyoung.springbootdeveloper_blog.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper_blog.dto.AddUserRequest;
import me.shinsunyoung.springbootdeveloper_blog.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserApiController {

    private final UserService userService;

    @PostMapping("/user")
    public String signUp(AddUserRequest request) {
        userService.save(request); // 회원 가입입 메서드 호출
        return "redirect:/login"; // 회원 가입이 완료된 이후에 로그인 페이지로 이동
    }

    /*
    동일한 요청 메서드와 경로(/logout)에 대해 컨트롤러와 필터 체인이 모두 처리하려고 할 경우, 우선순위가 높은 컨트롤러가 요청을 먼저 처리.
    여기서는 get, 체인에서는 post /logout 처리.
    로그아웃 버튼이 get 요청을 보내는 href를 사용하고 있으므로 이 컨트롤러가 처리한다.
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());

        return "redirect:/login";
    }

}
