package com.visual.android.superbowlsquares;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.*;
import com.parse.Parse;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Rami on 12/6/2015.
 */
public class ShareGame extends Activity {

    int teamPos = 0;
    ArrayList<String> arrayOfNames = new ArrayList<>();
    String[] Row_Names_EDs = new String[100];
    ArrayList<Integer> numRow = new ArrayList<>();
    ArrayList<Integer> numCol = new ArrayList<>();
    private ParseObject gameData = new ParseObject("GameData");
    ArrayList<String> namesOnBoard = new ArrayList<>();
    String objectId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharegame);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            namesOnBoard = extras.getStringArrayList("NAMES_ON_BOARD");
            teamPos = extras.getInt("TEAM_POS");
            arrayOfNames = extras.getStringArrayList("ARRAY_NAMES");
            //Row_Names_EDs = extras.getStringArray("ROW_NAMES_EDS");
            numRow = extras.getIntegerArrayList("NUM_ROW");
            numCol = extras.getIntegerArrayList("NUM_COL");
        }
        if (numCol == null) numCol = new ArrayList<>();
        if (numRow == null) numRow = new ArrayList<>();
        if (arrayOfNames == null) arrayOfNames = new ArrayList<>();
        if (namesOnBoard == null) namesOnBoard = new ArrayList<>();


        final Button mShareGame = (Button)findViewById(R.id.share);
        final Button mReceiveGame = (Button)findViewById(R.id.receive);
        final TextView mShareText = (TextView)findViewById(R.id.shareText);
        final EditText mReceiveText = (EditText)findViewById(R.id.receiveText);
        TextView backButton = (TextView) findViewById(R.id.backbutton);
        backButton.setText("<");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShareGame.this, MainActivity.class);
                i.putExtra("TEAM_POS", teamPos);
                //i.putExtra("ROW_NAMES_EDS", Row_Names_EDs);
                i.putStringArrayListExtra("NAMES_ON_BOARD", namesOnBoard);
                i.putStringArrayListExtra("ARRAY_NAMES", arrayOfNames);
                i.putIntegerArrayListExtra("NUM_ROW", numRow);
                i.putIntegerArrayListExtra("NUM_COL", numCol);
                startActivity(i);
            }
        });

        mShareGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SHAREGAME", "SHARED");
                gameData.put("Names", arrayOfNames);
                gameData.put("TeamPosition", teamPos);
                gameData.put("Row", numRow);
                gameData.put("Column", numCol);
                gameData.put("NamesOnBoard", namesOnBoard);
                gameData.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // Saved successfully.
                            Log.d("TAG", "User update saved!");
                            String id = gameData.getObjectId();
                            Log.d("TAG", "The object id is: " + id);
                            mShareText.setText(id);
                        } else {
                            // The save failed.
                            Log.d("TAG", "User update error: " + e);
                        }
                    }
                });

            }
        });
        mReceiveGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                objectId = mReceiveText.getText().toString();
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("GameData");
                    query.getInBackground(objectId, new GetCallback<ParseObject>() {
                        public void done(ParseObject object, ParseException e) {
                            if (e == null) {
                                teamPos = object.getInt("TeamPosition");
                                arrayOfNames = (ArrayList<String>) object.get("Names");
                                numRow = (ArrayList<Integer>) object.get("Row");
                                numCol = (ArrayList<Integer>) object.get("Column");
                                namesOnBoard = (ArrayList<String>) object.get("NamesOnBoard");
                                Log.d("CODEFOUND", String.valueOf(teamPos));
                                Intent i = new Intent(ShareGame.this, MainActivity.class);
                                i.putExtra("TEAM_POS", teamPos);
                                //i.putExtra("ROW_NAMES_EDS", Row_Names_EDs);
                                i.putStringArrayListExtra("NAMES_ON_BOARD", namesOnBoard);
                                i.putStringArrayListExtra("ARRAY_NAMES", arrayOfNames);
                                i.putIntegerArrayListExtra("NUM_ROW", numRow);
                                i.putIntegerArrayListExtra("NUM_COL", numCol);
                                startActivity(i);
                            } else {
                                Log.d("CODENOTFOUND", objectId);
                                Toast.makeText(ShareGame.this, "Code not found.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

        });
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(ShareGame.this, MainActivity.class);
        i.putExtra("TEAM_POS", teamPos);
        //i.putExtra("ROW_NAMES_EDS", Row_Names_EDs);
        i.putStringArrayListExtra("NAMES_ON_BOARD", namesOnBoard);
        i.putStringArrayListExtra("ARRAY_NAMES", arrayOfNames);
        i.putIntegerArrayListExtra("NUM_ROW", numRow);
        i.putIntegerArrayListExtra("NUM_COL", numCol);
        startActivity(i);
    }
}
