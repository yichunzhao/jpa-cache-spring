package com.ynz.jpa.cache.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
public class BookDto {
    @NotBlank(message="Book must have a title")
    private String title;
    private Set<AuthorDto> authors = new HashSet<>();
}
