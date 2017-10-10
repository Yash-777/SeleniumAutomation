package sample.testscripts;

import java.util.Date;

import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.github.web.automation.Browser;
import com.github.web.automation.PageActions;
import com.github.web.automation.Verifications.ByType;

import enums.LocalBrowser;

public class WebDriverAutomation {
	
	static RemoteWebDriver driver;
	Browser browser;
	public WebDriverAutomation(Browser browser) throws Exception {
		this.browser = browser;
	}
	
	static LocalBrowser browserType = LocalBrowser.CHROME;
	public static void main(String[] args) {

		try {
			String browserVersion = "52";
			String binaryPath = "C:\\Users\\yashwanth.m\\AppData\\Local\\Google\\Chrome\\Application\\chrome.exe";
			String driverPath = null;
			String seleniumVersion = "2.53.0";
			String chromeDriverPack = "2.23";
			Browser browserObj = new Browser(LocalBrowser.CHROME, browserVersion,
					binaryPath, driverPath, seleniumVersion, chromeDriverPack );
			
			//Browser browserObj = new Browser(browserType);
			
			System.out.println( browserObj.toString() );
			RemoteWebDriver webDriver = browserObj.getWebDriver();
			driver = webDriver;
			
			WebDriverAutomation enumProps = new WebDriverAutomation( browserObj );
			enumProps.test();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void test() {
		
		this.new OpenCartLoginInner().loginTest();
		
		String version = browser.responseCaps.getVersion();
		Object json = browser.responseCaps.getCapability( browser.responseCaps.getBrowserName() );
		
		System.out.println("JSON : "+json+"\n version : "+version);
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
	
	public void analyzeLog() {
		LogEntries logEntries = driver.manage().logs().get( LogType.BROWSER );
		for (LogEntry entry : logEntries) {
			System.out.println(new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage());
			//do something useful with the data
		}
	}
	
	class OpenCartLoginInner {
		String appURL = "http://demo.opencart.com/index.php?route=account/login";
		
		String userName = "//*[@id='input-email']";
		String userKey = "yashwanth.merugu@gmail.com";
		
		String password = "//*[@id='input-password']";
		String secretKey = "MySecretPassword";
		
		String login = "//*[@id='content']/div/div[2]/div/form/input";
		
		String verify = "/html/body/div[2]/div[1]";
		String verifyText = "Warning: No match for E-Mail Address and/or Password.";
		
		String lastEle = "/html/body/footer/div/div/div[4]/h5";
		
		public void loginTest() {
			driver.get( appURL );
			driver.manage().window().maximize();
			
			String title = driver.getTitle();
			System.out.println("Application Title : "+title);
			
			PageActions page = new PageActions( driver, true, browserType );
			System.out.println("Page Obj : "+ page.hashCode());
			boolean name = page.sendText( userName, ByType.XPATH_EXPRESSION, userKey );
			System.out.println("Text Sent : "+name);
			
			boolean pass = page.sendText( password, ByType.XPATH_EXPRESSION, secretKey );
			System.out.println("Text Sent : "+pass);
			
			boolean loginbutton = page.clickOnElement( login, ByType.XPATH_EXPRESSION );
			System.out.println("Element Clicked : "+loginbutton);
			
			browser.sleepThread( 1000 * 2 );
			
			boolean text = page.verifyText( verify, ByType.XPATH_EXPRESSION, verifyText);
			System.out.println("Text Sent : "+text);
			
		}
		
	}
}