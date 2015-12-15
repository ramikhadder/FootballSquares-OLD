package com.visual.android.superbowlsquares;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Rami on 12/14/2015.
 */
public class GameSelection extends Activity {

    private static String webSourceCode;
    private ArrayList<String> arrayOfTeams = new ArrayList<>();
    private ArrayList<Integer> arrayOfScores = new ArrayList<>();
    private ArrayList<String> arrayOfTeamVersus = new ArrayList<>();
    private ArrayList<Game> arrayTest = new ArrayList<>();
    private TeamSelectionAdapter teamSelectionAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teamnameselection);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //String checkPrevActivity = extras.getString("ClassName");
            arrayOfTeams = extras.getStringArrayList("ARRAY_TEAMS");
            arrayOfScores = extras.getIntegerArrayList("ARRAY_SCORES");
            arrayOfTeamVersus = extras.getStringArrayList("ARRAY_VERSUS");
            webSourceCode = extras.getString("SOURCE");

        }
        if (arrayOfTeams == null) arrayOfTeams = new ArrayList<>();
        if (arrayOfScores == null) arrayOfScores = new ArrayList<>();

        GameData gameData = new GameData(webSourceCode);
        ScrollView empty = (ScrollView) findViewById(R.id.emptyscroll);
        ListView lv = (ListView) findViewById(R.id.listviewts);
        arrayTest = gameData.getGames();
        teamSelectionAdapter = new TeamSelectionAdapter(GameSelection.this, arrayTest);
        lv.setEmptyView(empty);
        lv.setAdapter(teamSelectionAdapter);

        Button confirm = (Button) findViewById(R.id.confirmbutton);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teamSelectionAdapter.isGameSelected()) {
                    Game selectedGame = teamSelectionAdapter.getGameSelected();
                    Intent i = new Intent(GameSelection.this, Test.class);
                    i.putExtra("SELECTED", selectedGame);
                    startActivity(i);
                }


                boolean ready = false;
                int teamPosition = 0;
                if (arrayOfTeams.size() > 0) {
                    ready = teamSelectionAdapter.isGameSelected();
                    teamPosition = teamSelectionAdapter.getPos();
                }
                if (ready == true || arrayOfTeams.size() <= 0) {
                    String homeTeamName = arrayOfTeams.get(2 * teamPosition);
                    String awayTeamName = arrayOfTeams.get((2 * teamPosition) + 1);
                    int homeTeamScore = arrayOfScores.get(2 * teamPosition);
                    int awayTeamScore = arrayOfScores.get((2 * teamPosition) + 1);
                    Game game = new Game(homeTeamName, awayTeamName, homeTeamScore, awayTeamScore);
                    Intent i = new Intent(GameSelection.this, Test.class);
                    i.putExtra("GAME", game);
                    startActivity(i);
                } else {
                    Toast.makeText(GameSelection.this, "Please select a game to track.", Toast.LENGTH_SHORT).show();
                }
            }
        });//confirm button end


    }//onCreate end

    private class MyAsyncTask extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... params) {
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
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            ArrayList<Integer> scores = new ArrayList<>();

            try {
                Thread.sleep(2500);
            }
            catch (InterruptedException e){
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return sb.toString();
        }
        protected void onPostExecute(String webSourceCode) {
            GameData gameData = new GameData(webSourceCode);
            gameData.getTeamNames();
            gameData.getTeamScores();
            GameSelection.webSourceCode = webSourceCode;
        }
    }//MyAyncTask end

}//class end
