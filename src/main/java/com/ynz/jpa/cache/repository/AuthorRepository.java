package com.ynz.jpa.cache.repository;

import com.ynz.jpa.cache.entities.Author;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Integer> {
    @Query("Select a from Author a Join Fetch a.books b where a.authorId = :authorId")
    Optional<Author> findAuthorBooksById(@Param("authorId") Integer authorId);

    @Query("Select DISTINCT  a from Author a Join Fetch a.books b where a.firstName = :firstName And a.lastName = :lastName")
    List<Author> findAuthorBooksByFullName(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
