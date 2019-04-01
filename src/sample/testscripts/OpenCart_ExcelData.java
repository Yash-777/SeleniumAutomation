package sample.testscripts;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.github.server.file.ExcelDataReading;
import com.github.web.automation.Browser;
import com.github.web.automation.PageActions;
import com.github.web.automation.PageActions.Options;
import com.github.web.automation.Verifications.ByType;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import enums.ActionType;
import enums.LocalBrowser;

public class OpenCart_ExcelData {
	static String applicationURL = null;
	static String browser = null, binaryPath = null, driverPath = null;
	
	static String[] rowHeaders = null;
	static ArrayList<String[]> cells = new ArrayList<String[]>();
	
	static org.apache.poi.ss.usermodel.Workbook workbook = null;
	
	static ExtentTest test;
	static ExtentReports report;
	static String folderName;
	
	public static void main(String[] args) {
		String fileName = "UploadExcel.xlsx", sheetName = "Sheet1";
		ExcelDataReading.fetchExcelData(fileName, sheetName);
		
		browser=ExcelDataReading.browser; 
		binaryPath=ExcelDataReading.binaryPath;
		driverPath=ExcelDataReading.driverPath;
		System.out.format("\n Borwser:[%s], Binary:[%s], Driver:[%s]\n", browser, binaryPath, driverPath);
		
		applicationURL = ExcelDataReading.appURL;
		System.out.println("Applicaion URL : "+ applicationURL);
		
		try {
			LocalBrowser browserType = LocalBrowser.CHROME;
			launchBrowser(applicationURL, browserType);
			
			String dir = System.getProperty("java.io.tmpdir");
					//System.getProperty("user.dir");
					
			createFolderDir( dir );
			
			report = new ExtentReports(folderName+File.separator+"ExtentReportResults.html");
			// report.config().documentTitle("Execution from Excel Data");
			test = report.startTest("ExtentDemo");
			
			for (String[] row : ExcelDataReading.cells) {
				if(/* driver != null &&*/ row != null ) {
					String stepName = row[0], locator = row[1], locateElementBy = row[2], 
							action = row[3], value = null;
					if( row.length == 5 ) {
						value = row[4];
					}
					
					performAction(stepName, locator, locateElementBy, action, value);
					
					try {
						//test.log(LogStatus.PASS, stepName +"~"+ locator +"~"+ action, test.addScreenCapture( "File with absolute lcoation" )
						test.log(LogStatus.PASS, stepName, test.addScreenCapture( capture(driver) ));
					} catch (Exception e) {
						test.log(LogStatus.FAIL, e.getMessage(), test.addScreenCapture( capture(driver) ));
					}
				}
			}
			
		} catch (Exception e) {
			System.err.println( e.getMessage() );;
		}
		
		report.endTest(test);
		report.flush();
		
		System.out.println("Enter something in console to quit the browser and driver.");
		try {
			System.in.read();
			System.in.read();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		
		page.quitDriver();
	}
	
	// ====================
	static RemoteWebDriver driver;
	static PageActions page;
	public static void launchBrowser( String applicationURL, LocalBrowser browserType ) throws Exception {
		Browser browserObj = new Browser( browserType );
		
		System.out.println( browserObj.toString() );
		RemoteWebDriver webDriver = browserObj.getWebDriver();
		driver = webDriver;
		
		driver.manage().window().maximize();
		driver.get( applicationURL );
		
		String title = driver.getTitle();
		System.out.println("Application Title : "+title);
		
		page = new PageActions( driver, true, browserType );
		System.out.println("Page Obj : "+ page.hashCode());
	}
	
	public enum Actions {
		SEND_TEXT, VERIFY_TEXT, VERIFY_TEXT_IGNORECASE, CLICK, RIGHT_CLICK, MOUSEHOVER,
		FILE_UPLOAD,DRAG_AND_DROP, DROPDOWN, DROPDOWN_OPTION, COMBO_BOX, SLIDER;
	}
	
	
	public static void performAction(String stepName, String locator, String locateElementBy, String action, String value ) {
		
		action = action.replaceAll(" ", "_");
		
		ByType locatorType = getLoatorType(locateElementBy);
		
		switch ( Actions.valueOf( action.toUpperCase() ) ) {
		
		case SEND_TEXT:
			boolean Textarea = page.sendText( locator, locatorType, value );
			System.out.println("Text Sent : "+ Textarea);
			break;
		case VERIFY_TEXT:
			boolean verifyText = page.verifyText(locator, locatorType, value);
			System.out.println("Verify Text : "+ verifyText);
			break;
		case CLICK:
			boolean clickOnElement = page.clickOnElement(locator, locatorType);
			System.out.println("Clicked on Element : "+ clickOnElement);
			break;
		case RIGHT_CLICK:
			boolean rightClick = page.rightClick(locator, locatorType);
			System.out.println("Right Clicked on Element : "+ rightClick);
			break;
		case MOUSEHOVER:
			boolean mouseHoverOnElement = page.mouseHoverOnElement(locator, locatorType);
			System.out.println("Mouse Hover : "+ mouseHoverOnElement);
			break;
		case FILE_UPLOAD:
			boolean fileUpload = page.FileUpload(locator, locatorType, value, ActionType.SEND_KEYS);
			System.out.println("File Upload using send keys : "+ fileUpload);
			break;
		case DRAG_AND_DROP:
			System.err.println("Drang and drop needs source locator and destination locator [we need to change excel]");
			//page.dragAndDrop(locator1, locatorType1, locator2, locatorType2)
			break;
		case DROPDOWN:
			boolean select = page.select(locator, locatorType);
			System.out.println("Drop down click action : "+ select);
			break;
		case DROPDOWN_OPTION:
			boolean selectOptions = page.selectOptions(locator, locatorType, Options.INDEX);
			System.out.println("Drop Down option based on its index position : "+ selectOptions );
			break;
		case COMBO_BOX:
			System.err.println("Current not implement to select multiple Drop Down option : Combobox");
			break;
		case SLIDER:
			System.err.println("SLIDER needs  xOffset, yOffset [we need to change excel]");
			//page.slider(locator, locatorType, xOffset, yOffset)
			break;
			
		default:
			break;
		
		}
	}
	
	public static ByType getLoatorType( String locateElementBy ) {
		ByType locatorType = ByType.XPATH_EXPRESSION;
		switch ( ByType.valueOf( locateElementBy.toUpperCase() ) ) {
		case CSS_SELECTOR:
			locatorType = ByType.CSS_SELECTOR;
			break;
			
		case ID:
			locatorType = ByType.ID;
			break;
			
		case NAME:
			locatorType = ByType.NAME;
			break;
		case CLASS:
			locatorType = ByType.CLASS;
			break;	
		default:
			break;
		}
		
		return locatorType;
	}
	
	public static String capture(WebDriver driver) throws IOException {
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		File Dest = new File(folderName +File.separator+ System.currentTimeMillis()+ ".png");
		String errflpath = Dest.getAbsolutePath();
		FileUtils.copyFile(scrFile, Dest);
		return errflpath;
	}
	public static void createFolderDir( String dir ) {
		folderName = dir+File.separator+"MyReports_"+System.currentTimeMillis();
		System.out.println("Folder name : "+ folderName);
		createFolder(folderName);
	}
	public static void createFolder(String folderPath) { // Java < 7
		File file = new File( folderPath );
		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}
	}
}
