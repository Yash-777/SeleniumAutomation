package sample.testscripts;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Download SafariBrowser for Windows:
	https://en.softonic.com/download/safari/windows/post-download?sl=1
	https://support.apple.com/en_IN/downloads/safari
	
Form Safari browser hit this URL
	http://selenium-release.storage.googleapis.com/index.html?path=2.45/
	http://www.seleniumhq.org/download/
	
after download download go through wiki images.

https://itisatechiesworld.wordpress.com/2015/04/15/steps-to-get-selenium-webdriver-running-on-safari-browser/

if (document.evaluate){
	"var element_node = document.evaluate('/html/body/div[2]/div[1]', window.document, null, 9, null ).singleNodeValue;"
	"element_node.style.setProperty ('border', '3px solid "+color+"', 'important');"
}

 * @author yashwanth.m
 *
 */
public class SafariBrowserTest {
	public static void main(String[] args) {
		
		String driverExtensionPath = "E:\\SafariDriver48.safariextz";
		System.setProperty("webdriver.safari.driver", driverExtensionPath );
		
		DesiredCapabilities safari = DesiredCapabilities.safari();
		safari.setCapability("nativeEvents", false);
		safari.setJavascriptEnabled( true );
		
		SafariOptions options = new SafariOptions();
		options.setUseCleanSession(true);
		safari.setCapability(SafariOptions.CAPABILITY, options);
		//safari.setCapability("safari.options.dataDir", "your download folder path");
		
		WebDriver driver = new SafariDriver( safari );
		
		OpencartLogin obj = new OpencartLogin();
		obj.driver = (RemoteWebDriver) driver;
		
		driver.get( obj.appURL );
		
		/* Put an Implicit wait, this means that any search for elements on the page could take 
		the time the implicit wait is set for before throwing exception.
		http://toolsqa.com/selenium-webdriver/switch-commands/ */
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.MINUTES);
		driver.manage().window().maximize();
		
		// wait up to 10 seconds for the Codes detail page to load
		(new WebDriverWait(driver, 10)).until( new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				String title = driver.getTitle();
				System.out.println("Application Title : "+title);
				return title.equals("Account Login");
			}
		});
		
		WebElement user = driver.findElement(By.xpath( obj.userName ));
		user.sendKeys(obj.userKey);
		obj.sleepThread( 1000 * 2 );
		WebElement secret = driver.findElement(By.xpath( obj.password ));
		secret.sendKeys(obj.secretKey);
		obj.sleepThread( 1000 * 2 );
		WebElement loginbutton = driver.findElement(By.xpath( obj.login ));
		loginbutton.click();
		obj.sleepThread( 1000 * 2 );
		WebElement data = driver.findElement(By.xpath( obj.verify ));
		String text = data.getText();
		System.out.println("Text My Order ["+text+"]");
		
		if( text.equalsIgnoreCase( obj.verifyText ) ) {
			obj.drawBorder(obj.verify, "green");
		} else {
			obj.drawBorder(obj.verify, "red");
		}
		
		System.out.println("Enter something in console to quit the browser and driver.");
		try {
			System.in.read();
			System.in.read();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		
		obj.quitDriver();
	}
}
