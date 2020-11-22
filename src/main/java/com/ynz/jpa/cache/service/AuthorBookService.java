package com.ynz.jpa.cache.service;

import com.ynz.jpa.cache.entities.Author;
import com.ynz.jpa.cache.entities.Book;
import com.ynz.jpa.cache.exception.NotFoundException;
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

    public Author findAuthorByName(String firstName, String lastName) {
        Author found = authorRepository.findAuthorBooksByName(firstName, lastName);
        return found;
    }

    public Author findAuthorById(Integer authorId) {
        return authorRepository.findAuthorBooksById(authorId)
                .orElseThrow(() -> new NotFoundException("Author is not existed!"));
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
