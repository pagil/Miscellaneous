package au.com.immersive.tu.experiment;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.joda.time.LocalDateTime;

public class TimeSlotsPrinter {

    public static void main(String[] args) {
        TimeZone tz = TimeZone.getTimeZone("Australia/Melbourne");
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        sdf.setTimeZone(tz);

        long OCT_25_2013 = 1385298000000L;
        int TIME_SLOT = 15; // 15 minutes

        // Joda time differs the LocalDate and LocalDateTime => it will ignore the added 15 minus below.

        // Minutes quarter = Minutes.minutes(15);
        // LocalDate date = new LocalDate(OCT_25_2013);

        // System.out.println(date.plus(quarter));

        // for (int i = 0; i < 96; i++) {
        //
        // LocalDate tmp = date.plus(quarter.multipliedBy(i));
        // System.out.printf("Timeslot %2d = %s\n", i, sdf.format(tmp.toDate()));
        // }

        LocalDateTime dateTime = new LocalDateTime(OCT_25_2013);

        for (int i = 0; i < 96; i++) {
            LocalDateTime tmp = dateTime.plusMinutes(TIME_SLOT * i);
            System.out.printf("Timeslot %2d = %s\n", i, sdf.format(tmp.toDate()));
        }

    }
}
