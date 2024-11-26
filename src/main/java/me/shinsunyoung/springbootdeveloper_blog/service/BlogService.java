package me.shinsunyoung.springbootdeveloper_blog.service;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper_blog.domain.Article;
import me.shinsunyoung.springbootdeveloper_blog.dto.AddArticleRequest;
import me.shinsunyoung.springbootdeveloper_blog.repository.BlogRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BlogService {

    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request) {

        return blogRepository.save(request.toEntity());

    }
}
