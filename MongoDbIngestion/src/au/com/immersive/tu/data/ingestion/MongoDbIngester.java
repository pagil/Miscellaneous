package au.com.immersive.tu.data.ingestion;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class MongoDbIngester {

    public static void main(String[] args) throws Exception {

        MongoClient mongoClient = new MongoClient("192.168.10.101");
        DB db = mongoClient.getDB("TU");

        BasicDBObject doc = new BasicDBObject("FORECAST", "322,883").append("DAY", "1377957600000")
                .append("LINK", "eastern").append("ACTUAL", "").append("INCIDENT", "").append("RAINFALL", "")
                .append("RAIN_VOLUME", "");

        DBCollection coll = db.getCollection("FORECAST_VYAFIMCHYK");

        coll.insert(doc);

        System.out.println("Done!");

        // {
        // "FORECAST" : 283915,
        // "DAY" : 1377957600000,
        // "LINK" : "western",
        // "ACTUAL" : "",
        // "INCIDENT" : "",
        // "RAINFALL" : "",
        // "RAIN_VOLUME" : ""
        // }
    }
}
