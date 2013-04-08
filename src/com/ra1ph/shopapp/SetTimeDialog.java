package com.ra1ph.shopapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.ra1ph.shopapp.data.DBEditor;
import com.ra1ph.shopapp.logic.ApiStruct;
import com.ra1ph.shopapp.service.StatusService;
import com.ra1ph.shopapp.service.TimeNotification;
import com.ra1ph.shopapp.update.ApiInterface;
import com.ra1ph.shopapp.update.ApiInterface.OnResultListener;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;

public class SetTimeDialog extends Activity {
	DatePicker date;
	TimePicker time;
	int notifyNum;
	Intent intent;
	MyServiceConnection sConn;
	StatusService service;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.template);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout parent = (LinearLayout) findViewById(R.id.parent_layout);
        LinearLayout child = (LinearLayout) inflater.inflate(R.layout.settimedialog, null);
        parent.addView(child);
        
        date = (DatePicker) findViewById(R.id.date_picker);
        time = (TimePicker) findViewById(R.id.time_picker);
        final CheckBox chkBox = (CheckBox) findViewById(R.id.precision_time);
        
        Button btnOk = (Button) findViewById(R.id.set_time_button);
        
        intent = new Intent(this,StatusService.class);
        sConn = new MyServiceConnection();
		getApplicationContext().bindService(intent,sConn,Context.BIND_AUTO_CREATE);
        
        final int dialog_res = this.getIntent().getIntExtra(Names.DIALOG_RESULT, -1);
        final int zakazId = this.getIntent().getIntExtra(Names.ZAKAZ_NUM, -1);
        if((dialog_res!=-1)&&(zakazId!=-1)){
        	btnOk.setOnClickListener(new OnClickListener(){

				public void onClick(View v) {
					// TODO Auto-generated method stub
					sendStatus(chkBox.isChecked(),zakazId);
					setNotification(chkBox.isChecked(),dialog_res,zakazId);					
					finish();
				}
        		
        	});
        }
        else this.finish();
    }
    
    @Override
    protected void onDestroy(){
    getApplicationContext().unbindService(sConn);
    super.onDestroy();
    }
    
    public void setNotification(boolean precision,int dialog_res,int zakazId){
 	
 	Calendar cal = Calendar.getInstance();
 	cal.set(cal.YEAR, date.getYear());
 	cal.set(cal.MONTH, date.getMonth());
 	cal.set(cal.DAY_OF_MONTH, date.getDayOfMonth());
 	cal.set(Calendar.HOUR_OF_DAY, time.getCurrentHour());
 	cal.set(Calendar.MINUTE, time.getCurrentMinute());
 	
 	long time = cal.getTimeInMillis();
 	if(!precision) time+=30*1000*1000;
 	if(dialog_res==Names.DIALOG_OK)time+=4*60*1000*1000;
 	
  	 Log.d("myLog", "notify sended!");
 	DBEditor dbe = new DBEditor(this);
 	notifyNum = dbe.addNotify(zakazId,time);
 	dbe.close();
 	
 	service.setTimeNotify(notifyNum, time, zakazId);
    }
    
    public void sendStatus(boolean precision, int zakazId){
    	
    	Calendar cal = Calendar.getInstance();
    	cal.set(date.getYear(), date.getMonth(), date.getDayOfMonth(), time.getCurrentHour(), time.getCurrentMinute());
    	SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    	String date = form.format(cal.getTime());  	
    	
    	SharedPreferences settings = getSharedPreferences(Names.APP_NAME,MODE_PRIVATE);
    	String idUser = settings.getString(Names.TEL, "");
		String secretId = settings.getString(Names.SECRET_ID, "");
    	
    	ApiInterface api = new ApiInterface();
    	String status = Names.ST_WILLTAKE;
    	if(precision)status = Names.ST_WILLTAKEBYTIME;
    	api.setOnResultListener(new OnResultListener(){

			public void OnResult(ApiStruct reply) {
				// TODO Auto-generated method stub
				Log.d("API",reply.request);
			}
    		
    	});
    	api.setOrderStatus(zakazId, idUser, secretId, status, date);
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
