package com.visual.android.superbowlsquares;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Rami on 12/14/2015.
 */
public class GameData {
    String webSourceCode;
    ArrayList<String> arrayOfTeamNames;
    ArrayList<String> arrayOfTeamVersus;
    ArrayList<Integer> arrayOfScores;
    ArrayList<Game> games = new ArrayList<>();

    public GameData (String webSourceCode){
        this.webSourceCode = webSourceCode;
        String keyWordTeam = "</td><td class=\"teamLabel\">";
        int checkForVersus = 0;
        int start = 0;
        arrayOfTeamNames = new ArrayList<>();
        arrayOfScores = new ArrayList<>();
        while (true) {
            int found = webSourceCode.indexOf(keyWordTeam, start);
            if (found != -1) {
                String teamName = "";
                int stop = teamName.indexOf("<");
                for(int i = 0; stop == -1; i++){
                    int teamNameStart = found+keyWordTeam.length();
                    teamName = webSourceCode.substring(teamNameStart, teamNameStart + i); //keeps building the string by adding i until it hits stop
                    stop = teamName.indexOf("<");
                }
                if (!teamName.contains("'") && !teamName.equals("")){
                    teamName = teamName.substring(0, teamName.length() - 1); //gets rid of the "<" at the end
                    Log.d("Team Name: ", teamName);
                    arrayOfTeamNames.add(teamName);
                    checkForVersus++;
                    if (checkForVersus % 2 == 0) {
                        String teamOne = arrayOfTeamNames.get(checkForVersus - 2);
                        String teamTwo = arrayOfTeamNames.get(checkForVersus - 1);
//                        arrayOfTeamVersus.add(teamOne + " vs. " + teamTwo);
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
        }//end loop

        //Get Team Scores
        String keyWordScoreOne = "<td class=\"finalScore\">";
        String keyWordScoreTwo = ")</td><td class=\"gameOdds\">";
        arrayOfScores = new ArrayList<>();
        int startOne = 0;
        int startTwo = 0;
        while (true) {
            int foundKeyOne = webSourceCode.indexOf(keyWordScoreOne, startOne);
            int foundKeyTwo = webSourceCode.indexOf(keyWordScoreTwo, startTwo);
            if (foundKeyTwo == -1){
                foundKeyTwo = foundKeyOne + 1;
            }
            if (foundKeyOne < foundKeyTwo) {
                if (foundKeyOne != -1) {
                    String score = "";
                    int stop = score.indexOf("<");
                    for (int i = 0; stop == -1; i++) {
                        int scoreStart = foundKeyOne + keyWordScoreOne.length();
                        score = webSourceCode.substring(scoreStart, scoreStart + i);
                        stop = score.indexOf("<");
                    }
                    if (!score.contains("'") && !score.equals("")) {
                        score = score.substring(0, score.length() - 1);
                        Log.d("score", score);
                        if (Integer.valueOf(score) != null) {
                            arrayOfScores.add(Integer.valueOf(score));
                        }
                    } else {
                        Log.d("Error(score)", "contains ' or empty");
                    }
                }
                if (foundKeyOne == -1) break;
                startOne = foundKeyOne + 2;  // move start up for next iteration
            }
            else{
                if (foundKeyTwo != -1) {
                    arrayOfScores.add(0);
                    arrayOfScores.add(0);
                    Log.d("score", "0");
                    Log.d("score", "0");
                }
                startTwo = foundKeyTwo + 2;  // move start up for next iteration
            }
        }//end loop
        for (int i = 0; i < arrayOfScores.size()/2; i++){
            games.add(new Game(arrayOfTeamNames.get(2*i), arrayOfTeamNames.get((2 * i) + 1), arrayOfScores.get(2 * i), arrayOfScores.get((2 * i) +1)));
        }
    }//end constructor

    public ArrayList<String> getTeamNames(){
        return arrayOfTeamNames;
    }

    public ArrayList<Integer> getTeamScores(){
        return arrayOfScores;
    }

    public ArrayList<String> getArrayOfTeamVersus(){
        return arrayOfTeamVersus;
    }

    public ArrayList<Game> getGames(){
        return games;
    }
}//end class
