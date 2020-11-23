package com.ynz.jpa.cache.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Builder
public class AuthorBookDto {
    private Integer authorId;

    @NotBlank(message = "Author must have a firstname")
    private String firstName;

    @NotBlank(message = "Author must have a lastname")
    private String lastName;

    private Set<BookDto> books = new LinkedHashSet<>();
}
