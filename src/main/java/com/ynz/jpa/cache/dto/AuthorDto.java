package com.ynz.jpa.cache.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
public class AuthorDto {
    @NotBlank(message = "Author must have a firstname")
    private String firstName;

    @NotBlank(message = "Author must have a lastname")
    private String lastName;

    private Set<BookDto> books = new HashSet<>();

    public void addBook(BookDto book) {
        books.add(book);
        book.getAuthors().add(this);
    }

    public void removeBook(BookDto book) {
        books.remove(book);
        book.getAuthors().remove(this);
    }
}
