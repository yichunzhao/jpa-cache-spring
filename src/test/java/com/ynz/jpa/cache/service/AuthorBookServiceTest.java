package com.ynz.jpa.cache.service;

import com.ynz.jpa.cache.entities.Author;
import com.ynz.jpa.cache.entities.Book;
import com.ynz.jpa.cache.exception.DuplicatedElementException;
import com.ynz.jpa.cache.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class AuthorBookServiceTest {
    @Autowired
    private AuthorBookService authorBookService;

    @Test
    void createAuthor() {
        Author author = new Author();
        author.setFirstName("Mike");
        author.setLastName("Zhao");

        Author created = authorBookService.createAuthor(author);

        assertAll(
                () -> assertThat(created, is(notNullValue())),
                () -> assertThat(created.getFirstName(), is("Mike")),
                () -> assertThat(created.getLastName(), is("Zhao"))
        );
    }

    @Test
    void whenCreateAuthorExistedAlready_ThenItThrowsDuplicatedException() {
        Author author = new Author();
        author.setFirstName("William");
        author.setLastName("Shakespeare");

        assertThrows(DuplicatedElementException.class, () -> authorBookService.createAuthor(author));
    }

    @Test
    void whenDeleteAuthorNotExisted_ThrowNotFoundException() {
        Author author = new Author();
        author.setFirstName("Mike");
        author.setLastName("Zhao");
        assertThrows(NotFoundException.class, () -> authorBookService.deleteAuthor(author));
    }

    @Test
    void findAuthorByName() {
        Author found = authorBookService.findAuthorByName("William", "Shakespeare");
        assertThat(found, is(notNullValue()));
    }

    @Test
    void whenFindExistingBookByTitle_ReturnThisBookAndItsAuthor() {
        String title = "The Tragical History of Hamlet";
        Book found = authorBookService.findBookByTitle(title);
        assertAll(
                () -> assertThat(found, is(notNullValue())),
                () -> assertThat(found.getTitle(), is(title)),
                () -> assertThat(found.getAuthors(), hasSize(1))
        );
    }

    @Test
    void whenFindBookByTitleNotExisted_ThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> authorBookService.findBookByTitle("unknown book"));
    }

    @Test
    void createBook() {
        Book book = new Book();
        book.setTitle("I will write a book");

        Author author = new Author();
        author.setFirstName("Mike");
        author.setLastName("Zhao");

        book.addAuthor(author);

        Book created = authorBookService.createBook(book);
        assertAll(
                () -> assertThat(created, is(notNullValue())),
                () -> assertThat(created.getAuthors(), hasSize(1)),
                () -> assertThat(created.getTitle(), is("I will write a book"))
        );
    }

    @Test
    void updateBook() {
    }

    @Test
    void deleteBook() {
    }
}