package com.mylove.happyvideo.base;



import android.app.Application;
import android.content.Context;



public class BaseApplication extends Application {
    private static BaseApplication mInstance;


    private Context mContext;


    // private Gson mG;
    public static BaseApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        
       
        CrashHandler mCrashHandler = CrashHandler.getInstance();
        mCrashHandler.init(getApplicationContext());
        Thread.currentThread().setUncaughtExceptionHandler(mCrashHandler);

    }


    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub

//        android.os.Process.killProcess(android.os.Process.myPid());
        super.onLowMemory();

    }

}
