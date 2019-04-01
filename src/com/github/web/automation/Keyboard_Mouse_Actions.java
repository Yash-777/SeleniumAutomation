package com.github.web.automation;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.github.web.automation.Verifications.ByType;

import enums.ActionType;

/**
 * <UL>All actions implement the 
 * <a href="https://github.com/SeleniumHQ/selenium/wiki/Advanced-User-Interactions"> Action interface.</a>
 * <LI>ClickAction - Equivalent to WebElement.click()</LI>
 * <LI>KeyDownAction - Holding down a modifier key.</LI>
 * <LI>KeyUpAction - Releasing a modifier key.</LI>
 * <LI>SendKeysAction - Equivalent to WebElement.sendKey(...)</LI>
 * <LI> ... </LI>
 * </UL>
 * 
 * <P>Element <a href="https://stackoverflow.com/a/14222645/5081877">Drag and Drop</a>
 * 
 * @author yashwanth.m
 *
 */
public class Keyboard_Mouse_Actions extends ScreenShot {
	
	/**
	 * https://stackoverflow.com/a/46447150/5081877
	 * https://sqa.stackexchange.com/questions/12851/how-can-i-work-with-file-uploads-during-a-webdriver-test
	 * 
	 * @param locator
	 * @param locatorType
	 * @param filePath
	 * @return
	 */
	public boolean FileUpload(String locator, ByType locatorType, String filePath, ActionType type) {
		By findBy = findBy(locator, locatorType);
		WebElement element = explicitWait.until(ExpectedConditions.elementToBeClickable( findBy ));
		System.out.println("\t ===== FILE UPLOAD =====");
		if( type == ActionType.SEND_KEYS ) {
			try {
				element.sendKeys( filePath );
				if ( takeScreenShot ) {
					takeElementScreenshot(element);
				}
				return true;
			} catch (WebDriverException e) {
				// Exception in thread "main" org.openqa.selenium.WebDriverException: unknown error: cannot focus element
				System.err.println("File Upload SendKeys Exception : "+e.getMessage());
				if ( !e.getMessage().contains("cannot focus element") ) {
					return false;
				} else {
					type = ActionType.WIN;
				}
			}
		} else if ( type == ActionType.FILE_DETECTOR ) {
			LocalFileDetector detector = new LocalFileDetector();
			File localFile = detector.getLocalFile( filePath );
			RemoteWebElement input = (RemoteWebElement) driver.findElement(By.xpath(locator));
			input.setFileDetector(detector);
			input.sendKeys(localFile.getAbsolutePath());
			input.click();
			/*
			 * String zip = new Zip().zipFile(localFile.getParentFile(), localFile);
			 * Response response = execute(DriverCommand.UPLOAD_FILE, ImmutableMap.of("file", zip));
			 * return (String) response.getValue();
			 */
			return true;
		}
		try {
			element.click();
			Thread.sleep( 1000 * 2 );
			
			setClipboardData(filePath);
			
			Robot robot = new Robot();
			if( type == ActionType.MAC ) { // Apple's Unix-based operating system.
				
				// “Go To Folder” on Mac - Hit Command+Shift+G on a Finder window.
				// http://osxdaily.com/2011/08/31/go-to-folder-useful-mac-os-x-keyboard-shortcut/
				robot.keyPress(KeyEvent.VK_META);
				robot.keyPress(KeyEvent.VK_SHIFT);
				robot.keyPress(KeyEvent.VK_G);
				robot.keyRelease(KeyEvent.VK_G);
				robot.keyRelease(KeyEvent.VK_SHIFT);
				robot.keyRelease(KeyEvent.VK_META);

				// Paste the clipBoard content - Command ⌘ + V.
				robot.keyPress(KeyEvent.VK_META);
				robot.keyPress(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_META);
				
				// Press Enter (GO - To bring up the file.)
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
				return true;
			} else if ( type == ActionType.WIN || type == ActionType.LINUX ) { // Ctrl + V to paste the content.
				// paste to the current using view [In debug mode pastes it in eclipse.]
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_CONTROL);
			}
			
			robot.delay( 1000 * 3 );
		
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			
			robot.delay( 1000 * 7 );
			
			if ( takeScreenShot ) {
				// takeElementScreenshot(element);
				try {
					File tempScreen = captureScreenAsFile();
					
					File screen = new File( getFileName() );
					FileUtils.copyFile( tempScreen , screen);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			return true;
		} catch (AWTException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Sets the specified file-path to the ClipBoard.
	 * <UL>Copy data to ClipBoard as.
	 * <li> WIN [ Ctrl + C ] </li>
	 * <li> MAC [ Command ⌘ + C ]</li> - https://superuser.com/questions/371513/how-to-tell-full-path-of-file-on-mac
	 * 
	 * @param filePath - the transferable object representing the clipboard content.
	 */
	public void setClipboardData(String filePath) {
		StringSelection stringSelection = new StringSelection( filePath );
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	}
	
	public boolean dragAndDrop(String locator1, ByType locatorType1, String locator2, ByType locatorType2) {
		try {
			
		By findBy1 = findBy(locator1, locatorType1);
		WebElement dragElement = explicitWait.until(ExpectedConditions.elementToBeClickable( findBy1 ));
		
		By findBy2 = findBy(locator2, locatorType2);
		WebElement dropElement = explicitWait.until(ExpectedConditions.elementToBeClickable( findBy2 ));
		
		Actions builder = new Actions(driver);
		builder.clickAndHold( dragElement )
			.moveToElement( dropElement )
			.release( dropElement )
			.build();
			
			return true;
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		
		return false;
	}
	/**
	 * Find the element offset position with respect to its parent element.
	 * <pre>// https://stackoverflow.com/a/10954954/5081877
	 * var elem = $("#sliderElementID");
	 * var offset = elem.offset().left - elem.parent().offset().left;
	 * console.dir( offset );
	 * 
	 * $('div > div.irs-bar').css({'left': '3.75%', 'width': '80%' });
	 * <pre>
	 * @param locator of an element.
	 * @param locatorType to verify|identify an element.
	 * @param xOffset horizontal move offset.
	 * @param yOffset vertical move offset.
	 * @return
	 */
	public boolean slider( String locator, ByType locatorType, int xOffset, int yOffset ) {
		try {
			By findBy = findBy(locator, locatorType);
			WebElement sliderElement = explicitWait.until(ExpectedConditions.elementToBeClickable( findBy ));
			Actions moveSlider = new Actions(driver);
			Action action6 = moveSlider.dragAndDropBy(sliderElement, xOffset, yOffset).build();

			action6.perform();
			return true;
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean rightClick( String locator, ByType locatorType ) {
		try {
			System.out.println("Right Click Element path : "+ locator);
			By findBy = findBy(locator, locatorType);
			WebElement element = explicitWait.until(ExpectedConditions.elementToBeClickable( findBy ));
			Actions rightClick = new Actions(driver);
			rightClick.moveToElement( element );
			rightClick.contextClick( element ).build().perform();
			return true;
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return false;
	}
}
