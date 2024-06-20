# Cucumber Testing Framework

Cucumber is a framework that supports the implementation of Behavior-Driven Development (BDD), allowing for automated tests to be written in a language that is easily understood by non-technical team members. The Cucumber framework uses Gherkin language to create feature files that describe the expected behavior of the software.

## Official Resources
- Website: [Cucumber](https://cucumber.io/)
- GitHub Repositories:
    - [Repository 1](https://github.com/surazkarn/Testing-SpringBoot-JavaApp-using-Cucumber/blob/main/README.md)
    - [Repository 2](https://github.com/hemanshuchauhan/microservices-test/blob/master/README.md)

## Behavior-Driven Development (BDD)

### Understanding User Stories
1. Developers and testers try to understand user stories.
2. They may have doubts, questions, or require clarifications.
3. The Product Owner needs to provide answers to these questions.

### Creating Feature Files
1. Testers create a "Feature File" once they understand all requirements for a specific user story.
2. The feature file contains "Scenarios" written using Gherkin keywords.
3. Cucumber understands these feature files and can execute the tests defined within them.

## Sample Scenarios

### Feature: Login Functionality
**Description**: User should be able to access the account using login functionality.

#### Scenario: Login with Valid Credentials
```gherkin
Given User has opened an Application URL
And User has navigated to Login page
When User enters valid email address
And User enters valid password
And Clicks on Login Button
Then User should be able to login.
```
#### Scenario: Login with Invalid Email Credentials

```ghirkin
Given User has opened an Application URL
And User has navigated to Login page
When User enters invalid email address
And User enters valid password
And Clicks on Login Button
Then User should be able to see "Email Does not exist".
```

#### Scenario: Login with Invalid Password Credentials
``` ghirkin
Given User has opened an Application URL
And User has navigated to Login page
When User enters valid email address
And User enters invalid password
And Clicks on Login Button
Then User should be able to see "Invalid Credentials".
```

#### Scenario: Login with Invalid Credentials
```ghirkin
Given User has opened an Application URL
And User has navigated to Login page
When User enters invalid email address
And User enters invalid password
And Clicks on Login Button
Then User should be able to see "Invalid Credentials".
```

### Ghirkin Language Structure
**Feature:** Description of the User Story.  
**Background:** Common steps across all scenarios.  
**Scenario:** Multiple scenarios within a feature.  
**Given:** Pre-condition of the scenario.  
**When:** Action to be taken as part of the scenario.  
**And:** Used for multiple preconditions or actions.  
**Then:** Expected result of the action.  

### Scenario Outline and Examples
When testing the same scenario with multiple data sets, use "Scenario Outline" and "Examples."

#### Installation and Setup in IntelliJ IDEA
### Installing Cucumber
1. Open IntelliJ IDEA.
2. Go to File > Settings > Plugins.
3. Search for Cucumber for Java and install it.

#### Creating and Running Feature Files
1. Create a new project in IntelliJ IDEA.
2. Add the necessary dependencies in your pom.xml or build.gradle file.
3. Create a Feature file using Gherkin language.
4. Write step definitions for each step in the feature file.

### Writing Step Definitions
1. If step definitions are not provided, you will get "undefined scenarios".
2. Step definitions are Java methods that map to the steps in the feature files.

#### Running Multiple Feature Files
### Using Runner Class
1. Create a simple StepDefinition Java Class.
2. Annotate it with @RunWith(Cucumber.class).

### Generating Cucumber HTML Reports
1. Using @CucumberOptions
2. Annotate the runner class with @CucumberOptions(plugin={"html:target/CucumberHTMLReport.html"}).

## Tags in Cucumber

Tags in Cucumber help organize and filter which test scenarios to run.

### Common Tags
- **@all**: Can be added on top of the `Feature` keyword and will apply to all scenarios within the feature.
- **@Login**
- **@Smoke**
- **@nocredentials**
- **@invalidcredentials**
- **not@nocredentials**

``` java
@CucumberOptions(
    features = "src/test/java/features",
    glue = {"stepdefinitions", "package where hook class is organized"},
    plugin = {
        "pretty",
        "html:target/CucumberReport.html",
        "json:target/CucumberHTMLReport.json",
        "junit:target/CucumberHTMLReport.xml"
    },
    tags = "@smoke and @regression",
    dryRun = true,
    publish = true,
    tags = "@all and not @dev and not @wip and not @ignore"
)
public class TestRunner {
    // This class will be empty
}
```
#### Hooks in Cucumber
Hooks are used to define actions that should be performed before or after each scenario or step.
- **@Before**
- **@After**
- **@BeforeStep**
- **@AfterStep**

### Tagged Hooks
Tagged hooks run only for scenarios with specified tags.

``` java
@Before("@Login")
public void beforeLogin() {
    // code to execute before scenarios tagged with @Login
}

@After("@Regression")
public void afterRegression() {
    // code to execute after scenarios tagged with @Regression
}

@BeforeStep("@Login")
public void beforeStepLogin() {
    // code to execute before each step in scenarios tagged with @Login
}

@AfterStep("@Regression")
public void afterStepRegression() {
    // code to execute after each step in scenarios tagged with @Regression
}

```
#### Multiple Hooks with Order
Hooks can have an order to define their execution sequence.

``` java
@Before(order=0)
public void firstBefore() {
    // first hook to execute
}

@Before(order=1)
public void secondBefore() {
    // second hook to execute
}

@After(order=1)
public void firstAfter() {
    // first after hook to execute
}

@After(order=0)
public void secondAfter() {
    // second after hook to execute
}

```

### Value Attribute in Hooks
When multiple attributes are used, the value attribute must be provided.

``` java
@Before(value="@Login", order=0)
public void setupLogin() {
    // code to execute before scenarios tagged with @Login
}
```

### Data Tables
Data tables are used to pass multiple values to a step in a tabular form.

``` ghirkin
When User enters below fields
| firstname  | Baba           |
| lastname   | Kalandhar      |
| middlename | C              |
| email      | baba@gmail.com |
| address    | Pileru         |
| contact    | 56987455654    |
| username   | kalandhar88    |
| password   | password@6     |

```

``` java
public void enterFields(DataTable dataTable) {
    Map<String, String> map = dataTable.asMap(String.class, String.class);
    // use the map to access the data
}
```

#### Organizing Cucumber Projects
### Project Structure
- **features package:** Contains feature files.  
- **stepdefinitions package:** Contains step definition classes.  
- **runner package:** Contains the runner class.  

#### Running Cucumber Tests Using Maven

### Runner Class Naming
Rename the runner class to include Test either as a prefix or postfix (e.g., TestRunner.java or RunnerTest.java).

### Maven Surefire Plugin
Ensure maven-surefire-plugin is included in your pom.xml.

### Example Maven Command

```cmd (Overriding tags from commandline)
mvn test -Dcucumber.filter.tags="@Search"
```

#### Retrieving Scenario Names in Hooks
```java
@Before
public void setUp(Scenario scenario) {
    log.info(scenario.getName());
}
```
### Generating Cucumber Reports

``` CucumberOptions attribute 
plugin = {
        "pretty",
        "html:target/CucumberReport.html",
        "json:target/CucumberHTMLReport.json",
        "junit:target/CucumberHTMLReport.xml"
    }
```

#### Organizing Hooks in Cucumber Projects
### Hooks Class
- Create a new Hooks Java class.  
- Define methods for setup and teardown.
```java
public class Hooks {
    @Before
    public void setUp() {
        // setup code
    }

    @After
    public void tearDown() {
        // teardown code
    }
}
```

```Cucumber Options
glue = {"stepdefinitions", "package where hook class is organized"}
```

### Dry Run in Cucumber Projects
## Dry Run Options
**dryRun = true:** Steps will not invoke their step definition methods. It checks for unimplemented steps.
**dryRun = false:** Steps will invoke their implemented methods.

#### Publishing Cucumber Reports
### Using Public Attribute
```java
@CucumberOptions(publish = true)
public class TestRunner {
    // This class will be empty
}
```
### Using Properties File
- Set the following in **cucumber.properties** or **junit-platform.properties**(located in src/test/resources)
```cucumber.properties
cucumber.publish.enabled = true
```