package com.ynz.jpa.cache.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
public class BookDto {
    @NotNull
    private String title;
    private Set<AuthorDto> authors = new HashSet<>();
}
