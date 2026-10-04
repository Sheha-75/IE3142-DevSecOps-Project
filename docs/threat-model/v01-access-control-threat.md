# V01 — Missing Function Level Access Control

## 1. Vulnerability Overview

**Vulnerability ID:** V01

**Vulnerability:** Missing Function Level Access Control

**STRIDE Category:** Elevation of Privilege (EoP)

**Security Property:** Authorization

**Affected Application:** OWASP WebGoat v2026.4

**Affected Function:** `GET /WebGoat/access-control/users`

V01 concerns insufficient server-side authorization enforcement for a protected application function.

In the vulnerable WebGoat implementation, the user-management functionality could be accessed through its HTTP endpoint without an effective authorization check on the affected GET operations.

The security issue is not limited to whether the function is visible in the user interface. The server must independently verify whether the authenticated user has sufficient privileges before allowing access to the protected functionality.

---

## 2. Protected Asset

The protected asset is the user-management functionality exposed through:

```text
GET /WebGoat/access-control/users