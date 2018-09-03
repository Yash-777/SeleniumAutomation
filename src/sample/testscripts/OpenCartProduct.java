package sample.testscripts;

import org.openqa.selenium.remote.RemoteWebDriver;

import com.github.web.automation.Browser;
import com.github.web.automation.PageActions;
import com.github.web.automation.PageActions.Options;
import com.github.web.automation.Verifications.ByType;

import enums.ActionType;
import enums.LocalBrowser;

/**
 * @author yashwanth.m
 *
 */
public class OpenCartProduct {
	static String[] xpaths = {
			"MouseOver", "//nav[1]/div[2]/ul[1]/li[3]/a[1]",
			"MouseOver_Item", "//li[3]/div[1]/div[1]/ul[1]/li[2]/a[1]",

			"Image", "//div[3]/div[1]/div[1]/div[1]/a[1]/img[1]",

			"Radio", "//div[1]/div[1]/div[2]/label[1]",
			"Checkbox", "//div[2]/div[1]/div[2]/label[1]",
			"Text", "//div[3]/input[1]",
			// //*[@id="input-option217"] « "//select[1]" //*[@id="input-option217"]/option[2] « //select[1]/option[2]
			"Select", "//*[@id='input-option217']",
			"Select_Option", "//*[@id='input-option217']/option[2]",
			"Textarea", "//div[5]/textarea[1]",
			"File_Upload", "//div[6]/button[1]/i[1]",
			
			"Date_Calender", "//div[7]/div[1]/span[1]/button[1]/i[1]",
			"Date_Calender_Selection", "//div[4]/div[1]/div[1]/table[1]/tbody[1]/tr[4]/td[6]",
			
			"Time", "//div[8]/div[1]/input[1]",
			"Date & Time", "//div[9]/div[1]/input[1]",
			"Qty", "//div[10]/input[1]",
			
			"Button Cart", "//button[@id='button-cart']"
			
			//"Toggle_Tooltip", "//div[2]/div[1]/button[1]/i[1]",
	};
	
	static RemoteWebDriver driver;
	public static void main(String[] args) throws Exception {
		/*File file = new File( "D:\\BrowsersCustomInstalation\\FF_43\\firefox.exe" );
		FirefoxBinary binary = new FirefoxBinary(file);
		
		FirefoxProfile profile = new FirefoxProfile(); // about:support - Troubleshooting Information
		profile.setPreference("browser.startup.homepage", "about:blank");
		profile.setPreference("browser.startup.homepage_override.mstone", "ignore");
		profile.setPreference("intl.accept_languages", "no,en-us,en");
		profile.setPreference( "app.update.enabled", false);
		profile.setPreference( "browser.tabs.autoHide", true);
		profile.setPreference("xpinstall.signatures.required", false);
		profile.setPreference("toolkit.telemetry.reportingpolicy.firstRun", false);
		driver = new FirefoxDriver(binary, profile, new DesiredCapabilities());*/
		
		LocalBrowser browserType = LocalBrowser.CHROME;
		Browser browserObj = new Browser( browserType );
		
		System.out.println( browserObj.toString() );
		RemoteWebDriver webDriver = browserObj.getWebDriver();
		driver = webDriver;
		
		driver.manage().window().maximize();
		driver.get("https://demo.opencart.com/index.php");
		
		String title = driver.getTitle();
		System.out.println("Application Title : "+title);
		
		PageActions page = new PageActions( driver, true, browserType );
		System.out.println("Page Obj : "+ page.hashCode());
		
		// for(int i = 0; i < xpaths.length; i++ ) { xpaths[i] - Action; i++; xpaths[i] - Xpath; }
		
		boolean MouseOver = page.mouseHoverOnElement( xpaths[1], ByType.XPATH_EXPRESSION );
		System.out.println("mouseHoverOnElement : "+ MouseOver);
		boolean MouseOver_Item = page.clickOnElement( xpaths[3], ByType.XPATH_EXPRESSION );
		System.out.println("Element Clicked : "+ MouseOver_Item);
		
		boolean image = page.clickOnElement( xpaths[5], ByType.XPATH_EXPRESSION );
		System.out.println("Element Clicked : "+ image);
		
		boolean Radio = page.clickOnElement( xpaths[7], ByType.XPATH_EXPRESSION );
		System.out.println("Element Clicked : "+ Radio);
		boolean Checkbox = page.clickOnElement( xpaths[9], ByType.XPATH_EXPRESSION );
		System.out.println("Element Clicked : "+ Checkbox);
		
		boolean text = page.sendText( xpaths[11], ByType.XPATH_EXPRESSION, "Yashwnath" );
		System.out.println("Text Sent : "+ text);
		
		boolean Select = page.select( xpaths[13], ByType.XPATH_EXPRESSION );
		System.out.println("Element Clicked : "+Select);
		
		boolean Select_Option = page.selectOptions( xpaths[15], ByType.XPATH_EXPRESSION, Options.INDEX );
		System.out.println("Element Clicked : "+Select_Option);
		
		boolean Textarea = page.sendText( xpaths[17], ByType.XPATH_EXPRESSION, "TextArea Multiline Text." );
		System.out.println("Text Sent : "+ Textarea);
		
		boolean File_Upload = page.FileUpload( xpaths[19], ByType.XPATH_EXPRESSION, "D:\\log.txt", ActionType.SEND_KEYS );
		System.out.println("Element Clicked : "+File_Upload);
		
		boolean Date_Calender = page.clickOnElement( xpaths[21], ByType.XPATH_EXPRESSION );
		System.out.println("Date_Calender Clicked : "+Date_Calender);
		boolean Date_Calender_Selection = page.clickOnElement( xpaths[23], ByType.XPATH_EXPRESSION );
		System.out.println("Date_Calender_Selection Clicked : "+Date_Calender_Selection);
		
		boolean Time = page.sendText( xpaths[25], ByType.XPATH_EXPRESSION, "24:25" );
		System.out.println("Time Text Sent : "+ Time);
		
		boolean Date_Time = page.sendText( xpaths[27], ByType.XPATH_EXPRESSION, "2017-08-20 22:25" );
		System.out.println("Date & Time Text Sent : "+ Date_Time);
		
		boolean Qty = page.sendText( xpaths[29], ByType.XPATH_EXPRESSION, "1" );
		System.out.println("Date & Time Text Sent : "+ Qty);
		
		boolean rightClick = page.rightClick( xpaths[31], ByType.XPATH_EXPRESSION );
		System.out.println("Button Cart : "+ rightClick);
		
		/*boolean Toggle_Tooltip = page.verifyToolTipText( xpaths[33], ByType.XPATH_EXPRESSION, "data-original-title", "Add to Wish List");
		System.out.println("Toggle_Tooltip Text Sent : "+Toggle_Tooltip);*/
		
		System.out.println("Enter something in console to quit the browser and driver.");
		try {
			System.in.read();
			System.in.read();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		
		page.quitDriver();
	}
}