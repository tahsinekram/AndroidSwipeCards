package com.androidtutorialpoint.androidswipecards;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidtutorialpoint.androidswipecards.data.AppContract;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity {

    private MyAppAdapter myAppAdapter;
    private ViewHolder viewHolder;
    private ArrayList<Data> array;
    private SwipeFlingAdapterView flingContainer;
    private String dataInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        dataInfo = intent.getExtras().getString("data");
        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.activity_swipe_cards);
        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        array = new ArrayList<>();
        array.add(new Data(null,"Why is it necessary to solve the problem?",dataInfo));
        array.add(new Data(null,"What benefits will you gain by solving the problem?",dataInfo));
        array.add(new Data(null,"What is the unknown?",dataInfo));
        array.add(new Data(null,"What is it you don't yet understand?",dataInfo));
        array.add(new Data(null,"What is the information you have?",dataInfo));
        array.add(new Data(null,"What isn't the problem?",dataInfo));
        array.add(new Data(null,"Is the information sufficient, insufficient, redundant or contradictory",dataInfo));
        array.add(new Data(null,"Should you draw a diagram of the problem?",dataInfo));
        array.add(new Data(null,"What are the boundaries of the problem?",dataInfo));
        array.add(new Data(null,"Can you seperate the various parts of the problem?",dataInfo));
        array.add(new Data(null,"What are the relationships of the parts of the problem?",dataInfo));
        array.add(new Data(null,"What are the constraints of the problem?",dataInfo));
        array.add(new Data(null,"Have you seen this problem before?",dataInfo));
        array.add(new Data(null,"Have you seen this problem in a slightly different form?",dataInfo));
        array.add(new Data(null,"Do you know a related problem?",dataInfo));
        array.add(new Data(null,"Try to think and write of a familiar problem having the same or similar unknown",dataInfo));
        array.add(new Data(null,"Suppose you find a problem related to yours that has already been solved. Can you use its method?",dataInfo));
        array.add(new Data(null,"What are some different ways you can restate your problem?",dataInfo));
        array.add(new Data(null,"Can the rules be changed?",dataInfo));
        array.add(new Data(null,"What are the best, worst and most probable cases you can imagine?",dataInfo));
        array.add(new Data(null,"Can you solve the whole problem or part of the problem?",dataInfo));
        array.add(new Data(null,"Can you picture the resolution of the problem?",dataInfo));
        array.add(new Data(null,"How much of the unknown can you determine?",dataInfo));
        array.add(new Data(null,"Can you derive something useful from the information you have?",dataInfo));
        array.add(new Data(null,"Have you used all the information?",dataInfo));
        array.add(new Data(null,"Have you taken into account all the essential notions of the problem?",dataInfo));
        array.add(new Data(null,"Can you seperate the steps in the problem-solving process?",dataInfo));
        array.add(new Data(null,"Can you determine the correctness of each step?",dataInfo));

        myAppAdapter = new MyAppAdapter(array, getApplicationContext());
        flingContainer.setAdapter(myAppAdapter);
        flingContainer.setMaxVisible(1);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {

            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                array.add(array.remove(0));
                myAppAdapter.notifyDataSetChanged();
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                if(viewHolder.DataText.getText().toString().trim()!=null && viewHolder.answerText.getText().toString().trim()!=null) {
                    //saveData(quest);
                    //Toast.makeText(QuestionsActivity.this,"Saving Item"+viewHolder.answerText.getText().toString().trim(),Toast.LENGTH_SHORT).show();
                    Save(dataInfo.trim(),viewHolder.DataText.getText().toString().trim(),viewHolder.answerText.getText().toString().trim());
                }
                array.remove(0);
                myAppAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });

    }

    private static class ViewHolder {
        public TextView DataText;
        public TextView ChallengeText;
        public EditText answerText;
    }

    private class MyAppAdapter extends BaseAdapter {


        private List<Data> parkingList;
        private Context context;

        private MyAppAdapter(List<Data> apps, Context context) {
            this.parkingList = apps;
            this.context = context;
        }

        @Override
        public int getCount() {
            return parkingList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View rowView = convertView;

            if (rowView == null) {

                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.item, parent, false);
                // configure view holder
                viewHolder = new ViewHolder();
                viewHolder.DataText = (TextView) rowView.findViewById(R.id.bookText);
                viewHolder.ChallengeText = (TextView) rowView.findViewById(R.id.header);
                //viewHolder.cardImage = (ImageView) rowView.findViewById(R.id.cardImage);
                viewHolder.answerText=(EditText)rowView.findViewById(R.id.edit);
                rowView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.DataText.setText(parkingList.get(position).getDescription() + "");
            viewHolder.ChallengeText.setText(parkingList.get(position).getChallenge() + "");
            return rowView;
        }
    }

    private void Save(String challenge,String quest,String ans){
            ContentValues values = new ContentValues();
            values.put(AppContract.BrainEntry.COLUMN_KEY, challenge);
            values.put(AppContract.BrainEntry.COLUMN_QUESTION, quest);
            values.put(AppContract.BrainEntry.COLUMN_ANSWER, ans);
            Uri newUri = getContentResolver().insert(AppContract.BrainEntry.CONTENT_URI_ANSWER, values);
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(getApplicationContext(), "error with insertion",
                        Toast.LENGTH_SHORT).show();
            }
        }

    @Override
    protected void onStop(){
        super.onStop();
        array.clear();
        flingContainer.setAdapter(null);
        flingContainer=null;
        myAppAdapter=null;
    }

}
