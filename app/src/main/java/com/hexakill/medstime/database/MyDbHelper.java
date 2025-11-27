package com.hexakill.medstime.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.hexakill.medstime.AlarmSet;
import com.hexakill.medstime.Medicine;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class MyDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "UserReminder.db";
    private static final int DB_VERSION = 1;
    private final Context context;

    // User table
    private static final String TABLE_USER = "Table_User";
    private static final String COL_USER_ID = "ID";
    private static final String COL_USER_NAME = "Medicine_Name";
    private static final String COL_USER_DESCRIPTION = "Medicine_Description";
    private static final String COL_USER_INTERVAL = "Interval";
    private static final String COL_USER_NOTE = "Note";

    public MyDbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_NAME + " TEXT, " +
                COL_USER_DESCRIPTION + " TEXT, " +
                COL_USER_INTERVAL + " TEXT, " +
                COL_USER_NOTE + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    // -------------------- User table methods --------------------
    public void addUserReminder(String name, String description, String interval, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USER_NAME, name);
        cv.put(COL_USER_DESCRIPTION, description);
        cv.put(COL_USER_INTERVAL, interval);
        cv.put(COL_USER_NOTE, note);

        long result = db.insert(TABLE_USER, null, cv);
        Toast.makeText(context, result == -1 ? "Failed" : "Added Successfully!", Toast.LENGTH_SHORT).show();
    }

    // Add prebuilt medicine to user table
    public void addPrebuiltReminder(String name, String description, int interval, String note) {
        addUserReminder(name, description, String.valueOf(interval), note);
    }

    public void updateUserReminder(int id, String name, String description, String interval, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USER_NAME, name);
        cv.put(COL_USER_DESCRIPTION, description);
        cv.put(COL_USER_INTERVAL, interval);
        cv.put(COL_USER_NOTE, note);

        int result = db.update(TABLE_USER, cv, COL_USER_ID + "=?", new String[]{String.valueOf(id)});
        Toast.makeText(context, result > 0 ? "Updated Successfully!" : "Update Failed", Toast.LENGTH_SHORT).show();
    }

    public void deleteUserReminder(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_USER, COL_USER_ID + "=?", new String[]{String.valueOf(id)});
        Toast.makeText(context, result > 0 ? "Deleted Successfully!" : "Delete Failed", Toast.LENGTH_SHORT).show();
    }

    public List<AlarmSet> getAllUserReminders() {
        List<AlarmSet> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER, null);

        if (cursor.moveToFirst()) {
            int nameIdx = cursor.getColumnIndex(COL_USER_NAME);
            int intervalIdx = cursor.getColumnIndex(COL_USER_INTERVAL);
            int noteIdx = cursor.getColumnIndex(COL_USER_NOTE);

            do {
                String name = nameIdx != -1 ? cursor.getString(nameIdx) : "";
                String interval = intervalIdx != -1 ? cursor.getString(intervalIdx) : "";
                String note = noteIdx != -1 ? cursor.getString(noteIdx) : "";
                list.add(new AlarmSet(name, interval, note)); // matches AlarmSet(String, String, String)
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // -------------------- Prebuilt medicines (hard-coded) --------------------
    public List<Medicine> getAllPrebuiltMedicines() {
        List<Medicine> prebuilt = new ArrayList<>();
        prebuilt.add(new Medicine("Paracetamol", "Pain reliever and fever reducer"));
        prebuilt.add(new Medicine("Ibuprofen", "Reduces inflammation and pain"));
        prebuilt.add(new Medicine("Vitamin C", "Boosts immune system"));
        prebuilt.add(new Medicine("Amoxicillin", "Antibiotic for bacterial infections"));
        return prebuilt;
    }
}
