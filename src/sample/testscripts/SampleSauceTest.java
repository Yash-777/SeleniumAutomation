package sample.testscripts;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

/**
 * <UL> Sauce Labs <a href="/display/DOCS/Sauce+Labs+Basics?src=sidebar">Basics</a>
 * <LI> <a href="https://github.com/saucelabs-sample-scripts">Sauce Labs Sample Scripts</a>
 * <LI> Sample Test <a href='https://github.com/saucelabs-sample-test-frameworks'>
 * Frameworks</a></LI>
 * </UL>
 * 
 * <UL>TestingBOT
 * <li>https://testingbot.com/support/getting-started/java.html</li>
 * </UL>
 * 
 * @author yashwanth.m
 *
 */
public class SampleSauceTest {

	public static void main(String[] args) throws Exception {

		DesiredCapabilities caps = DesiredCapabilities.chrome();
		caps.setCapability("platform", "Windows XP");
		caps.setCapability("version", "43.0");

		WebDriver driver = new RemoteWebDriver(new URL( RemoteSeleniumURL.getURL() ), caps);

		/**
		 * Goes to Sauce Lab's guinea-pig page and prints title
		 */
		driver.get("https://saucelabs.com/test/guinea-pig");
		System.out.println("title of page is: " + driver.getTitle());
		driver.quit();
	}
}