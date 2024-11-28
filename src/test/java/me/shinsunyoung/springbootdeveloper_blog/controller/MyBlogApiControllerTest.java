package me.shinsunyoung.springbootdeveloper_blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.shinsunyoung.springbootdeveloper_blog.domain.Article;
import me.shinsunyoung.springbootdeveloper_blog.dto.AddArticleRequest;
import me.shinsunyoung.springbootdeveloper_blog.repository.BlogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class MyBlogApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

//    @Autowired
//    private WebApplicationContext webApplicationContext; // 없어도 됨.

    @Autowired
    BlogRepository blogRepository;

    @BeforeEach
    public void setMockMvc() {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build(); // 없어도 됨.
        blogRepository.deleteAll();
    }

    @DisplayName("addArticle: 블로그 글 추가에 성공한다.")
    @Test
    public void addArticle() throws Exception {
        final String url = "/api/articles";
        AddArticleRequest request = new AddArticleRequest("title", "test");
        ResultActions result = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)));

        List<Article> articles = blogRepository.findAll();
        result.andExpect(status().is(201));
        assertThat(articles.get(0).getTitle()).isEqualTo("title");
    }

    @DisplayName("findArticles: 블로그 글 목록 조회에 성공한다.")
    @Test
    public void findArticles() throws Exception {
        final String url = "/api/articles";

        Article article1 = Article.builder().title("title 1").content("content 1").build();
        Article article2 = Article.builder().title("title 2").content("content 2").build();
        Article article3 = Article.builder().title("title 3").content("content 3").build();


        blogRepository.save(article1);
        blogRepository.save(article2);
        blogRepository.save(article3);

        ResultActions result = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].title").value("title 1"))
                .andExpect(jsonPath("$[0].content").value("content 1"))
                .andExpect(jsonPath("$[1].content").value("content 2"));
    }

    @DisplayName("findArticle: 블로그 글 조회에 성공한다.")
    @Test
    public void findArticle() throws Exception {
        final String url = "/api/articles/{id}";
        Article savedArticle = Article.builder().title("title 1").content("content 1").build();
        blogRepository.save(savedArticle);

        ResultActions result = mockMvc.perform(get(url, savedArticle.getId()));
        result.andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("title 1"))
        .andExpect(jsonPath("$.content").value("content 1"));
    }

    @DisplayName("deleteArticle: 블로그 글 삭제에 성공한다.")
    @Test
    public void deleteArticle() throws Exception {
        final String url = "/api/articles/{id}";
        ResultActions result = mockMvc.perform(delete(url, 1L).contentType(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        assertThat(blogRepository.findById(1L)).isEmpty();
    }

}