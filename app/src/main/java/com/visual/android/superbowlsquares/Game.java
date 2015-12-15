package com.visual.android.superbowlsquares;

import java.io.Serializable;

/**
 * Created by Rami on 12/14/2015.
 */
public class Game implements Serializable {
    public static String homeName;
    public static String awayName;
    private int homeScore;
    private int awayScore;

    public Game(String homeName, String awayName, int homeScore, int awayScore){
        this.homeName = homeName;
        this.awayName = awayName;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public String getNFLHomeTeamName(){
        return homeName;
    }
    public String getNFLAwayTeamName(){
        return awayName;
    }
    public int getHomeTeamScore(){
        return homeScore;
    }
    public int getAwayTeamScore(){
        return awayScore;
    }

}
