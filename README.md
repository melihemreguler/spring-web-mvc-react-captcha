# BM470 Captcha Project

A Spring-based web application for handling CAPTCHA functionality, built with modern Java technologies.

## Project Overview

This project is a web application that implements CAPTCHA functionality using Spring Framework 6.2.3. It's designed to provide secure user verification through CAPTCHA challenges.

## Technology Stack

- **Framework**: Spring Framework 6.2.3
- **Database**: 
  - MySQL (Driver: 9.1.0)
  - PostgreSQL (Driver: 42.7.5)
- **ORM**: Hibernate 6.6.9.Final
- **Connection Pool**: C3P0 0.10.2
- **Logging**: 
  - SLF4J 2.0.16
  - Log4j2 2.24.3
- **Build Tool**: Maven
- **Java Version**: Compatible with Jakarta EE 6.0.0
- **Additional Libraries**:
  - Spring Security Core 6.4.4
  - Lombok 1.18.36
  - JSTL 3.0.0
  - JSON-lib 2.4

## Project Structure

```
bm470-captcha/
├── src/
│   ├── main/
│   │   ├── java/        # Java source files
│   │   ├── resources/   # Configuration files
│   │   └── webapp/      # Web application files
│   └── test/            # Test files
├── pom.xml              # Maven configuration
└── README.md           # Project documentation
```

## Prerequisites

- Java Development Kit (JDK) compatible with Jakarta EE 6.0.0
- Maven 3.x
- PostgreSQL database
- Web server (e.g., Tomcat) compatible with Jakarta EE 6.0.0

## Building the Project

1. Clone the repository
2. Navigate to the project directory
3. Run Maven build:
   ```bash
   mvn clean install
   ```

## Configuration

The project uses Spring Framework for configuration. Make sure to configure the following:

1. Database connection properties
2. Logging configuration

## Features

- CAPTCHA generation and validation
- Database persistence with Hibernate
- RESTful API endpoints
- Web interface for CAPTCHA interaction

## Dependencies

The project uses Maven for dependency management. All dependencies are listed in the `pom.xml` file.

## Testing

The project includes JUnit 5 test framework. Tests can be run using:

```bash
mvn test
```
