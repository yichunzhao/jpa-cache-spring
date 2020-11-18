# jpa-cache-spring
Setup the 2nd level JPA cache with Springboot


### 2nd level Cache in JPA
The purpose of 2nd level cache is to store the often-visited data in the memory staying with the business logic, and therefore reducing the frequency to visit the database via the nextwork. So, the 2nd level cache is applied only with the fetching operations. Normally, we put the caching at the service layer, for we want the business logic directly access data there, instead of going further to visit the persistence layer. 

### Good practices for Many-to-Many with JPA-Hibernate

* Model associations as a Set; don't use List as modeling many-to-many. when Hibernate handling List on the many-to-many case, it first remove all records from the associated table; before inserting the remaining ones. 

* Bi-lateral association and provide utility methods

* Always use FetchType.Lazy: 

*  query-specific fetching: using joint-fetch; named entity graph;  joint-fetch cross(JOIN FETCH) is almost identical to a simple join clause in a JPQL query. Altough they look similar but Joint-fectch has much impact on the generated SQL query. 

* Don't use CascadeTye Remove and ALL. It may remove the whole of database. 

### Difference between Join and Join Fetch

Join and Join Fetch looks like very similar, but the generated SQL is quite different; Join-Fetch generates less SQL queries. 

When using Join, It generates two SQL queries; It causes 3 table joins totally.   

`@Query("Select a from Author a Join a.books b where a.authorId = :authorId")`

````
Hibernate: 
    select
        author0_.author_id as author_i1_0_,
        author0_.first_name as first_na2_0_,
        author0_.last_name as last_nam3_0_ 
    from
        authors author0_ 
    inner join
        book_author books1_ 
            on author0_.author_id=books1_.author_id 
    inner join
        books book2_ 
            on books1_.book_id=book2_.book_id 
    where
        author0_.author_id=?
Hibernate: 
    select
        books0_.author_id as author_i2_1_0_,
        books0_.book_id as book_id1_1_0_,
        book1_.book_id as book_id1_2_1_,
        book1_.title as title2_2_1_ 
    from
        book_author books0_ 
    inner join
        books book1_ 
            on books0_.book_id=book1_.book_id 
    where
        books0_.author_id=?

````

When using Join Fetch, it generates a single query by extending query selection fields.

`@Query("Select a from Author a Join Fetch a.books b where a.authorId = :authorId")`

````
Hibernate: 
    select
        author0_.author_id as author_i1_0_0_,
        book2_.book_id as book_id1_2_1_,
        author0_.first_name as first_na2_0_0_,
        author0_.last_name as last_nam3_0_0_,
        book2_.title as title2_2_1_,
        books1_.author_id as author_i2_1_0__,
        books1_.book_id as book_id1_1_0__ 
    from
        authors author0_ 
    inner join
        book_author books1_ 
            on author0_.author_id=books1_.author_id 
    inner join
        books book2_ 
            on books1_.book_id=book2_.book_id 
    where
        author0_.author_id=?
````
