package com.ynz.jpa.cache.service;

import com.ynz.jpa.cache.entities.Author;
import com.ynz.jpa.cache.entities.Book;
import com.ynz.jpa.cache.exception.NotFoundException;
import com.ynz.jpa.cache.repository.AuthorRepository;
import com.ynz.jpa.cache.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorBookService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    /**
     * Create an non-existed author and link to his/her books.
     *
     * @param author Author contains Books
     * @return Author
     */
    public Author createAuthorBook(Author author) {
        return authorRepository.save(author);
    }

    /**
     * Update an-existed author and its books
     *
     * @param author Author contains Books
     * @return Author updated Author
     */
    public Author updateAuthor(Author author) {
        Author found = authorRepository.findById(author.getAuthorId())
                .orElseThrow(() -> new NotFoundException("Author is not existed!"));

        return authorRepository.save(author);
    }

    public void deleteAuthor(Author author) {
        Author found = findAuthorByName(author.getFirstName(), author.getLastName());
        if (found == null) throw new NotFoundException("Author is not existed!");
        authorRepository.delete(author);
    }

    public Author findAuthorByName(String firstName, String lastName) {
        Author found = authorRepository.findAuthorBooksByName(firstName, lastName);
        return found;
    }

    public List<Book> findBookAuthorByTitle(String title) {
        List<Book> found = bookRepository.findBookAuthor(title);
        if (found.isEmpty()) throw new NotFoundException("book is not found");
        return found;
    }

    public Book updateBook(Book updatedBook) {
        findBookAuthorByTitle(updatedBook.getTitle());
        return bookRepository.save(updatedBook);
    }

    public void deleteBook(Book book) {
        findBookAuthorByTitle(book.getTitle());
        bookRepository.deleteById(book.getBookId());
    }


}
