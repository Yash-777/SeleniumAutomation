package sample.testscripts;

import org.apache.commons.lang3.StringUtils;

public class Test {
	public static void main(String[] args) {
		String locator = 
				//"//select[1]"; 
				"//select[1]/option[2]";
		int n = locator.lastIndexOf("/");
		String options = locator.substring(n+1, locator.length());
		System.out.println(options);
		String index = options.replaceAll("[^\\d]", "");
		System.out.println(options +" : "+ index);
		
		String select = locator.substring(0, n);
		System.out.println(select);
		
		System.out.println( containsIgnoreCase(options, "SELECT"));
	}
	public static boolean containsIgnoreCase(String source, String searchChar) {
		return StringUtils.containsIgnoreCase(source, searchChar);
	}
}
