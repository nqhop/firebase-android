package com.example.firebasevideo;

import android.content.Context;
import android.util.Log;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;

public class CSVReaderExample {
    public static void myMethod(Context context) {
//        String csvFilePath = "path/to/your/file.csv";
        String csvFilePath = context.getExternalCacheDir() + "/output2.csv";

        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                // Process each line of the CSV file
                for (String value : nextLine) {
                    Log.d("CSVReader", value);
                    System.out.print(value + " ");
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }
}
