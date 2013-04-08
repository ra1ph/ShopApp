package com.ra1ph.shopapp.logic;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.ra1ph.shopapp.R;
import com.ra1ph.shopapp.logic.Order.Option;

public class Basket{
	public ArrayList<Item> items_basket;
	public static Basket instance;
	
public Basket(){
	items_basket = new ArrayList<Item>();
}

public boolean search(int id){
	int i=0;
	for(i=0;i<items_basket.size();i++) if(items_basket.get(i).id==id) return true;
	return false;
}

public void removeItem(int id){
	int i=0;
	for(i=0;i<items_basket.size();i++) if(items_basket.get(i).id==id) items_basket.remove(i);
}

public void clear(){
items_basket.clear();
}

public void addItem(final Item item,Activity context)
{
	if(item.options.size()>0){
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    //Inflate the view from a predefined XML layout
    View layout = inflater.inflate(R.layout.option_popup,null);
    // create a 300px width and 470px height PopupWindow
    final PopupWindow pw = new PopupWindow(layout, LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    // display the popup in the center
    pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
    LinearLayout labels = (LinearLayout) layout.findViewById(R.id.conainer_option_label);
    final LinearLayout values = (LinearLayout) layout.findViewById(R.id.conainer_option);    
    int i = 0;
    for(i=0;i<item.options.size();i++){
    	TextView label = new TextView(context);
    	label.setText(item.options.get(i).name);
    	Spinner value = new Spinner(context);
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item);
    	int j=0;
    	for(j=0;j<item.options.get(i).value.size();j++){
    		adapter.add(item.options.get(i).value.get(j));
    	}
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	value.setAdapter(adapter);
    	labels.addView(label);
    	values.addView(value);
    }
    Button btn = (Button) layout.findViewById(R.id.select_option);
    btn.setOnClickListener(new OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub
			ArrayList<Option> options = new ArrayList<Option>();
			int i=0;
			for(i=0;i<item.options.size();i++){
				Spinner spn = (Spinner) values.getChildAt(i);
				ArrayList<String> arr = new ArrayList<String>();
				arr.add(item.options.get(i).value.get(spn.getSelectedItemPosition()));
				Option opt = new Option(arr,item.options.get(i).name);
				options.add(opt);
			}
			item.options=options;
			items_basket.add(item);
			pw.dismiss();
		}
    
    });
	}else{
		items_basket.add(item);
	}
}

public static Basket getInstance() {
    if (instance == null) {
        instance = new Basket();
    }
    return instance;
}
}