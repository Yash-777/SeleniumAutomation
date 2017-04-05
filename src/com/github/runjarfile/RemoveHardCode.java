package com.github.runjarfile;

public class RemoveHardCode {
	public static void main(String[] args) throws InterruptedException {
		StringBuffer argumentsList = new StringBuffer();
		int i = 0;
		while (i < args.length) {
			argumentsList.append("args[" + i + "] = " + args[i] + "\n");
			++i;
		}
		System.out.println("Command line arguments :\n" + argumentsList.toString());
		System.out.format("System properties :\n [key1:%s] \n [key2:%s]", System.getProperty("key1"), System.getProperty("key2"));
		Thread.sleep(5000);
	}
}