package com.github.gridlauncher;

import org.openqa.grid.internal.utils.GridHubConfiguration;
import org.openqa.grid.web.Hub;

/**
 * 
 * <UL> <B>Use GRID HUB Server:</B>
 * <LI> To distribute your tests over multiple machines or virtual machines (VMs).
 * <LI> You want to connect to a remote machine that has a particular 
 * browser version that is not on your current machine.
 * 
 * http://www.eclipse.org/jetty/
 * 
 * http://docs.seleniumhq.org/docs/07_selenium_grid.jsp
 * 
 * Selenium-Grid 2.0 - Configuring Selenium-Grid
 * 		« org.openqa.grid.internal.utils.GridHubConfiguration;
 * 		« org.openqa.selenium.server.SeleniumServer;
 * 
 * Selenium 3 - org.seleniumhq.jetty9.server.Server - Started @1026ms
 * 		« org.openqa.grid.internal.utils.configuration.GridHubConfiguration;
 * 		« hub configuration json file
 * 
 * @author yashwanth.m
 *
 */
public class Grid_HUBServer {
	
	public static String HUBIP = GRIDINFO.HOSTIP.toString();
	public static Integer HUBPORT = 4444;
	static String separator = "\n------------------------\n";
	
	public static void hubServer() {
		try {
			GridHubConfiguration gridHubConfig = new GridHubConfiguration();
			gridHubConfig.setHost( HUBIP );
			gridHubConfig.setPort( HUBPORT );
			gridHubConfig.setNewSessionWaitTimeout( 50000 );
			gridHubConfig.loadFromJSON( GRIDINFO.HUBJSON.toString() );
			
			Hub hub = new Hub(gridHubConfig);
			hub.start();
			
			System.out.println("Nodes should register to " + hub.getRegistrationURL());
			System.out.format("%s Running as a grid hub: %s\n" +
					"Console URL : %s/grid/console \n", separator,separator,hub.getUrl());
			
			System.out.println("Please Enter to stop service.");
			System.in.read();
			System.in.read();
			
			hub.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		hubServer();
	}
}
