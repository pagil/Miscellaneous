package au.com.immersive.tu.data.ingestion;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class JSONSimpleTest {

    public static final String TEST_SERVER = "192.168.10.101";
    public static final String PROD_SERVER = "192.168.10.114";
    public static final String QUERY_RESULT_FILE = "C:\\Users\\vyafimchyk.IMMERSIVE\\Desktop\\Transurban - FORECAST\\QueryResultModofied01.js";
    public static final String QUERY_RESULT_FILE_SMALL = "C:\\Users\\vyafimchyk.IMMERSIVE\\Desktop\\Transurban - FORECAST\\QueryResultModofiedSmall01.txt";
    public static final String FORECAST_PASSAGES_FILE = "C:\\Users\\vyafimchyk.IMMERSIVE\\Desktop\\Transurban - FORECAST\\forecast passages.js";
    public static final String INCIDENT_QUERY_RESULT_FILE = "C:\\Users\\vyafimchyk.IMMERSIVE\\Desktop\\Transurban - FORECAST\\INCIDENT_QUERY.js";

    public static final String CASTTRIP_VOLUME_AGGREGATE_FILE = "C:\\Users\\vyafimchyk.IMMERSIVE\\Desktop\\Transurban - FORECAST\\volume_aggregate.js";
    public static final String INCIDENT_AGGREGATE_FILE = "C:\\Users\\vyafimchyk.IMMERSIVE\\Desktop\\Transurban - FORECAST\\incident_aggregate.js";

    public static final TimeZone tz = TimeZone.getTimeZone("Australia/Melbourne");
    public static final SimpleDateFormat sdf = new SimpleDateFormat("MMMMM dd yyyy");

    static {
        sdf.setTimeZone(tz);
    }

    public static void main(String[] args) throws Exception {

        List<JSONObject> queryResultList = readJsonFile(CASTTRIP_VOLUME_AGGREGATE_FILE);
        Map<Long, JSONObject> forecastPassagesMap = readJsonFileToMap(FORECAST_PASSAGES_FILE);
        List<JSONObject> incidentQueryList = readJsonFile(INCIDENT_AGGREGATE_FILE);

        MongoClient mongoClient = new MongoClient(PROD_SERVER);
        DB db = mongoClient.getDB("TU");
        DBCollection coll = db.getCollection("FORECAST_VYAFIMCHYK");

        int counter = 0;
        for (JSONObject queryResultJSON : queryResultList) {

            counter++;
            if (queryResultJSON.get("DAY") instanceof Long) {
                BasicDBObject doc = new BasicDBObject("DAY", queryResultJSON.get("DAY"))
                        .append("LINK", queryResultJSON.get("LINK"))
                        .append("FORECAST",
                                Long.parseLong(getForecast(queryResultJSON, forecastPassagesMap).toString()))
                        .append("ACTUAL", Long.parseLong(queryResultJSON.get("ACTUAL").toString()))
                        .append("INCIDENT", getIncident(queryResultJSON, incidentQueryList))
                        .append("RAINFALL", "0".equals(queryResultJSON.get("RAINFALL")) ? "false" : "true")
                        .append("RAIN_VOLUME", Double.parseDouble(queryResultJSON.get("RAINFALL").toString()));

                coll.insert(doc);

                System.out.printf("Processed %d object %s succesfully \n", counter, doc.toString());
            } else {
                System.err.println("Wrong DAY type. This queryResultJSON is skipped: " + queryResultJSON);
            }
        }
        System.out.println("Done!");
    }

    // System.out.println("RAINFALL = " + jsonObj.get("RAINFALL"));
    // long time = Long.parseLong(jsonObj.get("DAY").toString());
    //
    // System.out.println(sdf.format(new Date(time)));
    // System.out.println(jsonObj);

    private static Object getIncident(JSONObject queryResultJSON, List<JSONObject> incidentQueryList) {
        Long result = 0L;
        Long day = (Long) queryResultJSON.get("DAY");
        String link = queryResultJSON.get("LINK").toString();
        int found = 0;
        for (JSONObject incident : incidentQueryList) {
            if (day.equals(incident.get("DAY")) && link.equals(incident.get("DAY"))) {
                found++;
                result = (Long) incident.get("ACTUAL");
                System.out.printf("getIncident: found %d equal value\n", found);
            }
        }
        System.out.printf("getIncident: returned %d value\n", found);
        return result;
    }

    private static Map<Long, JSONObject> readJsonFileToMap(String fileName) throws IOException, ParseException,
            FileNotFoundException {
        Map<Long, JSONObject> map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String jsonStr = br.readLine();
            while (jsonStr != null) {
                System.out.println("Read String: " + jsonStr);
                JSONObject jsonForecastObject = (JSONObject) new JSONParser().parse(jsonStr);
                map.put((Long) jsonForecastObject.get("DAY"), jsonForecastObject);
                jsonStr = br.readLine();
            }
        }
        System.out.printf("Read file: %s completed.\n", fileName);
        return map;
    }

    private static Object getForecast(JSONObject queryResultJSON, Map<Long, JSONObject> forecastPassages) {

        JSONObject forecastJSON = forecastPassages.get(queryResultJSON.get("DAY"));

        Object forecastValue = forecastJSON.get(queryResultJSON.get("LINK"));

        return forecastValue == null ? "0" : forecastValue.toString();
    }

    private static List<JSONObject> readJsonFile(String fileName) throws IOException, ParseException,
            FileNotFoundException {
        List<JSONObject> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String jsonStr = br.readLine();
            while (jsonStr != null) {
                System.out.println("Read String: " + jsonStr);
                JSONObject jsonForecastObject = (JSONObject) new JSONParser().parse(jsonStr);
                list.add(jsonForecastObject);
                jsonStr = br.readLine();
            }
        }
        System.out.printf("Read file: %s completed.\n", fileName);
        return list;
    }

}
