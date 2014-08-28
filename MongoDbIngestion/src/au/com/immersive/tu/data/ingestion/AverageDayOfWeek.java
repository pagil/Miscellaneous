package au.com.immersive.tu.data.ingestion;

import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class AverageDayOfWeek {

    private static final String PROD_MONGO_SERVER = "192.168.10.114";
    private static final String TARGET_COLLECTION = "AVERAGE_BY_DAY_OF_WEEK";
    private static final String SOURCE_COLLECTION = "AVERAGE";
    private static final int NUMBER_OF_DAYS_IN_WEEK = 13;
    private static DBCollection AVERAGE_BY_DAY_OF_WEEK_COLLECTION;
    private static DBCollection CUMULATIVE_COLLECTION;

    public static void main(String[] args) throws Exception {
        MongoClient mongoClient = new MongoClient(PROD_MONGO_SERVER);
        DB db = mongoClient.getDB("TU");
        AVERAGE_BY_DAY_OF_WEEK_COLLECTION = db.getCollection(TARGET_COLLECTION);
        CUMULATIVE_COLLECTION = db.getCollection(SOURCE_COLLECTION);
        int counter = 0;
        long startTime = 0;
        System.out.println("-Duser.timezone = " + System.getProperty("user.timezone"));
        System.out.println("Start time = " + (startTime = System.currentTimeMillis()));

        List<DBObject> list = readDataFromCUMULATIVECollection();

        for (DBObject doc : list) {
            saveDocumentToTargetCollection(doc);
            counter++;
            System.out.println("Processed document number: " + counter);
        }
        System.out.println("Time spent for calculation = " + (System.currentTimeMillis() - startTime));
        mongoClient.close();
    }

    private static void saveDocumentToTargetCollection(DBObject doc) {
        Double revenue = Double.parseDouble(doc.get("REVENUE").toString());
        Double volume = Double.parseDouble(doc.get("VOLUME").toString());
        revenue /= NUMBER_OF_DAYS_IN_WEEK;
        volume /= NUMBER_OF_DAYS_IN_WEEK;

        BasicDBObject newDoc = new BasicDBObject("TIMESLOT", doc.get("TIMESLOT")).append("ENTITY", doc.get("ENTITY"))
                .append("DAY_OF_WEEK", doc.get("DAY_OF_WEEK")).append("AVERAGE_SPEED", doc.get("AVERAGE_SPEED"))
                .append("AVERAGE_REVENUE", revenue).append("AVERAGE_VOLUME", volume)
                .append("AMOUNT_OF_DAYS_OF_WEEK", NUMBER_OF_DAYS_IN_WEEK);

        AVERAGE_BY_DAY_OF_WEEK_COLLECTION.insert(newDoc);
    }

    private static List<DBObject> readDataFromCUMULATIVECollection() {
        BasicDBObject selectedFields = new BasicDBObject();
        selectedFields.append("_id", 1);
        selectedFields.append("TIMESLOT", 1);
        selectedFields.append("ENTITY", 1);
        selectedFields.append("DAY_OF_WEEK", 1);
        selectedFields.append("AVERAGE_SPEED", 1);
        selectedFields.append("REVENUE", 1);
        selectedFields.append("VOLUME", 1);

        DBCursor cursor = CUMULATIVE_COLLECTION.find(new BasicDBObject(), selectedFields);

        return cursor.toArray();
    }

}
