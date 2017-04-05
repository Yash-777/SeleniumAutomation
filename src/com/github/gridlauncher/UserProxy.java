package com.github.gridlauncher;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openqa.grid.common.RegistrationRequest;
import org.openqa.grid.internal.Registry;
import org.openqa.grid.internal.TestSession;
import org.openqa.grid.selenium.proxy.DefaultRemoteProxy;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;

/**
 * A proxy or proxy server is basically another computer which serves as a hub through 
 * which Internet requests are processed. By connecting through one of these servers,
 * your computer sends your requests to the proxy server which then processes your 
 * request and returns what you were wanting.
 * 
 * @author yashwanth.m
 *
 */
public class UserProxy extends DefaultRemoteProxy {

	static String separator = "\n-------------------------------\n";
	
	public UserProxy(RegistrationRequest request, Registry registry) {
		super(request, registry);
	}

	@Override
	public void beforeCommand(TestSession session, HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println( separator +" beforeCommand");
		
		Map<String, Object> cap = session.getRequestedCapabilities();
		for (Entry<String, Object> e : cap.entrySet() ) {
			System.out.println(String.format("%s = %s", e.getKey(), e.getValue()));
		}
		if (BrowserType.FIREFOX.equals(cap.get(CapabilityType.BROWSER_NAME))) {
			System.out.println("\t\t FIREFOX ");
		}
		if (BrowserType.CHROME.equals(cap.get(CapabilityType.BROWSER_NAME))) {
			System.out.println("\t\t CHROME ");
		}
		if (BrowserType.IE.equals(cap.get(CapabilityType.BROWSER_NAME))) {
			System.out.println("\t\t IE ");
		}
		super.beforeCommand(session, request, response);
	}
	
	@Override
	public void afterCommand(TestSession session, HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println(" afterCommand " + separator);
		super.afterCommand(session, request, response);
	}
	
	@Override
	public void beforeSession(TestSession session) {
		System.out.println("beforeSession");
		super.beforeSession(session);
	}
	
	@Override
	public void afterSession(TestSession session) {
		System.out.println("afterSession");
		super.afterSession(session);
	}
}
