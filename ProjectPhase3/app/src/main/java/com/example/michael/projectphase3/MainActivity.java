package com.example.michael.projectphase3;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*

This code was developed and created by Michael Tong for CS4301 Spring 2018.
In this java class, the MainActivity handles the contact list, where the user
can create, import, or reinitialize contacts.

For Phase 4, I have created a MapsActivity.java class to implement the
location services and maps using Google's API.


 */

public class MainActivity extends AppCompatActivity {


    ListView listView;
    EditText nameTxt;
    ArrayList<String> names = new ArrayList<String>();

    ArrayAdapter<String> adapter;
    Database myDb;

    Button button1;
    Button buttonclear1;
    Button buttonexport1;
    Button buttonimport1;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeFeature mShakeDetector;
    public int temp = 1;
    String Todaysdate = new SimpleDateFormat("MM-dd-yyyy").format(new Date());




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new Database(this);

        // Here starts the writing to a file
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1000);

        }

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeFeature(new ShakeFeature.OnShakeListener() {
            @Override
            public void onShake() {
                if (temp == 0) {
                    handleNewAnswer(temp);
                    temp = 1;
                } else {
                    handleNewAnswer(temp);
                    temp = 0;
                }
            }

        });


        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);

        button1 = (Button) findViewById(R.id.button1);
        buttonclear1 = (Button) findViewById(R.id.buttonclear);
        buttonexport1 = (Button) findViewById(R.id.buttonexport);
        buttonimport1 = (Button) findViewById(R.id.buttonimport);
        //       button2 = (Button) findViewById(R.id.Button2);
        //       button3 = (Button) findViewById(R.id.Button3);

        listView = (ListView) findViewById(R.id.listView1);
        ArrayList<String> theList = new ArrayList<>();
        Cursor data = myDb.getAllData();

        if (temp == 0)
            data = myDb.sortDESC();
        else
            data = myDb.sortASC();

        if (data.getCount() == 0) {
            Toast.makeText(MainActivity.this, "The Database is empty", Toast.LENGTH_LONG).show();
        } else {
            while (data.moveToNext()) {
                theList.add(data.getString(1) + " " + data.getString(2));
            }

            ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, theList);
            listView.setAdapter(listAdapter);
        }
//        btnviewAll = (Button)findViewById(R.id.button_viewAll);
        //      viewAll();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, AddContact.class);
                intent.putExtra("FirstName", listView.getItemAtPosition(i).toString());
                startActivity(intent);
            }
        });




        button1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UpdateContact.class);
                startActivity(intent);
            }
        });

        buttonimport1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                boolean isInserted = (myDb.insertData("0","John","Cole","L","972-577-1111","05-07-1965",
                        Todaysdate,"505 Birchwood Dr", "Garland", "TX", "75043")) &&
                        (myDb.insertData("0","Michael","Tong","B","972-577-1111","05-07-1965", Todaysdate,
                                "2400 W Prairie Creek Dr", "Richardson", "TX", "75080")) &&
                        (myDb.insertData("0","Elizabeth","Sanchez","L","972-577-1111","05-07-1965", Todaysdate,
                                        "2069 N Central Expy", "Richardson", "TX", "75080")) &&
                        (myDb.insertData("0","Paul","Nguyen","E","972-577-1111","05-07-1965", Todaysdate,
                                        "3356 W Campbell Rd", "Richardson", "TX", "75080")) &&
                        (myDb.insertData("0","Stephanie","Nunez","D","972-577-6595","05-12-1975", Todaysdate,
                                "7651 Campbell Rd", "Richardson", "TX", "75248")) &&
                        (myDb.insertData("0","Alexander","Johnson","L","972-577-1111","05-07-1965", Todaysdate,
                                "1301 W Belt Line Rd", "Richardson", "TX", "75080"));
                if(isInserted == true) {
                    Toast.makeText(MainActivity.this, "Data Imported", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                }


                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);


            }
        });


        buttonclear1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                myDb.deleteAll();
                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });



        buttonexport1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                try {
                    exportDB();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });



    }

    private void saveTextAsFile (String filename, String content) {
        String fileName = filename + ".txt";
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.close();
            Toast.makeText(this, "Saved!", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "File not found!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving!", Toast.LENGTH_LONG).show();
        }


    }


    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1000:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted!!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Permission not granted!!!!!", Toast.LENGTH_LONG).show();
                    finish();

                }
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector,mAccelerometer,SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mShakeDetector);
    }



    private void handleNewAnswer(int temp) {

        if (temp == 1) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            Toast toast = Toast.makeText(getApplicationContext(), "Contacts has been sorted from Z to A!", Toast.LENGTH_LONG);
            toast.show();
            listView = (ListView) findViewById(R.id.listView1);
            ArrayList<String> theList = new ArrayList<>();
            Cursor data = myDb.sortDESC();

            if (data.getCount() == 0) {
                Toast.makeText(MainActivity.this, "The Database is empty", Toast.LENGTH_LONG).show();
            } else {
                while (data.moveToNext()) {
                    theList.add(data.getString(1) + " " + data.getString(2));
                }

                ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, theList);
                listView.setAdapter(listAdapter);
            }
        }
        else {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            Toast toast = Toast.makeText(getApplicationContext(), "Contacts has been sorted from A to Z!", Toast.LENGTH_LONG);
            toast.show();
            listView = (ListView) findViewById(R.id.listView1);
            ArrayList<String> theList = new ArrayList<>();
            Cursor data = myDb.sortASC();

            if (data.getCount() == 0) {
                Toast.makeText(MainActivity.this, "The Database is empty", Toast.LENGTH_LONG).show();
            } else {
                while (data.moveToNext()) {
                    theList.add(data.getString(1) + " " + data.getString(2));
                }

                ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, theList);
                listView.setAdapter(listAdapter);
            }

        }}

    private void exportDB() throws FileNotFoundException {

        Cursor data = myDb.sortASC();
        String input = "";
        data.moveToFirst();

        while (data.moveToNext()) {
            input = input + data.getString(0) + " " + data.getString(1) + " " + data.getString(2) + " " + data.getString(3) + " " + data.getString(4) + " " + data.getString(5)
                    + " " + data.getString(6) + " " + data.getString(7) + " " + data.getString(8) + " " + data.getString(9) + " " + data.getString(10) + "\n";
        }

        String filename = "Database_is_stored_here";

        //   String input = "John Cole Y 972-456-1232 04-03-1965 30-03-2018" + "\n";
        // do whatever you need with current line
        if (!filename.equals("") && !input.equals("")) {
            saveTextAsFile(filename,input);
        }

    }



}