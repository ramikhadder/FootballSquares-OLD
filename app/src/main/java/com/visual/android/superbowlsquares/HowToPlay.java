package com.visual.android.superbowlsquares;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rami on 11/19/2015.
 */
public class HowToPlay extends Activity {

    ArrayList<String> arrayOfNames = new ArrayList<>();
    String[] Row_Names_EDs = new String[100];
    int teamPos = 0;
    ArrayList<Integer> numRow = new ArrayList<>();
    ArrayList<Integer> numCol = new ArrayList<>();
    ArrayList<String> namesOnBoard = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howtoplay);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            namesOnBoard = extras.getStringArrayList("NAMES_ON_BOARD");
            arrayOfNames = extras.getStringArrayList("ARRAY_NAMES");
            //Row_Names_EDs = extras.getStringArray("ROW_NAMES_EDS");
            teamPos = extras.getInt("TEAM_POS");
            numRow = extras.getIntegerArrayList("NUM_ROW");
            numCol = extras.getIntegerArrayList("NUM_COL");
        }
        if (numCol == null) numCol = new ArrayList<>();
        if (numRow == null) numRow = new ArrayList<>();
        if (arrayOfNames == null) arrayOfNames = new ArrayList<>();
        if (namesOnBoard == null) namesOnBoard = new ArrayList<>();

        Button mGotIt = (Button)findViewById(R.id.gotit);
        mGotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HowToPlay.this, MainActivity.class);
                i.putExtra("TEAM_POS", teamPos);
                i.putStringArrayListExtra("NAMES_ON_BOARD", namesOnBoard);
                //i.putExtra("ROW_NAMES_EDS", Row_Names_EDs);
                i.putStringArrayListExtra("ARRAY_NAMES", arrayOfNames);
                i.putIntegerArrayListExtra("NUM_ROW", numRow);
                i.putIntegerArrayListExtra("NUM_COL", numCol);
                startActivity(i);
            }
        });
    }
}
