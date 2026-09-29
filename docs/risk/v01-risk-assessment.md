## 4. Impact Assessment

**Impact: High**

### Justification

Successful exploitation allows an authenticated non-administrator user to access protected user-management functionality and user-related information intended for a higher-privileged role.

The primary security impact is unauthorized access resulting from elevation of privilege.

---

## 5. Likelihood x Impact Risk Rating

A 3x3 qualitative likelihood-impact matrix is used.

| Likelihood / Impact | Low | Medium | High |
|---|---:|---:|---:|
| Low | Low | Low | Medium |
| Medium | Low | Medium | High |
| High | Medium | High | Critical |

For V01:

- Likelihood: **High**
- Impact: **High**
- Risk Rating: **Critical**

### Justification

The likelihood is rated High because the vulnerability was successfully reproduced against the unmodified application by an authenticated user.

The impact is rated High because the affected function provides protected user-management functionality and user-related information intended for a higher-privileged role.

Therefore, the High likelihood and High impact combination produces a Critical risk rating under the selected 3x3 matrix.

---

## 6. Risk Mitigation and Control Mapping

| Risk | Mitigation / Control | Implementation | Verification |
|---|---|---|---|
| V01 - Missing Function Level Access Control | Server-side role authorization | `MissingFunctionACUsers.java` verifies the current user and requires administrator privileges before returning protected user data | Same GET request returns `403 Forbidden` |
| V01 - Unauthorized access | Deny access to non-administrator users | `currentUser == null || !currentUser.isAdmin()` results in HTTP 403 | `MissingFunctionACUsersTest` passes |
| V01 - Regression | Automated regression test | `MissingFunctionACUsersTest.java` verifies that the protected GET endpoint returns 403 | Maven test: 2 tests, 0 failures, 0 errors |
| V01 - Pipeline verification | Automated security validation | DevSecOps CI pipeline executes security checks for repository changes | CI pipeline evidence |

---

## 7. Residual Risk

After implementation of the server-side authorization control, the demonstrated V01 attack path is blocked.

The same protected GET request now results in `HTTP 403 Forbidden`.

The automated regression test also passes:

- Tests run: 2
- Failures: 0
- Errors: 0
- Skipped: 0
- Build: SUCCESS

The residual risk for the demonstrated V01 vulnerability is assessed as **Low**, provided that the authorization control remains enforced and the regression test continues to pass.

Other access-control weaknesses outside the demonstrated V01 endpoint are outside the scope of this risk assessment.

---

## 8. Evidence

The following evidence supports this assessment:

1. V01 vulnerable-behavior demonstration.
2. Source-code evidence showing the server-side authorization control.
3. Post-fix evidence showing the same request returning HTTP 403.
4. Automated regression-test result for `MissingFunctionACUsersTest`.
5. DevSecOps CI/CD pipeline evidence.

Sensitive user information or credential-like values shown in screenshots should be redacted before inclusion in the final report.