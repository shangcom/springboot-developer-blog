package me.shinsunyoung.springbootdeveloper_blog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// JPA는 기본 생성자로 객체 자동 생성하기 때문에 꼭 필요함. 다른 곳에서 사용 못하게 protected로 막아둠.
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Article {

    /*
    JPA는 엔티티 객체 생성 후 DB로부터 주키 값을 할당하므로, final로 선언하면 안 된다.
     기본형 long을 사용하면 초기값이 0이 되고, JPA는 이를 신규 엔티티로 오인하거나, 주키 중복 오류가 발생할 가능성이 있음
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Builder
    public Article(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
