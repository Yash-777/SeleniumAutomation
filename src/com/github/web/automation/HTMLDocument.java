package com.github.web.automation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * 
 * @author yashwanth.m
 *
 */
public class HTMLDocument {
	
	
	/**
	 * If Window number is 3, then gets all the WindowHandles and based on those Handles find's
	 * the provided window number and switches to that window.
	 * <p> EX: switchToWindowHandle( driver, 3 );
	 * 
	 * @param driver        the driver object
	 * @param window_number the number of the window
	 * @return
	 */
	public String switchToWindowHandle( WebDriver driver, int window_number ) {
		String currentWindow = "";
		List<String> windowlist = null;
		driver.switchTo().defaultContent();
		Set<String> windows = driver.getWindowHandles();
		windowlist = new ArrayList<String>(windows);
		currentWindow = driver.getWindowHandle();
		
		if (!currentWindow.equalsIgnoreCase(windowlist.get(window_number - 1))) {
			driver.switchTo().window(windowlist.get(window_number - 1));
			driver.manage().window().maximize();
		}
		
		return currentWindow;
	}
	
	/**
	 * This function will directly switches to the provided window number.
	 * 
	 * @param driver        the driver object
	 * @param currentWindow the number of the window
	 */
	public void switchToWindow( WebDriver driver, String currentWindow ) {
		driver.switchTo().window(currentWindow);
	}
	
	
	/**
	 * This function will switch to frame based on the index provided.
	 * <p> <a href="https://stackoverflow.com/a/35773587/5081877">Selenium Web Driver Handling Frames </a>
	 * 
	 * @param driver      the driver object
	 * @param frameIndex  the index of frame
	 * @param isIFrame    to find weather the function call is for Frame type or IFrame type.
	 */
	public void switchToFrame( WebDriver driver, int frameIndex, boolean isIFrame  ) {
		driver.switchTo().defaultContent();
		List<WebElement> listFrames = new ArrayList<WebElement>();
		if( isIFrame ) {
			listFrames = driver.findElements(By.tagName("iframe"));
		} else {
			listFrames = driver.findElements(By.tagName("frame"));
		}
		driver.switchTo().frame( listFrames.get( frameIndex ) );
	}
}
