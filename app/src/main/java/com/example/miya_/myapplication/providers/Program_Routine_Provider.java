package com.example.miya_.myapplication.providers;

/**
 * Created by chuluo on 28/4/17.
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

public class Program_Routine_Provider extends ContentProvider {

    private static final int DATABASE_VERSION = 6;

    public static String AUTHORITY = "io.github.cluo29.build.provider.programroutine";
    // ContentProvider query paths
    private static final int ACCEL_DATA = 1;
    private static final int ACCEL_DATA_ID = 2;

    public static final class Program_Routine_Data implements BaseColumns {
        private Program_Routine_Data() {
        }
        public static final Uri CONTENT_URI = Uri.parse("content://"
                + Program_Routine_Provider.AUTHORITY + "/programroutine");
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.build.programroutine.data";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.build.programroutine.data";

        public static final String _ID = "_id";
        public static final String PROGRAM_ID = "program_id";
        public static final String ROUTINE_ID = "routine_id";
        public static final String ROUTINE_NAME = "routine_name";
    }

    public static String DATABASE_NAME = "programroutine.db";

    public static final String[] DATABASE_TABLES = {"programroutine" };


    public static final String[] TABLES_FIELDS = {
            Program_Routine_Data._ID + " integer primary key autoincrement,"
                    + Program_Routine_Data.PROGRAM_ID + " integer default 0,"
                    + Program_Routine_Data.ROUTINE_NAME + " text default '',"
                    + Program_Routine_Data.ROUTINE_ID + " integer default 0"
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
                return Program_Routine_Data.CONTENT_TYPE;
            case ACCEL_DATA_ID:
                return Program_Routine_Data.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        if( ! initializeDB() ) {
            Log.d("AWARE","Database unavailable...");
            return null;
        }
        ContentValues values = (initialValues != null) ? new ContentValues(
                initialValues) : new ContentValues();

        switch (sUriMatcher.match(uri)) {
            case ACCEL_DATA:
                long accelData_id = database.insertWithOnConflict(DATABASE_TABLES[0],
                        Program_Routine_Data.PROGRAM_ID, values, SQLiteDatabase.CONFLICT_IGNORE);
                if (accelData_id > 0) {
                    Uri accelDataUri = ContentUris.withAppendedId(
                            Program_Routine_Data.CONTENT_URI, accelData_id);
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
                        id = database.insertOrThrow(DATABASE_TABLES[0], Program_Routine_Data.PROGRAM_ID, v);
                    } catch (SQLException e) {
                        id = database.replace(DATABASE_TABLES[0], Program_Routine_Data.PROGRAM_ID, v);
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
    public boolean onCreate() {

        //AUTHORITY = "io.github.cluo29.build.provider.programroutine";

        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        sUriMatcher.addURI(Program_Routine_Provider.AUTHORITY,
                DATABASE_TABLES[0], ACCEL_DATA);
        sUriMatcher.addURI(Program_Routine_Provider.AUTHORITY, DATABASE_TABLES[0]
                + "/#", ACCEL_DATA_ID);

        accelDataMap = new HashMap<String, String>();
        accelDataMap.put(Program_Routine_Data._ID, Program_Routine_Data._ID);
        accelDataMap.put(Program_Routine_Data.PROGRAM_ID, Program_Routine_Data.PROGRAM_ID);
        accelDataMap.put(Program_Routine_Data.ROUTINE_NAME, Program_Routine_Data.ROUTINE_NAME);
        accelDataMap.put(Program_Routine_Data.ROUTINE_ID, Program_Routine_Data.ROUTINE_ID);
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
