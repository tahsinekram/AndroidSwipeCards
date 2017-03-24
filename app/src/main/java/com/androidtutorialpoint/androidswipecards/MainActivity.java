package com.androidtutorialpoint.androidswipecards;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidtutorialpoint.androidswipecards.data.AppContract.BrainEntry;
import com.androidtutorialpoint.androidswipecards.swipemenulistview.SwipeMenu;
import com.androidtutorialpoint.androidswipecards.swipemenulistview.SwipeMenuCreator;
import com.androidtutorialpoint.androidswipecards.swipemenulistview.SwipeMenuItem;
import com.androidtutorialpoint.androidswipecards.swipemenulistview.SwipeMenuListView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText input;
    private static final int BRAIN_LOADER = 0;
    private BrainCursorAdapter mCursorAdapter;
    private SearchView searchViewMain;
    private SwipeMenuListView BrainListView;
    private AlertDialog ad;
    private AlertDialog.Builder builder;
    private FloatingActionButton fab;
    private SwipeMenuCreator creator;
    private ContentValues values;
    private static final String [] projection = {
            BrainEntry._ID,
            BrainEntry.COLUMN_CHALLENGE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_main);
        ImageView img=(ImageView)findViewById(R.id.no);
        img.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.brainstorm,120,180));
        values = new ContentValues();
        input = new EditText(getApplicationContext());
        input.setTextColor(Color.BLACK);
        input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        mCursorAdapter=new BrainCursorAdapter(getApplicationContext(),null,1);
        creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                //create an action that will be showed on swiping an item in the list
                SwipeMenuItem item1 = new SwipeMenuItem(
                        getApplicationContext());
                item1.setBackground(new ColorDrawable(Color.GREEN));
                // set width of an option (px)
                item1.setIcon(R.drawable.ic_mode_edit_white_48dp);
                item1.setWidth(200);
                item1.setHeight(200);
                menu.addMenuItem(item1);

                SwipeMenuItem item2 = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                item2.setIcon(R.drawable.ic_delete_white_24dp);
                item2.setBackground(new ColorDrawable(Color.RED));
                item2.setWidth(200);
                item2.setHeight(200);
                menu.addMenuItem(item2);
            }
        };
        UITask();
        BrainListView = (SwipeMenuListView) findViewById(R.id.Question);
        BrainListView.setEmptyView(img);
        BrainListView.setAdapter(mCursorAdapter);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.show();
            }
        });

        getSupportLoaderManager().initLoader(BRAIN_LOADER,null,this);
    }

    @Override
    protected void onStart(){
        super.onStart();


        BrainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Intent intent = new Intent(getApplicationContext(), AnswerActivity.class);
            intent.putExtra("qdata",((TextView)view.findViewById(R.id.challenge)).getText().toString().trim());
            startActivity(intent);
            }
        });
        //set MenuCreator
        BrainListView.setMenuCreator(creator);
        // set SwipeListener
        BrainListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });
        BrainListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "Action 1 for ", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Cursor c= (Cursor)mCursorAdapter.getItem(position);
                        getContentResolver().delete(BrainEntry.CONTENT_URI, BrainEntry.COLUMN_CHALLENGE + " LIKE '%" + (c.getString(c.getColumnIndex(BrainEntry.COLUMN_CHALLENGE))) + "%' COLLATE NOCASE", null);
                        getContentResolver().delete(BrainEntry.CONTENT_URI_ANSWER, BrainEntry.COLUMN_KEY + " LIKE '%" +(c.getString(c.getColumnIndex(BrainEntry.COLUMN_CHALLENGE)))+ "%' COLLATE NOCASE", null);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        final String [] selection={BrainEntry._ID, BrainEntry.COLUMN_CHALLENGE};
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.search,menu);
        searchViewMain = (SearchView) menu.findItem(R.id.search).getActionView();
        searchViewMain.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(newText!=null){
                    mCursorAdapter.swapCursor(getContentResolver().query(BrainEntry.CONTENT_URI,selection,BrainEntry.COLUMN_CHALLENGE +" LIKE '%" + newText +"%' COLLATE NOCASE",null,null));
                }
                return true;
            }
        });
        return true;
    }

    private void UITask(){
            builder = new AlertDialog.Builder(this);
            builder.setTitle("CHALLENGE");
            builder.setIcon(R.drawable.head);
            builder.setMessage("Enter challenge and press submit to move to questions");
            builder.setView(input);
            builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    values.put(BrainEntry.COLUMN_CHALLENGE, input.getText().toString().trim());
                    getContentResolver().insert(BrainEntry.CONTENT_URI,values);
                    values.clear();
                    Intent intent = new Intent(getApplicationContext(), QuestionsActivity.class);
                    intent.putExtra("data",input.getText().toString().trim());
                    startActivity(intent);
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            ad=builder.create();
        }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                BrainEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(ad!=null){
            ad.dismiss();
            ad=null;
        }
        searchViewMain =null;
        input=null;
        getSupportLoaderManager().destroyLoader(BRAIN_LOADER);

    }

    private void unbindDrawable(Drawable d) {
        if (d != null)
            d.setCallback(null);
    }

    private Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                   int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
