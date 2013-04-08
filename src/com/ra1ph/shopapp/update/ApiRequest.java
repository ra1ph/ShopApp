package com.ra1ph.shopapp.update;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.zip.GZIPInputStream;

import com.ra1ph.shopapp.logic.ApiStruct;
import com.ra1ph.shopapp.logic.Utils;

import android.os.AsyncTask;
import android.util.Log;

public class ApiRequest extends AsyncTask<ApiStruct, Integer,ApiStruct> {

	@Override
	protected ApiStruct doInBackground(ApiStruct... arg0)  {
		// TODO Auto-generated method stub
		ApiStruct reply = new ApiStruct();
        reply.listener=arg0[0].listener;
		String url = "";
		url = arg0[0].request;
		reply.type=arg0[0].type;
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
            reply.request=Utils.convertStreamToString(is);
            if(reply.request==null)reply.request="";
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        finally{
            if(connection!=null)
                connection.disconnect();
        }
        return reply;
	}
	
	protected void onPostExecute(ApiStruct reply){
		reply.listener.OnResult(reply);
	}
}