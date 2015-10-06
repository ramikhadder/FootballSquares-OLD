package com.visual.android.superbowlsquares;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

/**
 * Created by Rami on 10/5/2015.
 */
public class CustomOnItemSelectedListener extends Activity implements AdapterView.OnItemSelectedListener {
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        /*Toast.makeText(getApplicationContext(), parent.getItemAtPosition(pos).toString(),
                Toast.LENGTH_LONG).show();*/
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
