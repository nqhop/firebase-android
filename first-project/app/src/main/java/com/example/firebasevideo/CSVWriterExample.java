package com.example.firebasevideo;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opencsv.CSVWriter;

import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CSVWriterExample {
    private DatabaseReference databaseReference;
    private Context context;

    public CSVWriterExample(Context context) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        this.context = context;
    }

//    public static void MyMethod(Context context) {
//        String csvFilePath = context.getExternalCacheDir() + "/output2.csv";
//
//        Log.d("writeFile", String.valueOf(context.getExternalCacheDir()));
//        Log.d("writeFile", String.valueOf(context.getExternalCacheDir().getAbsolutePath()));
//        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath))) {
//            String[] record1 = {"John", "Doe", "john.doe@example.com"};
//            String[] record2 = {"Jane", "Smith", "jane.smith@example.com"};
//
//            writer.writeNext(record1);
//            writer.writeNext(record2);
//
//            // You can write more records as needed
//
//            writer.flush();
//            Log.d("writeFile", "CSVWriter succefull");
//        } catch (IOException e) {
//            Log.d("writeFile", "CSVWriter fail");
//            Log.d("writeFile", e.toString());
//            e.printStackTrace();
//        }
//    }

    public void exportDataToCSV() {
        String csvFilePath = context.getExternalFilesDir(null) + "/exportDataToCSV2.csv";
        String csvFilePath2 = context.getExternalFilesDir(null) + "/t5.csv";

        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath))) {

            String[] headerRecord = { "id", "code", "name" };
            writer.writeNext(headerRecord);

            writer.writeNext(new String[] { "1", "US", "United States" });
            writer.writeNext(new String[] { "2", "VN", "Viet Nam" });
            writer.writeNext(new String[] { "3", "AU", "Australia" });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath2))) {
            // Read data from Firebase Database
            databaseReference.child("students").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Process the retrieved data

                    // Retrieve the data from the first child
                    DataSnapshot firstChildSnapshot = dataSnapshot.getChildren().iterator().next();
                    List<String> headerRecord = (List<String>) getKeyAndValue(firstChildSnapshot.getValue().toString()).get("key");
                    String[] headerRecordArr = headerRecord.toArray(new String[0]);

                    Log.d("test", Arrays.toString(headerRecordArr));


                    writer.writeNext(headerRecordArr);
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        List<String> row = (List<String>) getKeyAndValue(firstChildSnapshot.getValue().toString()).get("value");

                        Log.d("exportDataToCSV", "childSnapshot.getKey() " + childSnapshot.getKey());
                        Log.d("exportDataToCSV", "childSnapshot.getValue() " + childSnapshot.getValue());
                        // Get the data values
                        String value1 = childSnapshot.child("value1").getValue(String.class);
                        String value2 = childSnapshot.child("value2").getValue(String.class);

                        // Create a CSV record
                        String[] record = {childSnapshot.getKey().toString(), childSnapshot.getValue().toString()};

                        // Write the record to the CSV file
                        writer.writeNext(row.toArray(new String[0]));
                    }
                    Log.d("exportDataToCSV", "exportDataToCSV successful");

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle the error
                }
            });
        } catch (IOException e) {
            Log.d("exportDataToCSV","IOException 2 " + e.toString());
            e.printStackTrace();
        }
    }
    private Map getKeyAndValue(Object object){
        String keyAndValue = object.toString();
        keyAndValue = keyAndValue.substring(1, keyAndValue.length() - 1);
//        Log.d("getKeyAndValue", keyAndValue);

        List<String> key = new ArrayList<>();
        List<String> value = new ArrayList<>();
                ;
        for (String item : keyAndValue.split(", ")) {
//            Log.d("getKeyAndValue", item);
            key.add(item.substring(0, item.indexOf("=")));
            value.add(item.substring(item.indexOf("=") + 1, item.length()));
//            Log.d("getKeyAndValue", item.substring(0, item.indexOf("=")));
//            Log.d("getKeyAndValue", item.substring(item.indexOf("=") + 1, item.length()));
        }

        Map<String, Object> multiValues = new HashMap<String, Object>();
        multiValues.put("key", key);
        multiValues.put("value", value);
        return multiValues;
    }
}
