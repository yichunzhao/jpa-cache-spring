package com.ynz.jpa.cache.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "AUTHOR")
@Data
public class Author {
    @Id
    @GeneratedValue
    private Integer authorId;

    @Column(nullable = false, length = 128)
    private String firstName;
    @Column(nullable = false, length = 128)
    private String lastName;

    @ManyToMany(mappedBy = "authors")
    private Set<Book> books = new HashSet<>();
}
