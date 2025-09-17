🚀 Ecommerce Web Automation Framework

📘 Overview
This is a Java Selenium Automation Framework designed for:
✅ Page Object Model (POM) for reusability and maintainability


✅ Parallel execution with TestNG and Selenium Grid


✅ Cross-browser testing (Chrome, Firefox, Edge)


✅ Reports & Logs via ExtentReports + Log4j2


✅ Screenshots on failure with auto-cleanup before each run


✅ Excel-based test data using Apache POI


✅ CI/CD ready (Jenkins + Docker Selenium Grid)



📂 Project Structure
Ecommerce_WebAutomation
│── pom.xml
│── testng.xml
│── README.md
│
├── src
│   ├── main
│   │   └── java
│   │       ├── base
│   │       │   ├── BaseTest.java
│   │       │   └── DriverFactory.java
│   │       │
│   │       ├── pages
│   │       │   ├── LoginPage.java
│   │       │   ├── HomePage.java
│   │       │   └── CheckoutPage.java
│   │       │
│   │       ├── utils
│   │       │   ├── ConfigReader.java
│   │       │   └── ExcelUtils.java
│   │       │
│   │       ├── reports
│   │       │   └── ExtentManager.java
│   │       │
│   │       └── webactions
│   │           └── WebActions.java
│   │
│   └── test
│       └── java
│           ├── tests
│           │   ├── LoginTest.java
│           │   ├── AddToCartTest.java
│           │   └── CheckoutTest.java
│           │
│           └── listeners
│               └── TestListener.java
│
├── logs/
├── reports/
└── screenshots/


📑 Package & Class Overview
Here’s a quick overview of what each file does and why it exists:
🔹 base package
BaseTest.java → Handles @BeforeSuite and @AfterSuite setup: driver init, ExtentReports lifecycle, screenshot cleanup.


DriverFactory.java → Uses ThreadLocal<WebDriver> to support parallel tests across browsers and Selenium Grid.


🔹 pages package (POM)
LoginPage.java → Page Object for login functionality (username, password, login button).


HomePage.java → Page Object for post-login flows (logout, dashboard, etc.).


CheckoutPage.java → Page Object for checkout flow (cart, payment, order confirmation).


👉 Why?
 POM ensures separation of concerns → Test classes don’t worry about locators, only business flows.
🔹 utils package
ConfigReader.java → Reads values from config.properties (browser, base.url, waits).


ExcelUtils.java → Reads test data from TestData.xlsx. Useful for data-driven testing.


👉 Why?
 Keeps configs and test data externalized so tests are flexible and environment-independent.
🔹 webactions package
WebActions.java → Common utilities:


captureScreenshot() → Takes screenshot & saves to screenshots/ folder.


getElements(), waitForVisibility(), selectDropdown(), click(), etc.


👉 Why?
 Promotes code reusability by centralizing all frequently used WebDriver actions.
🔹 reports package
ExtentManager.java → Configures ExtentReports (theme, file path).


Manages thread-safe ExtentTest objects for logging test steps.


👉 Why?
 So every test execution gets a beautiful HTML report with screenshots & logs.
🔹 tests package
LoginTest.java → Tests login with valid & invalid data (using Excel).


AddToCartTest.java → Tests product add-to-cart flow.


CheckoutTest.java → Tests checkout & payment workflow.


👉 Why?
 Keeps actual business flow validation separate from setup code.
🔹 listeners package
TestListener.java → Implements TestNG listeners:


Captures screenshots on failure.


Attaches them in ExtentReports automatically.


👉 Why?
 Eliminates boilerplate — failures are auto-logged.

▶️ How to Run
Run full suite
mvn clean test

Run a specific test class
mvn -Dtest=LoginTest test

Run in parallel on Selenium Grid
mvn clean test -Dbrowser=chrome -DhubURL=http://localhost:4444/wd/hub


📊 Reports & Logs
ExtentReport → reports/ExtentReport.html


Logs → logs/app.log


Screenshots (failures only) → screenshots/



🔗 CI/CD Integration
Works with Jenkins pipeline (mvn clean test)


Can run on Docker Selenium Grid for scalable parallel execution


Can be extended with GitHub Actions / GitLab CI




