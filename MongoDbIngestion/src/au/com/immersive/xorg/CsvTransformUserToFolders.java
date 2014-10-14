package au.com.immersive.xorg;

import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import com.mongodb.DBObject;

import au.com.bytecode.opencsv.CSVReader;

public class CsvTransformUserToFolders {

    public static final String CSV_FILE = "c:\\Documents and Settings\\Zshah\\Desktop\\Issues\\UserToFolderQuery1.csv";
    public static final TimeZone tz = TimeZone.getTimeZone("Australia/Melbourne");
    public static final SimpleDateFormat sdf = new SimpleDateFormat("MMMMM dd yyyy");

    static {
        sdf.setTimeZone(tz);
    }

    public static void main(String[] args) throws Exception {

        CSVReader reader = new CSVReader(new FileReader(CSV_FILE));
        String[] nextLine;
        ListMultimap<String, String> map = ArrayListMultimap.create();
        while ((nextLine = reader.readNext()) != null) {
        	map.put(nextLine[0], nextLine[1]);
        }
        for (String key: map.keySet()) {
        	System.out.print(key+",\"");
        	List<String> list = map.get(key);
        	for (int i = 0; i < list.size(); i++){
        		System.out.print((i==0 ? "": ", ") + list.get(i));
        	}
        	System.out.println();
        }
        //System.out.println(map);
        System.out.println("Done!");
    }
}
