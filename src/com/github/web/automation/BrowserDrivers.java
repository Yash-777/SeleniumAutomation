package com.github.web.automation;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.github.server.file.FileFromURL;
import com.github.server.file.ZIPExtracter;

import enums.LocalBrowser;

/**
 * This class makes driver automation. If the driver is not available in the Application path
 * then it is going to download from the cloud and saves to application Drivers directory and
 * servers from that location.
 * 
 * <UL> DRIVERS:
 * <LI> <B>Firefox</B> - <I>Firefox driver is included in the selenium-server-standalone.jar 
 * available in the downloads</I></LI>
 * <LI> <B>Internet Explorer</B> - <I>The InternetExplorerDriver is a standalone server 
 * which implements WebDriver's wire protocol.</I>
 * <p><b>Note:</b> Use only 32 bit IE driver to increase performance in SendKeys, Screenshot and Links.</p></LI>
 * <LI> <B>Chrome</B> - <I>ChromeDriver is a standalone server which implements WebDriver's 
 * wire protocol for Chromium.</I></LI>
 * <LI> <B>Opera</B> - <I>OperaDriver is a vendor-supported WebDriver implementation developed
 *  by Opera Software and volunteers that implements WebDriver API for Opera.</I>
 * 	<UL> <B><a href="https://stackoverflow.com/a/31586683/5081877" >OperaChromiumDriver:</a></B>
 * 		<LI>OperaChromiumDriver is a WebDriver implementation derived from ChromeDriver 
 *  and adapted by Opera. http://www.opera.com/docs/history/</LI>
 *  </UL>
 * </LI>
 * <LI> <B>Safari</B> - <I>The SafariDriver is implemented as a Safari browser extension.
 * The driver inverts the traditional client/server relationship and communicates with the 
 * WebDriver client using WebSockets.</I></LI>
 * 
 * @author yashwanth.m
 *
 */
public class BrowserDrivers extends Platform {

	protected LocalBrowser browserName;
	protected String browserVersion;
	protected String binaryPath, browserExtension;
	protected String driverEXEPath;
	protected String chromeDriverVersion;
	protected String seleniumVersion;
	protected Boolean useExtensions = false, privatebrowsing = false; // https://stackoverflow.com/a/36332841/5081877
	
	protected RemoteWebDriver driver;
	protected JavascriptExecutor jse;
	
	protected static BrowserDrivers.ScreenShot screen;

	public void seleniumDriverSetUP() {
		if ( (driverEXEPath == null || driverEXEPath.equalsIgnoreCase("NULL") || driverEXEPath.equals("") ) 
				&& seleniumVersion != "" ) {
			if ( OS.contains("WINDOWS") || driverOS.equalsIgnoreCase("WIN") ) {
				// Local ZIP File path to save server file
				StringBuilder ZIPFile = new StringBuilder( userDir+"/Drivers/" );
	// http://selenium-release.storage.googleapis.com/2.53/IEDriverServer_Win32_2.53.0.zip
	// http://selenium-release.storage.googleapis.com/2.53/IEDriverServer_x64_2.53.0.zip
	// http://chromedriver.storage.googleapis.com/2.24/chromedriver_win32.zip
	// https://github.com/Yash-777/SeleniumDrives/raw/master/py/selenium/webdriver/firefox/webdriver.xpi
	
	// For now to drive the Chromium-based Opera you�ll need to use the RemoteWebDriver
	// https://github.com/operasoftware/operachromiumdriver/releases/download/v0.2.2/operadriver_win32.zip
				String ZIPFolder = null;
				String driverZIPURL = null;
				boolean isDriverExists = false;
				
				switch ( browserName ) {
				
				case IEXPLORE:
					String IE_DRIVER_Storage = "http://selenium-release.storage.googleapis.com";
					Matcher m = Pattern.compile("(\\d+.\\d+).(\\d+)").matcher(seleniumVersion);
					String IE_Pack = null;
					if( m.find() ) IE_Pack = m.group(1);
					System.out.println("IE Selenium Package : "+IE_Pack);
					
					// Use only 32 bit IE driver to increase performance in SendKeys, Screenshot and Links.
					if( IE_Pack != null) {
					if ( jvmBitVersion.equalsIgnoreCase("64 - Performance Increase") ) { // IEDriverServer_x64_2.53.0.zip
						driverZIPURL = String.format("%s/%s/IEDriverServer_x64_%s.zip",
									IE_DRIVER_Storage, IE_Pack, seleniumVersion);
						ZIPFolder = ZIPFile.toString() + "IExplore/64";
						ZIPFile.append("IExplore/64/IEDriverServer_x64_"+seleniumVersion+".zip");
						
					} else { // IEDriverServer_Win32_2.53.0.zip
						driverZIPURL = String.format("%s/%s/IEDriverServer_Win32_%s.zip",
								IE_DRIVER_Storage, IE_Pack, seleniumVersion);
						ZIPFolder = ZIPFile.toString() + "IExplore/32";
						ZIPFile.append("IExplore/32/IEDriverServer_x64_"+seleniumVersion+".zip");
					}
					
					createDirectory( ZIPFolder );
					
					File ieFile = new File( ZIPFolder+"/IEDriverServer.exe");
					if ( ieFile.exists() ) {
						isDriverExists = true;
						driverEXEPath = ieFile.getAbsolutePath();
						System.out.println("IE Driver Exists Locally : "+driverEXEPath);
					}
					}
				break;
				case CHROME:
					String CHROME_Driver_storage = "http://chromedriver.storage.googleapis.com";
					if ( browserVersion != null && !"".equalsIgnoreCase( browserVersion ) ) {
						driverZIPURL = String.format("%s/%s/chromedriver_win32.zip",
								CHROME_Driver_storage, chromeDriverVersion);
					} else {
						System.out.format("Chrome version is NULL : [%s] \n", browserVersion);
					}
					
					ZIPFolder = ZIPFile.append("Chrome/"+chromeDriverVersion).toString();
					
					createDirectory( ZIPFolder );
					
					File ChromeFile = new File( ZIPFolder+"/chromedriver.exe");
					if ( ChromeFile.exists() ) {
						isDriverExists = true;
						driverEXEPath = ChromeFile.getAbsolutePath();
						System.out.println("Chrome Driver Exists Locally : "+driverEXEPath);
					} else {
						ZIPFile.append("/chromedriver_win32.zip");
					}
				break;
				case OPERA:
					driverZIPURL = 
					"https://github.com/operasoftware/operachromiumdriver/releases/download/v0.2.2/operadriver_win32.zip";
					
					ZIPFolder = ZIPFile.append("Opera").toString();
					
					createDirectory( ZIPFolder );
					
					File OperaFile = new File( ZIPFolder+"/operadriver.exe");
					if ( OperaFile.exists() ) {
						isDriverExists = true;
						driverEXEPath = OperaFile.getAbsolutePath();
						System.out.println("Opera Driver Exists Locally : "+driverEXEPath);
					} else {
						ZIPFile.append("/operadriver_win32.zip");
					}
					break;
				case FIREFOX:
					StringBuffer extensionURL = new StringBuffer();
					extensionURL.append("https://github.com/Yash-777/SeleniumDrives/raw/master/py/selenium/webdriver/firefox/");
					
					ZIPFile.append("FireFox/");
							
					String firefoxFile = null, firefoxPack = null;
					String[] array53_1 = {"46", "47"};
					String[] array53 = {"39", "40", "41", "42", "43", "44", "45"};
					String[] array47 = {"34", "35", "36", "37", "38"};
					String[] array44 = {"24", "31", "32", "33"};
					
					for(String i : array53_1) {
						if(browserVersion.equals(i)) {
							firefoxPack = "2.53.1";
							System.out.println("FF 2.53.1 Pack. FF version b/w [46 ~ 47]");
						}
					}
					for(String i : array53) {
						if(browserVersion.equals(i)) {
							firefoxPack = "2.53";
							System.out.println("FF 2.53 Pack. FF version b/w [39 ~ 45]");
						}
					}
					for(String i : array47) {
						if(browserVersion.equals(i)) {
							firefoxPack = "2.47";
							System.out.println("FF 2.47 Pack. FF version b/w [34 ~ 38]");
						}
					}
					for(String i : array44) {
						if(browserVersion.equals(i)) {
							firefoxPack = "2.44";
							System.out.println("FF 2.44 Pack. FF version b/w [24 ~ 33]");
						}
					}
					if( firefoxPack != null ) {
						ZIPFolder = ZIPFile.append( firefoxPack ).toString();
						firefoxFile = ZIPFolder+"/webdriver.xpi".toString();
						createDirectory( ZIPFolder );
						extensionURL.append( firefoxPack +"/webdriver.xpi");
						File ffFile = new File( firefoxFile );
						try {
							if ( !ffFile.exists() ) {
								System.out.println("Downloading FireFox Driver Extension from server...");
								new FileFromURL( extensionURL.toString() ).downloadUsing_NIOChannel( firefoxFile );
							} else {
								System.out.println("Locally FireFox Driver Extension is available.");
							}
							driverEXEPath = firefoxFile;
							isDriverExists = true;
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					} else {
						System.err.println("Please select Firefox Browser version above 24.");
						System.exit(0);
					}
					break;
					
				}
				
				
				if ( !isDriverExists ) {
					try {
						String driverZIPLocal = ZIPFile.toString();
						
						System.out.println("DRIVER ZIP URL : "+driverZIPURL);
						System.out.println("Downloading from server...");
						new FileFromURL( driverZIPURL ).downloadUsing_NIOChannel( driverZIPLocal );
						driverEXEPath = new ZIPExtracter().exratctFileList( driverZIPLocal, ZIPFolder);
						System.out.println("Successfully got the driver from server.\n\t"+driverEXEPath);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
		} else {
			System.out.println("User Specified Driver Path : "+driverEXEPath);
		}
	}
	
	public static void createDirectory( String folderName ) {
		File folder = new File( folderName );
		if ( !folder.exists()) {
			try {
				Files.createDirectories(Paths.get( folderName ));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.println("Created Directory Sucess fully : "+folderName);
		} else {
			System.out.println("Directory available locally.");
		}
	}
	
	/**
	 * Causes the currently executing thread to sleep for the specified number of milliseconds.
	 * @param time	the length of time to sleep in milliseconds
	 */
	public void sleepThread(long millis) {
		try {
			Thread.sleep( millis );
		} catch (InterruptedException e) {
			System.out.println("Sleep Exception:"+ e.getMessage());
		}
	}
	
	public class ScreenShot {
		
		/**
		 * Captures screen as a file.
		 * 
		 * @return the File Name of the screenshot.
		 */
		public File captureScreenAsFile() {
			return ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		}
		
		// FF - Change File Name with ImageFolder.getsession().time.png
		/**
		 * <UL> FireFox console.
	<LI>FullPage « screenshot d:\yash.png --delay 1 --fullpage
	<LI>Window   « screenshot d:\\yash.png --chrome
	<LI>Element  « screenshot d:\\yash.png --delay 0 --selector  #toc
	</UL>
		 */
		public File takeElementScreenshot(WebElement element) {
			File screen = null, tempScreen = null;
			Long startTime = System.currentTimeMillis();
			try {
				//Point windowPosition = driver.manage().window().getPosition();
				Point viewPoint = ((Locatable) element).getCoordinates().inViewPort();
				System.out.format("Window View Location : [%d - %d] \n",
						viewPoint.x, viewPoint.y);
				
				Point elementLocation = element.getLocation();
				System.out.format("Element Location : [%d - %d] \n",
						elementLocation.x, elementLocation.y);
				
				switch ( browserName ) {
				case CHROME:
					if( viewPoint.y != elementLocation.y) {
						jse.executeScript("arguments[0].scrollIntoView(true);", element);
					}
					tempScreen = captureScreenAsFile();
					break;
				default:
					WrapsDriver wrapsDriver = (WrapsDriver) element;
					tempScreen = ((TakesScreenshot) wrapsDriver.getWrappedDriver()).getScreenshotAs(OutputType.FILE);
					Rectangle rectangle = new Rectangle(element.getSize().width, element.getSize().height);
					
					BufferedImage bufferedImage = ImageIO.read(tempScreen);
					
					BufferedImage cropImage = bufferedImage.getSubimage(
							elementLocation.x, elementLocation.y, rectangle.width, rectangle.height);
					ImageIO.write(cropImage, "png", tempScreen);
					break;
				}
				screen = new File( userDir+"/images/"+browserName+"_"+getSessionID()+"_"+startTime.toString()+".png" );
				FileUtils.copyFile(tempScreen, screen);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return screen;
		}
	}
	
	/**
	 * Browser session is going to create when browser is opened.
	 * 
	 * URL : http://127.0.0.1:4444/wd/hub/session
	 * <p>
	 * Example:<blockquote>
	 * <code>cmd=getNewBrowserSession&amp;1=*firefox&amp;2=http://www.google.com
	 * <br>Got result: 1140738083345
	 * <br>cmd=open&amp;1=http://www.google.com&amp;sessionId=1140738083345
	 * <br>Got result: OK
	 * <br>cmd=type&amp;1=q&amp;2=hello world&amp;sessionId=1140738083345
	 * <br>Got result: OK
	 * <br>cmd=testComplete&amp;sessionId=1140738083345
	 * <br>Got result: OK
	 * </code></blockquote>
	 * <h1>The "null" session</h1>
	 * <p>
	 * @return
	 */
	public String getSessionID() {
		return ((RemoteWebDriver) driver).getSessionId().toString();
	}
}
