package com;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Java7ReadFileImpl {

    public static void main(String[] args) {

        Collection<String> lines = new ArrayList<>();

        try (BufferedReader in = new BufferedReader(new FileReader("C:\\test\\test.txt"))) {
            String line = in.readLine();
            while (line != null) {
                switch (line) {
                case "test string 4":
                    lines.add("FOUR!!!");
                    break;
                default:
                    lines.add(line);
                    break;
                }
                line = in.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Read lines: ");
        for (String line : lines) {
            System.out.println(line);
        }

        Map<String, ? super Number> map = new HashMap<>();// Very cool feature
                                                          // discussed on the
                                                          // page 9 of the
                                                          // Effective Java
                                                          // Second Edition
        map.put("Integer", Integer.SIZE);
        map.put("Double", Double.SIZE);

        for (Entry<String, ? super Number> entry : map.entrySet()) {
            System.out.println(entry);
        }

        Map<String, List<String>> mapOfLists = new HashMap<>();
        List<String> values = new ArrayList<>();
        values.add("One");
        values.add("Two");
        values.add("Three");
        mapOfLists.put("Numbers", values);
        values = new ArrayList<>();
        values.add("Aa");
        values.add("Bb");
        values.add("Cc");
        values.add("Dd");
        mapOfLists.put("Alphabet", values);

        for (Entry<String, List<String>> entry : mapOfLists.entrySet()) {
            System.out.println(entry);
        }
    }
}
