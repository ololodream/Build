package com.example.miya_.myapplication.providers;

/**
 * Created by chuluo on 27/4/17.
 */

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Environment;
import android.provider.BaseColumns;
import android.util.Log;

import java.io.File;
import java.util.HashMap;
import com.example.miya_.myapplication.providers.DatabaseHelper;

public class Routine_Exercise_Provider extends ContentProvider {

    private static final int DATABASE_VERSION = 8;

    public static String AUTHORITY = "io.github.cluo29.build.provider.routineexercise";

    // ContentProvider query paths
    private static final int ACCEL_DATA = 1;
    private static final int ACCEL_DATA_ID = 2;

    public static final class Routine_Exercise_Data implements BaseColumns {
        private Routine_Exercise_Data() {
        }

        public static final Uri CONTENT_URI = Uri.parse("content://"
                + Routine_Exercise_Provider.AUTHORITY + "/routineexercise");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.build.routineexercise.data";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.build.routineexercise.data";

        public static final String _ID = "_id";
        public static final String ROUTINE_ID = "routine_id";
        public static final String EXERCISE_ID = "exercise_id";
        public static final String EXERCISE_NAME = "exercise_name";
        public static final String SET = "setset";
        public static final String REPT = "rept";
    }

    public static String DATABASE_NAME = "routineexercise.db";

    public static final String[] DATABASE_TABLES = {"routineexercise" };

    public static final String[] TABLES_FIELDS = {
            Routine_Exercise_Data._ID + " integer primary key autoincrement,"
                    + Routine_Exercise_Data.ROUTINE_ID + " integer default 0,"
                    + Routine_Exercise_Data.EXERCISE_ID + " integer default 0,"
                    + Routine_Exercise_Data.EXERCISE_NAME + " text default '',"
                    + Routine_Exercise_Data.SET + " integer default 0,"
                    + Routine_Exercise_Data.REPT + " integer default 0"
    };

    private static UriMatcher sUriMatcher = null;
    private static HashMap<String, String> accelDataMap = null;
    private static DatabaseHelper databaseHelper = null;
    private static SQLiteDatabase database = null;

    private boolean initializeDB() {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper( getContext(), DATABASE_NAME, null, DATABASE_VERSION, DATABASE_TABLES, TABLES_FIELDS );
        }
        if( databaseHelper != null && ( database == null || ! database.isOpen() )) {
            //HERE PROBLEM
            database = databaseHelper.getWritableDatabase();
        }
        return( database != null && databaseHelper != null);
    }

    public static void resetDB( Context c ) {

        File db = new File(DATABASE_NAME);
        if( db.delete() ) {
            databaseHelper = new DatabaseHelper( c, DATABASE_NAME, null, DATABASE_VERSION, DATABASE_TABLES, TABLES_FIELDS);
            database = databaseHelper.getWritableDatabase();
        }
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        if( ! initializeDB() ) {

            return 0;
        }
        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case ACCEL_DATA:
                database.beginTransaction();
                count = database.delete(DATABASE_TABLES[0], selection,
                        selectionArgs);
                database.setTransactionSuccessful();
                database.endTransaction();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case ACCEL_DATA:
                return Routine_Exercise_Data.CONTENT_TYPE;
            case ACCEL_DATA_ID:
                return Routine_Exercise_Data.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }


    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        if( ! initializeDB() ) {
            Log.d("AWARE","Database unavailable...111111");
            return null;
        }
        ContentValues values = (initialValues != null) ? new ContentValues(
                initialValues) : new ContentValues();

        switch (sUriMatcher.match(uri)) {
            case ACCEL_DATA:
                long accelData_id = database.insertWithOnConflict(DATABASE_TABLES[0],
                        Routine_Exercise_Data.ROUTINE_ID, values, SQLiteDatabase.CONFLICT_IGNORE);
                if (accelData_id > 0) {
                    Uri accelDataUri = ContentUris.withAppendedId(
                            Routine_Exercise_Data.CONTENT_URI, accelData_id);
                    getContext().getContentResolver().notifyChange(accelDataUri,
                            null);
                    return accelDataUri;
                }
                throw new SQLException("Failed to insert row into " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }


    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        if (!initializeDB()) {

            return 0;
        }
        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case ACCEL_DATA:
                database.beginTransaction();
                for (ContentValues v : values) {
                    long id;
                    try {
                        id = database.insertOrThrow(DATABASE_TABLES[0], Routine_Exercise_Data.ROUTINE_ID, v);
                    } catch (SQLException e) {
                        id = database.replace(DATABASE_TABLES[0], Routine_Exercise_Data.ROUTINE_ID, v);
                    }
                    if (id <= 0) {
                        Log.w("Accelerometer.TAG", "Failed to insert/replace row into " + uri);
                    } else {
                        count++;
                    }
                }
                database.setTransactionSuccessful();
                database.endTransaction();
                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public boolean onCreate(){

        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //change provider!

        sUriMatcher.addURI(Routine_Exercise_Provider.AUTHORITY,
                DATABASE_TABLES[0], ACCEL_DATA);
        sUriMatcher.addURI(Routine_Exercise_Provider.AUTHORITY, DATABASE_TABLES[0]
                + "/#", ACCEL_DATA_ID);

        accelDataMap = new HashMap<String, String>();
        accelDataMap.put(Routine_Exercise_Data._ID, Routine_Exercise_Data._ID);
        accelDataMap.put(Routine_Exercise_Data.ROUTINE_ID,
                Routine_Exercise_Data.ROUTINE_ID);
        accelDataMap.put(Routine_Exercise_Data.EXERCISE_ID,
                Routine_Exercise_Data.EXERCISE_ID);
        accelDataMap.put(Routine_Exercise_Data.EXERCISE_NAME,
                Routine_Exercise_Data.EXERCISE_NAME);
        accelDataMap.put(Routine_Exercise_Data.SET,
                Routine_Exercise_Data.SET);
        accelDataMap.put(Routine_Exercise_Data.REPT,
                Routine_Exercise_Data.REPT);
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        if( ! initializeDB() ) {
            //Log.w(AUTHORITY,"Database unavailable...");
            return null;
        }
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)) {
            case ACCEL_DATA:
                qb.setTables(DATABASE_TABLES[0]);
                qb.setProjectionMap(accelDataMap);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        try {
            Cursor c = qb.query(database, projection, selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(getContext().getContentResolver(), uri);
            return c;
        } catch (IllegalStateException e) {

            return null;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        if( ! initializeDB() ) {
            //Log.w(AUTHORITY,"Database unavailable...");
            return 0;
        }
        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case ACCEL_DATA:
                database.beginTransaction();
                count = database.update(DATABASE_TABLES[0], values, selection,
                        selectionArgs);
                database.setTransactionSuccessful();
                database.endTransaction();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}

