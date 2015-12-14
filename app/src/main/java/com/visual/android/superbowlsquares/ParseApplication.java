package com.visual.android.superbowlsquares;

import android.app.Application;

/**
 * Created by Rami on 12/8/2015.
 */
public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        com.parse.Parse.enableLocalDatastore(this);
        com.parse.Parse.initialize(this, "laoILmY3xJinnSXwHwcvT0G8mhobjSnHcVvtJiSd", "X4sDuwu7opMwhsEtiTpPT4EcELz21MDpFiLBwcST");
    }
}
