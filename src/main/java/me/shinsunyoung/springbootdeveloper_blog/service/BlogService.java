package me.shinsunyoung.springbootdeveloper_blog.service;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper_blog.domain.Article;
import me.shinsunyoung.springbootdeveloper_blog.dto.AddArticleRequest;
import me.shinsunyoung.springbootdeveloper_blog.dto.UpdateArticleRequest;
import me.shinsunyoung.springbootdeveloper_blog.repository.BlogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BlogService {

    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }

    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    public Article findById(long id) {
        return blogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }

    public void delete(long id) {
//        blogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Article with id " + id + "does not exist."));
        blogRepository.deleteById(id);
    }

    @Transactional
    public Article update(Long id, UpdateArticleRequest updateRequest) {

        Article article = blogRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("not found: " + id));

        article.update(updateRequest.getTitle(), updateRequest.getContent());

        return article;
    }
}
