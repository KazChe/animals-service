# Animal Image Service v0.1

A Spring Boot application that fetches and stores animal images Cats, Bears, and Dogs.

## Quick Start

```bash
# Clone the repository
git clone https://github.com/yourusername/animal-image-service.git

# cd into the project
cd animal-service

# Run with Docker
docker compose up

# Or run with Maven
./mvnw spring-boot:run
```

Access the application at http://localhost:8080

## Design Choices & Trade-offs

### Architecture

- **Spring Boot Framework**: Chosen for rapid development, robust dependency injection, and extensive ecosystem
- **H2 In-Memory Database**: Used for simplicity and portability, trade-off being data persistence
- **Multi-stage Docker Build**: Optimizes image size and improves security
- **RESTful API Design**: Simple CRUD operations for scalability
- **Non-blocking UI**: Async JavaScript for better user experience

### Security Considerations

- Non-root user in Docker container
- Environment-based configuration
- Health checks implemented

### Trade-offs

- In-memory database vs. persistence
- Spring Boot's larger footprint vs. development speed
- Docker image size vs. development convenience
- Simple error handling vs. complexity

## Features

- Fetch and store images of cats, dogs, and bears
- Retrieve the latest stored image for each animal type
- Simple web UI for interaction
- Containerized application with Docker
- H2 in-memory database for storage

## Running the Application

### Uses

- Java 19
- Maven Apache Maven 3.9.9
- Spring Boot 3.2
- Docker (optional)

### Quick Start - Maven

```bash
# clean and build the project
mvn clean install

# just compile if in a hurry
mvn compile

# skip tests
mvn clean install -DskipTests

# build nd make jar
mvn package

# run application
./mvnw spring-boot:run

# run with specific profile
./mvnw spring-boot:run -Dspring.profiles.active=dev
```

### Running Tests - WIP

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=ImageServiceTest
```

### Accessing H2 Database Console

1. Application need to be be running
2. Navigate to: http://localhost:8080/h2-console
3. Use these settings:
   - JDBC URL: `jdbc:h2:mem:animaldb`
   - Username: `sa`
   - Password: ` ` (empty)
   - Driver Class: `org.h2.Driver`

### Via Docker Compose

```bash
# build and start services
docker compose up --build

# run in detached mode
docker compose up -d

# view logs
docker compose logs -f

# stop services
docker compose down
```

## API Reference

### Save Images

POST /api/images/save/{type}?count={count}

- `type`: Animal type (CAT, DOG, BEAR)
- `count`: Number of images to save (default: 1, max: 10)
- Returns: Array of image URLs

### Get Latest Image

GET /api/images/latest/{type}

- `type`: Animal type (CAT, DOG, BEAR)
- Returns: URL of the most recently saved image

### Response Codes

- 200: Success
- 404: Image not found
- 500: Server error

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/challenge/
│   │       ├── config/
│   │       │   └── RestTemplateConfig.java
│   │       ├── controller/
│   │       │   └── ImageController.java
│   │       ├── dto/
│   │       │   ├── CatApiResponse.java
│   │       │   └── DogApiResponse.java
│   │       ├── exception/
│   │       │   ├── GlobalExceptionHandler.java
│   │       │   └── ImageNotFoundException.java
│   │       ├── model/
│   │       │   ├── Animal.java
│   │       │   └── AnimalType.java
│   │       ├── repository/
│   │       │   └── AnimalRepository.java
│   │       ├── service/
│   │       │   └── ImageService.java
│   │       └── ImageServiceApplication.java
│   └── resources/
│       ├── static/
│       │   ├── index.html
│       │   └── js/
│       │       └── main.js
│       └── application.properties
├── test/
│   └── java/
│       └── com/challenge/
│           └── service/
│               └── ImageServiceTest.java
├── Dockerfile
├── docker-compose.yml
├── mvnw
├── mvnw.cmd
└── pom.xml
```

shows the tree structure of project

```cli
tree -I 'target|.git|.mvn|.vscode' --dirsfirst -a
```
