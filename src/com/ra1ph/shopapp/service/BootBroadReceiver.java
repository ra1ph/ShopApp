package com.ra1ph.shopapp.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootBroadReceiver extends BroadcastReceiver {

	  final String LOG_TAG = "myLogs";

	  public void onReceive(Context context, Intent intent) {
	    Log.d(LOG_TAG, "onReceive " + intent.getAction());
	    context.startService(new Intent(context, StatusService.class));
	  }
	}