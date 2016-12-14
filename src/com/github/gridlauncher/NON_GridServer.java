package com.github.gridlauncher;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;

import org.openqa.selenium.server.RemoteControlConfiguration;
import org.openqa.selenium.server.SeleniumServer;

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

			SeleniumServer server = new SeleniumServer(false, configuration);
			server.start();
			
			System.out.println("Launching a standalone Selenium Server on this Port : "+ server.getPort());
			String separator = "\n-------------------------------\n";
			String consoelULR = "http://127.0.0.1:4444/wd/hub/static/resource/hub.html";
			System.out.format("%s Running as a standalone server %s HTML URL : %s", 
					separator, separator,consoelULR);
			
			System.out.println("Please Enter to stop service.");
			System.in.read();
			System.in.read();
		
			server.stop();
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
