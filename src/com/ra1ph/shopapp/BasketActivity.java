package com.ra1ph.shopapp;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.ra1ph.shopapp.ItemList.ItemAdapter;
import com.ra1ph.shopapp.logic.Item;


public class BasketActivity extends BaseActivity {
	
	private DisplayImageOptions options;
	private ArrayList<Item> bask;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	bask = basket_list.items_basket;
    	
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout parent = (LinearLayout) findViewById(R.id.parent_layout);
        LinearLayout child = (LinearLayout) inflater.inflate(R.layout.basket_list, null);
        LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        child.setLayoutParams(params);
        parent.addView(child); 
        
        ListView item_list = (ListView) findViewById(R.id.basket_itemlist);
        
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_launcher)
		.cacheInMemory()
		.cacheOnDisc()
		.displayer(new RoundedBitmapDisplayer(0xff424242, 30))
		.build();
		final ItemAdapter adapter = new ItemAdapter();
		item_list.setAdapter(adapter);
		item_list.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(BasketActivity.this,ItemView.class);
				intent.putExtra(Names.ITEM, bask.get(position));
				startActivity(intent);
			}
		});
		
		Button order = (Button) findViewById(R.id.confirm_order);
		order.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				Intent intent = new Intent(BasketActivity.this,NewOrder.class);
				startActivity(intent);
				adapter.notifyDataSetChanged();
				finish();
			}
			
		});
	    }
	class ItemAdapter extends BaseAdapter {

		private class ViewHolder {
			public TextView name;
			public ImageView image;
			public TextView description;
			public TextView cost;
			public Button delete;
		}

		public int getCount() {
			return bask.size();
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
				view = getLayoutInflater().inflate(R.layout.basket_list_item, null);
				holder = new ViewHolder();
				holder.name = (TextView) view.findViewById(R.id.basket_name_item);
				holder.cost = (TextView) view.findViewById(R.id.basket_cost_item);
				holder.description = (TextView) view.findViewById(R.id.basket_description_item);
				holder.image = (ImageView) view.findViewById(R.id.basket_image_item);
				view.setTag(holder);
			} else
				holder = (ViewHolder) view.getTag();

			holder.name.setText(bask.get(position).name);
			holder.cost.setText(Double.toString(bask.get(position).cost));
			holder.description.setText(bask.get(position).description);
			
			imageLoader.init(ImageLoaderConfiguration.createDefault(BasketActivity.this));
			String img = Names.SERVER_NAME + Names.IMAGE_FOLDER + bask.get(position).image+"0.jpg";
			imageLoader.displayImage(img, holder.image, options);

			return view;
		}
		
	}
    }
