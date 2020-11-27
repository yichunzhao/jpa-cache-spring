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
    public Author convert(AuthorBookDto authorDto) {
        Author author = new Author();
        author.setAuthorId(authorDto.getAuthorId());
        author.setFirstName(authorDto.getFirstName());
        author.setLastName(authorDto.getLastName());

        author.setBooks(authorDto.getBooks().stream().map(dto -> BookMapper.create().convert(dto))
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
    public List<Author> convert(Collection<AuthorBookDto> sources) {
        return sources.stream().map(dto -> convert(dto)).collect(toList());
    }

    @Override
    public List<AuthorBookDto> invert(Collection<Author> entities) {
        return entities.stream().map(entity -> invert(entity)).collect(toList());
    }
}
