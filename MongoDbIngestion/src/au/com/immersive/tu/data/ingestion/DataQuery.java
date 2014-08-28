package au.com.immersive.tu.data.ingestion;

import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class DataQuery {

    public static void main(String[] args) throws Exception {
        MongoClient mongoClient = new MongoClient("192.168.10.101");
        DB db = mongoClient.getDB("TU");
        DBCollection coll = db.getCollection("FORECAST_VYAFIMCHYK");

        DBCursor cursor = coll.find();

        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }

        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<");

        BasicDBObject query = new BasicDBObject("LINK", "eastern");

        cursor = coll.find(query);

        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }

        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<");

        coll = db.getCollection("CASTTRIP_VYAFIMCHYK");
        System.out.println(coll.getCount());
        coll = db.getCollection("INCIDENT");
        System.out.println(coll.getCount());

        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        long start_time = 0;
        long end_time = 0;
        System.out.println("Start time: " + (start_time = System.nanoTime()));
        List list = coll.distinct("TOLLPOINTID");
        System.out.println("End time: " + (end_time = System.nanoTime()));
        System.out.println("Processing time: " + (end_time - start_time));
        System.out.println(list.size());
        for (Object obj : list) {
            System.out.println(obj);
        }

        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<");

        coll = db.getCollection("CASTTRIP_VYAFIMCHYK");

        query = new BasicDBObject("TIMESLOT_DATE", (new BasicDBObject("$gt", "1377957600000")).append("$lt",
                "1378043999000"));

        start_time = 0;
        end_time = 0;
        System.out.println("Start time: " + (start_time = System.nanoTime()));
        cursor = coll.find(query);
        System.out.println("End time: " + (end_time = System.nanoTime()));
        System.out.println("Processing time: " + (end_time - start_time));
        System.out.println("Start time: " + (start_time = System.nanoTime()));
        System.out.println(cursor.count());
        System.out.println("End time: " + (end_time = System.nanoTime()));
        System.out.println("Processing time: " + (end_time - start_time));
        System.out.println("Start time: " + (start_time = System.nanoTime()));
        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }
        System.out.println("End time: " + (end_time = System.nanoTime()));
        System.out.println("Processing time: " + (end_time - start_time));
        System.out.println("Done!");
    }
}
