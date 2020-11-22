package com.ynz.jpa.cache.repository;

import com.ynz.jpa.cache.entities.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

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
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager manager;

    @Test
    void testFindAuthorBooksByAuthorId() {
        Optional<Author> author = authorRepository.findAuthorBooksById(1);
        assertAll("find Author and Books",
                () -> assertTrue(author.isPresent()),
                () -> assertThat(author.get().getBooks(), hasSize(3))
        );
    }

    @Test
    void whenFindAuthorBooksByAuthorIdNotExisted_ReturnNull() {
        assertThat(authorRepository.findAuthorBooksById(10).isPresent(), is(false));
    }

    @Test
    void testFindAuthorBookByAuthorName() {
        Author author = authorRepository.findAuthorBooksByName("William", "Shakespeare");
        assertAll("find Author and Books",
                () -> assertThat(author, is(notNullValue())),
                () -> assertThat(author.getBooks(), hasSize(3))
        );
    }

    @Test
    void whenFindAuthorBookByFullName_ItReturnsList() {
        List<Author> foundAuthors = authorRepository.findAuthorBooksByFullName("William", "Shakespeare");
        assertAll(
                () -> assertThat(foundAuthors, hasSize(1)),
                () -> assertThat(foundAuthors.get(0).getBooks(), hasSize(3))
        );
    }

    @Test
    void findAuthorBook_WhenBookHavingMoreThanOneAuthor() {
        List<Author> foundAuthors = authorRepository.findAuthorBooksByFullName("K.Sam", "Shanmugan");
        assertAll(
                () -> assertThat(foundAuthors, hasSize(1)),
                () -> assertThat(foundAuthors.get(0).getBooks(), hasSize(1))
        );
    }

    @Test
    void whenFindAuthorNotExisted_ItReturnsEmptyList() {
        List<Author> foundAuthors = authorRepository.findAuthorBooksByFullName("Kang", "Yang");
        assertThat(foundAuthors, hasSize(0));
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