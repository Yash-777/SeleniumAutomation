package sample.testscripts;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.io.Zip;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.github.web.automation.Browser;
import com.google.common.collect.ImmutableMap;

import enums.ActionType;
import enums.LocalBrowser;

/**
 * https://sqa.stackexchange.com/a/12871/14385
 * 
 * @author yashwanth.m
 *
 */
public class FileUploadDownloadTest {

	static RemoteWebDriver driver;
	public static void main(String[] args) throws Exception {
		LocalBrowser browserType = LocalBrowser.CHROME;
		Browser browserObj = new Browser( browserType );
		
		System.out.println( browserObj.toString() );
		RemoteWebDriver webDriver = browserObj.getWebDriver();
		driver = webDriver;
		
		String baseUrl = "https://www.filehosting.org/";
		driver.get( baseUrl );
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		FileUpload("//input[@name='upload_file'][@type='file'][1]", "D:\\log.txt", ActionType.SEND_KEYS);
		
		//uploadTest( driver );
		//fileDownload();
		
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
	
	/**
	 * Download files from browser, Save in a specified folder on hard disk.
	 * http://qavalidation.com/2016/03/selenium-downloadfiles-chrome.html/
	 */
	public static void fileDownload() {
		System.setProperty("webdriver.chrome.driver","./Drivers/Chrome/2.24/chromedriver.exe");
		String downloadFilepath = "E:\\download";
		
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", downloadFilepath);
		ChromeOptions options = new ChromeOptions();
		HashMap<String, Object> chromeOptionsMap = new HashMap<String, Object>();
		options.setExperimentalOption("prefs", chromePrefs);
		options.addArguments("--test-type");
		options.addArguments("--disable-extensions"); //to disable browser extension popup
		
		DesiredCapabilities cap = DesiredCapabilities.chrome();
		cap.setCapability(ChromeOptions.CAPABILITY, chromeOptionsMap);
		cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		cap.setCapability(ChromeOptions.CAPABILITY, options);
		driver = new ChromeDriver(cap);  
			driver.get("http://www.seleniumhq.org/download/");
			driver.findElement(By.linkText("32 bit Windows IE")).click();
	}
	public static void uploadTest( RemoteWebDriver driver ) throws Exception {
		//driver.setFileDetector(new LocalFileDetector());
		String baseUrl = "file:///D:/Yashwanth/D%20Drive/YashGt/SeleniumDriverAutomation/fileUploadBytes.html";
		driver.get( baseUrl );
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		FileUpload("//input[1]", "D:\\log.txt", ActionType.SEND_KEYS);
		
		Thread.sleep( 1000 * 10 );
		
		FileUpload("//input[1]", "D:\\DB_SQL.txt", ActionType.WIN);
		
	}
	public static boolean FileUpload(String locator, String filePath, ActionType type) {
		WebDriverWait explicitWait = new WebDriverWait(driver, 10);
		
		WebElement element = explicitWait.until(ExpectedConditions.elementToBeClickable( By.xpath(locator) ));
		if( type == ActionType.SEND_KEYS ) {
			element.sendKeys( filePath );
			return true;
		} else if ( type == ActionType.FILE_DETECTOR ) {
			LocalFileDetector detector = new LocalFileDetector();
			File localFile = detector.getLocalFile( filePath );
			RemoteWebElement input = (RemoteWebElement) driver.findElement(By.xpath(locator));
			input.setFileDetector(detector);
			input.sendKeys(localFile.getAbsolutePath());
			input.click();
			/*
			 * String zip = new Zip().zipFile(localFile.getParentFile(), localFile);
			 * Response response = execute(DriverCommand.UPLOAD_FILE, ImmutableMap.of("file", zip));
			 * return (String) response.getValue();
			 */
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
