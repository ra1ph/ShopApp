package com.ra1ph.shopapp.data;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;

import com.ra1ph.shopapp.Names;
import com.ra1ph.shopapp.logic.Category;
import com.ra1ph.shopapp.logic.Item;
import com.ra1ph.shopapp.logic.Order;
import com.ra1ph.shopapp.logic.Order.Option;
import com.ra1ph.shopapp.logic.Order.Product;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DBEditor {
	private SQLiteDatabase db;
	
	public static String TBL_CATS = "tbl_cats";
	public static String TBL_ORDERS = "tbl_orders";
	public static String TBL_PRODUCTS = "tbl_products";
	public static String TBL_OPTIONS = "tbl_options";
	public static String TBL_ITEMS = "tbl_items_";
	public static String TBL_NOTIFY = "tbl_notifys";
	
	private Context context;
	private String appFld;
	
	
	public DBEditor(Context context){
		this.context = context;
		appFld = Names.getAppFolder(context);
		db = context.openOrCreateDatabase(Names.DB_NAME, 0, null);
		createTables();
		
	}
	
	public void createTables(){
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TBL_ORDERS + " (_id INTEGER PRIMARY KEY, idZakaz INTEGER, status TEXT, remark TEXT, sklad_dt);");
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TBL_OPTIONS + " (_id INTEGER PRIMARY KEY, idZakaz INTEGER, idProd INTEGER, name TEXT, value TEXT);");
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TBL_PRODUCTS + " (_id INTEGER PRIMARY KEY, idZakaz INTEGER, idProd INTEGER);");
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TBL_CATS + " (_id INTEGER PRIMARY KEY, num INTEGER, name TEXT, link TEXT, storage TEXT);");
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TBL_NOTIFY + " (_id INTEGER PRIMARY KEY, num INTEGER, zakazId INTEGER, time LONG);");
	}
	
	public int getNotifyNum(int zakazId){
		String whereClause = "zakazId=?";
		Cursor cursor = db.query(TBL_NOTIFY, null, whereClause, new String[]{Integer.toString(zakazId)}, null, null, null);
		int notifyNum = -1;
		if(cursor.getCount()>0){
		cursor.moveToLast();
		notifyNum = cursor.getInt(cursor.getColumnIndex("num")) + 1;
		cursor.close();
		}
		return notifyNum;
	}
	
	public int addNotify(int zakazId, long time){
		Cursor cursor = db.query(TBL_NOTIFY, null, null, null, null, null, null);
		int notifyNum = 0;
		if(cursor.getCount()>0){
		cursor.moveToLast();
		notifyNum = cursor.getInt(cursor.getColumnIndex("num")) + 1;
		}
		cursor.close();
		
		ContentValues val = new ContentValues();
		val.put("zakazId", zakazId);
		val.put("num", notifyNum);
		val.put("time", time);
		db.insert(TBL_NOTIFY, null, val);
		
		return notifyNum;
	}
	
	public void delNotify(int zakazId){
		String whereClause = "zakazId = ?";
		db.delete(TBL_NOTIFY, whereClause, new String[]{Integer.toString(zakazId)});
	}
	
	public void updateOrders(ArrayList<Order> orders){
	String filter = "idZakaz=?";
	for(int i=0;i<orders.size();i++){
		ContentValues val = new ContentValues();
		val.put("idZakaz", orders.get(i).idZakaz);
		val.put("status", orders.get(i).status);
		val.put("remark", orders.get(i).remark);
		val.put("sklad_dt", orders.get(i).sklad_dt);
		db.update(TBL_ORDERS, val, filter, new String[]{Integer.toString(orders.get(i).idZakaz)});
	}
	}
	
	public boolean isZakazList(){
	db.execSQL("CREATE TABLE IF NOT EXISTS " + TBL_ORDERS + " (_id INTEGER PRIMARY KEY, idZakaz INTEGER, status TEXT, remark TEXT, sklad_dt);");
	Cursor cursor = db.rawQuery("select * from '"+TBL_ORDERS+"'", null);
	int i=cursor.getCount();
	cursor.close();
	if(i>0)return true;
	else return false;
	}
	
	public void addOptions(ArrayList<Option> options, int idZakaz, int idProd)
	{
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TBL_OPTIONS + " (_id INTEGER PRIMARY KEY, idZakaz INTEGER, idProd INTEGER, name TEXT, value TEXT);");
		for(int i=0;i<options.size();i++){
			Option option = options.get(i);
			ContentValues val = new ContentValues();
			val.put("name", option.name);
			val.put("value", option.value.get(0));
			val.put("idZakaz", idZakaz);
			val.put("idProd", idProd);
			db.insert(TBL_OPTIONS, null, val);
		}
	
	}
	
	public void addOrder(Order order){
		int id=0;
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TBL_ORDERS + " (_id INTEGER PRIMARY KEY, idZakaz INTEGER, status TEXT, remark TEXT, sklad_dt);");
		Cursor cursor = db.rawQuery("select * from '"+TBL_ORDERS+"'", null);
		cursor.moveToLast();
			ContentValues val = new ContentValues();
			val.put("idZakaz", order.idZakaz);
			val.put("status", order.status);
			val.put("remark", order.remark);
			val.put("sklad_dt", order.sklad_dt);
			int i=0;
			for(i=0;i<order.products.size();i++){
				addProduct(order.idZakaz,order.products.get(i));
				addOptions(order.products.get(i).options,order.idZakaz,order.products.get(i).idProd);
			}
			db.insert(TBL_ORDERS, null, val);
		cursor.close();
	}
	
	public void addProduct(int idZakaz,Product prod){
		int id=0;
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TBL_PRODUCTS + " (_id INTEGER PRIMARY KEY, idZakaz INTEGER, idProd INTEGER);");
		Cursor cursor = db.rawQuery("select * from '"+TBL_PRODUCTS+"'", null);
			cursor.moveToLast();
			ContentValues val = new ContentValues();
			val.put("idProd", prod.idProd);
			val.put("idZakaz", idZakaz);
			db.insert(TBL_PRODUCTS, null, val);
		cursor.close();
	}
	
	public void fillCats(ArrayList<Category> cats){
		db.execSQL("DROP TABLE IF EXISTS "+TBL_CATS);
		db.execSQL("CREATE TABLE " + TBL_CATS + " (_id INTEGER PRIMARY KEY, num INTEGER, name TEXT, link TEXT, storage TEXT);");
		int i=0;
		for(i=0;i<cats.size();i++){
			ContentValues val = new ContentValues();
			val.put("_id", Integer.toString(i));
			val.put("num", cats.get(i).id);
			val.put("name", cats.get(i).name);
			val.put("link", cats.get(i).link);
			db.insert(TBL_CATS, null, val);
		}
	}
	
	public void fillItems(ArrayList<Item> items, int id_cat){
			db.execSQL("DROP TABLE IF EXISTS "+TBL_ITEMS+Integer.toString(id_cat));
			db.execSQL("CREATE TABLE " + TBL_ITEMS+Integer.toString(id_cat) + " (_id INTEGER PRIMARY KEY, num INTEGER, name TEXT, image TEXT, id_cat INTEGER, description TEXT, cost REAL, num_photo INTEGER, options TEXT);");
			int i=0;
			for(i=0;i<items.size();i++){
				ContentValues val = new ContentValues();
				val.put("_id", Integer.toString(i));
				val.put("num", items.get(i).id);
				val.put("num_photo", items.get(i).num_photo);
				val.put("name", items.get(i).name);
				val.put("image", items.get(i).image);
				val.put("description", items.get(i).description);
				val.put("cost", items.get(i).cost);
				val.put("options", Order.ArrayToString(items.get(i).options));
				db.insert(TBL_ITEMS+Integer.toString(id_cat), null, val);
			}
	}
	
	public ArrayList<Category> getCats(){
		Cursor cursor = db.query(TBL_CATS, null, null, null, null, null, null);
		ArrayList<Category> cats = new ArrayList<Category>();
		if((cursor!=null)&&(cursor.getCount()!=0)){
		int i=0;
		cursor.moveToFirst();
		for(i=0;i<cursor.getCount();i++){
			Category cat = new Category(cursor.getInt(cursor.getColumnIndex("num")), cursor.getString(cursor.getColumnIndex("name")));
			cat.link = cursor.getString(cursor.getColumnIndex("link"));
			cats.add(cat);
			cursor.moveToNext();
		}
		}
		cursor.close();
		return cats;		
	}	
	
	public ArrayList<Item> getItems(int id_cat){
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TBL_ITEMS+Integer.toString(id_cat) + " (_id INTEGER PRIMARY KEY, num INTEGER, name TEXT, image TEXT, id_cat INTEGER, description TEXT, cost REAL, num_photo INTEGER, options TEXT);");
		Cursor cursor = db.query(TBL_ITEMS+Integer.toString(id_cat), null, null, null, null, null, null);
		ArrayList<Item> items = new ArrayList<Item>();
		if((cursor!=null)&&(cursor.getCount()!=0)){
		int i=0;
		cursor.moveToFirst();
		for(i=0;i<cursor.getCount();i++){
			Item item = new Item(cursor.getInt(cursor.getColumnIndex("num")), cursor.getString(cursor.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("description")));
			item.image = cursor.getString(cursor.getColumnIndex("image"));
			item.cost = cursor.getDouble(cursor.getColumnIndex("cost"));
			item.num_photo = cursor.getInt(cursor.getColumnIndex("num_photo"));
			JSONArray arr;
			try {
				arr = new JSONArray(cursor.getString(cursor.getColumnIndex("options")));
				item.options = Option.parseOptions(arr);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			items.add(item);
			cursor.moveToNext();
		}
		}
		cursor.close();
		return items;		
	}
	
	public ArrayList<Order> getOrderList(){
		Cursor cursor = db.query(TBL_ORDERS, null, null, null, null, null, null);
		ArrayList<Order> items = new ArrayList<Order>();
		if(cursor.getCount()>0){
		int i=0;
		cursor.moveToFirst();
		for(i=0;i<cursor.getCount();i++){
			Order item = new Order();
			item.idZakaz = cursor.getInt(cursor.getColumnIndex("idZakaz"));
			item.remark = cursor.getString(cursor.getColumnIndex("remark"));
			item.sklad_dt = cursor.getString(cursor.getColumnIndex("sklad_dt"));
			item.status = cursor.getString(cursor.getColumnIndex("status"));
			items.add(item);
			cursor.moveToNext();
		}
		}
		cursor.close();
		return items;
	}
	
	public ArrayList<Item> getOrderProductList(int idZakaz){
	String select = "idZakaz = ?";
	Cursor cursor = db.query(TBL_PRODUCTS, null, select, new String[]{Integer.toString(idZakaz)}, null, null, null);
	ArrayList<Item> arr = new ArrayList<Item>();
	cursor.moveToFirst();
	int i;
	for(i=0;i<cursor.getCount();i++){
		int g = cursor.getInt(cursor.getColumnIndex("idProd"));
		arr.add(getItem(g));
		cursor.moveToNext();
	}
	cursor.close();
	return arr;
	}
	
	public void deleteOrder(int idZakaz){
		String where = "idZakaz = ?";
		db.delete(TBL_ORDERS, where, new String[]{Integer.toString(idZakaz)});
		deleteProducts(idZakaz);
	}
	
	public void deleteProducts(int idZakaz){
		String where = "idZakaz = ?";
		db.delete(TBL_PRODUCTS, where, new String[]{Integer.toString(idZakaz)});
	}
	
	public Item getItem(int pId){
		ArrayList<Category> cats = getCats();
		int i;
		for(i=0;i<cats.size();i++){
		ArrayList<Item> items = getItems(cats.get(i).id);
		int j;
		for(j=0;j<items.size();j++){
			if(items.get(j).id==pId)return items.get(j);
		}
		}
		return new Item(0,"","");
	}
	
	public void setOrderStatus(){
	
	}
	
	public void close(){
		db.close();
	}
}
