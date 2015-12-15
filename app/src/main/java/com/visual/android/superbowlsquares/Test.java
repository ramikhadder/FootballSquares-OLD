package com.visual.android.superbowlsquares;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
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
public class Test extends Activity {

    String webSourceCode = "yo";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        new MyAsyncTask().execute(); //gets the source code
        //Game game = new Game(webSourceCode);
        GameData gameData = new GameData(webSourceCode);
        gameData.getTeamNames();
        gameData.getTeamScores();

        final TextView text = (TextView) findViewById(R.id.text);
        text.setText(webSourceCode);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Test.this, "clicked", Toast.LENGTH_SHORT).show();
                text.setText(webSourceCode);
            }
        });
    }

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
        protected void onPostExecute(String code) {
            webSourceCode = code;
        }
    }

}
