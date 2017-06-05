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

public class User_Provider {}
/*
extends ContentProvider {

    private static final int DATABASE_VERSION = 5;

    public static String AUTHORITY = "io.github.cluo29.build.provider.user";

    // ContentProvider query paths
    private static final int ACCEL_DATA = 1;
    private static final int ACCEL_DATA_ID = 2;

    public static final class User_Data implements BaseColumns {
        private User_Data() {
        }

        public static final Uri CONTENT_URI = Uri.parse("content://"
                + User_Provider.AUTHORITY + "/user");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.build.user.data";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.build.user.data";

        public static final String _ID = "_id";

        public static final String USER_ID = "user_id";
        public static final String USERNAME = "username";
        public static final String EMAIL = "email";
        public static final String GENDER = "gender";
        public static final String BIRTHDAY = "birthday";
        public static final String ADDRESS = "address";

        public static final String ARM = "arm";
        public static final String LEG = "leg";
        public static final String WEIGHT = "weight";
    }

    public static String DATABASE_NAME = "user.db";

    public static final String[] DATABASE_TABLES = {"user" };

    public static final String[] TABLES_FIELDS = {
            User_Data._ID + " integer primary key autoincrement,"
                    + User_Data.USER_ID + " text default '',"
                    + Routine_Exercise_Data.EXERCISE_ID + " integer default 0,"
                    + Routine_Exercise_Data.SET + " integer default 0,"
                    + Routine_Exercise_Data.REPT + " integer default 0"
    };
}
*/