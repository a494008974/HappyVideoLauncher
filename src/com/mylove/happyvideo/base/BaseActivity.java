package com.mylove.happyvideo.base;



import org.xmlpull.v1.XmlPullParser;

import com.mylove.happyvideo.R;
import com.mylove.happyvideo.bean.Item;
import com.mylove.happyvideo.utils.Contanst;
import com.zhy.autolayout.AutoLayoutActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Xml;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;



public abstract class BaseActivity extends AutoLayoutActivity {

	/**
	 * 屏幕的宽度、高度、密度
	 */
	protected int mScreenWidth;
	protected int mScreenHeight;
	protected float mDensity;
	protected Context mContext;
	protected String LogName; // 打印的名称

	private static String mDialogTag = "basedialog";


	protected Boolean isfinish = false;
	protected ActivityTack tack = ActivityTack.getInstanse();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
		mScreenHeight = metric.heightPixels;
		mDensity = metric.density;
		LogName = this.getClass().getSimpleName();
		tack.addActivity(this);

	}

	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		super.setContentView(layoutResID);
	}

	/** 通过Class跳转界面 **/
	protected void startActivity(Class<?> cls) {
		startActivity(cls, null);
	}

	/** 含有Bundle通过Class跳转界面 **/
	protected void startActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(mContext, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

	/** 通过Action跳转界面 **/
	protected void startActivity(String action) {
		startActivity(action, null);
	}

	public boolean openApk(String pkg) {
		try {
			PackageManager pm = getPackageManager();
			Intent intent = pm.getLaunchIntentForPackage(pkg);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
	}
	/** 含有Bundle通过Action跳转界面 **/
	protected void startActivity(String action, Bundle bundle) {
		Intent intent = new Intent();
		intent.setAction(action);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}


	@Override
	public void finish() {
		super.finish();
		tack.removeActivity(this);

	}
    public void onResume() {
        super.onResume();
    }
    public void onPause() {
        super.onPause();
    }
    
    public void configData(){
 	   XmlResourceParser xmlParser = getResources().getXml(R.xml.desktop);
 	   AttributeSet as = Xml.asAttributeSet(xmlParser);
 	   try {
 		   int eventType = xmlParser.getEventType();
 		   while (eventType != XmlPullParser.END_DOCUMENT) {  
 			   if(eventType == XmlPullParser.START_TAG){
 				   String tagName = xmlParser.getName();
 				   TypedArray a = this.obtainStyledAttributes(as, R.styleable.Config);
 				   if(tagName.equals("config")){
 					   
 					   Item item = new Item();
 					   item.setPkg(a.getString(R.styleable.Config_pname));
 					   item.setTag(a.getString(R.styleable.Config_tag));
 					   item.setTitle(a.getString(R.styleable.Config_title));
 					   item.setIslock("0");
 					   item.setIslink("0");
 					   item.setIshold("0");
 					   Contanst.fd.save(item);
 					   
 				   }
 			   }
 			   eventType = xmlParser.next();
 		   }
 	   } catch (Exception e) {
 		// TODO Auto-generated catch block
 	   }  
 	}
    
    public static void request(final View focusView){
        final ViewTreeObserver vto = focusView.getViewTreeObserver();
        vto.addOnDrawListener(new ViewTreeObserver.OnDrawListener() {

            @Override
            public void onDraw() {
                if (focusView.getWidth() != 0) {
                    focusView.requestFocus();
                    vto.removeOnDrawListener(this);
                }
            }
        });
    }
}
