package com.github.web.automation;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class Verifications extends Keyboard_Mouse_Actions {
	
	public static enum ByType {
		XPATH_EXPRESSION, CSS_SELECTOR, ID, NAME, CLASS;
	}
	
	public static boolean containsIgnoreCase(String source, String searchChar) {
		return StringUtils.containsIgnoreCase(source, searchChar);
	}
	
	
	public boolean verifyTitle(String expectedTitle, boolean ignoreCase) {
		
		explicitWait.until( new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				String title = driver.getTitle();
				System.out.println("Application Title : "+title);
				if( ignoreCase == true )
					return title.equalsIgnoreCase( expectedTitle );
				return title.equals( expectedTitle );
			}
		});
		return false;
	}

	public boolean verifyText(String locator, ByType locatorType, String expectedText) {
		
		WebElement webElement = getWebElement(locator, locatorType);
		if( webElement != null ) {
			String elementText = webElement.getText();
			
			if( elementText.equalsIgnoreCase( expectedText ) ) {
				drawBorder(locator, locatorType, "green");
			} else {
				drawBorder(locator, locatorType, "red");
			}
			
			if ( takeScreenShot ) {
				takeElementScreenshot(webElement);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * https://jqueryui.com/tooltip/
	 * <a href="/" title="jQuery UI ToolTip">jQuery UI</a>
	 * 
	 * @param locator
	 * @param locatorType
	 * @param attribute
	 * @param expectedText
	 * @return
	 */
	public boolean verifyToolTipText(String locator, ByType locatorType, String attribute, String expectedText) {
		
		WebElement webElement = getWebElement(locator, locatorType);
		if( webElement != null ) {
			Actions actions = new Actions(driver);
			actions.moveToElement( webElement ).build().perform();
			
			// https://stackoverflow.com/q/13133185/5081877
			String tooltipTextToVerify = webElement.getAttribute( attribute ); // title, data-original-title
			System.out.println("Tooltip Text : "+ tooltipTextToVerify );
			// Actions Hover on Element before taking screenshot.
			if( tooltipTextToVerify.equalsIgnoreCase( expectedText ) ) {
				drawBorder(locator, locatorType, "green");
			} else {
				drawBorder(locator, locatorType, "red");
			}
			
			if ( takeScreenShot ) {
				takeElementScreenshot(webElement);
			}
			return true;
		}
		return false;
	}
	
}
