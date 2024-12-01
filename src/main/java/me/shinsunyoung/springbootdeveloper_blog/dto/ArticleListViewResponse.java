package me.shinsunyoung.springbootdeveloper_blog.dto;

import lombok.Getter;
import me.shinsunyoung.springbootdeveloper_blog.domain.Article;

@Getter
public class ArticleListViewResponse {

    private final Long id;
    private final String title;
    private final String content;

    public ArticleListViewResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}
