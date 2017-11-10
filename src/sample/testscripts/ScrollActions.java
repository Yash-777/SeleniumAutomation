package sample.testscripts;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.github.web.automation.Browser;

import enums.LocalBrowser;

/**
 * http://scrollmagic.io/examples/advanced/parallax_scrolling.html
 * 
 * @author yashwanth.m
 *
 */
public class ScrollActions {
	private static RemoteWebDriver driver;
	static JavascriptExecutor jse;
	private Browser browser;
	public ScrollActions( Browser browser ) throws Exception {
		this.browser = browser;
	}
	public static void main(String[] args) {
		try {
			Browser b = new Browser( LocalBrowser.FIREFOX );
			b.useWaitTimes = false;
			
			RemoteWebDriver webDriver = b.getWebDriver();
			driver = webDriver;
			jse = (JavascriptExecutor) driver;
			driver.manage().timeouts().pageLoadTimeout(3, TimeUnit.MINUTES);
			
			ScrollActions obj = new ScrollActions( b );
			//obj.test();
			obj.scrollPageElements();
			obj.scrollPage();
			
			System.out.println("Enter something in console to quit the browser and driver.");
			try {
				System.in.read();
				System.in.read();
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
			
			driver.close();
			driver.quit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void scrollPageElements() throws InterruptedException {
		driver.get("https://trello.com/b/LakLkQBW/jsfiddle-roadmap");
		/*
		 * var ele = $x("//a[@class='board-header-btn mod-show-menu js-show-sidebar']");
			console.log( ele );
			ele[0].click();

			var objDiv = $x("//div[@id='board']/div[1]/div[1]/div[2]")[0];
			console.log( objDiv );
	
			objDiv.scrollTop += 100; 
			//objDiv.scrollTop = objDiv.scrollHeight;
		*/
		WebElement element = driver.findElement(By.xpath("//div[@id='board']/div[1]/div[1]/div[2]") );
		for (int i = 0; i < 5; i++) {
			jse.executeScript("arguments[0].scrollTop += 200;", element);
		}
		
		WebElement horizontalbar = driver.findElement(By.id("board") );
		Actions action = new Actions(driver);
		Actions moveToElement = action.moveToElement( horizontalbar );
		for (int i = 0; i < 5; i++) {
			moveToElement.sendKeys(Keys.RIGHT).build().perform();
		}
		
	}
	
	public void scrollPage() throws InterruptedException {
		driver.get("https://stackoverflow.com/questions/33094727/selenium-scroll-till-end-of-the-page");
		
		Actions actions = new Actions(driver);
		WebElement element = driver.findElement(By.xpath("//body") );
		Actions scrollDown = actions.moveToElement( element );
		scrollDown.keyDown(Keys.CONTROL).sendKeys(Keys.END).build().perform();
		//jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");

		
		/*Actions scrollUP = actions.moveToElement( element );
		scrollUP.keyDown(Keys.CONTROL).sendKeys(Keys.HOME).build().perform();*/
		jse.executeScript("window.scrollTo(0, -document.body.scrollHeight)");
		/*window.scrollBy(0,800);
		window.scrollTo(0, -document.body.scrollHeight);
		scroll(0, -document.body.scrollHeight);*/
		
		scroll_Till_Element( "answer-33142037" );
	}
	
	public void scroll_Till_Element(String id) {
		WebElement element = driver.findElement(By.id( id ) );
		jse.executeScript("arguments[0].scrollIntoView(true);", element);
	}
}
