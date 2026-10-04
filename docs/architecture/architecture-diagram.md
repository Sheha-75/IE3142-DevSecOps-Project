# IE3142 DevSecOps Project — Architecture Diagram Specification

Use this Mermaid diagram in the technical report or convert it to a rendered image.

```mermaid
flowchart TB
    DEV[Developer<br/>Source Code / Git] -->|DF-01: git push| GH[GitHub Repository<br/>IE3142-DevSecOps-Project]

    subgraph CI["TB-02: CI/CD Trust Boundary"]
        GH -->|DF-02| GA[GitHub Actions]
        GA --> SAST[Semgrep<br/>SAST]
        GA --> SCA[OWASP Dependency-Check<br/>SCA]
        GA --> GL[Gitleaks<br/>Secrets]
        GA --> BT[Build & Test]
        BT --> DB[Docker Build]
        DB --> TR[Trivy<br/>Container Scan]
        SAST --> GATE[Security Gate]
        SCA --> GATE
        GL --> GATE
        TR --> GATE
        BT --> GATE
    end

    subgraph APP["Application / Runtime Boundary"]
        USER[Local User / Browser] -->|DF-05: HTTP| WG[WebGoat<br/>Spring Boot<br/>Port 8080]
        WG -->|DF-06: JDBC| DB1[(HSQLDB)]
        WG -->|DF-07: supporting interaction| WW[WebWolf<br/>Port 9090]
        WW -->|DF-08: database operations| DB1
    end

    GATE -->|validated build| APP

    TB1{{TB-01<br/>Developer ↔ GitHub}}
    TB3{{TB-03<br/>User ↔ WebGoat}}
    TB4{{TB-04<br/>Application ↔ Database}}
    TB5{{TB-05<br/>WebGoat ↔ WebWolf}}
```

## Diagram legend

- **TB-01:** source-code/repository trust boundary.
- **TB-02:** automated CI/CD processing boundary.
- **TB-03:** main external application attack surface.
- **TB-04:** application-to-database boundary.
- **TB-05:** component-to-component application boundary.

## Vulnerability placement

| Vulnerability | Diagram location |
|---|---|
| V01 Missing Function Level Access Control | TB-03: User → WebGoat authorization |
| V02 SQL Injection | TB-04: WebGoat → HSQLDB |
| V03 XSS | TB-03: User input → WebGoat rendering |
| V04 Insecure Deserialization | WebGoat application deserialization boundary |

> Note: The exact V04 STRIDE classification must be justified from the demonstrated data flow and impact, as required by the group work plan.
