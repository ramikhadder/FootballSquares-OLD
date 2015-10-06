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
    Context context;
    int layoutResourceId;
    Names data[] = null;

    public CustomAdapter (Context context, ArrayList<Names> namez){
        super(context, 0, namez);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Names names = getItem(position);

        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_namechoices, parent, false);
        }

        TextView tv = (TextView)convertView.findViewById(R.id.nameTest);
        tv.setText(names.name);

        return convertView;
    }

}
