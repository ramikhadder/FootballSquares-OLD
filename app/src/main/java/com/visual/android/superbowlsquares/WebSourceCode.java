package com.visual.android.superbowlsquares;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Rami on 12/14/2015.
 */
public class WebSourceCode extends AsyncTask<Void, Void, ArrayList<String>> {
    String webSourceCode;
    public WebSourceCode(String code){
        webSourceCode = code;
    }
    @Override
    protected ArrayList<String> doInBackground(Void... params){
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
        webSourceCode = sb.toString();
        ArrayList<Integer> scores = new ArrayList<>();

        try {
            Thread.sleep(2500);
        }
        catch (InterruptedException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }//end doInBackground
}//end class
