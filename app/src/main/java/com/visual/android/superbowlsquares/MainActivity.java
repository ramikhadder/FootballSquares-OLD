package com.visual.android.superbowlsquares;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.widget.Toast;

public class MainActivity extends Activity {

    private int ScoreOne, ScoreTwo, S1PlaceHolder, S2PlaceHolder;
    private List<Integer> list = new ArrayList<Integer>();
    private List<Integer> list2 = new ArrayList<Integer>();
    private Button mReset;

    private TableLayout layout;
    private TableRow rowZ;

    TextView[] Row_One_Column = new TextView[10];
    TextView[] Column_One_Row = new TextView[10];
    TextView[][] Row_EDs = new TextView[10][10];
    TableRow[] Rows = new TableRow[11];
    int[] mNumberRows = new int[10];
    int[] mNumberColumns = new int[10];

    private ArrayList<Names> arrayOfNames = new ArrayList<Names>();
    private List<String> xferNames = new ArrayList<>();
    private ArrayList<TeamNames> arrayOfTeams = new ArrayList<>();
    private List<String> xferTeams = new ArrayList<>();
    private TeamSelectionAdapter teamSelectionAdapter;
    private CustomAdapter adapter;

    private Random rand, rando;

    private EditText mUserInput;
    private String selectedName;

    private String webSourceCode, checkBoxResult;
    private CheckBox checkBox;
    private static final String PREFS_NAME = "MyPrefsFile1";

    private int count = 0;
    private int webSourceCodeNum = 0;

    private List<String> bufferTeamNames = new ArrayList<>();

    private Boolean displayInput = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        welcomeDialog();

        initializeInfo();

        //create a handler in an activity or fragment
        Handler handler = new Handler();
        //create a timer task and pass the handler in
        CustomTimerTask task = new CustomTimerTask(handler);
        //use timer to run the task every 10 seconds
        //new Timer().scheduleAtFixedRate(task, 0, 30000);
        new MyAsyncTask().execute();

        //shuffles the two arrays
        shuffleNumbers();

        //sets the shuffled numbers in the rows and columns
        setNumbers();

        for (int i = 0; i < 10; i++){
            for (int y = 0; y < 10; y++) {
                Row_EDs[i][y].setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        //adapter = new CustomAdapter(MainActivity.this, arrayOfNames);
                        //v.setBackgroundColor(Color.WHITE);
                        TextView t = (TextView) v;
                        t.setText(selectedName);
                    }
                });
            }
        }

        mReset = (Button)findViewById(R.id.reset);
        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainActivity.this, mReset);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Setup Board")){
                            setUpBoard();
                        }
                        if (item.getTitle().equals("Select Game")){
                            showTeams();
                        }
                        if (item.getTitle().equals("Show Team Positions")){
                            String teamNameOne = "";
                            String teamNameTwo = "";
                            if (!bufferTeamNames.isEmpty()) {
                                teamNameOne = bufferTeamNames.get((2 * webSourceCodeNum));
                                teamNameTwo = bufferTeamNames.get((2 * webSourceCodeNum) + 1);
                            }
                            if ((teamNameOne.equals("") && teamNameTwo.equals(""))){
                                Toast.makeText(getApplicationContext(), "No games found in the current week.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), teamNameOne + " (Row) vs. " + teamNameTwo + " (Column)",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (item.getTitle().equals("Reset Board")){
                            clearBoard();
                            resetWinner();
                            shuffleNumbers();
                            setNumbers();
                        }
                        if (item.getTitle().equals("How to Play")){
                            howToPlay();
                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        });
        mReset.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return true;
            }
        });

    }
    public void showWinner(){
        //I added 1 because the Button isn't apart of the array
        rowZ = (TableRow)layout.getChildAt(1 + list2.indexOf(ScoreTwo));
        rowZ.getChildAt(1 + list.indexOf(ScoreOne)).setBackgroundColor(Color.YELLOW);

    }
    private void setNumbers(){
        for (int i = 0; i < 10; i++){
            Row_One_Column[i].setText(String.valueOf(mNumberRows[i]));
            Column_One_Row[i].setText(String.valueOf(mNumberColumns[i]));
        }
    }
    private void clearBoard(){
        for (int i = 0; i < 10; i++){
            for (int y = 0; y < 10; y++) {
                Row_EDs[i][y].setText("");
            }
        }
    }
    private void shuffleNumbers(){
        //Random number generator for row
        Collections.shuffle(list);
        for (int a = 0; a < 10; a++){
            mNumberRows[a] = list.get(a);
        }

        //Random number generator for column
        Collections.shuffle(list2);
        for (int a = 0; a < 10; a++){
            mNumberColumns[a] = list2.get(a);
        }
    }
    private void initializeInfo(){
        layout = (TableLayout) findViewById(R.id.table);
        for (int i = 0; i < 11; i++){
            Rows[i] = (TableRow)layout.getChildAt(i);
        }

        //sets the ints of the arrays equal to each respective child
        for (int i = 0; i < 10; i++){
            Row_One_Column[i] = (TextView)Rows[0].getChildAt(i + 1);
            Column_One_Row[i] = (TextView)Rows[i+1].getChildAt(0);
            for (int y = 0; y < 10; y++) {
                Row_EDs[i][y] = (TextView) Rows[i+1].getChildAt(y+1);
            }
            //creates two list arrays using numbers 0-9
            list.add(i);
            list2.add(i);
        }
    }
    private void testUpdateScore(){
        rand = new Random();
        rando = new Random();
        ScoreOne = rand.nextInt(9);
        ScoreTwo = rando.nextInt(9);
    }
    private void clearOldWinner(){
        rowZ = (TableRow)layout.getChildAt(1 + list2.indexOf(S2PlaceHolder));
        rowZ.getChildAt(1 + list.indexOf(S1PlaceHolder)).setBackgroundColor(Color.WHITE);
    }
    private void resetWinner(){
        rowZ = (TableRow)layout.getChildAt(1 + list2.indexOf(ScoreTwo));
        rowZ.getChildAt(1 + list.indexOf(ScoreOne)).setBackgroundColor(Color.WHITE);
    }
    private boolean isNumeric(String s){
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, List<Integer>> {
        protected List<Integer> doInBackground(Void... params) {
            StringBuilder sb = new StringBuilder();
            try {
                URL nfl = new URL("http://www.cbssports.com/nfl/scoreboard?nocache="+new Date().getTime());
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
            webSourceCode = sb.toString();
            List<Integer> scores = new ArrayList<Integer>();
            //Keywords for score
            String str1 = "<td class=\"finalScore\">";
            String str2 = ")</td><td class=\"gameOdds\">";
            String str4 = "</td><td class=\"teamLabel\">";

            try {
                Log.d("Sleep", "sleep");
                Thread.sleep(2500);
            }
            catch (InterruptedException e){
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            arrayOfTeams.clear();
            bufferTeamNames = new ArrayList<>();
            int test = 0;
            int start = 0;
            while (true) {
                int found = webSourceCode.indexOf(str4, start);
                if (found != -1) {
                    String teamName = "";
                    int zz = teamName.indexOf("<");
                    for(int i = 0; zz == -1; i++){
                        teamName = webSourceCode.substring(found+str4.length(), found+str4.length() + i);
                        zz = teamName.indexOf("<");
                    }
                    if (!teamName.contains("'") && !teamName.equals("")){
                        teamName = teamName.substring(0, teamName.length() - 1);
                        //Log.d("Team Name: ", teamName);
                        bufferTeamNames.add(teamName);
                        test++;
                        if (test%2 == 0){
                            //System.out.println(bufferTeamNames.get(test-2) + " vs. " + bufferTeamNames.get(test-1));
                            arrayOfTeams.add(new TeamNames(bufferTeamNames.get(test-2) + " vs. " + bufferTeamNames.get(test-1)));
                        }
                    }
                    else {
                        Log.d("Error(name)", "contains ' or empty");
                    }

                }
                if (found == -1) {
                    break;
                }
                start = found + 2;  // move start up for next iteration
            }
            List<Integer> scoree = new ArrayList<>();
            start = 0;
            int mStart = 0;
            while (true) {
                int found = webSourceCode.indexOf(str1, start);
                int mFound = webSourceCode.indexOf(str2, mStart);
                if (mFound == -1){
                    mFound = found+1;
                }
                if (found < mFound) {
                    if (found != -1) {
                        String score = "";
                        int zz = score.indexOf("<");
                        for (int i = 0; zz == -1; i++) {
                            score = webSourceCode.substring(found + str1.length(), found + str1.length() + i);
                            zz = score.indexOf("<");
                        }
                        if (!score.contains("'") && !score.equals("")) {
                            score = score.substring(0, score.length() - 1);
                            Log.d("score", score);
                            if (Integer.valueOf(score) != null) {
                                scoree.add(Integer.valueOf(score));
                            }
                        } else {
                            Log.d("Error(score)", "contains ' or empty");
                        }
                    }
                    if (found == -1) break;
                    start = found + 2;  // move start up for next iteration
                }
                else{
                    if (mFound != -1) {
                        scoree.add(0);
                        scoree.add(0);
                        Log.d("score", "0");
                        Log.d("score", "0");
                    }
                    mStart = mFound + 2;  // move start up for next iteration
                }
            }

            //adding both scores to a listarray
            if (scoree.size() > (2*webSourceCodeNum) && !scoree.isEmpty()) {
                Log.d("Added", "allowed");
                scores.add(scoree.get((2 * webSourceCodeNum)));
                scores.add(scoree.get((2 * webSourceCodeNum) + 1));
            }
            else{
                Log.d("Added", "declined");
                scores.add(0);
                scores.add(0);
            }
            return scores;
        }
        protected void onPostExecute(List<Integer> listScore) {
            Log.d("post", "execute");
            //used to get the last integer of each score
            ScoreOne = listScore.get(0)%10;
            ScoreTwo = listScore.get(1)%10;
            Log.d("Score One", String.valueOf(ScoreOne));
            Log.d("Score Two", String.valueOf(ScoreTwo));
            //these if/else statements prevents the app from flickering everytime it updates
            if (S1PlaceHolder == ScoreOne && S2PlaceHolder == ScoreTwo){
                Log.d("Are they equal?", "true");
                showWinner();
            }
            else{
                Log.d("Are they equal?", "false");
                clearOldWinner();
                showWinner();
            }
            //recursion
            count++;
            Log.d("Recursion",String.valueOf(count));
            new MyAsyncTask().execute();
            //placeholders for the score before they change
            S1PlaceHolder = ScoreOne;
            S2PlaceHolder = ScoreTwo;
        }
    }
    private class CustomTimerTask extends TimerTask{
        private Handler handler;
        public CustomTimerTask(Handler h) {this.handler = h;}

        @Override
        public void run() {
            //run the code you want to run
            Log.d("Updated", "It's been 10 seconds");
            //update UI using the handler
            handler.post(new Runnable() {
                public void run() {
                    //executes the task every 10 seconds
                    //new MyAsyncTask().execute();
                    //placeholders for the score before they change
                    S1PlaceHolder = ScoreOne;
                    S2PlaceHolder = ScoreTwo;
                }
            });
        }

    }
    private void setUpBoard(){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        final AlertDialog dialog = alertDialogBuilder.create();
        LayoutInflater inflater = getLayoutInflater();
        View listviewLayout = inflater.inflate(R.layout.activity_boardinput, null);
        dialog.setTitle("Board Set-up");
        final ListView lv = (ListView) listviewLayout.findViewById(R.id.listview);
        final TextView empty = (TextView) listviewLayout.findViewById(R.id.emptyelement);
        dialog.setView(listviewLayout);
        lv.setEmptyView(empty);

        adapter = new CustomAdapter(MainActivity.this, arrayOfNames);
        lv.setAdapter(adapter);
        mUserInput = (EditText) listviewLayout.findViewById(R.id.nameInput);
        Button mConfirm = (Button) listviewLayout.findViewById(R.id.addButton);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUserInput.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter a name.",
                            Toast.LENGTH_SHORT).show();

                } else {
                    arrayOfNames.add(new Names(mUserInput.getText().toString()));
                    mUserInput.setText("");
                    lv.setAdapter(adapter);
                }

            }
        });
        Button mRemove = (Button) listviewLayout.findViewById(R.id.removeButton);
        mRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayOfNames.size() > 0) {
                    arrayOfNames.remove(arrayOfNames.get(arrayOfNames.size() - 1));
                    lv.setAdapter(adapter);
                }
            }
        });
        mRemove.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mUserInput.setText("");
                Log.d("LONG", "CLICKED");
                arrayOfNames.clear();
                lv.setAdapter(adapter);
                return true;
            }
        });
        Button mCancel = (Button) listviewLayout.findViewById(R.id.cancelButton);
        mCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialog.cancel();
            }
        });

        Button mReady = (Button) listviewLayout.findViewById(R.id.readyButton);
        mReady.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (adapter.getSelectedName().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please select a name.", Toast.LENGTH_SHORT).show();
                } else {
                    selectedName = adapter.getSelectedName();
                    dialog.cancel();
                    Toast.makeText(MainActivity.this, "Click any box to input the name.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }
    private void welcomeDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View welcomeLayout = inflater.inflate(R.layout.activity_checkbox, null);
        checkBox = (CheckBox)welcomeLayout.findViewById(R.id.dontShow);
        alertDialogBuilder.setView(welcomeLayout);
        alertDialogBuilder.setTitle("Welcome to Football Squares!");
        String test = "<b>Click the \"+\" button to see more options</b>";
        TextView mWelcomeText = (TextView) welcomeLayout.findViewById(R.id.welcomeText);
        mWelcomeText.setText(Html.fromHtml(test) + "\n\n\u2022 \"Setup Board\" to enter everyone's names onto the board."
                + "\n\u2022 \"Select Game\" allows you to choose which Football game to use scores from.\n\u2022 \"Team Position\" will display which team is on the row and which is on the column."
                + "\n\u2022 Lastly, \"Reset Board\" will clear all names and generate new numbers for the row and column\n\nNeed more help? Click \"How to Play.\"");//
        /*alertDialogBuilder.setMessage(Html.fromHtml(test) + "\n\n\u2022 \"Setup Board\" to enter everyone's names onto the board."
                + "\n\u2022 \"Select Game\" allows you to choose which Football game to use scores from.\n\u2022 \"Team Position\" will display which team is on the row and which is on the column."
                + "\n\u2022 Lastly, \"Reset Board\" will clear all names and generate new numbers for the row and column\n\nNeed more help? Click \"How to Play.\"");//*/
        checkBoxResult = "NOT checked";
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (checkBox.isChecked()) {
                    Log.d("Check", "is checked");
                    checkBoxResult = "checked";
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("skipMessage", checkBoxResult);
                    // Commit the edits!
                    editor.commit();

                }
                System.out.println("WORKING");
            }
        });
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String skipMessage = settings.getString("skipMessage", "NOT checked");
        if (!skipMessage.equalsIgnoreCase("checked")) {
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
    private void showTeams() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        final AlertDialog dialog = alertDialogBuilder.create();
        LayoutInflater inflater = getLayoutInflater();
        View listviewLayout = inflater.inflate(R.layout.activity_teamnameselection, null);
        dialog.setTitle("Available Games");
        final ListView lv = (ListView) listviewLayout.findViewById(R.id.listviewts);
        final TextView empty = (TextView) listviewLayout.findViewById(R.id.emptyelement);
        lv.setEmptyView(empty);
        dialog.setView(listviewLayout);

        teamSelectionAdapter = new TeamSelectionAdapter(MainActivity.this, arrayOfTeams);
        lv.setAdapter(teamSelectionAdapter);
        Button mConfirm = (Button) listviewLayout.findViewById(R.id.confirmbutton);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webSourceCodeNum = teamSelectionAdapter.getPos();
                dialog.cancel();
                Log.d("Position", String.valueOf(webSourceCodeNum));
            }
        });
        dialog.show();
    }
    private void howToPlay(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = alertDialogBuilder.create();
        LayoutInflater inflater = getLayoutInflater();
        View listviewLayout = inflater.inflate(R.layout.testhtp2, null);
        dialog.setTitle("How to Play");
        dialog.setView(listviewLayout);
        TextView mIntro = (TextView) listviewLayout.findViewById(R.id.intro);
        mIntro.setText("Grab your friends and family and decide on a game to track from the \"Select Game\" option in the drop-down menu." +
                " All the games listed are the games from the current week, regardless of whether or not they happened or will happen." +
                " Next, click use the \"Setup Board\" option to enter participant names. There are 100 squares so it's recommended to split them equally to have a fair game.");
        TextView mTextOne = (TextView) listviewLayout.findViewById(R.id.text1);
        mTextOne.setText("The numbers in the row and column are randomly generated, ranging from 0 to 9. The numbers correspond to the " +
                "last digit of the football score. The row and the column are assigned to each football team. To see which team belongs to which"
                + "click on \"Show Team Positions\" in the drop-down menu.");
        TextView mTextTwo = (TextView) listviewLayout.findViewById(R.id.text2);
        mTextTwo.setText("Using the last digits of both scores, the picture below demonstrates how the winner is chosen.");

        dialog.show();
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        for (int i = 0; i < arrayOfNames.size(); i++){
            //xferNames.add(arrayOfNames.get(i));

            Names test = arrayOfNames.get(i);
            //savedInstanceState.putString("test", test);
        }

        super.onSaveInstanceState(savedInstanceState);
    }
}