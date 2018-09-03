package sample.testscripts;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class DownloadCRXFile {
	public static void main(String[] args) throws IOException {
		String crx = "https://clients2.google.com/service/update2/crx?";
		String urlStr = crx+"response=redirect&prodversion=49.0&x=id%3Dieelmcmcagommplceebfedjlakkhpden%26installsource%3Dondemand%26uc";
		URL url = new URL( urlStr );
		String tempPath = System.getProperty("java.io.tmpdir");
		String targetFile = "Disable-Content-Security-Policy.crx";
		
		String storeFileName = tempPath + File.separator + targetFile;
		Path path = new File( storeFileName ).toPath();
		Files.copy(url.openStream(), path, StandardCopyOption.REPLACE_EXISTING);
		
		System.out.println( "Extension file saved into File : "+ storeFileName );
	}
}
