package com.ra1ph.shopapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Contacts extends BaseActivity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    LinearLayout parent = (LinearLayout) findViewById(R.id.parent_layout);
    LinearLayout child = (LinearLayout) inflater.inflate(R.layout.contacts, null);
    child.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
    parent.addView(child);
    
    TextView title = (TextView) findViewById(R.id.title);
    title.setText("Контакты");    
    
    ((TextView) findViewById(R.id.icq1)).setOnClickListener(new ICQClickListener());
    ((TextView) findViewById(R.id.icq2)).setOnClickListener(new ICQClickListener());
    }
    
    public class ICQClickListener implements OnClickListener{

		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			 clipboard.setText(((TextView)arg0).getText());
		}
    	
    }
}
