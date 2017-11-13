package com.puti.education.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadUtil {
	private static ScheduledExecutorService mExecutor = Executors.newSingleThreadScheduledExecutor();
	private static Handler mHandler = new Handler(Looper.getMainLooper());

	public static boolean runAtMain(Runnable task) {
		return mHandler.post(task);
	}

	public static boolean runAtMain(Runnable task, long delayMillis) {
		return mHandler.postDelayed(task, delayMillis);
	}

	public static void removeMainTask(Runnable task) {
		mHandler.removeCallbacks(task);
	}

	public static Future<?> runAtBg(final Runnable task) {
		if (mExecutor == null)
			return null;
		return mExecutor.submit(task);
	}

	public static Future<?> runAtBgWithFixedDelay(Runnable task, long delay, long period) {
		if (mExecutor == null)
			return null;
		return mExecutor.scheduleWithFixedDelay(task, delay, period, TimeUnit.MILLISECONDS);
	}

	/**
	 * 单位毫秒
	 * 
	 * @param task
	 * @param delay
	 * @param period
	 */
	public static Future<?> runAtBgAtFixedRate(Runnable task, long delay, long period) {
		if (mExecutor == null)
			return null;
		return mExecutor.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS);
	}

	public static Future<?> runAtBg(Runnable task, long delayMillis) {
		if (mExecutor == null)
			return null;
		return mExecutor.schedule(task, delayMillis, TimeUnit.MILLISECONDS);
	}

	public static void release() {
		if (mExecutor != null) {
			mExecutor.shutdown();
			mExecutor = null;
		}
	}

	@SuppressWarnings("deprecation")
	public static WakeLock getLock(Context context, boolean isKeepScreenOn) {
		PowerManager manager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		return manager.newWakeLock(isKeepScreenOn ? PowerManager.FULL_WAKE_LOCK : PowerManager.PARTIAL_WAKE_LOCK,
				"wakelock");
	}
}
