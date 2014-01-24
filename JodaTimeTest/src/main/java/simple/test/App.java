package simple.test;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

/**
 * Hello world!
 * 
 */
public class App {
	public static void main(String[] args) {
		System.out.println("-Duser.timezone = " + System.getProperty("user.timezone"));
		System.out.println("joda time library version: "+(args.length > 0 ? args[0] : "undefined"));
		DateTime dateTime = new DateTime(1353990300000L);

		System.out.println(dateTime);

		DateTimeFormatter dateTimeFormatter = org.joda.time.format.DateTimeFormat.forPattern("dd.MM.yyyy");

		DateTime strDateTime = dateTimeFormatter.parseDateTime("27.11.2012");

		System.out.println(strDateTime);

		System.out.println("Default TimeZone = " + TimeZone.getDefault());
		System.out.println("Default TimeZone ID = " + TimeZone.getDefault().getID());
		System.out.println("Default TimeZone DisplayName = " + TimeZone.getDefault().getDisplayName());
		java.util.Date jdkDate = new java.util.Date(1353990300000L);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss Z");
		System.out.println("jdkDate = " + jdkDate);
		System.out.println("jdkDate = " + simpleDateFormat.format(jdkDate));
	}
}
