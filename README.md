<div align="center">
    <img height="36" src="https://raw.githubusercontent.com/marceloakalopes/minilinkr/refs/heads/main/images/logo.png" alt="MiniLinkr Logo" />
</div>

<p align="center">
  <i align="center">Lightning-fast, tiny URLs for effortless sharing.</i>
</p>


![MiniLinkr Sample](/images/sample.png)

MiniLinkr is a lightning-fast URL shortener API built with Spring Boot. It allows you to create custom short URLs with user-defined aliases (or generate random ones), manage mappings, and seamlessly redirect users to original URLs. Out-of-the-box support for OpenAPI documentation and Docker makes it production-ready.

## Table of Contents

* [Features](#features)
* [Tech Stack](#tech-stack)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Installation](#installation)
  * [Configuration](#configuration)
* [Running the Application](#running-the-application)
  * [With Maven](#with-maven)
  * [As Executable JAR](#as-executable-jar)
  * [Using Docker](#using-docker)
* [API Endpoints](#api-endpoints)
* [Swagger Documentation](#swagger-documentation)
* [Contributing](#contributing)
* [License](#license)

## Features

* **Custom URL Shortening**: Generate short URLs with your own alias or let the system auto-generate one.
* **Redirects**: Visit `/{alias}` to be redirected to the original URL.
* **Validation**: URL format checks and alias length validation (3–20 characters).
* **Error Handling**: Meaningful HTTP responses for duplicate aliases, invalid input, not found, etc.
* **OpenAPI Support**: Auto-generated Swagger UI for easy exploration and testing.
* **Configurable Persistence**: Plug in MySQL, PostgreSQL, or any JDBC-compatible database via Spring Data JPA.
* **Docker-Ready**: Dockerfile included for containerized deployments.

## Tech Stack

* **Language & Framework**: Java 17 & Spring Boot
* **Persistence**: Spring Data JPA (MySQL/PostgreSQL)
* **Validation**: Hibernate Validator
* **API Docs**: Springdoc OpenAPI (Swagger UI)
* **Build Tool**: Maven
* **Containerization**: Docker

## Getting Started

### Prerequisites

* Java 17+
* Maven 3.6+
* A running instance of MySQL or PostgreSQL (or use Docker Compose)

### Installation

1. **Clone the repository**:

   ```bash
   git clone https://github.com/marceloakalopes/minilinkr.git
   cd minilinkr
   ```

2. **Configure the application**:
   Copy `src/main/resources/application.properties.example` to `application.properties` and update your database credentials. Example settings:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/minilinkr
   spring.datasource.username=root
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   server.port=8080
   ```

## Running the Application

### With Maven

```bash
mvn spring-boot:run
```

### As Executable JAR

```bash
mvn clean package
java -jar target/minilinkr-0.0.1-SNAPSHOT.jar
```

### Using Docker

1. **Build the Docker image**:

   ```bash
   docker build -t minilinkr .
   ```

2. **Run the container**:
   ```bash
   docker run -d -p 8080:8080 --name minilinkr minilinkr
   ```

## API Endpoints

All endpoints are prefixed with `/api/v1`.

| Method | Endpoint               | Description                            |
| ------ | ---------------------- | -------------------------------------- |
| POST   | `/api/v1/urls`         | Create a new short URL mapping.        |
| GET    | `/api/v1/urls`         | Retrieve all URL mappings.             |
| GET    | `/api/v1/urls/{alias}` | Retrieve details for a specific alias. |
| DELETE | `/api/v1/urls/{alias}` | Delete a URL mapping.                  |
| GET    | `/{alias}`             | Redirect to the original URL.          |

### Sample: Create Short URL

**Request**:

```http
POST /api/v1/urls
Content-Type: application/json

{  
  "originalUrl": "https://example.com/page",  
  "alias": "example"
}
```

**Response** (`201 Created`):

```json
{
  "alias": "example",
  "shortUrl": "http://localhost:8080/example",
  "originalUrl": "https://example.com/page",
  "createdAt": "2025-05-18T12:34:56Z"
}
```

## Swagger Documentation

Once running, visit [`http://localhost:8080/swagger-ui/index.html`](http://localhost:8080/swagger-ui/index.html) to explore and test the API interactively.

## Contributing

Contributions are welcome!

1. Fork the repository.
2. Create a feature branch: `git checkout -b feature/my-feature`.
3. Commit your changes: `git commit -m "Add feature"`.
4. Push to your branch: `git push origin feature/my-feature`.
5. Open a Pull Request.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
