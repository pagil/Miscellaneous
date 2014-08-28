package au.com.immersive.tu.data.ingestion;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class MongoDbQueryTest {

    public static void main(String[] args) throws Exception {
        MongoClient mongoClient = new MongoClient("192.168.10.101");
        DB db = mongoClient.getDB("TU");
        DBCollection coll = db.getCollection("INCIDENT");

        BasicDBObject query = new BasicDBObject("DETECTEDTIME", new BasicDBObject("$gt", 1383211730000L).append("$lt",
                1383211750000L));
        // db.INCIDENT.find({ $and: [ {DETECTEDTIME: {$gt: 1383211730000}}, {DETECTEDTIME: {$lt: 1383211750000}}]})

        DBCursor cursor = coll.find(query);

        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }

        System.out.println("Done!");
    }

}
