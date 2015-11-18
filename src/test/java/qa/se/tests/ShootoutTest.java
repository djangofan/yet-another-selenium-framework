package qa.se.tests;

import com.codeborne.selenide.WebDriverRunner;
import com.saucelabs.testng.SauceOnDemandTestListener;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Sleeper;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;
import qa.se.framework.SauceLabsTestBase;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import static com.codeborne.selenide.Selenide.*;
import static org.testng.Assert.*;

@Listeners({SauceOnDemandTestListener.class})
public class ShootoutTest extends SauceLabsTestBase
{
    @BeforeMethod
    public void prepareBrowserLocation(Object[] params)
    {
        WebDriver driver = (WebDriver)params[0];
        WebDriverRunner.setWebDriver(driver);
        open("http://tutorialapp.saucelabs.com");
        Assert.assertTrue(title().equals("Shootout"));
    }

    @Test(dataProvider = "sauceTestParams")
    public void testLogout(WebDriver driver, String testIdentifier) {
        Reporter.log("Running '" + testIdentifier + "' test.");
        Map<String, String> userDetails = shootoutHelper.createRandomUser();
        shootoutHelper.doRegister(WebDriverRunner.getWebDriver(), userDetails, true);
    }

    @Test(dataProvider = "sauceTestParams")
    public void testLoginFailsWithBadCredentials(WebDriver driver, String testIdentifier) {
        Reporter.log("Running '" + testIdentifier + "' test.");
        String userName = shootoutHelper.getUniqueId();
        String password = shootoutHelper.getUniqueId();
        driver.findElement(By.name("login")).sendKeys(userName);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.cssSelector("input.login")).click();
        assertNotNull(driver.findElement(By.id("message")), "Text not found");
    }

    @Test(dataProvider = "sauceTestParams")
    public void testLogin(WebDriver driver, String testIdentifier) {
        Reporter.log("Running '" + testIdentifier + "' test.");
        Map<String, String> userDetails = shootoutHelper.createRandomUser();
        shootoutHelper.doRegister(WebDriverRunner.getWebDriver(), userDetails, true);
        shootoutHelper.doLogin(WebDriverRunner.getWebDriver(), userDetails.get("username"), userDetails.get("password"));
    }

    @Test(dataProvider = "sauceTestParams")
    public void testRegister(WebDriver driver, String testIdentifier) {
        Reporter.log("Running '" + testIdentifier + "' test.");
        Map<String, String> userDetails = shootoutHelper.createRandomUser();
        shootoutHelper.doRegister(WebDriverRunner.getWebDriver(), userDetails, false);
        assertTrue(driver.findElement(By.cssSelector(".username")).getText().contains("You are logged in as "), "Message not found");
    }

    @Test(dataProvider = "sauceTestParams")
    public void testRegisterFailsWithoutUsername(WebDriver driver, String testIdentifier) {
        Reporter.log("Running '" + testIdentifier + "' test.");
        Map<String, String> userDetails = shootoutHelper.createRandomUser();
        userDetails.put("username", "");
        shootoutHelper.doRegister(WebDriverRunner.getWebDriver(), userDetails, false);
        assertEquals(driver.findElement(By.cssSelector(".error")).getText(), "Please enter a value", "Message not found");
    }

    @Test(dataProvider = "sauceTestParams")
    public void testRegisterFailsWithoutName(WebDriver driver, String testIdentifier) {
        Reporter.log("Running '" + testIdentifier + "' test.");
        Map<String, String> userDetails = shootoutHelper.createRandomUser();
        userDetails.put("name", "");
        shootoutHelper.doRegister(WebDriverRunner.getWebDriver(), userDetails, false);
        assertEquals(driver.findElement(By.cssSelector(".error")).getText(), "Please enter a value", "Message not found");
    }

    @Test(dataProvider = "sauceTestParams")
    public void testRegisterFailsWithMismatchedPasswords(WebDriver driver, String testIdentifier) {
        Reporter.log("Running '" + testIdentifier + "' test.");
        Map<String, String> userDetails = shootoutHelper.createRandomUser();
        userDetails.put("confirm_password", shootoutHelper.getUniqueId());
        shootoutHelper.doRegister(WebDriverRunner.getWebDriver(), userDetails, false);
        assertEquals(driver.findElement(By.cssSelector(".error")).getText(), "Fields do not match", "Message not found");
    }

    @Test(dataProvider = "sauceTestParams")
    public void testRegisterFailsWithBadEmail(WebDriver driver, String testIdentifier) {
        Reporter.log("Running '" + testIdentifier + "' test.");
        Map<String, String> userDetails = shootoutHelper.createRandomUser();
        userDetails.put("email", "test");
        shootoutHelper.doRegister(WebDriverRunner.getWebDriver(), userDetails, false);
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

    @AfterMethod
    public void tearDown() {
        WebDriverRunner.setWebDriver(null);
        try {
            Sleeper.SYSTEM_SLEEPER.sleep(new org.openqa.selenium.support.ui.Duration(5, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
