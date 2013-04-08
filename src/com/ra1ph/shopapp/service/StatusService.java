package com.ra1ph.shopapp.service;

import java.util.Timer;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class StatusService extends Service {
  NotificationManager nm;
  UpdateAlarm updateAlarm = new UpdateAlarm();
  TimeNotification timeNotify = new TimeNotification();
  
  @Override
  public void onCreate() {
    super.onCreate();
  }

  public int onStartCommand(Intent intent, int flags, int startId) {
	Log.d("myLog", "Service started!");
	updateAlarm.SetAlarm(this);
	super.onStartCommand(intent, flags, startId);
    return START_STICKY;
  }
  
  public void updateStatus(){
  
  }
  
  public void setTimeNotify(int notifyNum, long time,int zakazId){
	  timeNotify.SetAlarm(this, notifyNum, time, zakazId);
  }
  
  public void cancelTimeNotify(int notifyNum,int zakazId){
	  timeNotify.CancelAlarm(this, notifyNum, zakazId);
	  Log.d("myLog", "Server canceled notify!");
  }
  
  public IBinder onBind(Intent arg0) {
    return new MyBinder();
  }
  
  public class MyBinder extends Binder{
	  public StatusService getService(){
		  return StatusService.this; 
	  }
  }
}