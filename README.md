# IE3142 DevSecOps - Building and Securing a DevSecOps Pipeline

## 1. Project Overview

This project was developed for the **IE3142 - DevOps Security** module as a group project titled:

> **Building and Securing a DevSecOps Pipeline**

The project demonstrates how security can be integrated throughout the Software Development Life Cycle (SDLC) using a practical **DevSecOps approach**.

The project uses **OWASP WebGoat** as the intentionally vulnerable open-source application and demonstrates the complete security lifecycle:

- Application and architecture analysis
- Containerisation
- Trust-boundary identification
- STRIDE threat modelling
- Risk assessment
- Vulnerability identification
- Controlled vulnerability demonstration
- Secure coding remediation
- Regression testing
- Static Application Security Testing (SAST)
- Software Composition Analysis (SCA)
- Secret scanning
- Container image scanning
- CI/CD security gates
- Security-gate failure handling
- Secrets management
- Security evidence collection
- Technical documentation

The project is designed to demonstrate that security is not treated only as a final testing activity. Security controls are integrated into development, testing, build, and delivery stages.

---

# 2. Assignment

**Module:** IE3142 - DevOps Security  
**Academic Year:** 2026  
**Semester:** Year 3 Semester 1  
**Project:** Building and Securing a DevSecOps Pipeline  
**Application:** OWASP WebGoat  
**Repository:** IE3142-DevSecOps-Project

The project follows the requirements defined in the IE3142 DevOps Security continuous assessment.

---

# 3. Project Objectives

The main objectives of this project are to:

1. Select an appropriate open-source application.
2. Run the application locally.
3. Understand the application architecture and communication paths.
4. Identify trust boundaries.
5. Containerise the application.
6. Develop an application-specific STRIDE threat model.
7. Identify realistic security threats.
8. Assess threat likelihood and impact.
9. Map threats to concrete security controls.
10. Demonstrate selected vulnerabilities against the original application.
11. Capture evidence of vulnerable behaviour.
12. Identify the vulnerable source-code locations.
13. Implement secure coding fixes.
14. Re-run the same exploit after remediation.
15. Demonstrate that the attack path is blocked.
16. Implement automated regression tests.
17. Run SAST before and after remediation where applicable.
18. Build a GitHub Actions CI/CD pipeline.
19. Integrate SAST, dependency scanning, secret scanning, and container scanning.
20. Implement security gates.
21. Demonstrate a genuine security-gate failure.
22. Apply secure secrets-management practices.
23. Maintain clear project documentation and evidence.
24. Demonstrate understanding of the complete DevSecOps lifecycle.

---

# 4. Selected Application

## OWASP WebGoat

The project uses **OWASP WebGoat**, an intentionally vulnerable educational web application designed for learning and demonstrating web application security vulnerabilities.

WebGoat is suitable for this project because it provides realistic security lessons that can be connected to:

- Secure coding
- Authentication and authorization
- Input validation
- Access control
- Injection
- Application security testing
- Security remediation

### Application Information

| Property | Details |
|---|---|
| Application | OWASP WebGoat |
| Version | v2026.4 |
| Primary Language | Java |
| Framework | Spring Boot |
| Build Tool | Maven |
| Containerization | Docker |
| CI/CD | GitHub Actions |
| Execution Environment | Local / controlled environment |

---

# 5. Overall DevSecOps Flow

The complete project follows the lifecycle below:

```text
                         PROJECT START
                              |
                              v
                  Select Open-Source Application
                              |
                              v
                    Understand Architecture
                              |
                              v
                       Containerisation
                              |
                              v
                     Threat Modelling
                         (STRIDE)
                              |
                              v
                      Risk Assessment
                              |
                              v
                  Identify Vulnerabilities
                              |
                              v
             Demonstrate Vulnerability Locally
                              |
                              v
                     Capture Evidence
                              |
                              v
                    Identify Source Code
                              |
                              v
                     Secure Coding Fix
                              |
                              v
                    Automated Regression Test
                              |
                              v
                 Re-run Same Exploit / Test
                              |
                              v
                     Verify Attack Blocked
                              |
                              v
                  SAST Before / After Analysis
                              |
                              v
                     CI/CD Integration
                              |
                              v
             +----------------+----------------+
             |                |                |
             v                v                v
           SAST             SCA          Secret Scanning
             |                |                |
             +----------------+----------------+
                              |
                              v
                       Build and Test
                              |
                              v
                        Docker Build
                              |
                              v
                       Trivy Scan
                              |
                              v
                       Security Gate
                              |
                    +---------+---------+
                    |                   |
                  PASS                 FAIL
                    |                   |
                    v                   v
             Continue Pipeline       Block / Fail

6. Architecture

The project analyses the application's components, communication paths, data flows, and trust boundaries.

At a high level:

                    User / Browser
                          |
                          | HTTP
                          v
                 +-------------------+
                 |     WebGoat       |
                 | Java / Spring Boot|
                 |                   |
                 | Security Lessons  |
                 | Web Endpoints     |
                 | Application Logic |
                 +---------+---------+
                           |
                           v
                    Embedded Database

The project architecture documentation is maintained under:

docs/architecture/

The architecture documentation covers:

Application components
Component relationships
Data flows
External/internal boundaries
Trust boundaries
Container boundaries
Security-relevant communication paths
7. Trust Boundaries

The project considers security boundaries between:

User/browser and application
Application and internal data storage
Host system and application container
Source repository and CI/CD environment
CI/CD pipeline and external security databases/services
Application source code and third-party dependencies

Trust boundaries are considered during threat modelling and risk assessment.

8. Containerisation

The project contains Docker configuration:

docker/
└── Dockerfile

docker-compose.yml

The Dockerfile packages the WebGoat application into a Java runtime container.

The application exposes:

8080
9090

Containerisation provides a reproducible application environment and supports the container-security stage of the DevSecOps pipeline.

The final containerisation configuration is maintained in the repository together with the application source code.

9. Technology Stack
Area	Technology
Application	OWASP WebGoat
Application Version	v2026.4
Programming Language	Java
Java Runtime	Eclipse Temurin JDK 25
Build Tool	Apache Maven
Source Control	Git
Repository	GitHub
Containerization	Docker
Container Orchestration for Local Setup	Docker Compose
CI/CD	GitHub Actions
SAST	Semgrep
SCA / Dependency Scanning	OWASP Dependency-Check
Secret Scanning	Gitleaks
Container Scanning	Trivy
10. Repository Structure
IE3142-DevSecOps-Project/
│
├── .github/
│   ├── workflows/
│   │   └── devsecops.yml
│   │
│   └── dependabot.yml
│
├── docker/
│   └── Dockerfile
│
├── docs/
│   ├── architecture/
│   │
│   ├── evidence/
│   │   └── vulnerabilities/
│   │
│   ├── risk/
│   │
│   └── threat-model/
│
├── webgoat-src/
│   ├── src/
│   │   ├── main/
│   │   └── test/
│   │
│   └── pom.xml
│
├── docker-compose.yml
├── README.md
└── .gitignore
11. Threat Modelling

Threat modelling is performed using the STRIDE methodology.

STRIDE	Threat
S	Spoofing
T	Tampering
R	Repudiation
I	Information Disclosure
D	Denial of Service
E	Elevation of Privilege

Threats are identified based on the actual application architecture, functionality, endpoints, assets, and trust boundaries.

The threat model documentation is maintained under:

docs/threat-model/

Each threat should identify:

Threat ID
Threat description
Threat actor
Affected component
Affected asset
STRIDE category
Attack path
Security impact
Existing or proposed control
Verification method
12. Risk Assessment

The project uses a qualitative likelihood-impact approach.

A 3x3 matrix is used:

Likelihood / Impact	Low	Medium	High
Low	Low	Low	Medium
Medium	Low	Medium	High
High	Medium	High	Critical

Risk assessment considers:

Likelihood
Impact
Resulting risk rating
Security control
Residual risk

Risk documentation is maintained under:

docs/risk/
13. Threat-to-Control Mapping

Each identified threat is mapped to a concrete mitigation.

The mapping follows:

Threat
  |
  v
Risk
  |
  v
Security Control
  |
  v
Source Code / Configuration
  |
  v
Automated Test
  |
  v
CI/CD Security Check

This allows the project to demonstrate how a threat identified during threat modelling is connected to an actual technical control.

14. Secure Coding Workflow

The secure coding process follows:

Original Application
        |
        v
Identify Vulnerability
        |
        v
Demonstrate Exploit
        |
        v
Capture Before Evidence
        |
        v
Locate Vulnerable Code
        |
        v
Implement Fix
        |
        v
Run Regression Test
        |
        v
Re-run Same Exploit
        |
        v
Confirm Exploit is Blocked
        |
        v
Document After Evidence

A fix is not considered fully demonstrated simply because the source code was changed.

The project verifies the fix by re-running the same attack path or equivalent regression test.

15. Vulnerability Portfolio

The project contains multiple application-specific vulnerability demonstrations as required by the assignment.

Each vulnerability follows the same structure:

Vulnerability identification
STRIDE classification
Risk assessment
Original vulnerable behaviour
Exploit evidence
Vulnerable source-code evidence
Secure coding fix
Same exploit after remediation
Regression test
SAST before/after evidence
Risk mitigation
Residual risk

Detailed evidence is stored under:

docs/evidence/
16. V01 - Missing Function Level Access Control
Vulnerability

Missing Function Level Access Control

Affected endpoint:

GET /WebGoat/access-control/users

STRIDE category:

Elevation of Privilege

Security property:

Authorization

Threat actor:

Authenticated non-administrator user

Primary control:

Server-side authorization
V01 Attack Scenario

An authenticated non-administrator user could directly request the protected user-management endpoint.

The vulnerable flow was:

Authenticated User
        |
        v
GET /WebGoat/access-control/users
        |
        v
Insufficient Authorization
        |
        v
HTTP 200 OK
        |
        v
Protected Functionality / Information
V01 Security Fix

Server-side authorization was implemented.

The application retrieves the current user and verifies administrator privileges before allowing access.

The authorization logic is based on:

var currentUser = userRepository.findByUsername(username);

if (currentUser == null || !currentUser.isAdmin()) {
    throw new ResponseStatusException(HttpStatus.FORBIDDEN);
}

The JSON endpoint also verifies authorization:

var currentUser = userRepository.findByUsername(username);

if (currentUser == null || !currentUser.isAdmin()) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
}

Unauthorized requests therefore return:

HTTP 403 Forbidden
V01 Regression Test

The regression test is located at:

webgoat-src/src/test/java/org/owasp/webgoat/lessons/missingac/MissingFunctionACUsersTest.java

The test verifies that the protected endpoint returns HTTP 403 for the unauthorized request.

Run:

cd webgoat-src
.\mvnw.cmd -Dtest=MissingFunctionACUsersTest test

Expected result:

Tests run: 2
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS

Detailed V01 risk documentation:

docs/risk/v01-risk-assessment.md
17. Additional Vulnerabilities

The remaining vulnerability demonstrations are documented using the same exploit-and-fix methodology.

V02

Document the actual group-selected vulnerability here.

Required information:

Vulnerability name
Affected endpoint/function
STRIDE category
Threat actor
Asset
Risk rating
Before-fix exploit
Before-fix evidence
Vulnerable source code
Secure coding fix
Same exploit after fix
Regression test
SAST before/after result
Residual risk
V03

Document the actual group-selected vulnerability here.

Required information:

Vulnerability name
Affected endpoint/function
STRIDE category
Threat actor
Asset
Risk rating
Before-fix exploit
Before-fix evidence
Vulnerable source code
Secure coding fix
Same exploit after fix
Regression test
SAST before/after result
Residual risk
V04

Document the actual group-selected vulnerability here.

Required information:

Vulnerability name
Affected endpoint/function
STRIDE category
Threat actor
Asset
Risk rating
Before-fix exploit
Before-fix evidence
Vulnerable source code
Secure coding fix
Same exploit after fix
Regression test
SAST before/after result
Residual risk

V02-V04 should only be populated with vulnerabilities that the group has actually demonstrated and verified.

18. SAST - Static Application Security Testing

The CI/CD pipeline uses Semgrep for Static Application Security Testing.

The SAST workflow is:

Source Code
    |
    v
Semgrep
    |
    v
Security Findings
    |
    v
Secure Coding Fix
    |
    v
Semgrep Again
    |
    v
Before / After Comparison

SAST evidence should demonstrate:

Scan performed before remediation
Relevant finding
Finding count or result
Secure coding change
Scan performed after remediation
Finding reduction or resolution where applicable
19. Software Composition Analysis

The project uses:

OWASP Dependency-Check

Purpose:

Identify vulnerable third-party dependencies.
Compare dependency versions against known vulnerability information.
Apply the configured severity threshold.
Prevent sufficiently severe dependency findings from silently passing the security pipeline.

The dependency scan forms part of the CI/CD security gates.

20. Secret Scanning

The project uses:

Gitleaks

Gitleaks is used to identify potentially committed secrets and credential-like values.

Examples of information that must never be committed include:

Passwords
API Keys
Access Tokens
Private Keys
Database Credentials
Credential-bearing Connection Strings

Secrets required by the CI/CD workflow should be stored using appropriate GitHub encrypted secrets rather than hardcoded in source code.

21. Container Security

The project uses:

Trivy

Trivy scans the generated container image for known vulnerabilities.

The project pipeline is configured to treat HIGH and CRITICAL findings as security-gate failures according to the configured workflow.

The purpose is to prevent a container containing sufficiently severe known vulnerabilities from silently passing the security pipeline.

22. CI/CD Pipeline

The CI/CD workflow is:

.github/workflows/devsecops.yml

The pipeline is triggered by repository activity according to the workflow configuration.

The overall pipeline is:

                    Git Push
                       |
                       v
                GitHub Actions
                       |
                       v
                  Checkout
                       |
                       v
              +----------------+
              |     Semgrep    |
              |      SAST      |
              +-------+--------+
                      |
                      v
              +----------------+
              | Dependency     |
              | Check / SCA    |
              +-------+--------+
                      |
                      v
              +----------------+
              |    Gitleaks    |
              | Secret Scanning|
              +-------+--------+
                      |
                      v
              +----------------+
              | Build / Tests  |
              +-------+--------+
                      |
                      v
              +----------------+
              | Docker Build   |
              +-------+--------+
                      |
                      v
              +----------------+
              |     Trivy      |
              | Image Scanning |
              +-------+--------+
                      |
                      v
              +----------------+
              | Security Gate  |
              +-------+--------+
                      |
                +-----+-----+
                |           |
               PASS        FAIL
                |           |
                v           v
             Continue     Block
23. Four Mandatory Security Controls

The pipeline integrates the four required security controls.

Gate 1 - SAST

Tool: Semgrep

Purpose:

Detect source-code security weaknesses.
Provide automated source analysis.
Support secure coding verification.
Gate 2 - Dependency / SCA

Tool: OWASP Dependency-Check

Purpose:

Detect vulnerable third-party dependencies.
Apply a configured severity threshold.
Prevent sufficiently severe dependency issues from passing silently.
Gate 3 - Secret Scanning

Tool: Gitleaks

Purpose:

Detect accidentally committed secrets.
Detect credential-like information.
Support repository security.
Gate 4 - Container Scanning

Tool: Trivy

Purpose:

Scan the built Docker image.
Identify known vulnerabilities.
Fail the configured security check when HIGH/CRITICAL findings are detected according to the workflow configuration.
24. Security Gate Failure

A major DevSecOps requirement is to demonstrate that the pipeline can genuinely fail when a security threshold is exceeded.

The intended behaviour is:

Security Finding
       |
       v
Severity Above Threshold
       |
       v
Security Tool Fails
       |
       v
Security Gate Fails
       |
       v
Pipeline Does Not Pass

A red pipeline in this situation does not necessarily represent a broken pipeline.

If the security tool correctly detects a vulnerability above the configured threshold and causes the security gate to fail, the failure demonstrates that the security control is working as designed.

The report should clearly distinguish between:

A genuine security finding causing an intentional gate failure
A technical/configuration failure of the pipeline itself
25. Secrets Management

The project follows the principle:

Secrets should not be stored directly in source code.

Sensitive configuration should be supplied through appropriate environment or CI/CD secret mechanisms.

For GitHub Actions, sensitive values should be stored using:

GitHub Actions Encrypted Secrets

The repository must not contain:

Passwords
API keys
Access tokens
Private keys
Database passwords
Credential-bearing connection strings

Sensitive values shown during testing should be redacted from screenshots and documentation.

26. Docker and Local Execution
Requirements

Recommended development environment:

Java 25
Maven Wrapper
Docker
Docker Compose
Git

Check Java:

java -version

Check Maven:

.\webgoat-src\mvnw.cmd -version

Check Docker:

docker --version

Check Docker Compose:

docker compose version
27. Build the Application

From the repository root:

cd webgoat-src

Build the application:

.\mvnw.cmd clean package

Run the complete test suite:

.\mvnw.cmd test
28. Run the V01 Regression Test
.\mvnw.cmd -Dtest=MissingFunctionACUsersTest test

Expected:

Tests run: 2
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
29. Docker Build

From the project root:

docker build -f docker/Dockerfile -t ie3142-webgoat .

The Docker image can then be inspected and scanned using the project's container-security workflow.

30. Docker Compose

The repository contains:

docker-compose.yml

Docker Compose provides the local application runtime configuration.

The final Compose configuration should be verified before submission to ensure that it satisfies the assignment requirements for local/offline execution and reproducible startup.

31. Evidence Management

Security evidence is stored under:

docs/evidence/

Evidence should demonstrate the complete security lifecycle.

Vulnerability Evidence
Vulnerable application state
Attack request/input
Vulnerable response
Relevant source code
Remediation Evidence
Security fix
Updated source code
Same attack re-attempt
Blocked attack
Regression test
CI/CD Evidence
Successful pipeline
SAST result
Dependency scan result
Gitleaks result
Docker build
Trivy result
Deliberate security-gate failure
Final pipeline state
32. Evidence Redaction

Security-testing screenshots and logs may contain sensitive information.

Before submission, review evidence for:

Passwords
Tokens
API keys
Hashes
Session identifiers
Private keys
Credential-like values
Personal information

Sensitive values must be redacted before inclusion in the final report or public repository.

33. Development Workflow

The recommended group development workflow is:

Issue / Requirement
        |
        v
Create / Modify Code
        |
        v
Local Build
        |
        v
Local Tests
        |
        v
Security Testing
        |
        v
Evidence Collection
        |
        v
Git Commit
        |
        v
Push to GitHub
        |
        v
GitHub Actions
        |
        v
Security Gates
        |
        v
Review Results
        |
        v
Fix Findings
        |
        v
Repeat
34. Git Branching

The project uses Git branches for development.

The V01 implementation is maintained on:

feature/v01-access-control

Meaningful commit messages should be used to maintain a clear project history.

Examples:

fix: enforce authorization on access control users endpoint
test: update access control expectations
docs: complete V01 risk assessment
ci: add DevSecOps security pipeline
ci: add Docker build definition

The final repository should contain meaningful contributions from the project members as required by the assignment.

35. Documentation Structure
Documentation	Location
Architecture	docs/architecture/
Threat Model	docs/threat-model/
Risk Assessment	docs/risk/
Vulnerability Evidence	docs/evidence/
CI/CD Workflow	.github/workflows/devsecops.yml
Dockerfile	docker/Dockerfile
Docker Compose	docker-compose.yml
Application Source	webgoat-src/
Project README	README.md
36. DevSecOps Security Principles Demonstrated
Shift Left Security

Security testing is introduced early in the development lifecycle.

Security as Code

Security checks are defined in the CI/CD workflow.

Automated Security

Security controls are automatically executed rather than relying only on manual review.

Continuous Validation

Security checks are repeated when repository changes trigger the pipeline.

Secure Coding

Vulnerabilities are demonstrated, remediated, and retested.

Least Privilege

Protected functionality is restricted according to authorization requirements.

Defense in Depth

Multiple security controls are applied across:

Source code
Dependencies
Secrets
Containers
CI/CD
Fail Securely

Security gates can prevent an insecure build from passing.

37. Academic Integrity

This repository is part of an academic group project.

All project members are responsible for understanding the work submitted and being able to explain the implementation and security decisions during the viva.

External sources, tools, documentation, and code should be appropriately acknowledged.

38. AI Usage Disclosure

AI-assisted tools may be used as supporting tools during the development and documentation process.

For this project, ChatGPT was used as an AI-assisted support tool.

AI assistance was used for activities such as:

Understanding DevSecOps concepts
Understanding security concepts
Troubleshooting development issues
Debugging assistance
Reviewing implementation approaches
Supporting documentation structure
Assisting with technical explanations
Assisting with project planning

AI-generated suggestions were reviewed, adapted, tested, and verified by the project group before being used.

The project group remains responsible for:

Final source code
Security decisions
Vulnerability demonstrations
Testing
Evidence
Technical analysis
Documentation
Final submission

AI tools were not treated as authoritative security sources. Technical claims and implementation decisions were verified against the actual project implementation and relevant technical documentation.

AI usage will also be disclosed in the Individual Contribution Statement as required by the assignment.

39. Individual and Group Contributions

The project is developed collaboratively by the group members.

Each member is responsible for documenting their actual contribution in the final contribution statement.

Examples of contribution areas include:

Application setup
Architecture
Threat modelling
Vulnerability research
Secure coding
Regression testing
Docker
CI/CD
SAST
SCA
Secret scanning
Trivy
Evidence collection
Documentation
Report preparation

Contributions should reflect the actual work performed by each member.

40. Technical Report

The technical report covers the project from architecture through security validation.

The report includes:

Executive Summary
System Overview
Architecture
Technology Stack
Containerisation
Threat Model
Risk Assessment
Threat-to-Control Mapping
Vulnerability Demonstrations
Secure Coding Fixes
Before/After SAST Results
CI/CD Pipeline
SAST Gate
SCA Gate
Secrets Gate
Trivy Container Gate
Security-Gate Failure Evidence
Secrets Management
Industry Trends / DevSecOps Practices
Reflection
Individual Contributions
AI Disclosure
References
41. Viva Preparation

Each group member should be able to explain:

Application
Why WebGoat was selected
Application architecture
Components
Communication paths
Trust boundaries
Threat Model
STRIDE
Identified threats
Threat actors
Assets
Risk ratings
Security controls
Vulnerabilities

For each vulnerability:

What was vulnerable?
        |
Why was it vulnerable?
        |
How was it demonstrated?
        |
What evidence was collected?
        |
What was changed?
        |
Why does the fix work?
        |
How was the fix tested?
        |
Was the same exploit blocked?
CI/CD

Each member should understand:

Semgrep
OWASP Dependency-Check
Gitleaks
Docker
Trivy
Security gates
Failed pipeline
Successful pipeline
Why a deliberate security failure is useful
42. Final Submission Checklist

Before submission, verify:

Application
 Application builds successfully
 Application runs locally
 Docker configuration works
 Docker Compose configuration is verified
Architecture
 Architecture diagram exists
 Components are identified
 Data flows are identified
 Trust boundaries are identified
Threat Model
 STRIDE applied
 At least four realistic application-specific threats documented
 Likelihood assessed
 Impact assessed
 Risk matrix included
 Threat-to-control mapping completed
Secure Coding
 At least four vulnerabilities demonstrated
 Before-fix evidence captured
 Vulnerable source code identified
 Fix implemented
 Same exploit/test executed after fix
 After-fix evidence captured
 Regression tests implemented
 SAST before/after evidence collected
CI/CD
 GitHub Actions workflow exists
 SAST configured
 Dependency/SCA scanning configured
 Secret scanning configured
 Trivy configured
 Security gate configured
 Genuine security-gate failure demonstrated
 Failure evidence captured
 Successful pipeline evidence captured
Secrets
 No credentials committed
 No API keys committed
 No connection strings containing secrets committed
 GitHub encrypted secrets used where required
 Sensitive screenshots redacted
 Sensitive logs reviewed
Repository
 Source code included
 Dockerfile included
 Docker Compose included
 CI/CD workflow included
 README completed
 Meaningful commit history
 Four-member contribution history
 No unnecessary files or secrets
 Working tree clean before final submission
Documentation
 Technical report completed
 Evidence referenced
 AI usage disclosed
 Individual contributions documented
 References included
 Ethical Clearance completed
43. Final DevSecOps Lifecycle
                         SOURCE CODE
                              |
                              v
                     APPLICATION SETUP
                              |
                              v
                       ARCHITECTURE
                              |
                              v
                     TRUST BOUNDARIES
                              |
                              v
                     STRIDE THREAT MODEL
                              |
                              v
                     RISK ASSESSMENT
                              |
                              v
                  VULNERABILITY IDENTIFICATION
                              |
                              v
                    BEFORE-FIX EXPLOIT
                              |
                              v
                       EVIDENCE
                              |
                              v
                     SECURE CODING FIX
                              |
                              v
                    REGRESSION TEST
                              |
                              v
                 SAME EXPLOIT AFTER FIX
                              |
                              v
                     SAST VERIFICATION
                              |
                              v
                       CI/CD PIPELINE
                              |
          +-------------------+-------------------+
          |                   |                   |
          v                   v                   v
        SAST                SCA              Gitleaks
          |                   |                   |
          +-------------------+-------------------+
                              |
                              v
                         DOCKER BUILD
                              |
                              v
                           TRIVY
                              |
                              v
                      SECURITY GATE
                              |
                   +----------+----------+
                   |                     |
                  PASS                  FAIL
                   |                     |
                   v                     v
             SECURE DELIVERY       BUILD BLOCKED
44. Conclusion

This project demonstrates a practical DevSecOps security lifecycle in which security is integrated throughout software development and delivery.

The project combines:

Architecture analysis
Threat modelling
Risk management
Secure coding
Vulnerability verification
Regression testing
SAST
Dependency analysis
Secret scanning
Container security
CI/CD
Automated security gates
Security evidence
Secure development practices

The project demonstrates that security controls can be continuously integrated into the development lifecycle and used to prevent known security issues from progressing through the delivery pipeline.

45. References

The following sources are used as technical references for the project:

OWASP Foundation - OWASP WebGoat
OWASP WebGoat project documentation.
OWASP Foundation - OWASP WebGoat Source Repository
WebGoat source code and project documentation.
OWASP Foundation - OWASP Top 10
Web application security risks and vulnerability concepts.
OWASP Foundation - OWASP Application Security Verification Standard (ASVS)
Application security verification requirements and secure development guidance.
OWASP Foundation - Threat Modeling
Threat-modelling concepts and methodology.
OWASP Foundation - Secure Coding Practices
Secure software development and coding guidance.
GitHub - GitHub Actions Documentation
CI/CD workflow and automation documentation.
Semgrep Documentation
Static Application Security Testing and code analysis.
OWASP Dependency-Check
Software Composition Analysis and dependency vulnerability detection.
Gitleaks
Secret and credential detection.
Trivy Documentation
Container and vulnerability scanning.
Docker Documentation
Containerisation and Docker Compose documentation.
IE3142 DevOps Security Continuous Assessment
Assignment requirements for Building and Securing a DevSecOps Pipeline.
Project Summary
Project:
Building and Securing a DevSecOps Pipeline

Module:
IE3142 - DevOps Security

Application:
OWASP WebGoat v2026.4

Core Approach:
DevSecOps

Threat Model:
STRIDE

SAST:
Semgrep

SCA:
OWASP Dependency-Check

Secret Scanning:
Gitleaks

Container Security:
Trivy

CI/CD:
GitHub Actions

Containerisation:
Docker / Docker Compose

Primary Demonstrated Vulnerability:
V01 - Missing Function Level Access Control

Development Branch:
feature/v01-access-control
