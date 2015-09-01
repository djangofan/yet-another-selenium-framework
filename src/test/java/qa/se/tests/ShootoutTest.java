package qa.se.tests;

import com.saucelabs.testng.SauceOnDemandTestListener;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.annotations.*;
import qa.se.framework.ShootoutSuiteTestBase;
import java.util.Map;
import static org.testng.Assert.*;

@Listeners({SauceOnDemandTestListener.class})
public class ShootoutTest extends ShootoutSuiteTestBase
{
    ShootoutTest()
    {
        Reporter.log("Initialized test: " + this.getClass().getName());
    }

    @Test(dataProvider = "assembleTestParams")
    public void testLogout(WebDriver driver, String testIdentifier) {
        Reporter.log("Running '" + testIdentifier + "' test.");
        Map<String, String> userDetails = shootoutHelper.createRandomUser();
        shootoutHelper.doRegister(driver, userDetails, true);
    }

    @Test(dataProvider = "assembleTestParams")
    public void testLoginFailsWithBadCredentials(WebDriver driver, String testIdentifier) {
        Reporter.log("Running '" + testIdentifier + "' test.");
        String userName = shootoutHelper.getUniqueId();
        String password = shootoutHelper.getUniqueId();
        driver.findElement(By.name("login")).sendKeys(userName);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.cssSelector("input.login")).click();
        assertNotNull(driver.findElement(By.id("message")), "Text not found");
    }

    @Test(dataProvider = "assembleTestParams")
    public void testLogin(WebDriver driver, String testIdentifier) {
        Reporter.log("Running '" + testIdentifier + "' test.");
        Map<String, String> userDetails = shootoutHelper.createRandomUser();
        shootoutHelper.doRegister(driver, userDetails, true);
        shootoutHelper.doLogin(driver, userDetails.get("username"), userDetails.get("password"));
    }

    @Test(dataProvider = "assembleTestParams")
    public void testRegister(WebDriver driver, String testIdentifier) {
        Reporter.log("Running '" + testIdentifier + "' test.");
        Map<String, String> userDetails = shootoutHelper.createRandomUser();
        shootoutHelper.doRegister(driver, userDetails, false);
        assertTrue(driver.findElement(By.cssSelector(".username")).getText().contains("You are logged in as "), "Message not found");
    }

    @Test(dataProvider = "assembleTestParams")
    public void testRegisterFailsWithoutUsername(WebDriver driver, String testIdentifier) {
        Reporter.log("Running '" + testIdentifier + "' test.");
        Map<String, String> userDetails = shootoutHelper.createRandomUser();
        userDetails.put("username", "");
        shootoutHelper.doRegister(driver, userDetails, false);
        assertEquals(driver.findElement(By.cssSelector(".error")).getText(), "Please enter a value", "Message not found");
    }

    @Test(dataProvider = "assembleTestParams")
    public void testRegisterFailsWithoutName(WebDriver driver, String testIdentifier) {
        Reporter.log("Running '" + testIdentifier + "' test.");
        Map<String, String> userDetails = shootoutHelper.createRandomUser();
        userDetails.put("name", "");
        shootoutHelper.doRegister(driver, userDetails, false);
        assertEquals(driver.findElement(By.cssSelector(".error")).getText(), "Please enter a value", "Message not found");
    }

    @Test(dataProvider = "assembleTestParams")
    public void testRegisterFailsWithMismatchedPasswords(WebDriver driver, String testIdentifier) {
        Reporter.log("Running '" + testIdentifier + "' test.");
        Map<String, String> userDetails = shootoutHelper.createRandomUser();
        userDetails.put("confirm_password", shootoutHelper.getUniqueId());
        shootoutHelper.doRegister(driver, userDetails, false);
        assertEquals(driver.findElement(By.cssSelector(".error")).getText(), "Fields do not match", "Message not found");
    }

    @Test(dataProvider = "assembleTestParams")
    public void testRegisterFailsWithBadEmail(WebDriver driver, String testIdentifier) {
        Reporter.log("Running '" + testIdentifier + "' test.");
        Map<String, String> userDetails = shootoutHelper.createRandomUser();
        userDetails.put("email", "test");
        shootoutHelper.doRegister(driver, userDetails, false);
        assertEquals(driver.findElement(By.cssSelector(".error")).getText(), "An email address must contain a single @", "Message not found");
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("@example.com");
        driver.findElement(By.id("form.submitted")).click();
        assertEquals(driver.findElement(By.cssSelector(".error")).getText(), "The username portion of the email address is invalid (the portion before the @: )", "Message not found");
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("test@example");
        driver.findElement(By.id("form.submitted")).click();
        assertEquals(driver.findElement(By.cssSelector(".error")).getText(), "The domain portion of the email address is invalid (the portion after the @: example)", "Message not found");
    }

}
