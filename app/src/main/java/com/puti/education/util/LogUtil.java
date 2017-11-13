package com.puti.education.util;

import android.util.Log;

public class LogUtil {

	private static boolean debug = true;

	public static void enableDebugMode(boolean flag) {
		debug = flag;
	}
	
	public static void v(String tag, String msg) {
		if (debug) {
			Log.v(tag, msg);
		}
	}	
	
	public static void i(String tag, String msg) {
		if (debug) {
			Log.i(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (debug) {
			Log.d(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (debug) {
			Log.w(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (debug) {
			Log.e(tag, msg);
		}
	}

}
