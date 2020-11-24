package com.ynz.jpa.cache.controller;

import com.ynz.jpa.cache.dto.BookDto;
import com.ynz.jpa.cache.entities.Book;
import com.ynz.jpa.cache.mapper.BookMapper;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BooKAuthorController {
    private final AuthorBookService authorBookService;

    @GetMapping("/{title}")
    ResponseEntity<List<BookDto>> findBookAuthorByBookTitle(@PathVariable("title") String title) {
        List<Book> books = authorBookService.findBookAuthorByTitle(title);
        return ResponseEntity.status(HttpStatus.FOUND).body(books.stream().map(book ->
                BookMapper.create().invert(book)).collect(Collectors.toCollection(LinkedList::new)));
    }

    @GetMapping(value = "/{bookId}", params = "id")
    ResponseEntity<BookDto> findBookAuthorByBookId(@PathVariable("bookId") Integer bookId) {
        Book book = authorBookService.findBookAuthorByBookId(bookId);
        return ResponseEntity.status(HttpStatus.FOUND).body(BookMapper.create().invert(book));
    }

}
