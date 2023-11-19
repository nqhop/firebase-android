package com.example.firebasevideo;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import com.opencsv.CSVWriter;


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MainAdapter mainAdapter;

    private String filename = "SampleFile.txt";
    private String filepath = "MyFileStorage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("students"), MainModel.class)
                        .build();


        mainAdapter = new MainAdapter(options);
        recyclerView.setAdapter(mainAdapter);

        //read and write file
        if(checkPermission()){
//            Toast.makeText(MainActivity.this, "permision allowed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(MainActivity.this, "permision not allowed", Toast.LENGTH_SHORT).show();
            requestPermission();
        }
//        writeFile();


//        CSVReaderExample.myMethod(this);
//        writeCSVToExternalStorage();
    }
    private void writeCSVToExternalStorage() {
        String csvFilePath = getExternalFilesDir(null) + "/output.csv";
        Log.d("writeFile", "writeCSVToExternalStorage");
        Log.d("writeFile", csvFilePath);

        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath))) {
            String[] record1 = {"John", "Doe", "john.doe@example.com"};
            String[] record2 = {"Jane", "Smith", "jane.smith@example.com"};

            writer.writeNext(record1);
            writer.writeNext(record2);

            // You can write more records as needed

            Log.d("writeFile", "CSVWriter succefull");
            writer.flush();
        } catch (IOException e) {
            Log.d("writeFile", "CSVWriter fail");
            Log.d("writeFile", e.toString());
            e.printStackTrace();
        }
    }

    private void writeFile() {
        String filename = "myfile.txt";
        String content = "Hello, World!";

        /*
        // Get the external storage directory
        File directory = Environment.getExternalStorageDirectory();

        // Create a file object with the directory and filename
        File file = new File(directory, filename);

        // Create the file if it doesn't exist
        if (!file.exists()) {
            file.createNewFile();
        }

        // Write the content to the file
        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.flush();
        writer.close();
         */

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Log.d("writeFile", "Environment.isExternalStorageManager()" + Environment.isExternalStorageManager());
            saveFileToExternalStorage(String.valueOf(System.currentTimeMillis()),"Test");
        }

        Toast.makeText(MainActivity.this, "Write file successful", Toast.LENGTH_SHORT).show();

        // File write operation successful

        /*try {
            String content = "Separe here integers by semi-colon";
            File file = new File("fileName" +".csv");
            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "Write file fail", Toast.LENGTH_SHORT).show();
            Log.d("writeFile", e.toString());
            e.printStackTrace();
        }
        */
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private void saveFileToExternalStorage(String displayName, String content) {
        Uri externalUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);

        String relativeLocation = Environment.DIRECTORY_DOCUMENTS;

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Files.FileColumns.DISPLAY_NAME, displayName + ".csv");
        contentValues.put(MediaStore.Files.FileColumns.MIME_TYPE, "application/text");
        contentValues.put(MediaStore.Files.FileColumns.TITLE, "Test");
        contentValues.put(MediaStore.Files.FileColumns.DATE_ADDED, System.currentTimeMillis() / 1000);
        contentValues.put(MediaStore.Files.FileColumns.RELATIVE_PATH, relativeLocation);
        contentValues.put(MediaStore.Files.FileColumns.DATE_TAKEN, System.currentTimeMillis());

        Uri fileUri = getContentResolver().insert(externalUri, contentValues);
        try {
            OutputStream outputStream =  getContentResolver().openOutputStream(fileUri);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportFileCSVToExternalStorage(){

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                111);
    }

    private boolean checkPermission() {
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        mainAdapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                txtSearch(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.export){
//            CSVWriterExample.MyMethod(this);
            CSVWriterExample csvWriterExample = new CSVWriterExample(this);
            csvWriterExample.exportDataToCSV();
        }
        return super.onOptionsItemSelected(item);
    }

    private void txtSearch(String str){
        FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("students").orderByChild("name").startAt(str).endAt(str+"~"), MainModel.class)
                        .build();
        mainAdapter = new MainAdapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
    }


}