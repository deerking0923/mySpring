package com.springboot.ezenbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.springboot.ezenbackend.domain.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    // JpaRepository에서 기본 제공하는 메서드 외에 커스텀 메서드를 추가하려면 여기에 정의
}
