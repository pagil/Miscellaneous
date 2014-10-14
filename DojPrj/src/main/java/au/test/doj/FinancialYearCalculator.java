package au.test.doj;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FinancialYearCalculator {
    private static final int BEGIN_MONTH = 6;
    private static final int BEGIN_DATE = 1;
    private static final int END_MONTH = 5;
    private static final int END_DATE = 30;

    public static Date getFinYearBegin(Calendar cal) {
        int year = cal.get(1);
        Calendar instance = Calendar.getInstance();
        instance.set(year, 6, 1, 0, 0, 0);
        if (cal.after(instance)) {
            return instance.getTime();
        }
        instance.set(1, year - 1);
        return instance.getTime();
    }

    public static Date getFinYearEnd(Calendar cal) {
        int year = cal.get(1);
        Calendar instance = Calendar.getInstance();
        instance.set(year, 5, 30);

        instance.set(14, 0);
        if (cal.after(instance)) {
            instance.set(1, year + 1);
            return instance.getTime();
        }
        return instance.getTime();
    }

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss a z");
        Calendar cal = Calendar.getInstance();
        cal.set(2, 5);
        System.out.println(sdf.format(cal.getTime()));
        System.out.println(sdf.format(getFinYearBegin(cal)));
        System.out.println(sdf.format(getFinYearEnd(cal)));

        Date[] ary = new Date[2];
        cal = Calendar.getInstance();
        int year = cal.get(1);
        cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year - 1);
        cal.set(Calendar.MONTH, 6);
        cal.set(Calendar.DATE, 1);
        // This sets HOURS to 12:00:00
        cal.set(Calendar.HOUR, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        Date minDate = cal.getTime();
        System.out.println(sdf.format(minDate));
        // This sets HOURS to 00:00:00
        cal.set(year, 6, 1, 0, 0, 0);
        System.out.println(sdf.format(cal.getTime()));
        cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year - 1);
        cal.set(Calendar.MONTH, 6);
        cal.set(Calendar.DATE, 1);
        // This sets HOURS to 00:00:00
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        System.out.println(sdf.format(cal.getTime()));
    }
}
