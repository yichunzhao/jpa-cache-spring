package com.ynz.jpa.cache.mapper;

import com.ynz.jpa.cache.dto.BookDto;
import com.ynz.jpa.cache.entities.Book;
import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data(staticConstructor = "create")
public class BookMapper implements Persistable<BookDto, Book> {


    @Override
    public Book convert(BookDto dto) {
        Book book = new Book();
        book.setBookId(dto.getBookId());
        book.setTitle(dto.getTitle());
        return book;
    }

    @Override
    public BookDto invert(Book book) {
        return BookDto.builder().bookId(book.getBookId()).title(book.getTitle()).build();
    }

    @Override
    public List<Book> convert(Collection<BookDto> dtos) {
        return dtos.stream().map(bookDto -> convert(bookDto)).collect(Collectors.toList());
    }

    @Override
    public List<BookDto> invert(Collection<Book> entities) {
        return entities.stream().map(entity -> invert(entity)).collect(Collectors.toList());
    }
}
