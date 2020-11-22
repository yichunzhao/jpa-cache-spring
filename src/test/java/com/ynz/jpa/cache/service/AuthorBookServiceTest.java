package com.ynz.jpa.cache.service;

import com.ynz.jpa.cache.entities.Author;
import com.ynz.jpa.cache.entities.Book;
import com.ynz.jpa.cache.exception.DuplicatedElementException;
import com.ynz.jpa.cache.exception.NotFoundException;
import com.ynz.jpa.cache.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class AuthorBookServiceTest {
    @Autowired
    private AuthorBookService authorBookService;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void testCreateAuthorBook() {
        Author author = new Author();
        author.setFirstName("Mike");
        author.setLastName("Zhao");

        Book book1 = new Book();
        book1.setTitle("my book1");

        Book book2 = new Book();
        book2.setTitle("my book2");

        //link author and books.
        author.addBook(book1);
        author.addBook(book2);

        Author created = authorBookService.createAuthorBook(author);

        assertAll(
                () -> assertThat(created, is(notNullValue())),
                () -> assertThat(created.getFirstName(), is("Mike")),
                () -> assertThat(created.getLastName(), is("Zhao")),
                () -> assertThat(created.getBooks(), hasSize(2))
        );
    }

    @Test
    void testUpdateExistingAuthorBook() {
        Author author = new Author();
        author.setFirstName("Mike");
        author.setLastName("Zhao");

        Book book1 = new Book();
        book1.setTitle("my book1");

        Book book2 = new Book();
        book2.setTitle("my book2");

        //link author and books.
        author.addBook(book1);
        author.addBook(book2);

        Author saved = authorRepository.save(author);
        assertNotNull(saved);

        saved.setFirstName("Mikey");
        Book book3 = new Book();
        book3.setTitle("my book3");
        saved.addBook(book3);

        Author updated = authorBookService.updateAuthor(saved);
        assertAll(
                () -> assertEquals(updated, saved),
                () -> assertThat(updated.getFirstName(), is("Mikey")),
                () -> assertThat(updated.getBooks(), hasSize(3))
        );
    }

    @Test
    void whenCreateAuthorExistedAlready_ThenItThrowsDuplicatedException() {
        Author author = new Author();
        author.setFirstName("William");
        author.setLastName("Shakespeare");

        assertThrows(DuplicatedElementException.class, () -> authorBookService.createAuthorBook(author));
    }

    @Test
    void findAuthorByName() {
        Author found = authorBookService.findAuthorByName("William", "Shakespeare");
        assertThat(found, is(notNullValue()));
    }

    @Test
    void whenFindExistingBookByTitle_ReturnThisBookAndItsAuthor() {
        String title = "The Tragical History of Hamlet";
        List<Book> found = authorBookService.findBookAuthorByTitle(title);
        assertAll(
                () -> assertThat(found, hasSize(1))
        );
    }

    @Test
    void whenFindBookByTitleNotExisted_ThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> authorBookService.findBookAuthorByTitle("unknown book"));
    }


    @Test
    void deleteBook() {
    }
}