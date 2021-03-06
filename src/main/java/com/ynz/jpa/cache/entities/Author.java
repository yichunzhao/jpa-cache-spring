package com.ynz.jpa.cache.entities;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "AUTHORS")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer authorId;

    @Column(nullable = false, length = 128)
    @NotBlank(message = "Author must have a firstname.")
    private String firstName;

    @Column(nullable = false, length = 128)
    @NotNull(message = "Author must have a lastname")
    private String lastName;

    @ManyToMany(mappedBy = "authors", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = Book.class)
    private Set<Book> books = new LinkedHashSet<>();

    public Author() {
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == null) return false;
        if (this == o) return true;
        if (!(o instanceof Author)) return false;
        Author author = (Author) o;
        return this.authorId != null && this.authorId.equals(author.getAuthorId());
    }

    @Override
    public int hashCode() {
        return 47;
    }

    public void addBook(Book book) {
        this.books.add(book);
        book.getAuthors().add(this);
    }
}
