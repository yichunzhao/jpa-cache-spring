package com.ynz.jpa.cache.service;

import com.ynz.jpa.cache.entities.Author;
import com.ynz.jpa.cache.entities.Book;
import com.ynz.jpa.cache.exception.ResourceNotFoundException;
import com.ynz.jpa.cache.repository.AuthorRepository;
import com.ynz.jpa.cache.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class AuthorBookServiceTest {
    @Autowired
    private AuthorBookService authorBookService;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

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

        Author updated = authorBookService.updateAuthor(saved.getAuthorId(), saved);
        assertAll(
                () -> assertEquals(updated, saved),
                () -> assertThat(updated.getFirstName(), is("Mikey")),
                () -> assertThat(updated.getBooks(), hasSize(3))
        );
    }

    @Test
    void givenBooksHasOneAuthor_DeleteAuthor_BookIsAlsoRemoved() {
        Author author = new Author();
        author.setFirstName("Mike");
        author.setLastName("Zhao");

        Book book1 = new Book();
        book1.setTitle("my book1");

        Book book2 = new Book();
        book2.setTitle("my book2");

        //books having one author
        author.addBook(book1);
        author.addBook(book2);

        Author saved = authorRepository.save(author);
        assertNotNull(saved);

        authorBookService.deleteAuthorById(saved.getAuthorId());

        Optional<Author> found = authorRepository.findAuthorBooksById(saved.getAuthorId());
        assertAll(
                () -> assertFalse(found.isPresent()),
                () -> saved.getBooks().stream().mapToInt(Book::getBookId)
                        .forEach(id -> assertFalse(bookRepository.findById(id).isPresent()))
        );
    }

    @Test
    @Sql("classpath:import_bookAuthors.sql")
    void givenBookHavingMoreThanOneAuthor_DeleteOneAuthor_TheBookShouldNotBeRemoved() {
        authorBookService.deleteAuthorById(10);
        assertAll(
                () -> assertTrue(bookRepository.findById(10).isPresent()),
                () -> assertFalse(authorRepository.findById(10).isPresent()),
                () -> assertThat(bookRepository.findById(10).get().getAuthors(), hasSize(1))
        );
    }

    @Test
    void itAllowsAuthorsHavingSameName() {
        Author author = new Author();
        author.setFirstName("William");
        author.setLastName("Shakespeare");

        //this William has one faked book published
        Book book1 = new Book();
        book1.setTitle("my book1");
        author.addBook(book1);

        Author persisted = authorBookService.createAuthorBook(author);
        assertNotNull(persisted);

        List<Author> anotherShakespeare = authorRepository.findAuthorBooksByFullName("William", "Shakespeare");
        assertThat(anotherShakespeare, hasSize(2));
    }

    @Test
    void findAuthorByName() {
        assertThat(authorBookService.findAuthorByName("William", "Shakespeare"), hasSize(1));
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
        assertThrows(ResourceNotFoundException.class, () -> authorBookService.findBookAuthorByTitle("unknown book"));
    }

    @Test
    void findAExistingBookByItsId() {
        Book found = authorBookService.findBookAuthorByBookId(1);
        assertAll(
                () -> assertNotNull(found),
                () -> assertThat(found.getAuthors(), is(not(emptyIterable())))
        );
    }

    @Test
    void whenFindBookNotExistedById_ThenItThrowsNotFoundException() {
        assertThrows(ResourceNotFoundException.class, () -> authorBookService.findBookAuthorByBookId(100));
    }

    @Test
    @Sql("classpath:import_bookAuthors.sql")
    void givenAuthorAndHisBook_UpdateAuthorInfo() {
        Author updated = new Author();
        updated.setFirstName("updatedFirstName");

        Author other = authorBookService.updateAuthor(10, updated);

        assertAll(
                () -> assertNotNull(other),
                () -> assertEquals(other.getFirstName(), "updatedFirstName"),
                () -> assertThat(other.getBooks(), hasSize(1))
        );
    }

    @Test
    void givenAuthorNotExisted_UpdateAuthorCauseException() {
        Author updated = new Author();
        assertThrows(ResourceNotFoundException.class, () -> authorBookService.updateAuthor(100, updated));
    }

    @Test
    @Sql("classpath:import_bookAuthors.sql")
    void givenBookAuthor_UpdateBookInfo() {
        Book newBook = new Book();
        newBook.setTitle("JAVA Generics-updated");
        Book updatedBook = authorBookService.updateBook(10, newBook);

        assertAll(
                () -> assertNotNull(updatedBook),
                () -> assertThat(updatedBook.getTitle(), is("JAVA Generics-updated")),
                () -> assertThat(updatedBook.getAuthors(), hasSize(2))
        );
    }

    @Test
    void givenBookNotExisted_UpdateCauseException() {
        Book newBook = new Book();
        assertThrows(ResourceNotFoundException.class, () -> authorBookService.updateBook(100, newBook));
    }


}