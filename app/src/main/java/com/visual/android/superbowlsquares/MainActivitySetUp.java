package com.visual.android.superbowlsquares;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Rami on 12/1/2015.
 */
public class MainActivitySetUp extends Activity {

    public ArrayList<String> arrayOfNames = new ArrayList<>();
    private TableLayout layout;
    TextView[][] Row_EDs = new TextView[10][10];
    public String[] Row_Names_EDs = new String[100];
    ArrayList<String> namesOnBoard = new ArrayList<>();
    TableRow[] Rows = new TableRow[11];
    private int z = 0;
    private int position;
    private Boolean freeze = false;
    private boolean lock = false;
    ArrayList<Integer> numRow = new ArrayList<>();
    ArrayList<Integer> numCol = new ArrayList<>();
    int teamPos = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainsetup);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            teamPos = extras.getInt("TEAM_POS");
            arrayOfNames = extras.getStringArrayList("ARRAY_NAMES");
            namesOnBoard = extras.getStringArrayList("NAMES_ON_BOARD");
            //Row_Names_EDs = extras.getStringArray("ROW_NAMES_EDS");
            numRow = extras.getIntegerArrayList("NUM_ROW");
            numCol = extras.getIntegerArrayList("NUM_COL");
        }
        if (numCol == null) numCol = new ArrayList<>();
        if (numRow == null) numRow = new ArrayList<>();
        if (arrayOfNames == null) arrayOfNames = new ArrayList<>();
        if (namesOnBoard == null) namesOnBoard = new ArrayList<>();


        final TextView message = (TextView)findViewById(R.id.message);
        message.setText("It's " + arrayOfNames.get(0) + "'s turn.");

        initializeInfo();

        Button mSkipTurn = (Button)findViewById(R.id.skipturn);
        mSkipTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                z++;
                if (z == arrayOfNames.size()) {
                    z = 0;
                }
                message.setText("It's " + arrayOfNames.get(z) + "'s turn.");
            }
        });

        final Button mFreeze = (Button)findViewById(R.id.freezeturn);
        mFreeze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (freeze == false) {
                    freeze = true;
                    mFreeze.setBackgroundColor(Color.parseColor("#03A9F4"));
                    //mFreeze.setBackground(getResources().getDrawable(R.drawable.unfreezetofreeze));
                }
                else{
                    freeze = false;
                    mFreeze.setBackgroundColor(Color.WHITE);
                }
            }
        });

        Button mLeggo = (Button)findViewById(R.id.letsgo);
        mLeggo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivitySetUp.this, MainActivity.class);
                i.putExtra("TEAM_POS", teamPos);
                //i.putExtra("ROW_NAMES_EDS", Row_Names_EDs);
                i.putStringArrayListExtra("NAMES_ON_BOARD", namesOnBoard);
                i.putStringArrayListExtra("ARRAY_NAMES", arrayOfNames);
                i.putIntegerArrayListExtra("NUM_ROW", numRow);
                i.putIntegerArrayListExtra("NUM_COL", numCol);
                startActivity(i);
            }
        });

        for (int i = 0; i < 10; i++) {
            for (int y = 0; y < 10; y++) {

                final int fy = y;
                final int fi = i;

                Row_EDs[i][y].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        position = (fi * 10) + fy;

                        TextView t = (TextView) v;
                        t.setText(arrayOfNames.get(z));
                        if (!freeze) {
                            z++;
                            if (z == arrayOfNames.size()) {
                                z = 0;
                            }
                        }
                        message.setText("It's " + arrayOfNames.get(z) + "'s turn.");

                        //Row_Names_EDs[position] = t.getText().toString();
                        namesOnBoard.set(position, t.getText().toString());

                    }
                });
            }
        }

    }
    private void initializeInfo(){
        layout = (TableLayout) findViewById(R.id.table);

        for (int i = 0; i < 11; i++){
            Rows[i] = (TableRow)layout.getChildAt(i);
        }

        //sets the ints of the arrays equal to each respective child
        for (int i = 0; i < 10; i++){
            for (int y = 0; y < 10; y++) {
                int pos = (i*10) + y;
                Row_EDs[i][y] = (TextView) Rows[i+1].getChildAt(y);
                //Row_EDs[i][y].setText(Row_Names_EDs[pos]);
                Row_EDs[i][y].setText(namesOnBoard.get(pos));
            }
        }
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(this, BoardInput.class);
        i.putExtra("TEAM_POS", teamPos);
        //i.putExtra("ROW_NAMES_EDS", Row_Names_EDs);
        i.putStringArrayListExtra("NAMES_ON_BOARD", namesOnBoard);
        i.putStringArrayListExtra("ARRAY_NAMES", arrayOfNames);
        i.putIntegerArrayListExtra("NUM_ROW", numRow);
        i.putIntegerArrayListExtra("NUM_COL", numCol);
        startActivity(i);
    }
}
