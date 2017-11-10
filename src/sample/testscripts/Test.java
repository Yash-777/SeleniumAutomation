package sample.testscripts;

import org.apache.commons.lang3.StringUtils;

public class Test {
	public static void main(String[] args) {
		verionRange();
		xpathIndex();
	}
	public static boolean containsIgnoreCase(String source, String searchChar) {
		return StringUtils.containsIgnoreCase(source, searchChar);
	}
	public static void verionRange() {
		String ff = "49";
		System.out.println( ff.matches("[0-9]+") );
		System.out.println( ff.length() );
		if (ff.matches("[0-9]+")) {
			int parseInt = Integer.parseInt(ff);
			System.out.println( "Int : "+ parseInt );
			
			if ( parseInt <= 47 ) {
				System.out.println("Marinto Driver");
			} else if ( parseInt > 47 ) {
				System.out.println("Gecho Driver");
			}
		}
	}
	public static void xpathIndex() {
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
}
