package com.springboot.ezenbackend.service;

import java.lang.StackWalker.Option;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springboot.ezenbackend.domain.Todo;
import com.springboot.ezenbackend.dto.PageRequestDTO;
import com.springboot.ezenbackend.dto.PageResponseDTO;
import com.springboot.ezenbackend.dto.TodoDto;
import com.springboot.ezenbackend.repository.TodoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;

    private final ModelMapper modelMapper;

    public TodoDto get(Long tno) {
        Optional<Todo> result = todoRepository.findById(tno);
        Todo todo = result.orElseThrow();
        TodoDto dto = modelMapper.map(todo, TodoDto.class);
        return dto;
    }

    public Long register(TodoDto todoDto) {
        Todo todo = modelMapper.map(todoDto, Todo.class);
        Todo savedToto = todoRepository.save(todo);
        return savedToto.getTno();
    }

    public Todo getTodo(TodoDto todoDto) {
        Todo todo = modelMapper.map(todoDto, Todo.class);
        todoRepository.save(todo);
        return todo;
    }

    public void modify(TodoDto todoDto) {
        Optional<Todo> result = todoRepository.findById(todoDto.getTno());
        Todo todo = result.orElseThrow();
        todo.setTitle(todoDto.getTitle());
        todo.setWriter(todoDto.getWriter());
        todo.setComplete(todoDto.isComplete());
        todoRepository.save(todo);
    }

    public void remove(Long tno) {
        todoRepository.deleteById(tno);
    }

    public PageResponseDTO<TodoDto> list(PageRequestDTO pageRequestDTO) {
        // PageRequest 객체 생성
        // 페이지 번호는 0부터 시작하므로 pageRequestDTO.getPage()에서 -1을 해줌
        // 정렬 기준은 "tno" 컬럼을 내림차순(descending)으로 설정
        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("tno").descending());

        // Repository에서 페이징된 결과를 가져옴 (Page<Todo> 반환)
        Page<Todo> result = todoRepository.findAll(pageable);

        // Page<Todo>에서 실제 데이터 리스트(List<Todo>)를 가져온 후,
        // 각 Todo 객체를 TodoDto로 변환 (ModelMapper 사용)
        List<TodoDto> dtoList = result.getContent().stream()
                .map(todo -> modelMapper.map(todo, TodoDto.class)) // Todo -> TodoDto 변환
                .collect(Collectors.toList()); // 변환된 객체를 리스트로 수집

        // 총 데이터 개수를 가져옴
        long totalCount = result.getTotalElements();

        // PageResponseDTO를 빌더 패턴으로 생성
        PageResponseDTO<TodoDto> responseDTO = PageResponseDTO.<TodoDto>withAll()
                .dtoList(dtoList) // 변환된 TodoDto 리스트 설정
                .pageRequestDTO(pageRequestDTO) // 요청 DTO 설정
                .totalCount(totalCount) // 총 데이터 개수 설정
                .build();

        // 생성된 PageResponseDTO 반환
        return responseDTO;
    }

}
