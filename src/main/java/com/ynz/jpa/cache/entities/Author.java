package com.ynz.jpa.cache.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "AUTHORS")
@Data
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer authorId;

    @Column(nullable = false, length = 128, unique = true)
    private String firstName;
    @Column(nullable = false, length = 128)
    private String lastName;

    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "authors", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = Book.class)
    private Set<Book> books = new HashSet<>();

    public void addBook(Book book) {
        books.add(book);
    }

}
