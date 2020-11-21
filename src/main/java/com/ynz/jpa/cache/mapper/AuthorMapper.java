package com.ynz.jpa.cache.mapper;

import com.ynz.jpa.cache.dto.AuthorDto;
import com.ynz.jpa.cache.entities.Author;

public class AuthorMapper implements Persistable<AuthorDto, Author> {
    @Override
    public Author convert(AuthorDto source) {
        return null;
    }

    @Override
    public AuthorDto invert(Author entity) {
        return null;
    }
}
