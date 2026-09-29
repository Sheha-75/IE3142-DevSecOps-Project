# V01 — Risk Assessment

## 1. Risk Identification

| Field | V01 |
|---|---|
| Vulnerability ID | V01 |
| Vulnerability | Missing Function Level Access Control |
| STRIDE Category | Elevation of Privilege |
| Security Property | Authorization |
| Affected Function | `GET /WebGoat/access-control/users` |
| Threat Actor | Authenticated non-administrator user |
| Asset | Protected user-management functionality and user-related information |
| Primary Control | Server-side authorization |
| Risk Treatment | Mitigate |

---

## 2. Risk Scenario

An authenticated user without administrator privileges may directly request the protected user-management endpoint.

In the vulnerable application, the request was successfully processed and returned `HTTP 200 OK`.

This creates a privilege-escalation risk because an authenticated user may access functionality intended for a higher-privileged role.

---

## 3. Likelihood Assessment

**Likelihood: High**

### Justification

The vulnerability was successfully reproduced against the unmodified application.

The attack path requires an authenticated user to directly request the affected HTTP endpoint. It does not require bypassing the application's authentication mechanism.

The vulnerable behavior was demonstrated using the same endpoint later used for verification:

```text
GET /WebGoat/access-control/users