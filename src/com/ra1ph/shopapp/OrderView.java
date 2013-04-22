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

public class OrderView extends BaseActivity {

	private DisplayImageOptions options;
	private DBEditor dbe;
	private ArrayList<Item> items;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dbe = new DBEditor(this);
		int orderId = this.getIntent().getIntExtra(Names.ORDER_ID, -1);
		items = dbe.getOrderProductList(orderId);

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout parent = (LinearLayout) findViewById(R.id.parent_layout);
		LinearLayout child = (LinearLayout) inflater.inflate(
				R.layout.order_view, null);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		child.setLayoutParams(params);
		parent.addView(child);
		
		TextView title = (TextView) findViewById(R.id.title);
        title.setText("Мои заказы");
        TextView caption = (TextView) findViewById(R.id.order_caption);
        caption.setText("Заказ № " + Integer.toString(orderId));

		ListView item_list = (ListView) findViewById(R.id.basket_itemlist);

		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_launcher).cacheInMemory()
				.cacheOnDisc()
				.displayer(new RoundedBitmapDisplayer(0xff424242, 30)).build();
		final ItemAdapter adapter = new ItemAdapter();
		item_list.setAdapter(adapter);
		item_list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(OrderView.this, ItemView.class);
				intent.putExtra(Names.ITEM, items.get(position).id);
				intent.putExtra(Names.FROM_BASKET, true);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbe.close();
	}

	class ItemAdapter extends BaseAdapter {

		private class ViewHolder {
			public TextView name;
			public ImageView image;
			public TextView description;
			public TextView cost;
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

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				view = getLayoutInflater().inflate(R.layout.orderview_item,
						null);
				holder = new ViewHolder();
				holder.name = (TextView) view
						.findViewById(R.id.basket_name_item);
				holder.cost = (TextView) view
						.findViewById(R.id.basket_cost_item);
				holder.description = (TextView) view
						.findViewById(R.id.basket_description_item);
				holder.image = (ImageView) view
						.findViewById(R.id.basket_image_item);
				view.setTag(holder);
			} else
				holder = (ViewHolder) view.getTag();

			holder.name.setText(items.get(position).name);
			holder.cost.setText(Double.toString(items.get(position).cost));
			holder.description.setText(items.get(position).description);

			imageLoader.init(ImageLoaderConfiguration
					.createDefault(OrderView.this));
			String img = Names.SERVER_NAME + Names.IMAGE_FOLDER
					+ items.get(position).image + "0.jpg";
			imageLoader.displayImage(img, holder.image, options);

			return view;
		}

	}
}
