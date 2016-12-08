package sample.testscripts;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import com.github.web.automation.Browser;


public class WebDriverAutomation extends Browser {
	public WebDriverAutomation(LocalBrowser driver) throws Exception {
		super(driver);
	}
	
	public WebDriverAutomation(LocalBrowser browserName, String browserVersion,
			String binaryPath, String driverPath,
			String seleniumVersion, String chromeDriverPack) {
		super(browserName, browserVersion, binaryPath, driverPath,
				seleniumVersion, chromeDriverPack);
	}
	
	public static void main(String[] args) {

		/*String browserVersion = "54";
		String binaryPath = "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe";
		String driverPath = null;
		String seleniumVersion = "2.53.0";
		String chromeDriverPack = "2.24";
		
		WebDriverAutomation browserObj = new WebDriverAutomation(LocalBrowser.CHROME, browserVersion, binaryPath, driverPath, seleniumVersion, chromeDriverPack );
		browserObj.initialSetUP();
		browserObj.test();*/
		
		try {
			WebDriverAutomation enumProps = new WebDriverAutomation( LocalBrowser.CHROME );
			enumProps.initialSetUP();
			enumProps.test();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void test() {
		
		this.new OpenCartLogin().Test();
		
		String version = responseCaps.getVersion();
		Object json = responseCaps.getCapability( responseCaps.getBrowserName() );
		
		System.out.println("JSON : "+json+"\n version : "+version);
		try {
			System.in.read();
			System.in.read();
		} catch (IOException e) {
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
	
	class OpenCartLogin {
		String appURL = "http://demo.opencart.com/index.php?route=account/login";
		
		String userName = "//*[@id='input-email']";
		String userKey = "yashwanth.merugu@gmail.com";
		
		String password = "//*[@id='input-password']";
		String secretKey = "MySecretPassword";
		
		String login = "//*[@id='content']/div/div[2]/div/form/input";
		
		String verify = "/html/body/div[2]/div[1]";
		String verifyText = "Warning: No match for E-Mail Address and/or Password.";
		
		String lastEle = "/html/body/footer/div/div/div[4]/h5";
		
		public void Test() {
			driver.get( appURL );
			
			/* Put an Implicit wait, this means that any search for elements on the page could take 
			the time the implicit wait is set for before throwing exception.
			http://toolsqa.com/selenium-webdriver/switch-commands/ */
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			driver.manage().window().maximize();
			String title = driver.getTitle();
			
			System.out.println("Application Title : "+title);
			
			WebElement user = driver.findElement(By.xpath( userName ));
			user.clear();
			user.sendKeys(userKey);
			
			screen.takeElementScreenshot(user);
			
			WebElement secret = driver.findElement(By.xpath( password ));
			secret.sendKeys(secretKey);
			
			screen.takeElementScreenshot(secret);
			
			WebElement loginbutton = driver.findElement(By.xpath( login ));
			
			screen.takeElementScreenshot(loginbutton);
			loginbutton.click();
			sleepThread( 1000 * 2 );
			
			WebElement data = driver.findElement(By.xpath( verify ));
			String text = data.getText();
			System.out.println("Text My Order ["+text+"]");
			
			if( text.equals( verifyText ) ) {
				drawBorder(verify, "green");
			} else {
				drawBorder(verify, "red");
			}
			
			screen.takeElementScreenshot(data);
			
			WebElement view = driver.findElement(By.xpath( lastEle ));
			screen.takeElementScreenshot(view);
		}
		
		/** 
		 * To highlight an element, it takes XPath and draw border around it.
		 * 
		 * @param xpath
		 * @param color the color around 
		 */
		public void drawBorder(String xpath, String color){
			WebElement element_node = driver.findElement(By.xpath(xpath));
			String elementStyle = "arguments[0].style.border='3px solid "+color+"'";
			if (driver.getClass().getName().contains("ie")) {
				jse.executeScript(elementStyle, element_node);
			}else {
				try {
					jse.executeScript(
					"if (document.evaluate){"
						+ "var element_node = document.evaluate('"+xpath+"', window.document, null, 9, null ).singleNodeValue;"
						+ "element_node.style.setProperty ('border', '3px solid "+color+"', 'important');"
					+"}"
						);
				} catch (Exception draw) {
					jse.executeScript(elementStyle, element_node);
				}
			}
		}
	}
}