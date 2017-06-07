package com.github.xuggle.screen;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.TimeUnit;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;

public class RecordVideoThread implements Runnable {

	VideoParameters obj;
	public RecordVideoThread(VideoParameters obj) {
		this.obj = obj;
	}
	
	public static boolean capturestared = false;
	public static boolean record = true;

	public static void main(String[] args) {
		String filePath = "E:\\Test.mp4";
		
		VideoParameters obj = new VideoParameters( new File( filePath ) );
		RecordVideoThread recordVideoThread = new RecordVideoThread( obj );
		new Thread( recordVideoThread ).start();
		sleepThread( 1000 * 15  * 1 );
		record = false;
		System.out.println("record : "+record);
	}
	
	public static BufferedImage convertToType(BufferedImage sourceImage, int targetType) {
		BufferedImage image;
		// if the source image is already the target type, return the source image
		if (sourceImage.getType() == targetType) {
			image = sourceImage;
		}
		// otherwise create a new image of the target type and draw the new image
		else {
			image = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), targetType);
			image.getGraphics().drawImage(sourceImage, 0, 0, null);
		}
		return image;
	}
	
	@Override
	public void run() {
		try {
			Dimension screenBounds = Toolkit.getDefaultToolkit().getScreenSize();
			double FRAME_RATE = 50;
			
			Robot robot = new Robot();
			Rectangle captureSize = new Rectangle(screenBounds);
			
			// let's make a IMediaWriter to write the file.
			IMediaWriter writer = ToolFactory.makeWriter( obj.getFile().getAbsolutePath() );
			
			// We tell it we're going to add one video stream, with id 0,
			// at position 0, and that it will have a fixed frame rate of FRAME_RATE.
			writer.addVideoStream(0, 0,ICodec.ID.CODEC_ID_H264, screenBounds.width, screenBounds.height);
			long startTime = System.nanoTime();
			while( record ) {
				capturestared = true;
				// take the screen shot
				BufferedImage screen = robot.createScreenCapture( captureSize );
				// convert to the right image type
				BufferedImage bgrScreen = convertToType(screen, BufferedImage.TYPE_3BYTE_BGR);
				// encode the image to stream #0
				writer.encodeVideo(0, bgrScreen, System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
				sleepThread( (long)(1000/FRAME_RATE) );
			}
			// tell the writer to close and write the trailer if  needed
			writer.flush();
			writer.close();
			System.out.println("Recoding completed and saved into file.");
			
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Causes the currently executing thread to sleep for the specified number of milliseconds.
	 * @param time	the length of time to sleep in milliseconds
	 */
	public static void sleepThread(long millis) {
		try {
			Thread.sleep( millis );
		} catch (InterruptedException e) {
			System.out.println("Sleep Exception:"+ e.getMessage());
		}
	}
}

class VideoParameters {
	VideoParameters( File file ){
		this.file = file;
	}
	private File file;

	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
}