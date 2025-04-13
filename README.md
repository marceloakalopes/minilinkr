# MiniLinkr API

![MiniLinkr Sample](/images/sample.png)

MiniLinkr is a URL shortener API built with Spring Boot that allows you to create custom short URLs with custom aliases. The API provides endpoints for shortening URLs, retrieving original URLs, and handling rich link previews via standard OpenAPI (Swagger) documentation.

> **Note:** This README covers the back-end API only.
## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
    - [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Swagger Documentation](#swagger-documentation)
- [Deployment](#deployment)
- [Security](#security)
- [Contributing](#contributing)
- [License](#license)

## Features

- **Custom URL Shortening:**  
  Generate a short URL using a custom alias provided by the user.
- **Redirection:**  
  Easily redirect from the short URL to the original long URL.
- **Validation:**  
  Validate URL formats and alias length (between 3 and 20 characters).
- **Dynamic Metadata for Rich Previews:**  
  Optionally generate a preview page with Open Graph meta tags for social media link previews.
- **Integrated Swagger API Docs:**  
  Built-in API documentation (via Springdoc) available at `/swagger-ui/index.html`.

## Tech Stack

- **Backend Framework:** Spring Boot
- **Persistence:** Spring Data JPA with a PostgreSQL Database
- **API Documentation:** Springdoc OpenAPI (Swagger UI)
- **Security:** Spring Security with CORS configuration (optional)
- **Build Tool:** Maven (or Gradle, if you prefer)
- **Java Version:** Java 17 LTS
- **Containerization:** Docker (optional)
- **Deployment:** Render (optional)

## Getting Started

### Prerequisites

Before running the application, you will need:

- Java 17 installed
- Maven installed (or Gradle if you prefer)
- (Optional) Docker, if you plan on containerizing the app

### Installation

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/marceloakalopes/minilinkr.git
   cd minilinkr
