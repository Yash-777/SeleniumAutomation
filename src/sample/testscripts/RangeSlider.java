package sample.testscripts;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.github.web.automation.Browser;

import enums.LocalBrowser;

/**
 * http://rangeslider.js.org/
 * 
 * @author yashwanth.m
 *
 */
public class RangeSlider {
	private static RemoteWebDriver driver;
	Browser browser;
	public RangeSlider( Browser browser ) throws Exception {
		this.browser = browser;
	}
	public static void main(String[] args) {
		try {
			Browser b = new Browser( LocalBrowser.OPERA );
			b.useWaitTimes = false;
			
			RemoteWebDriver webDriver = b.getWebDriver();
			driver = webDriver;
			
			RangeSlider obj = new RangeSlider( b );
			obj.automateApplication( driver );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static WebDriverWait explicitWait;
	void automateApplication( RemoteWebDriver driver ) {
		driver.get( "http://rangeslider.js.org/" );
		driver.manage().window().maximize();
		explicitWait = new WebDriverWait( driver, 1000 * 60 * 2 );
		//Thread.sleep( 1000 * 60 * 1 );
		
		//rangeSlidePointer("//div[@id='js-rangeslider-0']/div[2]", 147, 0);
		//To slide using jQuery
		slideUsingJQuery( "//div[@id='js-rangeslider-0']/div[2]", 100 );
		
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
	void rangeSlidePointer( String locator, int xOffset, int yOffset ) {
		By findBy = By.xpath( locator );
		WebElement sliderElement = explicitWait.until(ExpectedConditions.elementToBeClickable( findBy ));
		Actions moveSlider = new Actions(driver);
		Action action = moveSlider.dragAndDropBy(sliderElement, xOffset, yOffset).release().build();
		// Actions action = moveSlider.moveToElement(sliderElement).clickAndHold().moveByOffset(xOffset,yOffset).release();
		action.perform();
	}
	
	public void slideUsingJQuery( String locator, int value ) {
		By findBy = By.xpath( locator );
		WebElement sliderElement = driver.findElement(By.cssSelector("input[type='range']"));
		((JavascriptExecutor)driver).executeScript("$(arguments[0]).val("+value+").change()", sliderElement );
	}
}
