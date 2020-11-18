package com.ynz.jpa.cache.repository;

import com.ynz.jpa.cache.entities.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


@DataJpaTest
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager manager;

    @Test
    void testFindAuthorBooksByAuthorId() {
        Author author = authorRepository.findAuthorBooks(1);
        assertAll("find Author and Books",
                () -> assertThat(author, is(notNullValue())),
                () -> assertThat(author.getBooks(), hasSize(3))
        );
    }

    @Test
    void whenFindAuthorBooksByAuthorIdNotExisted_ReturnNull() {
        assertThat(authorRepository.findAuthorBooks(10), is(nullValue()));
    }

    @Test
    void testFindAuthorBookByName() {
        Author author = authorRepository.findAuthorBooksByName("William", "Shakespeare");
        assertAll("find Author and Books",
                () -> assertThat(author, is(notNullValue())),
                () -> assertThat(author.getBooks(), hasSize(3))
        );
    }

    @Test
    void whenFindAuthorBookWhoIsNotExisted_ReturnNull() {
        Author author = authorRepository.findAuthorBooksByName("Kang", "Yang");
        assertThat(author, is(nullValue()));
    }

    @Test
    void saveNonExistedAuthor() {
        Author author = new Author();
        authorRepository.save(author);
    }

    @Test
    void testDeleteAuthorById() {
        Author author = new Author();
        author.setFirstName("Mike");
        author.setLastName("Zhao");

        Author persisted = manager.persistAndFlush(author);
        assertNotNull(persisted);
        authorRepository.deleteById(persisted.getAuthorId());
        assertNull(manager.find(Author.class, persisted.getAuthorId()));
    }

}