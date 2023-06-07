package com.example.memorip.repository;

import com.example.memorip.dto.SignUpDTO;
import com.example.memorip.dto.UserDTO;
import com.example.memorip.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    ArrayList<User> findAll();

    // @EntityGraph : 쿼리가 수행이 될때 Lazy조회가 아니고 Eager 조회로 authorities 정보를 같이 가져오게 된다.
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByEmail(String email);
}
