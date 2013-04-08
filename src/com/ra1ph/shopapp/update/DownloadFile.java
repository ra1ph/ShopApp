package com.ra1ph.shopapp.update;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import com.ra1ph.shopapp.update.Update.OnUpdateListener;

import android.os.AsyncTask;
import android.util.Log;

public class DownloadFile extends AsyncTask<String, Integer, Boolean> {
	OnUpdateListener listener;
	public DownloadFile(OnUpdateListener listener){
		this.listener = listener;
	}
	
    @Override
    protected Boolean doInBackground(String... sUrl) {
        try {
            URL url = new URL(sUrl[0]);
            URLConnection connection = url.openConnection();
            connection.connect();
            // this will be useful so that you can show a typical 0-100% progress bar
            int fileLength = connection.getContentLength();

            // download the file
            File wallpaperDirectory = new File(sUrl[1]);
            // have the object build the directory structure, if needed.
            wallpaperDirectory.mkdirs();
            // create a File object for the output file
            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(sUrl[1]+sUrl[2]);

            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
            return true;
        } catch (Exception e) {
        	return false;
        }
    }
    
    @Override
    protected void onPostExecute(Boolean result) {
      super.onPostExecute(result);
      if(!result) Log.e("myLogs", "Error download file");
      System.out.print("Result:"+Boolean.toString(result));    
      listener.OnUpdate();
    }
    
    @Override
    protected void onProgressUpdate(Integer... i){
    	listener.OnProgress(i[0]);
    }
}