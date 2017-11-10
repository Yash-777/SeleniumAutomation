package com.github.web.automation;

import java.io.File;
import java.net.URL;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteWebDriver;


/**
 * Capture the screenshot by using remote WebDriver and store it in the specified location. 
 * 
 * @author yashwanth.m
 *
 */
public class AndroidRemoteWebDriver extends RemoteWebDriver implements TakesScreenshot {

	/**
	 * @param url	the remoteAddress of the Selenium.
	 * @param dc	the desiredCapabilities of an browser to interact with selenium.
	 */
	public AndroidRemoteWebDriver( URL url, DesiredCapabilities dc ) {
		super(url, dc);
	}

	/* (non-Javadoc)
	 * @see org.openqa.selenium.remote.RemoteWebDriver#getScreenshotAs(org.openqa.selenium.OutputType)
	 */
	@Override
	public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
		if ((Boolean) getCapabilities().getCapability(CapabilityType.TAKES_SCREENSHOT)) {
			return target.convertFromBase64Png(execute(DriverCommand.SCREENSHOT).getValue().toString());
		}
		return null;
	}
	
	public <X> X getScreenshotSafariCode(OutputType<X> target) throws WebDriverException {
		// Get the screenshot as base64.
		String base64 = (String) execute(DriverCommand.SCREENSHOT).getValue();
		// ... and convert it.
		return target.convertFromBase64Png(base64);
	}

	/**
	 * Captures screen as a file.
	 * 
	 * @return the File Name of the screenshot.
	 */
	public File captureScreenAsFile( AndroidRemoteWebDriver driver ) {
		return ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	}
}