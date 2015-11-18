package qa.se.framework;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.testng.SauceOnDemandAuthenticationProvider;
import org.testng.ITestContext;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * Suite-level test base class.
 *
 * This TestBase requires you define a DataProvider called assembleTestParams.
 */
public abstract class TestNGSuiteTestBase implements SauceOnDemandSessionIdProvider, SauceOnDemandAuthenticationProvider {

    protected static final String BUILD_TAG = "BUILD_TAG";
    protected SauceOnDemandAuthentication authentication;
    protected String sessionId;

    @BeforeSuite
    @Parameters( {"configFile"} )
    public void setUpSuite(@Optional("src/test/resources/config.properties") String configFile)
    {
        InputStream input = null;
        Properties props = new Properties();
        try {
            input = new FileInputStream(configFile);
            props.load(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        authentication = new SauceOnDemandAuthentication(props.getProperty("sauceUser"), props.getProperty("sauceKey"));
    }

    @DataProvider(name = "assembleTestParams", parallel = true)
    public abstract Object[][] assembleTestParams(ITestContext context, Method method);

    @Override
    public SauceOnDemandAuthentication getAuthentication()
    {
        return this.authentication;
    }

    @Override
    public String getSessionId() {
        if ( this.sessionId == null ) this.sessionId = "---";
        return sessionId;
    }

}
