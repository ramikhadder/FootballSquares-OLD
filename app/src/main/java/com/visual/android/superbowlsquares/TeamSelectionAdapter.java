package com.visual.android.superbowlsquares;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rami on 10/9/2015.
 */
public class TeamSelectionAdapter extends ArrayAdapter<Game> {

    private ArrayList<Game> items;
    private TextView tvHolder;
    public Boolean isGameSelected = false;
    private int pos = 0;
    private String selectedTeam = "";
    private View positionClicked;
    private Game game;

    public TeamSelectionAdapter(Context context, ArrayList<Game> items) {
        super(context, 0, items);
        this.items = items;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Game tNames = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_namechoices, parent, false);

        }
        Game tm = items.get(position);
        if (tm != null) {
            final TextView tv = (TextView)convertView.findViewById(R.id.nameItem);
            tv.setOnClickListener(  new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                    SplashScreen loadWebSourceCode = new SplashScreen();
                    String webSourceCode = loadWebSourceCode.getWebSourceCode();

                    GameData gameData = new GameData(webSourceCode);
                    ArrayList<String> teamNames = gameData.getTeamNames();
                    ArrayList<Integer> teamScores = gameData.getTeamScores();

                    String homeTeamName = teamNames.get(2 * position);
                    String awayTeamName = teamNames.get((2 * position) + 1);
                    int homeTeamScore = teamScores.get(2 * position);
                    int awayTeamScore = teamScores.get((2 * position) + 1);

                    game = new Game(homeTeamName, awayTeamName, homeTeamScore, awayTeamScore);
*/

                    if (tvHolder != null) { //every click afterwards
                        isGameSelected = true;
                        tvHolder.setBackgroundColor(Color.TRANSPARENT);
                        v.setBackgroundColor(Color.YELLOW);
                        tvHolder = tv;
                        selectedTeam = tv.getText().toString();
                        positionClicked = v;
                        pos = position;
                    } else { //first click
                        isGameSelected = true;
                        v.setBackgroundColor(Color.YELLOW);
                        tvHolder = tv;
                        selectedTeam = tv.getText().toString();
                        positionClicked = v;
                        pos = position;

                    }
                }
            });
        }
        TextView tv = (TextView)convertView.findViewById(R.id.nameItem);
        Log.d("TEST", String.valueOf(tm.getNFLHomeTeamName()));
        tv.setText(tNames.getNFLHomeTeamName() + " vs. " + tNames.getNFLAwayTeamName());
        return convertView;
    }
    public int getPos(){
        return pos;
    }
    public Boolean isGameSelected(){
        return isGameSelected;
    }
    public Game getGameSelected() {
        return game;
    }
}
