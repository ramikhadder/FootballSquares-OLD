package com.visual.android.superbowlsquares;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Rami on 11/8/2015.
 */
public class Parse {

    public int keyword1, keyword2, x1, y1, webSourceCodeNum;
    public boolean checkTeams;
    public ArrayList<Names> arrayOfNames = new java.util.ArrayList<Names>();
    public ArrayList<TeamNames> arrayOfTeams = new java.util.ArrayList<TeamNames>();
    public List<Integer> scores = new ArrayList<Integer>();

    public String teamNameOne, teamNameTwo;
    public List<String> teamNames = new ArrayList<>();

    public List<String> getTeamNames (String webSourceCode, int int3, String str4){
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
        if (teamNameOne == null && teamNameTwo == null){
            Log.d("Parse", "null");
            teamNames.add("");
            teamNames.add("");
        }
        else {
            Log.d("Parse", "not null");
            teamNames.add(teamNameOne);
            teamNames.add(teamNameTwo);
        }
        return teamNames;
    }
    private boolean isNumeric(String s){
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

}
