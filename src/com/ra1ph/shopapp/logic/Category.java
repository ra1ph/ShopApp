package com.ra1ph.shopapp.logic;

import org.json.JSONException;
import org.json.JSONObject;

public class Category {
public int id;
public String name,link;

public Category(int id, String name){
	this.id=id;
	this.name=name;
}

public static Category ParseCat(JSONObject o) throws JSONException{
	Category cat = new Category(o.getInt("id"),o.getString("name"));
	cat.link = o.getString("link");
	return cat;
}
}
