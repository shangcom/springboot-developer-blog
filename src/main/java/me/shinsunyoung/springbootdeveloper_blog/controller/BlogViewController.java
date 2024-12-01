package me.shinsunyoung.springbootdeveloper_blog.controller;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper_blog.domain.Article;
import me.shinsunyoung.springbootdeveloper_blog.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BlogViewController {

    private final BlogService blogService;

    @GetMapping("/articles")
    public String getArticles(Model model) {
        List<Article> articles = blogService.findAll();
        model.addAttribute("articles", model);
        return "articles";
    }
}
