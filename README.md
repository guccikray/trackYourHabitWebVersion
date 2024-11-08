# TrackYourhabits Web Version

This project is web version of my console app.

Tools list:
 - Spring Boot 3.3.5
 - Spring Data JPA
 - Spring Security
 - Spring Web
 - JJWT-Api 0.12.6
 - PostgreSQL
 - Lombok
 - JUnit 5.11.2
 - AssertJ 3.26.3
 - Mockito 5.14.2

I used JWT tokens for user authentication instead of session.

To demonstrate how the application works i will use ***Postman*** and ***psql***

Firstly we need to register new account at path ***http://localhost:8080/registration***
This what we suppose to send by POST method:

![изображение](https://github.com/user-attachments/assets/b6422dd3-23c4-4576-ba89-d487c64011ff)

That's answer from server:

![изображение](https://github.com/user-attachments/assets/c4f3f3b4-f920-406a-83dc-02e4bbe60d58)

Now we need to sign in, but let's try to access paths that require authentvation without having it.
We received message that access is prohibited, that's because we don't have special token with which Spring Security can authonticate us.

![изображение](https://github.com/user-attachments/assets/04e72db5-07c1-4048-a471-771a93762fb4)

Sign in by this path ***http://localhost:8080/signin***

![изображение](https://github.com/user-attachments/assets/4d0db046-6b55-4a30-980f-fc1a63ce03b7)

We received our special token from server, to now all queries to path that requires authentication must be sent with special header. Key is "Authorization" and Value is "Bearer $token$"

![изображение](https://github.com/user-attachments/assets/4539a58e-25bb-49dd-801b-c4b299a63394)

Now let's create new habit, by POST query

![изображение](https://github.com/user-attachments/assets/86162aa8-cbf9-4f28-a53c-65fc82f58b7d)

![изображение](https://github.com/user-attachments/assets/621e087e-ddf9-4696-9000-aee9c492afe1)

I want to change description and frequency of 2nd habit, to do this i need to send PATCH query

![изображение](https://github.com/user-attachments/assets/530d9b28-a431-4f9a-9588-eb0789eea5b0)

To ensure that all changes applies lets check our table:

![изображение](https://github.com/user-attachments/assets/7cb13a21-31c8-4bd2-9c16-aa44b69f574f)

Let's try to delete our 2nd habit:

![изображение](https://github.com/user-attachments/assets/5e2517b9-25e5-4b6d-9143-d187b43bbf0e)

![изображение](https://github.com/user-attachments/assets/096efbe0-3cdf-44db-855d-7953b342ee26)

Now i want to try mark completions, to make it i need send POST query to ***http://localhost:8080/habits/habitProgress***:

![изображение](https://github.com/user-attachments/assets/21845509-4778-4a9c-baeb-e564fca735d9)

I also added few more completions and change data for test, because ***dateOfCompletion*** field filled in automatically:

![изображение](https://github.com/user-attachments/assets/e29c358d-5f21-4d9e-85a8-8165ee21402c)
 
For the conclusion, we can see our completion progress, send query with GET method:

![изображение](https://github.com/user-attachments/assets/e143c883-f8bb-4bb2-9b76-5eb14ad3f732)
