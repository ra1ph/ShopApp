package com.ra1ph.shopapp.update;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ra1ph.shopapp.data.DBEditor;
import com.ra1ph.shopapp.logic.ApiStruct;
import com.ra1ph.shopapp.logic.Category;
import com.ra1ph.shopapp.logic.Item;
import com.ra1ph.shopapp.logic.Parser;
import com.ra1ph.shopapp.Names;

import android.content.Context;

public class Update{
	private Context context; 
	
	private String appFld;
	
	public Update(Context context){
		appFld = Names.getAppFolder(context);
		this.context = context;
	}
	
	public void getUpdateCat(final OnUpdateListener listener1){
		final String catFile=appFld+Names.TEMP;
		final DownloadFile df = new DownloadFile(new OnUpdateListener(){

			public void OnUpdate() {
				Parser parser = new Parser(context);
				InputStream is;
				try {
					is = new FileInputStream(catFile+Names.CAT_FILE);
					ArrayList<Category> Categories = parser.parseCatFile(is);
					DBEditor dbEditor = new DBEditor(context);
					dbEditor.fillCats(Categories);
					dbEditor.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("myLogs", "Not downloading file!");
				}
				listener1.OnUpdate();
				}

			public void OnProgress(Integer prg) {
				// TODO Auto-generated method stub
				listener1.OnProgress(prg);
			}
			}
			);
		df.execute(Names.SERVER_NAME+Names.URL_CATEGORY,catFile,Names.CAT_FILE);

	}
	
	public void getUpdateItem(String url,final int id_cat,final OnUpdateListener listener1){
		final String itemFile=appFld+Names.TEMP;
		final DownloadFile df = new DownloadFile(new OnUpdateListener(){

			public void OnUpdate() {
				Parser parser = new Parser(context);
				InputStream is;
				try {
					is = new FileInputStream(itemFile+Names.ITEM_FILE+Integer.toString(id_cat)+".jsn");
					ArrayList<Item> Items = parser.parseItemFile(is);
					DBEditor dbEditor = new DBEditor(context);
					dbEditor.fillItems(Items, id_cat);
					dbEditor.close();
					listener1.OnUpdate();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("myLogs", "Not downloading file!");
				}
			}

			public void OnProgress(Integer prg) {
				// TODO Auto-generated method stub
				listener1.OnProgress(prg);
			}
		});
		df.execute(Names.SERVER_NAME+url,itemFile,Names.ITEM_FILE+Integer.toString(id_cat)+".jsn");
	}
	
	public abstract interface OnUpdateListener{
		abstract void OnUpdate();
		abstract void OnProgress(Integer prg);
	}
}