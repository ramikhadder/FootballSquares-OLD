package com.visual.android.superbowlsquares;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rami on 10/9/2015.
 */
public class TeamSelectionAdapter extends ArrayAdapter<TeamNames> {

    private ArrayList<TeamNames> items;
    private TextView tvHolder;
    private Boolean ready = false;
    private int pos = 0;
    private String selectedTeam = "";
    private View positionClicked;

    public TeamSelectionAdapter(Context context, ArrayList<TeamNames> items) {
        super(context, 0, items);
        this.items = items;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TeamNames tNames = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_namechoices, parent, false);

        }
        TeamNames tm = items.get(position);
        if (tm != null) {
            final TextView tv = (TextView)convertView.findViewById(R.id.nameItem);
            tv.setOnClickListener(  new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tvHolder != null) {
                        ready = true;
                        tvHolder.setBackgroundColor(Color.TRANSPARENT);
                        v.setBackgroundColor(Color.YELLOW);
                        tvHolder = tv;
                        selectedTeam = tv.getText().toString();
                        positionClicked = v;
                        pos = position;
                    } else {
                        ready = true;
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
        tv.setText(tNames.teamName);
        return convertView;
    }
    public int getPos(){
        return pos;
    }
}
