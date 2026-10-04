# IE3142 DevSecOps Project — System Architecture

## 1. Scope

This document describes the architecture used for the IE3142 DevOps Security project. The canonical application is OWASP WebGoat v2026.4. The project integrates the application with a DevSecOps CI/CD security pipeline and uses one common architecture and threat model for V01–V04.

The assignment requires an architecture diagram showing application components/services, data flows, and trust boundaries, followed by a STRIDE-based threat model and risk assessment.

## 2. Verified Application Components

### WebGoat
- Spring Boot-based WebGoat application.
- Application entry point: `org/owasp/webgoat/server/StartWebGoat.java`.
- Default WebGoat port: `8080`.
- Context: `/WebGoat`.
- Contains authentication, lesson controllers, security logic, and intentionally vulnerable lessons used for controlled local security testing.

### WebWolf
- Supporting WebWolf application component.
- Default port: `9090`.
- Context: `/WebWolf`.
- Provides supporting functionality used by selected WebGoat lessons.

### HSQLDB
- WebGoat uses HSQLDB through JDBC.
- The standard application configuration uses a file-based HSQLDB database.
- Test-related configuration also defines an in-memory HSQLDB connection.
- WebWolf also has HSQLDB datasource configuration.

### Containerisation
The WebGoat source tree contains the project's Dockerfile at:

`webgoat-src/Dockerfile`

The Dockerised runtime exposes the WebGoat and WebWolf application ports used by the local lab.

## 3. CI/CD Security Architecture

Source changes are pushed to the GitHub repository and processed by GitHub Actions.

The security pipeline contains:
1. SAST using Semgrep.
2. Software Composition Analysis using OWASP Dependency-Check.
3. Secret detection using Gitleaks.
4. Application build and tests.
5. Docker image build.
6. Container/image scanning using Trivy.
7. Security-gate evaluation.

The pipeline therefore applies security checks before a change is considered acceptable, consistent with the DevSecOps principle of integrating security throughout the software delivery lifecycle.

## 4. Data Flows

| ID | Source | Destination | Data / Interaction |
|---|---|---|---|
| DF-01 | Developer | GitHub repository | Source code, configuration, commits |
| DF-02 | GitHub repository | GitHub Actions | Repository contents for build and security analysis |
| DF-03 | GitHub Actions | Build/Test | Compiled application and test execution |
| DF-04 | GitHub Actions | Docker build | Application artifact used to construct image |
| DF-05 | User/browser | WebGoat | HTTP requests, authentication/session information, lesson input |
| DF-06 | WebGoat | HSQLDB | JDBC queries and application/user data |
| DF-07 | WebGoat | WebWolf | Supporting application interactions required by selected lessons |
| DF-08 | WebWolf | HSQLDB | Database operations required by WebWolf |

## 5. Trust Boundaries

### TB-01 — Developer / GitHub boundary
Source code and configuration cross from the developer environment into the shared repository.

### TB-02 — Repository / CI boundary
Repository contents are consumed by automated build and security-analysis jobs. Integrity of the source and workflow configuration is important.

### TB-03 — User / WebGoat boundary
Untrusted HTTP requests enter the WebGoat application. This is the principal application attack surface for the four demonstrated vulnerabilities.

### TB-04 — Application / Database boundary
WebGoat and WebWolf access HSQLDB through JDBC. SQL construction and database access controls therefore affect confidentiality and integrity.

### TB-05 — WebGoat / WebWolf application boundary
The two application components exchange supporting application data/requests. Cross-component trust assumptions must be considered when analysing threats.

## 6. Security-Relevant Architecture

The architecture supports the four canonical vulnerabilities:

- **V01 — Missing Function Level Access Control:** User → WebGoat server-side authorization boundary.
- **V02 — SQL Injection:** WebGoat application → HSQLDB data-access boundary.
- **V03 — Cross-Site Scripting:** Untrusted user-controlled data → WebGoat rendering/client boundary.
- **V04 — Insecure Deserialization:** Untrusted serialized data → application deserialization boundary.

Each vulnerability must be connected to the same architecture, trust boundary, STRIDE classification, risk assessment, mitigation, code change, and CI/CD verification.

## 7. Secure Design Principles Applied

The architecture and fixes are evaluated using principles from the IE3142 secure-design material:

- Defence in Depth
- Fail Securely
- Least Privilege
- Fail-Safe Defaults
- Secure Defaults
- Minimize Attack Surface
- Separation of Duties
- Complete Mediation

For V01, **Least Privilege**, **Fail-Safe Defaults**, and especially **Complete Mediation** are directly relevant because protected functions must verify authorization rather than relying on the existence of an authenticated session.

## 8. Architecture-to-Threat-Model Chain

The project uses the following chain:

`Architecture → Data Flows → Trust Boundaries → STRIDE Threats → Risk → Vulnerability → Control → Code Fix → CI/CD Security Test → Retest`

This ensures that the architecture is not an isolated diagram: it provides the foundation for the threat model, risk matrix, secure-coding demonstrations, and automated DevSecOps controls.
