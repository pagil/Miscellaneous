package au.com.immersive.tu.data.ingestion.test;

import org.junit.Assert;
import org.junit.Test;

import au.com.immersive.tu.data.ingestion.AvgByDay;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class ProcessDocTest {

    @Test
    public void testNullEntity() {
        DBObject object = new BasicDBObject("UT_TRIP_START_TOLL_POINT_ID", null);
        Assert.assertFalse(AvgByDay.processDoc(object));
    }

    @Test
    public void testEmptyEntity() {
        DBObject object = new BasicDBObject("UT_TRIP_START_TOLL_POINT_ID", "");
        Assert.assertFalse(AvgByDay.processDoc(object));
    }

    @Test
    public void testNullSpeed1() {
        DBObject object = new BasicDBObject("UT_TRIP_START_TOLL_POINT_ID", "3A");
        Assert.assertFalse(AvgByDay.processDoc(object));
    }

    @Test
    public void testNullSpeed2() {
        DBObject object = new BasicDBObject("UT_TRIP_START_TOLL_POINT_ID", "3A");
        object.put("SPEED", null);
        Assert.assertFalse(AvgByDay.processDoc(object));
    }

    @Test
    public void testEmptySpeed() {
        DBObject object = new BasicDBObject("UT_TRIP_START_TOLL_POINT_ID", "3A");
        object.put("SPEED", "");
        Assert.assertFalse(AvgByDay.processDoc(object));
    }

    @Test
    public void testNaNSpeed() {
        DBObject object = new BasicDBObject("UT_TRIP_START_TOLL_POINT_ID", "3A");
        object.put("SPEED", "NaN");
        Assert.assertFalse(AvgByDay.processDoc(object));
    }

    @Test
    public void testSuccessCase() {
        DBObject object = new BasicDBObject("UT_TRIP_START_TOLL_POINT_ID", "3A");
        object.put("SPEED", "34.57");
        Assert.assertTrue(AvgByDay.processDoc(object));
    }

}
