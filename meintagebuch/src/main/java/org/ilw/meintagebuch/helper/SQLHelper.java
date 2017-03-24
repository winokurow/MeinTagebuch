package org.ilw.meintagebuch.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.ilw.meintagebuch.dto.Day;
import org.ilw.meintagebuch.dto.Subject;

import java.util.HashMap;
import java.util.Map;

public class SQLHelper extends SQLiteOpenHelper {

    private static final String TAG = SQLHelper.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    private static final String TABLE_RECORDS = "records";

    private static final String KEY_RECORDS_DATE = "date";
    private static final String KEY_RECORDS_SUBJECTS = "subjects";
    private static final String KEY_RECORDS_COMMENT = "comment";

    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_RECORDS_TABLE = "CREATE TABLE " + TABLE_RECORDS + "("
                + KEY_RECORDS_DATE + " TEXT PRIMARY KEY," + KEY_RECORDS_SUBJECTS + " TEXT,"
                + KEY_RECORDS_COMMENT + " TEXT)";
        db.execSQL(CREATE_RECORDS_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDS);
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
                    Map<String, Subject> subjects = new HashMap<>();
                    for (String subjectText:subjectsText) {
                        String[] temp = subjectText.split("XXXXX");
                        subjects.put(temp[0], new Subject(Integer.parseInt(temp[1]), temp[2]));
                    }
                    records.put(cursor.getString(0), new Day(subjects, cursor.getString(2)));
                    // get the data into array, or class variable
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
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

}
