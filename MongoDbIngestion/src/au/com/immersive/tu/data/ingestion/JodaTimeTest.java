package au.com.immersive.tu.data.ingestion;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class JodaTimeTest {

    public static void main(String[] args) {

        TimeZone tz = TimeZone.getTimeZone("Australia/Melbourne");
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        sdf.setTimeZone(tz);
        long time = 1377957600000L;

        LocalDate date = new LocalDate(time);
        Multimap<Integer, Long> map = ArrayListMultimap.create();
        for (int i = 0; i < 91; i++) {
            LocalDate nextDate = date.plusDays(i);
            int dayOfWeek = nextDate.dayOfWeek().get() - 1;
            long dayTime = nextDate.toDate().getTime();
            map.put(dayOfWeek, dayTime);
        }

        System.out.println(map);

        for (Long dayStart : map.get(3)) {
            System.out.print("{$and: [ {TIMESLOT_DATE: {$gte: " + dayStart + "}}, {TIMESLOT_DATE: {$lt: ");
            date = new LocalDate(dayStart);
            LocalDate nextDay = date.plusDays(1);
            System.out.print(nextDay.toDate().getTime());
            System.out.println("}} ]},");
        }

        for (Integer key : map.keySet()) {
            System.out.println("Day of week: " + key + " Number of days in 91 days: " + map.get(key).size());
        }

        LocalDate nextWeekDate = date.plusWeeks(1);
        int dayOfWeek = date.dayOfWeek().get() - 1;
        long time_end_day = 1377957600000L + ((23 * 60 + 59) * 60 + 59) * 1000;

        System.out.println(sdf.format(new Date(time)));
        System.out.println(date);
        System.out.println(time_end_day);

        System.out.println();

        System.out.println(nextWeekDate);
        System.out.println(nextWeekDate.getWeekOfWeekyear());
        System.out.println(nextWeekDate.withDayOfWeek(DateTimeConstants.MONDAY));



        //
        // LocalDate nextWeekDate = date.plusWeeks(1);
        //
        // long time_end_day = 1377957600000L + ((23 * 60 + 59) * 60 + 59) * 1000;
        //
        // System.out.println(date);
        // System.out.println(time_end_day);
        //
        // System.out.println();
        //
        // System.out.println(nextWeekDate);
        // System.out.println(nextWeekDate.getWeekOfWeekyear());
        // System.out.println(nextWeekDate.withDayOfWeek(DateTimeConstants.MONDAY));

        long SIX_OCTOBER_2013 = 1380981600000L;
        date = new LocalDate(SIX_OCTOBER_2013);
        System.out.println(date);
        System.out.println(date.dayOfWeek().getAsText());
        System.out.println(date.dayOfWeek().get());
        long OCT_6_2013 = 82800000L;
        date = new LocalDate(SIX_OCTOBER_2013 + OCT_6_2013);
        System.out.println(date);
        System.out.println(date.dayOfWeek().getAsText());
        System.out.println(date.dayOfWeek().get());
    }
}
