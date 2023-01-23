# PwC

Interview task for PwC project

### Instruction to run

````
mvn clean install spring-boot:run
````

Tested with:

````
Maven home: C:\apache-maven-3.8.7
Java version: 17.0.1,
````

### Additional notes

There could be some improvements introduced, but I could not invest more time.

1. More detailed error handling with validation etc.
2. More detailed testing with test country resources.
3. We could load the json directly from URL instead of maintaining it locally, but it wasn't present directly in requirement.
4. I used jGraphT to calculate the shortest path with Dijsktra algorithm. Not sure if the point of this task was to
   implement it by myself, but when there's some handy tool which helps to solve particular problem I prefer to use it.

