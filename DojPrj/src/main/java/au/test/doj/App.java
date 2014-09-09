package au.test.doj;

import java.util.Calendar;
import java.util.Date;

/**
 * Hello world!
 * 
 */
public class App {

    public static Date[] getCurrentFinYearZankar() throws Exception {
        Date[] ary = new Date[2];
        Calendar cal = Calendar.getInstance();
        int year = cal.get(1);
        cal = Calendar.getInstance();
        cal.set(1, year - 1);
        cal.set(2, 6);
        cal.set(5, 1);
        cal.set(10, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        Date minDate = cal.getTime();

        cal = Calendar.getInstance();
        cal.set(1, year);
        cal.set(2, 5);
        cal.set(5, 30);

        cal.set(14, 0);
        Date maxDate = cal.getTime();

        ary[0] = minDate;
        ary[1] = maxDate;
        return ary;
    }

    public static Date[] getCurrentFinYearViktar() throws Exception {
        Date[] ary = new Date[2];
        Calendar cal = Calendar.getInstance();

        ary[0] = FinancialYearCalculator.getFinYearBegin(cal);
        ary[1] = FinancialYearCalculator.getFinYearEnd(cal);

        return ary;
    }

}
