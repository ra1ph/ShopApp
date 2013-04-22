package com.ra1ph.shopapp;

import java.util.Calendar;

import com.ra1ph.shopapp.SetTimeDialog.MyServiceConnection;
import com.ra1ph.shopapp.data.DBEditor;
import com.ra1ph.shopapp.service.StatusService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

public class NotifyDialog extends Activity {
	int zakazId;
	StatusService service;
	boolean isOk=false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.template);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout parent = (LinearLayout) findViewById(R.id.parent_layout);
        LinearLayout child = (LinearLayout) inflater.inflate(R.layout.notifydialog, null);
        child.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
        parent.addView(child);
        
        Intent intent = new Intent(this,StatusService.class);
        MyServiceConnection sConn = new MyServiceConnection();
		getApplicationContext().bindService(intent,sConn,Context.BIND_AUTO_CREATE);
        
        int notifyType = this.getIntent().getIntExtra(Names.NOTIFY_TYPE, 0);
        
        Button yes = (Button) findViewById(R.id.notify_yes_button);
        Button no = (Button) findViewById(R.id.notify_no_button);
        zakazId = this.getIntent().getIntExtra(Names.ZAKAZ_NUM, -1);
        yes.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(NotifyDialog.this, SetTimeDialog.class);
				i.putExtra(Names.DIALOG_RESULT, Names.DIALOG_OK);
				i.putExtra(Names.ZAKAZ_NUM, zakazId);
				startActivityForResult(i,SetTimeDialog.RESULT_BAD);
			}
        	
        });

        
        no.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(NotifyDialog.this, SetTimeDialog.class);
				i.putExtra(Names.DIALOG_RESULT, Names.DIALOG_NO);
				i.putExtra(Names.ZAKAZ_NUM, zakazId);
				startActivityForResult(i,SetTimeDialog.RESULT_BAD);
			}
        	
        });
    }
    
    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data)
    {
    	if(resCode==SetTimeDialog.RESULT_OK) {
    		isOk=true;
    		finish();
    	}
    }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
	 	if(!isOk){
	 	long time = System.currentTimeMillis();
	 	
	  	 Log.d("myLog", "notify sended!");
	 	DBEditor dbe = new DBEditor(this);
	 	int notifyNum = dbe.addNotify(zakazId,time);
	 	dbe.close();
	 	
	 	service.setTimeNotify(notifyNum, time, zakazId);
	 	}
	 	super.onDestroy();
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
