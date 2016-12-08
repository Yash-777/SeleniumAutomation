package sample.testscripts;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class OpencartLogin {
	public WebDriver driver;

	String appURL = "http://demo.opencart.com/index.php?route=account/login";
	
	String userName = "//*[@id='input-email']";
	String userKey = "yashwanth.merugu@gmail.com";
	
	String password = "//*[@id='input-password']";
	String secretKey = "MySecretPassword";
	
	String login = "//*[@id='content']/div/div[2]/div/form/input";
	
	String verify = "/html/body/div[2]/div[1]";
	String verifyText = "Warning: No match for E-Mail Address and/or Password.";
	
	public OpencartLogin( WebDriver driver ) {
		this.driver = driver;
	}
	
	public void Test( ) {
		driver.get( appURL );
		
		/* Put an Implicit wait, this means that any search for elements on the page could take 
		the time the implicit wait is set for before throwing exception.
		http://toolsqa.com/selenium-webdriver/switch-commands/ */
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		String title = driver.getTitle();
		
		System.out.println("Application Title : "+title);
		
		WebElement user = driver.findElement(By.xpath( userName ));
		user.sendKeys(userKey);
		
		WebElement secret = driver.findElement(By.xpath( password ));
		secret.sendKeys(secretKey);
		
		WebElement loginbutton = driver.findElement(By.xpath( login ));
		loginbutton.click();
		
		WebElement data = driver.findElement(By.xpath( verify ));
		String text = data.getText();
		System.out.println("Text My Order ["+text+"]");
		
		if( text.equals( verifyText ) ) {
			drawBorder(verify, "green");
		} else {
			drawBorder(verify, "red");
		}
		
	}
	
	/** 
	 * To highlight an element, it takes XPath and draw border around it.
	 * 
	 * @param xpath
	 * @param color the color around 
	 */
	public void drawBorder(String xpath, String color){
		JavascriptExecutor jse = (JavascriptExecutor) driver;
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
