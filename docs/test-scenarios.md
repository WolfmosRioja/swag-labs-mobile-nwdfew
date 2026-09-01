# Swag Labs Mobile — Test Scenarios

**Version:** 1.0  
**Date:** 2026-09-01  
**Scope:** Swag Labs Mobile App (Android + iOS)  
**Framework:** Appium + Selenium + Cucumber (Gherkin)

---

## 1. Purpose

Authoritative reference for test coverage planning, automation backlog, and traceability. Each scenario maps to an epic, tagged with account(s), priority, and automation status.

---

## 2. User Accounts


| Account                   | Expected Behavior                     |
| ------------------------- | ------------------------------------- |
| `standard_user`           | Normal operation — full functionality |
| `locked_out_user`         | Login blocked; error displayed        |
| `problem_user`            | Broken images; layout glitches        |
| `performance_glitch_user` | High latency; must remain functional  |
| `error_user`              | Cart icon error class bug             |
| `visual_user`             | Visual/sort bugs                      |


---



## 3. Test Scenarios



### Epic: Login


| ID           | Title                                   | Description                                                  | Why It Matters                                       | Account(s)                | Priority | Status            |
| ------------ | --------------------------------------- | ------------------------------------------------------------ | ---------------------------------------------------- | ------------------------- | -------- | ----------------- |
| SC-LOGIN-001 | Standard user logs in successfully      | Verify standard_user authenticates and reaches products page | Primary happy-path; gates all downstream flows       | `standard_user`           | Critical | **AUTOMATED**     |
| SC-LOGIN-002 | Locked out user rejected at login       | Verify locked_out_user sees error and stays on login         | Security gate — locked accounts must not gain access | `locked_out_user`         | Critical | **AUTOMATED**     |
| SC-LOGIN-003 | Problem user logs in                    | Verify problem_user authenticates despite visual issues      | Defect-specific user can still complete core flows   | `problem_user`            | High     | NOT YET AUTOMATED |
| SC-LOGIN-004 | Performance glitch user logs in         | Verify performance_glitch_user authenticates (slower)        | App functional under degraded latency                | `performance_glitch_user` | High     | NOT YET AUTOMATED |
| SC-LOGIN-005 | Error user logs in                      | Verify error_user authenticates despite cart bug             | Known-bug user can reach products page               | `error_user`              | High     | NOT YET AUTOMATED |
| SC-LOGIN-006 | Visual user logs in                     | Verify visual_user authenticates despite visual/sort bugs    | Known-bug user can reach products page               | `visual_user`             | High     | NOT YET AUTOMATED |
| SC-LOGIN-007 | Empty credentials rejected              | Submit blank username/password; expect error                 | Prevents unauthenticated access; input validation    | None                      | Critical | **AUTOMATED**     |
| SC-LOGIN-008 | Wrong password rejected                 | Valid username + incorrect password; expect error            | Credential validation for incorrect passwords        | None                      | High     | **AUTOMATED**     |
| SC-LOGIN-009 | Username only (no password) rejected    | Valid username + empty password; expect error                | Partial-credential rejection                         | None                      | Medium   | **AUTOMATED**     |
| SC-LOGIN-010 | Password only (no username) rejected    | Empty username + valid password; expect error                | Partial-credential rejection                         | None                      | Medium   | **AUTOMATED**     |
| SC-LOGIN-011 | Special chars in username rejected      | Username with special chars; expect error                    | Input sanitization / injection prevention            | None                      | Medium   | NOT YET AUTOMATED |
| SC-LOGIN-012 | Login button inactive with empty fields | Verify button disabled/error when fields empty               | UI guard — prevents accidental submission            | None                      | Low      | NOT YET AUTOMATED |




### Epic: Product Catalog


| ID          | Title                                     | Description                                         | Why It Matters                                       | Account(s)                | Priority | Status            |
| ----------- | ----------------------------------------- | --------------------------------------------------- | ---------------------------------------------------- | ------------------------- | -------- | ----------------- |
| SC-PROD-001 | Product catalog displays after login      | Verify ≥4 products listed                           | Core content delivery — empty catalog = broken store | `standard_user`           | Critical | **AUTOMATED**     |
| SC-PROD-002 | Catalog loads for problem_user            | Verify list renders (images may be broken)          | Visual defects must not prevent browsing             | `problem_user`            | Medium   | NOT YET AUTOMATED |
| SC-PROD-003 | Catalog loads for performance_glitch_user | Verify list renders within timeout for slow user    | Latency must not cause timeouts/blank screens        | `performance_glitch_user` | High     | NOT YET AUTOMATED |
| SC-PROD-004 | Catalog loads for error_user              | Verify list renders (cart icon bug may show)        | Catalog unaffected by unrelated cart defect          | `error_user`              | Medium   | NOT YET AUTOMATED |
| SC-PROD-005 | Catalog loads for visual_user             | Verify list renders (sort may display wrong)        | Visual defects must not prevent browsing             | `visual_user`             | Medium   | NOT YET AUTOMATED |
| SC-PROD-006 | Products sorted by default (Name A-Z)     | Verify initial sort order is Name A-Z               | Default UX contract — users expect alphabetical      | `standard_user`           | Medium   | **AUTOMATED**     |
| SC-PROD-007 | Sort by Price (low to high)               | Change sort to "Price: Low to High"; verify order   | Primary catalog feature; must work correctly         | `standard_user`           | High     | **AUTOMATED**     |
| SC-PROD-008 | Sort by Price (high to low)               | Change sort to "Price: High to Low"; verify order   | Primary catalog feature; must work correctly         | `standard_user`           | High     | **AUTOMATED**     |
| SC-PROD-009 | Sort by Name (Z to A)                     | Change sort to "Name: Z to A"; verify reverse order | Sorting correctness for reverse order                | `standard_user`           | Medium   | **AUTOMATED**     |
| SC-PROD-010 | Visual user sort behavior                 | Verify visual_user sort produces correct results    | Documents known visual/sort bug for regression       | `visual_user`             | Medium   | NOT YET AUTOMATED |
| SC-PROD-011 | Product images load for standard_user     | Verify all product images displayed (no broken)     | Broken images degrade UX and trust                   | `standard_user`           | Medium   | NOT YET AUTOMATED |
| SC-PROD-012 | Problem user has broken images            | Verify problem_user sees broken/incorrect images    | Documents known image bug for regression             | `problem_user`            | Low      | NOT YET AUTOMATED |




### Epic: Product Detail


| ID            | Title                               | Description                                             | Why It Matters                                  | Account(s)      | Priority | Status            |
| ------------- | ----------------------------------- | ------------------------------------------------------- | ----------------------------------------------- | --------------- | -------- | ----------------- |
| SC-DETAIL-001 | Open product detail page            | Tap product; verify detail page with title/description  | Product detail essential for purchase decisions | `standard_user` | High     | **AUTOMATED**     |
| SC-DETAIL-002 | Product title non-empty on detail   | Verify title text present and not blank                 | Empty titles indicate broken data binding       | `standard_user` | Medium   | NOT YET AUTOMATED |
| SC-DETAIL-003 | Add product to cart from detail     | Tap "Add to Cart" on detail, back, verify badge++       | Alternative add-to-cart path must work          | `standard_user` | High     | **AUTOMATED**     |
| SC-DETAIL-004 | Detail page for problem_user        | Verify detail renders for problem_user (images broken)  | Visual defects must not block detail viewing    | `problem_user`  | Medium   | NOT YET AUTOMATED |
| SC-DETAIL-005 | Back navigation detail→catalog      | Tap back on detail; verify return to product list       | Navigation integrity between screens            | `standard_user` | Medium   | NOT YET AUTOMATED |
| SC-DETAIL-006 | Detail shows correct price          | Verify detail price matches catalog price               | Price consistency critical for purchase trust   | `standard_user` | High     | NOT YET AUTOMATED |
| SC-DETAIL-007 | Product description displayed       | Verify description area present and non-empty           | Product info completeness                       | `standard_user` | Medium   | NOT YET AUTOMATED |
| SC-DETAIL-008 | Remove product from cart via detail | Add product, go to detail, tap "Remove", verify badge-- | Removal must work from both catalog and detail  | `standard_user` | Medium   | NOT YET AUTOMATED |




### Epic: Cart


| ID          | Title                                    | Description                                              | Why It Matters                                   | Account(s)                    | Priority | Status            |
| ----------- | ---------------------------------------- | -------------------------------------------------------- | ------------------------------------------------ | ----------------------------- | -------- | ----------------- |
| SC-CART-001 | Add item to cart and verify in cart page | Add from catalog, open cart, verify 1 item               | Core shopping flow — cart must reflect additions | `standard_user`, `error_user` | Critical | **AUTOMATED**     |
| SC-CART-002 | Remove item from cart                    | Add item, open cart, remove, verify empty                | Cart management — removal must decrement/remove  | `standard_user`               | High     | **AUTOMATED**     |
| SC-CART-003 | Cart badge count accurate after add      | Verify badge increments to 1 after add                   | Badge must reflect true cart state always        | `standard_user`               | High     | **AUTOMATED**     |
| SC-CART-004 | Cart persists across navigation          | Add item, navigate to detail and back, verify in cart    | Cart state must survive screen transitions       | `standard_user`               | High     | NOT YET AUTOMATED |
| SC-CART-005 | Cart persists after logout/login         | Add item, log out, log in, verify item (or reset)        | Session persistence behavior documented          | `standard_user`               | Medium   | NOT YET AUTOMATED |
| SC-CART-006 | Error user cart icon behavior            | Verify cart icon state for error_user (error class)      | Documents known cart bug for regression          | `error_user`                  | Medium   | NOT YET AUTOMATED |
| SC-CART-007 | Empty cart shows correct state           | Open cart with no items; verify empty message/list       | Empty state UX must be clear                     | `standard_user`               | Medium   | NOT YET AUTOMATED |
| SC-CART-008 | Cart badge disappears when empty         | Remove last item; verify badge no longer visible         | Badge cleanup part of accurate cart state        | `standard_user`               | Medium   | NOT YET AUTOMATED |
| SC-CART-009 | Add multiple products to cart            | Add 2+ different products; verify count and all items    | Multi-item cart correctness                      | `standard_user`               | High     | NOT YET AUTOMATED |
| SC-CART-010 | Cart item displays correct details       | Verify cart item shows correct name, description, price  | Cart item data must match catalog data           | `standard_user`               | Medium   | NOT YET AUTOMATED |
| SC-CART-011 | Performance glitch user cart interaction | Verify add/remove complete for slow user without timeout | Latency must not cause cart operation failures   | `performance_glitch_user`     | Medium   | NOT YET AUTOMATED |




### Epic: Checkout


| ID           | Title                                       | Description                                                     | Why It Matters                                       | Account(s)                | Priority | Status            |
| ------------ | ------------------------------------------- | --------------------------------------------------------------- | ---------------------------------------------------- | ------------------------- | -------- | ----------------- |
| SC-CHECK-001 | Standard user completes full purchase       | Login → add → cart → checkout info → overview → complete → home | End-to-end happy path — most critical business flow  | `standard_user`           | Critical | **AUTOMATED**     |
| SC-CHECK-002 | Problem user completes full purchase        | Same flow using problem_user account                            | Defect user must still complete purchases            | `problem_user`            | High     | **AUTOMATED**     |
| SC-CHECK-003 | Performance glitch user completes purchase  | Same flow using performance_glitch_user                         | Slow user must complete without timeout              | `performance_glitch_user` | High     | NOT YET AUTOMATED |
| SC-CHECK-004 | Checkout requires shipping info             | Proceed without entering info; verify form required             | Shipping info mandatory for order processing         | `standard_user`           | High     | NOT YET AUTOMATED |
| SC-CHECK-005 | Cancel checkout and return to cart          | Enter checkout info, cancel, verify return to cart with items   | Users must be able to abandon checkout               | `standard_user`           | High     | NOT YET AUTOMATED |
| SC-CHECK-006 | Checkout overview shows correct items/total | Verify overview lists correct product(s), prices, total         | Order summary accuracy legally/financially important | `standard_user`           | High     | NOT YET AUTOMATED |
| SC-CHECK-007 | Checkout with empty first name              | Submit checkout info with empty first name; expect error        | Form validation for required fields                  | `standard_user`           | Medium   | NOT YET AUTOMATED |
| SC-CHECK-008 | Checkout with empty last name               | Submit checkout info with empty last name; expect error         | Form validation for required fields                  | `standard_user`           | Medium   | NOT YET AUTOMATED |
| SC-CHECK-009 | Checkout with empty postal code             | Submit checkout info with empty postal code; expect error       | Form validation for required fields                  | `standard_user`           | Medium   | NOT YET AUTOMATED |
| SC-CHECK-010 | Checkout with invalid postal code           | Submit non-numeric postal code; expect error/correction         | Input validation for postal code format              | `standard_user`           | Medium   | NOT YET AUTOMATED |
| SC-CHECK-011 | Order completion shows success message      | Verify "Thank you for your order!" displayed                    | Confirms order processed; user assurance             | `standard_user`           | Medium   | **AUTOMATED***    |
| SC-CHECK-012 | Navigate home after order completion        | Tap "Back Home" on completion; verify return to products        | Post-purchase flow must reset cleanly                | `standard_user`           | Medium   | **AUTOMATED***    |
| SC-CHECK-013 | Error user checkout behavior                | Verify error_user can complete checkout (cart bug)              | Documents if cart bug impacts checkout               | `error_user`              | Medium   | NOT YET AUTOMATED |
| SC-CHECK-014 | Visual user checkout behavior               | Verify visual_user can complete checkout (visual bugs)          | Documents if visual bug impacts checkout             | `visual_user`             | Medium   | NOT YET AUTOMATED |
| SC-CHECK-015 | Checkout with multiple items                | Add multiple products; verify overview shows all and total      | Multi-item order accuracy                            | `standard_user`           | High     | NOT YET AUTOMATED |


> *SC-CHECK-011 and SC-CHECK-012 are verified as steps within SC-CHECK-001, not as separate scenarios.

---



## 4. Coverage Matrix: User Account × Flow

Legend: **A** = Automated | **M** = Not Yet Automated / Manual | **N/A** = Not applicable


| Account                   | Login | Browse Catalog | Product Detail | Add to Cart | Cart Page | Checkout |
| ------------------------- | ----- | -------------- | -------------- | ----------- | --------- | -------- |
| `standard_user`           | A     | A              | A              | A           | A         | A        |
| `locked_out_user`         | A     | N/A            | N/A            | N/A         | N/A       | N/A      |
| `problem_user`            | M     | M              | M              | M           | M         | A        |
| `performance_glitch_user` | M     | M              | M              | M           | M         | M        |
| `error_user`              | M     | M              | M              | A           | M         | M        |
| `visual_user`             | M     | M              | M              | M           | M         | M        |


---



## 5. Summary


| Metric            | Count  |
| ----------------- | ------ |
| Total scenarios   | 48     |
| **Automated**     | **19** |
| Not yet automated | 29     |
| Critical priority | 7      |
| High priority     | 19     |
| Medium priority   | 21     |
| Low priority      | 1      |




### Notes

- **19 automated scenarios** traced to current Cucumber feature files under `src/test/resources/features/` (login.feature, products.feature, cart.feature, checkout.feature).
- Login: 6 automated (SC-LOGIN-001, 002, 007, 008, 009, 010). SC-LOGIN-003/004/005/006 covered implicitly via other flows but not as standalone login tests.
- Products: 8 automated (SC-PROD-001, SC-CART-001, SC-CART-003, SC-DETAIL-001, SC-DETAIL-003, SC-PROD-006, SC-PROD-007, SC-PROD-008, SC-PROD-009 — sorting outline = 3).
- Cart: 3 automated (SC-CART-001 outline with 2 examples, SC-CART-002).
- Checkout: 2 automated (SC-CHECK-001 outline with 2 examples).
- Coverage gaps: SC-LOGIN-011/012, sorting for problem/visual users (SC-PROD-010), product images (SC-PROD-011/012), detail page validations (SC-DETAIL-002/004-008), cart persistence (SC-CART-004/005/007-011), checkout validation (SC-CHECK-004-010/013-015).
- Full automated coverage only for `standard_user`. `locked_out_user` covered for login only. Other 4 personas covered partially.

---

