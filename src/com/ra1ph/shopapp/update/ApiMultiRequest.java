package com.ra1ph.shopapp.update;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.ra1ph.shopapp.logic.ApiMultiStruct;
import com.ra1ph.shopapp.logic.ApiStruct;
import com.ra1ph.shopapp.logic.Utils;

import android.os.AsyncTask;
import android.util.Log;

public class ApiMultiRequest extends AsyncTask<ApiMultiStruct, Integer, ApiMultiStruct> {

	@Override
	protected ApiMultiStruct doInBackground(
			ApiMultiStruct... params) {
		ApiMultiStruct str = new ApiMultiStruct();
		String[] replys = new String[params.length];
		
		String replyStr = "";
		if(params.length>0){
		for(int i=0;i<params[0].request.size();i++){			
		ApiStruct reply = new ApiStruct();
		String url = params[0].request.get(i);
		reply.type=params[0].type;
        HttpURLConnection connection=null;
        
        try{
            connection = (HttpURLConnection)new URL(url).openConnection();
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            connection.setUseCaches(false);
            connection.setDoOutput(false);
            connection.setDoInput(true);
            int code=connection.getResponseCode();
            Log.i("MyLog", "code="+code);
            //It may happen due to keep-alive problem http://stackoverflow.com/questions/1440957/httpurlconnection-getresponsecode-returns-1-on-second-invocation
            //может стоит проверить на код 200
            //on error can also read error stream from connection.
            InputStream is = new BufferedInputStream(connection.getInputStream(), 8192);
            String enc=connection.getHeaderField("Content-Encoding");
            replyStr = Utils.convertStreamToString(is);
            if(replyStr==null)replyStr = "";
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        finally{
            if(connection!=null)
                connection.disconnect();
        }
        str.request.add(replyStr);
		}
		str.listener = params[0].listener;
		}
        return str;
	}
	
	protected void onPostExecute(ApiMultiStruct reply){
		reply.listener.OnResult(reply);
	}
	
	public abstract interface OnMultiResultListener{
		abstract void OnResult(ApiMultiStruct reply);
	}

}
