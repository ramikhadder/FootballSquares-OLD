package com.visual.android.superbowlsquares;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Rami on 11/8/2015.
 */
public class Parse {

    public ArrayList<String> bufferTeamNames = new ArrayList<>();

    public ArrayList<String> getTeamNames(String webSourceCode){
        String str4 = "</td><td class=\"teamLabel\">";
        ArrayList<String> arrayOfTeams = new ArrayList<>();
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
                        arrayOfTeams.add((bufferTeamNames.get(test-2) + " vs. " + bufferTeamNames.get(test-1)));
                    }
                }
                else {
                    Log.d("Error(name)", "contains ' or empty");
                }

            }
            if (found == -1) {
                return arrayOfTeams;
            }
            start = found + 2;  // move start up for next iteration
        }
    }

    public ArrayList<String> getBufferTeamNames(){
        return bufferTeamNames;
    }

    public ArrayList<Integer> getScores(String webSourceCode){
        String str1 = "<td class=\"finalScore\">";
        String str2 = ")</td><td class=\"gameOdds\">";
        ArrayList<Integer> scoree = new ArrayList<>();
        int start = 0;
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
                if (found == -1) return scoree;
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
    }

    public static String xwebSourceCode = "blank";


    public class MyxAsyncTask extends AsyncTask<Void, Void, String> {
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
            return webSourceCode;
        }

        protected void onPostExecute(String webCode) {
            xwebSourceCode = webCode;

            Log.d("onPostExecutex", xwebSourceCode);
            Log.d("onPostExecute", webCode);
            
        }
    }

    public String getWebSourceCode(){
        Log.d("getWebSourceCode", xwebSourceCode);
        return xwebSourceCode;
    }

    public int[] getTeamColors(){
        int[] teamTwoColors = {
                Color.parseColor("#000000"), Color.parseColor("#A71930"), //Atlanta Falcons (0,1)
                Color.parseColor("#9E7C0C"), Color.parseColor("#241773"), //Baltimore Ravens (2,3)
                Color.parseColor("#C60C30"), Color.parseColor("#00338D"), //Buffalo Bills (4,5)
                Color.parseColor("#C83803"), Color.parseColor("#0B162A"), //Chicago Bears (6,7)
                Color.parseColor("#FB4F14"), Color.parseColor("#000000"), //Cincinnati Bengals (8,9)
                Color.parseColor("#4C230E"), Color.parseColor("#FB4F14"), //Cleveland Browns (10,11)
                Color.parseColor("#FFFFFF"), Color.parseColor("#002E4D"), //Dallas Cowboys (12,13)
                Color.parseColor("#FB4F14"), Color.parseColor("#002244"), //Denver Broncos (14,15)
                Color.parseColor("#FFB612"), Color.parseColor("#294239"), //Green Bay Packers (16,17)
                Color.parseColor("#A71930"), Color.parseColor("#03202F"), //Houston Texans (18,19)
                Color.parseColor("#FFFFFF"), Color.parseColor("#002C5F"), //Indianapolis Colts (20,21)
                Color.parseColor("#FFFFFF"), Color.parseColor("#E31837"), //Kansas City Chiefs (22,23)
                Color.parseColor("#FFC62F"), Color.parseColor("#4F2683"), //Minnesota Vikings (24,25)
                Color.parseColor("#000000"), Color.parseColor("#9F8958"), //New Orleans Saints (26,27)
                Color.parseColor("#FFFFFF"), Color.parseColor("#203731"), //New York Jets (28,29)
                Color.parseColor("#A5ACAF"), Color.parseColor("#000000"), //Oakland Raiders (30,31)
                Color.parseColor("#000000"), Color.parseColor("#FFBF00"), //Pittsburgh Steelers (32,33)
                Color.parseColor("#B3995D"), Color.parseColor("#AA0000"), //San Francisco 49ers (34,35)
                Color.parseColor("#030F1F"), Color.parseColor("#283E67"), //Seattle Seahawks (36,37)
                Color.parseColor("#00295B"), Color.parseColor("#C1A05B"), //St. Louis Rams (38,39)
                Color.parseColor("#665C50"), Color.parseColor("#D50A0A"), //Tampa Bay Buccaneers (40,41)
                Color.parseColor("#8C001A"), Color.parseColor("#FFBF00"), //Washington Redskins (42,43)

                Color.parseColor("#A5ACAF"), Color.parseColor("#000000"), Color.parseColor("#004953"), //Philadelphia Eagles (44,45,46)
                Color.parseColor("#A71930"), Color.parseColor("#A5ACAF"), Color.parseColor("#0B2265"), //New York Giants (48,49,50)
                Color.parseColor("#F58220"), Color.parseColor("#005778"), Color.parseColor("#008E97"), //Miami Dolphins (51,52,53)
                Color.parseColor("#000000"), Color.parseColor("#FFB612"), Color.parseColor("#97233F"), //Arizona Cardinals (54,55,56)
                Color.parseColor("#0097C6"), Color.parseColor("#101B24"), Color.parseColor("#A2A7AB"), //Carolina Panthers (57,68,69)
                Color.parseColor("#B0B7BC"), Color.parseColor("#006EA1"), Color.parseColor("#000000"), //Detroit Lions (60,61,62)
                Color.parseColor("#00839C"), Color.parseColor("#101B24"), Color.parseColor("#FFFFFF"), //Jacksonville Jaguars (63,64,65)
                Color.parseColor("#C60C30"), Color.parseColor("#B0B7BC"), Color.parseColor("#002244"), //New England Patriots (66,67,68)
                Color.parseColor("#05173C"), Color.parseColor("#0F83B8"), Color.parseColor("#FFBF00"), //San Diego Chargers (69,70,71)
                Color.parseColor("#4B92DB"), Color.parseColor("#C60C30"), Color.parseColor("#002244") //Tennessee Titans (72,73,74)

        };

        return teamTwoColors;
    }
    public String[] getTeamNames(){
        //                        0          1         2        3         4         5          6          7          8         9        10        11        12        13        14        15
        String[] teamNames = { "Falcons", "Ravens", "Bills", "Bears", "Bengals", "Browns", "Cowboys", "Broncos", "Packers", "Texans", "Colts", "Chiefs", "Vikings", "Saints", "Jets", "Raiders",
                "Steelers", "49ers", "Seahawks", "Rams", "Buccaneers", "Redskins", "Eagles", "Giants", "Dolphins", "Cardinals", "Panthers", "Lions", "Jaguars", "Patriots", "Chargers", "Titans"
        //          16         17        18         19        20           21         22        23         24          25          26         27        28         29          30          31
        };
        return teamNames;
    }


    public void getExtras(Bundle extras, ArrayList<String> names, String[] row_col){
        names = extras.getStringArrayList("ARRAY_NAMES");
        row_col = extras.getStringArray("ROW_NAMES_EDS");
        //numCol = extras.getIntegerArrayList("NUM_COL");
        //numRow = extras.getIntegerArrayList("NUM_ROW");
        //, ArrayList<Integer> numCol, ArrayList<Integer> numRow
    }
}
