package com.ra1ph.shopapp;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.ra1ph.shopapp.data.DBEditor;
import com.ra1ph.shopapp.logic.Item;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class ItemList extends BaseActivity {

	ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	private int id_cat;
	private ArrayList<Item> items;
	private ItemAdapter adapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout parent = (LinearLayout) findViewById(R.id.parent_layout);
        LinearLayout child = (LinearLayout) inflater.inflate(R.layout.itemlist, null);
        parent.addView(child);          
      
        ListView item_list = (ListView) findViewById(R.id.itemlist);
        DBEditor dbe = new DBEditor(this);
        id_cat = this.getIntent().getIntExtra(CATEGORY_ID,NO_CATEGORY);
        String cat_name=this.getIntent().getStringExtra(CATEGORY_NAME);
        items = dbe.getItems(id_cat);
        dbe.close();
        
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(cat_name);
        
        if (Intent.ACTION_SEARCH.equals(this.getIntent().getAction())) { 
            //Берем строку запроса из экстры
            String query = this.getIntent().getStringExtra(SearchManager.QUERY);
          }
        
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_launcher)
		.cacheInMemory()
		.cacheOnDisc()
		.displayer(new RoundedBitmapDisplayer(0xff424242, 30))
		.build();

		adapter = new ItemAdapter();
	item_list.setAdapter(adapter);
	item_list.setOnItemClickListener(new OnItemClickListener() {
		
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(ItemList.this,ItemView.class);
			intent.putExtra(Names.ITEM, items.get(position).id);
			startActivity(intent);
		}
	});
    }
    
    @Override
    protected void onResume(){
    	super.onResume();
    	adapter.notifyDataSetChanged();
    }
    
	class ItemAdapter extends BaseAdapter {

		private class ViewHolder {
			public TextView name;
			public ImageView image;
			public TextView description;
			public TextView cost;
			public CheckBox compare;
			public CheckBox best;
			public CheckBox basket;
		}

		public int getCount() {
			return items.size();
		}

		
		public Object getItem(int position) {
			return position;
		}

		
		public long getItemId(int position) {
			return position;
		}

		
		public View getView(final int position, View convertView, final ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				view = getLayoutInflater().inflate(R.layout.item_list_item, null);
				holder = new ViewHolder();
				holder.name = (TextView) view.findViewById(R.id.item_name);
				holder.cost = (TextView) view.findViewById(R.id.item_cost);
				holder.description = (TextView) view.findViewById(R.id.item_description);
				holder.image = (ImageView) view.findViewById(R.id.item_image);
				holder.compare = (CheckBox) view.findViewById(R.id.btn_compare);
				holder.basket = (CheckBox) view.findViewById(R.id.btn_basket);
				holder.best = (CheckBox) view.findViewById(R.id.btn_best);
				view.setTag(holder);
			} else
				holder = (ViewHolder) view.getTag();

			holder.name.setText(items.get(position).name);
			holder.cost.setText(Double.toString(items.get(position).cost));
			holder.description.setText(items.get(position).description);
			
			holder.compare.setChecked(compare_list.search(items.get(position).id));
			holder.basket.setChecked(basket_list.search(items.get(position).id));			
			holder.compare.setOnClickListener(new View.OnClickListener(){

				public void onClick(View v) {
					// TODO Auto-generated method stub
					CheckBox chk = (CheckBox) v;
					if(chk.isChecked()) compare_list.items_compare.add(items.get(position));
					else compare_list.removeItem(items.get(position).id);
					updateFooter();
				}
				
			});
			
			holder.basket.setOnClickListener(new View.OnClickListener(){

				public void onClick(View v) {
					// TODO Auto-generated method stub
					CheckBox chk = (CheckBox) v;
					if(chk.isChecked()){
						basket_list.addItem(items.get(position), ItemList.this);
						}
					else basket_list.removeItem(items.get(position).id, ItemList.this);
				}
				
			});
		

			imageLoader.init(ImageLoaderConfiguration.createDefault(ItemList.this));
			String img = Names.SERVER_NAME + Names.IMAGE_FOLDER + items.get(position).image+"0.jpg";
			imageLoader.displayImage(img, holder.image, options);

			return view;
		}
		
	}
}