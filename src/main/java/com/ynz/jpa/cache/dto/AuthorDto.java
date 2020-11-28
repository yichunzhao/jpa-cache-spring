package com.ynz.jpa.cache.dto;

import javax.validation.constraints.NotBlank;

public class AuthorDto {
    private Integer authorId;

    @NotBlank(message = "Author must have a firstname")
    private String firstName;

    @NotBlank(message = "Author must have a lastname")
    private String lastName;

}
