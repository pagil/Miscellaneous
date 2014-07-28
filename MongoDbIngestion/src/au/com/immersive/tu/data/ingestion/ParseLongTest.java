package au.com.immersive.tu.data.ingestion;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import au.com.immersive.tu.data.AverageValueObject;

public class ParseLongTest {

    public static long SIX_OCTOBER_2013 = 1380981600000L;
    public static long OCT_6_2013 = 82800000L;

    public static void main(String[] args) {
        // Long var = Long.parseLong("4366.799999999726");
        Double var = Double.parseDouble("4366.799999999726");
        System.out.println(var);
        long firstSeptember2013 = 1377957600000L;
        System.out.printf("%d\n", firstSeptember2013);
        TimeZone tz = TimeZone.getTimeZone("Australia/Melbourne");
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        sdf.setTimeZone(tz);
        System.out.println(sdf.format(new Date(1377957600000L)));
        System.out.println(sdf.format(new Date(1382450400000L)));
        System.out.println(sdf.format(new Date(SIX_OCTOBER_2013)));
        System.out.println(sdf.format(new Date(SIX_OCTOBER_2013 + OCT_6_2013)));

        AverageValueObject vo = new AverageValueObject();
        System.out.println(vo);

        double val = Double.NaN;
        System.out.println(val == Double.NaN);

        Double revenue = Double.parseDouble("4320.379999999947");
        Double volume = Double.parseDouble("829");
        revenue /= 13;
        volume /= 13;
        System.out.println("Revenue= " + revenue);
        System.out.println("Volume= " + volume);
    }

}
