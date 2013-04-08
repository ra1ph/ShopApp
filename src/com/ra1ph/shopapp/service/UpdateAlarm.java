   package com.ra1ph.shopapp.service;
   
   	import java.util.ArrayList;
import java.util.Random;

import org.json.JSONException;

import com.ra1ph.shopapp.CatList;
import com.ra1ph.shopapp.Names;
import com.ra1ph.shopapp.R;
import com.ra1ph.shopapp.NotifyDialog;
import com.ra1ph.shopapp.SetTimeDialog;
import com.ra1ph.shopapp.SetTimeDialog.MyServiceConnection;
import com.ra1ph.shopapp.data.DBEditor;
import com.ra1ph.shopapp.logic.ApiMultiStruct;
import com.ra1ph.shopapp.logic.Order;
import com.ra1ph.shopapp.logic.Parser;
import com.ra1ph.shopapp.update.ApiInterface;
import com.ra1ph.shopapp.update.ApiMultiRequest.OnMultiResultListener;
import com.ra1ph.shopapp.update.Update;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
    import android.app.PendingIntent;
    import android.content.BroadcastReceiver;
import android.content.ComponentName;
    import android.content.Context;
    import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
    import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

    public class UpdateAlarm extends BroadcastReceiver 
    {    
    	private Context context;
    	Intent intent;
    	StatusService service;
    	MyServiceConnection sConn;
         @Override
         public void onReceive(final Context context, Intent intent) 
         {   
        	 this.context = context;
        	 SharedPreferences settings = context.getSharedPreferences(Names.APP_NAME,Context.MODE_PRIVATE);
        	 
     		String fio_str = settings.getString(Names.FIO, "");
    		String tel_str = settings.getString(Names.TEL, "");
    		String secretId = settings.getString(Names.SECRET_ID, "");    		
    		        	 
        	 Log.d("Alarm", "Update alarm!");
        	 final DBEditor dbe = new DBEditor(context);
        	 ArrayList<Order> orders = dbe.getOrderList();
        	 ApiInterface api = new ApiInterface();
				api.updateOrdersStatuses(orders, tel_str, secretId, new OnMultiResultListener(){

					public void OnResult(ApiMultiStruct reply) {
						// TODO Auto-generated method stub
						Log.d("myLog", "DebugMessage!!!");
						ArrayList<String> requests = reply.request;
						ArrayList<Order> orders = dbe.getOrderList();
						Parser parser = new Parser(context);
						for(int i = 0;i<requests.size();i++){
							String st = parser.parseZakazStatus(requests.get(i));
							String old_st = orders.get(i).status;
							if(!st.equals("")){								
								orders.get(i).status=st;
								
							}
							if((orders.get(i).status.equals(Names.ST_DELIVERED))&&(!orders.get(i).status.equals(old_st)))NotifyAboutDelivered(orders.get(i).idZakaz);
							//NotifyAboutDelivered(orders.get(i).idZakaz);
							if((orders.get(i).status.equals(Names.ST_GOTTEN))&&(!orders.get(i).status.equals(old_st)))OrderGotten(orders.get(i).idZakaz);
						}
						dbe.updateOrders(orders);
						dbe.close();
					}
					 
				 });
         }
         
     public void OrderGotten(int zakazId){
    	 DBEditor dbe = new DBEditor(context);
    	 Log.d("API","Order has been deleted "+Integer.toString(zakazId));
    	 
    	 dbe.delNotify(zakazId);
    	 dbe.close();
     }
     
    /* public void cancelNotify(int notifyNum){
     	Intent i = new Intent(context,TimeNotification.class);
     	i.setData(Uri.parse("timer:"+notifyNum));
     	
     	PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);    	
     	AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); 	 
     	am.cancel(pi);
     }*/
         
     public void NotifyAboutDelivered(int zakazId){  	   	 
    	 
    	 Intent i = new Intent(context,NotifyDialog.class);
    	 i.putExtra(Names.ZAKAZ_NUM, zakazId);
    	 i.putExtra(Names.NOTIFY_TYPE, Names.NOTIFY_ONCE);
    	 i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	 i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);    	 
    	 NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    	 Notification notification = new Notification(R.drawable.ic_launcher, "Test", System.currentTimeMillis());
    	 int nn = new Random().nextInt(120);
         PendingIntent pIntent = PendingIntent.getActivity(context, nn, i, 0);
         
         // 2-я часть
         notification.setLatestEventInfo(context, "Your order has been delivered.", "Your order number "+Integer.toString(zakazId), pIntent);
         
         // ставим флаг, чтобы уведомление пропало после нажатия
         notification.flags |= Notification.FLAG_AUTO_CANCEL;
    	 nm.notify(nn, notification);
    	 
    	 Log.d("myLog", "Notify ID "+Integer.toString(nn));
    	 nn++;
     }

     public void SetAlarm(Context context)
     {
         AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
         Intent i = new Intent(context, UpdateAlarm.class);
         PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
         am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 1, pi); // Millisec * Second * Minute
     }

     public void CancelAlarm(Context context)
     {
         Intent intent = new Intent(context, UpdateAlarm.class);
         PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
         AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
         alarmManager.cancel(sender);
     }
     
     public class MyServiceConnection implements ServiceConnection{
     	public MyServiceConnection(){
     		Log.d("myTag", "Connection has been created");
     	}
     	
 		public void onServiceConnected(ComponentName name, IBinder binder) {
 			// TODO Auto-generated method stub
 			Log.d("myLog","MainActivity onServiceConnected");
 			service = ((StatusService.MyBinder) binder).getService();
 		}

 		public void onServiceDisconnected(ComponentName name) {
 			// TODO Auto-generated method stub
 			Log.d("myLog","MainActivity onServiceDisconnected");
 		}
     	
     };
 }