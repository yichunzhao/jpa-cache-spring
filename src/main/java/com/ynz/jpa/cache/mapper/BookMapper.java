package com.ynz.jpa.cache.mapper;

import com.ynz.jpa.cache.dto.BookDto;
import com.ynz.jpa.cache.entities.Book;

public class BookMapper implements Persistable<BookDto, Book> {
    @Override
    public Book convert(BookDto source) {
        return null;
    }

    @Override
    public BookDto invert(Book entity) {
        return null;
    }
}
