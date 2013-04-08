package com.ra1ph.shopapp.logic;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ra1ph.shopapp.logic.Order.Option;

import android.graphics.BitmapFactory.Options;

public class Item implements Serializable {
	public int id,num_photo;
	public String name,image,description;
	public double cost;
	public ArrayList<Option> options; 
	
	public Item(int id,String name,String description){
		this.id=id;
		this.name=name;
		this.description=description;
		this.options = new ArrayList<Option>();
	}
	
	public void addOption(ArrayList<String> value, String name){
		Option opt = new Option(value,name);
		options.add(opt);
	}

	public static Item ParseItem(JSONObject o) throws JSONException{
		Item item = new Item(o.getInt("id"),o.getString("name"),o.getString("description"));
		item.cost = o.getDouble("cost");
		item.image = o.getString("image");
		item.num_photo = o.getInt("num_photos");
		JSONArray opts = o.getJSONArray("Options");
		item.options = Option.parseOptions(opts);
		return item;
	}
}
