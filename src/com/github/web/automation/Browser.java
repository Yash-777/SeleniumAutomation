/**
 * 
 */
package com.github.web.automation;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.github.server.file.FileFromURL;
import com.github.server.file.ZIPExtracter;

/**
 * Browser class is to create Driver instance by dynamically providing driver executable.
 * @author yashwanth.m
 *
 */
public class Browser extends Platform {
	
	public enum LocalBrowser {
		FIREFOX, CHROME, IEXPLORE;
	}
	
	static Properties props = new Properties();
	
	protected DesiredCapabilities capabilities;
	protected RemoteWebDriver driver;
	protected JavascriptExecutor jse;
	protected Capabilities responseCaps;
	protected Browser.ScreenShot screen;
	
	protected LocalBrowser browserName;
	private String browserVersion;
	private String binaryPath;
	private String driverEXEPath;
	private String seleniumVersion;
	private String chromeDriverVersion;
	
	static {
		// Class Loader - Reads Form 'src' Folder (Stand Alone application)
		ClassLoader AppClassLoader = Browser.class.getClassLoader();
		try {
			props.load(AppClassLoader.getResourceAsStream("localBrowserDetails.properties"));
			System.out.println("===== Propertie [KEY : Values] =====");
			for(String key : props.stringPropertyNames()) {
				System.out.format("%s : %s \n", key, props.getProperty(key));
			}
			System.out.println("===== ===== ===== =====");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Browser( LocalBrowser localBrowser ) throws Exception {
		browserName = localBrowser;
		switch ( browserName ) {
			case FIREFOX:
				System.out.println("FIREFOX");
				this.browserVersion = props.getProperty("F_Version");
				this.binaryPath = props.getProperty("F_Binary");
				break;
			case CHROME:
				System.out.println("CHROME");
				this.browserVersion = props.getProperty("C_Version");
				this.binaryPath = props.getProperty("C_Binary");
				this.chromeDriverVersion =  props.getProperty("C_DriverPack");
				break;
			case IEXPLORE:
				System.out.println("IEXPLORE");
				this.browserVersion = props.getProperty("I_Version");
				this.binaryPath = props.getProperty("I_Binary");
				break;
			default:
				throw new Exception("No enum constant > UN Suppoeted Browser.");
		}
		this.seleniumVersion = props.getProperty("SeleniumVersion");
	}
	
	public Browser(LocalBrowser browserName, String browserVersion, String binaryPath,
			String driverPath, String seleniumVersion, String chromeDriverPack) {
		super();
		this.browserName = browserName;
		this.browserVersion = browserVersion;
		this.binaryPath = binaryPath;
		this.driverEXEPath = driverPath;
		this.seleniumVersion = seleniumVersion;
		this.chromeDriverVersion = chromeDriverPack;
	}
	
	public Browser() {	}
	
	public void initialSetUP() {
		super.toString();
		
		seleniumDriverSetUP();
		
		StringBuilder logFiles = new StringBuilder( userDir+"/Drivers/log/" );
		
		
		switch ( browserName ) {
		case FIREFOX:
			System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, logFiles+"firefoxAutomator.log");
			System.setProperty(FirefoxDriver.SystemProperty.DRIVER_XPI_PROPERTY, driverEXEPath);
			capabilities = new DesiredCapabilities();
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,
										UnexpectedAlertBehaviour.DISMISS);
			
			File file = new File( binaryPath );
			FirefoxBinary binary = new FirefoxBinary(file);
			FirefoxProfile profile = new FirefoxProfile();
			driver = new FirefoxDriver(binary, profile, capabilities);
			break;
		case CHROME:
			System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, driverEXEPath);
			System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, logFiles+"chromeServer.log");
			capabilities = DesiredCapabilities.chrome();
			
			Proxy proxy = new Proxy();
			proxy.setHttpProxy("security.mixed_content.block_active_content:false");
			capabilities.setCapability("proxy", proxy);
			capabilities.setCapability( CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,
										UnexpectedAlertBehaviour.DISMISS);
			
			ChromeOptions options = new ChromeOptions();
			options.setBinary( binaryPath );
			
			// test-type > will not show any warnings when you launch CHROME.
			String[] arguments = {"start-maximized","test-type","disable-webgl","blacklist-accelerated-compositing",
					"disable-accelerated-2d-canvas","disable-accelerated-compositing","disable-accelerated-layers",
					"disable-accelerated-plugins","disable-accelerated-video","disable-accelerated-video-decode",
					"disable-gpu","disable-infobars","blacklist-webgl"};
			options.addArguments(arguments);
			options.addArguments("chrome.switches", "--disable-extensions");
			capabilities.setCapability(ChromeOptions.CAPABILITY, options);
			
			driver = new ChromeDriver( capabilities );
			break;
		case IEXPLORE:
			System.setProperty(InternetExplorerDriverService.IE_DRIVER_EXE_PROPERTY, driverEXEPath);
			System.setProperty(InternetExplorerDriverService.IE_DRIVER_LOGFILE_PROPERTY, logFiles+"iexploreLog.log");
			
			capabilities = DesiredCapabilities.internetExplorer();
			capabilities.setCapability("initialBrowserUrl", "about:blank");
			capabilities.setCapability("ensureCleanSession", true);
			
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			capabilities.setCapability("ignoreProtectedModeSettings", true);
			capabilities.setCapability("ignore-certificate-error", true);
			capabilities.setCapability("requireWindowFocus", true);
			capabilities.setJavascriptEnabled(true);
			
			capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,
										UnexpectedAlertBehaviour.DISMISS);
			driver = new InternetExplorerDriver(capabilities);
			break;
		}
		
		responseCaps = ( (RemoteWebDriver) driver ).getCapabilities();
		jse = (JavascriptExecutor) driver;
		screen = this.new ScreenShot();;
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
	
	public void seleniumDriverSetUP() {
		if ( (driverEXEPath == null || driverEXEPath.equalsIgnoreCase("NULL") || driverEXEPath.equals("") ) 
				&& seleniumVersion != "" ) {
			if ( OS.contains("WINDOWS") || driverOS.equalsIgnoreCase("WIN") ) {
				// Local ZIP File path to save server file
				StringBuilder ZIPFile = new StringBuilder( userDir+"/Drivers/" );
	// http://selenium-release.storage.googleapis.com/2.53/IEDriverServer_Win32_2.53.0.zip
	// http://selenium-release.storage.googleapis.com/2.53/IEDriverServer_x64_2.53.0.zip
	// http://chromedriver.storage.googleapis.com/2.24/chromedriver_win32.zip
	// https://github.com/Yash-777/selenium/raw/master/py/selenium/webdriver/firefox/webdriver.xpi
				String ZIPFolder = null;
				String driverZIPURL = null;
				boolean isDriverExists = false;
				
				switch ( browserName ) {
				
				case IEXPLORE:
				if( true ) {
					String IE_DRIVER_Storage = "http://selenium-release.storage.googleapis.com";
					Matcher m = Pattern.compile("(\\d+.\\d+).(\\d+)").matcher(seleniumVersion);
					String IE_Pack = null;
					if( m.find() ) IE_Pack = m.group(1);
					System.out.println("IE Selenium Package : "+IE_Pack);
					
					if( IE_Pack != null) {
					if ( jvmBitVersion.equalsIgnoreCase("64") ) { // IEDriverServer_x64_2.53.0.zip
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
				}
				break;
				case CHROME:
				if( true ) {
					String CHROME_Driver_storage = "http://chromedriver.storage.googleapis.com";
					if ( browserVersion != null && !"".equalsIgnoreCase( browserVersion ) ) {
						driverZIPURL = String.format("%s/%s/chromedriver_win32.zip",
								CHROME_Driver_storage, chromeDriverVersion);
					} else {
						System.out.format("Chrome version is NULL : [%s] \n", browserVersion);
					}
					
					ZIPFolder = ZIPFile.append("Chrome/"+chromeDriverVersion).toString();
					
					createDirectory( ZIPFolder );
					
					File ieFile = new File( ZIPFolder+"/chromedriver.exe");
					if ( ieFile.exists() ) {
						isDriverExists = true;
						driverEXEPath = ieFile.getAbsolutePath();
						System.out.println("Chrome Driver Exists Locally : "+driverEXEPath);
					} else {
						ZIPFile.append("/chromedriver_win32.zip");
					}
				}
				break;
				case FIREFOX:
					StringBuffer extensionURL = new StringBuffer();
					extensionURL.append("https://github.com/Yash-777/SeleniumDrives/raw/master/py/selenium/webdriver/firefox/");
					
					String firefoxFile = null;
					String[] array53 = {"39", "40", "41", "42", "43", "44", "45"};
					String[] array47 = {"34", "35", "36", "37", "38"};
					String[] array44 = {"24", "31", "32", "33"};
					
					for(String i : array53) {
						if(browserVersion.equals(i)) {
							firefoxFile = ZIPFile.append("FireFox/2.53/webdriver.xpi").toString();
							System.out.println("FF 2.53 Pack. FF version b/w [39 ~ 45]");
							createDirectory( ZIPFolder + "2.53");
							extensionURL.append("2.53/webdriver.xpi");
						}
					}
					for(String i : array47) {
						if(browserVersion.equals(i)) {
							firefoxFile = ZIPFile.append("FireFox/2.47/webdriver.xpi").toString();
							System.out.println("FF 2.47 Pack. FF version b/w [34 ~ 38]");
							createDirectory( ZIPFolder + "2.47");
							extensionURL.append("2.47/webdriver.xpi");
						}
					}
					for(String i : array44) {
						if(browserVersion.equals(i)) {
							firefoxFile = ZIPFile.append("FireFox/2.47/webdriver.xpi").toString();
							System.out.println("FF 2.44 Pack. FF version b/w [24 ~ 33]");
							createDirectory( ZIPFolder + "2.44");
							extensionURL.append("webdriver.xpi");
						}
					}
					
					File ffFile = new File( firefoxFile );
					try {
						if ( !ffFile.exists() ) {
							System.out.println("Downloading FireFox Extension from server...");
							new FileFromURL( extensionURL.toString() ).downloadUsing_NIOChannel( firefoxFile );
						} else {
							System.out.println("Locally FireFox Extension is available.");
						}
						driverEXEPath = firefoxFile;
						isDriverExists = true;
					} catch (IOException e1) {
						e1.printStackTrace();
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
						System.out.println("Successfully got the driver from server.");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
		} else {
			System.out.println("User Specified Driver Path : "+driverEXEPath);
		}
	}
	
	@Override
	public String toString() {
		return "Browsers [browserName=" + browserName + ", browserVersion="
				+ browserVersion + ", binaryPath=" + binaryPath
				+ ", driverPath=" + driverEXEPath + "]";
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
		 * <UL> Fire Fox console.
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
	public void createDirectory( String folderName ) {
		File folder = new File( folderName );
		if ( folder.isDirectory() && !folder.exists()) {
			try {
				Files.createDirectories(Paths.get( folderName ));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.println("Created Directory Sucess fully : "+folderName);
		}
	}
}