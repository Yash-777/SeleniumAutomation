package com.github.xuggle.screen;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import com.github.web.automation.BrowserDrivers;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.ICodec.ID;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IRational;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;


class StartRecord extends RecordToFile implements Runnable {
	public void run() {
		StartRecording( outputFileMP3, outputFileMP4 );
	}
}

class StopRecord extends RecordToFile implements Runnable {
	public void run() {
		isSTOPRecording = true;
	}
}

/**
 * The class RecordFile uses IMediaWriter,
 * <UL>IMediaWriter
 * <LI> Video « takes snapshots of your desktop and encodes them to video.</LI>
 * <LI> Audio « reads data line and save as audio</LI>
 * @author yashwanth.m
 *
 */
public class RecordToFile extends BrowserDrivers {

	static String outputFileMP4, outputFileMP3, outputFileFLV;
	static boolean isSTOPRecording = false;
	
	public static void main(String[] args) {
		outputFileMP4 = "IMediaWriterVedio.mp4";
		outputFileMP3 = "IMediaWriterAudio.mp3";
		outputFileFLV = "IMediaWriterAudioVedio.flv";
		new Thread( new StartRecord() ).start();
		
		new BrowserDrivers().sleepThread( 1000 * 10 );
		System.out.println("sleeping completed");
		
		new Thread( new StopRecord() ).start();
		
	}
	
	public void StartRecording( String audioOutput, String vedioOutput ) {
	
	try {
		String vediosPath = System.getProperty("user.dir")+"/images/",
			audioFile =  new File ( vediosPath+audioOutput ).toString(),
			veioFile = new File( vediosPath+vedioOutput ).toString();
		System.out.format("AUDIO FILE : [%S], \n VEDIO FILE : [%s]\n", audioFile, veioFile);
		
		Robot robot = new Robot();
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Rectangle screenBounds = new Rectangle( toolkit.getScreenSize() );
		Integer VIDEO_FRAMERATE = 100;
		ID AUDIO_CODEC = ICodec.ID.CODEC_ID_MP3, VEDIO_CODEC = ICodec.ID.CODEC_ID_MPEG4;
		float SAMPLE_RATE = 44100.0f;
		int CHANNEL_COUNT = 2;
		
		IMediaWriter vedioWriter = ToolFactory.makeWriter( veioFile );
		IRational FRAME_RATE = IRational.make( VIDEO_FRAMERATE, 1 );
		vedioWriter.addVideoStream(0, 0, VEDIO_CODEC, screenBounds.width, screenBounds.height);
		
		AudioFormat audioFormat = new AudioFormat(SAMPLE_RATE, 16, CHANNEL_COUNT, true, false);
		DataLine.Info dataLineInfo = new DataLine.Info( TargetDataLine.class, audioFormat );
		TargetDataLine line = (TargetDataLine) AudioSystem.getLine( dataLineInfo );
		IMediaWriter audioWriter = ToolFactory.makeWriter( audioFile );
		audioWriter.addAudioStream(0, 1, AUDIO_CODEC, CHANNEL_COUNT, (int) SAMPLE_RATE);
		line.open(audioFormat, line.getBufferSize());
		line.start();
		
		System.out.format("File Location MP4 [%s], MP3 [%s] \n", vedioOutput, audioOutput);
		
		long startTime = System.nanoTime();
		
		while ( !isSTOPRecording ) {
			BufferedImage screen = robot.createScreenCapture( screenBounds );
			BufferedImage bgrScreen = convertToType(screen, BufferedImage.TYPE_3BYTE_BGR);
			TimeUnit DEFAULT_TIME_UNIT = TimeUnit.NANOSECONDS;
			
			byte[] audioBytes = new byte[ line.getBufferSize() / 2 ];
			int numBytesRead = 0;
			numBytesRead = line.read(audioBytes, 0, audioBytes.length);
			int numSamplesRead = numBytesRead/2;
			short[] audioSamples = new short[numSamplesRead];
			if ( audioFormat.isBigEndian() ) {
				for (int j = 0; j < numSamplesRead; j++) {
					audioSamples[j] = (short)((audioBytes[2 * j] << 8) | audioBytes[2 * j + 1]);
				}
			} else {
				for (int j = 0; j < numSamplesRead; j++) {
					audioSamples[j] = (short)((audioBytes[2 * j + 1] << 8) | audioBytes[2 * j]);
				}
			}
			
			vedioWriter.encodeVideo(0, bgrScreen, System.nanoTime()-startTime, DEFAULT_TIME_UNIT);
			audioWriter.encodeAudio(0, audioSamples, System.nanoTime()-startTime, DEFAULT_TIME_UNIT);
			
			sleepThread( (long) (1000 / FRAME_RATE.getDouble()) );
			
			System.out.println("done");
		}
		
		// Tell the writer to close and write the trailer if needed
		if( isSTOPRecording ) {
			vedioWriter.close();
			audioWriter.close();

			System.out.println("Completed audio and vedio capturing, Ready to merge them together.");
			decodeAndSaveAudioVideo(audioFile, veioFile, vediosPath+outputFileFLV);
		}
	} catch (Throwable e) {
		System.err.println("an error occurred: " + e.getMessage());
	}
	
	}
	protected void stopRecording() {
		System.out.println("Stop Recording...");
		isSTOPRecording = true;
	}
	
	public BufferedImage convertToType(BufferedImage sourceImage, int targetType){
		BufferedImage image;
		/* If the source image is the same type as the target type,
		 * then original image is returned.*/
		if (sourceImage.getType() == targetType) {
			return sourceImage;
		}
		/* otherwise new image of the correct type is created 
		 * and the content of the source image is copied into the new image.
		 */
		else { 
			image = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), targetType);
			image.getGraphics().drawImage(sourceImage, 0, 0, null);
		}
		return image;
	}
	
	@SuppressWarnings("deprecation")
	public static void decodeAndSaveAudioVideo(String audioFile, String veioFile, String audioVedioFile) {
		IMediaWriter mWriter = ToolFactory.makeWriter( new File( audioVedioFile ).toString());
		
		IContainer containerVideo = IContainer.make();
		IContainer containerAudio = IContainer.make();
		
		if (containerVideo.open(veioFile, IContainer.Type.READ, null) < 0)
			throw new IllegalArgumentException("Cant find " + veioFile);
		
		if (containerAudio.open(audioFile, IContainer.Type.READ, null) < 0)
			throw new IllegalArgumentException("Cant find " + audioFile);
		
		int numStreamVideo = containerVideo.getNumStreams();
		int numStreamAudio = containerAudio.getNumStreams();
		
		System.out.println("Number of video streams: "+numStreamVideo+ 
							" \n Number of audio streams: "+numStreamAudio );

		int videostreamt = -1; //this is the video stream id
		int audiostreamt = -1;

		IStreamCoder videocoder = null;

		for(int i=0; i<numStreamVideo; i++) {
			IStream stream = containerVideo.getStream(i);
			IStreamCoder code = stream.getStreamCoder();
		
			if(code.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO)
			{
				videostreamt = i;
				videocoder = code;
				break;
			}
		
		}
	
		for(int i=0; i<numStreamAudio; i++) {
			IStream stream = containerAudio.getStream(i);
			IStreamCoder code = stream.getStreamCoder();
			
			if(code.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO)
			{
				audiostreamt = i;
				break;
			}
		}
	
		if (videostreamt == -1) throw new RuntimeException("No video steam found");
		if (audiostreamt == -1) throw new RuntimeException("No audio steam found");	
		if(videocoder.open() < 0 ) throw new RuntimeException("Cant open video coder");
		
		IPacket packetvideo = IPacket.make();
		IStreamCoder audioCoder = containerAudio.getStream(audiostreamt).getStreamCoder();
		
		if(audioCoder.open() < 0 ) throw new RuntimeException("Cant open audio coder");
		
		mWriter.addAudioStream(1, 1, audioCoder.getChannels(), audioCoder.getSampleRate());
		mWriter.addVideoStream(0, 0, videocoder.getWidth(), videocoder.getHeight());
		IPacket packetaudio = IPacket.make();
	
	while(containerVideo.readNextPacket(packetvideo) >= 0 || containerAudio.readNextPacket(packetaudio) >= 0) {
	
		if(packetvideo.getStreamIndex() == videostreamt) { //video packet
			IVideoPicture picture = 
				IVideoPicture.make(videocoder.getPixelType(),
						videocoder.getWidth(), videocoder.getHeight() );
			int offset = 0;
		
			while ( offset < packetvideo.getSize() ) {
				int bytesDecoded = 
						videocoder.decodeVideo(picture, packetvideo, offset);
				if(bytesDecoded < 0) throw new RuntimeException("bytesDecoded not working");
				
				offset += bytesDecoded;
				
				if(picture.isComplete()){
					System.out.println( picture.getPixelType() );
					mWriter.encodeVideo(0, picture);
				}
			}
		}
	
		if(packetaudio.getStreamIndex() == audiostreamt) { //audio packet
	
			IAudioSamples samples = 
				IAudioSamples.make(512, audioCoder.getChannels(), IAudioSamples.Format.FMT_S32);
			int offset = 0;
			while( offset<packetaudio.getSize() ) {
				int bytesDecodedaudio = 
					audioCoder.decodeAudio(samples, packetaudio, offset);
				if (bytesDecodedaudio < 0) throw new RuntimeException("could not detect audio");
				
				offset += bytesDecodedaudio;
				
				if ( samples.isComplete() ) {
					mWriter.encodeAudio(1, samples);
				}
			}
		}
	}
	}
}