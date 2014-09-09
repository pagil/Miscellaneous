package au.com.immersive.xorg.encryption;

import java.io.FileReader;
import java.io.FileWriter;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class CsvEncryptor {
	/* @formatter:off */
	// Total 87 columns
	private static final int[] FIELDS_FOR_ENCRYPTION = new int[] { 2, 3, 5, 7, 49, 51, 53, 55, 57, 60, 62, 64, 66, 68, 70, 72, 74, 76 };
	private static final int[] FIELDS_FOR_POSTFIX = new int[] { 1, 45 };
    public static final String CSV_FILE_IN = "C:\\Documents and Settings\\Zshah\\Desktop\\Issues\\DEMO\\export-1-input.csv";
    public static final String CSV_FILE_OUT = "C:\\Documents and Settings\\Zshah\\Desktop\\Issues\\DEMO\\export-1-output.csv";
    public static final char SEPARATOR = ';';
    public static final String POSTFIX = "1";
	/* @formatter:on */

	public static void main(String[] args) throws Exception {
		CSVReader reader = new CSVReader(new FileReader(CSV_FILE_IN), SEPARATOR);
		CSVWriter writer = new CSVWriter(new FileWriter(CSV_FILE_OUT), SEPARATOR);
		String[] nextLine;
		int i = 0;
		while ((nextLine = reader.readNext()) != null) {
			encryptFields(nextLine);
			postfixFields(nextLine);
			writer.writeNext(nextLine);
			i++;
			System.out.printf("Line %4d processed succesfully.\n", i);
		}
		writer.close();
		System.out.println("Done!");
	}

	private static void postfixFields(String[] nextLine) {
		for (int i = 0; i < FIELDS_FOR_POSTFIX.length; i++) {
			nextLine[FIELDS_FOR_POSTFIX[i]] += POSTFIX;
		}
	}

	private static void encryptFields(String[] nextLine) {
		for (int i = 0; i < FIELDS_FOR_ENCRYPTION.length; i++) {
			nextLine[FIELDS_FOR_ENCRYPTION[i]] = CaesarImpl.encrypt(nextLine[FIELDS_FOR_ENCRYPTION[i]]);
		}
	}
}
