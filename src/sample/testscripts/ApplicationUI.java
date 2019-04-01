package sample.testscripts;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.github.web.automation.Javascript_Actions;
import com.github.web.automation.PageActions;
import com.github.web.automation.Verifications.ByType;

import enums.LocalBrowser;
import io.github.yash777.driver.Browser;
import io.github.yash777.driver.Drivers;
import io.github.yash777.driver.WebDriverException;

public class ApplicationUI {
	static RemoteWebDriver driver;
	static JavascriptExecutor jse;
	static WebDriverWait explicitWait;
	public static void main(String[] args) throws WebDriverException, IOException {
		Drivers drivers = new Drivers();
		
		boolean useChrome = false;
		
		if (useChrome) {
			String driverPath = drivers.getDriverPath(Browser.CHROME, 63, "");
			System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, driverPath);
			
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			driver = new ChromeDriver( capabilities );
			
			
		} else {
			System.out.println("Marinto Driver is for FF-Version less than 47.");
			String driverPath = drivers.getDriverPath(Browser.FIREFOX, 65, "v0.24.0");
			System.setProperty("webdriver.gecko.driver", driverPath );
			System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");
			System.setProperty(FirefoxDriver.SystemProperty.BROWSER_BINARY, "C:/Program Files (x86)/Mozilla Firefox/firefox.exe");
			
			// sets the app.update.disabledForTesting preference to prevent Firefox >= 65
			// from automatically updating whilst under automation. https://bugzilla.mozilla.org/show_bug.cgi?id=1511312
			//System.setProperty("app.update.disabledForTesting", "false" );
			DesiredCapabilities capabilities = DesiredCapabilities.firefox();
			driver = new FirefoxDriver( capabilities );
		
		}
		
		explicitWait = new WebDriverWait(driver, 10);
		jse = (JavascriptExecutor) driver;
		
		//Maximize browser window
		driver.manage().window().maximize();
		
		//spiceJet();
		emicalculator(false);
		//stackOverflow();
		
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
	public static void stackOverflow() {
		driver.navigate().to("https://stackoverflow.com/users/5081877/yash");
		
		PageActions page = new PageActions((RemoteWebDriver) driver, false, LocalBrowser.FIREFOX);
		page.sendText_GeckoDriver("//*[@id='search']/div/input", ByType.XPATH_EXPRESSION, "20,00,000");
	}
	public static void emicalculator(boolean isGekoDriver) {
		if (isGekoDriver) {
			driver.navigate().to("https://emicalculator.net/");
			
			PageActions page = new PageActions((RemoteWebDriver) driver, false, LocalBrowser.FIREFOX);
			page.sendText_GeckoDriver("//input[@id='loanamount']", ByType.XPATH_EXPRESSION, "20,00,000");
			page.sendText_GeckoDriver("//input[@id='loaninterest']", ByType.XPATH_EXPRESSION, "10");
			page.sendText_GeckoDriver("//input[@id='loanterm']", ByType.XPATH_EXPRESSION, "15");
			page.webElementClick("//*[@id='emicalculatorinnerform']/div[7]/div/div/div[1]/label[1]", ByType.XPATH_EXPRESSION);

		} else {
			driver.get("https://emicalculator.net/");
			
			PageActions page = new PageActions((RemoteWebDriver) driver, false, LocalBrowser.CHROME);
			//page.sendText_Actions("//input[@id='loanamount']", "20,00,000");
			//page.click_sendTextUsingJavaScript("//input[@id='loanamount']", "20,00,000");
			page.sendText_GeckoDriver("//input[@id='loanamount']", ByType.XPATH_EXPRESSION, "20,00,000");
			//page.webElementClick("//div[@class='form-group lamount']/div[1]/div[1]/span", ByType.XPATH_EXPRESSION);
			
			page.sendText_GeckoDriver("//input[@id='loaninterest']", ByType.XPATH_EXPRESSION, "10");
			//page.webElementClick("//div[@class='sep form-group lint']/div[1]/div[1]/span", ByType.XPATH_EXPRESSION);
			
			page.sendText_GeckoDriver("//input[@id='loanterm']", ByType.XPATH_EXPRESSION, "15");
			page.webElementClick("//*[@id='emicalculatorinnerform']/div[7]/div/div/div[1]/label[1]", ByType.XPATH_EXPRESSION);
		}
		
	}
	
	public static void spiceJet() {
		//Go to URL which you want to navigate
		driver.get("https://spicejet.com/");
		
		clickElement("//input[@id='ctl00_mainContent_ddl_originStation1_CTXT'][1]");
		clickElement("//div[@id='ctl00_mainContent_ddl_originStation1_CTNR']//a[@text='Guwahati (GAU)']");
		clickElement("//input[@id='ctl00_mainContent_ddl_destinationStation1_CTXT'][1]");
		clickElement("//div[@id='ctl00_mainContent_ddl_destinationStation1_CTNR']//a[@text='Goa (GOI)']");
		
		Javascript_Actions jsObj = new Javascript_Actions();
		/*
		 * Exception in thread "main" org.openqa.selenium.WebDriverException: unknown error:
		 * Element <input type="text" readonly="readonly" id="ctl00_mainContent_view_date2" class="custom_date_pic required home-date-pick">
		 * is not clickable at point (784, 241). Other element would receive the click: <span class="ui-datepicker-month">...</span>
		 */
		//clickElement("//div[@class='picker-first2']//input[1]");
		jsObj.clickUsingJavaScript("//div[contains(@class, 'ui-datepicker-group-first')]//a[contains(@class, 'ui-state-active')]");
		
		jsObj.click_Actions("//div[@class='picker-second']//input[1]");
		jsObj.clickUsingJavaScript("//div[contains(@class, 'ui-datepicker-group-last')]//a[contains(@class, 'ui-state-active')]");
	}
	
	/*
var elem = document.evaluate("//div[@class='picker-second']", window.document, null, 9, null ).singleNodeValue;
elem.getAttribute("style");
elem.setAttribute("style", "display: block;opacity: 0.5;");
	 */
	public static void clickElement(String locator) {
		By findBy = By.xpath( locator );
		WebElement element = explicitWait.until(ExpectedConditions.elementToBeClickable( findBy ));
		element.click();
	}

}
