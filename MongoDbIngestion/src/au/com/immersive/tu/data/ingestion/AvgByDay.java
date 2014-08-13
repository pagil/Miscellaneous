package au.com.immersive.tu.data.ingestion;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;

import au.com.immersive.tu.data.AverageKey;
import au.com.immersive.tu.data.AverageValueObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class AvgByDay {

    private static final String LOG_FILE_NAME = "C:\\Users\\vyafimchyk.IMMERSIVE\\Desktop\\Transurban - FORECAST\\AvgByDay.log";
    private static final String PROD_SERVER = "192.168.10.114";
    public static final int NUMBER_OF_SLOTS = 96;
    private static final String CASTTRIP_COLLECTION_NAME = "CASTTRIP";
    private static DBCollection CASTTRIP_COLLECTION;
    private static final String TARGET_COLLECTION_NAME = "AVERAGE_NEW";
    private static final int AMOUNT_OF_DAYS_OF_WEEK = 13;
    private static DBCollection TARGET_COLLECTION;

    private static PrintWriter out = null;

    public static void main(String[] args) throws Exception {
        MongoClient mongoClient = new MongoClient(PROD_SERVER);
        DB db = mongoClient.getDB("TU");
        CASTTRIP_COLLECTION = db.getCollection(CASTTRIP_COLLECTION_NAME);
        TARGET_COLLECTION = db.getCollection(TARGET_COLLECTION_NAME);
        long startTime = 0;
        out = new PrintWriter(LOG_FILE_NAME);
        log("-Duser.timezone = " + System.getProperty("user.timezone"));
        log("Start time = " + (startTime = System.currentTimeMillis()));
        for (int i = 0; i < NUMBER_OF_SLOTS; i++) {

            List<DBObject> list = getDataFromCASTTRIP(i, db);
            log("List size for time slot %d : %d \n", i, list.size());
            Map<AverageKey, AverageValueObject> map = new HashMap<AverageKey, AverageValueObject>();
            for (DBObject value : list) {
                if (processDoc(value)) {

                    int timeSlot = Integer.parseInt(value.get("TIMESLOT").toString());
                    String startTollPointID = value.get("UT_TRIP_START_TOLL_POINT_ID").toString();
                    long dateTime = Long.parseLong(value.get("TIMESLOT_DATE").toString());
                    LocalDate date = new LocalDate(dateTime);
                    int dayOfWeek = date.dayOfWeek().get() - 1;

                    AverageKey key = new AverageKey(timeSlot, startTollPointID, dayOfWeek);
                    AverageValueObject vo = null;
                    if (map.containsKey(key)) {
                        vo = map.get(key);
                    } else {
                        vo = new AverageValueObject();
                        map.put(key, vo);
                    }
                    updateValueObject(vo, value);
                }
            }
            log("Map size for time slot %d : %d\n", i, map.size());
            for (Map.Entry<AverageKey, AverageValueObject> entry : map.entrySet()) {
                AverageKey key = entry.getKey();
                AverageValueObject vo = entry.getValue();
                out.println(key.toString() + vo.toString());
                persistDataToMongoDb(key, vo, db);
            }
            System.out.printf("Map size for time slot %d : %d successfully processed.\n", i, map.size());
            out.flush();
            map = null;
        }
        log("Time spent for calculation = " + (System.currentTimeMillis() - startTime));
        out.close();
    }

    public static boolean processDoc(DBObject value) {
        // @formatter:off
        return  (value.get("UT_TRIP_START_TOLL_POINT_ID") != null && !value.get("UT_TRIP_START_TOLL_POINT_ID").toString().isEmpty());
        // @formatter:on
    }

    public static boolean processSpeed(DBObject value) {
        Object obj = value.get("SPEED");
        boolean result = obj == null ? false : !obj.toString().isEmpty();
        if (!result) {
            return result;
        }
        Double doubleValue = Double.parseDouble(obj.toString());
        return (doubleValue > 0.0);
    }

    public static boolean processPrice(DBObject value) {
        Object obj = value.get("PRICE");
        boolean result = obj == null ? false : !obj.toString().isEmpty();
        if (!result) {
            return result;
        }
        Double doubleValue = Double.parseDouble(obj.toString());
        return (doubleValue > 0.0);
    }

    private static void log(String str) {
        System.out.println(str);
        out.println(str);
    }

    private static void log(String format, Object... args) {
        System.out.printf(format, args);
        out.printf(format, args);
    }

    private static void persistDataToMongoDb(AverageKey key, AverageValueObject vo, DB db) {
        // @formatter:off
        BasicDBObject doc = new BasicDBObject("TIMESLOT", key.getTimeSlot())
                .append("UT_TRIP_START_TOLL_POINT_ID", key.getTollPoint())
                .append("DAY_OF_WEEK", key.getDayOfWeek())
                .append("AVERAGE_SPEED", vo.getSpeed() / vo.getSpeedCounter())
                .append("AVERAGE_REVENUE", vo.getPrice() / AMOUNT_OF_DAYS_OF_WEEK)
                .append("TOTAL_REVENUE", vo.getPrice())
                .append("AVERAGE_VOLUME", vo.getTotalVolume() / AMOUNT_OF_DAYS_OF_WEEK)
                .append("TOTAL_VOLUME", vo.getTotalVolume())
                .append("AMOUNT_OF_DAYS_OF_WEEK", AMOUNT_OF_DAYS_OF_WEEK);
        // @formatter:on
        TARGET_COLLECTION.insert(doc);
    }

    private static void updateValueObject(AverageValueObject vo, DBObject dbObject) {
        vo.setTotalVolume(vo.getTotalVolume() + 1);
        updateSpeed(vo, dbObject);

        updatePrice(vo, dbObject);
    }

    private static void updateSpeed(AverageValueObject vo, DBObject dbObject) {
        if (processSpeed(dbObject)) {
            double speed = Double.parseDouble(dbObject.get("SPEED").toString());
            vo.setSpeed(vo.getSpeed() + speed);
            vo.setSpeedCounter(vo.getSpeedCounter() + 1);
        }
    }

    private static void updatePrice(AverageValueObject vo, DBObject dbObject) {
        if (processPrice(dbObject)) {
            double price = Double.parseDouble(dbObject.get("PRICE").toString());
            vo.setPrice(vo.getPrice() + price);
            vo.setPriceCounter(vo.getPriceCounter() + 1);
        }
    }

    private static List<DBObject> getDataFromCASTTRIP(int timeSlot, DB db) {
        BasicDBObject query = new BasicDBObject("TIMESLOT", new BasicDBObject("$eq", timeSlot));
        BasicDBObject selectedFields = new BasicDBObject();
        selectedFields.append("_id", 1);
        selectedFields.append("UT_TRIP_START_TOLL_POINT_ID", 1);
        selectedFields.append("TIMESLOT", 1);
        selectedFields.append("SPEED", 1);
        selectedFields.append("PRICE", 1);
        selectedFields.append("TIMESLOT_DATE", 1);

        DBCursor cursor = CASTTRIP_COLLECTION.find(query, selectedFields);

        return cursor.toArray();
    }

}
