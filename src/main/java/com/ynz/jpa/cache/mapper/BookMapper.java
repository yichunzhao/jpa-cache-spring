package com.ynz.jpa.cache.mapper;

import com.ynz.jpa.cache.dto.BookDto;
import com.ynz.jpa.cache.entities.Book;
import lombok.Data;

@Data(staticConstructor = "create")
public class BookMapper implements Persistable<BookDto, Book> {

    @Override
    public Book convert(BookDto bookDto) {
        Book book = new Book();
        book.setBookId(bookDto.getBookId());
        book.setTitle(bookDto.getTitle());
        return book;
    }

    @Override
    public BookDto invert(Book book) {
        return BookDto.builder().bookId(book.getBookId()).title(book.getTitle()).build();
    }
}
