package qa.se.framework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;

/**
 * Helper methods for Shootout website.
 *
 * http://tutorialapp.saucelabs.com/
 */
public class ShootoutHelper {

    public String getUniqueId() {
        return Long.toHexString(Double.doubleToLongBits(Math.random()));
    }

    public void doRegister(WebDriver driver, Map<String, String> userDetails, boolean logout) {
        userDetails.put("confirm_password", userDetails.get("confirm_password") != null ?
                userDetails.get("confirm_password") : userDetails.get("password"));
        driver.get("http://tutorialapp.saucelabs.com/register");
        driver.findElement(By.id("username")).sendKeys(userDetails.get("username"));
        driver.findElement(By.id("password")).sendKeys(userDetails.get("password"));
        driver.findElement(By.id("confirm_password")).sendKeys(userDetails.get("confirm_password"));
        driver.findElement(By.id("name")).sendKeys(userDetails.get("name"));
        driver.findElement(By.id("email")).sendKeys(userDetails.get("email"));
        driver.findElement(By.id("form.submitted")).click();

        if (logout) {
            doLogout(driver);
        }
    }

    private void doLogout(WebDriver driver) {
        driver.get("http://tutorialapp.saucelabs.com/logout");
        assertEquals(driver.findElement(By.id("message")).getText(), "Logged out successfully.", "Message not found");
    }

    public Map<String, String> createRandomUser() {
        Map<String, String> userDetails = new HashMap<String, String>();
        String fakeId = getUniqueId();
        userDetails.put("username", fakeId);
        userDetails.put("password", "testpass");
        userDetails.put("name", "Fake " + fakeId);
        userDetails.put("email", fakeId + "@example.com");
        return userDetails;
    }

    public void doLogin(WebDriver driver, String username, String password) {
        driver.findElement(By.name("login")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.cssSelector("input.login")).click();
        assertEquals(driver.findElement(By.id("message")).getText(), "Logged in successfully.", "Message not found");
    }

}
