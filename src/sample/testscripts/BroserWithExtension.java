package sample.testscripts;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.github.web.automation.Browser;

import enums.LocalBrowser;

/**
 * http://crxextractor.com/
 * https://developer.chrome.com/extensions/packaging
 * 
 * C:\Users\yashwanth.m\AppData\Local\Google\Chrome\User Data\Default\Extensions\@extensionID\
 * https://chrome.google.com/webstore/detail/take-webpage-screenshots/mcbpblocgmgfnpjjppndjkmgjaogfceg
 * https://chrome.google.com/webstore/detail/scirocco-recorder-for-chr/ibclajljffeaafooicpmkcjdnkbaoiih
 * 
 * https://addons.mozilla.org/en-US/firefox/addon/fireshot/versions/
 * 
 * https://github.com/QAPractice/SeleniumIDE
 * https://github.com/samitbadle/seleniumide/blob/master/selenium-ide-multi.xpi
 * https://addons.cdn.mozilla.net/user-media/addons/719653/selenium_ide_screen_capture_extension-1.0.7-fx.xpi
 * 
 * Adding browser extension at launch time.
 * 
 * @author yashwanth.m
 *
 */
public class BroserWithExtension {
	private static RemoteWebDriver driver;
	private Browser browser;
	public BroserWithExtension( Browser browser ) throws Exception {
		this.browser = browser;
	}
	public static void main(String[] args) {
		try {
			Browser b = new Browser( LocalBrowser.IEXPLORE );
			b.useWaitTimes = false;
			
			RemoteWebDriver webDriver = b.getWebDriver();
			driver = webDriver;
			
			BroserWithExtension obj = new BroserWithExtension( b );
			obj.test();
			//obj.firefoxScreenshot();
			
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
	public void firefoxScreenshot() throws AWTException, InterruptedException {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		/*Actions act = new Actions(driver);
		driver.get("about:config");
		
		WebElement elem = driver.findElement(By.id("showWarningNextTime"));
		jse.executeScript("arguments[0].checked=false;", elem);
		jse.executeScript("window.document.getElementById('warningButton').click();");
		jse.executeScript("var ele = window.document.getElementById('textbox');"+
				"ele.value = 'javascript'; ele.click();");
		Thread.sleep(1000);
		act.sendKeys(Keys.TAB).sendKeys(Keys.RETURN).perform(); Thread.sleep(1000); */
		
		
		driver.get("https://www.w3schools.com/");
		
		String saveFilePath = "";
		boolean captureFullScreen = false;
		if( captureFullScreen ) {
			try {
				File tempScreen = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				File screen = new File( saveFilePath );
				FileUtils.copyFile( tempScreen , screen);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (captureFullScreen) {
			
			// Opening the Developer Toolbar « https://developer.mozilla.org/en-US/docs/Tools/GCLI
			
			/*True if the screenshot should also include parts of the webpage which are outside the current scrolled bounds.
			screenshot d:\yash.png --fullpage

			screenshot --clipboard D:\file.png
			The name of the file (should have a '.png' extension) to which we write the screenshot.*/

			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_SHIFT);
			robot.keyPress(KeyEvent.VK_F2);
			robot.keyRelease(KeyEvent.VK_F2);
			robot.keyRelease(KeyEvent.VK_SHIFT);
			robot.delay( 1000 * 3 );
			
			Actions builder = new Actions(driver);
			builder
				.sendKeys("screenshot d:\\yash.png --fullpage")
				//.sendKeys(Keys.ENTER)
				.build().perform();
			
			int mask = InputEvent.BUTTON1_DOWN_MASK;
			//robot.mouseMove(100, 100);
			robot.mousePress( mask );
			robot.mouseRelease( mask );
			
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
		}
		
	}
	public void test() {
		/*OpencartLogin test = new OpencartLogin();
		test.loginTest( driver );*/
		driver.get("https://cldilpfapp02.cloudapp.net/Configurator/CnfgtrMenu/ICICI/Webmenu.aspx");
		
		String version = browser.responseCaps.getVersion();
		Object json = browser.responseCaps.getCapability( browser.responseCaps.getBrowserName() );
		
		System.out.println("JSON : "+json+"\n version : "+version);
		
	}
}
