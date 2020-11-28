package com.ynz.jpa.cache.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class BookAuthorDto {
    private Integer bookId;

    @NotBlank(message = "Book must have a title")
    private String title;

    private List<AuthorDto> authors = new ArrayList<>();
}
