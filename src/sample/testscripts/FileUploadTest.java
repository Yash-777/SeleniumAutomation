package sample.testscripts;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.github.web.automation.Browser;

import enums.ActionType;
import enums.LocalBrowser;

/**
 * https://sqa.stackexchange.com/a/12871/14385
 * 
 * @author yashwanth.m
 *
 */
public class FileUploadTest {

	static RemoteWebDriver driver;
	public static void main(String[] args) throws Exception {
		LocalBrowser browserType = LocalBrowser.CHROME;
		Browser browserObj = new Browser( browserType );
		
		System.out.println( browserObj.toString() );
		RemoteWebDriver webDriver = browserObj.getWebDriver();
		driver = webDriver;
		
		uploadTest( driver );
	}
	
	public static void uploadTest( RemoteWebDriver driver ) throws Exception {
		//driver.setFileDetector(new LocalFileDetector());
		String baseUrl = "file:///D:/fileUploadBytes.html";
		driver.get( baseUrl );
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		FileUpload("//input[1]", "D:\\log.txt", ActionType.SEND_KEYS);
		
		Thread.sleep( 1000 * 10 );
		
		FileUpload("//input[1]", "D:\\DB_SQL.txt", ActionType.WIN);
		
		Thread.sleep( 1000 * 10 );
		
		driver.quit();
	}
	public static boolean FileUpload(String locator, String filePath, ActionType type) {
		WebDriverWait explicitWait = new WebDriverWait(driver, 10);
		
		WebElement element = explicitWait.until(ExpectedConditions.elementToBeClickable( By.xpath(locator) ));
		if( type == ActionType.SEND_KEYS ) {
			element.sendKeys( filePath );
			return true;
		} else {
			try {
				element.click();
				Thread.sleep( 1000 * 3 );
				
				setClipboardData(filePath);
				
				Robot robot = new Robot();
				if( type == ActionType.MAC ) { // Apple's Unix-based operating system.
					
					// “Go To Folder” on Mac - Hit Command+Shift+G on a Finder window.
					// http://osxdaily.com/2011/08/31/go-to-folder-useful-mac-os-x-keyboard-shortcut/
					robot.keyPress(KeyEvent.VK_META);
					robot.keyPress(KeyEvent.VK_SHIFT);
					robot.keyPress(KeyEvent.VK_G);
					robot.keyRelease(KeyEvent.VK_G);
					robot.keyRelease(KeyEvent.VK_SHIFT);
					robot.keyRelease(KeyEvent.VK_META);
	
					// Paste the clipBoard content - Command ⌘ + V.
					robot.keyPress(KeyEvent.VK_META);
					robot.keyPress(KeyEvent.VK_V);
					robot.keyRelease(KeyEvent.VK_V);
					robot.keyRelease(KeyEvent.VK_META);
					
					// Press Enter (GO - To bring up the file.)
					robot.keyPress(KeyEvent.VK_ENTER);
					robot.keyRelease(KeyEvent.VK_ENTER);
					return true;
				} else if ( type == ActionType.WIN || type == ActionType.LINUX ) { // Ctrl + V to paste the content.
					
					robot.keyPress(KeyEvent.VK_CONTROL);
					robot.keyPress(KeyEvent.VK_V);
					robot.keyRelease(KeyEvent.VK_V);
					robot.keyRelease(KeyEvent.VK_CONTROL);
				}
				
				robot.delay( 1000 * 4 );
			
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
				return true;
			} catch (AWTException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * Sets the specified file-path to the ClipBoard.
	 * <UL>Copy data to ClipBoard as.
	 * <li> WIN [ Ctrl + C ] </li>
	 * <li> MAC [ Command ⌘ + C ]</li> - https://superuser.com/questions/371513/how-to-tell-full-path-of-file-on-mac
	 * 
	 * @param filePath - the transferable object representing the clipboard content.
	 */
	public static void setClipboardData(String filePath) {
		StringSelection stringSelection = new StringSelection( filePath );
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	}

}
