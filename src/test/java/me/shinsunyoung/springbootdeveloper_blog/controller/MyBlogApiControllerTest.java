package me.shinsunyoung.springbootdeveloper_blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.shinsunyoung.springbootdeveloper_blog.domain.Article;
import me.shinsunyoung.springbootdeveloper_blog.dto.AddArticleRequest;
import me.shinsunyoung.springbootdeveloper_blog.dto.UpdateArticleRequest;
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
        final String title = "title";
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);
        final String requestBody = objectMapper.writeValueAsString(userRequest);

        ResultActions result = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(requestBody));
        result.andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();
        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);

    }



    @DisplayName("findArticles: 블로그 글 목록 조회에 성공한다.")
    @Test
    public void findArticles() throws Exception {
        final String url = "/api/articles";
        blogRepository.save(Article.builder().title("title 1").content("content 1").build());
        blogRepository.save(Article.builder().title("title 2").content("content 2").build());
        blogRepository.save(Article.builder().title("title 3").content("content 3").build());

        ResultActions result = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].title").value("title 1"))
                .andExpect(jsonPath("$[0].content").value("content 1"));


    }

    @DisplayName("findArticle: 블로그 글 조회에 성공한다.")
    @Test
    public void findArticle() throws Exception {
        final String url = "/api/articles/{id}";
        Article savedId = blogRepository.save(Article.builder().title("title 1").content("content 1").build());

        ResultActions result = mockMvc.perform(get(url, savedId.getId()));
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("title 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("content 1"));
    }

    @DisplayName("deleteArticle: 블로그 글 삭제에 성공한다.")
    @Test
    public void deleteArticle() throws Exception {
        final String url = "/api/articles/{id}";
        Article savedArticle = blogRepository.save(Article.builder().title("title").content("content").build());


        mockMvc.perform(delete(url, savedArticle.getId()))
                .andExpect(status().isOk());
        assertThat(blogRepository.findAll()).isEmpty();
    }

    @DisplayName("updateArticle: 블로그 글 수정에 성공한다.")
    @Test
    public void updateArticle() throws Exception {

        final String url = "/api/articles/{id}";
        Article savedArticle = blogRepository.save(Article.builder().content("content 1").title("title 1").build());
        UpdateArticleRequest updateRequest = new UpdateArticleRequest("new title", "new content");

        ResultActions result = mockMvc.perform(put(url, savedArticle.getId()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateRequest)));


        result.andExpect(status().isOk());

        Article updatedArticle = blogRepository.findById(savedArticle.getId()).get();
        assertThat(updatedArticle.getTitle()).isEqualTo("new title");
        assertThat(updatedArticle.getContent()).isEqualTo("new content");

    }
}

