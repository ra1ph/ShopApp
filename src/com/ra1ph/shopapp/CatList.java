package com.ra1ph.shopapp;

import java.util.ArrayList;

import com.ra1ph.shopapp.data.DBEditor;
import com.ra1ph.shopapp.logic.Category;
import com.ra1ph.shopapp.update.Update;
import com.ra1ph.shopapp.update.Update.OnUpdateListener;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class CatList extends BaseActivity {
	
	private ArrayList<Category> cats;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);   
        setContentView(R.layout.template_1);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //LinearLayout template_parent = (LinearLayout) findViewById(R.id.template_parent);
            
        LinearLayout parent = (LinearLayout) findViewById(R.id.parent_layout);
        LinearLayout child = (LinearLayout) inflater.inflate(R.layout.catlist, null);
        parent.addView(child);
        
        cats = new ArrayList<Category>();
        DBEditor dbe = new DBEditor(this); 
        cats = dbe.getCats();
        dbe.close();
             
        ListView cat_list = (ListView) findViewById(R.id.catlist);
        cat_list.setAdapter(new CatAdapter());
    	cat_list.setOnItemClickListener(new OnItemClickListener() {
    		
    		public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
    	        Update update = new Update(CatList.this);
    	        final ProgressDialog progDialog = new ProgressDialog(CatList.this);
    	        progDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    	        progDialog.setMax(100);
    	        progDialog.setProgress(0);
    	        progDialog.show();
    			update.getUpdateItem(cats.get(position).link, cats.get(position).id,new OnUpdateListener(){

    				public void OnUpdate() {
    					// TODO Auto-generated method stub
    					Log.d("myLogs", "Update complete!");
    					progDialog.dismiss();
    	    			Intent intent = new Intent(CatList.this,ItemList.class);
    	    			intent.putExtra(CATEGORY_ID, cats.get(position).id);
    	    			startActivity(intent);
    				}

					public void OnProgress(Integer prg) {
						// TODO Auto-generated method stub
						progDialog.setProgress(prg);
					}
    			});
    			

    		}
    	});
        }
        
    
	class CatAdapter extends BaseAdapter {

		private class ViewHolder {
			public TextView text;
		}

		
		public int getCount() {
			return cats.size();
		}

		
		public Object getItem(int position) {
			return position;
		}

		
		public long getItemId(int position) {
			return position;
		}

		
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				view = getLayoutInflater().inflate(R.layout.cat_list_item, parent,false);
				holder = new ViewHolder();
				holder.text = (TextView) view.findViewById(R.id.cat_name);
				view.setTag(holder);
			} else
				holder = (ViewHolder) view.getTag();

			holder.text.setText(cats.get(position).name);

			return view;
		}
	}
}