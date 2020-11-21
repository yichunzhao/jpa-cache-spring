package com.ynz.jpa.cache.repository;

import com.ynz.jpa.cache.entities.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.validation.ConstraintViolationException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DataJpaTest
public
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
    void whenSaveNonExistedAuthorWithoutName_JpaThrowValidationException() {
        Author author = new Author();
        assertThrows(ConstraintViolationException.class, () -> authorRepository.save(author));
    }

    @Test
    void afterTransientAuthorIsPersisted_HashCodeIsSameEqualsIsTrue() {
        Author author = new Author();
        assertNull(author.getAuthorId());
        Integer transientHashCode = author.hashCode();

        author.setFirstName("Mike");
        author.setLastName("Zhao");

        Author saved = authorRepository.save(author);

        assertAll(
                () -> assertNotNull(saved),
                () -> assertNotNull(saved.getAuthorId()),
                () -> assertEquals(transientHashCode, saved.hashCode()),
                () -> assertEquals(author, saved)//now pointing to the same DB entry.
        );
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