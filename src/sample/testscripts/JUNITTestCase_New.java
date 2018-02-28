package sample.testscripts;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;

/**
 * https://stackoverflow.com/a/31677543/5081877
 * 
 * @author yashwanth.m
 *
 */
@FixMethodOrder(MethodSorters.DEFAULT)
public class JUNITTestCase_New {
	private static WebDriver driver;
	public static int random = 0;
	private String baseURL;
	// @BeforeClass : Executes only once for the Test-Class.
	@BeforeClass
	public static void setting_SystemProperties() {
		System.out.println("System Properties seting Key value.");
		
		System.setProperty("webdriver.chrome.driver", "D:\\chromedriver.exe"); // Chrome Driver Location.
	}
	
	// @Before : To execute once before ever Test.
	@Before
	public void test_Setup() {
		System.out.println("Launching Browser");
		if (random == 0) {
			driver = new ChromeDriver(); // Creates new SessionID & opens the Browser.
		} else {
			driver = new FirefoxDriver();
		}
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		SessionId sessionId = ((org.openqa.selenium.remote.RemoteWebDriver) driver).getSessionId();
		System.out.println("Session ID : " + sessionId );
	}
	
	// @Test : Testing scenarios.
	@Test
	public void robot_ScreenShot() throws AWTException, IOException{
		System.out.println("Robot Tset Screen Shot.");  
		baseURL = "http://searchsoa.techtarget.com/definition/stickiness";
			Robot robot = new Robot(); // CTRL+T new tab in Browser.
			
		driver.get(baseURL);
		
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			int width = (int) toolkit.getScreenSize().getWidth();
			int height = (int) toolkit.getScreenSize().getHeight();
			
			Rectangle area = new Rectangle(0, 0, width, height);
			BufferedImage bufferedImage = robot.createScreenCapture(area);
			ImageIO.write(bufferedImage, "png", new File("D:\\Screenshots\\JUNIT-Robot.png"));
			random += 1;
	}
	
	@Test
	public void selenium_ScreenShot() throws IOException {
		baseURL = "http://www.w3schools.com/css/css_positioning.asp";
		driver.get(baseURL);
		System.out.println("Selenium Screen shot.");
		File screenshotFile = ((RemoteWebDriver) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshotFile, new File("D:\\Screenshots\\JUNIT-Selenium.jpg"));
		random += 1;
	}
	
	// @After : To execute once after ever Test.
	@After
	public void test_Cleaning() {
		System.out.println("Closing Browser");
		baseURL = null;	 
		driver.close(); // Removing SessionID & Close the Browser.
		/*if you are not using driver.quit().
		 * then JUNIT-Test will not terminate untill you end chromedriver.exe process in Task-Manager.
		 * use CTRL+SHIFT+ESC to open TaskManager then goto processes-view find chromedriver.exe and then end the process manually.
		 * */
		driver.quit(); // Ends the Process by removing chromedriver.exe process from Task-Manager.
	}
	
	// @AfterClass : Executes only once before Terminating the Test-Class.
	@AfterClass
	public static void clearing_SystemProperties() {
		System.out.println("System Property Removing Key value.");
		System.clearProperty("webdriver.chrome.driver");
	}
}