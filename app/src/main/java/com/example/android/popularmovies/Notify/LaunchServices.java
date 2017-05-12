package com.example.android.popularmovies.Notify;

import android.content.Context;
import android.content.Intent;

/**
 * Created by AGWU SMART ELEZUO on 5/8/2017.
 */

public class LaunchServices {

    public static void startIntentService(Context context){

        Intent intent = new Intent(context, IntentServicesMethod.class);
        intent.setAction(ServicesTasks.INTENT_SERVICE_ACTION);
        context.startService(intent);
    }
}
