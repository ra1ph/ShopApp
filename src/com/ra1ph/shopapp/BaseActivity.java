package com.ra1ph.shopapp;

import java.util.ArrayList;
import java.util.Date;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ra1ph.shopapp.data.DBEditor;
import com.ra1ph.shopapp.logic.Basket;
import com.ra1ph.shopapp.logic.Compare;
import com.ra1ph.shopapp.update.ApiInterface;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public abstract class BaseActivity extends Activity implements AnimationListener {

    View menu;
    View app;
    boolean menuOut = false;
    AnimParams animParams = new AnimParams();
    float fromPosition=-1,fromYPosition=-1;
	
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected static Basket basket_list;
	protected static Compare compare_list;
	public String CATEGORY_ID = "id_cat";
	public String CATEGORY_NAME = "cat_name";
	public int NO_CATEGORY = -1;
	public final float IS_SLIDE = 10;
	public static SharedPreferences settings;
	
	
    class ClickListener implements OnClickListener {
        public void onClick(View v) {
            System.out.println("onClick " + new Date());
            BaseActivity me = BaseActivity.this;
            Context context = me;
            Animation anim;

            if (!menuOut) {
            	menuOut();
            } else {
            	menuIn();
            }
        }
    }


    void layoutApp(boolean menuOut) {
        System.out.println("layout [" + animParams.left + "," + animParams.top + "," + animParams.right + ","
                + animParams.bottom + "]");
        app.layout(animParams.left, animParams.top, animParams.right, animParams.bottom);
    }

    public void onAnimationEnd(Animation animation) {
        System.out.println("onAnimationEnd");
        ViewUtils.printView("menu", menu);
        ViewUtils.printView("app", app);
        menuOut = !menuOut;
        if (!menuOut) {
            menu.setVisibility(View.INVISIBLE);
        }
        layoutApp(menuOut);
    }

    public void onAnimationRepeat(Animation animation) {
        System.out.println("onAnimationRepeat");
    }

    public void onAnimationStart(Animation animation) {
        System.out.println("onAnimationStart");
    }

    static class AnimParams {
        int left, right, top, bottom;

        void init(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }
    }
    
    @Override
    protected void onResume() {
    	updateFooter();
    	super.onResume();
    }
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    	basket_list = Basket.getInstance();   
    	compare_list = Compare.getInstance();
    	
        setContentView(R.layout.template);

        menu = findViewById(R.id.menu);
        app = findViewById(R.id.app);

        ViewUtils.printView("menu", menu);
        ViewUtils.printView("app", app);       

        updateFooter();
    }

    public void updateFooter(){
    	LinearLayout footer = (LinearLayout) findViewById(R.id.footer);
    	TextView comp = (TextView) findViewById(R.id.in_compare_text);
    	TextView bask = (TextView) findViewById(R.id.in_basket_text);
    	if((footer!=null)&&(comp!=null)&&(bask!=null)){
    	if(compare_list.items_compare.size()>0){
    		comp.setText("В сравнении "+Integer.toString(compare_list.items_compare.size())+" устройства");
    		comp.setVisibility(View.VISIBLE);
    	}else comp.setText("В сравнении ни одного устройства");
    	if(basket_list.items_basket.size()>0){
    		bask.setVisibility(View.VISIBLE);
    		bask.setText("В корзине "+Integer.toString(basket_list.items_basket.size())+" устройства на сумму "+Double.toString(basket_list.summaryCost)+" usd");
    	}else bask.setText("В корзине ни одного устройства");
    	}
    	/*if((compare_list.items_compare.size()==0)&&(basket_list.items_basket.size()==0))footer.setVisibility(View.INVISIBLE);
    	else footer.setVisibility(View.VISIBLE);*/
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.item_clear_memory_cache:
				imageLoader.clearMemoryCache();
				return true;
			case R.id.item_clear_disc_cache:
				imageLoader.clearDiscCache();
				return true;
			default:
				return false;
		}
	}
	
    public boolean dispatchTouchEvent(MotionEvent event){
    	boolean childDispatch = false;
    	int x = (int) event.getX();
    	int y = (int) event.getY();
    	
        Context context = this;
        Animation anim=new TranslateAnimation(0,0,0,0);
    	
        int w = app.getMeasuredWidth();
        int h = app.getMeasuredHeight();
        int left = (int) (app.getMeasuredWidth() * 0.8);
    		
	        switch (event.getAction())
	        {
	        
	        case MotionEvent.ACTION_DOWN: // Пользователь нажал на экран, т.е. начало движения 
	            // fromPosition - координата по оси X начала выполнения операции
	        	if(((x<(w*0.1))&&(!menuOut))||((x>(w*0.8))&&(menuOut))){  	
	            fromPosition = event.getX();
	            fromYPosition = event.getY();
	            Log.d("myLogs", "OnDown!"+Float.toString(fromPosition));
	        	}
	            break;
	         
	        case MotionEvent.ACTION_MOVE:
	        	if((fromPosition!=-1)&&(Math.abs(event.getX()-fromPosition)>IS_SLIDE)) childDispatch = true;	
	        	break;
	            
	        case MotionEvent.ACTION_UP: // Пользователь отпустил экран, т.е. окончание движения
	            float toPosition = event.getX();
	            Log.d("myLogs", "OnUp!"+Float.toString(toPosition)+Float.toString(fromPosition));
	            if((Math.abs(toPosition-fromPosition)>Math.abs(event.getY()-fromYPosition))&&(Math.abs(toPosition-fromPosition)>IS_SLIDE)&&(fromPosition!=-1)){
		            if ((fromPosition < toPosition)&&(!menuOut)){
		            	menuOut();
			            Log.d("myLogs", "Menu out!");
		            }
		            else if ((fromPosition > toPosition)&&(menuOut)){
		                menuIn();
		                Log.d("myLogs", "Menu in!");
		            }
		            childDispatch = true;
	            }
	            fromPosition = -1;
	            fromYPosition = -1;
	        }
    	if(childDispatch){
    		Log.d("myLogs","Child dispatch");
    		return true;
    	}
    	else return super.dispatchTouchEvent(event);	
    }
    
    void setMenu(){
    	boolean isBasket=false,isCompare=false,isBest=false,isOrderList=false;
    	if(basket_list.items_basket.size()>0)isBasket=true;
    	if(compare_list.items_compare.size()>0)isCompare=true;
    	DBEditor dbe = new DBEditor(this);
    	if(dbe.isZakazList())isOrderList=true;
    	dbe.close();
    	
    	//if(basket_list.items_basket.size()>0)isBest=true;		For Best list
    	final ArrayList<String> arr=new ArrayList<String>();
    	arr.add("Акции");
    	arr.add("Контакты");
    	if(isBest)arr.add("Избранное");
    	if(isCompare)arr.add("Сравнение");
    	if(isBasket)arr.add("Корзина");
    	if(isOrderList)arr.add("Заказы");
    	
    	ListView lst = (ListView) findViewById(R.id.menu_list);
        lst.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr));
        lst.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				// TODO Auto-generated method stub
				String item = arr.get(position);
				if(item.equals("Акции")){
				}else if (item.equals("Контакты")){
					Intent intent = new Intent(BaseActivity.this,Contacts.class);
					startActivity(intent);
				}else if (item.equals("Избранное")){
				}else if (item.equals("Сравнение")){
					Intent intent = new Intent(BaseActivity.this,CompareActivity.class);
					startActivity(intent);
				}else if (item.equals("Корзина")){
					Intent intent = new Intent(BaseActivity.this,BasketActivity.class);
					startActivity(intent);
				}else if (item.equals("Заказы")){
					Intent intent = new Intent(BaseActivity.this,OrderList.class);
					startActivity(intent);
				}
				
			menuIn();	
			}
        });	
    }
    
    void destroyMenu(){
    	ListView lst = (ListView) findViewById(R.id.menu_list);
    	lst.setAdapter(null);
    }
    
    void menuOut(){

    	Animation anim; 
        int w = app.getMeasuredWidth();
        int h = app.getMeasuredHeight();
        int left = (int) (app.getMeasuredWidth() * 0.8);
        setMenu();
        
        // anim = AnimationUtils.loadAnimation(context, R.anim.push_right_out_80);
        anim = new TranslateAnimation(0, left, 0, 0);
        menu.setVisibility(View.VISIBLE);
        animParams.init(left, 0, left + w, h);
        
        anim.setDuration(500);
        anim.setAnimationListener(this);

        // Only use fillEnabled and fillAfter if we don't call layout ourselves.
        // We need to do the layout ourselves and not use fillEnabled and fillAfter because when the anim is finished
        // although the View appears to have moved, it is actually just a drawing effect and the View hasn't moved.
        // Therefore clicking on the screen where the button appears does not work, but clicking where the View *was* does
        // work.
         //anim.setFillEnabled(true);
        // anim.setFillAfter(true);

        app.startAnimation(anim);
    }
    
    void menuIn(){
    	Animation anim; 
        int w = app.getMeasuredWidth();
        int h = app.getMeasuredHeight();
        int left = (int) (app.getMeasuredWidth() * 0.8);
        anim = new TranslateAnimation(left,0, 0, 0);
        animParams.init(0, 0, w, h);
        
        //anim.setFillEnabled(true);
        //anim.setFillAfter(true);
        
        anim.setDuration(500);
        anim.setAnimationListener(this);
        app.startAnimation(anim);
        destroyMenu();
    }
}
