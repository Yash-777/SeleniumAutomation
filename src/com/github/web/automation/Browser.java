package com.github.web.automation;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Browser class is to create Driver instance by dynamically providing driver executable.
 * @author yashwanth.m
 *
 */
public class Browser extends BrowserDrivers {
	
	static Properties props = new Properties();
	
	protected DesiredCapabilities capabilities;
	protected Capabilities responseCaps;
	
	
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
			// profile.setPreference("security.mixed_content.block_active_content", false);
			driver = new FirefoxDriver(binary, profile, capabilities);
			break;
		case CHROME:
			System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, driverEXEPath);
			System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, logFiles+"chromeServer.log");
			capabilities = DesiredCapabilities.chrome();
			
			Proxy proxy = new Proxy();
			proxy.setHttpProxy("security.mixed_content.block_active_content:false");
			capabilities.setCapability( CapabilityType.PROXY, proxy );
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
	
	@Override
	public String toString() {
		return "Browsers [browserName=" + browserName + ", browserVersion="
				+ browserVersion + ", binaryPath=" + binaryPath
				+ ", driverPath=" + driverEXEPath + "]";
	}

}
