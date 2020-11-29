package com.ynz.jpa.cache.mapper;

import com.ynz.jpa.cache.dto.AuthorBookDto;
import com.ynz.jpa.cache.entities.Author;
import lombok.Data;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

@Data(staticConstructor = "create")
public class AuthorMapper implements Persistable<AuthorBookDto, Author> {
    @Override
    public Author convert(AuthorBookDto entity) {
        Author author = new Author();
        author.setAuthorId(entity.getAuthorId());
        author.setFirstName(entity.getFirstName());
        author.setLastName(entity.getLastName());

        author.setBooks(entity.getBooks().stream().map(dto -> BookMapper.create().convert(dto))
                .collect(toCollection(LinkedHashSet::new)));
        return author;
    }

    @Override
    public AuthorBookDto invert(Author author) {
        return AuthorBookDto.builder().authorId(author.getAuthorId())
                .firstName(author.getFirstName()).lastName(author.getLastName())
                .books(author.getBooks().stream()
                        .map(book -> BookMapper.create().invert(book)).collect(toCollection(LinkedHashSet::new)))
                .build();
    }

    @Override
    public List<Author> convert(Collection<AuthorBookDto> entities) {
        return entities.stream().map(dto -> convert(dto)).collect(toList());
    }

    @Override
    public List<AuthorBookDto> invert(Collection<Author> entities) {
        return entities.stream().map(entity -> invert(entity)).collect(toList());
    }
}
