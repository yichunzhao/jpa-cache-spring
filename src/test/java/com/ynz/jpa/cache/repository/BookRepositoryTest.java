package com.ynz.jpa.cache.repository;

import com.ynz.jpa.cache.entities.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.validation.ConstraintViolationException;
import java.util.Collection;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager manager;

    @Test
    void testFetchBookByBookId() {
        Optional<Book> book = bookRepository.findById(1);
        assertTrue(book.isPresent());
    }

    @Test
    void whenBookIdIsNotExisted_ReturnEmptyOptional() {
        assertFalse(bookRepository.findById(1000).isPresent());
    }

    @Test
    void testFindAllBook() {
        Collection<Book> books = (Collection<Book>) bookRepository.findAll();
        assertThat(books, hasSize(6));
    }

    @Test
    void givenBookTitle_FindBookAndItsAuthors() {
        String bookName = "The Tragical History of Hamlet";
        Book found = bookRepository.findBookAuthor(bookName);

        assertAll("find found and its author: ",
                () -> assertNotNull(found),
                () -> assertThat(found.getAuthors(), hasSize(1))
        );
    }

    @Test
    void testDeleteBookById() {
        Book book = new Book();
        book.setTitle("my book");

        Book persisted = manager.persistAndFlush(book);
        assertNotNull(persisted);
        bookRepository.deleteById(persisted.getBookId());
        assertNull(manager.find(Book.class, persisted.getBookId()));
    }

    @Test
    void testSaveNewBookWithoutAuthor() {
        Book book = new Book();
        book.setTitle("my book");

        Book persisted = bookRepository.save(book);
        assertNotNull(persisted);
    }

    @Test
    void whenSavingBookWithoutTitle_JpaThrowValidationException() {
        Book book = new Book();
        assertThrows(ConstraintViolationException.class, () -> bookRepository.save(book));
    }

    @Test
    void afterTransientBookIsPersisted_HashCodeSameEqualsTrue() {
        Book book = new Book();
        book.setTitle("my book");
        //PK is null
        assertNull(book.getBookId());

        Book saved = bookRepository.save(book);
        assertEquals(book.hashCode(), saved.hashCode());
        //now they both refer to the same db entry, because of the same PK.
        assertEquals(book, saved);
    }


}