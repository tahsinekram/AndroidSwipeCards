package com.androidtutorialpoint.androidswipecards;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidtutorialpoint.androidswipecards.data.AppContract;
/**
 * Created by tahsi on 12/29/2016.
 */

public class AnswerActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private BrainCursorAdapter qCursorAdapter;
    private String rowSelect,texting,quest;
    private EditText editAnswer;
    private SearchView searchView;
    private AlertDialog ad;
    private ContentValues values;
    //Toolbar toolbar;
    private ListView qaView;
    private static final int ANSWER_LOADER = 0;
    private static final String[] projection = {
            AppContract.BrainEntry._ANSWERID,
            AppContract.BrainEntry.COLUMN_QUESTION,
            AppContract.BrainEntry.COLUMN_ANSWER};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editAnswer=new EditText(this);
        values = new ContentValues();
        ad=dialogBuilder(this);
        Intent intent = getIntent();
        rowSelect = intent.getExtras().getString("qdata");
        setContentView(R.layout.qa_list);
        qCursorAdapter=new BrainCursorAdapter(getApplicationContext(),null,2);
        qaView= (ListView) findViewById(R.id.Question);
        qaView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor ans=(Cursor)qCursorAdapter.getItem(i);
                texting=((Cursor)qCursorAdapter.getItem(i)).getString(ans.getColumnIndex(AppContract.BrainEntry.COLUMN_ANSWER));
                quest=((TextView) view.findViewById(R.id.challenge)).getText().toString().trim();
                Toast.makeText(getApplicationContext(),texting,Toast.LENGTH_LONG).show();
                editAnswer.setText(texting);
                ad.show();
            }
        });
        qaView.setAdapter(qCursorAdapter);
        getSupportLoaderManager().initLoader(ANSWER_LOADER,null,this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        final String [] selection={AppContract.BrainEntry._ANSWERID,AppContract.BrainEntry.COLUMN_QUESTION};
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.search,menu);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText!=null){
                    String whereClause= AppContract.BrainEntry.COLUMN_QUESTION +" LIKE '%" + newText +"%' COLLATE NOCASE";
                    Cursor c = getContentResolver().query(AppContract.BrainEntry.CONTENT_URI_ANSWER,selection,whereClause,null,null);
                    qCursorAdapter.swapCursor(c);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        String whereClause= AppContract.BrainEntry.COLUMN_KEY +" LIKE '%" + rowSelect +"%' COLLATE NOCASE";
        //String []arg=new String[]{rowSelect};

        return new CursorLoader(this,   // Parent activity context
                AppContract.BrainEntry.CONTENT_URI_ANSWER,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                whereClause,                   // No selection clause
                null,                 // No selection arguments
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        qCursorAdapter.swapCursor(data);
    }


    @Override
    public void onLoaderReset(Loader loader) {
        qCursorAdapter.swapCursor(null);
    }


    private AlertDialog dialogBuilder(Context ctx){
        AlertDialog dialog;
        AlertDialog.Builder build;
        build = new AlertDialog.Builder(ctx);
        build.setTitle("Answer");
        build.setIcon(R.drawable.head);
        build.setView(editAnswer);
        build.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(getApplicationContext(),ans,Toast.LENGTH_LONG).show();
                values.put(AppContract.BrainEntry.COLUMN_ANSWER, editAnswer.getText().toString().trim());
                String answerClause = AppContract.BrainEntry.COLUMN_QUESTION + " LIKE '%" + quest + "%' COLLATE NOCASE";
                int answerupdate=getContentResolver().update(AppContract.BrainEntry.CONTENT_URI_ANSWER,values,answerClause,null);
                values.clear();
                if (answerupdate == 0) {
                    // If no rows were deleted, then there was an error with the delete.
                    Toast.makeText(getApplicationContext(), "answer doesn't work", Toast.LENGTH_LONG).show();
                } else {
                    // Otherwise, the delete was successful and we can display a toast.
                    Toast.makeText(getApplicationContext(), "answer works",Toast.LENGTH_LONG).show();
                }
                dialogInterface.dismiss();
            }
        });

        build.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog=build.create();

        return dialog;
    }

    @Override
    protected void onDestroy(){
        if(ad!=null){
            ad.dismiss();
            ad=null;
        }
        qaView=null;
        rowSelect=null;
        texting=null;
        quest=null;
        editAnswer=null;
        searchView=null;
        super.onDestroy();
    }
}
