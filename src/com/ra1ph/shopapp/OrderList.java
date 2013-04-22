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
import com.ra1ph.shopapp.data.DBEditor;
import com.ra1ph.shopapp.logic.Item;
import com.ra1ph.shopapp.logic.Order;


public class OrderList extends BaseActivity {
	
	private DisplayImageOptions options;
	private DBEditor dbe;
	private ArrayList<Order> bask;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	    	
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout parent = (LinearLayout) findViewById(R.id.parent_layout);
        LinearLayout child = (LinearLayout) inflater.inflate(R.layout.orderlist, null);
        LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        child.setLayoutParams(params);
        parent.addView(child); 
        
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("Мои заказы");
        
        ListView item_list = (ListView) findViewById(R.id.orderlist);
        
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_launcher)
		.cacheInMemory()
		.cacheOnDisc()
		.displayer(new RoundedBitmapDisplayer(0xff424242, 30))
		.build();
		
		dbe = new DBEditor(this);
		bask = dbe.getOrderList();
		
		item_list.setAdapter(new ItemAdapter());
		item_list.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(OrderList.this,OrderView.class);
				intent.putExtra(Names.ORDER_ID, bask.get(position).idZakaz);
				startActivity(intent);
			}
		});
		
	    }   
    
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    dbe.close();
    }
    
    
	class ItemAdapter extends BaseAdapter {

		private class ViewHolder {
			public TextView name;
			public ImageView image;
			public TextView description;
			public TextView cost;
			public Button delete;
			public TextView idZakaz;
			public TextView status;
		}
		
		private ArrayList<Item>items;

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
			
			items = dbe.getOrderProductList(bask.get(position).idZakaz);
			if (convertView == null) {
				view = getLayoutInflater().inflate(R.layout.orderlist_item, null);
				holder = new ViewHolder();
				holder.name = (TextView) view.findViewById(R.id.orderlist_name_item);
				holder.cost = (TextView) view.findViewById(R.id.orderlist_cost_item);
				holder.description = (TextView) view.findViewById(R.id.orderlist_description_item);
				holder.image = (ImageView) view.findViewById(R.id.orderlist_image_item);
				holder.delete = (Button) view.findViewById(R.id.orderlist_delbutton);
				holder.status = (TextView) view.findViewById(R.id.orderlist_status);
				holder.idZakaz = (TextView) view.findViewById(R.id.orderlist_idZakaz);
				view.setTag(holder);
			} else
				holder = (ViewHolder) view.getTag();

			holder.name.setText(items.get(0).name);
			holder.cost.setText(Double.toString(items.get(0).cost));
			holder.description.setText(items.get(0).description);
			String id = Integer.toString(bask.get(position).idZakaz);
			holder.idZakaz.setText(id);
			holder.status.setText(bask.get(position).status);
			holder.delete.setOnClickListener(new OnClickListener(){

				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dbe.deleteOrder(bask.get(position).idZakaz);
					bask = dbe.getOrderList();
					ItemAdapter.this.notifyDataSetChanged();
				}
				
			});
			
			imageLoader.init(ImageLoaderConfiguration.createDefault(OrderList.this));
			String img = Names.SERVER_NAME + Names.IMAGE_FOLDER + items.get(0).image+"0.jpg";
			imageLoader.displayImage(img, holder.image, options);

			return view;
		}
		
	}
    }
