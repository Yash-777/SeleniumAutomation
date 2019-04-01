package com.github.web.automation;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;

import enums.LocalBrowser;

/**
 * Browser class is to create Driver instance by dynamically providing driver executable.
 * @author yashwanth.m
 *
 */
public final class Browser extends BrowserDrivers {
	
	static Properties props = new Properties();
	
	/**
	 * org.openqa.selenium.WebDriverException: waiting for doc.body failed
	 * driver.get("about:config");
	 */
	public boolean useWaitTimes = true;
	
	protected DesiredCapabilities capabilities;
	public Capabilities responseCaps;
	
	
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
				this.browserExtension = props.getProperty("F_Extensions");
				break;
			case CHROME:
				System.out.println("CHROME");
				this.browserVersion = props.getProperty("C_Version");
				this.binaryPath = props.getProperty("C_Binary");
				this.chromeDriverVersion =  props.getProperty("C_DriverPack");
				this.browserExtension = props.getProperty("C_Extensions");
				break;
			case IEXPLORE:
				System.out.println("IEXPLORE");
				this.browserVersion = props.getProperty("I_Version");
				this.binaryPath = props.getProperty("I_Binary");
				break;
			case OPERA:
				System.out.println("OPERA");
				this.browserVersion = props.getProperty("O_Version");
				this.binaryPath = props.getProperty("O_Binary");
				break;
			default:
				throw new Exception("No enum constant > UN Suppoeted Browser.");
		}
		this.useExtensions = Boolean.valueOf( props.getProperty("useExtensions") );
		this.privatebrowsing = Boolean.valueOf( props.getProperty("OpenInPrivateWindow") );
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
	
	public RemoteWebDriver getWebDriver() {
		if ( driver == null ) {
			
			try {
				System.out.println("Initial step for a Driver.");
				initialSetUP();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return driver;
	}
	
	public void initialSetUP() throws IOException {
		super.toString();
		
		seleniumDriverSetUP();
		
		StringBuilder logFiles = new StringBuilder( userDir+"/Drivers/log/" );
		
	try {	
		switch ( browserName ) {
		case FIREFOX:
			System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, logFiles+"firefoxAutomator.log");
			// WebDriver implementation of FireFox - FireFox WebDriver{SeleniumVersion} as Extension. 
			System.setProperty(FirefoxDriver.SystemProperty.DRIVER_XPI_PROPERTY, driverEXEPath);
			
			capabilities = new DesiredCapabilities();
			capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,
										UnexpectedAlertBehaviour.DISMISS);
			File file = new File( binaryPath );
			FirefoxBinary binary = new FirefoxBinary(file);
			
			// https://developer.mozilla.org/en-US/docs/Mozilla/Profile_Manager
			// https://support.mozilla.org/en-US/kb/profiles-where-firefox-stores-user-data#w_how-do-i-find-my-profile
			// To make a manual change to preferences, you can visit the URL about:config
			// To access file « Win + R « %APPDATA%\Mozilla\Firefox\Profiles\ - profile folder\prefs.js
			FirefoxProfile profile = new FirefoxProfile(); // about:support - Troubleshooting Information
			profile.setPreference("browser.startup.homepage", "about:blank");
			profile.setPreference("browser.startup.homepage_override.mstone", "ignore");
			
			profile.setPreference("xpinstall.signatures.required", false);
			profile.setPreference("toolkit.telemetry.reportingpolicy.firstRun", false);
			//Set language
			profile.setPreference("intl.accept_languages", "no,en-us,en");
			
			//some more prefs: https://support.mozilla.org/en-US/questions/1232918
			profile.setPreference( "app.update.enabled", false);
			profile.setPreference( "browser.tabs.autoHide", true);
			
			// Accept untrusted SSL certificates. - Defaults true.
			// Assume that the untrusted certificates will come from untrusted issuers or will be self signed.
			// capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			profile.setAcceptUntrustedCertificates( true );
			profile.setAssumeUntrustedCertificateIssuer( true );
			profile.setEnableNativeEvents( true );
			
			//profile.setPreference("general.useragent.override", "Any UserAgent String");
			// profile.setPreference("security.mixed_content.block_active_content", false);
			profile.setPreference("browser.link.open_newwindow.disabled_in_fullscreen", true);
			
			if( privatebrowsing )
				profile.setPreference("browser.privatebrowsing.autostart", true);
			
			if( useExtensions ) {
				File ext = new File( browserExtension );
				// The new FIREFOX instance initialized without SELENIUM IDE installed.
				if (ext.exists()) {
					profile.addExtension( ext ); // Selenium 2 - throws IOException
				}
			}
			
			/* http://qavalidation.com/2015/07/firefox-profile-and-preferences-in-selenium.html/
			ProfilesIni profini = new ProfilesIni();
			FirefoxProfile profile = profini.getProfile("MyFFProfile");*/
			//capabilities.setCapability(FirefoxDriver.PROFILE, profile);

			driver = new FirefoxDriver(binary, profile, capabilities);
			
			if( useWaitTimes ) {
				
				new FluentWait<WebDriver>(driver)
					.withTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
					.pollingEvery(5, java.util.concurrent.TimeUnit.SECONDS);
			}
			
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
			
			// https://sites.google.com/a/chromium.org/chromedriver/capabilities
			ChromeOptions options = new ChromeOptions();
			options.setBinary( binaryPath );
			//capabilities.setCapability("chrome.args",
			//Arrays.asList("--disable-web-security", "--allow-running-insecure-content", "--start-maximized"));
			
			if( useExtensions ) {
				options.addExtensions( new File( browserExtension ) );
				
				String installedExtensionPath = "C:/Users/yashwanth.m/AppData/Local/Google/Chrome/User Data/Default";
				options.addArguments("--always-authorize-plugins");
				options.addArguments("load-extension="+installedExtensionPath+"/Extensions/gighmmpiobklfepjocnamgkkbiglidom/3.15.0_0");
				options.addArguments("user-data-dir="+installedExtensionPath);
				
				//options.addArguments("chrome.switches", "--debug-packed-apps");
			} else {
				options.addArguments("chrome.switches", "--disable-extensions --disable-extensions-file-access-check "
						+ "--disable-extensions-http-throttling");
			}
			
			if( privatebrowsing ) { // Browser in Private Mode.
				options.addArguments("chrome.switches", "--incognito --non-secure-while-incognito");
			}
			
			// https://peter.sh/experiments/chromium-command-line-switches/
			String[] arguments = {"start-maximized","test-type","disable-webgl","blacklist-accelerated-compositing",
					"disable-accelerated-2d-canvas","disable-accelerated-compositing","disable-accelerated-layers",
					"disable-accelerated-plugins","disable-accelerated-video","disable-accelerated-video-decode",
					"disable-gpu","disable-infobars","blacklist-webgl"};
			options.addArguments(arguments);
			
			// List of Chrome command line switches to exclude that ChromeDriver by default passes when starting Chrome.
			// Do not prefix switches with --.
			String[] exclude = {"ignore-certificate-errors", "safebrowsing-disable-download-protection",
					"safebrowsing-disable-auto-update", "disable-client-side-phishing-detection"};
			options.addArguments("excludeSwitches", exclude.toString());
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
			
			if( privatebrowsing ) {
				capabilities.setCapability(InternetExplorerDriver.FORCE_CREATE_PROCESS, true); 
				capabilities.setCapability(InternetExplorerDriver.IE_SWITCHES, "-private");
			}
			
			if ( jvmBitVersion.equalsIgnoreCase("64") ) {
				capabilities.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
			}
			
			driver = new InternetExplorerDriver(capabilities);
			break;
		case OPERA:
			// https://stackoverflow.com/a/31586683/5081877
			System.setProperty("webdriver.opera.driver", driverEXEPath);
			
			// https://sny.no/2011/10/capabilities
			DesiredCapabilities capabilities = new DesiredCapabilities();
			//capabilities.setCapability("opera.binary", binaryPath);
			capabilities.setCapability("opera.no_quit", false);
			
			ChromeOptions options_Opera = new ChromeOptions();
			options_Opera.setBinary( binaryPath );
			String[] arguments_Opera = {"--use-fake-ui-for-media-stream","--ignore-certificate-errors"};
			options_Opera.addArguments(arguments_Opera);
			options_Opera.addArguments("chrome.switches", "--disable-extensions");
			
			if( privatebrowsing ) {
				options_Opera.addArguments("private");
			}
			
			capabilities.setCapability(ChromeOptions.CAPABILITY, options_Opera);
			capabilities.setCapability("opera.logging.level", "ALL");
			
			File logFile = new File(logFiles+"operadriver.log");
			System.out.println("Log file : "+logFile.toString());
			capabilities.setCapability("opera.loggin.file", logFile.toString());
			capabilities.setCapability("opera.port", 0);
			capabilities.setCapability("opera.launcher", binaryPath);
			capabilities.setCapability("opera.no_restart", true);
			
			driver = new OperaDriver(capabilities);
			break;
		default:
			System.err.println("Please select Browsers from this list - ['FF', 'CH', 'IE', 'Opera', 'Edge']");
			System.exit(0);
			break;
		}
		
		responseCaps = ( (RemoteWebDriver) driver ).getCapabilities();
		jse = (JavascriptExecutor) driver;
		screen = this.new ScreenShot();
	} catch ( WebDriverException e ) {
		// unknown error: no chrome binary at ***
		if ( e.getMessage().contains("binary at") ) {
			System.err.println("Invalid Binary Path - "+ e.getMessage() );
			System.exit(0);
		} else {
			System.err.println("WebDriver Exception « ");
			throw new WebDriverException( e.getMessage() );
		}
	}
	}
	
	@Override
	public String toString() {
		return "Browsers [browserName=" + browserName + ", browserVersion="
				+ browserVersion + ", binaryPath=" + binaryPath
				+ ", driverPath=" + driverEXEPath + "]";
	}

}
