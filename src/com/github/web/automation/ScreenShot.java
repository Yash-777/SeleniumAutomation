package com.github.web.automation;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import enums.LocalBrowser;

public class ScreenShot extends Javascript_Actions {
	
	public LocalBrowser browserName;
	
	public ScreenShot() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Captures screen as a file.
	 * 
	 * @return the File Name of the screenshot.
	 */
	public File captureScreenAsFile() {
		return ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	}
	public String getFileName() {
		Long startTime = System.currentTimeMillis();
		String newFileName = System.getProperty("user.dir")+"/images/"+browserName+"_"+getSessionID()+"_"+startTime.toString()+".png";
		return newFileName;
	}
	public File takeElementScreenshot(WebElement element) {
		File screen = null, tempScreen = null;
		
		try {
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
				if( viewPoint.y != elementLocation.y ) {
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
			
			} catch (WebDriverException e) {
				// Exception in thread "main" org.openqa.selenium.WebDriverException: unknown error: cannot focus element
				System.err.println("Screenshot Exception : "+e.getMessage());
				if ( e.getMessage().contains("cannot focus element") ) {
					try {
						tempScreen = captureScreenAsFile();
					} catch ( WebDriverException e2 ) { // stale element reference: element is not attached to the page document
						System.err.println("Screenshot Exception : "+e2.getMessage());
						if ( e2.getMessage().contains("element is not attached to the page document") ) {
							tempScreen = captureScreenAsFile();
						}
					}
				}
			}
			screen = new File( getFileName() );
			FileUtils.copyFile(tempScreen, screen);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return screen;
	}
	public String getSessionID() {
		return ((RemoteWebDriver) driver).getSessionId().toString();
	}
}
