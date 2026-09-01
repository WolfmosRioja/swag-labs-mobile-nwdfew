# Swag Labs Mobile — Project Documentation

This document maps the implementation to the QA Automation Challenge requirements.

---

## Challenge Requirements & Implementation

### 1. Identify Test Scenarios

**Requirement**: Explore the app, list testable user flows with descriptions.

**Done**: `docs/test-scenarios.md` — 48 scenarios across 5 epics (Login, Product Catalog, Product Detail, Cart, Checkout). Each has ID, title, description, rationale, account(s), priority, status. Includes coverage matrix (6 accounts × 6 flows).

### 2. Prioritize & Automate ≥2 Critical Flows

**Requirement**: Pick at least 2 critical scenarios, automate them, explain why.

**Automated** (all tagged `@smoke @sanity`):


| Priority | Flow                      | Scenarios                       | Why                                     |
| -------- | ------------------------- | ------------------------------- | --------------------------------------- |
| 1        | **Login (standard_user)** | 1                               | Every journey starts here               |
| 2        | **Add to Cart**           | 3 (catalog + detail)            | Core value — users must select products |
| 3        | **Full Checkout**         | 2 (standard_user, problem_user) | Revenue-critical end-to-end             |


**Why not others**: Locked-out user is negative-only. Problem/performance users validated at login + checkout to prove core flows work despite defects. Sorting, multi-item, form validation are secondary to the purchase happy path.

### 3. Cover Both Platforms (iOS + Android)

**Requirement**: Tests run on iOS Simulator and Android Emulator.

**Implementation**: Interface-based POM — 8 shared contracts, 8 Android impls, 8 iOS impls. Runtime dispatch via `PageObjectHelper` + `CapabilityManager.isAndroid()`.

**Locators**: React Native `testID` (stable cross-platform)

- Android → `resourceId` (`@AndroidFindBy`)
- iOS → accessibility `name` (`@iOSXCUITFindBy`)



### 4. Cover All User Accounts

**Requirement**: Test all 4 provided accounts.


| Account                   | Login               | Products | Cart | Checkout |
| ------------------------- | ------------------- | -------- | ---- | -------- |
| `standard_user`           | ✅                   | ✅        | ✅    | ✅        |
| `locked_out_user`         | ✅                   | N/A      | N/A  | N/A      |
| `problem_user`            | (via checkout)      | —        | —    | ✅ (2)    |
| `performance_glitch_user` | (via products/cart) | ✅        | ✅    | —        |


**Also covered**: `error_user` (cart icon bug), `visual_user` (visual/sort bugs).

### 5. Implement Automated Mobile Tests

**Stack**: Appium 10 + Selenium 4.40 + Cucumber 7 + JUnit 5 + Maven (Java 23)

**19 automated scenarios** across 4 feature files:


| Feature            | Scenarios | Coverage                                          |
| ------------------ | --------- | ------------------------------------------------- |
| `login.feature`    | 6         | All 4 required accounts + negative cases          |
| `products.feature` | 8         | Catalog, add-to-cart, detail, sorting (3 options) |
| `cart.feature`     | 3         | Add/remove, error_user (outline)                  |
| `checkout.feature` | 2         | Full purchase (standard_user, problem_user)       |


**Run**:

```powershell
.\run-tests.ps1                    # Android, local, full suite
.\run-tests.ps1 -Platform ios      # iOS simulator
.\run-tests.ps1 -Tags "@sanity"    # Sanity tier (includes smoke)
```

Framework boots its own Appium server via `AppiumServerManager` — no manual setup.

---



## Automated Scenarios (Current)



### login.feature (6)


| #   | Scenario                                   | Tags             | SC- Ref      |
| --- | ------------------------------------------ | ---------------- | ------------ |
| 1   | Standard user can log in successfully      | `@smoke @sanity` | SC-LOGIN-001 |
| 2   | Locked out user is blocked from logging in | `@smoke @sanity` | SC-LOGIN-002 |
| 3   | Login with empty credentials is rejected   | `@smoke @sanity` | SC-LOGIN-007 |
| 4   | Wrong password is rejected                 | `@sanity`        | SC-LOGIN-008 |
| 5   | Username only (no password) is rejected    | `@sanity`        | SC-LOGIN-009 |
| 6   | Password only (no username) is rejected    | `@sanity`        | SC-LOGIN-010 |




### products.feature (8)


| #   | Scenario                                     | Tags                     | SC- Ref                  |
| --- | -------------------------------------------- | ------------------------ | ------------------------ |
| 1   | User can see the product catalog after login | `@smoke @sanity`         | SC-PROD-001              |
| 2   | User can add a product to the cart           | `@smoke @sanity`         | SC-CART-001, SC-CART-003 |
| 3   | User can open a product and view its details | `@smoke @sanity`         | SC-DETAIL-001            |
| 4   | User can add a product from its detail page  | `@sanity`                | SC-DETAIL-003            |
| 5   | Products sorted by default (Name A-Z)        | `@sanity`                | SC-PROD-006              |
| 6   | Sort by Price (low to high)                  | `@standard_user @sanity` | SC-PROD-007              |
| 7   | Sort by Price (high to low)                  | `@standard_user @sanity` | SC-PROD-008              |
| 8   | Sort by Name (Z to A)                        | `@standard_user @sanity` | SC-PROD-009              |


*Scenarios 6–8 are a Scenario Outline with 3 examples.*

### cart.feature (3)


| #   | Scenario                                                       | Tags             | SC- Ref     |
| --- | -------------------------------------------------------------- | ---------------- | ----------- |
| 1   | User can add a product to the cart (standard_user, error_user) | `@smoke @sanity` | SC-CART-001 |
| 2   | User can remove an item from the cart                          | `@smoke @sanity` | SC-CART-002 |


*Scenario 1 is a Scenario Outline with 2 examples.*

### checkout.feature (2)


| #   | Scenario                                                     | Tags             | SC- Ref      |
| --- | ------------------------------------------------------------ | ---------------- | ------------ |
| 1   | User completes a full purchase (standard_user, problem_user) | `@smoke @sanity` | SC-CHECK-001 |


*Scenario Outline with 2 examples.*

---



## Evaluation Criteria


| Criterion                          | How It's Met                                                                              |
| ---------------------------------- | ----------------------------------------------------------------------------------------- |
| **Automation & QA best practices** | Interface POM, explicit waits, thread-local driver, scenario state, screenshot on failure |
| **Commit history**                 | Incremental: framework → page objects → step defs → features → reporting                  |
| **Code quality**                   | Java 23, strict typing, no `Thread.sleep`, reusable managers, centralized timeouts        |
| **Completeness**                   | Key flows (login→catalog→cart→checkout) + all 4 required accounts + 2 bonus               |
| **Correctness**                    | Exact error message assertions, explicit waits for async, platform-specific handling      |
| **Maintainability**                | Clean separation: features/steps/pages/interfaces/managers; single-responsibility         |


---



## Submission Requirements

**Video Evidence**

- `videos/*`
- Also in `target/generated-report/videos/`

---



## Project Structure

```
swag-labs-mobile-nwdfew/
├── .opencode/              # AI agent config
├── docs/
│   ├── test-scenarios.md   # 48 scenarios with SC- IDs
│   └── tsdr.md             # Test strategy reference
├── Resources/              # App binaries (provided)
│   ├── Android.zip
│   ├── IOS.tar.gz
│   ├── application-*.apk
│   └── SauceDemo.app/
├── src/
│   ├── main/java/com/swaglabs/
│   │   ├── capabilities/   # DesiredCapabilityBuilder, CapabilityManager
│   │   ├── device/         # DeviceManager
│   │   ├── entities/       # MobilePlatform enum
│   │   ├── exceptions/     # Custom exceptions
│   │   ├── managers/       # 6 managers (driver, server, caps, props, scenario, device)
│   │   └── utils/          # Waits, Timeouts, JsonParser, helpers
│   └── test/
│       ├── java/
│       │   ├── com/swaglabs/
│       │   │   ├── hooks/Hooks.java          # Driver lifecycle, Appium server, screenshots
│       │   │   ├── runner/TestRunner.java    # JUnit Platform Suite
│       │   │   └── stepsDefs/                # 5 step definition classes
│       │   └── page_objects/                 # POM (interfaces + android + ios)
│       └── resources/features/               # 4 Gherkin feature files
├── videos/                 # Video evidence
├── COMMENTS.md             # Candidate comments
├── CLAUDE.md               # AI guidance
├── pom.xml                 # Maven config
└── run-tests.ps1           # Execution wrapper
```

---



## Key Technical Decisions


| Decision                  | Rationale                                             |
| ------------------------- | ----------------------------------------------------- |
| Interface-based POM       | Single test codebase, platform locators isolated      |
| Appium Java Client 10     | Current stable; non-generic driver types handled      |
| Selenium Manager          | No WebDriverManager dependency; built into Selenium 4 |
| Cucumber + JUnit Platform | Parallel via `THREADS`, native JUnit integration      |
| Cluecumber Reports        | Rich HTML with screenshots, no external CI needed     |
| Auto Appium Server        | Boots/stops in `@BeforeAll`/`@AfterAll`               |


---



## Tag Taxonomy


| Type             | Tags                                                                                                             |
| ---------------- | ---------------------------------------------------------------------------------------------------------------- |
| **Severity**     | `@smoke`, `@sanity` (sanity includes smoke)                                                                      |
| **Feature**      | `@login`, `@products`, `@cart`, `@checkout`                                                                      |
| **Account**      | `@standard_user`, `@locked_out_user`, `@problem_user`, `@performance_glitch_user`, `@error_user`, `@visual_user` |
| **Traceability** | `@sc-login-XXX`, `@sc-prod-XXX`, `@sc-cart-XXX`, `@sc-check-XXX`                                                 |


---

