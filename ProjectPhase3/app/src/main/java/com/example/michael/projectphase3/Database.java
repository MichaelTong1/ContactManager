package com.example.michael.projectphase3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Contact.db";
    public static final String TABLE_NAME = "contact_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "FIRSTNAME";
    public static final String COL_3 = "LASTNAME";
    public static final String COL_4 = "MIDDLEINITIAL";
    public static final String COL_5 = "PHONENUMBER";
    public static final String COL_6 = "BIRTHDATE";
    public static final String COL_7 = "DATEOFFIRSTCONTACT";
    public static final String COL_8 = "ADDRESSLINE";
    public static final String COL_9 = "CITY";
    public static final String COL_10 = "STATE";
    public static final String COL_11 = "ZIPCODE";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER,FIRSTNAME TEXT PRIMARY KEY,LASTNAME TEXT," +
                "MIDDLEINITIAL TEXT,PHONENUMBER TEXT,BIRTHDATE TEXT, DATEOFFIRSTCONTACT TEXT, " +
                "ADDRESSLINE TEXT, CITY TEXT, STATE TEXT, ZIPCODE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String id, String first, String last, String middle, String phone, String birth, String date, String address, String city, String state, String zip) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, first);
        contentValues.put(COL_3, last);
        contentValues.put(COL_4, middle);
        contentValues.put(COL_5, phone);
        contentValues.put(COL_6, birth);
        contentValues.put(COL_7, date);
        contentValues.put(COL_8, address);
        contentValues.put(COL_9, city);
        contentValues.put(COL_10,state);
        contentValues.put(COL_11, zip);


        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public boolean updateData(String id, String first, String last, String middle, String phone, String birth, String date, String address, String city, String state, String zip) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, first);
        contentValues.put(COL_3, last);
        contentValues.put(COL_4, middle);
        contentValues.put(COL_5, phone);
        contentValues.put(COL_6, birth);
        contentValues.put(COL_7, date);
        contentValues.put(COL_8, address);
        contentValues.put(COL_9, city);
        contentValues.put(COL_10, state);
        contentValues.put(COL_11, zip);


        db.update(TABLE_NAME, contentValues, "FIRSTNAME = ?", new String[]{first});
        return true;
    }

    public Integer deleteData(String first) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "FIRSTNAME = ?", new String[]{first});
    }

    public Cursor getFirstData(String temp) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM contact_table WHERE FirstName ='" + temp + "'", null);
        return res;
    }

    public Cursor sortASC() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.query(TABLE_NAME, null, null, null, null, null, COL_2 + " ASC", null);
        return res;
    }

    public Cursor sortDESC() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.query(TABLE_NAME, null, null, null, null, null, COL_2 + " DESC", null);
        return res;
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);

    }

    public boolean createExample(String id, String first, String last, String middle, String phone, String birth, String address, String city, String state, String zip) {
        String date = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, first);
        contentValues.put(COL_3, last);
        contentValues.put(COL_4, middle);
        contentValues.put(COL_5, phone);
        contentValues.put(COL_6, birth);
        contentValues.put(COL_7, date);
        contentValues.put(COL_8, address);
        contentValues.put(COL_9, city);
        contentValues.put(COL_10, state);
        contentValues.put(COL_11, zip);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;

    }


/*
    public void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DATABASE_NAME);
        String outFileName = (Environment.getExternalStorageDirectory().getAbsolutePath) + DATABASE_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[10];
        int length;
        while ((length = myInput.read(buffer)) > 0 ) {

        }
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }
*/

}