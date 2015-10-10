sparq-selenium-framework
======================
Selenium test framework in Java, using 'TestNG' as a threaded test runner, and browsers executing within SauceLabs.

STILL IN DEVELOPMENT, NOT QUITE FINISHED

## Requirements

Provide a `src/test/resources/config.properties` containing these two properties:  `sauceUser` and `sauceKey`

## Implemented Features
<table>
  <tr>
    <th>Feature</th>
    <th>Description</th>
  </tr>
    <tr>
    <th>TestNG WebDriver Factory</th>
    <td>This framework uses the 'DataProvider' to pass separate instances of WebDriver to each test method.</td>
  </tr>
  <tr>
    <th>Mockito Tested</th>
    <td>Tested with Mockito using test driven development.</td>
  </tr>
  <tr>
    <th>TestNG Multi-Threading</th>
    <td>Uses TestNG as a multi-threaded test runner, running each test method in a separate thread.</td>
  </tr>
  <tr>
    <th>SauceLabs Enabled</th>
    <td>All tests run on SauceLabs.com and with a free account you can run 2 threads at a time.</td>
  </tr>
</table>

## Instructions

This project will run within Eclipse, IntelliJ IDEA, or from the command line using Maven.

## Report

After tests are finished the report is stored in the 'test-output' folder in your project.

![alt text][logo]

[logo]: https://raw.githubusercontent.com/djangofan/sparq-selenium-framework/master/Sample-Report.png "Logo Title Text 2"
