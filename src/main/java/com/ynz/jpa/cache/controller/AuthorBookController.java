package com.ynz.jpa.cache.controller;

import com.ynz.jpa.cache.dto.AuthorBookDto;
import com.ynz.jpa.cache.entities.Author;
import com.ynz.jpa.cache.mapper.AuthorMapper;
import com.ynz.jpa.cache.service.AuthorBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/author")
@RequiredArgsConstructor
public class AuthorBookController {
    private final AuthorBookService authorBookService;

    @GetMapping("/{id}")
    ResponseEntity<AuthorBookDto> findAuthorBookById(@PathVariable("id") Integer authorId) {
        Author author = authorBookService.findAuthorById(authorId);
        return ResponseEntity.status(HttpStatus.FOUND).body(AuthorMapper.create().invert(author));
    }

}
