package sample.testscripts;

public class RemoteSeleniumURL extends ReadProperties {
	private static String USERNAME = remoteProps.getProperty("USERNAME"),
			ACCESS_KEY = remoteProps.getProperty("ACCESS_KEY"),
			DOMAIN_NAME = remoteProps.getProperty("DOMAIN_NAME");
	private static String URL = 
			"https://" + USERNAME + ":" + ACCESS_KEY + "@"+DOMAIN_NAME+"/wd/hub";
	
	public static String getURL() {
		StringBuilder remoteURL = new StringBuilder("https://");
		remoteURL.append( USERNAME );
		remoteURL.append(":");
		remoteURL.append( ACCESS_KEY );
		remoteURL.append("@"+ DOMAIN_NAME +"/wd/hub");
		return URL;
	}
}
