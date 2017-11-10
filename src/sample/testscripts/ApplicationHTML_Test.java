package sample.testscripts;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.github.web.automation.Browser;
import com.github.web.automation.Javascript_Actions;

import enums.LocalBrowser;

public class ApplicationHTML_Test {
	private static RemoteWebDriver driver;
	public static void main(String[] args) throws Exception {
		Browser b = new Browser( LocalBrowser.CHROME );
		RemoteWebDriver webDriver = b.getWebDriver();
		driver = webDriver;
		
		File htmlFile = new File("./fileUploadBytes.html");
		String absoluteFile = htmlFile.getAbsoluteFile().toString();
		System.out.println("File Path :"+absoluteFile);
		String baseUrl = "file:///"+absoluteFile;
				// "file:///C:/Users/yashwanth.m/Desktop/fileUploadBytes.html";
		driver.get( baseUrl );
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		String xpathExpression = "//div[@style='overflow: auto; height: auto;']";
		WebElement findElement = driver.findElement(By.xpath(xpathExpression));
		System.out.println("Text : "+findElement.getText());
		// Text : No results found, click tab to use entered text
		JavascriptExecutor jse = driver;
		String jsPath = "//div[@style='overflow: auto; height: auto;']/.";
		String innerValue = (String) jse.executeScript("document.evaluate('arguments[0]', window.document, null, 9, null ).singleNodeValue;", jsPath);
		System.out.println("Text : "+innerValue);
		
		driver.close();
		driver.quit();
	}
}
