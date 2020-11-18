# jpa-cache-spring
Setup the 2nd level JPA cache with Springboot



### Good practices for Many-to-Many with JPA-Hibernate

* Model associations as a Set; don't use List as modeling many-to-many. when Hibernate handling List on the many-to-many case, it first remove all records from the associated table; before inserting the remaining ones. 

* Bi-lateral association and provide utility methods

* Always use FetchType.Lazy: 

*  query-specific fetching

using joint-fetch; named entity graph; 
joint-fetch cross(JOIN FETCH) is almost identical to a simple join clause in a JPQL query. Altough they look similar but Joint-fectch has much impact on the generated SQL query. 


* Don't use CascadeTye Remove and ALL. It may remove the whole of database. 
