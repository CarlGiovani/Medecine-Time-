package com.hexakill.medstime.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hexakill.medstime.AlarmScheduler;
import com.hexakill.medstime.AlarmSet;
import com.hexakill.medstime.Medicine;

import java.util.ArrayList;
import java.util.List;

public class MyDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "medstime.db";
    private static final int DATABASE_VERSION = 5;

    private static final String TABLE_USER = "reminders_user";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "medicine_name";
    private static final String COL_DESC = "medicine_description";
    private static final String COL_INTERVAL = "interval_hours";
    private static final String COL_NOTE = "note";
    private static final String COL_START = "start_time";
    private static final String COL_ACTIVE = "active";

    private static final String TABLE_PREBUILT = "reminders_prebuilt";
    private static final String TABLE_PREBUILT_MED = "prebuilt_medicines";
    private static final String COL_MED_NAME = "name";
    private static final String COL_MED_DESC = "description";

    public MyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USER + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT, "
                + COL_DESC + " TEXT, "
                + COL_INTERVAL + " TEXT, "
                + COL_NOTE + " TEXT, "
                + COL_START + " LONG, "
                + COL_ACTIVE + " INTEGER DEFAULT 1)");

        db.execSQL("CREATE TABLE " + TABLE_PREBUILT + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT, "
                + COL_DESC + " TEXT, "
                + COL_INTERVAL + " TEXT, "
                + COL_NOTE + " TEXT, "
                + COL_START + " LONG, "
                + COL_ACTIVE + " INTEGER DEFAULT 1)");

        db.execSQL("CREATE TABLE " + TABLE_PREBUILT_MED + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_MED_NAME + " TEXT, "
                + COL_MED_DESC + " TEXT)");

        insertDefaultPrebuiltMedicines(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PREBUILT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PREBUILT_MED);
        onCreate(db);
    }

    private void insertDefaultPrebuiltMedicines(SQLiteDatabase db) {
        insertMedicine(db, "Paracetamol", "Painkiller & fever reducer");
        insertMedicine(db, "Amoxicillin", "Antibiotic");
        insertMedicine(db, "Ibuprofen", "Anti-inflammatory");
        insertMedicine(db, "Cetirizine", "Anti-allergy");
        insertMedicine(db, "Aspirin", "Painkiller / blood thinner");
    }

    private void insertMedicine(SQLiteDatabase db, String name, String desc) {
        ContentValues cv = new ContentValues();
        cv.put(COL_MED_NAME, name);
        cv.put(COL_MED_DESC, desc);
        db.insert(TABLE_PREBUILT_MED, null, cv);
    }

    public List<Medicine> getAllPrebuiltMedicines() {
        List<Medicine> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_PREBUILT_MED, null);

        while (c.moveToNext()) {
            list.add(new Medicine(
                    c.getInt(c.getColumnIndexOrThrow(COL_ID)),
                    c.getString(c.getColumnIndexOrThrow(COL_MED_NAME)),
                    c.getString(c.getColumnIndexOrThrow(COL_MED_DESC))
            ));
        }
        c.close();
        return list;
    }

    public long addPrebuiltReminder(String name, String desc, int intervalHours, String note, long startTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        cv.put(COL_DESC, desc);
        cv.put(COL_INTERVAL, String.valueOf(intervalHours));
        cv.put(COL_NOTE, note);
        cv.put(COL_START, startTime);
        cv.put(COL_ACTIVE, 1);
        return db.insert(TABLE_PREBUILT, null, cv);
    }

    public long addUserReminder(String name, String desc, String interval, String note, long startTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        cv.put(COL_DESC, desc);
        cv.put(COL_INTERVAL, interval);
        cv.put(COL_NOTE, note);
        cv.put(COL_START, startTime);
        cv.put(COL_ACTIVE, 1);
        return db.insert(TABLE_USER, null, cv);
    }

    public void updateUserReminder(int id, String name, String desc, String interval, String note, long startTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        cv.put(COL_DESC, desc);
        cv.put(COL_INTERVAL, interval);
        cv.put(COL_NOTE, note);
        cv.put(COL_START, startTime);
        db.update(TABLE_USER, cv, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void updatePrebuiltReminder(int id, String name, String desc, String interval, String note, long startTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        cv.put(COL_DESC, desc);
        cv.put(COL_INTERVAL, interval);
        cv.put(COL_NOTE, note);
        cv.put(COL_START, startTime);
        db.update(TABLE_PREBUILT, cv, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteUserReminder(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deletePrebuiltReminder(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PREBUILT, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    // ============================================================
    // UPDATE ALARM STATUS (Switch toggle) WITH CANCEL/RESCHEDULE
    // ============================================================
    public void updateAlarmStatus(Context context, AlarmSet alarm, boolean active) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_ACTIVE, active ? 1 : 0);

        db.update(TABLE_USER, cv, COL_ID + "=?", new String[]{String.valueOf(alarm.getId())});
        db.update(TABLE_PREBUILT, cv, COL_ID + "=?", new String[]{String.valueOf(alarm.getId())});

        // Cancel or reschedule in AlarmManager
        if (active) {
            String ringtoneUri = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
                    .getString("alarm_ringtone", null);
            AlarmScheduler.scheduleAlarm(context, alarm, ringtoneUri);
        } else {
            AlarmScheduler.cancelAlarm(context, alarm);
        }
    }

    public List<AlarmSet> getAllUserReminders() {
        return fetchAlarms(TABLE_USER, true);
    }

    public List<AlarmSet> getAllPremadeReminders() {
        return fetchAlarms(TABLE_PREBUILT, false);
    }

    private List<AlarmSet> fetchAlarms(String tableName, boolean isUser) {
        List<AlarmSet> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + tableName, null);

        while (c.moveToNext()) {
            AlarmSet alarm = new AlarmSet(
                    c.getInt(c.getColumnIndexOrThrow(COL_ID)),
                    c.getString(c.getColumnIndexOrThrow(COL_NAME)),
                    c.getString(c.getColumnIndexOrThrow(COL_INTERVAL)),
                    c.getString(c.getColumnIndexOrThrow(COL_NOTE)),
                    c.getLong(c.getColumnIndexOrThrow(COL_START))
            );
            alarm.setUserCreated(isUser);
            alarm.setActive(c.getInt(c.getColumnIndexOrThrow(COL_ACTIVE)) == 1);
            list.add(alarm);
        }
        c.close();
        return list;
    }
}
