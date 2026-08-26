# Swag Labs Mobile - QA Automation Challenge

## Objective

Your assignment is to write automated mobile test cases for the **Swag Labs** app to test the core functionality of the application on **Android** and **iOS**.

## Brief

The product team at CKW AG has developed a mobile MVP for an online shop. Help the team gain confidence in further development by automatically testing the most critical features of the mobile app.

## Resources

The mobile application executables are provided in the `Resources/` folder:

| Platform | File               | Usage                                      |
|----------|--------------------|---------------------------------------------|
| Android  | `Android.zip`      | Extract and install the `.apk` on an Android emulator |
| iOS      | `IOS.tar.gz`       | Extract and install the `.app` on an iOS simulator     |

### Setup

1. **Android**: Extract `Resources/Android.zip` and use the resulting `.apk` file with an Android emulator (e.g., Android Studio AVD).
2. **iOS**: Extract `Resources/IOS.tar.gz` and use the resulting `.app` file with an iOS Simulator (e.g., via Xcode).

## Tasks

1. **Identify test scenarios**: Explore the mobile app and compile a list of testable user flows, cases, or scenarios. Provide a **brief description** of each scenario you identify (e.g., what it tests and why it matters). Focus on what you consider most fundamental (e.g., sign in, add to cart, checkout). This doesn't have to cover every possible case.

2. **Prioritize and automate**: From all the scenarios you identify, **prioritize at least 2** that you consider the most critical and implement automated tests for them. Explain why you chose those scenarios over others.

3. **Cover both platforms**: The automation must target **both iOS and Android**. Tests should be executable on an iOS Simulator and an Android Emulator.

4. **Cover all user accounts**: Make sure to test scenarios for all provided user accounts:

   | User                       | Description                                                        |
   |----------------------------|--------------------------------------------------------------------|
   | `standard_user`            | The app should work as expected for this user.                     |
   | `locked_out_user`          | User is locked out and should not be able to log in.               |
   | `problem_user`             | Images are not loading correctly for this user.                    |
   | `performance_glitch_user`  | This user has high loading times. Does the app still work as expected? |

5. **Implement automated mobile tests**: Write automated tests for the prioritized flows using any mobile testing framework of your choice.

## Evaluation Criteria

- **Automation & QA best practices**: Proper test structure, naming conventions, reusable components, and clear reporting.
- **Commit history**: Show us your work through your commit history. We want to see your thought process and incremental progress.
- **Code quality**: Working code with enough room to demonstrate how to structure components in a small program.
- **Completeness**: Did you cover the key user flows and all user accounts?
- **Correctness**: Does the test logic act in sensible, thought-out ways?
- **Maintainability**: Is the code clean, well-organized, and easy to maintain?

## Submission Requirements

### Video Evidence

- **Upload a video** demonstrating the project running successfully. This video should show the automated tests executing against the mobile app, including test results and any relevant output.
- The video serves as proof that the solution works in a real environment and helps evaluators understand your setup and execution flow.
- You may upload the video directly to the platform.

### Candidate Comments

- **Include your comments and observations** about the project. Your insights are highly valuable to the evaluation process.
- Consider addressing:
  - Your approach and reasoning behind the chosen test scenarios.
  - Any challenges you encountered and how you solved them.
  - Trade-offs or decisions you made during implementation.
  - Suggestions for improvements or additional test coverage you would add given more time.
  - Any bugs or issues you discovered in the application during testing.
- You may include these comments in a `COMMENTS.md` file at the root of the project or as part of your submission on the platform.

## CodeSubmit

Please organize, design, test, and document your code as if it were going into production — then push your changes to the `master` branch. After you have pushed your code, you may submit the assignment on the assignment page.

Have fun building! 🚀

**The CKW AG Team**
