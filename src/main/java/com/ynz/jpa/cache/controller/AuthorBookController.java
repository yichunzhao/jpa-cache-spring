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

import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toCollection;

@RestController
@RequestMapping("/api/author")
@RequiredArgsConstructor
public class AuthorBookController {
    private final AuthorBookService authorBookService;

    @GetMapping("/{id}")
    ResponseEntity<AuthorBookDto> findAuthorBookById(@PathVariable("id") Integer authorId) {
        Author author = authorBookService.findAuthorById(authorId);
        return ResponseEntity.status(HttpStatus.FOUND).body(AuthorMapper.create().invert(author));
    }

    @GetMapping("/firstName/{firstName}/lastName/{lastName}")
    ResponseEntity<List<AuthorBookDto>> findBookAuthorByFullName(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName) {
        List<Author> authors = authorBookService.findAuthorByName(firstName, lastName);
        return ResponseEntity.status(HttpStatus.FOUND).body(authors.stream().map(author ->
                AuthorMapper.create().invert(author)).collect(toCollection(LinkedList::new)));
    }

}
