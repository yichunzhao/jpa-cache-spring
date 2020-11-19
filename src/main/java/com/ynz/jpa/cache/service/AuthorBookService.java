package com.ynz.jpa.cache.service;

import com.ynz.jpa.cache.entities.Author;
import com.ynz.jpa.cache.entities.Book;
import com.ynz.jpa.cache.exception.DuplicatedElementException;
import com.ynz.jpa.cache.exception.NotFoundException;
import com.ynz.jpa.cache.repository.AuthorRepository;
import com.ynz.jpa.cache.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorBookService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public Author createAuthor(Author author) {
        Author found = findAuthorByName(author.getFirstName(), author.getLastName());
        if (found != null) throw new DuplicatedElementException("Author is already existed!");
        return authorRepository.save(author);
    }

    public Author updateAuthor(Author author) {
        Author found = findAuthorByName(author.getFirstName(), author.getLastName());
        if (found != null) throw new NotFoundException("Author is not existed!");
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

    public Book findBookByTitle(String title) {
        Book found = bookRepository.findBookAuthor(title);
        if (found == null) throw new NotFoundException("book is found");
        return found;
    }

    public Book createBook(Book book) {
        try {
            return findBookByTitle(book.getTitle());
        } catch (NotFoundException e) {
            return bookRepository.save(book);
        }
    }

    public Book updateBook(Book updatedBook) {
        findBookByTitle(updatedBook.getTitle());
        return bookRepository.save(updatedBook);
    }

    public void deleteBook(Book book){
        findBookByTitle(book.getTitle());
        bookRepository.deleteById(book.getBookId());
    }


}
