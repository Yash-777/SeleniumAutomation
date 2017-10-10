package sample.testscripts;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;

public class SampleTest {
	public static void main(String[] args) throws IOException {
		File file = new File( "D:\\BrowsersCustomInstalation\\FF_43\\firefox.exe" );
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
		
		WebDriver driver = new FirefoxDriver(binary, profile, new DesiredCapabilities());
		//Maximize browser window
		driver.manage().window().maximize();
		//Go to URL which you want to navigate
		driver.get("https://www.w3schools.com/html/");
		//Set  timeout  for 5 seconds so that the page may load properly within that time
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
		
		System.out.println("Enter something in console to quit the browser and driver.");
		try {
			System.in.read();
			System.in.read();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		
		//close firefox browser
		driver.close();
	}
}
