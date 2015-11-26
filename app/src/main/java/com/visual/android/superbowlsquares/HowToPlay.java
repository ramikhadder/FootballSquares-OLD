package com.visual.android.superbowlsquares;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Rami on 11/19/2015.
 */
public class HowToPlay extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_htp);

        System.out.println("intent successful");
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

    }
}
