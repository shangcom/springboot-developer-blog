package me.shinsunyoung.springbootdeveloper_blog.dto;

import lombok.Getter;
import me.shinsunyoung.springbootdeveloper_blog.domain.Article;

@Getter
public class ArticleResponse {

    private String title;
    private String content;

    public ArticleResponse(Article article) {
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}
