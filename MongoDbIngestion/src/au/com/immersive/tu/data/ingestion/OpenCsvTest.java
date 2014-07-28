package au.com.immersive.tu.data.ingestion;

import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import au.com.bytecode.opencsv.CSVReader;

public class OpenCsvTest {

    public static final String CSV_FILE = "C:\\Users\\vyafimchyk.IMMERSIVE\\Desktop\\Transurban - FORECAST\\forecast passages modified.csv";
    public static final TimeZone tz = TimeZone.getTimeZone("Australia/Melbourne");
    public static final SimpleDateFormat sdf = new SimpleDateFormat("MMMMM dd yyyy");

    static {
        sdf.setTimeZone(tz);
    }

    public static void main(String[] args) throws Exception {

        CSVReader reader = new CSVReader(new FileReader(CSV_FILE));
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            // nextLine[] is an array of values from the line

            Date date = sdf.parse(nextLine[0]);

            if (date.getTime() > 1380981600000L) {
                // Add 1 hour
                System.out.println("{\"Day\":" + (date.getTime() + (1 * 60 * 60 * 1000)) + ",\"Western\":"
                        + nextLine[1] + ",\"Southern\":" + nextLine[2] + "}");
            } else {
                System.out.println("{\"Day\":" + date.getTime() + ",\"Western\":" + nextLine[1] + ",\"Southern\":"
                        + nextLine[2] + "}");
            }
        }
        System.out.println("Done!");
    }
}
