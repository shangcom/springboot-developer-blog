package me.shinsunyoung.springbootdeveloper_blog.repository;

import me.shinsunyoung.springbootdeveloper_blog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /*
    Spring Data JPA는 메서드 이름을 분석하여 자동으로 쿼리를 생성.
    findByEmail의 경우, email이라는 필드명을 기준으로 SELECT 쿼리가 생성.
    SELECT * FROM user WHERE email = ?;
     */
    Optional<User> findByEmail(String email);
}
