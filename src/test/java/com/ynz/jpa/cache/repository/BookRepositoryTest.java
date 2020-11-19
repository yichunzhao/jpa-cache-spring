package com.ynz.jpa.cache.repository;

import com.ynz.jpa.cache.entities.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Collection;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
    void testFetchAllBook() {
        Collection<Book> books = (Collection<Book>) bookRepository.findAll();
        assertThat(books, hasSize(6));
    }

    @Test
    void testFindBookAuthors() {
        String bookName = "The Tragical History of Hamlet";
        Book book = bookRepository.findBookAuthor(bookName);

        assertAll("find book and its author: ",
                () -> assertNotNull(book),
                () -> assertThat(book.getAuthors(), hasSize(1))
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
    void testSaveNewBookWithoutAuthor(){
        Book book = new Book();
        book.setTitle("my book");

        Book persisted = bookRepository.save(book);
        assertNotNull(persisted);

    }


}