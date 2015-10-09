package com.visual.android.superbowlsquares;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Rami on 10/5/2015.
 */
public class BoardInput extends Activity {

    private EditText mUserInput;
    private Button mConfirm;
    private Button mReady;
    private List<String> names = new ArrayList<>();
    private ArrayList<Names> arrayOfNames = new ArrayList<>();
    private CustomAdapter adapter;
    private ListView listview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boardinput);

        adapter = new CustomAdapter(BoardInput.this, arrayOfNames);
        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);

        mUserInput = (EditText) findViewById(R.id.nameInput);
        mConfirm = (Button) findViewById(R.id.confirm);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //names.add(mUserInput.getText().toString());
                if (mUserInput.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter a name.",
                            Toast.LENGTH_SHORT).show();

                } else {
                    arrayOfNames.add(new Names(mUserInput.getText().toString()));
                    mUserInput.setText("");
                    listview.setAdapter(adapter);
                }

            }
        });
        //mReady = (Button) findViewById(R.id.ready);
        mReady.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (adapter.getSelectedName().isEmpty()){
                    Log.d("NULL","NULL");
                }
                else{
                    Log.d("BoardInput", adapter.getSelectedName());
                    Intent i = new Intent(BoardInput.this, MainActivity.class);
                    startActivity(i);
                }
            }
        });
    }
    public CustomAdapter getAdapter(){
        return adapter;
    }
}
