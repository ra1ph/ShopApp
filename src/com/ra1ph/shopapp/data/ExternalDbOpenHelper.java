package com.ra1ph.shopapp.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ExternalDbOpenHelper extends SQLiteOpenHelper {

	//Путь к папке с базами на устройстве
	public static String DB_PATH;
	//Имя файла с базой
	public static String DB_NAME;
	public SQLiteDatabase database;
	public final Context context;
	public  static final String APP_PREFERENCES = "mysettings";
	private SharedPreferences sett;

	public SQLiteDatabase getDb() {
		return database;
	}

	public ExternalDbOpenHelper(Context context, String databaseName) {
		super(context, databaseName, null, 1);
		this.context = context;
		//Составим полный путь к базам для вашего приложения
		String packageName = context.getPackageName();
		DB_PATH = String.format("//data//data//%s//", packageName);
		DB_NAME = "data/databases/" + databaseName;
		//openDataBase();
	 
	}

	//Создаст базу, если она не создана
	public void createDataBase() {
		    this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				Log.e(this.getClass().toString(), "Copying error");
				throw new Error("Error copying database!");
			}
	}
	
	private boolean checkDataBase(){
		{
		    File dbFile = new File(DB_PATH + DB_NAME);
		    return dbFile.exists();
		}
	}
	
	
	//Метод копирования базы
	private void copyDataBase() throws IOException {
		// Открываем поток для чтения из уже созданной нами БД
		//источник в assets
		InputStream externalDbStream = context.getAssets().open(DB_NAME);

		// Путь к уже созданной пустой базе в андроиде
		String outFileName = DB_PATH + DB_NAME;
		// Теперь создадим поток для записи в эту БД побайтно
		OutputStream localDbStream = new FileOutputStream(outFileName);
		// Собственно, копирование
		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = externalDbStream.read(buffer)) > 0) {
			localDbStream.write(buffer, 0, bytesRead);
			
		}
		// принятие изменений и закрытие потока
		localDbStream.flush();
		localDbStream.close();
		externalDbStream.close();
      //  sett = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
     //   SharedPreferences.Editor editor = sett.edit();

	}

	public SQLiteDatabase openDataBase() throws SQLException {
		String path = DB_PATH + DB_NAME;
		if(checkDataBase()){
		database = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		}else createDataBase();
		return database;
	}
	public SQLiteDatabase openDataToWrite() throws SQLException {
		String path = DB_PATH + DB_NAME;
		if(checkDataBase()){
		database = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READWRITE);
		}
		return database;
	}
	@Override
	public synchronized void close() {
		if (database != null) {
			database.close();
		}
		super.close();
	}
	@Override
	public void onCreate(SQLiteDatabase db) {}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}