# jpa-cache-spring
Setup the 2nd level JPA cache with Springboot


### 2nd level Cache in JPA
The purpose of 2nd level cache is to store the often-visited data in the memory staying with the business logic, and therefore reducing the frequency to visit the database via the nextwork. So, the 2nd level cache is applied only with the fetching operations. Normally, we put the caching at the service layer, for we want the business logic directly access data there, instead of going further to visit the persistence layer. 

### Good practices for Many-to-Many with JPA-Hibernate

* Model associations as a Set; don't use List as modeling many-to-many. On Hibernate removing an element from a List ref. to a many-to-many case, it first remove all records from the associated table, then inserting the remaining ones. It costs much than the spending as using a Set. 

* Bi-lateral association and provide utility methods; a bi-lateral relationship facilitates table joins; utility methods at the both end, may help to build bi-directional association by invoking one method, so as to reducing errors.

* Always use FetchType.Lazy: always using lazy fetching to reduce N+1 problem. Instead, using join queries.  

*  Query-specific fetching: using joint-fetch; named entity graph;  joint-fetch cross(JOIN FETCH) is almost identical to a simple join clause in a JPQL query. Altough they look similar but Joint-fectch has much impact on the generated SQL query. 

* Don't use CascadeTye Remove and ALL. It may remove the whole of database. 

### Spring Data JPA: Query Projections


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

### Issues as using Lombok with Entities

It was not a good experience as using Lombok with Entities, but it is totally fine with DTO. Don't use @Data, @EqualsAndHashCode and @ToString

#### StackOver flow as using lombok in bi-relationship
Watching out stackover flow exception as using lombok to create hashcode in a bi-directional relationship; it causes a recursive invoking, and eventually leading to a stack overflow. In the entities, the relationship achors doesn't stand for model physical meanings, they should be excluded. 

#### HashCode
Primary key is auto-generated as persisting in the database, otherwise it is a null. Hence, caclulating hascode with the primary key doesn't return a fixed value as using the Lombok. 

#### ToString
Lombok toString method may include lazy loaded fields, within a open persistence session it may cause extra queries and leaving database overhead.  

### Writting Entity HashCode and Equals

JPA entities maintain several states during their life cycles. Primary key is obtained only when the entity is persisted; so the same object may in different states having different primary key value. this produces different hashCode value, however this doesn't fit the Java doc requirement on the hashCode method. 

JavaDoc requirements on the hashCode method: 

> whenever hashcode method is invoked on the same object more than once during an execution of a java application, the hashCode method must consistently return the same integer, > if no information used in equals comparisions on the object is modified. However, this integer need not remain consistent from one execution of an application to another execution of the same application. 

> if two objects are equal according to the their equals methods, then calling the hashCode method on each of the two objects must produce the same integer result.

> if two object are not equal according to the equals method, it is not required to produce distinct integer as invoking hashCode method; However, if it produces distinct hashCodes, it may improve hashtable performance. (ref. to hashtable or hashmap internal implementation)

principles:  

> if you tell the JPA to generate the primary key, then you need to return a fixed value from the hashCode method. Certainly, this will reduce hashtable performance, for all the > values go to the same buket. On this case, the equals method determines if two objects are duplicated objects. Special handling of null in Equals();  

> if programmatically set primary key;  use them in equals() and hashCode()

> if you use naturalId or business key; use them in equals()  and hashCode()

### Bean-Validation within JPA

Since Springboot 2.3, bean-validation framework become an independent starter. 

````
<dependency> 
    <groupId>org.springframework.boot</groupId> 
    <artifactId>spring-boot-starter-validation</artifactId> 
</dependency>
````
