<%@ page language="java" contentType="application/x-java-jnlp-file" pageEncoding="ISO-8859-1"%>

<%
int xmx = 1024;
int xms = 256;
String JVM_Options = "-Xss8m"; /* "-ea -Xincgc"; "-Dsun.java2d.noddraw=true"; */

String server = "2.53.0";
int javaVersion = Integer.valueOf( request.getParameter("java") );
if ( javaVersion < 7 ) {
	server = "2.45.0";
}

String appURL = "http://127.0.0.1:8088/";
String ApplicationName = "ApplicationName";
String Application_RunnableJAR = "ApplicationMainJAR.jar";
String mainClassName = "com.selenium.yash.SeleniumWebDriver";
String args0 = request.getParameter("testApp");
String args1 = request.getParameter("platform");
String args2 = request.getParameter("browser");
String args3 = request.getParameter("version");
%>

<?xml version="1.0" encoding="utf-8"?>
<!-- 
Configure the Web server to use the Java Web Start MIME type. application/x-java-jnlp-file JNLP

http://IP:PORT/ApplicationName/DynamicJNLP_ThroughJSP.jsp?testApp=https://github.com/Yash-777&platform=windows&browser=chrome&version=53.0&java=6

This is the main xml element for a jnlp file. Everything [like describes application and its resources...] is contained within the jnlp element.
http://docs.oracle.com/javase/8/docs/technotes/guides/javaws/developersguide/syntax.html

jnlp href - all values must be provided here and their should be no spacess in between parameters.
-->
<jnlp 
	spec="1.0+" 
	codebase="<%=appURL%>" 
	href="<%=ApplicationName%>/DynamicJNLP_ThroughJSP.jsp?testApp=<%= args0 %>&platform=<%= args1 %>&browser=<%= args2 %>&version=<%= args3 %>&java=<%= javaVersion %>" 
	version="0.1">
	
	<information>
		<title> JNLP File Title for an Application </title>
		<vendor>Yash-777 provides this Application </vendor>
		<homepage href="<%=appURL%>"> The homepage of the application. </homepage>
		<description kind="automation"> A short statement describing the application. </description>
		<icon href="<%=appURL%><%=ApplicationName%>/logo.png" kind="default"/>
	
		<offline-allowed/>
		<!-- https://docs.oracle.com/javase/8/docs/technotes/guides/javaws/developersguide/setup.html -->
		<association mime-type="application/x-java-jnlp-file" extensions="MyJNLP"/>
		<shortcut online="false" install="false">
			<desktop> Shortcut on the users desktop. </desktop>
			<menu submenu="My Corporation Apps"/>
		</shortcut>
	
	</information>
	
	<security>
		<all-permissions/>
	</security>
	
	<update check="background" policy="always"/>
	
	<!-- Selects the initial and maximum memory sizes available to the JVM, respectively. 
		These values are used for the JVM heap. Specifies the amount of memory, in Megabytes,
		that will be dedicated to running the server.
		https://docs.oracle.com/cd/E22289_01/html/821-1274/configuring-the-default-jvm-and-java-arguments.html
	-->
	<resources>
		<j2se version="1.6+" 
		java-vm-args="-Xms<%=xms%>m -Xmx<%=xmx%>m <%=JVM_Options%> 
						-Dwebdriver.chrome.driver=D:\\Drivers\\chromedrivers\\2.24\\chromedriver.exe" />
		
		<!-- Make the application (Ex. jar file) accessible on the Web server.
			JWS queries the Webserver to determine if all the resources needed for the application are already downloaded (cached). 
		-->
		<jar href="<%=Application_RunnableJAR%>" main="true"/>
		
		<!-- Application Runnable JAR depends on these below specified jar's -->
		<jar href="selenium-server-standalone-<%=server%>.jar"/>
	</resources>
	
	<!-- Runnable Jar file Main class and its input parameters -->
	<application-desc main-class="<%= mainClassName %>" >
		<argument><%= args0 %></argument>
		<argument><%= args1 %></argument>
		<argument><%= args2 %></argument>
		<argument><%= args3 %></argument>
	</application-desc>	
</jnlp>
