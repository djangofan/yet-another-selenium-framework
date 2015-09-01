package qa.se.framework;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Sleeper;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by austenjt on 8/29/2015.
 */
public class ShootoutSuiteTestBase extends SuiteTestBase
{
    public ShootoutHelper shootoutHelper;

    protected ShootoutSuiteTestBase()
    {
        shootoutHelper = new ShootoutHelper();
    }

    /**
     * Setup before each test.
     *
     * @param params is a reference to the parameters of the current test method
     * @throws Exception thrown if any errors occur in the creation of the WebDriver instance
     */
    @BeforeMethod
    public void setUp(Object[] params) {
        WebDriver driver = (WebDriver) params[0];
        driver.get("http://tutorialapp.saucelabs.com");
    }

    /**
     * Creates a new {@link RemoteWebDriver} instance to be used to run WebDriver tests on SauceLabs.
     * Should return one array item per
     *
     * Platform reference:
     * https://code.google.com/p/selenium/source/browse/java/client/src/org/openqa/selenium/Platform.java
     */
    @DataProvider(name = "assembleTestParams", parallel = true)
    public Object[][] assembleTestParams(ITestContext context, Method method) {
        WebDriver driver = null;
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(CapabilityType.BROWSER_NAME, context.getCurrentXmlTest().getParameter("browser"));
        caps.setCapability(CapabilityType.VERSION, context.getCurrentXmlTest().getParameter("browserVersion"));
        caps.setCapability(CapabilityType.PLATFORM, context.getCurrentXmlTest().getParameter("platform"));
        String testIdentifier = this.getClass().getSimpleName() + "-" + method.getName();
        caps.setCapability("name", testIdentifier);
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
        return new Object[][]{{driver, testIdentifier}};
    }

    @AfterMethod
    public void tearDown(Object[] params) {
        WebDriver driver = (WebDriver) params[0];
        try {
            Sleeper.SYSTEM_SLEEPER.sleep(new org.openqa.selenium.support.ui.Duration(5, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.quit();
    }

}
