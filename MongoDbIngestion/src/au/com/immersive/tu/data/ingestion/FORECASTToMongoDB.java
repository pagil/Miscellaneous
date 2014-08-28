package au.com.immersive.tu.data.ingestion;

import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import au.com.bytecode.opencsv.CSVReader;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class FORECASTToMongoDB {

    public static final String TEST_SERVER = "192.168.10.101";
    public static final String PROD_SERVER = "192.168.10.114";
    public static final String AWS_SERVER = "54.253.251.199";
    public static final String CSV_FILE = "C:\\Users\\vyafimchyk.IMMERSIVE\\Desktop\\Transurban - FORECAST\\ACTUAL_PROD_TO_INSERT.csv";
    public static final String CSV_FILE_FORECAST = "C:\\Users\\vyafimchyk.IMMERSIVE\\Desktop\\Transurban - FORECAST\\forecast passages.csv";

    public static final TimeZone tz = TimeZone.getTimeZone("Australia/Melbourne");
    public static final SimpleDateFormat sdf = new SimpleDateFormat("MMMMM dd yyyy");

    static {
        sdf.setTimeZone(tz);
    }

    public static void main(String[] args) throws Exception {

        MongoClient mongoClient = new MongoClient(AWS_SERVER);
        DB db = mongoClient.getDB("TU");
        DBCollection coll = db.getCollection("FORECAST_VYAFIMCHYK");

        CSVReader reader = new CSVReader(new FileReader(CSV_FILE));

        int counter = 0;

        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            BasicDBObject doc = new BasicDBObject("DAY", Long.parseLong(nextLine[1])).append("LINK", "Western")
                    .append("FORECAST", Long.parseLong(getForecast("Western", counter)))
                    .append("ACTUAL", Long.parseLong(nextLine[4])).append("INCIDENT", Long.parseLong(nextLine[11]))
                    .append("RAINFALL", "0".equals(nextLine[6]) ? "0" : "1");

            coll.insert(doc);

            System.out.printf("Processed %d object %s succesfully \n", counter, doc.toString());

            doc = new BasicDBObject("DAY", Long.parseLong(nextLine[1])).append("LINK", "Southern")
                    .append("FORECAST", Long.parseLong(getForecast("Southern", counter)))
                    .append("ACTUAL", Long.parseLong(nextLine[5])).append("INCIDENT", Long.parseLong(nextLine[12]))
                    .append("RAINFALL", "0".equals(nextLine[7]) ? "0" : "1");

            coll.insert(doc);

            System.out.printf("Processed %d object %s succesfully \n", counter, doc.toString());

            counter++;
        }

        reader.close();
        System.out.println("Done!");
    }

    private static String getForecast(String link, int dayNumber) throws Exception {

        CSVReader reader = new CSVReader(new FileReader(CSV_FILE_FORECAST));

        try {
            String[] nextLine;
            int counter = 0;
            while ((nextLine = reader.readNext()) != null) {
                if (counter == dayNumber) {
                    if ("Western".equals(link)) {
                        return nextLine[0];
                    } else {
                        return nextLine[1];
                    }
                }
                counter++;
            }
        } finally {
            reader.close();
        }

        return "0";
    }

}
