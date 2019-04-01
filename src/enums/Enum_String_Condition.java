package enums;

/**
 * https://stackoverflow.com/a/2836482/5081877
 * 
 * @author yashwanth.m
 *
 */
public class Enum_String_Condition {
	public enum MethodType {
		DOUBLE,LIST,STRING,ARRAYLIST,FLOAT,LONG;
	}
	public static void main( String[] args ) throws Exception {
		String value = "DOUBLE";
		System.out.println("Vlaue : " + value);
		switch( MethodType.valueOf( value.toUpperCase() ) ) {
		case DOUBLE:
			System.out.println( "It's a double" );
			break;
		case LIST:
			System.out.println( "It's a list" );
			break;
		default:
			break;
		}
	}
}
