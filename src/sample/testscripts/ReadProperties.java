package sample.testscripts;

import java.io.IOException;
import java.util.Properties;

import com.github.web.automation.Browser;

public class ReadProperties {
	static Properties localProps = new Properties(), remoteProps = new Properties();
	static {
		// Class Loader - Reads Form 'src' Folder (Stand Alone application)
		ClassLoader AppClassLoader = Browser.class.getClassLoader();
		try {
			localProps.load(AppClassLoader.getResourceAsStream("localBrowserDetails.properties"));
			System.out.println("===== Propertie [KEY : Values] =====");
			for(String key : localProps.stringPropertyNames()) {
				System.out.format("%s : %s \n", key, localProps.getProperty(key));
			}
			System.out.println("===== ===== ===== =====");
			
			remoteProps.load(AppClassLoader.getResourceAsStream("remoteBrowserProperties.properties"));
			System.out.println("===== Propertie [KEY : Values] =====");
			for(String key : remoteProps.stringPropertyNames()) {
				System.out.format("%s : %s \n", key, remoteProps.getProperty(key));
			}
			System.out.println("===== ===== ===== =====");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static Properties getLocalProperties() {
		return localProps;
	}
	static Properties getRemoteProperties() {
		return remoteProps;
	}
}
