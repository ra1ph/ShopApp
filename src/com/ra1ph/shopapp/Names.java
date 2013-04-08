package com.ra1ph.shopapp;

import android.content.Context;

public class Names{
	public static int UPDATE_ALARM = 100;
	
	public static String SERVER_NAME = "http://eat.dp.ua/shopapp/";
	public static String API_URL = "http://eat.dp.ua/torgAPI/ajax.php?query=";
	
	public static String IMAGE_FOLDER = "images/";
	public static String URL_CATEGORY="cat.jsn";
	public static String TEMP="tmp//";
	public static String CAT_FILE="cat.jsn";
	public static String ITEM_FILE="item";
	public static String DB_NAME = "shop.db"; 
	public static String ZAKAZ_NUM = "zakazId"; 
	public static String APP_NAME = "ShopApp";
	public static String ZAKAZ_STATUS = "status"; 
		
	public static String ST_NEW = "N";
	public static String ST_DELIVERED = "W";
	public static String ST_WILLTAKE = "V";
	public static String ST_WILLTAKEBYTIME = "T";
	public static String ST_GOTTEN = "E";
	
	public static String ITEM = "Item";
	public static String FOTO_ID = "foto_id";
	public static String FIO = "fio";
	public static String TEL = "tel";
	public static String SECRET_ID = "secretId";
	public static String ERROR = "error";
	public static String NOTIFY_NUM = "notifyNum";
	
	public static String NOTIFY_TYPE = "notifyType";
	public static int NOTIFY_ONCE = 0;
	public static int NOTIFY_RECEDIVE = 1;
	
	public static String DIALOG_RESULT="dialogResult";
	public static int DIALOG_OK=1;
	public static int DIALOG_NO=0;
	
	
	public static String getAppFolder(Context context){
		String packageName = context.getPackageName();
		String appFld = String.format("//data//data//%s//", packageName);
		return appFld;
	}
}