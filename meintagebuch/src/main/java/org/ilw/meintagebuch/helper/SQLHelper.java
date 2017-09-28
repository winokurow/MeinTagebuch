package org.ilw.meintagebuch.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import org.ilw.meintagebuch.dto.Day;
import org.ilw.meintagebuch.dto.Subject;
import org.ilw.meintagebuch.dto.SubjectMod;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class SQLHelper extends SQLiteOpenHelper {

    private static final String TAG = SQLHelper.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 10;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    private static final String TABLE_RECORDS = "records";

    private static final String KEY_RECORDS_DATE = "date";
    private static final String KEY_RECORDS_SUBJECTS = "subjects";
    private static final String KEY_RECORDS_COMMENT = "comment";

    private static final String TABLE_SUBJECTS = "subjects";

    private static final String KEY_SUBJECTS_ID = "id";
    private static final String KEY_SUBJECTS_NAME = "name";
    private static final String KEY_SUBJECTS_STATUS = "status";
    private static final String KEY_SUBJECTS_DESCRIPTION = "description";

    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        if (!(isTableExists(db, TABLE_RECORDS, false))) {
            String CREATE_RECORDS_TABLE = "CREATE TABLE " + TABLE_RECORDS + "("
                    + KEY_RECORDS_DATE + " TEXT PRIMARY KEY," + KEY_RECORDS_SUBJECTS + " TEXT,"
                    + KEY_RECORDS_COMMENT + " TEXT)";
            db.execSQL(CREATE_RECORDS_TABLE);
            String CREATE_SUBJECTS_TABLE = "CREATE TABLE " + TABLE_SUBJECTS + "("
                    + KEY_SUBJECTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_SUBJECTS_NAME + " TEXT," + KEY_SUBJECTS_DESCRIPTION + " TEXT,"
                    + KEY_SUBJECTS_STATUS + " TEXT)";
            db.execSQL(CREATE_SUBJECTS_TABLE);

            addSubject(db, "Schule", "1", "Schule");
            addSubject(db, "Sport", "1", "Sport");
            addSubject(db, "Entspannen", "1", "Entspannen");
            addSubject(db, "Aussehen", "1", "Aussehen");
            addSubject(db, "Freunde", "1", "Freunde");
            addSubject(db, "Neues", "1", "Etwas neues...");
            addSubject(db, "Zimmer", "1", "Zimmer und Wohnung");
            addSubject(db, "Eltern", "1", "Eltern");
            addSubject(db, "Kontrolle", "1", "Selbstkontrolle");
            addSubject(db, "Hobby", "1", "Hobby");
            Log.d(TAG, "Database tables created");
        }
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        int upgradeTo = oldVersion + 1;
        while (upgradeTo != newVersion)
        {
            switch (upgradeTo)
            {
                case 5:
                    break;
                case 6:
                    break;
                case 9:
                    addSubject(db, "Test", "1", "Test");
                    break;
            }
            upgradeTo++;
        }

        // Create tables again
        onCreate(db);
    }

    public Map<String,Day> getRecords()
    {
        String selectQuery = "SELECT * FROM " + TABLE_RECORDS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Map<String,Day> records = new HashMap<>();
        if (cursor.getCount() > 0) {
            // Move to first row
            if (cursor.moveToFirst()) {
                do {
                    String[] subjectsText = cursor.getString(1).split("YYYYY");
                    Map<Integer, Subject> subjects = new HashMap<>();
                    for (String subjectText:subjectsText) {
                        String[] temp = subjectText.split("XXXXX");
                        subjects.put(Integer.parseInt(temp[0]), new Subject(Integer.parseInt(temp[1]), temp[2]));
                    }
                    records.put(cursor.getString(0), new Day(subjects, cursor.getString(2)));
                    // get the data into array, or class variable
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return records;
    }

    public boolean isDateExists(String date)
    {
        String selectQuery = "SELECT * FROM " + TABLE_RECORDS + " WHERE " + KEY_RECORDS_DATE + "= '"+ date + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Map<String,Day> records = new HashMap<>();
        boolean isFound = false;
        if (cursor.getCount() > 0) {
            isFound = true;
            }
            cursor.close();
        return isFound;
    }

    /**
     * Storing user details in database
     * */
    public void addDay(String date, Day day) {
        ContentValues values = new ContentValues();
        values.put(KEY_RECORDS_DATE, date);
        values.put(KEY_RECORDS_SUBJECTS, day.getSubjectsString());
        values.put(KEY_RECORDS_COMMENT, day.getComment());

        SQLiteDatabase db = this.getWritableDatabase();

        if (isDateExists(date)) {
            db.update(TABLE_RECORDS, values, KEY_RECORDS_DATE+"='"+date + "'", null);
        } else
        {
            long id = db.insert(TABLE_RECORDS, null, values);
        }
    }

    /**
     * Storing user details in database
     * */
    public void addSubject(String name, String status, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        addSubject(db, name, status, description);
    }

    /**
     * Storing subject in database
     * */
    public void addSubject(SQLiteDatabase db,String name, String status, String description) {
        ContentValues values = new ContentValues();
        values.put(KEY_SUBJECTS_NAME, name);
        values.put(KEY_SUBJECTS_STATUS, status);
        values.put(KEY_SUBJECTS_DESCRIPTION, description);

        long id = db.insert(TABLE_SUBJECTS, null, values);
    }

    /**
     * Storing user details in database
     * */
    public void updateSubject(String id, String name, String status, String description) {
        ContentValues values = new ContentValues();
        values.put(KEY_SUBJECTS_NAME, name);
        values.put(KEY_SUBJECTS_STATUS, status);
        values.put(KEY_SUBJECTS_DESCRIPTION, description);

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_SUBJECTS, values, KEY_SUBJECTS_ID+"='"+id + "'", null);
    }

    public Map<Integer, SubjectMod> getSubjects(String status)
    {
        String selectQuery = "SELECT * FROM " + TABLE_SUBJECTS + " WHERE STATUS = " + status;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Map<Integer, SubjectMod> records = new HashMap<>();
        if (cursor.getCount() > 0) {
            // Move to first row
            if (cursor.moveToFirst()) {
                do {
                    records.put(cursor.getInt(0), new SubjectMod(cursor.getString(1), cursor.getString(2)));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return records;
    }

    public Map<Integer, SubjectMod> getSubjects()
    {
        String selectQuery = "SELECT * FROM " + TABLE_SUBJECTS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Map<Integer, SubjectMod> records = new HashMap<>();
        if (cursor.getCount() > 0) {
            // Move to first row
            if (cursor.moveToFirst()) {
                do {
                    records.put(cursor.getInt(0), new SubjectMod(cursor.getString(1), cursor.getString(2), cursor.getString(3)));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return records;
    }

    public boolean isTableExists(SQLiteDatabase db, String tableName, boolean openDb) {
        if(openDb) {
            if(db == null || !db.isOpen()) {
                db = getReadableDatabase();
            }

            if(!db.isReadOnly()) {
                db.close();
                db = getReadableDatabase();
            }
        }

        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public static void backupDatabase() throws IOException {
        //Open your local db as the input stream
        String inFileName = "/data/data/org.ilw.meintagebuch/databases/" + DATABASE_NAME;
        File dbFile = new File(inFileName);
        FileInputStream fis = new FileInputStream(dbFile);

        String outFileName = Environment.getExternalStorageDirectory()+"/" + DATABASE_NAME;
        //Open the empty db as the output stream
        OutputStream output = new FileOutputStream(outFileName);
        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer))>0){
            output.write(buffer, 0, length);
        }
        //Close the streams
        output.flush();
        output.close();
        fis.close();
    }

    public void importDB() throws IOException {
        // Close the SQLiteOpenHelper so it will commit the created empty
        // database to internal storage.
        //close();

        //Open your local db as the input stream
        String outFileName = "/data/data/org.ilw.meintagebuch/databases/" + DATABASE_NAME;
        String inFileName = Environment.getExternalStorageDirectory()+"/" + DATABASE_NAME;
        File dbFile = new File(inFileName);
        File newFile = new File(outFileName);
        if (newFile.exists())
        {
            newFile.delete();
        }
        FileInputStream fis = new FileInputStream(dbFile);


        //Open the empty db as the output stream
        OutputStream output = new FileOutputStream(outFileName);
        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer))>0){
            output.write(buffer, 0, length);
        }
        //Close the streams
        output.flush();
        output.close();
        fis.close();
        this.
        getWritableDatabase().close();
    }

    public void resetDatabase() {
        getWritableDatabase().close();
        onCreate(getWritableDatabase());
    }
}
