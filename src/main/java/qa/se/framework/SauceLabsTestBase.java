package qa.se.framework;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Selenium enabled test base.
 */
public class SauceLabsTestBase extends TestNGSuiteTestBase
{
    protected ShootoutHelper shootoutHelper;

    public SauceLabsTestBase()
    {
        shootoutHelper = new ShootoutHelper();
        Reporter.log("Initialized test class: " + this.getClass().getName());
    }

    /**
     * Creates a new {@link RemoteWebDriver} instance to be used to run WebDriver tests on SauceLabs.
     * Should return one array item per
     *
     * Platform reference:
     * https://code.google.com/p/selenium/source/browse/java/client/src/org/openqa/selenium/Platform.java
     */
    @DataProvider(name = "sauceTestParams", parallel = true)
    public Object[][] assembleTestParams(ITestContext context, Method method) {
        WebDriver driver = null;
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(CapabilityType.BROWSER_NAME, context.getCurrentXmlTest().getParameter("browser"));
        caps.setCapability(CapabilityType.VERSION, context.getCurrentXmlTest().getParameter("browserVersion"));
        caps.setCapability(CapabilityType.PLATFORM, context.getCurrentXmlTest().getParameter("platform"));
        String testName = this.getClass().getSimpleName() + "-" + method.getName();
        caps.setCapability("name", testName);
        caps.setCapability("tags", context.getCurrentXmlTest().getParameter("browser"));
        caps.setCapability("build", System.getProperty(BUILD_TAG, "unspecified"));
        caps.setCapability("captureHtml", Boolean.TRUE);
        caps.setCapability("seleniumVersion", "2.46.0");
        try {
            driver = new RemoteWebDriver(new URL("http://" + authentication.getUsername() + ":" + authentication.getAccessKey() + "@ondemand.saucelabs.com:80/wd/hub"), caps);
            this.sessionId = ((RemoteWebDriver)driver).getSessionId().toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return new Object[][]{{driver, testName}};
    }

}
