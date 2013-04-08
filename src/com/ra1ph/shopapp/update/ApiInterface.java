package com.ra1ph.shopapp.update;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ra1ph.shopapp.Names;
import com.ra1ph.shopapp.logic.ApiMultiStruct;
import com.ra1ph.shopapp.logic.ApiStruct;
import com.ra1ph.shopapp.logic.Order;
import com.ra1ph.shopapp.logic.Order.Product;
import com.ra1ph.shopapp.update.ApiMultiRequest.OnMultiResultListener;

public class ApiInterface {
	ApiRequest api;
	OnResultListener listener;
	
	public ApiInterface(){
		api=new ApiRequest();
	}
	
	public void setOrderStatus(int zakazId, String idUser, String secretId, String status, String date){
		JSONObject req = new JSONObject();
		try {
			req.put("id", "SETSTATUS");
			req.put("idUser", URLEncoder.encode(idUser));
			req.put("secretId", URLEncoder.encode(secretId));
			req.put("zakazId" , zakazId);
			req.put("sklad_dt", URLEncoder.encode(date));
			req.put("Status", URLEncoder.encode(status));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String url = Names.API_URL + req.toString();
		Log.i("API", url);
		
		ApiStruct str = new ApiStruct();
		str.type=0;
		str.request=url;
		str.listener = listener;
		api.execute(str);
	}
	
	public void updateOrdersStatuses(ArrayList<Order> orders, String idUser, String secretId,OnMultiResultListener list){
		ApiMultiStruct struct = new ApiMultiStruct();
		for(int i=0;i<orders.size();i++){
			
			JSONObject req = new JSONObject();
			try {
			req.put("id", "GETSTATUS");
			req.put("idUser", idUser);
			req.put("secretId", secretId);
			req.put("zakazId", orders.get(i).idZakaz);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String url = Names.API_URL + req.toString();
			Log.i("API", url);
		
			struct.request.add(url);
		}
		struct.listener = list;
		ApiMultiRequest api = new ApiMultiRequest();
		api.execute(struct);
	}
		
	public void getOrderList(String idUser,String secretId) throws JSONException{
		JSONObject req = new JSONObject();
		req.put("id", "ZLIST");
		req.put("idUser", idUser);
		req.put("secretId", secretId);
		String url = Names.API_URL + req.toString();
		Log.i("API", url);
		
		ApiStruct str = new ApiStruct();
		str.type=0;
		str.request=url;
		str.listener = listener;
		api.execute(str);
	}
	
	public void sendOrder(ArrayList<Product> products,String idUser,String secretId,String fio, String contact) throws JSONException{
		JSONObject req = new JSONObject();
		req.put("id", "ZAKAZ");
		req.put("idUser", URLEncoder.encode(idUser));
		req.put("secretId", URLEncoder.encode(secretId));
		req.put("fio", URLEncoder.encode(fio));
		req.put("contact", URLEncoder.encode(contact));
		int i=0;
		JSONArray arr = new JSONArray();
		
		while(i<products.size())
		{
		JSONObject o = new JSONObject();
		o.put("idProd", products.get(i).idProd);
		JSONArray opts = new JSONArray();
		int j;
		for(j=0;j<products.get(i).options.size();j++){
			JSONObject obj = new JSONObject();
			obj.put("name", products.get(i).options.get(j).name);
			obj.put("value", products.get(i).options.get(j).value.get(0));
			opts.put(obj);
		}
		o.put("Option", opts);		
		arr.put(o);
		i++;
		}
		
		req.put("Products", arr);
		String url = Names.API_URL + req.toString();
		Log.i("API", url);
		
		ApiStruct str = new ApiStruct();
		str.type=0;
		str.request=url;
		str.listener = listener;
		api.execute(str);		
	}
	
	public void setOnResultListener(OnResultListener listener){
		this.listener = listener;
	}
	
	public abstract interface OnResultListener{
		abstract void OnResult(ApiStruct reply);
	}
	
}