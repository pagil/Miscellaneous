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
    private static final String TARGET_COLLECTION = "AVERAGE_VYAFIMCHYK";
    private static PrintWriter out = null;

    public static void main(String[] args) throws Exception {
        MongoClient mongoClient = new MongoClient(PROD_SERVER);
        DB db = mongoClient.getDB("TU");
        long startTime = 0;
        out = new PrintWriter(LOG_FILE_NAME);
        System.out.println("-Duser.timezone = " + System.getProperty("user.timezone"));
        out.println("-Duser.timezone = " + System.getProperty("user.timezone"));
        System.out.println("Start time = " + (startTime = System.currentTimeMillis()));
        out.println("Start time = " + (startTime = System.currentTimeMillis()));
        for (int i = 0; i < NUMBER_OF_SLOTS; i++) {

            // System.out.println("Start time = " + (startTime = System.currentTimeMillis()));
            List<DBObject> list = getDataFromCASTTRIP(i, db);
            System.out.printf("List size for time slot %d : %d \n", i, list.size());
            out.printf("List size for time slot %d : %d \n", i, list.size());
            Map<AverageKey, AverageValueObject> map = new HashMap<AverageKey, AverageValueObject>();
            for (DBObject value : list) {
                // System.out.println(value);
                if (value.get("ENTITY") != null && !value.get("ENTITY").toString().isEmpty()) {

                    int timeSlot = Integer.parseInt(value.get("TIMESLOT").toString());
                    String entity = value.get("ENTITY").toString();
                    long dateTime = Long.parseLong(value.get("TIMESLOT_DATE").toString());
                    LocalDate date = new LocalDate(dateTime);
                    // System.out.println(date + " | " + date.dayOfWeek().getAsText() + " | " + date.dayOfWeek().get());
                    int dayOfWeek = date.dayOfWeek().get() - 1;

                    AverageKey key = new AverageKey(timeSlot, entity, dayOfWeek);
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
            System.out.printf("Map size for time slot %d : %d\n", i, map.size());
            out.printf("Map size for time slot %d : %d\n", i, map.size());
            for (Map.Entry<AverageKey, AverageValueObject> entry : map.entrySet()) {
                AverageKey key = entry.getKey();
                AverageValueObject vo = entry.getValue();
                // System.out.println(key.toString() + vo.toString());
                out.println(key.toString() + vo.toString());
                persistDataToMongoDb(key, vo, db);
            }
            out.flush();
            map = null;
        }
        System.out.println("Time spent for calculation = " + (System.currentTimeMillis() - startTime));
        out.println("Time spent for calculation = " + (System.currentTimeMillis() - startTime));
        out.close();
    }

    private static void persistDataToMongoDb(AverageKey key, AverageValueObject vo, DB db) {
        DBCollection coll = db.getCollection(TARGET_COLLECTION);
        BasicDBObject doc = new BasicDBObject("TIMESLOT", key.getTimeSlot()).append("ENTITY", key.getTollPoint())
                .append("DAY_OF_WEEK", key.getDayOfWeek()).append("AVERAGE_SPEED", vo.getSpeed() / vo.getTotalVolume())
                .append("REVENUE", vo.getPrice()).append("VOLUME", vo.getTotalVolume());
        coll.insert(doc);
    }

    private static void updateValueObject(AverageValueObject vo, DBObject dbObject) {
        vo.setTotalVolume(vo.getTotalVolume() + 1);
        double speed = Double
                .parseDouble(dbObject.get("SPEED") == null || dbObject.get("SPEED").toString().isEmpty() ? "0.0"
                        : dbObject.get("SPEED").toString());
        vo.setSpeed(vo.getSpeed() + speed);
        double price = Double
                .parseDouble(dbObject.get("PRICE") == null || dbObject.get("PRICE").toString().isEmpty() ? "0.0"
                        : dbObject.get("PRICE").toString());
        vo.setPrice(vo.getPrice() + price);
    }

    private static List<DBObject> getDataFromCASTTRIP(int timeSlot, DB db) {
        BasicDBObject query = new BasicDBObject("TIMESLOT", new BasicDBObject("$eq", timeSlot));
        BasicDBObject selectedFields = new BasicDBObject();
        selectedFields.append("_id", 1);
        selectedFields.append("ENTITY", 1);
        selectedFields.append("TIMESLOT", 1);
        selectedFields.append("SPEED", 1);
        selectedFields.append("PRICE", 1);
        selectedFields.append("TIMESLOT_DATE", 1);

        // db.INCIDENT.find({ $and: [ {DETECTEDTIME: {$gt: 1383211730000}}, {DETECTEDTIME: {$lt: 1383211750000}}]})
        DBCollection coll = db.getCollection("CASTTRIP");
        DBCursor cursor = coll.find(query, selectedFields);

        return cursor.toArray();
    }

}
