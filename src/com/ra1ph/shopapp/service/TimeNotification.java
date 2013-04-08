package com.ra1ph.shopapp.service;

import java.util.Calendar;
import java.util.Random;

import com.ra1ph.shopapp.CatList;
import com.ra1ph.shopapp.Names;
import com.ra1ph.shopapp.NotifyDialog;
import com.ra1ph.shopapp.R;
import com.ra1ph.shopapp.SearchActivity;
import com.ra1ph.shopapp.SetTimeDialog;
import com.ra1ph.shopapp.data.DBEditor;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class TimeNotification extends BroadcastReceiver {
 @Override
 public void onReceive(Context context, Intent intent) {
	 int zakazId = intent.getIntExtra(Names.ZAKAZ_NUM,-1);
	 
	 if(zakazId!=-1){
	 DBEditor dbe = new DBEditor(context);
	 int notifyNum = dbe.getNotifyNum(zakazId);
	 dbe.delNotify(zakazId);	
	 if(notifyNum==-1) return; 
	 dbe.close();	 
	 
	 NotifyDelivered(context,zakazId);
	 }

 }
 
 public void NotifyDelivered(Context context,int zakazId){
	 Intent i = new Intent(context,NotifyDialog.class);
	 i.putExtra(Names.ZAKAZ_NUM, zakazId);
	 i.putExtra(Names.NOTIFY_TYPE, Names.NOTIFY_RECEDIVE);
	 i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	 i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 
	 int nn = new Random().nextInt(120);
	 
	 NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	 Notification notification = new Notification(R.drawable.ic_launcher, "Test", System.currentTimeMillis());
     PendingIntent pIntent = PendingIntent.getActivity(context, nn, i, 0);
     
     // 2-я часть
     notification.setLatestEventInfo(context, "Your order has been delivered.", "ZakazID "+zakazId, pIntent);
     
     // ставим флаг, чтобы уведомление пропало после нажатия
     notification.flags |= Notification.FLAG_AUTO_CANCEL;
	 nm.notify(nn, notification);
 }
 
 public void SetAlarm(Context context, int notifyNum, long time, int zakazId)
 {
 	Intent i = new Intent(context,TimeNotification.class);
 	i.putExtra(Names.ZAKAZ_NUM, zakazId);
 	i.setData(Uri.parse("timer:"+notifyNum));
 	
 	int nn = new Random().nextInt(120);
 	PendingIntent pi = PendingIntent.getBroadcast(context, nn, i, 0);    	
 	AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
 	    	
 	am.set(AlarmManager.RTC_WAKEUP, time, pi);
 	
 }

 public void CancelAlarm(Context context, int notifyNum, int zakazId)
 {
	 Intent i = new Intent(context,TimeNotification.class);
	 	i.putExtra(Names.ZAKAZ_NUM, zakazId);
	 	i.setData(Uri.parse("timer:"+notifyNum));
	 	
	 	PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);    	
	 	AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	 	    	
	 	am.cancel(pi);
 }
 
 public void notifyDeliver(){
	 /*
	 Log.d("Alarm", "Time notify!!");
	 
	 NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	 Notification notification = new Notification(R.drawable.ic_launcher, "Test", System.currentTimeMillis());
     Intent in = new Intent(context, CatList.class);
     PendingIntent pIntent = PendingIntent.getActivity(context, 0, in, 0);
     
     // 2-я часть
     notification.setLatestEventInfo(context, "Notification's title", "Notification's text", pIntent);
     
     // ставим флаг, чтобы уведомление пропало после нажатия
     notification.flags |= Notification.FLAG_AUTO_CANCEL;
	 nm.notify(1, notification);
	 
	 try {
		     Bundle bundle = intent.getExtras();
		     String message = bundle.getString("alarm_message");
		     Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		    } catch (Exception e) {
		     Toast.makeText(context, "There was an error somewhere, but we still received an alarm", Toast.LENGTH_SHORT).show();
		     e.printStackTrace();
		 
		    }
		    */
 }
}