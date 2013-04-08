package com.ra1ph.shopapp;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ra1ph.shopapp.data.DBEditor;
import com.ra1ph.shopapp.logic.ApiStruct;
import com.ra1ph.shopapp.logic.Category;
import com.ra1ph.shopapp.logic.Item;
import com.ra1ph.shopapp.logic.Order;
import com.ra1ph.shopapp.logic.Order.Product;
import com.ra1ph.shopapp.logic.Parser;
import com.ra1ph.shopapp.update.ApiInterface;
import com.ra1ph.shopapp.update.ApiInterface.OnResultListener;

public class NewOrder extends BaseActivity {
	
	String idUser="VASYA987";
	String secretId="1ffb80008b7a8f656ec7a819582c6024";
	String contact="st.Tereshkova,1";
	EditText fio,ntel;
	String fio_str,tel_str;
	
	ApiInterface api_int;
	ProgressDialog prog;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);       
    
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout parent = (LinearLayout) findViewById(R.id.parent_layout);
        LinearLayout child = (LinearLayout) inflater.inflate(R.layout.new_order, null);
        parent.addView(child);
        
        final SharedPreferences settings = getSharedPreferences(Names.APP_NAME,MODE_PRIVATE);
        
		fio = (EditText) findViewById(R.id.edit_fio);
		ntel = (EditText) findViewById(R.id.edit_n_tel);
		
		fio_str = settings.getString(Names.FIO, "");
		tel_str = settings.getString(Names.TEL, "");
		secretId = settings.getString(Names.SECRET_ID, "");

		if(tel_str.equals("")){
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		tel_str = telephonyManager.getLine1Number();
		}
		
		fio.setText(fio_str);
		ntel.setText(tel_str);
				
        Button sub = (Button) findViewById(R.id.order_sub);
        sub.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub

				final Order order = new Order();
				fio_str = fio.getText().toString();
				tel_str = ntel.getText().toString();
				order.idZakaz = 0;
				order.remark = "Тестовый заказ";
				Calendar c = Calendar.getInstance(); 
				order.sklad_dt = c.getTime().toString();
				order.status = "Z";
				ArrayList<Product> prods = new ArrayList<Product>();
				ArrayList<Item> bask = basket_list.items_basket;
				int i;
				for(i=0;i<bask.size();i++){
					Product prod = new Product(bask.get(i).id,1);
					prod.options = bask.get(i).options;
					prods.add(prod);
				}
				order.products = prods;
				
				prog = new ProgressDialog(NewOrder.this);
				prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				prog.show();
				
				final Thread progUpdate = new Thread(new Runnable(){

					public void run() {
						// TODO Auto-generated method stub
						prog.incrementProgressBy(10);
					}
					
				});
				
				try {
					
					api_int = new ApiInterface();
					api_int.setOnResultListener(new OnResultListener(){

						public void OnResult(ApiStruct reply) {
							// TODO Auto-generated method stub
							
							try {
								progUpdate.join();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							prog.dismiss();
							
							if(reply.request!=null){
								Log.d("API", reply.request);
							String SID="";
							int errorCode = 0,zakazServerId=0;
							SID = (new Parser(NewOrder.this)).parseSecretId(reply.request);
							errorCode = (new Parser(NewOrder.this)).parseErrorCode(reply.request);
							zakazServerId = (new Parser(NewOrder.this)).parseZakazId(reply.request);
							
							Editor edit = settings.edit();
							edit.putString(Names.FIO, fio_str);
							edit.putString(Names.TEL, tel_str);
							
							if((errorCode==0)||(errorCode==2)){
							order.idZakaz = zakazServerId;								
							DBEditor dbe = new DBEditor(NewOrder.this);
							dbe.addOrder(order);
							dbe.close();
							basket_list.clear();
							
							NewOrder.this.finish();
							}
							
							if(errorCode==2){							
								if((!SID.equals(secretId))&&(!SID.equals(""))){
									edit.putString(Names.SECRET_ID, SID);
									secretId = SID;
								}

							}
							
							edit.commit();
							}
							else {
							AlertDialog alert = new AlertDialog.Builder(NewOrder.this).create();
							alert.setTitle("Error!");
							alert.setMessage("Your order has not been sended. Please check your internet connection!");
							alert.setOnDismissListener(new OnDismissListener(){

								public void onDismiss(DialogInterface arg0) {
									// TODO Auto-generated method stub
									NewOrder.this.finish();
								}
								
							});
							alert.show();
							}
							
						}
						
					});
					api_int.sendOrder(order.products,tel_str,secretId,fio_str,contact);
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
        	
        });
    }
}