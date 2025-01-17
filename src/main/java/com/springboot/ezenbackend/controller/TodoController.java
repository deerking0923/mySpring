package com.springboot.ezenbackend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.ezenbackend.domain.Todo;
import com.springboot.ezenbackend.dto.PageRequestDTO;
import com.springboot.ezenbackend.dto.PageResponseDTO;
import com.springboot.ezenbackend.dto.TodoDto;
import com.springboot.ezenbackend.service.TodoService;

import lombok.RequiredArgsConstructor;

import org.hibernate.query.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todo")
public class TodoController {

    private final TodoService todoService;

    @GetMapping("/{tno}")
    public TodoDto get(@PathVariable("tno") Long tno) {
        return todoService.get(tno);
    }

    @GetMapping("/list")
    public PageResponseDTO<TodoDto> list(PageRequestDTO pageRequestDTO) {
        return todoService.list(pageRequestDTO);
    }

}
