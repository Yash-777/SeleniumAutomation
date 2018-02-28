package com.github.server.file;

import java.io.File;
import java.io.IOException;

import com.github.web.automation.BrowserDrivers;

public class GeckoDriver {
	public static void main(String[] args) {
		String FF_DriverPack = "v0.19.1";
		getDriver(FF_DriverPack);
	}
	
	public static String getDriver( String FF_DriverPack ) {
		// https://github.com/mozilla/geckodriver/releases/download/~~/geckodriver-~~-win64.zip
		StringBuilder ZIPFile = new StringBuilder( System.getProperty("user.dir")+"/Drivers/FireFox/" );
		ZIPFile.append(FF_DriverPack);
		
		BrowserDrivers.createDirectory( ZIPFile.toString() );
		
		ZIPFile.append("/geckodriver.exe");
		
		File ffFile = new File( ZIPFile.toString() );
		
		try {
			if ( !ffFile.exists() ) {
				System.out.println("Downloading FireFox Driver Extension from server...");
				
				String downloadURL = 
						String.format( "https://github.com/mozilla/geckodriver/releases/download/%s/geckodriver-%s-win64.zip",
						FF_DriverPack, FF_DriverPack);
				
				new FileFromURL( downloadURL ).downloadUsing_NIOChannel( ZIPFile.toString() );
			} else {
				System.out.println("Locally FireFox Driver Extension is available.");
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return ffFile.getAbsolutePath();
	}
}
