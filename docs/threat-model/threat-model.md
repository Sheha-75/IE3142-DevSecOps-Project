# DevSecOps Threat Model

## IE3142 – Building and Securing a DevSecOps Pipeline

This document defines the application-level threat model for the WebGoat-based
application used in the IE3142 DevOps Security project.

The threat model follows the STRIDE methodology and identifies realistic
security threats affecting the application, its authentication and
authorization mechanisms, input processing, data handling, and software
delivery pipeline.

The identified threats are linked to security controls, secure coding
practices, testing, and CI/CD security checks.

---

# 1. Application Scope

The application used for this project is OWASP WebGoat, a deliberately
vulnerable Java/Spring Boot web application used for security education and
secure software development.

The application contains multiple web-based security lessons and communicates
with an embedded database.

For this project, selected vulnerabilities are used to demonstrate:

- Threat modelling
- Risk assessment
- Vulnerability exploitation in a controlled local environment
- Secure coding
- Security regression testing
- Static Application Security Testing (SAST)
- Software Composition Analysis (SCA)
- Secrets scanning
- Container security scanning
- CI/CD security gates

The application is executed locally and is not intended to be exposed as a
public production service.

---

# 2. System Components

The main components considered in the threat model are:

1. WebGoat web application
2. Spring Boot application components
3. Embedded application database
4. HTTP/API endpoints
5. User authentication and authorization mechanisms
6. Docker container
7. Source-code repository
8. CI/CD pipeline
9. Security scanning tools
10. Build artifacts and Docker image

---

# 3. Trust Boundaries

The following trust boundaries are considered:

### TB-01 – User to Web Application

An external user interacts with WebGoat through HTTP requests.

The application must not trust client-side authorization decisions or
user-controlled request parameters.

### TB-02 – Web Application to Database

The application accesses user and application data through the persistence
layer.

Unauthorized application logic must not expose or modify protected data.

### TB-03 – Developer to Source Repository

Developers commit application source code, configuration, tests, Docker
definitions, and CI/CD configuration to the repository.

Sensitive information must not be committed to the repository.

### TB-04 – Source Repository to CI/CD Pipeline

The CI/CD pipeline retrieves source code and executes build and security
analysis stages.

The pipeline must prevent insecure code or artifacts from progressing when
defined security thresholds are exceeded.

### TB-05 – CI/CD Pipeline to Container Image

The application is packaged into a Docker image.

The resulting image must be scanned for known vulnerabilities before it is
accepted by the security pipeline.

---

# 4. STRIDE Threat Identification

The following application-specific threats have been identified.

| ID | Threat | STRIDE Category | Asset | Likelihood | Impact | Risk |
|---|---|---|---|---|---|---|
| V01 | Missing server-side authorization on protected user-management endpoint | Elevation of Privilege | User-management functionality and protected user information | High | High | Critical |
| V02 | Injection through insufficient validation of application input | Tampering / Information Disclosure | Application data and database | Medium | High | High |
| V03 | Cross-Site Scripting through unsafe handling of user-controlled input | Tampering / Spoofing | User sessions and application content | Medium | High | High |
| V04 | Unsafe object/data deserialization or processing | Tampering / Remote Code Execution risk | Application runtime and application data | Medium | High | High |

> V01 is the currently demonstrated vulnerability in this project.
> V02–V04 represent additional application-specific threats selected for
> the project threat model and must be supported by corresponding
> vulnerability evidence before being claimed as completed secure-coding
> demonstrations in the final report.

---

# 5. V01 – Missing Function-Level Access Control

## 5.1 Threat Description

The application contains a protected user-management endpoint:

`GET /WebGoat/access-control/users`

The vulnerability occurs when access to a privileged function is not
properly enforced on the server side.

A user who should not have administrative privileges could attempt to access
the endpoint directly instead of relying on the application's user
interface.

Hiding an administrative function in the user interface is not sufficient.
Authorization must be enforced by the server for every protected request.

---

## 5.2 STRIDE Classification

**Primary STRIDE category:**

- Elevation of Privilege

The threat allows a lower-privileged or unauthorized user to attempt to
perform functionality intended for an administrator.

---

## 5.3 Protected Asset

The affected assets include:

- User-management functionality
- User account information
- Administrative functionality
- Application authorization boundaries

---

## 5.4 Attack Scenario

An authenticated user sends a direct HTTP request to:

`GET /WebGoat/access-control/users`

instead of navigating through the normal application interface.

If server-side authorization is missing, the endpoint can return information
intended only for an administrator.

This demonstrates why authorization must be enforced at the backend rather
than relying only on frontend visibility or navigation controls.

---

## 5.5 Risk Assessment

### Likelihood

**High**

The vulnerable behaviour was reproducible against the unmodified
application by directly requesting the affected endpoint.

### Impact

**High**

Unauthorized access to protected user-management functionality can expose
information intended for a higher-privileged role and can weaken the
application's authorization boundary.

### Risk Rating

**Critical**

The project uses a 3 × 3 likelihood-impact matrix in which a High
likelihood combined with a High impact produces a Critical risk rating.

---

## 5.6 Security Control

The vulnerability was mitigated by enforcing server-side authorization.

The endpoint retrieves the current user and verifies administrative
privileges before returning protected information.

The authorization condition is:

```java
var currentUser = userRepository.findByUsername(username);

if (currentUser == null || !currentUser.isAdmin()) {
    throw new ResponseStatusException(HttpStatus.FORBIDDEN);
}
```

Unauthorized requests therefore receive:

```text
HTTP 403 Forbidden
```

The JSON endpoint applies the same authorization principle.

---

## 5.7 Regression Test

A regression test was added to verify that an unauthorized request to the
affected endpoint returns HTTP 403.

The test also verifies that the same access-control behaviour remains enforced
after the user-management operation used in the lesson.

Test:

```text
MissingFunctionACUsersTest
```

Command:

```powershell
.\mvnw.cmd -Dtest=MissingFunctionACUsersTest test
```

Expected result:

```text
Tests run: 2
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

---

# 6. V02 – Injection Through Insufficient Input Validation

## 6.1 Threat Description

Application endpoints that process user-controlled input may be vulnerable
when input is incorporated into database queries or other interpreted
operations without appropriate validation, parameterization, or encoding.

An attacker may attempt to manipulate application input so that the
application interprets the supplied data as part of an operation rather than
as ordinary data.

---

## 6.2 STRIDE Classification

Primary categories:

* Tampering
* Information Disclosure

---

## 6.3 Assets

Potentially affected assets include:

* Database records
* Application data
* Query results
* Confidential information

---

## 6.4 Threat Scenario

A malicious user supplies specially crafted input to an application field.

If the affected application logic constructs an operation using unsafe input
handling, the input may alter the intended operation.

The threat is controlled through:

* Input validation
* Parameterized queries
* Safe data-access APIs
* Secure coding review
* SAST
* Regression testing

---

## 6.5 Planned Security Control

The final implementation must demonstrate a vulnerable behaviour and then
apply an appropriate secure coding fix.

The same test or exploit used against the vulnerable implementation should
be repeated after the fix to demonstrate that the vulnerability is no longer
exploitable.

---

# 7. V03 – Cross-Site Scripting

## 7.1 Threat Description

Cross-Site Scripting (XSS) can occur when attacker-controlled input is
returned to a user's browser without appropriate output encoding or other
context-specific protection.

If successful, attacker-controlled content may be interpreted by the
browser as active web content.

---

## 7.2 STRIDE Classification

Primary categories:

* Spoofing
* Tampering

---

## 7.3 Assets

Potentially affected assets include:

* User sessions
* Browser-side application context
* Application content
* User trust

---

## 7.4 Threat Scenario

A user submits maliciously crafted input through an application field.

If the application subsequently renders the value without appropriate
output encoding, the browser may interpret the supplied content as executable
web content.

The risk is reduced through:

* Context-aware output encoding
* Input validation where appropriate
* Secure templating
* Content Security Policy where applicable
* Security testing
* SAST and regression tests

---

## 7.5 Planned Security Control

The final secure-coding demonstration must show:

1. Vulnerable behaviour
2. Evidence of the vulnerability
3. Source-code location
4. Security fix
5. The same test after the fix
6. Evidence that the original behaviour is blocked

---

# 8. V04 – Unsafe Deserialization / Data Processing

## 8.1 Threat Description

Unsafe deserialization or processing of untrusted serialized data can create
security risks when application-controlled objects or data structures are
constructed from attacker-controlled input without adequate validation.

Depending on the affected implementation, unsafe processing may result in
tampering with application state or more serious application-level
consequences.

---

## 8.2 STRIDE Classification

Primary categories:

* Tampering
* Elevation of Privilege

---

## 8.3 Assets

Potentially affected assets include:

* Application runtime
* Application data
* User privileges
* Server-side application state

---

## 8.4 Threat Scenario

An attacker supplies specially crafted serialized or structured data to an
application component that does not sufficiently restrict or validate the
input.

If unsafe processing occurs, application behaviour may be manipulated beyond
the intended operation.

---

## 8.5 Planned Security Control

The secure implementation should use appropriate data validation and safe
deserialization practices.

Security controls should include:

* Strict input validation
* Safe object handling
* Restricted accepted data types
* Secure coding practices
* Automated regression testing
* SAST
* Dependency/SCA scanning

The final report must only claim this threat as a demonstrated
vulnerability after corresponding evidence has been collected.

---

# 9. Risk Assessment Matrix

The project uses a 3 × 3 likelihood-impact model.

| Impact \ Likelihood |    Low | Medium |     High |
| ------------------- | -----: | -----: | -------: |
| **Low**             |    Low |    Low |   Medium |
| **Medium**          |    Low | Medium |     High |
| **High**            | Medium |   High | Critical |

### Rating Definitions

#### Low

The threat has limited security consequences and/or is difficult to
realize.

#### Medium

The threat has a meaningful security consequence or a moderate likelihood
of occurrence.

#### High

The threat has significant security consequences and/or can reasonably be
exploited.

#### Critical

The threat combines high likelihood with high impact under the selected
risk matrix.

---

# 10. Threat-to-Control Mapping

| Threat                                      | Security Control                         | Implementation / Evidence                |
| ------------------------------------------- | ---------------------------------------- | ---------------------------------------- |
| V01 – Missing Function-Level Access Control | Server-side role authorization           | `MissingFunctionACUsers.java`            |
| V01 – Missing Function-Level Access Control | Authorization regression test            | `MissingFunctionACUsersTest.java`        |
| V02 – Injection                             | Input validation and safe query handling | Vulnerability-specific secure coding fix |
| V02 – Injection                             | SAST / regression testing                | CI/CD security pipeline                  |
| V03 – XSS                                   | Context-aware output encoding            | Vulnerability-specific secure coding fix |
| V03 – XSS                                   | Regression testing                       | Vulnerability-specific test              |
| V04 – Unsafe Deserialization                | Safe data/object handling                | Vulnerability-specific secure coding fix |
| V04 – Unsafe Deserialization                | SAST / dependency scanning               | CI/CD security pipeline                  |
| Source-code secrets                         | Secrets scanning                         | Gitleaks                                 |
| Vulnerable dependencies                     | Software Composition Analysis            | OWASP Dependency-Check                   |
| Vulnerable container packages               | Container scanning                       | Trivy                                    |
| Insecure source code patterns               | Static analysis                          | Semgrep                                  |

---

# 11. DevSecOps Security Controls

The threat model is connected to the project's CI/CD pipeline.

The pipeline includes security checks for:

### SAST

Static Application Security Testing is used to identify security issues in
source code before deployment.

Tool:

```text
Semgrep
```

### Software Composition Analysis

Third-party dependencies are analysed for known vulnerabilities.

Tool:

```text
OWASP Dependency-Check
```

### Secrets Scanning

The repository is scanned for accidentally committed secrets and sensitive
values.

Tool:

```text
Gitleaks
```

### Container Security

The resulting container image is scanned for known vulnerabilities.

Tool:

```text
Trivy
```

### Security Gate

The CI/CD workflow contains security thresholds so that defined high-severity
security findings can cause the pipeline to fail.

This demonstrates that security checks are integrated into the software
delivery process rather than being performed only at the end of development.

---

# 12. Threat Model and Secure Development Lifecycle

The identified threats are considered throughout the development lifecycle.

```text
Threat Identification
        |
        v
Risk Assessment
        |
        v
Secure Coding
        |
        v
Security Testing
        |
        v
SAST / SCA / Secrets Scan
        |
        v
Docker Image Build
        |
        v
Trivy Container Scan
        |
        v
Security Gate
        |
        v
Secure Build Result
```

The same security requirements are therefore considered during development,
testing, and CI/CD rather than relying only on manual review.

---

# 13. V01 Evidence

The following evidence is maintained for V01:

* Vulnerable behaviour before the fix
* HTTP response demonstrating the vulnerable endpoint
* Vulnerable source-code location
* Authorization fix
* Post-fix source code
* Same endpoint tested after the fix
* HTTP 403 response after the fix
* Automated regression test
* CI/CD security pipeline evidence

Sensitive information contained in screenshots must be redacted before
inclusion in the final report.

---

# 14. Residual Risk

The V01 fix addresses the demonstrated access-control weakness for the
affected endpoint.

However, fixing one endpoint does not establish that every authorization
boundary throughout the application is secure.

Other access-control paths and unrelated vulnerabilities remain outside the
specific V01 scope and require separate assessment.

Residual risk should therefore be considered in the context of the specific
demonstrated vulnerability rather than the entire application.

---

# 15. Threat Model Limitations

This threat model focuses on the security requirements relevant to the
IE3142 DevSecOps project.

The application is an intentionally vulnerable educational application.
Therefore, not every vulnerability present in WebGoat is within the scope of
this project.

Only vulnerabilities for which the project team collects appropriate
evidence should be claimed as completed secure-coding demonstrations.

The threat model may be updated as additional vulnerability demonstrations,
security fixes, tests, and CI/CD evidence are completed.

---

# 16. Security Principles Demonstrated

The threat model demonstrates the following security principles:

* Server-side authorization
* Least privilege
* Defense in depth
* Secure input handling
* Secure output handling
* Security testing
* Automated regression testing
* Continuous security validation
* Security-by-design
* Shift-left security
* Security gates in CI/CD
* Container security
* Protection against accidental secret disclosure

---

# 17. Final Threat Model Summary

| ID  | STRIDE                             | Threat                                          | Current Status                           |
| --- | ---------------------------------- | ----------------------------------------------- | ---------------------------------------- |
| V01 | Elevation of Privilege             | Missing Function-Level Access Control           | Demonstrated and fixed                   |
| V02 | Tampering / Information Disclosure | Injection through insufficient input validation | Threat identified; evidence/fix required |
| V03 | Spoofing / Tampering               | Cross-Site Scripting                            | Threat identified; evidence/fix required |
| V04 | Tampering / Elevation of Privilege | Unsafe deserialization/data processing          | Threat identified; evidence/fix required |

The threat model provides the security foundation for the project's risk
assessment, secure coding demonstrations, regression testing, and DevSecOps
pipeline controls.
