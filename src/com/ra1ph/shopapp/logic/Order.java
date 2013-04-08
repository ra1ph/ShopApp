package com.ra1ph.shopapp.logic;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Order{
	public int idZakaz;
	public String status,remark,sklad_dt;
	public ArrayList<Product> products;

	public static Order parseOrder(JSONObject o) throws JSONException{
		Order order = new Order();
		order.idZakaz = o.getInt("idZakaz");
		order.status = o.getString("Status");
		order.remark = o.getString("remark");
		order.sklad_dt = o.getString("sklad_dt");
		order.products = new ArrayList<Product>();
		JSONArray arr = new JSONArray();
		arr = o.getJSONArray("products");
		int i=0;
		for(i=0;i<arr.length();i++){
			Product prod = Product.parseProduct(arr.getJSONObject(i));
			order.products.add(prod);
		}
		return order;		
	}
	
	public static class Product{
		public int idProd;
		public int count;
		public ArrayList<Option> options;
		public Product(int idProd,int count){
			this.idProd=idProd;
			this.count=count;
			this.options = new ArrayList<Option>();
		}
		
		public void addOption(ArrayList<String> value, String name){
			Option opt = new Option(value,name);
			options.add(opt);
		}
		
		public static Product parseProduct(JSONObject o) throws JSONException{
			Product product = new Product(o.getInt("idProd"),1); 
			return product;			
		}
	}
	
	public static class Option{
		public ArrayList<String> value;
		public String name;
		public Option(ArrayList<String> value, String name){
			this.value = value;
			this.name = name;
		}
		
		public static ArrayList<Option> parseOptions(JSONArray opts){
			ArrayList<Option> options = new ArrayList<Option>();
			int i=0;
			while(i<opts.length()){
				JSONObject obj;
				try {
					Option opt;
					obj = opts.getJSONObject(i);
					ArrayList<String> str = new ArrayList<String>();
					JSONArray arr = obj.getJSONArray("values");
					int j=0;
					for(j=0;j<arr.length();j++){
						str.add(arr.getString(j));
					}
					opt = new Option(str,obj.getString("name"));
					options.add(opt);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i++;
			}
			return options;
		}
	}
	
	public static String ArrayToString(ArrayList<Option> opts){
		String result="";
		int i=0;
		JSONArray arr = new JSONArray();
		for(i=0;i<opts.size();i++){
			JSONObject o = new JSONObject();
			try {
				JSONArray str = new JSONArray();
				int j=0;
				for(j=0;j<opts.size();j++){
					str.put(opts.get(i).value.get(j));
				}
				o.put("values", str);
				o.put("name", opts.get(i).name);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			arr.put(o);
		}
		result = arr.toString();
		return result;
	}
}