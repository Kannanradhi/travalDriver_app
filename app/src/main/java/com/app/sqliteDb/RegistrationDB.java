package com.app.sqliteDb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cabily.cabilydriver.Pojo.DriverDocumentPojo;

import java.util.ArrayList;

/**
 * Created by user144 on 5/21/2018.
 */

public class RegistrationDB extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "document";
    private static final int DATABASE_VERSION = 1;


    private static final String TABLE_DOCUMENT = "document";
    private static final String TABLE_VEHICLEHIRED_DOCUMENT = "vehiclehireddocument";


    // User Table Columns
    private static final String KEY__ID = "id";
    private static final String KEY_VALUES = "name";
    private static final String KEY_DOCUM_ID = "docid";

    private static final String DOCUMENT_STATUS = "document_status";
    private static final String DOCUMENT_PATH = "document_path";
    private static final String DOCUMENT_AVAIL = "document_avail";

    public RegistrationDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // SQLiteDatabase db=this.getReadableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CHAT_TABLE = "CREATE TABLE "
                + TABLE_DOCUMENT +
                "(" +
                KEY__ID + " INTEGER PRIMARY KEY,"
                + KEY_DOCUM_ID + " VARCHAR," +
                KEY_VALUES + " TEXT," +
                DOCUMENT_PATH + " TEXT," +
                DOCUMENT_STATUS + " TEXT," +
                DOCUMENT_AVAIL + " TEXT" + ")";


        String CREATE_VEHICLEHIRED_TABLE = "CREATE TABLE "
                + TABLE_VEHICLEHIRED_DOCUMENT +
                "(" +
                KEY__ID + " INTEGER PRIMARY KEY,"
                + KEY_DOCUM_ID + " VARCHAR," +
                KEY_VALUES + " TEXT," +
                DOCUMENT_PATH + " TEXT," +
                DOCUMENT_STATUS + " TEXT," +
                DOCUMENT_AVAIL + " TEXT" + ")";

//        db.execSQL(CREATE_POSTS_TABLE);
        db.execSQL(CREATE_CHAT_TABLE);
        db.execSQL(CREATE_VEHICLEHIRED_TABLE);

        System.out.println("----------------------------------document table created--------------------------------");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCUMENT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_VEHICLEHIRED_DOCUMENT);
            onCreate(db);
        }
    }

    // Adding new contact
    public void AddDocumentData(DriverDocumentPojo chat, int flag, boolean IsUpdatedDocument) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();


        if (IsUpdatedDocument && flag == 1) {
            values.put(KEY_DOCUM_ID, chat.getId());
            values.put(KEY_VALUES, "");
        } else {
            if (flag == 1) {
                values.put(KEY_DOCUM_ID, chat.getId());
                values.put(KEY_VALUES, "");

            } else {
                values.put(KEY_DOCUM_ID, chat.getId());
                values.put(KEY_VALUES, "");
                values.put(DOCUMENT_AVAIL, chat.getDocument_avail());
                values.put(DOCUMENT_PATH, chat.getDocument_path());
                values.put(DOCUMENT_STATUS, chat.getDocument_status());
            }
        }


        // Inserting Row
        db.insert(TABLE_DOCUMENT, null, values);
        System.out.println("******************add chat***************" + values);
        db.close();
    }

    public ArrayList<String> getAllID() {
        ArrayList<String> books = new ArrayList<String>();
        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_DOCUMENT;
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // 3. go over each row, build book and add it to list
        try {
            if (cursor.moveToFirst()) {
                do {
                    String id = cursor.getString(cursor.getColumnIndex(KEY_DOCUM_ID));
                    books.add(id);

                    // Add book to books
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        db.close();
        // return books
        return books;
    }

    public ArrayList<String> getAllName() {
        ArrayList<String> books = new ArrayList<String>();
        try {
            // 1. build the query
            String query = "SELECT  * FROM " + TABLE_DOCUMENT;

            // 2. get reference to writable DB
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            // 3. go over each row, build book and add it to list
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex(KEY_VALUES));
                    books.add(name);

                    // Add book to books
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return books
        return books;
    }


    public void updateStatus(String DocumentName, String titleId) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(KEY_VALUES, DocumentName);
            // cv.put(KEY_DOCUM_ID, titleId);

            db.update(TABLE_DOCUMENT, cv, KEY_DOCUM_ID + "=?", new String[]{String.valueOf(titleId)});
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   /* public void updateExpiryDocumentStatus(String  DocumentName,String titleId,String document_avail,String document_path,String document_status)
    {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(KEY_VALUES, DocumentName);

            cv.put(DOCUMENT_AVAIL, document_avail);
            cv.put(DOCUMENT_PATH, document_path);
            cv.put(DOCUMENT_STATUS, document_status);

        *//*return *//*
            db.update(TABLE_DOCUMENT, cv, KEY_DOCUM_ID + "=?"+" and "+DOCUMENT_PATH+"=?"+" and "+DOCUMENT_AVAIL+"=?"+" and "+DOCUMENT_STATUS+"=?", new String[]{String.valueOf(titleId),document_path,document_avail,document_status});
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/

    public void deleteTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_DOCUMENT); //delete all rows in a table
        db.close();


    }

    public void clearTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_DOCUMENT, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            if (count > 0) {
                db.delete(TABLE_DOCUMENT, null, null);
                db.close();
            }
            cursor.close();
        }

    }


/*
    public void AddVehicleHiredDocument(VehicleHiredPojo chat) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_DOCUM_ID, chat.getId());
        values.put(KEY_VALUES, "");

        db.insert(TABLE_VEHICLEHIRED_DOCUMENT, null, values);
        System.out.println("******************add chat***************" + values);
        db.close();
    }
*/

    public ArrayList<String> getAllVehiclehiredID() {
        ArrayList<String> books = new ArrayList<String>();
        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_VEHICLEHIRED_DOCUMENT;
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // 3. go over each row, build book and add it to list
        try {
            if (cursor.moveToFirst()) {
                do {
                    String id = cursor.getString(cursor.getColumnIndex(KEY_DOCUM_ID));
                    books.add(id);

                    // Add book to books
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        // return books
        return books;
    }


    public ArrayList<String> getAllVehiclehiredName() {
        ArrayList<String> books = new ArrayList<String>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_VEHICLEHIRED_DOCUMENT;
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // 3. go over each row, build book and add it to list
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(KEY_VALUES));
                books.add(name);
                // Add book to books
            } while (cursor.moveToNext());
        }


        // return books
        return books;
    }

    public void updateVehicleHiredStatus(String DocumentName, String titleId) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(KEY_VALUES, DocumentName);
            // cv.put(KEY_DOCUM_ID, titleId);

        /*return */
            db.update(TABLE_VEHICLEHIRED_DOCUMENT, cv, KEY_DOCUM_ID + "=?", new String[]{String.valueOf(titleId)});
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteVehicleHiredTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_VEHICLEHIRED_DOCUMENT); //delete all rows in a table
        db.close();
    }

    public void clearVehicleHiredTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_VEHICLEHIRED_DOCUMENT, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            if (count > 0) {
                db.delete(TABLE_VEHICLEHIRED_DOCUMENT, null, null);
                db.close();
            }
            cursor.close();
        }

    }


}