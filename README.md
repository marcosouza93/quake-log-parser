# Quake Log Parser

It's a project to parse some statistics from quake 3 arena game and show them.

## Getting Started

To begin using this project, choose one of the following options to get started:
* [Download the latest release](https://github.com/marcosouza93/quake-log-parser/archive/master.zip)
* Clone the repo: `git clone https://github.com/marcosouza93/quake-log-parser.git`
* Fork the repo

### Requirements

```
JDK 1.8
Apache Maven
Docker
```

### Installing

Use the container plataform [docker](https://www.docker.com/) to start the project. Execute the following command on the source folder to generate an image from the project:

```
docker build -t labs/quake-log-parser .
```

And this commmand to start the project inside of a docker container (exposing the port number 8080):

```
docker run -d -p 8080:8080 labs/quake-log-parser:latest
```

### Executing

It was used spring actuator to expose some service information, to access it use the following URL's:

```
http://localhost:8080/actuator/info
http://localhost:8080/actuator/health
```

To call the endpoint with the parser service, use the following URL from Swagger:

```
http://localhost:8080/swagger-ui.html
```

## Running the tests

This project includes jacoco dependency, so it's possible to get information about the project coverage using the following command:

```
mvn clean test
```

At the final will be generated a report on the path "target/site/jacoco/index.html" like this one:

```
[IMAGE]
```

## Built With

* [Java 8](https://rometools.github.io/rome/) - Programming Language
* [Spring Boot](https://spring.io/projects/spring-boot) - Web Framework
* [Maven](https://maven.apache.org/) - Dependency Management
* [Jacoco](https://www.eclemma.org/jacoco/) - Java Code Coverage Library
* [Swagger](https://swagger.io/) - Webpack
* [Docker](https://www.docker.com/) - Enterprise Container Platform
* [Lombok](https://projectlombok.org/) - Java Library

## Author

* **Marco Souza** - [marcosouza93](https://github.com/marcosouza93)
