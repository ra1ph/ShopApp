package com.ra1ph.shopapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class NotifyDialog extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.template);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout parent = (LinearLayout) findViewById(R.id.parent_layout);
        LinearLayout child = (LinearLayout) inflater.inflate(R.layout.notifydialog, null);
        parent.addView(child);
        
        int notifyType = this.getIntent().getIntExtra(Names.NOTIFY_TYPE, 0);
        
        Button yes = (Button) findViewById(R.id.notify_yes_button);
        Button no = (Button) findViewById(R.id.notify_no_button);
        final int zakazId = this.getIntent().getIntExtra(Names.ZAKAZ_NUM, -1);
        yes.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(NotifyDialog.this, SetTimeDialog.class);
				i.putExtra(Names.DIALOG_RESULT, Names.DIALOG_OK);
				i.putExtra(Names.ZAKAZ_NUM, zakazId);
				startActivity(i);
				finish();
			}
        	
        });

        
        no.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(NotifyDialog.this, SetTimeDialog.class);
				i.putExtra(Names.DIALOG_RESULT, Names.DIALOG_NO);
				i.putExtra(Names.ZAKAZ_NUM, zakazId);
				startActivity(i);
				finish();
			}
        	
        });
    }
}
