package au.com.immersive.tu.data.ingestion;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import au.com.immersive.tu.data.enums.Backward;
import au.com.immersive.tu.data.enums.Direction;
import au.com.immersive.tu.data.enums.Forward;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class CumulativeAggregatorToMongoDb {

    private static final String LOG_FILE_NAME = "C:\\Users\\vyafimchyk.IMMERSIVE\\Desktop\\Transurban - FORECAST\\Cumulative.log";
    private static final String PROD_MONGO_SERVER = "192.168.10.114";
    // private static final String TARGET_COLLECTION = "CUMULATIVE_VYAFIMCHYK";
    private static final String TARGET_COLLECTION = "CUMULATIVE";
    private static DBCollection TARGET_COLLECTION_CONNECTION = null;
    private static DBCollection CASTTRIP_COLLECTION_CONNECTION = null;
    private static DBCollection FORECAST_COLLECTION_CONNECTION = null;
    public static final long FIRST_SEPTEMBER_2013 = 1377957600000L;
    public static final long DAY = 86400000L;
    public static final long SIX_OCTOBER_2013 = 1380981600000L;
    public static final long OCT_6_2013 = 82800000L;
    // public static final int NUMBER_OF_DAYS = 91;
    public static final int NUMBER_OF_DAYS = 36;// Fixed to 36 to process 1 day = 6 Oct 2013
    public static final int NUMBER_OF_SLOTS = 96;
    public static final int NUMBER_OF_SLOTS_OCT_6_2013 = 92;

    private static long[] VOLUME_CUMULATIVE = new long[NUMBER_OF_SLOTS];
    private static long[] VOLUME_CUMULATIVE_WL = new long[NUMBER_OF_SLOTS];
    private static long[] VOLUME_CUMULATIVE_SL = new long[NUMBER_OF_SLOTS];

    private static long[] VOLUME_BY_TIMESLOT = new long[NUMBER_OF_SLOTS];
    private static long[] VOLUME_BY_TIMESLOT_WL = new long[NUMBER_OF_SLOTS];
    private static long[] VOLUME_BY_TIMESLOT_SL = new long[NUMBER_OF_SLOTS];

    private static double[] PRECIPITATION = new double[NUMBER_OF_SLOTS];
    private static double[] PRECIPITATION_WL = new double[NUMBER_OF_SLOTS];
    private static double[] PRECIPITATION_SL = new double[NUMBER_OF_SLOTS];

    private static long FORECAST_VOLUME = 0L;
    private static long FORECAST_VOLUME_SL = 0L;
    private static long FORECAST_VOLUME_WL = 0L;

    // private static PrintWriter out = null;

    public static void main(String[] args) throws Exception {
        MongoClient mongoClient = new MongoClient(PROD_MONGO_SERVER);
        DB db = mongoClient.getDB("TU");
        TARGET_COLLECTION_CONNECTION = db.getCollection(TARGET_COLLECTION);
        CASTTRIP_COLLECTION_CONNECTION = db.getCollection("CASTTRIP");
        FORECAST_COLLECTION_CONNECTION = db.getCollection("FORECAST");
        long startTime = 0;
        long startDate = 0;
        // long endDate = FIRST_SEPTEMBER_2013;
        long endDate = SIX_OCTOBER_2013;
        TimeZone tz = TimeZone.getTimeZone("Australia/Melbourne");
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        sdf.setTimeZone(tz);
        // out = new PrintWriter(LOG_FILE_NAME);
        System.out.println("Start time = " + (startTime = System.currentTimeMillis()));
        // For each day
        for (int i = 35; i < NUMBER_OF_DAYS; i++) {
            startDate = endDate;
            endDate = startDate + (startDate == SIX_OCTOBER_2013 ? OCT_6_2013 : DAY);

            correctArraysSize(startDate);

            FORECAST_VOLUME = 0L;
            FORECAST_VOLUME_SL = 0L;
            FORECAST_VOLUME_WL = 0L;
            // For each time slot in the day
            for (int j = 0; j < (startDate == SIX_OCTOBER_2013 ? NUMBER_OF_SLOTS_OCT_6_2013 : NUMBER_OF_SLOTS); j++) {
                VOLUME_CUMULATIVE[j] = j == 0 ? 0 : VOLUME_CUMULATIVE[j - 1];
                VOLUME_CUMULATIVE_WL[j] = j == 0 ? 0 : VOLUME_CUMULATIVE_WL[j - 1];
                VOLUME_CUMULATIVE_SL[j] = j == 0 ? 0 : VOLUME_CUMULATIVE_SL[j - 1];

                VOLUME_BY_TIMESLOT[j] = 0;
                VOLUME_BY_TIMESLOT_WL[j] = 0;
                VOLUME_BY_TIMESLOT_SL[j] = 0;

                PRECIPITATION[j] = 0.0;
                PRECIPITATION_WL[j] = 0.0;
                PRECIPITATION_SL[j] = 0.0;

                // System.out.println("Start time = " + (startTime = System.currentTimeMillis()));
                List<DBObject> list = getDataFromCASTTRIP(startDate, endDate, correctTimeSlot(startDate, j));
                // System.out.printf("For TIMESLOT: %d  selected: %d\n", j, list.size());

                for (DBObject value : list) {
                    int numberOfTollPoints = 0;
                    Direction startPosition = Forward.getByCode(value.get("UT_TRIP_START_TOLL_POINT_ID").toString());
                    if (startPosition != null) {
                        Direction endPosition = Forward.getByCode(value.get("UT_TRIP_END_TOLL_POINT_ID").toString());
                        numberOfTollPoints = endPosition.getPosition() - startPosition.getPosition();

                        calculateForward(startPosition, endPosition, j);

                        calculateRainfall(value, startPosition, j);
                    } else {
                        startPosition = Backward.getByCode(value.get("UT_TRIP_START_TOLL_POINT_ID").toString());
                        if (startPosition != null) {
                            Direction endPosition = Backward.getByCode(value.get("UT_TRIP_END_TOLL_POINT_ID")
                                    .toString());
                            numberOfTollPoints = endPosition.getPosition() - startPosition.getPosition();

                            calculateBackward(startPosition, endPosition, j);

                            calculateRainfall(value, startPosition, j);
                        } else {
                            System.err.println("Something is wrong with: " + value
                                    + ". Cannot get position for this value.");
                        }
                    }
                    // Increment by 1 is required
                    numberOfTollPoints++;
                    VOLUME_BY_TIMESLOT[j] += numberOfTollPoints;
                    // Calculate amount of Rain
                    PRECIPITATION[j] = value.get("PRECIPITATION").toString().isEmpty() ? 0.0 : Double.parseDouble(value
                            .get("PRECIPITATION").toString());
                }
                VOLUME_CUMULATIVE[j] = (j == 0 ? 0 : VOLUME_CUMULATIVE[j - 1]) + VOLUME_BY_TIMESLOT[j];
                VOLUME_CUMULATIVE_WL[j] = (j == 0 ? 0 : VOLUME_CUMULATIVE_WL[j - 1]) + VOLUME_BY_TIMESLOT_WL[j];
                VOLUME_CUMULATIVE_SL[j] = (j == 0 ? 0 : VOLUME_CUMULATIVE_SL[j - 1]) + VOLUME_BY_TIMESLOT_SL[j];
            }
            List<DBObject> list = getDataFromFORECAST(startDate);
            calculateForecast(list);
            System.out.println(sdf.format(new Date(startDate)));

            saveToMongoDb(startDate);

            // minPrintToFile(startDate);
            // prettyPrintToConsole(startDate);

        }
        System.out.println("Time spent for calculation = " + (System.currentTimeMillis() - startTime));
        // out.close();
        mongoClient.close();
    }

    /**
     * This function is very iportant.<br>
     * It corrects the TIMESLOT for 6 Oct 2013.<br>
     * The problem is that the 6 Oct 2013 has 92 TIMESLOTs instead of 96.<br>
     * IMPORTANT: TIMESLOTS [8-12] are missing/skipped =>
     *
     * @param startDate
     *
     * @param j
     * @return
     */
    private static int correctTimeSlot(long startDate, int j) {
        // Index to TIMESLOT mapping:
        // {"0":0,"1":1,"2":2,"3":3,"4":4,"5":5,"6":6,"7":7,"8":13,"9":14,"10":15,"11":16,"12":17,"13":18,"14":19,"15":20,"16":21,"17":22,"18":23,"19":24,"20":25,"21":26,"22":27,"23":28,"24":29,"25":30,"26":31,"27":32,"28":33,"29":34,"30":35,"31":36,"32":37,"33":38,"34":39,"35":40,"36":41,"37":42,"38":43,"39":44,"40":45,"41":46,"42":47,"43":48,"44":49,"45":50,"46":51,"47":52,"48":53,"49":54,"50":55,"51":56,"52":57,"53":58,"54":59,"55":60,"56":61,"57":62,"58":63,"59":64,"60":65,"61":66,"62":67,"63":68,"64":69,"65":70,"66":71,"67":72,"68":73,"69":74,"70":75,"71":76,"72":77,"73":78,"74":79,"75":80,"76":81,"77":82,"78":83,"79":84,"80":85,"81":86,"82":87,"83":88,"84":89,"85":90,"86":91,"87":92,"88":93,"89":94,"90":95}
        if (startDate == SIX_OCTOBER_2013 && j > 7) {
            j += 5;
        }
        return j;
    }

    private static void correctArraysSize(long startDate) {
        if (startDate == SIX_OCTOBER_2013) {
            VOLUME_CUMULATIVE = new long[NUMBER_OF_SLOTS_OCT_6_2013];
            VOLUME_CUMULATIVE_WL = new long[NUMBER_OF_SLOTS_OCT_6_2013];
            VOLUME_CUMULATIVE_SL = new long[NUMBER_OF_SLOTS_OCT_6_2013];

            VOLUME_BY_TIMESLOT = new long[NUMBER_OF_SLOTS_OCT_6_2013];
            VOLUME_BY_TIMESLOT_WL = new long[NUMBER_OF_SLOTS_OCT_6_2013];
            VOLUME_BY_TIMESLOT_SL = new long[NUMBER_OF_SLOTS_OCT_6_2013];

            PRECIPITATION = new double[NUMBER_OF_SLOTS_OCT_6_2013];
            PRECIPITATION_WL = new double[NUMBER_OF_SLOTS_OCT_6_2013];
            PRECIPITATION_SL = new double[NUMBER_OF_SLOTS_OCT_6_2013];
        } else if (VOLUME_CUMULATIVE.length != NUMBER_OF_SLOTS) {
            VOLUME_CUMULATIVE = new long[NUMBER_OF_SLOTS];
            VOLUME_CUMULATIVE_WL = new long[NUMBER_OF_SLOTS];
            VOLUME_CUMULATIVE_SL = new long[NUMBER_OF_SLOTS];

            VOLUME_BY_TIMESLOT = new long[NUMBER_OF_SLOTS];
            VOLUME_BY_TIMESLOT_WL = new long[NUMBER_OF_SLOTS];
            VOLUME_BY_TIMESLOT_SL = new long[NUMBER_OF_SLOTS];

            PRECIPITATION = new double[NUMBER_OF_SLOTS];
            PRECIPITATION_WL = new double[NUMBER_OF_SLOTS];
            PRECIPITATION_SL = new double[NUMBER_OF_SLOTS];
        }
    }

    // private static void log(String s) {
    // System.out.println(s);
    // out.println(s);
    // }

    private static void saveToMongoDb(long startDate) {
        // DBCollection coll = db.getCollection(TARGET_COLLECTION);
        // WEST
        BasicDBObject doc = new BasicDBObject("DAY", startDate).append("LINK", "Western")
                .append("VOLUME_BY_TIME_SLOT", VOLUME_BY_TIMESLOT_WL).append("VOLUME_CUMULATIVE", VOLUME_CUMULATIVE_WL)
                .append("FORECAST", FORECAST_VOLUME_WL).append("PRECIPITATION", PRECIPITATION_WL);
        TARGET_COLLECTION_CONNECTION.insert(doc);
        // SOUTH
        doc = new BasicDBObject("DAY", startDate).append("LINK", "Southern")
                .append("VOLUME_BY_TIME_SLOT", VOLUME_BY_TIMESLOT_SL).append("VOLUME_CUMULATIVE", VOLUME_CUMULATIVE_SL)
                .append("FORECAST", FORECAST_VOLUME_SL).append("PRECIPITATION", PRECIPITATION_SL);
        TARGET_COLLECTION_CONNECTION.insert(doc);
        // ALL
        doc = new BasicDBObject("DAY", startDate).append("LINK", "All")
                .append("VOLUME_BY_TIME_SLOT", VOLUME_BY_TIMESLOT).append("VOLUME_CUMULATIVE", VOLUME_CUMULATIVE)
                .append("FORECAST", FORECAST_VOLUME).append("PRECIPITATION", PRECIPITATION);

        TARGET_COLLECTION_CONNECTION.insert(doc);
    }

    // private static void minPrintToFile(long startDate) {
    // out.print(startDate);
    // out.print(",Western");
    // out.print("," + Arrays.toString(VOLUME_BY_TIMESLOT_WL));
    // out.print("," + Arrays.toString(VOLUME_CUMULATIVE_WL));
    // out.print("," + FORECAST_VOLUME_WL);
    // out.print("," + Arrays.toString(PRECIPITATION_WL));
    //
    // out.print("," + startDate);
    // out.print(",Southern");
    // out.print("," + Arrays.toString(VOLUME_BY_TIMESLOT_SL));
    // out.print("," + Arrays.toString(VOLUME_CUMULATIVE_SL));
    // out.print("," + FORECAST_VOLUME_SL);
    // out.print("," + Arrays.toString(PRECIPITATION_SL));
    //
    // out.print("," + startDate);
    // out.print(",All");
    // out.print("," + Arrays.toString(VOLUME_BY_TIMESLOT));
    // out.print("," + Arrays.toString(VOLUME_CUMULATIVE));
    // out.print("," + FORECAST_VOLUME);
    // out.println("," + Arrays.toString(PRECIPITATION));
    // }

    private static void printToConsole(long startDate) {
        System.out.println(">>>>>>  WEST   >>>>");
        System.out.println("DAY: " + startDate);
        System.out.println("LINK: Western");
        System.out.println("VOLUME_BY_TIME_SLOT: " + Arrays.toString(VOLUME_BY_TIMESLOT_WL));
        System.out.println("VOLUME_CUMULATIVE: " + Arrays.toString(VOLUME_CUMULATIVE_WL));
        System.out.println("FORECAST: " + FORECAST_VOLUME_WL);
        System.out.println("PRECIPITATION: " + Arrays.toString(PRECIPITATION_WL));

        System.out.println(">>>>>>  SOUTH  >>>>");
        System.out.println("DAY: " + startDate);
        System.out.println("LINK: Southern");
        System.out.println("VOLUME_BY_TIME_SLOT: " + Arrays.toString(VOLUME_BY_TIMESLOT_SL));
        System.out.println("VOLUME_CUMULATIVE: " + Arrays.toString(VOLUME_CUMULATIVE_SL));
        System.out.println("FORECAST: " + FORECAST_VOLUME_SL);
        System.out.println("PRECIPITATION: " + Arrays.toString(PRECIPITATION_SL));

        System.out.println(">>>>>>  ALL    >>>>");
        System.out.println("DAY: " + startDate);
        System.out.println("LINK: All");
        System.out.println("VOLUME_BY_TIME_SLOT: " + Arrays.toString(VOLUME_BY_TIMESLOT));
        System.out.println("VOLUME_CUMULATIVE: " + Arrays.toString(VOLUME_CUMULATIVE));
        System.out.println("FORECAST: " + FORECAST_VOLUME);
        System.out.println("PRECIPITATION: " + Arrays.toString(PRECIPITATION));
    }

    private static void calculateForecast(List<DBObject> list) {
        for (DBObject value : list) {
            Long forecast = (Long) value.get("FORECAST");
            Long actual = (Long) value.get("ACTUAL");
            String link = value.get("LINK").toString();
            if ("Western".equals(link)) {
                FORECAST_VOLUME_WL = forecast;
                if ((actual - VOLUME_CUMULATIVE_WL[VOLUME_CUMULATIVE_WL.length - 1]) != 0) {
                    System.err.println("Check sum verification failed! Western. EXPECTED = " + actual
                            + " CALCULATED = " + VOLUME_CUMULATIVE_WL[VOLUME_CUMULATIVE_WL.length - 1]);
                    System.err.println(value);
                }
            } else {
                FORECAST_VOLUME_SL = forecast;
                if ((actual - VOLUME_CUMULATIVE_SL[VOLUME_CUMULATIVE_SL.length - 1]) != 0) {
                    System.err.println("Check sum verification failed! Southern. EXPECTED = " + actual
                            + " CALCULATED = " + VOLUME_CUMULATIVE_SL[VOLUME_CUMULATIVE_SL.length - 1]);
                    System.err.println(value);
                }
            }
        }
        FORECAST_VOLUME = FORECAST_VOLUME_WL + FORECAST_VOLUME_SL;
    }

    private static void calculateRainfall(DBObject value, Direction startPosition, int j) {
        if (isWestLink(startPosition)) {
            PRECIPITATION_WL[j] = value.get("PRECIPITATION").toString().isEmpty() ? 0.0 : Double.parseDouble(value.get(
                    "PRECIPITATION").toString());
        } else {
            PRECIPITATION_SL[j] = value.get("PRECIPITATION").toString().isEmpty() ? 0.0 : Double.parseDouble(value.get(
                    "PRECIPITATION").toString());
        }
    }

    private static void calculateBackward(Direction startPosition, Direction endPosition, int j) {
        if ((startPosition.getPosition() < Backward.B3.getPosition())
                && (endPosition.getPosition() >= Backward.B3.getPosition())) {
            // Starts in Southern Link and Ends in Western Link
            VOLUME_BY_TIMESLOT_WL[j] += (endPosition.getPosition() - Backward.B3.getPosition() + 1);
            // No need to add 1 in the case below
            VOLUME_BY_TIMESLOT_SL[j] += (Forward.A3.getPosition() - startPosition.getPosition());
        } else if (startPosition.getPosition() <= Forward.A3.getPosition()) {
            // Starts and Ends in the Western Link
            VOLUME_BY_TIMESLOT_WL[j] += (endPosition.getPosition() - startPosition.getPosition() + 1);
        } else {
            // Starts and Ends in the Southern Link
            VOLUME_BY_TIMESLOT_SL[j] += (endPosition.getPosition() - startPosition.getPosition() + 1);
        }
    }

    private static void calculateForward(Direction startPosition, Direction endPosition, int j) {
        if ((startPosition.getPosition() <= Forward.A3.getPosition())
                && (endPosition.getPosition() > Forward.A3.getPosition())) {
            // Starts in Western Link and Ends in Southern Link
            VOLUME_BY_TIMESLOT_WL[j] += (Forward.A3.getPosition() - startPosition.getPosition() + 1);
            // No need to add 1 in the case below
            VOLUME_BY_TIMESLOT_SL[j] += (endPosition.getPosition() - Forward.A3.getPosition());

        } else if (startPosition.getPosition() <= Forward.A3.getPosition()) {
            // Starts and Ends in the Western Link
            VOLUME_BY_TIMESLOT_WL[j] += (endPosition.getPosition() - startPosition.getPosition() + 1);
        } else {
            // Starts and Ends in the Southern Link
            VOLUME_BY_TIMESLOT_SL[j] += (endPosition.getPosition() - startPosition.getPosition() + 1);
        }
    }

    private static List<DBObject> getDataFromCASTTRIP(long startDate, long endDate, int timeSlot) {
        BasicDBObject query = new BasicDBObject("TIMESLOT_DATE", new BasicDBObject("$gte", startDate).append("$lt",
                endDate)).append("TIMESLOT", new BasicDBObject("$eq", timeSlot));
        BasicDBObject selectedFields = new BasicDBObject();
        selectedFields.append("_id", 1);
        selectedFields.append("TIMESLOT_DATE", 1);
        selectedFields.append("UT_TRIP_START_TOLL_POINT_ID", 1);
        selectedFields.append("UT_TRIP_END_TOLL_POINT_ID", 1);
        selectedFields.append("PRECIPITATION", 1);

        // db.INCIDENT.find({ $and: [ {DETECTEDTIME: {$gt: 1383211730000}}, {DETECTEDTIME: {$lt: 1383211750000}}]})
        // DBCollection coll = db.getCollection("CASTTRIP");
        DBCursor cursor = CASTTRIP_COLLECTION_CONNECTION.find(query, selectedFields);

        return cursor.toArray();
    }

    private static List<DBObject> getDataFromFORECAST(long startDate) {
        BasicDBObject query = new BasicDBObject("DAY", new BasicDBObject("$eq", startDate));
        BasicDBObject selectedFields = new BasicDBObject();
        selectedFields.append("_id", 1);
        selectedFields.append("LINK", 1);
        selectedFields.append("FORECAST", 1);
        selectedFields.append("ACTUAL", 1);

        // db.INCIDENT.find({ $and: [ {DETECTEDTIME: {$gt: 1383211730000}}, {DETECTEDTIME: {$lt: 1383211750000}}]})
        // DBCollection coll = db.getCollection("FORECAST");
        DBCursor cursor = FORECAST_COLLECTION_CONNECTION.find(query, selectedFields);

        return cursor.toArray();
    }

    private static boolean isWestLink(Direction startPoint) {
        if (startPoint == Forward.A1 || startPoint == Forward.A2 || startPoint == Forward.A3
                || startPoint == Backward.B1 || startPoint == Backward.B2 || startPoint == Backward.B3) {
            return true;
        }
        return false;
    }

}
