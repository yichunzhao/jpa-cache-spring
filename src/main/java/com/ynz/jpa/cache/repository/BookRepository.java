package com.ynz.jpa.cache.repository;

import com.ynz.jpa.cache.entities.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {
    @Query("Select b from Book b Join Fetch b.authors a where b.title=:bookTitle")
    List<Book> findBookAuthor(@Param("bookTitle") String bookTitle);
}
