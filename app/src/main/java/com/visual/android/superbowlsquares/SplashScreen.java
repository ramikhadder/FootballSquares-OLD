package com.visual.android.superbowlsquares;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.parse.ParseObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Rami on 12/12/2015.
 */
public class SplashScreen extends Activity {

    private ArrayList<String> arrayOfTeams = new ArrayList<>();
    private ArrayList<Integer> arrayOfScores = new ArrayList<>();
    private ArrayList<String> arrayOfTeamVersus = new ArrayList<>();
    private String webSourceCode;
    //Parse parse = new Parse();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        new MyAsyncTask().execute();

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

        protected void onPostExecute(final String webCode) {

            webSourceCode = webCode;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    GameData gameData = new GameData(webCode);
                    arrayOfTeams = gameData.getTeamNames();
                    arrayOfScores = gameData.getTeamScores();
                    arrayOfTeamVersus = gameData.getArrayOfTeamVersus();

                    Log.d("TeamSelection", "AsyncTaskSurvived");
                    //arrayOfTeams = parse.getTeamNames(webSoureceCode);
                    Intent i = new Intent(SplashScreen.this, GameSelection.class);
                    i.putExtra("ClassName", "SplashScreen");
                    i.putExtra("SOURCE", webCode);
                    i.putStringArrayListExtra("ARRAY_VERSUS", arrayOfTeamVersus);
                    i.putStringArrayListExtra("ARRAY_TEAMS", arrayOfTeams);
                    i.putIntegerArrayListExtra("ARRAY_SCORES", arrayOfScores);
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    startActivity(i);
                    finish();
                }
            }, 1200);
        }
    }//AsyncTask end

    public String getWebSourceCode(){
        return webSourceCode;
    }

}//class end
