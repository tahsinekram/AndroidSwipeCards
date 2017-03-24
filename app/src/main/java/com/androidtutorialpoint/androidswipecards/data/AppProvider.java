package com.androidtutorialpoint.androidswipecards.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by tahsi on 12/25/2016.
 */

public class AppProvider extends ContentProvider{

    public static final String LOG_TAG = AppProvider.class.getSimpleName();

    private static final int BRAIN = 100;

    private static final int BRAIN_ID = 101;

    private static final int ANSWER = 102;

    private static final int ANSWER_ID = 103;



    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // The content URI of the form "content://com.example.android.pets/pets" will map to the
        // integer code {@link #PETS}. This URI is used to provide access to MULTIPLE rows
        // of the pets table.
        sUriMatcher.addURI(AppContract.CONTENT_AUTHORITY, AppContract.PATH_BRAIN, BRAIN);

        // The content URI of the form "content://com.example.android.pets/pets/#" will map to the
        // integer code {@link #PET_ID}. This URI is used to provide access to ONE single row
        // of the pets table.
        //
        // In this case, the "#" wildcard is used where "#" can be substituted for an integer.
        // For example, "content://com.example.android.pets/pets/3" matches, but
        // "content://com.example.android.pets/pets" (without a number at the end) doesn't match.
        sUriMatcher.addURI(AppContract.CONTENT_AUTHORITY, AppContract.PATH_BRAIN + "/#", BRAIN_ID);

        sUriMatcher.addURI(AppContract.CONTENT_AUTHORITY, AppContract.PATH_QA, ANSWER);

        // The content URI of the form "content://com.example.android.pets/pets/#" will map to the
        // integer code {@link #PET_ID}. This URI is used to provide access to ONE single row
        // of the pets table.
        //
        // In this case, the "#" wildcard is used where "#" can be substituted for an integer.
        // For example, "content://com.example.android.pets/pets/3" matches, but
        // "content://com.example.android.pets/pets" (without a number at the end) doesn't match.
        sUriMatcher.addURI(AppContract.CONTENT_AUTHORITY, AppContract.PATH_QA + "/#", ANSWER_ID);


    }
    private AppDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new AppDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case BRAIN:
                // For the PETS code, query the pets table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.
                cursor = database.query(AppContract.BrainEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case BRAIN_ID:
                // For the PET_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = AppContract.BrainEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(AppContract.BrainEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case ANSWER:
                // For the PETS code, query the pets table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.
                cursor = database.query(AppContract.BrainEntry.TABLE_NAME_QA, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case ANSWER_ID:
                // For the PET_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = AppContract.BrainEntry._ANSWERID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(AppContract.BrainEntry.TABLE_NAME_QA, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BRAIN:
                return AppContract.BrainEntry.CONTENT_LIST_TYPE_BRAIN;
            case BRAIN_ID:
                return AppContract.BrainEntry.CONTENT_ITEM_TYPE_ANSWER;
            case ANSWER:
                return AppContract.BrainEntry.CONTENT_LIST_TYPE_BRAIN;
            case ANSWER_ID:
                return AppContract.BrainEntry.CONTENT_ITEM_TYPE_ANSWER;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BRAIN:
                return insertPetBrain(uri, contentValues);
            case ANSWER:
                return insertPetAnswer(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertPetBrain(Uri uri, ContentValues values) {
        // Check that the name is not null
        String challenge = values.getAsString(AppContract.BrainEntry.COLUMN_CHALLENGE);
        if (challenge == null) {
            throw new IllegalArgumentException("Pet requires a name");
        }
        // No need to check the breed, any value is valid (including null).

        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(AppContract.BrainEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertPetAnswer(Uri uri, ContentValues values) {
        // Check that the name is not null
        String question = values.getAsString(AppContract.BrainEntry.COLUMN_QUESTION);
        if (question == null) {
            throw new IllegalArgumentException("Pet requires a name");
        }

        String answer = values.getAsString(AppContract.BrainEntry.COLUMN_ANSWER);
        if (answer == null) {
            throw new IllegalArgumentException("Pet requires a name");
        }
        // No need to check the breed, any value is valid (including null).

        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(AppContract.BrainEntry.TABLE_NAME_QA, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BRAIN:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(AppContract.BrainEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case BRAIN_ID:
                // Delete a single row given by the ID in the URI
                selection = AppContract.BrainEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(AppContract.BrainEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ANSWER:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(AppContract.BrainEntry.TABLE_NAME_QA, selection, selectionArgs);
                break;
            case ANSWER_ID:
                // Delete a single row given by the ID in the URI
                selection = AppContract.BrainEntry._ANSWERID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(AppContract.BrainEntry.TABLE_NAME_QA, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BRAIN:
                return updatePet(uri, contentValues, selection, selectionArgs);
            case BRAIN_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = AppContract.BrainEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePet(uri, contentValues, selection, selectionArgs);
            case ANSWER:

                return updatePet(uri, contentValues, selection, selectionArgs);
            case ANSWER_ID:
                selection = AppContract.BrainEntry._ANSWERID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePet(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update pets in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more pets).
     * Return the number of rows that were successfully updated.
     */
    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
        // check that the name value is not null.

        if (values.containsKey(AppContract.BrainEntry.COLUMN_QUESTION)) {
            String question = values.getAsString(AppContract.BrainEntry.COLUMN_QUESTION);
            if (question == null) {
                throw new IllegalArgumentException("App requiers a question");
            }
        }

        if (values.containsKey(AppContract.BrainEntry.COLUMN_ANSWER)) {
            String answer = values.getAsString(AppContract.BrainEntry.COLUMN_ANSWER);
            if (answer == null) {
                throw new IllegalArgumentException("App requiers a answer");
            }
        }
        // No need to check the breed, any value is valid (including null).

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(AppContract.BrainEntry.TABLE_NAME_QA, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }


}
