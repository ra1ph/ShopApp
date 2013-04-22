package com.ra1ph.shopapp;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.ra1ph.shopapp.logic.Item;

public class CompareActivity extends BaseActivity {
	
	private DisplayImageOptions options;
	
	private ArrayList<Item> cmp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        
    	cmp = compare_list.items_compare;
        
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout parent = (LinearLayout) findViewById(R.id.parent_layout);
        LinearLayout child = (LinearLayout) inflater.inflate(R.layout.compare_list, null);
        LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        child.setLayoutParams(params);

        parent.addView(child);
        final ViewPager pgr = (ViewPager) findViewById(R.id.pager); 
        pgr.setAdapter(new SimplePagerAdapter());
        pgr.setCurrentItem(1);
        
        
		options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.ic_launcher)
		.showStubImage(R.drawable.ic_launcher)
		.cacheInMemory()
		.cacheOnDisc()
		.build();

    }
    
    public class SimplePagerAdapter extends PagerAdapter{
 	
		@Override
		public void destroyItem(View v, int position, Object view) {
			// TODO Auto-generated method stub
			((ViewPager) v).removeView((View) view);
		}

		@Override
		public void finishUpdate(View v) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public int getCount() {
			return cmp.size();
		}

		@Override
		public Object instantiateItem(View v, int position) {
	        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        LinearLayout page = (LinearLayout) inflater.inflate(R.layout.item_compare, null);
			enterPage(page,position,v);
			((ViewPager) v).addView(page,0);
			return page;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
	    public boolean isViewFromObject(View view, Object object){
	        return view.equals(object);
	    }
    	
		private void enterPage(View v,final int position,View parent){
			TextView cost = (TextView) v.findViewById(R.id.cost_val);
			TextView descr = (TextView) v.findViewById(R.id.description_text);
			TextView name = (TextView) v.findViewById(R.id.name_text);
			
			cost.setText(Double.toString(cmp.get(position).cost));
			descr.setText(cmp.get(position).description);
			name.setText(cmp.get(position).name);
			
			Gallery gallery = (Gallery) v.findViewById(R.id.gallery);
			final Item item = cmp.get(position);
			gallery.setAdapter(new ImagePagerAdapter(cmp.get(position)));
			ArrayList<View> lst = new ArrayList<View>();
			lst.add(gallery);
			((ViewPager)parent).addFocusables(lst, ViewPager.FOCUSABLES_ALL,ViewGroup.FOCUS_BEFORE_DESCENDANTS);
			
			TextView addToBasket = (TextView) v.findViewById(R.id.add_basket);
	        addToBasket.setOnClickListener(new OnClickListener(){

				public void onClick(View v) {
					// TODO Auto-generated method stub
					basket_list.addItem(cmp.get(position), CompareActivity.this);
					v.setVisibility(View.INVISIBLE);
				}
	        	
	        });
	        
	        TextView delFromComp = (TextView) v.findViewById(R.id.del_compare);
	        delFromComp.setOnClickListener(new OnClickListener(){

				public void onClick(View v) {
					// TODO Auto-generated method stub
					compare_list.items_compare.remove(position);
					cmp = compare_list.items_compare;
					if(cmp.size()==0)CompareActivity.this.finish();
					updateFooter();
					SimplePagerAdapter.this.notifyDataSetChanged();
				}
	        	
	        });
			
			
		}
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