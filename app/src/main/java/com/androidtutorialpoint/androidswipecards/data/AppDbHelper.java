package com.androidtutorialpoint.androidswipecards.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tahsi on 12/25/2016.
 */

public class AppDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = AppDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "brain.db";

    private static final int DATABASE_VERSION = 1;

    public AppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_BRAIN_TABLE =  "CREATE TABLE " + AppContract.BrainEntry.TABLE_NAME + " ("
                + AppContract.BrainEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + AppContract.BrainEntry.COLUMN_CHALLENGE + " TEXT)";

        String SQL_CREATE_ANSWER_TABLE =  "CREATE TABLE " + AppContract.BrainEntry.TABLE_NAME_QA + " ("
                + AppContract.BrainEntry._ANSWERID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + AppContract.BrainEntry.COLUMN_QUESTION + " TEXT, "
                + AppContract.BrainEntry.COLUMN_KEY + " TEXT, "
                + AppContract.BrainEntry.COLUMN_ANSWER + " TEXT)";

        // Execute the SQL statement

        db.execSQL(SQL_CREATE_BRAIN_TABLE);
        db.execSQL(SQL_CREATE_ANSWER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
