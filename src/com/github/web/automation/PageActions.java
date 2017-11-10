package com.github.web.automation;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import enums.LocalBrowser;

/**
 * https://docs.rs/webdriver/0.27.0/webdriver/command/enum.WebDriverCommand.html
 * 
 * @author yashwanth.m
 *
 */
public class PageActions extends Verifications {
	
	
	/** <P>IMPLICIT wait - Global wait for all Elements in an Application.
	 * Driver wait for all the commands over the 
	 * Implicit wait is set for the entire duration of the webDriver object.
	 * if element appear before specified time than script will start executing otherwise script 
	 * will throw NoSuchElementException.</P>
	 * 
	 * <P>EXPLICIT Wait + ExpectedConditions - use for a particular element to identify over DOM.
	 * for example - element which is created over DOM when the AJAX responses bask
	 * for a single page application before giveUP wait for specified time.</P>
	 * 
	 * <P>FLUENT wait - poll apply() method for every time till the specified time.
	 * "poll the DOM" he just means "periodically read the DOM again to check for a reload".</P>
	 * 
	 */
	public PageActions( RemoteWebDriver driver, boolean takeElementScreenShot, LocalBrowser browserName ) {
		this.driver = driver;
		jse = (JavascriptExecutor) driver;
		takeScreenShot = takeElementScreenShot;
		this.browserName = browserName;
		
		// driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(3, TimeUnit.MINUTES);
		driver.manage().timeouts().setScriptTimeout(1, TimeUnit.MINUTES);
		
		Integer ELEMENT_WAIT_TIME_SEC = 10;
		explicitWait = new WebDriverWait(driver, ELEMENT_WAIT_TIME_SEC);
		
		fluentWait = new FluentWait<WebDriver>(driver)
				.withTimeout(30, TimeUnit.SECONDS)
				.pollingEvery(5, TimeUnit.SECONDS)
				.ignoring( NoSuchElementException.class ); // We need to ignore this exception.
				//.ignoring( StaleElementReferenceException.class );
	}
	
	/**
	 * To fire any event is available to an element use click.
	 *  - CheckBox, RadioButton, Button.
	 * @param locator
	 * @param locatorType
	 * @return
	 */
	public boolean clickOnElement(String locator, ByType locatorType) {
		boolean isElementClicked = false;
		
		isElementClicked = webElementClick(locator, locatorType);
		if (!isElementClicked) { // Actions
			isElementClicked = actionsClick(locator, locatorType);
		}
		if (!isElementClicked) { // JavaScript
			System.out.println("Javascript XPath Click.");
			isElementClicked = clickUsingJavaScript(locator);
		}
		return isElementClicked;
	}
	/**
	 * https://stackoverflow.com/a/46424894/5081877
	 * 
	 * An expectation for checking an element is visible and enabled such that you can click it.
	 * 
	 * org.openqa.selenium.ElementNotVisibleException: element not visible
	 * @param locator
	 * @param locatorType
	 * @return
	 */
	private boolean webElementClick( String locator, ByType locatorType ) {
		By findBy = findBy(locator, locatorType);
		try {
			WebElement element = explicitWait.until(ExpectedConditions.elementToBeClickable( findBy ));
			if( element != null ) {
				if ( takeScreenShot ) {
					drawBorder(locator, locatorType, "Blue");
					takeElementScreenshot(element);
				}
				element.click();
				return true;
			}
		} catch (ElementNotVisibleException e) {
			WebElement webElement = getWebElement(locator, locatorType);
			jse.executeScript("arguments[0].click()", webElement);
			return true;
		}
		return false;
	}
	/**
	 * An expectation for checking that an element is present on the DOM of a page and visible.
	 * Visibility means that the element is not only displayed but also has a height and width that is greater than 0.
	 * 
	 * @param locator
	 * @param locatorType
	 * @return
	 */
	private boolean actionsClick( String locator, ByType locatorType ) {
		boolean isElementClicked = false;
		By findBy = findBy(locator, locatorType);
		System.out.println("Actions Click.");
		WebElement visible = explicitWait.until(ExpectedConditions.visibilityOfElementLocated( findBy ));
		Actions action = new Actions(driver);
		
		if ( takeScreenShot ) {
			drawBorder(locator, locatorType, "Blue");
			takeElementScreenshot( visible );
		}

		action.moveToElement( visible ).click();
		isElementClicked = true;
		
		return isElementClicked;
	}
	
	public boolean mouseHoverOnElement(String locator, ByType locatorType) {
		boolean isElementClicked = false;
		By findBy = findBy(locator, locatorType);
		WebElement element = explicitWait.until(ExpectedConditions.elementToBeClickable( findBy ));
		if( element != null ) {
			System.out.println("Actions Build.");
			Actions action = new Actions(driver);
			action.moveToElement(element).build().perform();
			if ( takeScreenShot ) {
				drawBorder(locator, locatorType, "Blue");
				takeElementScreenshot(element);
			}
			// element.click();
			isElementClicked = true;
		}
		isElementClicked = clickOnElement(locator, locatorType );
		
		return isElementClicked;
	}
	
	// InvalidElementStateException: Element must be user-editable in order to clear it.
	public boolean sendText(String locator, ByType locatorType, String textToSend) {
		
		WebElement webElement = getWebElement(locator, locatorType);
		if( webElement != null ) {
			webElement.clear();
			webElement.sendKeys(textToSend);
			
			if ( takeScreenShot ) {
				takeElementScreenshot(webElement);
			}
			return true;
		}
		return false;
	}
	
	public String getText(String locator, ByType locatorType) {
		
		WebElement webElement = getWebElement(locator, locatorType);
		if( webElement != null ) {
			String elementText = webElement.getText();
			
			if ( takeScreenShot ) {
				takeElementScreenshot(webElement);
			}
			return elementText;
		}
		return null;
	}
	
	/**
	 * ComboBox in HTML 5
	 * HTML 4 equivalent Select followed by options.
	 * 
	 * Element send keys to search combo box item, after sort it get filtered and displayed down 
	 * using element XPATH click on it.
	 * @return
	 */
	public boolean ComboBox() {
		
		return false;
	}
	
	
	String SELECT_ELE = null;
	public enum Options {
		VISIBLE_TEXT, VALUE, INDEX;
	}
	public boolean select(String locator, ByType locatorType) {
		boolean returnVal = false;
		SELECT_ELE = locator;
		int n = locator.lastIndexOf("/");
		String select = locator.substring(n+1, locator.length());
		System.out.println("XPath Contains Select : "+select);
		if( containsIgnoreCase(select, "SELECT") ) { // Locator contains select word then 
			
			Actions selectMenu = new Actions( driver );
			WebElement webElement = getWebElement(SELECT_ELE, locatorType);
			
			Actions moveToElement = selectMenu.moveToElement( webElement );
			moveToElement.click();
			// moveToElement.release().build(); // Releases the depressed left mouse button at the current mouse location.
			// moveToElement.build().perform();
			// webElement.click();
			
			if ( takeScreenShot ) {
				takeElementScreenshot(webElement);
			}
			returnVal = true;
		} else {
			returnVal = clickOnElement( SELECT_ELE, locatorType );
		}
		return returnVal;
	}
	/**
	 * <ul>Sample Xpaths:
	 * <li> //select[@id='groupSelect']/option[@value='data' and . = 'First value'] </li>
	 * <li> //select[1]/option[1]
	 * </ul>
	 * 
	 * @param locator		the DOM element unique id over DOM Tree.
	 * @param locatorType	the action to perform based on xpath, css, id, ...
	 * @param selectionType	select the option based on id, value or postion.
	 * @return
	 */
	public boolean selectOptions(String locator, ByType locatorType, Options selectionType) {
		boolean returnVal = false;
		int n = locator.lastIndexOf("/");
		String SELECT_ELE = locator.substring(0, n);
		System.out.println( SELECT_ELE );
		
		String options = locator.substring(n+1, locator.length());
		String index = options.replaceAll("[^\\d]", "");
		System.out.println(options +" : "+ index);
		
		WebElement selectElement = getWebElement(SELECT_ELE, locatorType);
		if( /*locator.contains("/option") || */pathContainsSubPath(locator) ) {
			
			WebElement optionElem = getWebElement(locator, locatorType);
			if( !optionElem.isDisplayed() ) {
				clickOnElement( SELECT_ELE, locatorType );
			}
			
			Select select = new Select( selectElement );
			if ( selectionType == Options.VISIBLE_TEXT ) {
				String optionText = optionElem.getText();
				System.out.println("Option Text Value : "+ optionText);
				// List<WebElement> allSelectedOptions = select.getAllSelectedOptions();
				select.selectByVisibleText( optionText );
				
				String selectedText = select.getFirstSelectedOption().getText();
				if ( selectedText.equalsIgnoreCase( optionText )) {
					returnVal = true;
				}
			} else if ( selectionType == Options.VALUE ) {
				String attribute = optionElem.getAttribute("value");
				System.out.println("Option Value = "+attribute);
				select.selectByValue( attribute );
			} else {
				System.out.println("Selecting Options by Index position : "+index);
				select.selectByIndex( Integer.valueOf( index) );
			}
		} else {
			WebElement optionElem = getWebElement(locator, locatorType);
			if( !optionElem.isDisplayed() ) {
				clickOnElement( SELECT_ELE, locatorType );
			}
			clickOnElement( locator, locatorType );
		}
		if ( takeScreenShot ) {
			takeElementScreenshot( selectElement );
		}
		return returnVal;
	}
	static boolean pathContainsSubPath(String xpath) {
		Pattern p = Pattern.compile("./select.*/option.*");
		Matcher m = p.matcher( xpath );
		boolean matches = m.matches();
		System.out.println("Match case >> "+matches);
		return matches;
	}
	
	public void quitDriver() {
		driver.close();
		driver.quit();
	}
}