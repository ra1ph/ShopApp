package com.ra1ph.shopapp;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.ra1ph.shopapp.data.DBEditor;
import com.ra1ph.shopapp.logic.Item;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ItemView extends BaseActivity {
	private Item item;
	private Gallery gallery;
	
	private DisplayImageOptions options;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout parent = (LinearLayout) findViewById(R.id.parent_layout);
        LinearLayout child = (LinearLayout) inflater.inflate(R.layout.item_view, null);
        LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        child.setLayoutParams(params);
        parent.addView(child);
       
        DBEditor dbe = new DBEditor(this);
        item = dbe.getItem(this.getIntent().getIntExtra(Names.ITEM,-1));
        dbe.close();
        
        TextView addToBasket = (TextView) findViewById(R.id.add_basket);
        boolean fromBasket = this.getIntent().getBooleanExtra(Names.FROM_BASKET, false);
        if(fromBasket) addToBasket.setVisibility(View.INVISIBLE);
        else addToBasket.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				basket_list.addItem(item, ItemView.this);
				v.setVisibility(View.INVISIBLE);
			}
        	
        });
                
		options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.ic_launcher)
		.showStubImage(R.drawable.ic_launcher)
		.cacheInMemory()
		.cacheOnDisc()
		.build();

	gallery = (Gallery) findViewById(R.id.gallery);
	gallery.setAdapter(new ImagePagerAdapter(item));
	gallery.setOnItemClickListener(new OnItemClickListener(){

		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(ItemView.this,ImageViewer.class);
			intent.putExtra(Names.ITEM, item.id);
			intent.putExtra(Names.FOTO_ID, position);
			startActivity(intent);
		}
		
	});
	
	TextView desc = (TextView) findViewById(R.id.description_text);
	TextView cost = (TextView) findViewById(R.id.cost_val);
	TextView title = (TextView) findViewById(R.id.title);
    title.setText(item.name);
	desc.setText(item.description);
	cost.setText(Double.toString(item.cost));
    }
    
	private class ImagePagerAdapter extends BaseAdapter {

		private Item item;
		private LayoutInflater inflater;

		public ImagePagerAdapter(Item item) {
			this.item = item;
			inflater = getLayoutInflater();
		}

		
		public int getCount() {
			return item.num_photo;
		}

		
		public Object getItem(int position) {
			return position;
		}

		
		public long getItemId(int position) {
			return position;
		}

		
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView = (ImageView) convertView;
			if (imageView == null) {
				imageView = (ImageView) inflater.inflate(R.layout.item_gallery_image, parent, false);
				
			}
			String img = Names.SERVER_NAME + Names.IMAGE_FOLDER + item.image+Integer.toString(position)+".jpg";
			imageLoader.displayImage(img, imageView, options);
			return imageView;
		}
	}
}