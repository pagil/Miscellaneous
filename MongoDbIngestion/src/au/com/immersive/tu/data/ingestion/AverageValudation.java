package au.com.immersive.tu.data.ingestion;

import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class AverageValudation {

    private static final String PROD_SERVER = "192.168.10.114";
    private static DBCollection coll;
    private static final long[][] DAY_BEGINNING = new long[][] {
            { 1378044000000L, 1378648800000L, 1379253600000L, 1379858400000L, 1380463200000L, 1381064400000L,
                    1381669200000L, 1382274000000L, 1382878800000L, 1383483600000L, 1384088400000L, 1384693200000L,
                    1385298000000L },
            { 1378130400000L, 1378735200000L, 1379340000000L, 1379944800000L, 1380549600000L, 1381150800000L,
                    1381755600000L, 1382360400000L, 1382965200000L, 1383570000000L, 1384174800000L, 1384779600000L,
                    1385384400000L },
            { 1378216800000L, 1378821600000L, 1379426400000L, 1380031200000L, 1380636000000L, 1381237200000L,
                    1381842000000L, 1382446800000L, 1383051600000L, 1383656400000L, 1384261200000L, 1384866000000L,
                    1385470800000L },
            { 1378303200000L, 1378908000000L, 1379512800000L, 1380117600000L, 1380722400000L, 1381323600000L,
                    1381928400000L, 1382533200000L, 1383138000000L, 1383742800000L, 1384347600000L, 1384952400000L,
                    1385557200000L },
            { 1378389600000L, 1378994400000L, 1379599200000L, 1380204000000L, 1380808800000L, 1381410000000L,
                    1382014800000L, 1382619600000L, 1383224400000L, 1383829200000L, 1384434000000L, 1385038800000L,
                    1385643600000L },
            { 1378476000000L, 1379080800000L, 1379685600000L, 1380290400000L, 1380895200000L, 1381496400000L,
                    1382101200000L, 1382706000000L, 1383310800000L, 1383915600000L, 1384520400000L, 1385125200000L,
                    1385730000000L },
            { 1377957600000L, 1378562400000L, 1379167200000L, 1379772000000L, 1380376800000L, 1380981600000L,
                    1381582800000L, 1382187600000L, 1382792400000L, 1383397200000L, 1384002000000L, 1384606800000L,
                    1385211600000L } };

    public static void main(String[] args) throws Exception {
        MongoClient client = new MongoClient(PROD_SERVER);
        DB db = client.getDB("TU");
        coll = db.getCollection("CASTTRIP");
        List<DBObject> list = getDataFromCASTTRIP();
        System.out.println(list.size());
        client.close();
    }

    private static List<DBObject> getDataFromCASTTRIP() {
        BasicDBObject query = new BasicDBObject("TIMESLOT", new BasicDBObject("$eq", 48)).append("ENTITY",
                new BasicDBObject("$eq", "3A"));
        BasicDBObject selectedFields = new BasicDBObject();
        selectedFields.append("_id", 1);
        selectedFields.append("ENTITY", 1);
        selectedFields.append("TIMESLOT", 1);
        selectedFields.append("SPEED", 1);
        selectedFields.append("PRICE", 1);
        selectedFields.append("TIMESLOT_DATE", 1);

        DBCursor cursor = coll.find(query, selectedFields);

        return cursor.toArray();
    }
}
