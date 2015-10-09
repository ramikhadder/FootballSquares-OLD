package com.visual.android.superbowlsquares;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
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
import java.util.Collections;
import java.util.List;
import java.util.Random;
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
    String[] spinner = new String[3];
    private ArrayList<Names> arrayOfNames = new ArrayList<Names>();

    private CustomAdapter adapter;
    private BoardInput bi;

    private Random rand, rando;

    private EditText mUserInput;
    private Button mConfirm;
    private Button mReady;
    private String selectedName;

    private String webSourceCode, x, y, teamNameOne, teamNameTwo, checkBoxResult;
    private int int1, int2, int3, int4, x1, y1;
    private CheckBox checkBox;
    private static final String PREFS_NAME = "MyPrefsFile1";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeInfo();

        //create a handler in an activity or fragment
        Handler handler = new Handler();
        //create a timer task and pass the handler in
        CustomTimerTask task = new CustomTimerTask(handler);
        //use timer to run the task every 10 seconds
        new Timer().scheduleAtFixedRate(task, 0, 30000);

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
                        v.setBackgroundColor(Color.WHITE);
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
                       if (item.getTitle().equals("Board Setup")){
                            setUpBoard();
                       }
                       if (item.getTitle().equals("Two")){
                           Toast.makeText(MainActivity.this, "Does nothing yet", Toast.LENGTH_SHORT).show();
                       }
                       if (item.getTitle().equals("Show Team Positions")){
                           if (teamNameOne == null && teamNameTwo == null){
                               Toast.makeText(getApplicationContext(), "No games found in the current week.",
                                       Toast.LENGTH_LONG).show();
                           }
                           else {
                               Toast.makeText(getApplicationContext(), teamNameOne + " (Row) vs. " + teamNameTwo + " (Column)",
                                       Toast.LENGTH_LONG).show();
                           }
                       }
                       if (item.getTitle().equals("Reset Table")){
                           clearBoard();
                           resetWinner();
                           shuffleNumbers();
                           setNumbers();
                           new MyAsyncTask().execute();
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
            Row_One_Column[i].setTextSize(32);
            Row_One_Column[i].setGravity(Gravity.CENTER);
            Column_One_Row[i].setText(String.valueOf(mNumberColumns[i]));
            Column_One_Row[i].setTextSize(32);
            Column_One_Row[i].setGravity(Gravity.CENTER);
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
                URL nfl = new URL("http://www.cbssports.com/nfl/scoreboard");
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
            String str = "<td class=\"finalScore\">";
            String str2 = "</td></tr><tr class=\"teamInfo homeTeam\">";
            String str3 = "</td></tr></table></span>";
            String str4 = "</td><td class=\"teamLabel\">";
            //location of scores
            int1 = webSourceCode.indexOf(str)+str.length();
            int2 = webSourceCode.indexOf(str2);
            int3 = webSourceCode.indexOf(str4)+str4.length();
            //String x is the first team's score
            if (int2 < int1){
                scores.add(0);
                scores.add(0);
                return scores;
            }
            x = webSourceCode.substring(int1,int2);

            teamNameOne = "";
            int zz = teamNameOne.indexOf("<");
            for(int i = 0; zz == -1; i++){
                teamNameOne = webSourceCode.substring(int3, int3 + i);
                zz = teamNameOne.indexOf("<");
            }
            teamNameOne = teamNameOne.substring(0, teamNameOne.length()-1);
            Log.d("Team Name", teamNameOne.substring(0, teamNameOne.length()-1));

            teamNameTwo = "";
            int z = teamNameTwo.indexOf("<");
            for(int i = 0; z == -1; i++){
                teamNameTwo = webSourceCode.substring(int3 + teamNameOne.length()+str4.length(), int3 +teamNameOne.length()+str4.length() + i);
                z = teamNameTwo.indexOf("<");
            }
            teamNameTwo = teamNameTwo.substring(0, teamNameTwo.length()-1);
            Log.d("Team Name Two", teamNameTwo.substring(0, teamNameTwo.length()));

            //checking to make sure that x is a number
            if (isNumeric(x) == true) {
                x1 = Integer.valueOf(x);
            }
            else{
                x1 = 0;
            }
            //I did this to eliminate lots of the source code, so when I index from left->right
            //it doesn't hit the first score instead, rather it hits the second score
            webSourceCode = webSourceCode.substring(int2);

            //relocation of scores
            int1 = webSourceCode.indexOf(str)+str.length();
            int2 = webSourceCode.indexOf(str3);
            //String y is the second team's score
            y = webSourceCode.substring(int1, int2);
            //checking to make sure that y is a number
            if (isNumeric(y) == true) {
                y1 = Integer.valueOf(y);
            }
            else{
                y1 = 0;
            }
            try {
                Thread.sleep(5000);
            }
            catch (InterruptedException e){
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //adding both scores to a listarray
            scores.add(x1);
            scores.add(y1);
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
            //new MyAsyncTask().execute();
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
                    new MyAsyncTask().execute();
                    //placeholders for the score before they change
                    S1PlaceHolder = ScoreOne;
                    S2PlaceHolder = ScoreTwo;
                }
            });
        }

    }
    private void setUpBoard(){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        final AlertDialog OptionDialog = new AlertDialog.Builder(MainActivity.this).create();
        final AlertDialog x = alertDialogBuilder.create();
        LayoutInflater inflater = getLayoutInflater();
        View listviewLayout = inflater.inflate(R.layout.activity_boardinput, null);
        alertDialogBuilder.setTitle("Board Set-up");
        final ListView lv = (ListView) listviewLayout.findViewById(R.id.listview);
        alertDialogBuilder.setView(listviewLayout);
        adapter = new CustomAdapter(MainActivity.this, arrayOfNames);
        lv.setAdapter(adapter);
        alertDialogBuilder.setNegativeButton("Ready", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int arg1) {
                if (adapter.getSelectedName().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please select a name.", Toast.LENGTH_LONG).show();
                } else {
                    selectedName = adapter.getSelectedName();
                    d.cancel();
                }
            }

            ;
        });
        mUserInput = (EditText) listviewLayout.findViewById(R.id.nameInput);
        mConfirm = (Button) listviewLayout.findViewById(R.id.confirm);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //names.add(mUserInput.getText().toString());
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
        Button nButton = x.getButton(DialogInterface.BUTTON_NEGATIVE);
        //nButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32);
        //nButton.setBackgroundColor(Color.GRAY);
        alertDialogBuilder.show();
    }
    private void welcomeDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View welcomeLayout = inflater.inflate(R.layout.activity_checkbox, null);
        checkBox = (CheckBox)welcomeLayout.findViewById(R.id.dontShow);
        alertDialogBuilder.setView(welcomeLayout);
        alertDialogBuilder.setTitle("Welcome to Football Squares!");
        alertDialogBuilder.setMessage("Click the button to display the teams and their respective axis.\nLong press the button " +
                "to reset the table and generate new axis numbers.");
        checkBoxResult = "NOT checked";
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (checkBox.isChecked()){
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
}
