package com.visual.android.superbowlsquares;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rami on 10/5/2015.
 */
public class CustomAdapter extends ArrayAdapter<String>{

    private ArrayList<String> items;
    private TextView tvHolder;
    private String selectedName = "";
    private Boolean ready = false;

    public CustomAdapter (Context context, ArrayList<String> items){
        super(context, 0, items);
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String name = getItem(position);
        Log.d("nameone", name);
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_namechoices, parent, false);
        }
        String nm = items.get(position);
        Log.d("namefour", nm);
        if (nm != null){
            final TextView tv = (TextView)convertView.findViewById(R.id.nameItem);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tvHolder != null) {
                        ready = true;
                        tvHolder.setBackgroundColor(Color.TRANSPARENT);
                        //tvHolder.setBackgroundColor(Color.WHITE);
                        v.setBackgroundColor(Color.YELLOW);
                        tvHolder = tv;
                        selectedName = tv.getText().toString();
                        Log.d("nametwo", selectedName);
                    } else {
                        ready = true;
                        v.setBackgroundColor(Color.YELLOW);
                        tvHolder = tv;
                        selectedName = tv.getText().toString();
                        Log.d("namethree", selectedName);
                    }
                }
            });
        }
        TextView tv = (TextView)convertView.findViewById(R.id.nameItem);
        tv.setText(name);
        return convertView;
    }

    public String getSelectedName(){
        return selectedName;
    }
    public Boolean getReady() {
        return ready;
    }
}
