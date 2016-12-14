package com.github.gridlauncher;

import java.io.File;

import org.openqa.selenium.net.NetworkUtils;

public enum GRIDINFO {
	PROJECTPATH () {
		@Override public String toString() {
			return System.getProperty("user.dir");
		}
	},
	HOSTIP () {
		@Override public String toString() {
			NetworkUtils utils = new NetworkUtils();
			String host = utils.getIp4NonLoopbackAddressOfThisMachine().getHostAddress();
			return host;
		}
	},
	CHROME_DRIVER_EXE () {
		@Override public String toString() {
			return GRIDINFO.PROJECTPATH.toString()+"/Drivers/Chrome/2.24/chromedriver.exe";
		}
	},
	IE_DRIVER_EXE () {
		@Override public String toString() {
			return GRIDINFO.PROJECTPATH.toString()+"/Drivers/IExplore/32/IEDriverServer.exe";
		}
	},
	NDOEJSON () {
		@Override public String toString() {
			File JOSNFile = new File( GRIDINFO.PROJECTPATH.toString()+"/registerNode.json" );
			return JOSNFile.toString();
		}
	},
	HUBJSON () {
		@Override public String toString() {
			File JOSNFile = new File( GRIDINFO.PROJECTPATH.toString()+"/gridHub.json" );
			return JOSNFile.toString();
		}
	};
}
