package com.visual.android.superbowlsquares;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rami on 10/5/2015.
 */
public class CustomAdapter extends ArrayAdapter<Names>{

    private ArrayList<Names> items;

    public CustomAdapter (Context context, ArrayList<Names> items){
        super(context, 0, items);
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Names names = getItem(position);

        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_namechoices, parent, false);

        }
        Names nm = items.get(position);
        if (nm != null){
            final TextView tv = (TextView)convertView.findViewById(R.id.nameItem);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv.setText("CLICKED");
                }
            });
        }
        TextView tv = (TextView)convertView.findViewById(R.id.nameItem);
        tv.setText(names.name);
        return convertView;
    }
    /*@Override
    public void onClick(View v){
        tv.setText("CLICKED");
    }*/

}
