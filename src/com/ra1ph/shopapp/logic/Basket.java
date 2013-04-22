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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ra1ph.shopapp.BaseActivity;
import com.ra1ph.shopapp.R;
import com.ra1ph.shopapp.logic.Order.Option;

public class Basket{
	public ArrayList<Item> items_basket;
	public static Basket instance;	
	public double summaryCost=0;
	
public Basket(){
	items_basket = new ArrayList<Item>();
}

public boolean search(int id){
	int i=0;
	for(i=0;i<items_basket.size();i++) if(items_basket.get(i).id==id) return true;
	return false;
}

public void removeItem(int id, BaseActivity context){
	int i=0;
	for(i=0;i<items_basket.size();i++) if(items_basket.get(i).id==id) items_basket.remove(i);
	updateSummaryCost();
	context.updateFooter();
}

public void clear(BaseActivity context){
items_basket.clear();
updateSummaryCost();
context.updateFooter();
}

public void updateSummaryCost(){
	summaryCost=0;
	for(int i=0;i<items_basket.size();i++){
		summaryCost+=items_basket.get(i).cost;
	}
}

public void addItem(final Item item,final BaseActivity context)
{
	if(item.options.size()>0){
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    //Inflate the view from a predefined XML layout
    View layout = inflater.inflate(R.layout.option_popup,null);
    // create a 300px width and 470px height PopupWindow
    
    final TableLayout values = (TableLayout) layout.findViewById(R.id.option_container);    
    int i = 0;
    for(i=0;i<item.options.size();i++){
    	
    	TableRow container = new TableRow(context);
    	container.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
    	container.setOrientation(LinearLayout.HORIZONTAL);
    	TextView label = (TextView) inflater.inflate(R.layout.option_caption_template, null);
    	label.setText(item.options.get(i).name);
    	Spinner value = new Spinner(context);
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item);
    	int j=0;
    	for(j=0;j<item.options.get(i).value.size();j++){
    		adapter.add(item.options.get(i).value.get(j));
    	}
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	value.setAdapter(adapter);
    	//value.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
    	container.addView(label);
    	container.addView(value);
    	values.addView(container);
    }
    Button btn = (Button) layout.findViewById(R.id.select_option);
    
    final PopupWindow pw = new PopupWindow(layout, LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    // display the popup in the center
    pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
    
    btn.setOnClickListener(new OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub
			ArrayList<Option> options = new ArrayList<Option>();
			int i=0;
			for(i=0;i<item.options.size();i++){
				Spinner spn = (Spinner) ((LinearLayout) values.getChildAt(i)).getChildAt(1);
				ArrayList<String> arr = new ArrayList<String>();
				arr.add(item.options.get(i).value.get(spn.getSelectedItemPosition()));
				Option opt = new Option(arr,item.options.get(i).name);
				options.add(opt);
			}
			item.options=options;
			items_basket.add(item);
			updateSummaryCost();
			context.updateFooter();
			pw.dismiss();
		}
    
    });
    
	}else{
		items_basket.add(item);
		updateSummaryCost();
		context.updateFooter();
	}
	
}

public static Basket getInstance() {
    if (instance == null) {
        instance = new Basket();
    }
    return instance;
}
}