package com.ra1ph.shopapp.logic;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ra1ph.shopapp.Names;

import android.content.Context;
import android.util.Log;

public class Parser{
	private Context context; 
	
	public Parser(Context context){
		this.context=context;
	}
	
	public String parseZakazStatus(String reply){
    	JSONObject o = null;
		try {
			o = new JSONObject(reply);
    	return o.getString(Names.ZAKAZ_STATUS);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
    public ArrayList<Category> parseCatFile(InputStream is){
        try
        {
            String x = "";
            int nn = is.available();
            byte [] buffer = new byte[nn];
            while (is.read(buffer) != -1);
            String jsontext = new String(buffer,"UNICODE");
            JSONArray entries = new JSONArray(jsontext);
            ArrayList<Category> Categories = new ArrayList<Category>();

            int i;
            for (i=0;i<entries.length();i++)
            {
                JSONObject o = entries.getJSONObject(i);
                Category cat = Category.ParseCat(o);
                Categories.add(cat);
            }
            Log.d("myLogs", x);
            return Categories;
        }
        catch (Exception je)
        {
            System.err.println("Error: " + je.getMessage());
            return null;
        }
    }
    
    public ArrayList<Item> parseItemFile(InputStream is){
        try
        {
            String x = "";
            byte [] buffer = new byte[is.available()];
            while (is.read(buffer) != -1);
            String jsontext = new String(buffer,"UNICODE");
            JSONArray entries = new JSONArray(jsontext);
            ArrayList<Item> Items = new ArrayList<Item>();
            
            int i;
            for (i=0;i<entries.length();i++)
            {
                JSONObject o = entries.getJSONObject(i);
                Item item = Item.ParseItem(o);
                Items.add(item);
            }
            Log.d("myLogs", x);
            return Items;
        }
        catch (Exception je)
        {
            System.err.println("Error: " + je.getMessage());
            return null;
        }
    }
    
    public ArrayList<Order> parseOrderList(ApiStruct reply) throws JSONException{
    	JSONObject o = new JSONObject(reply.request);
    	if(o.getInt("error")==0){
    	JSONArray list = o.getJSONArray("list");
    	ArrayList<Order> orders = new ArrayList<Order>();
    	
        int i;
        for (i=0;i<list.length();i++){
        	Order order = Order.parseOrder(list.getJSONObject(i));
        	orders.add(order);
        }        
    	return orders;
    	}
    	else{ 
    		Log.e("API", "Error code:" + Integer.toString(o.getInt("error")));
    		return null; 
    	}
    }
    
    public String parseSecretId(String reply){
    	try {
    		JSONObject o = new JSONObject(reply);
			return o.getString(Names.SECRET_ID);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	
    	return "";
    }
    
    public int parseErrorCode(String reply){
    	JSONObject o;
		try {
			o = new JSONObject(reply);
			return o.getInt(Names.ERROR); 
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	   	return -1;
    }
    
    public int parseZakazId(String reply){
    	JSONObject o;
		try {
			o = new JSONObject(reply);
	    	return o.getInt(Names.ZAKAZ_NUM); 
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   	
		return -1;
    }
}