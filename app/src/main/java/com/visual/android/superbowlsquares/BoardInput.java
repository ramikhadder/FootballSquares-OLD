package com.visual.android.superbowlsquares;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by Rami on 10/5/2015.
 */
public class BoardInput extends Activity {

    private EditText mUserInput;
    public ArrayList<String> arrayOfNames = new ArrayList<>();
    private ArrayAdapter<String> listViewAdapter;
    private ListView listview;
    private String webSourceCode;
    private TeamSelection teamSelection;
    private Boolean changeBoard;
    public String[] Row_Names_EDs = new String[100];
    ArrayList<Integer> numRow = new ArrayList<>();
    ArrayList<Integer> numCol = new ArrayList<>();
    ArrayList<String> namesOnBoard = new ArrayList<>();
    int teamPos = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boardinput);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            teamPos = extras.getInt("TEAM_POS");
            arrayOfNames = extras.getStringArrayList("ARRAY_NAMES");
            namesOnBoard = extras.getStringArrayList("NAMES_ON_BOARD");
            //Row_Names_EDs = extras.getStringArray("ROW_NAMES_EDS");
            numRow = extras.getIntegerArrayList("NUM_ROW");
            numCol = extras.getIntegerArrayList("NUM_COL");
        }
        if (numCol == null) numCol = new ArrayList<>();
        if (numRow == null) numRow = new ArrayList<>();
        if (arrayOfNames == null) arrayOfNames = new ArrayList<>();
        if (namesOnBoard == null) namesOnBoard = new ArrayList<>();

        //adapter = new CustomAdapter(BoardInput.this, arrayOfNames);
        listViewAdapter = new ArrayAdapter<>(this, R.layout.item_namechoices, R.id.nameItem, arrayOfNames);
        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(listViewAdapter);


        mUserInput = (EditText)findViewById(R.id.nameInput);
        Button mConfirm = (Button)findViewById(R.id.addButton);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUserInput.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter a name.",
                            Toast.LENGTH_SHORT).show();

                } else {
                    arrayOfNames.add(0, mUserInput.getText().toString());
                    mUserInput.setText("");
                    listview.setAdapter(listViewAdapter);
                }

            }
        });
        Button mRemove = (Button)findViewById(R.id.removeButton);
        mRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayOfNames.size() > 0) {
                    arrayOfNames.remove(0);
                    listview.setAdapter(listViewAdapter);
                }
            }
        });
        mRemove.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mUserInput.setText("");
                Log.d("LONG", "CLICKED");
                arrayOfNames.clear();
                listview.setAdapter(listViewAdapter);
                return true;
            }
        });
        Button mCancel = (Button)findViewById(R.id.cancelButton);
        mCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(BoardInput.this, TeamSelection.class);
                i.putExtra("TEAM_POS", teamPos);
                i.putStringArrayListExtra("ARRAY_NAMES", arrayOfNames);
                //i.putExtra("ROW_NAMES_EDS", Row_Names_EDs);
                i.putStringArrayListExtra("NAMES_ON_BOARD", namesOnBoard);
                i.putIntegerArrayListExtra("NUM_ROW", numRow);
                i.putIntegerArrayListExtra("NUM_COL", numCol);
                startActivity(i);
            }
        });

        Button mReady = (Button)findViewById(R.id.readyButton);
        mReady.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (!arrayOfNames.isEmpty()) {
                    Intent i = new Intent(BoardInput.this, MainActivitySetUp.class);
                    i.putStringArrayListExtra("ARRAY_NAMES", arrayOfNames);
                    i.putExtra("TEAM_POS", teamPos);
                    //i.putExtra("ROW_NAMES_EDS", Row_Names_EDs);
                    i.putStringArrayListExtra("NAMES_ON_BOARD", namesOnBoard);
                    i.putIntegerArrayListExtra("NUM_ROW", numRow);
                    i.putIntegerArrayListExtra("NUM_COL", numCol);
                    startActivity(i);
                }
                else{
                    Toast.makeText(BoardInput.this, "Please enter a name.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onBackPressed(){
        Intent i = new Intent(BoardInput.this, TeamSelection.class);
        i.putExtra("TEAM_POS", teamPos);
        i.putStringArrayListExtra("ARRAY_NAMES", arrayOfNames);
        //i.putExtra("ROW_NAMES_EDS", Row_Names_EDs);
        i.putStringArrayListExtra("NAMES_ON_BOARD", namesOnBoard);
        i.putIntegerArrayListExtra("NUM_ROW", numRow);
        i.putIntegerArrayListExtra("NUM_COL", numCol);
        startActivity(i);
    }

}
