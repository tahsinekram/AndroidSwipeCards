package com.androidtutorialpoint.androidswipecards.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by tahsi on 12/25/2016.
 */

public final class AppContract {

    private AppContract(){}

    public static final String CONTENT_AUTHORITY ="com.androidtutorialpoint.androidswipecards";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_BRAIN = "brain";

    public static final String PATH_QA = "answers";



    public static final class BrainEntry implements BaseColumns {

        /** The content URI to access the pet data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BRAIN);
        public static final Uri CONTENT_URI_ANSWER = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_QA);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE_BRAIN =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BRAIN;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE_ANSWER =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_QA;

        /** Name of database table for pets */
        public final static String TABLE_NAME = "brain";
        public final static String TABLE_NAME_QA = "answer";


        /**
         * Unique ID number for the pet (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        public final static String _ANSWERID = BaseColumns._ID;

        /**
         * Name of the pet.
         *
         * Type: TEXT
         */
        public final static String COLUMN_QUESTION ="question";


        /**
         * Breed of the pet.
         *
         * Type: TEXT
         */
        public final static String COLUMN_ANSWER = "answer";

        public final static String COLUMN_CHALLENGE = "challenge";

        public final static String COLUMN_KEY = "key";



    }
}
