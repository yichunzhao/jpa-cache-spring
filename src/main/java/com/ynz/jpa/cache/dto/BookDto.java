package com.ynz.jpa.cache.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class BookDto {
    private Integer bookId;

    @NotBlank(message = "Book must have a title")
    private String title;
}
