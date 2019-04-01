package com.github.server.file;

import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Which reads the data from Excel and stores into java objects.
 * @author yashwanth.m
 *
 */
public class ExcelDataReading {
	
	public static String appURL = null;
	public static String[] rowHeaders = null;
	public static ArrayList<String[]> cells = new ArrayList<String[]>();
	
	public static String browser = null;
	public static String binaryPath = null;
	public static String driverPath = null;
			
	static org.apache.poi.ss.usermodel.Workbook workbook = null;
	public static void main(String[] args) {
		String fileName = "UploadExcel.xlsx", sheetName = "Sheet1";
		fetchExcelData(fileName, sheetName);
	}
	public static void fetchExcelData( String fileName, String sheetName ){
		try {
			File file = new File( fileName );
			FileInputStream inputStream = new FileInputStream(file);
			
			String fileExtensionName = fileName.substring(fileName.indexOf(".")); // fileName.split(".")[1];
			System.out.println("File Extension : "+ fileExtensionName);
			
			if( fileExtensionName.equals(".xlsx") ) {
				workbook = WorkbookFactory.create( inputStream );
				Sheet sheet = workbook.getSheet(sheetName);
				
				int totalRows = sheet.getPhysicalNumberOfRows();
				System.out.println("Total Number of Rows : "+ totalRows );
				
				int totalNoOfRows = sheet.getLastRowNum();
				
				appURL = columnIndexValue(sheet, 0, 1);
				System.out.println("Applicaiton URL : "+ appURL);
				
				browser = columnIndexValue(sheet, 1, 1);
				System.out.println("Browser : "+ appURL);
				
				binaryPath = columnIndexValue(sheet, 2, 1);
				System.out.println("BinaryPath : "+ binaryPath);
				
				driverPath = columnIndexValue(sheet, 3, 1);
				System.out.println("DriverPath : "+ driverPath);
				
				int startHeadersIndex = 4;
				for (int i = startHeadersIndex; i <= totalNoOfRows; i++) {
					Row row = sheet.getRow(i);
					
					String[] rowsOfCells = null;
					for (int j = 0; j < row.getLastCellNum(); j++) {
						Cell cell = row.getCell(j);
						String stringCellValue = getCellValue(cell);
						
						if( i == startHeadersIndex) {
							if(rowHeaders == null)
								rowHeaders = new String[ row.getLastCellNum() ];
							
							rowHeaders[j] = stringCellValue;
						} else {
							if( rowsOfCells == null ) {
								rowsOfCells = new String[ row.getLastCellNum() ];
							}
							
							rowsOfCells[j] = stringCellValue;
						}
					}
					cells.add( rowsOfCells );
				}
				System.out.println("Excel Sheet Data.");
				System.out.println( stringArray_As_String( rowHeaders ) );
				for (String[] cell : cells) {
					System.out.println( stringArray_As_String( cell ) );
				}
			}
			
		} catch ( Exception e) {
			e.printStackTrace();
		}
	}
	
	static String columnIndexValue(Sheet sheet, int rowIndex, int columnIndex ) {
		Row applicationInfo = sheet.getRow(rowIndex);
		Cell applicationURL = applicationInfo.getCell(columnIndex);
		return getCellValue(applicationURL);
	}
	
	static String getCellValue( Cell cell ) {
		
		if( cell == null ) return "";
		
		String stringCellValue = null;
		//  Checking Data Formats.
		if( cell.getCellTypeEnum() == CellType.STRING ) {
			stringCellValue = cell.getStringCellValue();
		} else if( cell.getCellTypeEnum() == CellType.NUMERIC || cell.getCellTypeEnum() == CellType.FORMULA ) {
			String cellValue = String.valueOf(cell.getNumericCellValue());
			if( DateUtil.isCellDateFormatted(cell) ) {
				DateFormat df = new SimpleDateFormat("dd/MM/yy");
				Date date = cell.getDateCellValue();
				cellValue = df.format(date);
			}
			stringCellValue = cellValue;
		} else if( cell.getCellTypeEnum() == CellType.BLANK ) {
			stringCellValue = "";
		} else {
			stringCellValue = String.valueOf( cell.getBooleanCellValue() );
		}
		return stringCellValue;
	}
	static String getCellValue_WithType( Cell cell ) {
		
		if( cell == null ) return " []";
		
		String stringCellValue = null;
		//  Checking Data Formats.
		if( cell.getCellTypeEnum() == CellType.STRING ) {
			stringCellValue = cell.getStringCellValue() +"[S]";
		} else if( cell.getCellTypeEnum() == CellType.NUMERIC || cell.getCellTypeEnum() == CellType.FORMULA ) {
			String cellValue = String.valueOf(cell.getNumericCellValue());
			if( DateUtil.isCellDateFormatted(cell) ) {
				DateFormat df = new SimpleDateFormat("dd/MM/yy");
				Date date = cell.getDateCellValue();
				cellValue = df.format(date) +"[D]";
			} else {
				cellValue += "[N]";
			}
			stringCellValue = cellValue;
		} else if( cell.getCellTypeEnum() == CellType.BLANK ) {
			stringCellValue = " [ ]";
		} else {
			stringCellValue = String.valueOf( cell.getBooleanCellValue() )+"[B]";
		}
		return stringCellValue;
	}

	public static String stringArray_As_String(String[] str) {
		StringBuffer buffer = new StringBuffer();
		
		int size = 0;
		if( str != null ) size = str.length;
		
		for (int i = 0; i < size; i++) {
			if( i == 0 ) buffer.append("[");
			
			buffer.append( str[i] );
			if( i+1 < size ) buffer.append(", ");
			
			if( i+1 == size ) buffer.append("]");
		}
		return buffer.toString();
	}
}
