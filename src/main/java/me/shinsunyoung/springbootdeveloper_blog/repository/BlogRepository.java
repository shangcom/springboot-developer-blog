package me.shinsunyoung.springbootdeveloper_blog.repository;

import me.shinsunyoung.springbootdeveloper_blog.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

/*
@Repository 붙이지 않아도, JpaRepository를 상속받은 인터페이스는 Spring Data JPA가 자동으로 프록시 객체를 생성하고, Spring 컨테이너에 빈으로 등록
 */
//@Repository
public interface BlogRepository extends JpaRepository<Article, Long> {
}
