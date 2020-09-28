/*
 * Edited by Ryan Lyons
 */
package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*; //dont need to import simple.parser since it is included under simple.*

public class Converter {
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        String results = "";
        try {
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();
            String[] colHeaders = iterator.next();
            JSONArray changeRecord = new JSONArray();
            LinkedHashMap<String, JSONArray> jsonData = new LinkedHashMap<>();
            String[] record;
            String[] rowHeaders = new String[full.size() - 1];
            int[][] data = new int[full.size() - 1][colHeaders.length - 1];
            int counter = 0;
            int counter2= 0;
            while (iterator.hasNext()){
                record= iterator.next();
                for (int i = 0; i <colHeaders.length; i++){
                    if(i == 0){
                        rowHeaders[counter] = record[i];
                        counter++;
                    }
                    else{
                        data[counter2][i - 1] = Integer.parseInt(record[i]);
                    }
                }
                counter2++;
            }
            counter = 0;
            for(String i : rowHeaders){
                changeRecord.add(counter, i);
                counter++;
            }
            jsonData.put("rowHeaders", changeRecord);
            changeRecord = new JSONArray();
            JSONArray changeRecord2 = new JSONArray();
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length; j++) {
                    changeRecord2.add(j, data[i][j]);
                }
                changeRecord.add(i, changeRecord2);
                changeRecord2 = new JSONArray();
            }
            jsonData.put("data", changeRecord);
            changeRecord = new JSONArray();
            counter = 0;
            for (String i : colHeaders) {
                changeRecord.add(counter, i);
                counter++;
            }
            jsonData.put("colHeaders", changeRecord);
            results = JSONValue.toJSONString(jsonData);
        }        
        catch(Exception e) { return e.toString(); }
        return results.trim();
    }
    
    public static String jsonToCsv(String jsonString) {
        String results = "";
        try {
            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\n');
            String[] sections = jsonString.split("],\"");
            String[] rowHeaders = sections[0].split(":\\[")[1].replaceAll("\"", "").split(",");
            String[] colHeaders = sections[2].split(":")[1].replaceAll("\"", "").replace("[", "").replace("]", "").replace("}", "").split(",");
            String[] beforeData = sections[1].split(":\\[")[1].split("],\\[");
            String[][] data = new String[beforeData.length][colHeaders.length];
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < colHeaders.length; j++) {
                    if (j == 0) {
                        data[i][j] = rowHeaders[i];
                    } 
                    else {
                        data[i][j] = beforeData[i].split(",")[j - 1].replace("[", "").replace("]", "");
                    }
                }
            }    
            csvWriter.writeNext(colHeaders);
            for (String[] i : data) {
                csvWriter.writeNext(i);
            }
            results = writer.toString();
        } catch(Exception e) { return e.toString(); }
        return results.trim();
    }
}