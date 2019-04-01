package enums;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 
 * @author yashwanth.m
 *
 */
public class RoundingModeMath {
	
	//  scale of the BigDecimal value to be returned.
	static int newScale = 2;
	
	public static void main(String[] args) {
		
		double value = 100.34567890;
		rounde(value, RoundingMode.CEILING);
		rounde(value, RoundingMode.FLOOR);
		
		System.out.println("Java DOC values test.");
		double[] values = {5.5, 2.5, 1.6, 1.1, 1.0, -1.0, -1.1, -1.6, -2.5, -5.5};
		
		for (RoundingMode mode : RoundingMode.values()) {
			System.out.println("===== "+ mode );
			for (double test : values) {
				try {
					rounde(test, mode);
				} catch( ArithmeticException e ) {
					System.err.println( e.getMessage() );
				}
			}
		}
		
	}
	
	public static double rounde( double value, RoundingMode mode ) {
		BigDecimal bd = new BigDecimal( value ).setScale( newScale , mode );
		Double doubleValue = bd.doubleValue();
		System.out.println(value +" : " + doubleValue );
		return doubleValue;
	}

}
