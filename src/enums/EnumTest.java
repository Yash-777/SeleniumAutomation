package enums;

import java.io.File;
import java.lang.reflect.Field;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.net.NetworkUtils;

/**
 * An enum type is a special data type 
 * that enables for a variable to be a set of predefined constants. The variable must 
 * be equal to one of the values that have been predefined for it.
 * 
 * <UL> All enums implicitly extend java.lang.Enum.
 * <LI> Enum type's fields are in UpperCase letters, As they are constants.
 * <LI> The enum declaration defines a class (called an enum type).
 * The enum class body can include methods and other Fields.
 * <LI> As You cannot invoke an enum constructor yourself. 
 * The constructor <B>Fields must be private accessed.</B>
 * <LI> The compiler automatically adds some <B>special methods</B> when it creates
 * an enum, Like:<P>EnumClass.class {getEnumConstants(), getFields(), isEnum(), ...}.
 * @author yashwanth.m
 * @see https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html
 * 
 */
public class EnumTest {
	enum constats {
		FIREFOX, CHROME;
	}
	enum ValuesFormOverriding {
		FIREFOX {
			@Override public String toString() {	return "Firefox";	}
		},
		CHROME {
			@Override public String toString() {	return "Chrome";	}
		}
	}
	enum ValuesThroughConstuctor {
		FIREFOX("Firefox"),
		CHROME("CH") {
			@Override public String toString() {	return "Chrome";	}
		};
		
		private String value;
		ValuesThroughConstuctor(final String value) {	this.value = value;	}

		public String getValue() {	return value;	}
	}
	
	enum Language{
		// Non equal parameters, So using Variable arguments String...
		English("english", "eng", "en", "en_GB", "en_US"),
		German("german", "de", "ge"),
		Russian("russian"),
		
		// DriverPack
		FF53 ("39", "40", "41", "42", "43", "44", "45"),
		FF47 ("34", "35", "36", "37", "38"),
		FF44 ("24", "31", "32", "33");
			
		private final List<String> values;
		
		Language(String ...values) {	this.values = Arrays.asList(values);	}
		
		public List<String> getValues() {	return values;	}
		
		public static List<String> getSimilarKeys( String value ) {
			Language language = ALIAS_MAP.get(value.toUpperCase());
			return language.getValues();
		}
		
		public static Language find(String name) {
			if (name == null) throw new NullPointerException("alias null");
			for (Language lang : Language.values()) {
				if (lang.getValues().contains(name)) {
					return lang;
				}
			}
			return null;
		}
		
		static private Map<String,Language> ALIAS_MAP = new HashMap<String,Language>();
		static {
			for (Language language:Language.values()) {
				// ignoring the case by normalizing to UpperCase
				ALIAS_MAP.put(language.name().toUpperCase(),language);
				for (String alias:language.values)
					ALIAS_MAP.put(alias.toUpperCase(),language);
			}
		}
		
		static public boolean has(String value) {
			return ALIAS_MAP.containsKey(value.toUpperCase());
		}
		
		static public Language fromString(String value) {
			if (value == null) throw new NullPointerException("alias null");
			
			Language language = ALIAS_MAP.get(value);
			if (language == null) throw new IllegalArgumentException("Not an alias: "+value);
			
			return language;
		}
	}
	
	enum Days {
		MON(1,"Monday"),	TUE(2,"Tuesday"),	WED(3,"Wednesday"),
		THU(4,"Thrusday"),	FRI(5,"Friday"),	SAT(6,"Saturday"),
		SUN(7,"Sunday");

		private int id;
		private String day;

		Days(int id, String day) {
			this.id = id;	this.day = day;
		}
		
		public int getId() {	return id;	}
		public String getDay() {	return day;	}
	}
	
	enum PlatformApp {

		OSName () {
			@Override public String toString() {
				String version = System.getProperty("os.version", "0.0.0");
				int major = 0;
				int min = 0;

				Pattern pattern = Pattern.compile("^(\\d+)\\.(\\d+).*");
				Matcher matcher = pattern.matcher(version);
				if (matcher.matches()) {
					try {
						major = Integer.parseInt(matcher.group(1));
						min = Integer.parseInt(matcher.group(2));
					} catch (NumberFormatException e) { }
				}
				
				System.out.println("SystemArchitecture : "+ System.getProperty("os.arch"));
				System.out.println("BIT Version : "+ version);
				System.out.format("Majour : %d Minor : %d \n", major , min);
				
				return System.getProperty("os.name").toUpperCase();
			}
		},
		codePath () {
			@Override
			public String toString() {
				ProtectionDomain protectionDomain = PlatformApp.class.getProtectionDomain();
				final File codePath = new File( protectionDomain.getCodeSource().getLocation().getPath() );
				System.out.println("Source CODE Path : "+codePath);
				return codePath.getPath();
			}
		},
		classPath () {
			@Override public String toString() {
				String classpath = System.getProperty("java.class.path");
				String[] classpathEntries = classpath.split( File.pathSeparator );
				String[] library = new String[ classpathEntries.length ];
				
				for ( int i = 0, j = 0; i < classpathEntries.length; i++ ) {
					if( !classpathEntries[i].equals( codePath ) 
							&& classpathEntries[i].contains("lib") ) {
						library[ j++ ] = classpathEntries[i];
						System.out.format("%d : %s \n", i, classpathEntries[i] );
					}
				}
				return super.toString();
			}
		},
		
		hostIP () {
			@Override public String toString() {
				NetworkUtils utils = new NetworkUtils();
				String host = utils.getIp4NonLoopbackAddressOfThisMachine().getHostAddress();
				return host;
			}
		};
	}
	
	public static void main(String[] args) {
		System.out.println("KEY's < ");
		System.out.println("F : " + constats.FIREFOX);
		System.out.println("C : " + constats.CHROME);
		
		System.out.println("Each toString @Override < ");
		System.out.println("F : " + ValuesFormOverriding.FIREFOX);
		System.out.println("C : " + ValuesFormOverriding.CHROME);
		
		System.out.println("Constructor instead of multipe toString @Override < ");
		System.out.println("F : " + ValuesThroughConstuctor.FIREFOX);
		System.out.println("C : " + ValuesThroughConstuctor.CHROME);
		
		System.out.println("Multi constructor Values < http://stackoverflow.com/a/12659023/5081877");
		System.out.println("ENG : "+Language.find("en_GB"));
		System.out.println("ENG Similar : "+Language.getSimilarKeys("en_GB"));
		
		System.out.println("ENG Values : "+Language.English.getValues());
		System.out.println("DriverPack : "+Language.find("37"));
		System.out.println("DriverPack Similar : "+Language.getSimilarKeys("37"));
		
		System.out.println("Fixed constructor values > http://stackoverflow.com/a/35057962/5081877");
		System.out.println("ID : "+Days.FRI.getId());
		System.out.println("Name : "+Days.FRI.getDay());
		System.out.println("Enum Day Fields : "+Days.class.getFields().length);
		Field[] constants = Days.class.getFields();
		for (Field field : constants) {
			System.out.println("DAY : "+field.getName());
		}
		
		System.out.println("Platfrom \n -------\n OS : "+ PlatformApp.OSName);
		System.out.println(" HOST IP : "+PlatformApp.hostIP);
	}
}