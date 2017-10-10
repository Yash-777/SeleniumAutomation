package sample.testscripts;

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
			Browser b = new Browser( LocalBrowser.CHROME );
			RemoteWebDriver webDriver = b.getWebDriver();
			driver = webDriver;
			
			BroserWithExtension enumProps = new BroserWithExtension( b );
			enumProps.test();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void test() {
		OpencartLogin test = new OpencartLogin();
		test.loginTest( driver );
		
		String version = browser.responseCaps.getVersion();
		Object json = browser.responseCaps.getCapability( browser.responseCaps.getBrowserName() );
		
		System.out.println("JSON : "+json+"\n version : "+version);
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
}
