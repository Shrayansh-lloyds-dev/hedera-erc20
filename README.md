# Hedera Stablecoin Spring Boot Project

## Overview

This project is a Spring Boot-based backend service for managing stablecoin operations on the Hedera Hashgraph network. It integrates with PostgreSQL for data persistence and uses Docker for containerized services.

---

## Prerequisites

Ensure the following tools are installed on your system:

- **Java 17 or above**  
  Download from [AdoptOpenJDK](https://adoptium.net/) or [Oracle](https://www.oracle.com/java/technologies/javase-downloads.html).

- **Maven 3.8+**  
  Download from [Maven Downloads](https://maven.apache.org/download.cgi).

- **Docker**  
  Install from [Docker Official Site](https://www.docker.com/).

- **DBeaver (for database management)**  
  Download from [DBeaver.io](https://dbeaver.io/download/).

---

## Setup Instructions

### 1. Verify Java Installation

```sh
java -version
```

### 2. Verify Maven Installation

```sh
mvn -version
```

### 3. Clone the Repository

```sh
git clone <repository-url>
```

### 4. Navigate to the Project Directory

```sh
cd hedera-stablecoin-spring-boot
```

### 5. Start Docker Services

Create a `docker-compose.yml` file in your project root with the following content:

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:17.5
    container_name: edb_postgres_container
    restart: always
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
      POSTGRES_DB: postgres
    ports:
      - "5432:5432"
    volumes:
      - edb_postgres_data:/var/lib/postgresql/data

volumes:
  edb_postgres_data:
```

Then run:

```sh
docker-compose up -d
```

### 6. Configure PostgreSQL in DBeaver

1. Open **DBeaver**.
2. Click **New Database Connection**.
3. Select **PostgreSQL** from the list.
4. Enter the following connection details:
  - **Host:** localhost
  - **Port:** 5432
  - **Database:** postgres
  - **Username:** postgres
  - **Password:** postgres
5. Click **Test Connection** to verify the settings.
6. Click **Finish** to save the connection.

### 7. Clean the Project

```sh
mvn clean
```

### 8. Build the Project

```sh
mvn clean install
```

### 9. Run the Application

```sh
mvn spring-boot:run
```