package com.springboot.ezenbackend;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.springboot.ezenbackend.domain.Todo;
import com.springboot.ezenbackend.repository.TodoRepository;

@SpringBootTest
class EzenbackendApplicationTests {

	@Test
	void contextLoads() {
		// for(int i = 1; i<100; i++){
		// Todo todo = Todo.builder().title("제목 :
		// "+i).writer("사용자").duDate(LocalDate.now()).build();
		// todoRepository.save(todo);
		// }

	}

}
