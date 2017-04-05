package com.github.runjarfile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RunJARFile {
	public static void main(String[] args) {
		RunJARFile.runJarUsingParams();
	}

	public static void runJarUsingParams() {
		/*MyJAR.jar - Main Class RemoveHardCode*/
		String jarLocation = "D:\\Yashwanth\\JarFiles\\MyJAR.jar";
		ArrayList<String> commandLineArguments = new ArrayList<String>();
		commandLineArguments.add("args1");
		commandLineArguments.add("args2");
		commandLineArguments.add("args3");
		
		HashMap<String, String> systemProperties = new HashMap<String, String>();
		systemProperties.put("key1", "value1");
		systemProperties.put("key2", "value2");
		systemProperties.put("key3", "value3");
		systemProperties.put("key4", "value4");
		System.out.println("Command-Line Arguments : " + RunJARFile.commandLineArgumentsList(commandLineArguments));
		System.out.println("Environment Variables" + RunJARFile.systemPropertiesMap(systemProperties));
		//RunJARFile.builder(jarLocation, commandLineArguments, systemProperties);
		RunJARFile.runtime(jarLocation, commandLineArguments, systemProperties);
	}

	public static String commandLineArgumentsList(List<String> commandLineArguments) {
		return commandLineArguments.toString().replaceAll("\\[|,|\\]", "");
	}

	public static String systemPropertiesMap(Map<String, String> systemProperties) {
		return systemProperties.toString().replaceAll("\\{|[,]", " -D").replaceAll("\\-D ", "-D").replaceAll("}", "");
	}

	public static void runtime(String jarLocation, List<String> commandLineArguments, Map<String, String> systemProperties) {
		String runCommand = "cmd.exe /c start java " + RunJARFile.systemPropertiesMap(systemProperties) + " -jar " + jarLocation + " " + RunJARFile.commandLineArgumentsList(commandLineArguments);
		System.out.println("Run Command : [" + runCommand + "]");
		try {
			Process process = Runtime.getRuntime().exec(runCommand);
			process.waitFor();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void builder(String jarLocation, List<String> commandLineArguments, Map<String, String> systemProperties) {
		String runCommand = "java " + RunJARFile.systemPropertiesMap(systemProperties) + " -jar " + jarLocation + " " + RunJARFile.commandLineArgumentsList(commandLineArguments);
		System.out.println("Run Command : [" + runCommand + "]");
		ProcessBuilder builder = new ProcessBuilder(Arrays.asList(runCommand.split(" ")));
		try {
			Process process = builder.start();
			InputStream inputStream = process.getInputStream();
			InputStream errorStream = process.getErrorStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			String s = "";
			while ((s = in.readLine()) != null) {
				System.out.println(s);
			}
			byte[] error = new byte[errorStream.available()];
			errorStream.read(error, 0, error.length);
			System.out.println("Error Stream \u00ab " + new String(error));
			process.destroy();
			int status = process.waitFor();
			System.out.println("Exited with status: " + status);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}