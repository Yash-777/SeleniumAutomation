package com.github.gridlauncher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openqa.grid.common.RegistrationRequest;
import org.openqa.grid.internal.Registry;
import org.openqa.grid.internal.TestSession;
import org.openqa.grid.selenium.proxy.DefaultRemoteProxy;

public class UserProxy extends DefaultRemoteProxy {

	static String separator = "\n-------------------------------\n";
	
	public UserProxy(RegistrationRequest request, Registry registry) {
		super(request, registry);
	}

	@Override
	public void beforeCommand(TestSession session, HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println( separator +" beforeCommand");
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
