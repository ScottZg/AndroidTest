package com.shuqu.microcredit.Crash;

import android.content.Context;
import java.lang.Thread.UncaughtExceptionHandler;

public class CrashHandler implements UncaughtExceptionHandler {

	private Context mContext;
	private UncaughtExceptionHandler mDefaultHandler;
	
	private static CrashHandler mInstance;
	
	private CrashHandler(){/*Do not new me*/};
	
	private CrashHandler(Context context) {
		mContext = context;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
	}
	
	public static CrashHandler startMonitor(Context context) {
		if (mInstance == null) {
			mInstance = new CrashHandler(context);
		}
		Thread.setDefaultUncaughtExceptionHandler(mInstance);
		return mInstance;
	}
	
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		handleException(ex);
		if (!handleException(ex) && mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			exit();
		}
	}
	
	private boolean handleException(Throwable ex) {
		System.out.println(ex.getLocalizedMessage());
		StackTraceElement[] datas = ex.getStackTrace();
		for (int i=0; i<datas.length; ++i) {
			System.out.println(datas[i]);
		}
		return true;
	}
	
	
	private void exit() {
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(1);
	}
}
