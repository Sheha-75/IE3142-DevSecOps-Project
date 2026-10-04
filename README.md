
# V01 - Missing Function Level Access Control

## IE3142 DevOps Security

This document describes the implementation, security testing, remediation, and verification of **V01 - Missing Function Level Access Control** in the OWASP WebGoat application.

---

# 1. Vulnerability Overview

| Field | Details |
|---|---|
| Vulnerability ID | V01 |
| Vulnerability | Missing Function Level Access Control |
| STRIDE Category | Elevation of Privilege |
| Security Property | Authorization |
| Affected Endpoint | `GET /WebGoat/access-control/users` |
| Threat Actor | Authenticated non-administrator user |
| Asset | Protected user-management functionality and user-related information |
| Primary Control | Server-side authorization |
| Risk Treatment | Mitigate |

---

# 2. Application

The vulnerability was investigated in:

**OWASP WebGoat v2026.4**

Technology stack:

- Java
- Spring Boot
- Maven
- JUnit
- Spring MockMvc
- Docker
- GitHub Actions

The relevant source code is located at:

```text
webgoat-src/
```

---

# 3. Vulnerable Function

The affected endpoint is:

```text
GET /WebGoat/access-control/users
```

The endpoint provides access to user-management functionality.

The security issue was that an authenticated user without administrator privileges could directly request the protected function.

---

# 4. Threat Scenario

The relevant threat scenario is:

```text
Authenticated Non-Administrator
            |
            v
GET /WebGoat/access-control/users
            |
            v
Missing / Insufficient Authorization
            |
            v
Protected User Functionality
            |
            v
Unauthorized Access
```

The important security issue is that authentication alone must not be treated as sufficient authorization for administrator-only functionality.

---

# 5. STRIDE Classification

V01 is classified as:

```text
Elevation of Privilege
```

The vulnerability can allow a lower-privileged authenticated user to access functionality intended for a higher-privileged administrator role.

---

# 6. Risk Assessment

## Likelihood

**High**

### Justification

The vulnerability was successfully reproduced against the unmodified application.

The attack path requires an authenticated user to directly request the affected HTTP endpoint.

The vulnerable endpoint was:

```text
GET /WebGoat/access-control/users
```

The vulnerability did not require bypassing the application's authentication mechanism.

---

## Impact

**High**

### Justification

Successful exploitation allows an authenticated non-administrator user to access protected user-management functionality and user-related information intended for a higher-privileged role.

The primary security impact is unauthorized access resulting from elevation of privilege.

---

# 7. Risk Rating

A 3x3 qualitative likelihood-impact matrix is used.

| Likelihood / Impact |    Low | Medium |     High |
| ------------------- | -----: | -----: | -------: |
| Low                 |    Low |    Low |   Medium |
| Medium              |    Low | Medium |     High |
| High                | Medium |   High | Critical |

For V01:

| Factor      | Rating   |
| ----------- | -------- |
| Likelihood  | High     |
| Impact      | High     |
| Risk Rating | Critical |

Therefore:

```text
High Likelihood
       +
High Impact
       =
Critical Risk
```

Detailed risk assessment:

```text
docs/risk/v01-risk-assessment.md
```

---

# 8. Vulnerable Behaviour

Before remediation, the protected endpoint could be requested by an authenticated non-administrator user.

The vulnerable behaviour was demonstrated using the affected endpoint:

```text
GET /WebGoat/access-control/users
```

The vulnerable response was:

```text
HTTP 200 OK
```

Evidence was captured before applying the authorization fix.

Sensitive user information and credential-like values shown in screenshots must be redacted before final submission.

---

# 9. Evidence

V01 evidence is stored under:

```text
docs/evidence/vulnerabilities/vuln01/
```

Relevant evidence includes:

```text
05-before-vulnerable-behavior-redacted.png
06-before-response.png
07-branch-baseline.png
08-source-vulnerable-endpoint.png
09-source-authorization-fix.png
11-after-same-test.png
```

The final report should use the **redacted evidence** where sensitive values are visible.

---

# 10. Vulnerable Source Code

The affected implementation is:

```text
webgoat-src/src/main/java/org/owasp/webgoat/lessons/missingac/MissingFunctionACUsers.java
```

The relevant endpoint is:

```java
@GetMapping(path = {"access-control/users"})
public ModelAndView listUsers(@CurrentUsername String username) {
```

The JSON endpoint is:

```java
@GetMapping(
    path = {"access-control/users"},
    consumes = "application/json")
@ResponseBody
public ResponseEntity<List<DisplayUser>> usersService(
    @CurrentUsername String username) {
```

---

# 11. Security Fix

Server-side authorization was added to the protected endpoint.

The application retrieves the current user:

```java
var currentUser = userRepository.findByUsername(username);
```

The application then verifies administrator privileges.

For the MVC endpoint:

```java
if (currentUser == null || !currentUser.isAdmin()) {
    throw new ResponseStatusException(HttpStatus.FORBIDDEN);
}
```

For the JSON endpoint:

```java
if (currentUser == null || !currentUser.isAdmin()) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
}
```

This implements server-side role-based authorization.

---

# 12. Authorization Logic

The authorization decision can be represented as:

```text
              Request
                 |
                 v
          Identify Current User
                 |
                 v
        Does Current User Exist?
             /          \
           No            Yes
           |              |
           v              v
        HTTP 403       Is Admin?
                         /   \
                       No     Yes
                       |       |
                       v       v
                    HTTP 403  Allow
```

The protected function is therefore only available when the current user has administrator privileges.

---

# 13. Expected Secure Behaviour

After remediation, an unauthorized request to:

```text
GET /WebGoat/access-control/users
```

must return:

```text
HTTP 403 Forbidden
```

The protected user-management functionality must not be returned to an unauthorized user.

---

# 14. Regression Test

The V01 regression test is located at:

```text
webgoat-src/src/test/java/org/owasp/webgoat/lessons/missingac/MissingFunctionACUsersTest.java
```

The test verifies that the protected endpoint returns HTTP 403.

The relevant test expectation is:

```java
mockMvc
    .perform(
        MockMvcRequestBuilders.get("/access-control/users")
            .header("Content-type", "application/json"))
    .andExpect(status().isForbidden());
```

---

# 15. Regression Test Command

From the `webgoat-src` directory:

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

The test verifies that the V01 authorization behaviour remains enforced.

---

# 16. Same Test After Fix

The remediation was verified by performing the same protected endpoint request after the authorization fix.

Before fix:

```text
GET /WebGoat/access-control/users
        |
        v
HTTP 200 OK
```

After fix:

```text
GET /WebGoat/access-control/users
        |
        v
Authorization Check
        |
        v
HTTP 403 Forbidden
```

This demonstrates that the demonstrated unauthorized access path is blocked after remediation.

---

# 17. Before and After

| Stage                | Result                                  |
| -------------------- | --------------------------------------- |
| Original application | Unauthorized access demonstrated        |
| Vulnerable response  | `HTTP 200 OK`                           |
| Security issue       | Missing function-level authorization    |
| Fix                  | Server-side administrator authorization |
| Regression test      | Implemented                             |
| Post-fix response    | `HTTP 403 Forbidden`                    |
| Regression tests     | 2 passed                                |
| Build                | SUCCESS                                 |

---

# 18. Risk Mitigation

| Risk                                        | Mitigation                         |
| ------------------------------------------- | ---------------------------------- |
| Unauthorized access                         | Server-side authorization          |
| Privilege escalation                        | Administrator role verification    |
| Authorization bypass through direct request | Authorization enforced at endpoint |
| Regression                                  | Automated regression test          |
| Future code changes                         | CI/CD validation                   |

---

# 19. Control Mapping

| Security Requirement  | Implementation                | Verification                      |
| --------------------- | ----------------------------- | --------------------------------- |
| Authorization         | Server-side role verification | HTTP 403 for unauthorized request |
| Least privilege       | Administrator-only access     | `isAdmin()` check                 |
| Fail securely         | Unauthorized requests denied  | `403 Forbidden`                   |
| Regression prevention | Automated test                | `MissingFunctionACUsersTest`      |
| Continuous validation | CI/CD pipeline                | GitHub Actions                    |

---

# 20. Residual Risk

After implementation of the server-side authorization control, the demonstrated V01 attack path is blocked.

The same protected GET request now results in:

```text
HTTP 403 Forbidden
```

The automated regression test also passes:

```text
Tests run: 2
Failures: 0
Errors: 0
Skipped: 0
Build: SUCCESS
```

The residual risk for the demonstrated V01 vulnerability is assessed as:

**Low**

provided that:

* The authorization control remains enforced.
* The regression test continues to pass.
* Future changes do not remove or bypass the authorization check.

Other access-control weaknesses outside the demonstrated V01 endpoint are outside the scope of this V01 assessment.

---

# 21. SAST and CI/CD

V01 is also considered within the project's DevSecOps pipeline.

The repository CI/CD workflow is:

```text
.github/workflows/devsecops.yml
```

The pipeline includes:

```text
SAST
  |
  v
Dependency / SCA
  |
  v
Secret Scanning
  |
  v
Build and Test
  |
  v
Docker Build
  |
  v
Trivy Container Scan
  |
  v
Security Gate
```

Security validation is therefore integrated into the development and delivery process.

---

# 22. CI/CD Security Tools

| Security Area      | Tool                   |
| ------------------ | ---------------------- |
| SAST               | Semgrep                |
| Dependency / SCA   | OWASP Dependency-Check |
| Secret Scanning    | Gitleaks               |
| Container Scanning | Trivy                  |
| CI/CD              | GitHub Actions         |
| Containerization   | Docker                 |

---

# 23. Git Branch

The V01 implementation was developed on:

```text
feature/v01-access-control
```

The branch contains the V01 implementation, regression test, CI/CD work, and V01 risk documentation.

---

# 24. Relevant Files

### Source Code

```text
webgoat-src/src/main/java/org/owasp/webgoat/lessons/missingac/MissingFunctionACUsers.java
```

### Regression Test

```text
webgoat-src/src/test/java/org/owasp/webgoat/lessons/missingac/MissingFunctionACUsersTest.java
```

### Risk Assessment

```text
docs/risk/v01-risk-assessment.md
```

### Evidence

```text
docs/evidence/vulnerabilities/vuln01/
```

### CI/CD

```text
.github/workflows/devsecops.yml
```

---

# 25. V01 Workflow Summary

```text
                    V01 IDENTIFICATION
                           |
                           v
                 Missing Function-Level
                   Access Control
                           |
                           v
                    STRIDE Analysis
                           |
                           v
                 Elevation of Privilege
                           |
                           v
                    Risk Assessment
                           |
                           v
                  High Likelihood
                  High Impact
                           |
                           v
                  Critical Risk
                           |
                           v
                Vulnerable Behaviour
                           |
                           v
                    HTTP 200 OK
                           |
                           v
                 Source Code Analysis
                           |
                           v
               Server-Side Authorization
                       Implemented
                           |
                           v
                 Automated Regression Test
                           |
                           v
                  Same Request Re-tested
                           |
                           v
                    HTTP 403 Forbidden
                           |
                           v
                    Attack Path Blocked
                           |
                           v
                 Evidence + Documentation
```

---

# 26. Security Principle Demonstrated

V01 demonstrates the importance of enforcing authorization **server-side**.

Hiding a function in a user interface is not sufficient protection for a sensitive endpoint.

The server must independently determine whether the current user has permission to perform the requested operation.

The implemented control therefore follows the principle:

> **Authenticate the user, then authorize every protected function according to the user's privileges.**

---

# 27. Final Verification Checklist

* [x] V01 identified
* [x] Affected endpoint identified
* [x] STRIDE category identified
* [x] Threat actor identified
* [x] Asset identified
* [x] Likelihood assessed
* [x] Impact assessed
* [x] Risk rating documented
* [x] Vulnerable behaviour demonstrated
* [x] Before-fix evidence collected
* [x] Vulnerable source code identified
* [x] Server-side authorization implemented
* [x] Regression test implemented
* [x] Same endpoint tested after remediation
* [x] HTTP 403 verified
* [x] Regression tests passed
* [x] Risk mitigation documented
* [x] Residual risk documented
* [x] Evidence locations documented
* [x] Sensitive evidence identified for redaction

---

# 28. References

1. OWASP Foundation - OWASP WebGoat.
2. OWASP Foundation - OWASP Top 10.
3. OWASP Foundation - Application Security Verification Standard (ASVS).
4. OWASP Foundation - Threat Modeling.
5. OWASP Foundation - Secure Coding Practices.
6. GitHub Actions Documentation.
7. Semgrep Documentation.
8. OWASP Dependency-Check.
9. Gitleaks Documentation.
10. Trivy Documentation.
11. Docker Documentation.
12. IE3142 DevOps Security Continuous Assessment.

---

# V01 Summary

| Category                         | Result                                |
| -------------------------------- | ------------------------------------- |
| Vulnerability                    | Missing Function Level Access Control |
| Endpoint                         | `GET /WebGoat/access-control/users`   |
| STRIDE                           | Elevation of Privilege                |
| Likelihood                       | High                                  |
| Impact                           | High                                  |
| Risk Rating                      | Critical                              |
| Security Control                 | Server-side authorization             |
| Unauthorized Response Before Fix | `HTTP 200 OK`                         |
| Unauthorized Response After Fix  | `HTTP 403 Forbidden`                  |
| Regression Tests                 | 2                                     |
| Test Failures                    | 0                                     |
| Test Errors                      | 0                                     |
| Build                            | SUCCESS                               |
| Residual Risk                    | Low                                   |
| Branch                           | `feature/v01-access-control`          |
