package com.springboot.ezenbackend.service;

import java.lang.StackWalker.Option;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.springboot.ezenbackend.domain.Todo;
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

    public Long registerID(TodoDto todoDto) {
        Todo todo = modelMapper.map(todoDto, Todo.class);
        Todo savedToto = todoRepository.save(todo);
        return savedToto.getTno();
    }

    public Todo register(TodoDto todoDto) {
        Todo todo = modelMapper.map(todoDto, Todo.class);
        todoRepository.save(todo);
        return todo;
    }
}
