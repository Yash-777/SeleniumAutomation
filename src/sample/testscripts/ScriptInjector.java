package sample.testscripts;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.github.yash777.driver.Browser;
import io.github.yash777.driver.Drivers;
import io.github.yash777.driver.WebDriverException;

public class ScriptInjector {
	public static void main(String[] args) throws WebDriverException, IOException, InterruptedException {
		Drivers drivers = new Drivers();
		String driverPath = drivers.getDriverPath(Browser.CHROME, 63, "");
		System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, driverPath);
		
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		WebDriver driver = new ChromeDriver( capabilities );
		//WebDriverWait explicitWait = new WebDriverWait(driver, 10);
		//JavascriptExecutor jse = (JavascriptExecutor) driver;
		driver.get("https://demo.opencart.com/");
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		
		boolean isServerFile = false, injectAsString = true;
		String currentPorj = System.getProperty("user.dir");
		String fileName = currentPorj+"/injectStringFun.js";
		String injectScript = injectScript(isServerFile, injectAsString, fileName);
		jse.executeScript( injectScript );
		
		Thread.sleep(1000);
		
		jse.executeScript( "document.docFun();" );
		
		
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
	static String scriptFileWebAddress = "https://code.jquery.com/jquery-3.2.1.js";
	static String charsetName = java.nio.charset.StandardCharsets.UTF_8.toString();
	/**
	 * It will create a Script element with given file as source and returns the Script element as String.
	 * @param fileName	the name of the file.
	 * @return the script element string.
	 * @throws IOException 
	 */
	@SuppressWarnings("resource")
	public static String injectScript(boolean isServerFile, boolean injectAsString, String fileName) throws IOException {
		String intectJS = "";
		if( injectAsString ) {
			if( isServerFile ) {
				URL url = new URL( scriptFileWebAddress );
				intectJS = new Scanner(url.openStream(), charsetName).useDelimiter("\\A").next();
			} else {
				Scanner scanner = new Scanner(new File(fileName), charsetName).useDelimiter("\\A");
				intectJS = scanner.hasNext() ? scanner.next() : "";
			}
		} else if ( isServerFile ) {
			intectJS =  "var getHeadTag = document.getElementsByTagName('head')[0];" +
					"var newScriptTag = document.createElement('script');" +
					"    newScriptTag.type='text/javascript';" +
					"    newScriptTag.src='"+scriptFileWebAddress+"';" +
					"getHeadTag.appendChild(newScriptTag);";
		}
		return intectJS;
	}
}
