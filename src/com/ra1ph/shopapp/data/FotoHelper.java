package com.ra1ph.shopapp.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class FotoHelper {
	public AssetManager assetmanager = null;
	private String folder = "//data//data//%s//";
	private String images = "images";
	private String tc_foto = "tc_foto";
	private String shop_logo = "shop_logo";
	private String IS_INSTALL = "is_install";
	private String card_foto = "card_foto";
	private String news_foto = "news_foto";
	public Context context; 
	public static final String APP_PREFERENCES = "mysettings";
	private SharedPreferences shr;
	
public FotoHelper(Context ctx){
	shr = ctx.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
	assetmanager = ctx.getAssets();
	context = ctx;
	String pack = ctx.getPackageName();
	folder = String.format("//data//data//%s", pack);
	if (!shr.getBoolean(IS_INSTALL, false)){
	copyFileOrDir("data");
	}
}

public Bitmap getCardFoto(String name){
	Bitmap foto = null ;
	String nm = folder +"/data/" + images + "/" + card_foto + "/" + name+ ".png";
	foto = BitmapFactory.decodeFile(nm);
	if (foto==null) {
		nm=folder +"/data/" + images + "/" + card_foto + "/" +  "nophoto.png";
		foto = BitmapFactory.decodeFile(nm);
	}
	return foto;
}

private void copyFileOrDir(String path) {

    String assets[] = null;
    try {
        assets = assetmanager.list(path);
        if (assets.length == 0) {
            copyFile(path);
        } else {
            String fullPath = folder + "/" + path;
            File dir = new File(fullPath);
            if (!dir.exists())
                dir.mkdir();
            for (int i = 0; i < assets.length; ++i) {
                copyFileOrDir(path + "/" + assets[i]);
            }
        }
        shr = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shr.edit();
        editor.putBoolean(IS_INSTALL, true);
        editor.commit();
    } catch (IOException ex) {
        Log.e("tag", "I/O Exception", ex);
    }
}

private void copyFile(String filename) {

    InputStream in = null;
    OutputStream out = null;
    try {
        in = assetmanager.open(filename);
        String newFileName = folder + "//" + filename;
        out = new FileOutputStream(newFileName);

        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        in.close();
        in = null;
        out.flush();
        out.close();
        out = null;
    } catch (Exception e) {
        Log.e("tag", e.getMessage());
    }

}

public void close()
{
	assetmanager.close();
}
}