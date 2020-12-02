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
     * Updating an-existed author by author id; An author data model is associated with book models, but here we only
     * allow to update author alone. The user needs to provide the target author Id, and fill up an Author
     * template where fields present the expected updated values.
     *
     * @param authorId  Integer; It points to a target author that needs to be updated.
     * @param newFields Author; It contains fields that are expected to be updated.
     * @return Author; It returns an updated author.
     */
    public Author updateAuthor(Integer authorId, Author newFields) {
        //identifying target in db first.
        Author existed = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author is not existed!"));

        //assigning new values to the existing fields.
        if (newFields.getFirstName() != null && !newFields.getFirstName().equals(existed.getFirstName()))
            existed.setFirstName(newFields.getFirstName());

        if (newFields.getLastName() != null && !newFields.getLastName().equals(existed.getLastName()))
            existed.setLastName(newFields.getLastName());

        return authorRepository.save(existed);
    }

    /**
     * Updating an existing book partially by looking for a book id.
     *
     * @param bookId  Integer. It points the target book.
     * @param newFields Book. A book template contains newly assigned book field values.
     * @return Book. It points a newly updated book entity.
     */
    public Book updateBook(Integer bookId, Book newFields) {
        //identifying target in db first.
        Book existed = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book id:" + bookId + " is not existed!"));

        //partially modifying a book.
        if (newFields.getTitle() != null && !newFields.getTitle().equals(existed.getTitle()))
            existed.setTitle(newFields.getTitle());

        return bookRepository.save(existed);
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
            else {
                bookRepository.deleteById(book.getBookId());
            }
        }
    }

    /**
     * Find authors by firstname and lastname
     *
     * @param firstName String
     * @param lastName  String
     * @return List<Author>
     */
    public List<Author> findAuthorByName(String firstName, String lastName) {
        return authorRepository.findAuthorBooksByFullName(firstName, lastName);
    }

    /**
     * Find author by author id
     *
     * @param authorId Integer
     * @return Author
     */
    public Author findAuthorById(Integer authorId) {
        return authorRepository.findAuthorBooksById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author Id: " + authorId + " is not existed!"));
    }

    /**
     * Find Book and its Authors by a book title; Many books may have the same title.
     *
     * @param title String This is the book title that is supposed to be searched.
     * @return List this returns a list of books that matches the target title.
     */
    public List<Book> findBookAuthorByTitle(String title) {
        List<Book> found = bookRepository.findBookAuthor(title);
        if (found.isEmpty()) throw new ResourceNotFoundException("Book title:" + title + " is not found");
        return found;
    }


    /**
     * Find Book and its Author(s) referring to a bookId.
     *
     * @param bookId Integer this is the book id that is supposed to be searched.
     * @return Book this returns the book if it is existed; otherwise, it will throws a runtime ResourceNotFoundException.
     */
    public Book findBookAuthorByBookId(Integer bookId) {
        return bookRepository.findBookAuthorByBookId(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book id:" + bookId + " is not found"));
    }


}
