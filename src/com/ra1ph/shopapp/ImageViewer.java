package com.ra1ph.shopapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.ra1ph.shopapp.logic.Item;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class ImageViewer extends BaseActivity {

	private ViewPager pager;
	private Item item;
	private DisplayImageOptions options;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout parent = (LinearLayout) findViewById(R.id.parent_layout);
        LinearLayout child = (LinearLayout) inflater.inflate(R.layout.ac_image_pager, null);
        parent.addView(child);
        
		item = (Item) this.getIntent().getSerializableExtra(Names.ITEM);
		int pagerPosition = this.getIntent().getIntExtra(Names.FOTO_ID, -1);

		options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.ic_launcher)
			.cacheOnDisc()
			.imageScaleType(ImageScaleType.EXACT)
			.build();

		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(new ImagePagerAdapter(item));
		pager.setCurrentItem(pagerPosition);
	}

	@Override
	protected void onStop() {
		imageLoader.stop();
		super.onStop();
	}

	private class ImagePagerAdapter extends PagerAdapter {

		private String[] images;
		private LayoutInflater inflater;
		private Item item;

		ImagePagerAdapter(Item item) {
			this.item = item;
			inflater = getLayoutInflater();
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public void finishUpdate(View container) {
		}

		@Override
		public int getCount() {
			return item.num_photo;
		}

		@Override
		public Object instantiateItem(View view, int position) {
			final View imageLayout = inflater.inflate(R.layout.item_pager_image, null);
			final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
			final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);

			String img = Names.SERVER_NAME + Names.IMAGE_FOLDER + item.image+Integer.toString(position)+".jpg";
			imageLoader.displayImage(img, imageView, options, new ImageLoadingListener() {
				
				public void onLoadingStarted() {
					spinner.setVisibility(View.VISIBLE);
				}

				
				public void onLoadingFailed(FailReason failReason) {
					String message = null;
					switch (failReason) {
						case IO_ERROR:
							message = "Input/Output error";
							break;
						case OUT_OF_MEMORY:
							message = "Out Of Memory error";
							break;
						case UNKNOWN:
							message = "Unknown error";
							break;
					}
					Toast.makeText(ImageViewer.this, message, Toast.LENGTH_SHORT).show();

					spinner.setVisibility(View.GONE);
					imageView.setImageResource(android.R.drawable.ic_delete);
				}

				
				public void onLoadingComplete(Bitmap loadedImage) {
					spinner.setVisibility(View.GONE);
					Animation anim = AnimationUtils.loadAnimation(ImageViewer.this, R.anim.fade_in);
					imageView.setAnimation(anim);
					anim.start();
				}

				
				public void onLoadingCancelled() {
					// Do nothing
				}
			});

			((ViewPager) view).addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View container) {
		}
	}
}