package me.shinsunyoung.springbootdeveloper_blog.controller;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper_blog.domain.Article;
import me.shinsunyoung.springbootdeveloper_blog.dto.AddArticleRequest;
import me.shinsunyoung.springbootdeveloper_blog.dto.ArticleResponse;
import me.shinsunyoung.springbootdeveloper_blog.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BlogApiController {

    private final BlogService blogService;


    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
        Article savedArticle = blogService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
//        List<Article> articles = blogService.findAll();
//        List<ArticleResponse> articleResponses = articles.stream().map(article -> new ArticleResponse(article)).toList();
//        ResponseEntity<List<ArticleResponse>> responseEntity = ResponseEntity.ok().body(articleResponses);
//        return responseEntity;
        return ResponseEntity.ok().body(blogService.findAll().stream().map(ArticleResponse::new).toList());
    }

    @GetMapping("/api/articles/{id}")
    public ResponseEntity<Article> findById(@PathVariable("id") Long id) {
        Article article = blogService.findById(id);

        return ResponseEntity.ok().body(article);
    }
}
