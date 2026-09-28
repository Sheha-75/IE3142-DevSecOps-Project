# System Architecture and Data Flow

## 1. System Overview

This project uses OWASP WebGoat as the intentionally vulnerable application and WebWolf as its supporting web application component.

The application environment is executed locally using Docker. The Docker container is named `webgoat` and exposes two localhost-bound HTTP services:

- WebGoat: `127.0.0.1:8080/WebGoat`
- WebWolf: `127.0.0.1:9090/WebWolf`

The application is implemented using Java and Spring Boot with Apache Tomcat as the web server.

WebGoat uses an HSQLDB file-based database for application persistence.

The environment is intentionally kept local because WebGoat is designed for security training and contains deliberate vulnerabilities.

---

## 2. Main Components

| Component | Technology / Interface | Responsibility |
|---|---|---|
| User / Browser | Web browser | Interacts with WebGoat and WebWolf |
| Docker Host | Windows + Docker Desktop | Provides the local execution environment |
| WebGoat | Java / Spring Boot / Tomcat / HTTP 8080 | Main intentionally vulnerable training application |
| WebWolf | Java / Spring Boot / Tomcat / HTTP 9090 | Supporting web application used by WebGoat security exercises |
| HSQLDB | HSQLDB file database | Stores WebGoat application data |
| Docker Bridge Network | Docker bridge networking | Provides container networking |

---

## 3. Container Architecture

The application is executed inside a Docker container named `webgoat`.

The container is connected to Docker's bridge network.

The current container has the following network characteristics:

- Container IP: `172.17.0.2`
- Docker bridge gateway: `172.17.0.1`
- WebGoat port: `8080/tcp`
- WebWolf port: `9090/tcp`

The Docker Compose configuration binds the services to the localhost interface:

```yaml
ports:
  - "127.0.0.1:8080:8080"
  - "127.0.0.1:9090:9090"