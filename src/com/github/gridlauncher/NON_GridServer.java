package com.github.gridlauncher;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;

import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.server.RemoteControlConfiguration;
import org.openqa.selenium.server.SeleniumServer;

/**
 * The Standalone Selenium Server acts as a Proxy between the test script and the 
 * browser-specific drivers.
 * 
 * <P>The browsers like Chrome, Internet Explorer, FireFox and Safari which run on a specific port.
 * If user not provide any specific port number than runs on default port.
 * 
 * <P> FireFox [.xpi] and Safari: Driver extension are PreInstalled to the actual browser executable.
 * 
 * <UL> <B>Use Standalone Selenium-Server</B> 
 * <LI> If your browser and tests will all run on the same machine.
 * 
 * @author yashwanth.m
 *
 */
public class NON_GridServer {
	public static void standaloneServer() {
		try {
			RemoteControlConfiguration configuration = new RemoteControlConfiguration();
			configuration.setPort( 4454 );
			
			
			ServerSocket serverSocket = new ServerSocket( configuration.getPort() );
			if ( !serverSocket.isClosed() ) {
				System.out.println("PORT is in use..., So closing the stream on this port.");
				serverSocket.close();
			}

			SeleniumServer seleniumProxy = new SeleniumServer(false, configuration);
			seleniumProxy.start();
			
			System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, 
					GRIDINFO.CHROME_DRIVER_EXE.toString());
			System.setProperty(InternetExplorerDriverService.IE_DRIVER_EXE_PROPERTY, 
					GRIDINFO.IE_DRIVER_EXE.toString());
			
			System.out.println("Launching a standalone Selenium Server on this Port : "+ seleniumProxy.getPort());
			String separator = "\n-------------------------------\n";
			String consoelULR = "http://127.0.0.1:4444/wd/hub/static/resource/hub.html";
			System.out.format("%s Running as a standalone server %s HTML URL : %s", 
					separator, separator,consoelULR);
			
			//String proxyHost = System.getProperty("http.proxyHost");
			String proxyPort = System.getProperty("http.proxyPort");
			System.out.format("\n PORT < http.proxyPort[%d] & Selenium Server[%d]\n", proxyPort,seleniumProxy.getPort());
			/**
			 * <P> -avoidProxy « By default, we proxy every browser request; set this flag to make the browser 
			 * use our proxy only for URLs containing '/selenium-server'
			 * 
			 */
			
			System.out.println("Please Enter to stop service.");
			System.in.read();
			System.in.read();
		
			seleniumProxy.stop();
		} catch (BindException e) {
			System.out.println("Selenium server already up, will reuse...");
		} catch(IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Could not create Selenium Server because of: " + e.getMessage());
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		standaloneServer();
	}
}
