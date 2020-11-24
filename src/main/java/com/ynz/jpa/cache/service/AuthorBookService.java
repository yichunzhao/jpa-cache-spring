package com.ynz.jpa.cache.service;

import com.ynz.jpa.cache.entities.Author;
import com.ynz.jpa.cache.entities.Book;
import com.ynz.jpa.cache.exception.AuthorHasNoBookException;
import com.ynz.jpa.cache.exception.ResourceNotFoundException;
import com.ynz.jpa.cache.repository.AuthorRepository;
import com.ynz.jpa.cache.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
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
        if (author.getBooks().isEmpty()) throw new AuthorHasNoBookException("Author should have a published book");
        return authorRepository.save(author);
    }

    /**
     * Update an-existed author and its books
     *
     * @param authorId      target author to be updated
     * @param updatedAuthor Author contains Books
     * @return Author updated Author
     */
    public Author updateAuthor(Integer authorId, Author updatedAuthor) {
        Author found = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author is not existed!"));
        updatedAuthor.setAuthorId(found.getAuthorId());

        return authorRepository.save(updatedAuthor);
    }

    /**
     * Delete an Author, but not associated books if having more than one author.
     *
     * @param authorId Integer target author id
     */
    public void deleteAuthorById(Integer authorId) {
        Author found = findAuthorById(authorId);

        authorRepository.deleteById(authorId);

        //book has 1 author; remove it.
        //book has >1 author; keep book, but remove this author.
        for (Book book : found.getBooks()) {
            if (book.getAuthors().size() > 1) book.removeAuthor(found);
            if (book.getAuthors().size() == 1) bookRepository.deleteById(book.getBookId());
        }
    }

    public List<Author> findAuthorByName(String firstName, String lastName) {
        return authorRepository.findAuthorBooksByFullName(firstName, lastName);
    }

    public Author findAuthorById(Integer authorId) {
        return authorRepository.findAuthorBooksById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author Id: " + authorId + " is not existed!"));
    }

    public List<Book> findBookAuthorByTitle(String title) {
        List<Book> found = bookRepository.findBookAuthor(title);
        if (found.isEmpty()) throw new ResourceNotFoundException("Book title:" + title + " is not found");
        return found;
    }

}
