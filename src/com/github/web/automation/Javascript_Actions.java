package com.github.web.automation;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.github.web.automation.Verifications.ByType;
import com.google.common.base.Function;

public class Javascript_Actions {
	
	RemoteWebDriver driver = null;
	JavascriptExecutor jse = null;
	Boolean takeScreenShot = false;
	
	WebDriverWait explicitWait;
	FluentWait<WebDriver> fluentWait;
	
	public By findBy(String locator, ByType locatorType) {
		
		checkDocumentLoaded(jse);
		
		if( locatorType == ByType.XPATH_EXPRESSION ) {
			By xpath = By.xpath( locator );
			return xpath;
		} else if( locatorType == ByType.CSS_SELECTOR ) {
			By cssSelector = By.cssSelector( locator );
			return cssSelector;
		} else if( locatorType == ByType.ID ) {
			By id = By.id( locator );
			return id;
		} else if( locatorType == ByType.CLASS ) {
			By className = By.className( locator );
			return className;
		} else if( locatorType == ByType.NAME ) {
			By name = By.name( locator );
			return name;
		}
		return null;
	}
	
	public WebElement getWebElement(String locator, ByType locatorType) {
		
		WebElement element = fluentWait.until(new Function<WebDriver, WebElement>() {
			@Override
			public WebElement apply(WebDriver driver) {
				By findBy = findBy(locator, locatorType);
				return driver.findElement( findBy );
			}
		});
		return  element;
		//return explicitWait.until(ExpectedConditions.visibilityOfElementLocated( findBy ));
	}
	
	/**
	 * This method is to check the loading state of the document.
	 * And it will wait until document get loaded completely.
	 * 
	 * <OL> <P><B>loading states are:</B>
	 * <LI> <B>loading</B> « The document is still loading.
	 * <LI> <B>interactive</B> « The document has finished loading and the document has been 
	 * parsed but sub-resources such as images, style-sheets and frames are still loading.
	 * <LI> <B>complete</B> « The document and all sub-resources have finished loading. 
	 * The state indicates that the load event is about to fire.</br>
	 * </OL>
	 * @param jse the Java Script executor object which has reference to current window.
	 * @return return the state as `complete`.
	 * 
	 * @see <a href="https://developer.mozilla.org/en/docs/Web/API/Document/readyState">Document.readyState</a>
	 */
	public synchronized String checkDocumentLoaded( JavascriptExecutor jse ) {
		
		String loadingStatus = (String) jse.executeScript("return window.document.readyState");
		System.out.println("\t Page Status : " + loadingStatus);
		if ( !(loadingStatus.equalsIgnoreCase("complete")) ) {
			try {
				wait( 1000 * 2 );
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			checkDocumentLoaded(jse);
		}
		return loadingStatus;
	}
	
	/** 
	 * To highlight an element, it takes XPath and draw border around it.
	 * 
	 * <p> FirePath Uses Class [.firepath-matching-node] { outline: 2px dashed #00F; },
	 * we can use `solid` line with out any break. </p>
	 * 
	 * <P> xpath-generator injected stylesheet =
	 * .xpath-verify-selected { background-color: rgb(225, 192, 220); outline: rgb(204, 0, 0) solid 1px; }
	 * @param locator
	 * @param color the color around 
	 */
	public void drawBorder(String locator, ByType locatorType, String color){
		WebElement element_node = getWebElement(locator, locatorType);
		String elementStyle = "arguments[0].style.border='3px solid "+color+"'";
		if (driver.getClass().getName().contains("ie")) {
			jse.executeScript(elementStyle, element_node);
		} else {
			try {
				StringBuffer border = new StringBuffer();
				border.append("if (document.evaluate) {");
				border.append("var elem = document.evaluate('"+locator+"', window.document, null, 9, null ).singleNodeValue;");
				border.append("elem.style.setProperty ('border', '2px dashed "+color+"', 'important')"); // solid | dashed
				border.append("}");
				
				jse.executeScript( border.toString() );
			} catch (Exception draw) {
				jse.executeScript(elementStyle, element_node);
			}
		}
	}
	
	/**
	 * It will create a Script element with given file as source and returns the Script element as String.
	 * @param fileName	the name of the file.
	 * @return the script element string.
	 */
	public static String injectScriptTag_UsingJavaScript(String scriptWebAddress, String fileName) {
		String intectJS =   "var getHeadTag = document.getElementsByTagName('head')[0];" +
							"var newScriptTag = document.createElement('script');" +
							"    newScriptTag.type='text/javascript';" +
							"    newScriptTag.src='"+ scriptWebAddress + "/" + fileName +"';" +
							"getHeadTag.appendChild(newScriptTag);";
		return intectJS;
	}
	
	public boolean clickUsingJavaScript( String locator ) {
		boolean returnVal = false;
		StringBuffer click = new StringBuffer();
		click.append("if (document.evaluate) {");
		click.append("var elem = document.evaluate('"+locator+"', window.document, null, 9, null ).singleNodeValue;");
		click.append("elem.click();");
		click.append("}");
		
		jse.executeScript( click.toString() );
		returnVal = true;
		
		return returnVal;
	}
	
	public void click_sendTextUsingJavaScript( String locator, boolean sendKeys, String sentText ) {
		StringBuffer send = new StringBuffer();
		send.append("if (document.evaluate) {");
		send.append("var elem = document.evaluate('"+locator+"', window.document, null, 9, null ).singleNodeValue;");
		send.append("elem.click();");
		send.append("ele.value="+sentText+";" );
		send.append("}");
		
		jse.executeScript( send.toString() );
	}

	/**
	 *
	 * JQuery
		var locator = "//*[@name='sectionCode']"
		var path = locator+'/@readonly';
		var ele = $x( locator );
		var attr = $x( path ).length;
		console.log( attr );
		if( attr == 1 ) {
		  console.log( "Exception: invalid element state: Element must be user-editable in order to clear it." );
		}
	
	 * Javascript
		var ele = document.evaluate(locator, window.document, null, 9, null ).singleNodeValue;
		console.log( ele );
		
		var count = document.evaluate('count('+path+')', window.document, null, 0, null);
		console.log(count.numberValue);
	 * @param locator
	 * @return
	 */
	public boolean isElementEditable( String locator ) {
		StringBuffer send = new StringBuffer();
		send.append("var path = "+locator+"/@readonly';");
		send.append("var elem = document.evaluate('count('path')', window.document, null, 0, null);");
		send.append("return elem.numberValue");
		
		Integer count = (Integer) jse.executeScript( send.toString() );
		if( count == 1 ) {
			// Element is Editable.
			return true;
		}
		// To avoid - Exception: invalid element state: Element must be user-editable in order to clear it.
		return false;
	}
}
