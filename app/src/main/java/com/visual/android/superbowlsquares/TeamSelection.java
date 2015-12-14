package com.visual.android.superbowlsquares;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Rami on 11/26/2015.
 */
public class TeamSelection extends Activity {

    private String webSourceCode;
    private ArrayList<String> arrayOfTeams = new ArrayList<>();
    private Parse parse;
    private TeamSelectionAdapter teamSelectionAdapter;
    Boolean ready = false;
    public ArrayList<String> arrayOfNames = new ArrayList<>();
    public String[] Row_Names_EDs = new String[100];
    ArrayList<Integer> numRow = new ArrayList<>();
    ArrayList<Integer> numCol = new ArrayList<>();
    ArrayList<String> namesOnBoard = new ArrayList<>();
    int teamPos = 0;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SwipeRefreshLayout mSwipreRefreshLayoutEmpty;
    MyAsyncTask myTask;
    private Boolean parseQ = true;
    ScrollView emp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teamnameselection);


        Bundle extras = getIntent().getExtras();
        Intent i = getIntent();
        System.out.println(i);
        System.out.println(extras);
        if (extras != null) {
            String checkPrevActivity = extras.getString("ClassName");
            if (checkPrevActivity == null) {
                Log.d("Class name", "not splash");
                arrayOfTeams = extras.getStringArrayList("ARRAY_TEAMS");
                teamPos = extras.getInt("TEAM_POS");
                arrayOfNames = extras.getStringArrayList("ARRAY_NAMES");
                //Row_Names_EDs = extras.getStringArray("ROW_NAMES_EDS");
                namesOnBoard = extras.getStringArrayList("NAMES_ON_BOARD");
                numRow = extras.getIntegerArrayList("NUM_ROW");
                numCol = extras.getIntegerArrayList("NUM_COL");
            }
        }
        if (numCol == null) numCol = new ArrayList<>();
        if (numRow == null) numRow = new ArrayList<>();
        if (arrayOfTeams == null) arrayOfTeams = new ArrayList<>();
        if (arrayOfNames == null) arrayOfNames = new ArrayList<>();
        if (namesOnBoard == null) namesOnBoard = new ArrayList<>();


        parse = new Parse();
        //parse.new MyxAsyncTask().execute();
        //webSourceCode = parse.getWebSourceCode();
        Log.d("TeamSelection", "Confirmed");
        myTask = new MyAsyncTask();
        if (arrayOfTeams.isEmpty()){
            Log.d("TEAMSELECTIONBEGIN", "executed");
            myTask.execute();
        }
        //myTask.execute();
        //new MyAsyncTask().execute();

        emp = (ScrollView) findViewById(R.id.emptyscroll);

        TextView empty = (TextView) findViewById(R.id.emptyelement);
        ListView lv = (ListView) findViewById(R.id.listviewts);
        teamSelectionAdapter = new TeamSelectionAdapter(TeamSelection.this, arrayOfTeams);
        lv.setEmptyView(emp);
        lv.setAdapter(teamSelectionAdapter);


        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(R.color.darkmetalblue, R.color.freeze);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("REFRESH", "true");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!myTask.isCancelled() && myTask.getStatus() != AsyncTask.Status.RUNNING) {
                            Log.d("TEAMSELECTION", "ASYNC MADE IT");
                            myTask.cancel(true);
                            myTask = new MyAsyncTask();
                            myTask.execute();
                        }
                    }
                }, 500);
            }
        });

        Button receive = (Button) findViewById(R.id.receiveButton);
        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiveGame();
            }
        });

        Button confirm = (Button) findViewById(R.id.confirmbutton);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayOfTeams.size() > 0) {
                    ready = teamSelectionAdapter.getReady();
                    teamPos = teamSelectionAdapter.getPos();
                }
                if (ready == true || arrayOfTeams.size() <= 0) {
                    Intent i = new Intent(TeamSelection.this, BoardInput.class);
                    i.putExtra("TEAM_POS", teamPos);
                    i.putStringArrayListExtra("ARRAY_NAMES", arrayOfNames);
                    //null exception on array namesOnBoard
                    if (namesOnBoard.isEmpty()) {
                        for (int z = 0; z < 100; z++) {
                            namesOnBoard.add("");
                        }
                    }
                    i.putStringArrayListExtra("NAMES_ON_BOARD", namesOnBoard);
                    //i.putExtra("ROW_NAMES_EDS", Row_Names_EDs);
                    i.putIntegerArrayListExtra("NUM_ROW", numRow);
                    i.putIntegerArrayListExtra("NUM_COL", numCol);
                    startActivity(i);
                } else {
                    Toast.makeText(TeamSelection.this, "Please select a game to track.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private class MyAsyncTask extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... params) {
            StringBuilder sb = new StringBuilder();
            try {
                URL nfl = new URL("http://www.cbssports.com/nfl/scoreboard?nocache=" + new Date().getTime());
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                nfl.openStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    sb.append(inputLine);
                }
                in.close();
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String webSourceCode = sb.toString();

            Log.d("TeamSelection", "AsyncTask doen");

            return webSourceCode;
        }

        protected void onPostExecute(String webCode) {

                arrayOfTeams = parse.getTeamNames(webCode);
                System.out.println(arrayOfTeams);
                Log.d("TeamSelection", "AsyncTaskSurvived");
                TextView empty = (TextView) findViewById(R.id.emptyelement);
                ListView lv = (ListView) findViewById(R.id.listviewts);
                teamSelectionAdapter = new TeamSelectionAdapter(TeamSelection.this, arrayOfTeams);
                System.out.println(arrayOfNames);
                lv.setEmptyView(emp);
                lv.setAdapter(teamSelectionAdapter);
                mSwipeRefreshLayout.setRefreshing(false);

        }
    }

    String objectId;
    private void receiveGame() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TeamSelection.this);
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("GameData");
        LayoutInflater inflater = getLayoutInflater();
        View listviewLayout = inflater.inflate(R.layout.item_receivegame, null);
        final EditText mGetCode = (EditText) listviewLayout.findViewById(R.id.tsgetcode);
        mGetCode.setHint("Enter Code");

        alertDialogBuilder.setPositiveButton("Receieve", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                query.cancel();
                parseQ = true;
                dialog.cancel();
            }
        });

        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.setTitle("Receive Game");
        dialog.setMessage("Enter the code given to you in the box below.");
        dialog.setView(listviewLayout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (namesOnBoard.isEmpty()) {
                    for (int z = 0; z < 100; z++) {
                        namesOnBoard.add("");
                    }
                }
                if (parseQ) {
                    parseQ = false;
                    Log.d("QUERY", "executed");
                    objectId = mGetCode.getText().toString();
                    //final ParseQuery<ParseObject> query = ParseQuery.getQuery("GameData");
                    query.getInBackground(objectId, new GetCallback<ParseObject>() {
                        public void done(ParseObject object, ParseException e) {
                            if (e == null) {
                                teamPos = object.getInt("TeamPosition");
                                arrayOfNames = (ArrayList<String>) object.get("Names");
                                numRow = (ArrayList<Integer>) object.get("Row");
                                numCol = (ArrayList<Integer>) object.get("Column");
                                namesOnBoard = (ArrayList<String>) object.get("NamesOnBoard");
                                Log.d("CODEFOUND", String.valueOf(teamPos));
                                Intent i = new Intent(TeamSelection.this, MainActivity.class);
                                i.putExtra("TEAM_POS", teamPos);
                                //i.putExtra("ROW_NAMES_EDS", Row_Names_EDs);
                                i.putStringArrayListExtra("NAMES_ON_BOARD", namesOnBoard);
                                i.putStringArrayListExtra("ARRAY_NAMES", arrayOfNames);
                                i.putIntegerArrayListExtra("NUM_ROW", numRow);
                                i.putIntegerArrayListExtra("NUM_COL", numCol);
                                parseQ = true;
                                startActivity(i);
                                dialog.cancel();
                            } else {
                                Log.d("CODENOTFOUND", objectId);
                                Toast.makeText(TeamSelection.this, "Code not found.", Toast.LENGTH_SHORT).show();
                                parseQ = true;
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onBackPressed(){
        if (!arrayOfNames.isEmpty()) {
            Log.d("TEAMSELECTION", "INTENT");
            Intent i = new Intent(TeamSelection.this, MainActivity.class);
            i.putExtra("TEAM_POS", teamPos);
            i.putStringArrayListExtra("ARRAY_NAMES", arrayOfNames);
            //i.putExtra("ROW_NAMES_EDS", Row_Names_EDs);
            i.putStringArrayListExtra("NAMES_ON_BOARD", namesOnBoard);
            i.putIntegerArrayListExtra("NUM_ROW", numRow);
            i.putIntegerArrayListExtra("NUM_COL", numCol);
            startActivity(i);
        }
        else{
            Log.d("TEAMSELECTION", "FINISH");
            finish();
        }
    }
}
