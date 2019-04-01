package sample.testscripts;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.yash777.driver.Browser;
import io.github.yash777.driver.Drivers;
import io.github.yash777.driver.WebDriverException;

public class SendKeys_KeysChord {
	static WebDriver driver;
	static WebDriverWait explicitWait;
	public static void main(String[] args) throws WebDriverException, IOException {
		// SeleniumWebDrivers API « https://yash-777.github.io/SeleniumWebDrivers/
		Drivers drivers = new Drivers();
		String driverPath = drivers.getDriverPath(Browser.FIREFOX, 56, "v0.19.1");
		System.setProperty("webdriver.gecko.driver", driverPath );
		System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "D:/firefoxStack.log");
		System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");
		System.setProperty(FirefoxDriver.SystemProperty.BROWSER_BINARY, "C:/Program Files (x86)/Mozilla Firefox/firefox.exe");
		
		
		/*FirefoxOptions options = new FirefoxOptions(); // Selenium 3.12.0
		options.setLogLevel(FirefoxDriverLogLevel.TRACE);
		driver = new FirefoxDriver(options);*/
		
		driver = new FirefoxDriver();
		
		//Maximize browser window
		driver.manage().window().maximize();
		
		explicitWait = new WebDriverWait(driver, 20);
		
		emicalculator();
		
		System.out.println("Enter something in console to quit the browser and driver.");
		try {
			System.in.read();
			System.in.read();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		
		driver.close();
		driver.quit();
	}
	public static void emicalculator() {
		driver.navigate().to("https://emicalculator.net/");
		sendText_KeyChord("//input[@id='loanamount']", "20,00,000");
		sendText_KeyChord("//input[@id='loaninterest']", "10");
		sendText_KeyChord("//input[@id='loanterm']", "15");
		
		driver.findElement(By.xpath( "//*[@id='emicalculatorinnerform']/div[7]/div/div/div[1]/label[1]" )).click();
	}
	public static void sendText_KeyChord(String locator, String textToSend) {
		By findBy = By.xpath( locator );
		explicitWait.until(ExpectedConditions.visibilityOfElementLocated( findBy ));
		WebElement webElement = driver.findElement( findBy );
		webElement.sendKeys(Keys.chord(Keys.META, "a"), textToSend); // Keys.CONTROL, Keys.COMMAND - MAC
	}
}
