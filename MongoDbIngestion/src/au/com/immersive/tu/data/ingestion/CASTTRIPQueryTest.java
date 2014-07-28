package au.com.immersive.tu.data.ingestion;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import au.com.immersive.tu.data.enums.Backward;
import au.com.immersive.tu.data.enums.Direction;
import au.com.immersive.tu.data.enums.Forward;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class CASTTRIPQueryTest {

    public static final long FIRST_SEPTEMBER_2013 = 1377957600000L;
    public static final long DAY = 86400000L;
    public static final long SIX_OCTOBER_2013 = 1380981600000L;
    public static final long OCT_6_2013 = 82800000L;
    public static final int NUMBER_OF_DAYS = 91;
    private static long ACTUAL = 0;
    private static long WEST_LINK = 0;
    private static long SOUTH_LINK = 0;
    private static double RAIN_FALL = 0.0;
    private static int INCIDENTS_WEST_LINK = 0;
    private static int INCIDENTS_SOUTH_LINK = 0;
    private static int RAINFALL_WEST_LINK = 0;
    private static int RAINFALL_SOUTH_LINK = 0;

    public static void main(String[] args) throws Exception {
        MongoClient mongoClient = new MongoClient("192.168.10.114");
        DB db = mongoClient.getDB("TU");
        long startTime = 0;
        long startDate = 0;
        long endDate = FIRST_SEPTEMBER_2013;
        TimeZone tz = TimeZone.getTimeZone("Australia/Melbourne");
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        sdf.setTimeZone(tz);
        System.out.println("Start time = " + (startTime = System.currentTimeMillis()));
        for (int i = 0; i < NUMBER_OF_DAYS; i++) {
            startDate = endDate;
            endDate = startDate + (startDate == SIX_OCTOBER_2013 ? OCT_6_2013 : DAY);

            ACTUAL = 0;
            WEST_LINK = 0;
            SOUTH_LINK = 0;
            RAIN_FALL = 0;
            RAINFALL_SOUTH_LINK = 0;
            RAINFALL_WEST_LINK = 0;
            INCIDENTS_SOUTH_LINK = 0;
            INCIDENTS_WEST_LINK = 0;

            ListMultimap<String, DBObject> map = ArrayListMultimap.create();

            // System.out.println("Start time = " + (startTime = System.currentTimeMillis()));
            List<DBObject> list = getDataFromCASTTRIP(startDate, endDate, db);
            // System.out.println(list.get(0));

            for (DBObject value : list) {
                map.put(value.get("_id").toString(), value);
                int numberOfTollPoints = 0;
                Direction startPosition = Forward.getByCode(value.get("UT_TRIP_START_TOLL_POINT_ID").toString());
                if (startPosition != null) {
                    Direction endPosition = Forward.getByCode(value.get("UT_TRIP_END_TOLL_POINT_ID").toString());
                    numberOfTollPoints = endPosition.getPosition() - startPosition.getPosition();

                    calculateForward(startPosition, endPosition);

                    calculateRainfall(value, startPosition);
                } else {
                    startPosition = Backward.getByCode(value.get("UT_TRIP_START_TOLL_POINT_ID").toString());
                    if (startPosition != null) {
                        Direction endPosition = Backward.getByCode(value.get("UT_TRIP_END_TOLL_POINT_ID").toString());
                        numberOfTollPoints = endPosition.getPosition() - startPosition.getPosition();

                        calculateBackward(startPosition, endPosition);

                        calculateRainfall(value, startPosition);
                    } else {
                        System.err.println("Something is wrong with: " + value
                                + ". Cannot get position for this value.");
                    }
                }
                // Increment by 1 is required
                numberOfTollPoints++;
                ACTUAL += numberOfTollPoints;
                // Calculate amount of Rain
                RAIN_FALL += value.get("PRECIPITATION").toString().isEmpty() ? 0.0 : Double.parseDouble(value.get(
                        "PRECIPITATION").toString());
            }
            // System.out.println("Time spent for calculation = " + (System.currentTimeMillis() - startTime));
            // System.out.println("ACTUAL (including W + S + Empty) = " + ACTUAL);
            // System.out.println("Total number of documents loaded = " + list.size());
            // System.out.println("Number of uniq _id = " + map.keySet().size());
            // System.out.println("Done!");
            System.out.print(sdf.format(new Date(startDate)));
            System.out.printf(",%d,%d,%d,%d,%d,%d,%d,%s,%f", startDate, endDate, ACTUAL, WEST_LINK, SOUTH_LINK,
                    (RAINFALL_WEST_LINK > 0.0 ? 1 : 0), (RAINFALL_SOUTH_LINK > 0.0 ? 1 : 0), (RAIN_FALL == 0.0 ? "Dry"
                            : "Rainig"), RAIN_FALL);

            list = getDataFromINCIDENT(startDate, endDate, db);
            for (DBObject value : list) {
                Direction startPosition = Forward.getByCode(value.get("TOLLPOINTID").toString());
                if (startPosition != null) {
                    calculateIncidents(startPosition);
                } else {
                    startPosition = Backward.getByCode(value.get("TOLLPOINTID").toString());
                    if (startPosition != null) {
                        calculateIncidents(startPosition);
                    } else {
                        System.err.println("Something is wrong with the value: " + value);
                    }
                }
            }
            System.out.printf(",%d,%d,%d\n", list.size(), INCIDENTS_WEST_LINK, INCIDENTS_SOUTH_LINK);
        }
        System.out.println("Time spent for calculation = " + (System.currentTimeMillis() - startTime));
        mongoClient.close();
    }

    private static void calculateIncidents(Direction startPosition) {
        if (isWestLink(startPosition)) {
            INCIDENTS_WEST_LINK++;
        } else {
            INCIDENTS_SOUTH_LINK++;
        }
    }

    private static void calculateRainfall(DBObject value, Direction startPosition) {
        if (isWestLink(startPosition)) {
            RAINFALL_WEST_LINK += value.get("PRECIPITATION").toString().isEmpty() ? 0.0 : Double.parseDouble(value.get(
                    "PRECIPITATION").toString());
        } else {
            RAINFALL_SOUTH_LINK += value.get("PRECIPITATION").toString().isEmpty() ? 0.0 : Double.parseDouble(value
                    .get("PRECIPITATION").toString());
        }
    }

    private static void calculateBackward(Direction startPosition, Direction endPosition) {
        if ((startPosition.getPosition() < Backward.B3.getPosition())
                && (endPosition.getPosition() >= Backward.B3.getPosition())) {
            // Starts in Southern Link and Ends in Western Link
            WEST_LINK += (endPosition.getPosition() - Backward.B3.getPosition() + 1);
            // No need to add 1 in the case below
            SOUTH_LINK += (Forward.A3.getPosition() - startPosition.getPosition());
        } else if (startPosition.getPosition() <= Forward.A3.getPosition()) {
            // Starts and Ends in the Western Link
            WEST_LINK += (endPosition.getPosition() - startPosition.getPosition() + 1);
        } else {
            // Starts and Ends in the Southern Link
            SOUTH_LINK += (endPosition.getPosition() - startPosition.getPosition() + 1);
        }
    }

    private static void calculateForward(Direction startPosition, Direction endPosition) {
        if ((startPosition.getPosition() <= Forward.A3.getPosition())
                && (endPosition.getPosition() > Forward.A3.getPosition())) {
            // Starts in Western Link and Ends in Southern Link
            WEST_LINK += (Forward.A3.getPosition() - startPosition.getPosition() + 1);
            // No need to add 1 in the case below
            SOUTH_LINK += (endPosition.getPosition() - Forward.A3.getPosition());
        } else if (startPosition.getPosition() <= Forward.A3.getPosition()) {
            // Starts and Ends in the Western Link
            WEST_LINK += (endPosition.getPosition() - startPosition.getPosition() + 1);
        } else {
            // Starts and Ends in the Southern Link
            SOUTH_LINK += (endPosition.getPosition() - startPosition.getPosition() + 1);
        }
    }

    private static List<DBObject> getDataFromINCIDENT(long startDate, long endDate, DB db) {
        BasicDBObject query = new BasicDBObject("DETECTEDTIME", new BasicDBObject("$gte", startDate).append("$lt",
                endDate)).append("TOLLPOINTID", new BasicDBObject("$ne", ""));
        BasicDBObject selectedFields = new BasicDBObject();
        selectedFields.append("_id", 1);
        selectedFields.append("DETECTEDTIME", 1);
        selectedFields.append("TOLLPOINTID", 1);

        // db.INCIDENT.find({ $and: [ {DETECTEDTIME: {$gt: 1383211730000}}, {DETECTEDTIME: {$lt: 1383211750000}}]})
        DBCollection coll = db.getCollection("INCIDENT");
        DBCursor cursor = coll.find(query, selectedFields);

        return cursor.toArray();
    }

    private static List<DBObject> getDataFromCASTTRIP(long startDate, long endDate, DB db) {
        BasicDBObject query = new BasicDBObject("TIMESLOT_DATE", new BasicDBObject("$gte", startDate).append("$lt",
                endDate));
        BasicDBObject selectedFields = new BasicDBObject();
        selectedFields.append("_id", 1);
        selectedFields.append("TIMESLOT_DATE", 1);
        selectedFields.append("UT_TRIP_START_TOLL_POINT_ID", 1);
        selectedFields.append("UT_TRIP_END_TOLL_POINT_ID", 1);
        selectedFields.append("PRECIPITATION", 1);

        // db.INCIDENT.find({ $and: [ {DETECTEDTIME: {$gt: 1383211730000}}, {DETECTEDTIME: {$lt: 1383211750000}}]})
        DBCollection coll = db.getCollection("CASTTRIP");
        DBCursor cursor = coll.find(query, selectedFields);

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
