package com.github.server.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FileUtils;
/**
 * Download the file and maintain locally.
 * @author yashwanth.m
 *
 */
public class FileFromURL {
	String urlStr;
	byte[] buffer; 
	URL url;
	
	public FileFromURL( String urlStr) {
		this.urlStr = urlStr;
		try {
			url = new URL( urlStr );
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		buffer = new byte[1024];
	}
	
	public static void main(String[] args) throws IOException {
		String urlStr = "http://chromedriver.storage.googleapis.com/2.24/chromedriver_win32.zip";
		String chromeDriver = "D:\\JARS\\chromedriver_stream.zip";
		//new FileFromURL(urlStr).downloadUsingStream( chromeDriver );
		new FileFromURL(urlStr).downloadUsing_NIOChannel( chromeDriver );
	}

	public void downloadUsingStream( String targetfile) throws IOException {
		
		BufferedInputStream bis = new BufferedInputStream( url.openStream() );
		FileOutputStream fos = new FileOutputStream( new File( targetfile ) );
		int count=0;
		
		while((count = bis.read(buffer,0,1024)) != -1) {
			fos.write(buffer, 0, count);
		}
		
		fos.close();
		bis.close();
	}
	
	/**
	 * Channels are designed to provide for bulk data transfers to and from NIO buffers.
	 * 
	 * https://en.wikipedia.org/wiki/Non-blocking_I/O_(Java)
	 * @param urlStr
	 * @param file
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public void downloadUsing_NIOChannel( String targetfile ) throws IOException {
		try {
		ReadableByteChannel rbc = Channels.newChannel( url.openStream() );
		
		// Getting file channels
		FileChannel out = new FileOutputStream( targetfile ).getChannel();
		// JavaVM does its best to do this as native I/O operations.
		out.transferFrom( rbc, 0, Long.MAX_VALUE );
		
		// Closing file channels will close corresponding stream objects as well.
		out.close();
		rbc.close();
		} catch ( javax.net.ssl.SSLHandshakeException e) {
			// Remote host closed connection during handshake - Check Internet.
		}
	}
	
	public void downloadUsing_NIOFileCopy( String targetfile ) throws IOException {
		Files.copy(url.openStream(), new File( targetfile ).toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
	
	public void downloadUsing_FileUtils( String targetfile ) throws IOException {
		FileUtils.copyURLToFile( url, new File(targetfile) ); 
	}
}