package com.ynz.jpa.cache.dto;

import com.ynz.jpa.cache.entities.Book;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
public class AuthorDto {
    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private Set<Book> books = new HashSet<>();
}
