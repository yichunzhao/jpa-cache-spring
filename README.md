# jpa-cache-spring
Setup the 2nd level JPA cache with Springboot


### 2nd level Cache in JPA
The purpose of 2nd level cache is to store the often-visited data in the memory staying with the business logic, and therefore reducing the frequency to visit the database via the nextwork. So, the 2nd level cache is applied only with the fetching operations. Normally, we put the caching at the service layer, for we want the business logic directly access data there, instead of going further to visit the persistence layer. 

### Good practices for Many-to-Many with JPA-Hibernate

* Model associations as a Set; don't use List as modeling many-to-many. On Hibernate removing an element from a List ref. to a many-to-many case, it first remove all records from the associated table, then inserting the remaining ones. It costs much than the spending as using a Set. 

* Bi-lateral association and provide utility methods; a bi-lateral relationship facilitates table joins; utility methods at the both end, may help to build bi-directional association by invoking one method, so as to reducing errors.

* Always use FetchType.Lazy: always using lazy fetching to reduce N+1 problem. Instead, using join queries.  

* Query-specific fetching: using joint-fetch; named entity graph;  joint-fetch cross(JOIN FETCH) is almost identical to a simple join clause in a JPQL query. Altough they look similar but Joint-fectch has much impact on the generated SQL query. 

* Don't use CascadeTye Remove and ALL. It may remove the whole of database. 

### Spring Data JPA: Query Projections

Instead of returning all the properties of the returned objects constained in an aggregate root; Spring data allows modeling dedicated return types, to retrieve a partial view of the managed aggregates; it contains only properties that we care about. 

Entity projection is the most commonly used, but it is often not the best approach. If you need to optimise persistence layer performance, you should only use entity projections for write operations, for you have to rely on the persistence provider to generates Insert, Update, and Delete based on the entity lifecycle state transitions.  

For Read operations, three projection types. 
* Interface projection: 
  * Closed projection: Spring-data creates a projection proxy wrapping around the entity for us; we may use a projection interface in a repository interface as element type in the returned collection; the interface method exaclty matching the Entity property name; and the interface can be nested with aggregate projection interfaces.  
  * Open Projections: Accessor methods in projection interfaces can also be used to compute new values by using the @Value annotation; meaning that, fetching desired field values
 from the target, and recalculate them to reform a new value; using annotation @Value 
 
   ````
   interface NamesOnly {

    @Value("#{target.firstname + ' ' + target.lastname}")
    String getFullName();
    }
    ````
* Class-base projection: we create our own projection class, fx: DTO projection.
  * Using DTO to hold a partial view of the traget Entity class; it doesn't cause proxy generated 
  * Dynamic projections: using java generic type within the reposistory method, to project to multiple class types.

### Difference between Join and Join Fetch

Join and Join Fetch looks like very similar, but the generated SQL is quite different; Join-Fetch generates less SQL queries. 

When using Join, it generates two SQL queries, fetching author first then fetching books; it may cause N+1 problem. 

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

It was not a good experience as using Lombok with Entities, but it is totally fine with DTO. Don't use @Data, @EqualsAndHashCode and @ToString with @Entity, for Lombok internal implementation may not satisfy the requirements as an Entity.

#### StackOver flow as using lombok in bi-relationship
Watching out stackover flow exception as using lombok to create hashcode in a bi-directional relationship; it causes a recursive invoking, and eventually leading to a stack overflow. In the entities, the relationship anchors should be excluded. 

#### HashCode
Primary key is auto-generated as persisting in the database, otherwise it is a null. Hence, caclulating hascode with the primary key doesn't return a fixed value as using the Lombok. Intuitively, we may thhink using primary key to calculate hashcode for an entity, when using an auto-generated strategy, it doesn't fit the requirement about the hashcode in the java doc. So, in another way, we set the hashcode return a fixed number; or we have to figure out a business key or a natural key that may identify an entity/entry uniquely.

#### ToString
Lombok toString method may include lazy loaded fields, within a open persistence session it may cause extra queries and leaving database overhead.  

### Writting Entity HashCode and Equals

JPA entities maintain several states during their life cycles. Primary key is obtained only when the entity is persisted; so the same object may in different states having different primary key value. this produces different hashCode value, however this doesn't fit the Java doc requirement on the hashCode method. 

JavaDoc requirements on the hashCode method: 

> whenever hashcode method is invoked on the same object more than once during an execution of a java application, the hashCode method must consistently return the same integer,  if there is no information used in equals comparisions on the object that is modified. However, this integer need not remain consistent from one execution of an application to another execution of the same application. 
>
> if two objects are equal according to the their equals methods, then calling the hashCode method on each of the two objects must produce the same integer result.
>
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
### Spring Test Annotation @Sql and @SqlGroup

In Spring, it is easy to use DDL(Data definition languagte) to populate test specific schema and/or data for test class or test method; can be applied on the class or method level. 

Define multiple @Sql set or using @SqlGroup to repeat @Sql

#### Path-resource Sematics

Each path is interpreted as a Spring resource.  Resource folders,   resources and test resources are projected to the class path root, which can be referred by keyword *classpath:* or *'/'*, fx: "classpath:data.sql" or "/schema.sql". A plain path without keywords, like "xyz-test.sql" is referred to the package path that the class is defined, so you need to watch out here, when you use a plain path without keywords, then putting your test DDL along with the test classes in the same package. 

### Github actions

Github ofers cloud-based CI and CD tools. It is referred Github-actions. It is quite easy to build with a min efforts. 

Github actions is a event driven CI and CD tool. A event, push to master or pull request to master, may trigger corresponding Github actions, fx: a pull-request to master may trigger a maven build. Gitbub actions offers many pre-defined actions in the market place, so you may re-used them without rewritting all by yourself. 

Action: https://github.com/marketplace/actions/docker-build-push-action - Docker image build and push

Github actions is managed by Github, and exceuted in the Github server. Each job in a workflow runs in a fresh virtual env. Jobs are executed in parallel by default. So jobs may need to pre-define dependencies. 

Syntax

> name: [name of workflow] optional; normally take the same name as the workflow file name

> on: [event or events] required; events in (Push,Pull request, Release)

> jobs: [required] workflow must have at least one job
>       job1: 
>      
>            name: first job
>            runs-on: ubuntu-lastest   selecting a runner
>            steps:
>             -name: 
>             - uses  - select an action 
>       
>       job2 : 
>       
>            name: second job
>            runs-on: windows-latest    selecting a runner
>            steps:
>             -name:
>             - uses  - select an action
>       


### @DataJpaTest

The annotation disables full auto-configuration and apllies only configuration relevant to JPA tests(it is a tailored Spring context only for repository layer tests). By default, @DataJPATest use an embedded in-memory database. In the data jpa test context, a TestEntityManager bean is created, and used for test tasks like persist/flush/find. 

### JPA @Query and @Query and @Modifying

@Query for reading data alone from database. @Modifying is used to enhance the @Query. @Query and @Modifying together for Update/Insert/Delete and even DDL queries.

````
    @Modifying
    @Query("update Book b set b.title = :title where b.id=:bookId")
    int updateBookTitleById(@Param("bookId") Integer bookId, @Param("title") String title);
````

@Modifying queries return void or the number of updated entities. 

Discussion: 
if a @Query-query is involved in persistence context, having an entity life-cycle?

Using modifying queries leaves the underlying persistence context outdated. The @Modifying @Query queries against the database.  One way to solve this problem is to tell persistence context to clear up and fetch the entites from the database next time. This can be achived by using the clearAutomatically property from the @Modifying annotation `@Modifying(clearAutomatically = true)`. by this way, the persistence context will be cleared automatically after the entity is updated. This may cause Another problem, if the persistence context contains the unflushed changes, then we definitely wish to flush it before clearing the persitence context. This can be setup by another @Modifying property, `@Modifying(flushAutomatically = true)` 















