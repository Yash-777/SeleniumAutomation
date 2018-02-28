package sample.testscripts;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class SampleTest {
	static WebDriver driver;
	public static void main(String[] args) throws IOException {
		/*String binaryPath = "D:\\BrowsersCustomInstalation\\FF_43\\firefox.exe";
		firefoxInstance( binaryPath );*/
		
		String binaryPath = "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe";
		String driverPath = "./Drivers/Chrome/2.33/chromedriver.exe";
		chromeInstance(binaryPath, driverPath, true);
		
		
		//Maximize browser window
		driver.manage().window().maximize();
		//Go to URL which you want to navigate
		driver.get("https://www.w3schools.com/html/");
		//Set  timeout  for 5 seconds so that the page may load properly within that time
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
		
		
		String jsTest = "window.history.pushState('Command', 'Title', '/chromium-command-line-switches/'); return 'JsWorking'";
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		
		try { // https://stackoverflow.com/q/3338642/5081877
			String JsWorking = (String) jse.executeAsyncScript(jsTest);
			System.out.println("Javascript : "+ JsWorking);
		} catch (org.openqa.selenium.TimeoutException e) {
			// org.openqa.selenium.TimeoutException: asynchronous script timeout: result was not received in 30 seconds
			
		}
		
		System.out.println("Enter something in console to quit the browser and driver.");
		try {
			System.in.read();
			System.in.read();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		
		driver.close();
	}
	
	static void chromeInstance(String binaryPath, String driverPath, boolean disableJS) {
		File file = new File( driverPath );
		System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, file.toString());
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		
		
		Proxy proxy = new Proxy();
		proxy.setHttpProxy("security.mixed_content.block_active_content:false");
		capabilities.setCapability( CapabilityType.PROXY, proxy );
		capabilities.setCapability( CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.DISMISS);
		
		// https://sites.google.com/a/chromium.org/chromedriver/capabilities
		ChromeOptions options = new ChromeOptions();
		if( binaryPath != null )
		options.setBinary( binaryPath );
		
		if (disableJS) {
			
			capabilities.setJavascriptEnabled(false);
			
			capabilities.setCapability("chrome.args",
					Arrays.asList("--js-flags", "--allow-running-insecure-content", "--disable-javascript-harmony-shipping"));
					
			/*// https://peter.sh/experiments/chromium-command-line-switches/
			String[] arguments = {"start-maximized","test-type","disable-webgl","blacklist-accelerated-compositing",
					"disable-accelerated-2d-canvas","disable-accelerated-compositing","disable-accelerated-layers",
					"disable-accelerated-plugins","disable-accelerated-video","disable-accelerated-video-decode",
					"disable-gpu","disable-infobars","blacklist-webgl"};
			options.addArguments(arguments);*/
		}

		
		// List of Chrome command line switches to exclude that ChromeDriver by default passes when starting Chrome.
		// Do not prefix switches with --.
		String[] exclude = {"ignore-certificate-errors", "safebrowsing-disable-download-protection",
				"safebrowsing-disable-auto-update", "disable-client-side-phishing-detection"};
		options.addArguments("excludeSwitches", exclude.toString());
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		
		driver = new ChromeDriver( capabilities );
	}
	static void firefoxInstance( String binaryPath ) {
		File file = new File( binaryPath );
		FirefoxBinary binary = new FirefoxBinary(file);
		
		//ProfilesIni myProfile = new ProfilesIni();
		//FirefoxProfile profile = myProfile.getProfile("Yash");

		FirefoxProfile profile = new FirefoxProfile(); // about:support - Troubleshooting Information
		
		profile.setPreference("browser.startup.homepage", "about:blank");
		profile.setPreference("browser.startup.homepage_override.mstone", "ignore");
		//Set language
		profile.setPreference("intl.accept_languages", "no,en-us,en");
		//some more prefs:
		profile.setPreference( "app.update.enabled", false);
		profile.setPreference( "browser.tabs.autoHide", true);
		profile.setPreference("xpinstall.signatures.required", false);
		profile.setPreference("toolkit.telemetry.reportingpolicy.firstRun", false);
		/*ExtensionsGenerator extn = new ExtensionsGenerator();
		extn.addExtension(arg0, arg1, arg2);*/
		/*File ext = new File( "D:\\Yashwanth\\EXTENSION\\IDE.xpi" );
		profile.addExtension( ext );*/
		//profile.setPreference("extensions.selenium-ide.preflight.performpreflight",true);
		//profile.setPreference("extensions.selenium-ide.version", "2.9.1.1-signed");
		
		driver = new FirefoxDriver(binary, profile, new DesiredCapabilities());
	}
}
