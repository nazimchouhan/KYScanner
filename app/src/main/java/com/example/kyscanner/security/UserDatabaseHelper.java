package com.example.kyscanner.security;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.kyscanner.model.UserModel;

import java.util.List;

public class UserDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final int DATABASE_VERSION = 5; // Incremented to force fresh table creation
    private static final String TABLE_NAME = "users";
    private static final String COLUMN_KY_ID = "ky_id";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_GMAIL = "gmail";
    public static final String COLUMN_EVENT1 = "event1";
    public static final String COLUMN_EVENT2 = "event2";
    public static final String COLUMN_EVENT3 = "event3";

    private static UserDatabaseHelper instance;
    private SQLiteDatabase database;

    public static synchronized UserDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new UserDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    public synchronized SQLiteDatabase getDatabase() {
        if (database == null || !database.isOpen()) {
            database = getWritableDatabase();
        }
        return database;
    }

    public synchronized void closeDatabase() {
        if (database != null && database.isOpen()) {
            database.close();
            database = null;
        }
    }

    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create fresh table
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_KY_ID + " TEXT PRIMARY KEY, " +
                COLUMN_GENDER + " TEXT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_GMAIL + " TEXT, " +
                COLUMN_EVENT1 + " INTEGER DEFAULT 0, " +
                COLUMN_EVENT2 + " INTEGER DEFAULT 0, " +
                COLUMN_EVENT3 + " INTEGER DEFAULT 0)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing table and create a fresh one
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // ✅ Insert or Update User Data
    public void insertOrUpdateUser(String kyId, String gender, String name, String gmail, boolean event1, boolean event2, boolean event3) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_KY_ID, kyId);
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_GMAIL, gmail);
        values.put(COLUMN_EVENT1, event1 ? 1 : 0);
        values.put(COLUMN_EVENT2, event2 ? 1 : 0);
        values.put(COLUMN_EVENT3, event3 ? 1 : 0);

        db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    // ✅ Fetch User Details by ky_id
    public UserModel getUserByKyId(String kyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db == null || !db.isOpen()) {
            Log.e("Database Error", "Database is closed when trying to fetch user!");
            return null;
        }

        Cursor cursor = null;
        UserModel user = null;

        try {
            String[] columns = {COLUMN_NAME, COLUMN_GENDER, COLUMN_GMAIL, COLUMN_EVENT1, COLUMN_EVENT2, COLUMN_EVENT3};
            String selection = COLUMN_KY_ID + " = ?";
            String[] selectionArgs = {kyId};

            cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                String gender = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENDER));
                String gmail = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GMAIL));
                boolean event1 = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EVENT1)) == 1;
                boolean event2 = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EVENT2)) == 1;
                boolean event3 = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EVENT3)) == 1;

                user = new UserModel(kyId, gender, name, gmail, event1, event2, event3);
            }
        } catch (Exception e) {
            Log.e("DB_ERROR", "Error fetching user: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }

        return user;
    }

    // ✅ Get Total Number of Users
    public int getRowCount() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        int rowCount = 0;
        if (cursor.moveToFirst()) {
            rowCount = cursor.getInt(0);
        }
        cursor.close();
        return rowCount;
    }

    // ✅ Check if a User Exists
    public boolean doesUserExist(String kyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_NAME + " WHERE " + COLUMN_KY_ID + " = ? LIMIT 1", new String[]{kyId});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }


    public boolean updateEventEligibility(String kyId,String eventColumn,boolean isEligible){
        boolean isUpdated=false;
        SQLiteDatabase db = this.getWritableDatabase(); // Ensure the database is open
        if (db == null || !db.isOpen()) {
            Log.e("DB_UPDATE", "Database is closed while updating event eligibility!");
            return false;
        }

        UserModel user=getUserByKyId(kyId);
        int rowsUpdated=0;
        db.beginTransaction(); // Start transaction

        try {
            ContentValues values = new ContentValues();
            values.put(eventColumn, isEligible ? 1 : 0); // Update the event column with the new value

            // Log before update
            Cursor checkCursor = db.rawQuery("SELECT " + eventColumn + " FROM " + TABLE_NAME + " WHERE " + COLUMN_KY_ID + "=?", new String[]{kyId});
            if (checkCursor.moveToFirst()) {
                int beforeUpdate = checkCursor.getInt(0);
                Log.e("DB_UPDATE_CHECK", "Before update, " + eventColumn + " is: " + beforeUpdate);
            }
            checkCursor.close();

            // Perform the update
            rowsUpdated = db.update(TABLE_NAME, values, COLUMN_KY_ID + "=?", new String[]{kyId});

            // Log after update
            Cursor cursor = db.rawQuery("SELECT " + eventColumn + " FROM " + TABLE_NAME + " WHERE " + COLUMN_KY_ID + "=?", new String[]{kyId});
            if (cursor.moveToFirst()) {
                int updatedValue = cursor.getInt(0);
                Log.e("DB_UPDATE_CHECK", "After update, " + eventColumn + " is now: " + updatedValue);
            }
            cursor.close();

            if (rowsUpdated > 0) {
                db.setTransactionSuccessful(); // Mark transaction as successful
                Log.e("DB_UPDATE", "Update successful for KY ID: " + kyId);

            } else {
                Log.e("DB_UPDATE", "No rows affected for KY ID: " + kyId);
            }
        } catch (Exception e) {
            Log.e("DB_UPDATE", "Error updating database: " + e.getMessage());
        } finally {
            db.endTransaction(); // End transaction
        }
        // Fetch updated user data after the transaction
        UserModel updatedUser = getUserByKyId(kyId);
        if (updatedUser != null) {
            Log.e("Event1", "Event1 detail is: " + updatedUser.isEvent1());
        }

        //db.close();
        return rowsUpdated > 0;
    }

}

