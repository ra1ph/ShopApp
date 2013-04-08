package com.ra1ph.shopapp;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ra1ph.shopapp.data.DBEditor;
import com.ra1ph.shopapp.logic.Category;
import com.ra1ph.shopapp.logic.Item;
import com.ra1ph.shopapp.service.StatusService;
import com.ra1ph.shopapp.service.TimeNotification;
import com.ra1ph.shopapp.update.Update;
import com.ra1ph.shopapp.update.Update.OnUpdateListener;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

public class ShopAppActivity extends BaseActivity {
   	TimeNotification service;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        startService(new Intent(this,StatusService.class));
        
        final ProgressDialog progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progDialog.setMax(100);
        progDialog.show();
        
        Update update = new Update(this);
        update.getUpdateCat(new OnUpdateListener(){

			public void OnUpdate() {
				// TODO Auto-generated method stub
				Log.d("myLogs", "Update complete!");
				progDialog.dismiss();
		        DBEditor dbe = new DBEditor(ShopAppActivity.this);
		        ArrayList<Category> cats = dbe.getCats();
		        dbe.close();
		        Intent i = new Intent(ShopAppActivity.this,CatList.class);
		        i.putExtra(CATEGORY_ID, 0);
		        startActivity(i);
		        finish();
			}

			public void OnProgress(Integer prg) {
				// TODO Auto-generated method stub
				if(progDialog.isShowing())progDialog.setProgress(prg);
			}
		});
    }
    
    
    @Override
    protected void onDestroy()
    {	
    	super.onDestroy();
        if(service!= null){unregisterReceiver(service);}
    }
    
}