package com.example.android.popularmovies.Notify;


import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by AGWU SMART ELEZUO on 5/8/2017.
 */

public class IntentServicesMethod extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public IntentServicesMethod() {

        super("IntentServicesMethod");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
        ServicesTasks.launchTasks(action, this);
    }
}
