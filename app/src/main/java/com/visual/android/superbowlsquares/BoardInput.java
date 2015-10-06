package com.visual.android.superbowlsquares;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rami on 10/5/2015.
 */
public class BoardInput extends Activity {

    private EditText mUserInput;
    private Button mConfirm;
    private List<String> names = new ArrayList<>();
    private ArrayList<Names> arrayOfNames = new ArrayList<Names>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_boardinput);
        mUserInput = (EditText) findViewById(R.id.nameInput);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //names.add(mUserInput.getText().toString());
                arrayOfNames.add(new Names(mUserInput.getText().toString()));
                mUserInput.setText("");
            }
        });
        arrayOfNames.add(new Names("TEST1"));
        arrayOfNames.add(new Names("TEST2"));
        arrayOfNames.add(new Names("TEST3"));
        CustomAdapter adapter = new CustomAdapter(this, arrayOfNames);
        ListView listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);
    }
}
