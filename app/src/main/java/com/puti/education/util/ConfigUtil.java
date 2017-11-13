package com.puti.education.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class ConfigUtil {
	private static final String TAG = ConfigUtil.class.getSimpleName();
	private SharedPreferences mSharedPreferences;
	private Editor mEditor;
	private final String NAME = "config";
	private static ConfigUtil mInstance;
	public synchronized static ConfigUtil getInstance(Context context)
	{
		if(mInstance == null)
			mInstance = new ConfigUtil(context);
		return mInstance;
	}
	
	private ConfigUtil(Context context)
	{
		mSharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
	}

	public void put(String key,String value)
	{
		mEditor.putString(key, value);
	}
	public void put(String key,boolean value)
	{
		mEditor.putBoolean(key, value);
	}
	public void put(String key,int value)
	{
		mEditor.putInt(key, value);
	}
	public void commit()
	{
		mEditor.commit();
	}

	public void putSchoolId(int schoolId){
		mEditor.putInt(Constant.KEY_SCHOOL_ID, schoolId);
		mEditor.commit();
	}
	public int getSchoolId(){
		return mSharedPreferences.getInt(Constant.KEY_SCHOOL_ID, -1);
	}
	
	public void putLoginName(String loginName){
		mEditor.putString(Constant.KEY_LOGIN_NAME, loginName);
		mEditor.commit();
	}
	public String getLoginName(){
		return mSharedPreferences.getString(Constant.KEY_LOGIN_NAME, "");
	}

	public void putPwd(String pwd){
		mEditor.putString(Constant.KEY_LOGIN_PWD, pwd);
		mEditor.commit();
	}
	public String getPwd(){
		return mSharedPreferences.getString(Constant.KEY_LOGIN_PWD, "");
	}

	public void saveToken(String token){
		mEditor.putString(Constant.KEY_TOKEN, token);
		mEditor.commit();
	}
	public String getToken(){
		return mSharedPreferences.getString(Constant.KEY_TOKEN, "");
	}
	
	public void clearToken(){
		mEditor.putString(Constant.KEY_TOKEN, "");
		mEditor.commit();
	}

	public void clearSearchParams(){
		putLoginName(null);
		putPwd(null);
		clearToken();

	}

	
	public String get(String key,String defValue)
	{
		return mSharedPreferences.getString(key, defValue);
	}
	
	public long get(String key,long defValue)
	{
		return mSharedPreferences.getLong(key, defValue);
	}
	
	public int get(String key,int defValue)
	{
		return mSharedPreferences.getInt(key, defValue);
	}
	
	public boolean get(String key,boolean defValue)
	{
		return mSharedPreferences.getBoolean(key, defValue);
	}
	
	public boolean containKey(String key)
	{
		return mSharedPreferences.contains(key);
	}

	public static void save(Context context,Serializable obj, String file) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = context.openFileOutput(file,Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(obj);
		} catch (Throwable e) {
			LogUtil.e(TAG, "save file:" + file + " error " + e.getMessage());
		} finally {
			try {
				if (oos != null)
					oos.close();
				if (fos != null)
					fos.close();
			} catch (Throwable e) {
				LogUtil.e(TAG, "save file:" + file + " close error " + e.getMessage());
			}
		}
	}

	public static Object load(Context context,String file) {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = context.openFileInput(file);
			ois = new ObjectInputStream(fis);
			return ois.readObject();
		} catch (Throwable e) {
			LogUtil.e(TAG, "load file:" + file + " error " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (ois != null)
					ois.close();
				if (fis != null)
					fis.close();
			} catch (Throwable e) {
				LogUtil.e(TAG, "load file:" + file + " close error" + e.getMessage());
			}
		}
		return null;
	}

	public static  String getAllVersion(Context ctx) {
		String version = null;
		PackageManager manager;
		PackageInfo info = null;
		manager = ctx.getPackageManager();

		try {
			info = manager.getPackageInfo(ctx.getPackageName(), 0);

		} catch (PackageManager.NameNotFoundException e) {

			e.printStackTrace();
		}

		if (info != null) {
			version = info.versionName + "(" + info.versionCode + ")";
		}
		return version;
	}
}
