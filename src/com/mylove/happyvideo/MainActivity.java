package com.mylove.happyvideo;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import com.mylove.happyvideo.base.BaseActivity;
import com.mylove.happyvideo.bean.Item;
import com.mylove.happyvideo.fragment.BaseFragment;
import com.mylove.happyvideo.fragment.FragmentFactory;
import com.mylove.happyvideo.killprocess.ProcessManage;
import com.mylove.happyvideo.service.CoreService;
import com.mylove.happyvideo.service.VooleService;
import com.mylove.happyvideo.utils.ApkManage;
import com.mylove.happyvideo.utils.AppInfoData;
import com.mylove.happyvideo.utils.Contanst;
import com.mylove.happyvideo.utils.DesHelper;
import com.mylove.happyvideo.utils.SharedPreferencesUtils;
import com.mylove.happyvideo.utils.SystemUtils;
import com.mylove.happyvideo.view.CheckApp;
import com.mylove.happyvideo.view.ContentAction;
import com.mylove.happyvideo.view.ContentMainAction;
import com.mylove.happyvideo.view.DownApp;
import com.mylove.happyvideo.view.HoldView;
import com.mylove.happyvideo.view.HomeFragment;
import com.mylove.happyvideo.view.IFragmentAction;
import com.mylove.happyvideo.view.FirstFragment;
import com.mylove.happyvideo.view.IViewUpdate;
import com.mylove.happyvideo.view.Size;
import com.mylove.happyvideo.view.SizeEvaluator;
import com.umeng.analytics.MobclickAgent;
import com.voole.epg.cooperation.aidl.VooleAIDL;

import android.content.pm.PackageManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements IFragmentAction,ContentMainAction{

	private FragmentManager mFragmentManager;
	
	public static final int HOME = 1;
	public static final int FIRST = 2;
	private HomeFragment mHomeFragment;
	private FirstFragment mFirstFragment;
	Fragment current;
	PackageManager pm;

	private VooleAIDL aidl ;
	private ServiceConnection conn ;
	private ImageView contentFocus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		contentFocus = (ImageView)findViewById(R.id.content_main_focus);
		
		mFragmentManager = getSupportFragmentManager();
		pm = getPackageManager();
		
		if((Boolean) SharedPreferencesUtils.getParam(this, Contanst.FIRSTCONFIG, true)){
			configData();
			SharedPreferencesUtils.setParam(MainActivity.this, Contanst.FIRSTCONFIG, false);
		}
		
		if((Boolean) SharedPreferencesUtils.getParam(this, Contanst.FIRSTRUN, true)){
			showFragment(FIRST);
		}else{
			MobclickAgent.updateOnlineConfig(this);
			showFragment(HOME);
			if(!(Boolean) SharedPreferencesUtils.getParam(this, Contanst.INSTALLED, false)){
				installApk();
			}
		}
		System.out.println("MainActivity ------------------ onCreate");
	}

	
	public void getPortalUrl() {
		new AsyncTask<String, String, String>() {

			@Override
			protected String doInBackground(String... params) {
				String portalUrl = "" ;
				try {
					if(aidl != null && aidl.getPortalUrl() != null){
						portalUrl = aidl.getPortalUrl() ;
						Intent intent = new Intent(MainActivity.this, VooleService.class);
						intent.putExtra("url", portalUrl);
						MainActivity.this.startService(intent);
						}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
				}
				return portalUrl ;
			}

			protected void onPostExecute(String result) {
				System.out.println("url =========================> "+result);
			};

		}.execute("");
	}

	private class VooleServiceConnection implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			aidl = (VooleAIDL) VooleAIDL.Stub.asInterface(service) ;
			getPortalUrl() ;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if(!(Boolean) SharedPreferencesUtils.getParam(this, Contanst.FIRSTRUN, true)){
			MobclickAgent.onPageStart("MainActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
			MobclickAgent.onResume(this);
		}
	    
	    appKill ak = new appKill();
		ak.execute();
		
		
		System.out.println("MainActivity ------------------ onResume");
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(!(Boolean) SharedPreferencesUtils.getParam(this, Contanst.FIRSTRUN, true)){
			MobclickAgent.onPageEnd("MainActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
		    MobclickAgent.onPause(this);
		}
	    
	    System.out.println("MainActivity ------------------ onPause");
	}
	
	public void hideFragments(FragmentTransaction ft) {
		if (mHomeFragment != null)
			ft.hide(mHomeFragment);
		if (mFirstFragment != null)
			ft.hide(mFirstFragment);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unRegister();
		if(!MainActivity.this.isFinishing()){
			MainActivity.this.finish();
			System.out.println("MainActivity ------------------ finish");
		}
		System.out.println("MainActivity ------------------ onDestroy");
	}
	
	public void showFragment(int index) {
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		hideFragments(ft);
		
		switch (index) {
		case HOME:
			if (mHomeFragment != null)
				ft.show(mHomeFragment);
			else {
				mHomeFragment =  (HomeFragment) FragmentFactory.getFragmentByTag(MainActivity.this, HomeFragment.TAG);
				ft.replace(R.id.main, mHomeFragment);
			}
			current = mHomeFragment;
			
            //pm.setApplicationEnabledSetting("com.android.packageinstaller", PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);
            //pm.setApplicationEnabledSetting("com.android.apkinstaller", PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);

			
			break;
		case FIRST:
			if (mFirstFragment != null)
				ft.show(mFirstFragment);
			else {
				mFirstFragment = (FirstFragment) FragmentFactory.getFragmentByTag(MainActivity.this, FirstFragment.TAG);
				((BaseFragment)mFirstFragment).setPageTag(8);
				((BaseFragment)mFirstFragment).setAction(mFirstFragment);
				((BaseFragment)mFirstFragment).setMainAction(this);
				ft.replace(R.id.main, mFirstFragment);
			}
			current = mFirstFragment;
			
            //pm.setApplicationEnabledSetting("com.android.packageinstaller", PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
            //pm.setApplicationEnabledSetting("com.android.apkinstaller", PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);

			break;
		}
		ft.commit();
		register();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		numAction(keyCode);
		switch (keyCode) {
			case 92:
				openForTag("101");
				break;
			case 93:
				openApk("com.voole.epg");
				break;
		}
		return super.onKeyDown(keyCode, event);
	}



	private void openForTag(String tag) {
		// TODO Auto-generated method stub
		Item item = null;
		// 通过v.getId() 获取数据
		List<Item> current = Contanst.fd.findAllByWhere(Item.class, "tag="+"'"+tag+"'");
		if(current != null && current.size() > 0){
			item = current.get(0);
		}
		Intent intent;
		if(item != null){

			if(ApkManage.checkInstall(this, item.getPkg())){
				if(item.getWay() == null || "0".equals(item.getWay()) || "".equals(item.getWay())){
					openApk(item.getPkg());
				}else if("1".equals(item.getWay())){
					try {
						intent = Intent.parseUri(item.getWayval(), 0);
						String url = item.getWayval();
						if (url.contains("S.type=videos")
								|| url.contains("S.type=video")) {
							int time = (int) (System.currentTimeMillis() / 1000L);
							String val = "QQ:26597925," + time;
							intent.putExtra("key",
									DesHelper.encrypt(val, "@^%(&(@%"));
						}
						startActivity(intent);
						overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block

					}
				}else if("2".equals(item.getWay())){
					try {
						intent = Intent.parseUri(item.getWayval(), 0);
						sendBroadcast(intent);
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
					}
				}
			}else{
				if(!"1".equals(item.getIslock().trim())){
					
				}else{
					new DownApp(item).show(mFragmentManager, "DOWNAPP");
				}
			}
		
		}
	}



	String num = "";
	public void numAction(int keyCode) {
		System.out.println("keyCode:"+keyCode);
		Map<String, String> KeyArr = new HashMap<String, String>();
		int Arr[] = { 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		int len = Arr.length;
		for (int i = 0; i < len; i++) {
			KeyArr.put(String.valueOf(Arr[i]), String.valueOf(i));
		}
		String cur = num;
		String station = "";

		if (!KeyArr.containsKey(String.valueOf(keyCode))) {
			return;
		}

		if (cur.length() <= 6) {
			if (KeyArr.get(String.valueOf(keyCode)) != null) {
				station = cur + KeyArr.get(String.valueOf(keyCode)).toString();
				num = station;
			}
		} else {
			num = KeyArr.get(String.valueOf(keyCode)).toString();
		}
		numHandler.removeCallbacks(numRunnable);
		numHandler.postDelayed(numRunnable, 1500);
	}

	Handler numHandler = new Handler();
	Runnable numRunnable = new Runnable() {
		public void run() {
			if (!num.equals("")) {
				if (num.trim().equals(Contanst.preinstall_key1) ||
					num.trim().equals(Contanst.preinstall_key2) ||
					num.trim().equals(Contanst.preinstall_key3)	||
					num.trim().equals(Contanst.preinstall_key4) ) {
					try {
						
						SharedPreferencesUtils.setParam(MainActivity.this, Contanst.FIRSTRUN, false);
						showFragment(HOME);
						
						sendBroadcast(new Intent(
								"com.yunos.tv.probe.services.CONTENTRECEIVER"));
						
						Toast.makeText(MainActivity.this, "更多精彩!",Toast.LENGTH_LONG).show();
						
						installApk();
						num = "";
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					num = "";
				}
			}
		}
	};

	private HoldView holdView;
	
	public void installApk() {
		fileList = new ArrayList<String>();
		
		holdView = new HoldView();
		holdView.show(getSupportFragmentManager(), "HOLD");
		
		for (int i = 0; i < Contanst.preinstall.length; i++) {
			searchApk(Contanst.preinstall[i]);
		}
		new Thread() {
			public void run() {
				apkInstallAll();
				runOnUiThread(new Runnable() {
					public void run() {
						SharedPreferencesUtils.setParam(MainActivity.this, Contanst.INSTALLED, true);
						holdView.dismiss();
						Toast.makeText(MainActivity.this, "安装完成!",Toast.LENGTH_LONG).show();
					}
				});
			};
		}.start();
	}

	protected void apkInstallAll() {
		// TODO Auto-generated method stub
		int n;
		if (fileList.size() != 0) {
			for (int i = 0; i < fileList.size(); i++) {
				if (fileList.get(i) != null) {
					n = ApkManage.installSlient(this, fileList.get(i));
				} else {
					n = 0;
				}
			}
		}
	}

	public void searchApk(String path) {
		File fileDirectory = new File(path);
		if (!fileDirectory.exists()) {
			return;
		}
		File[] files = fileDirectory.listFiles();
		if (files == null) {
			return;
		}
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				searchApk(files[i].getAbsolutePath());
			} else {
				String strFileName = files[i].getAbsolutePath();

				if (strFileName.endsWith(".apk")) {
					AppInfoData info = ApkManage.getApkFileInfo(this,
							strFileName);
					if (!ApkManage.checkInstall(this, info.getPkgName())) {
						fileList.add(strFileName);
					}
				}
			}
		}
	}

	private List<String> fileList;

	
	
	@Override
	public void fragmentClick(View v,IViewUpdate update,int n) {
		
		switch (n) {
		case Contanst.FIRST:
			if("809".equals(((String)v.getTag()).trim())){
//				System.out.println("First ........... More");
				try {
					Intent intent = new Intent();
					intent.setClassName("com.mylove.settting","com.mylove.settting.MoreActivity");
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					MainActivity.this.startActivity(intent);
					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
					return;
				} catch (Exception e) {
					// TODO Auto-generated catch block
				}
				startActivity(new Intent(MainActivity.this, MoreActivity.class));
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				return;
			}
		case Contanst.SECOND:
			if("411".equals(((String)v.getTag()).trim())){
				System.out.println("Second ........... More");
				try {
					Intent intent = new Intent();
					intent.setClassName("com.mylove.settting","com.mylove.settting.MoreActivity");
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					MainActivity.this.startActivity(intent);
					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
					return;
				} catch (Exception e) {
					// TODO Auto-generated catch block
				}
				startActivity(new Intent(MainActivity.this, MoreActivity.class));
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				return;
			}
		}
		// TODO Auto-generated method stub
		Item item = null;
		// 通过v.getId() 获取数据
		List<Item> current = Contanst.fd.findAllByWhere(Item.class, "tag="+"'"+v.getTag()+"'");
		if(current != null && current.size() > 0){
			item = current.get(0);
		}
		Intent intent;
		if(item != null){
			if(ApkManage.checkInstall(this, item.getPkg())){
				if(item.getWay() == null || "0".equals(item.getWay()) || "".equals(item.getWay())){
					openApk(item.getPkg());
				}else if("1".equals(item.getWay())){
					try {
						intent = Intent.parseUri(item.getWayval(), 0);
						String url = item.getWayval();
						if (url.contains("S.type=videos")
								|| url.contains("S.type=video")) {
							int time = (int) (System.currentTimeMillis() / 1000L);
							String val = "QQ:26597925," + time;
							intent.putExtra("key",
									DesHelper.encrypt(val, "@^%(&(@%"));
						}
						startActivity(intent);
						overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block

					}
				}else if("2".equals(item.getWay())){
					try {
						intent = Intent.parseUri(item.getWayval(), 0);
						sendBroadcast(intent);
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
					}
				}
			}else{
				if(!"1".equals(item.getIslock().trim())){
					new CheckApp(update,(String)v.getTag()).show(mFragmentManager, "CHECKAPP");
				}else{
					new DownApp(item).show(mFragmentManager, "DOWNAPP");
				}
			}
		}else{
			new CheckApp(update,(String)v.getTag()).show(mFragmentManager, "CHECKAPP");
		}
	
	}

	@Override
	public boolean fragmentLongClick(View v,int n) {
		// TODO Auto-generated method stub
		
		switch (n) {
		case Contanst.FIRST:
			if("809".equals(((String)v.getTag()).trim())){
				System.out.println("First ........... More fragmentLongClick");
				return true;
			}
		case Contanst.SECOND:
			if("411".equals(((String)v.getTag()).trim())){
				System.out.println("Second ........... More fragmentLongClick");
				return true;
			}
			
		}
		
		Item item = null;
		// 通过v.getId() 获取数据
		List<Item> current = Contanst.fd.findAllByWhere(Item.class, "tag="+"'"+v.getTag()+"'");
		if(current != null && current.size() > 0){
			item = current.get(0);
		}
		if(item != null){
			try {
				ApkManage.uninstall(this, item.getPkg());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return true;
	}
	@Override
	public boolean onFragmentActionMenu(View v,IViewUpdate update,int n) {
		
		switch (n) {
		case Contanst.FIRST:
			if("809".equals(((String)v.getTag()).trim())){
				System.out.println("First ........... More onFragmentActionMenu");
				return true;
			}
		case Contanst.SECOND:
			if("411".equals(((String)v.getTag()).trim())){
				System.out.println("Second ........... More onFragmentActionMenu");
				return true;
			}
		}
		
		Item item = null;
		// 通过v.getId() 获取数据
		List<Item> current = Contanst.fd.findAllByWhere(Item.class, "tag="+"'"+v.getTag()+"'");
		if(current != null && current.size() > 0){
			item = current.get(0);
		}
		if(item != null){
			if("1".equals(item.getIslock().trim())){
				return true;
			}else{
				String s = null;
				if(!"".equals(item.getPkg())&&ApkManage.checkInstall(MainActivity.this, item.getPkg())){
					s = this.getResources().getString(R.string.select_replace);
				}else{
					s = this.getResources().getString(R.string.select_app);
				}
				new CheckApp(update,(String)v.getTag(),s).show(mFragmentManager, "CHECKAPP");
				return true;
			}
		}
		return false;
	}
	@Override
	public void onFragmentMoveFocus(View v,View focus,boolean hasFocus) {
		// TODO Auto-generated method stub
		if(hasFocus){
			getFocus(v);
			contentFocus.setVisibility(View.VISIBLE);
			moveFocus(v, contentFocus);
		}else{
			cancleFocus(v);
			contentFocus.setVisibility(View.GONE);
		}
	}

	
	private void register() {
		// TODO Auto-generated method stub
		mNetWorkChangeReceiver = new NetWorkChangeReceiver();
		IntentFilter filterNECT = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		filterNECT.addAction("android.net.wifi.STATE_CHANGE");
		filterNECT.addAction("android.net.ethernet.STATE_CHANGE");
		registerReceiver(mNetWorkChangeReceiver, filterNECT);
		
		mTimeReceiver = new TimeReceiver();
		IntentFilter filterTime = new IntentFilter(Intent.ACTION_TIME_TICK);
		registerReceiver(mTimeReceiver, filterTime);
		
		viewUpdateReceiver = new ViewUpdateReceiver();
		IntentFilter filterViewUpdate = new IntentFilter(Contanst.VIEWUPDATE);
		registerReceiver(viewUpdateReceiver, filterViewUpdate);
		
		mAppReceiver = new AppReceiver();
		IntentFilter filterAPP = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
		filterAPP.addDataScheme("package");
		filterAPP.addAction(Intent.ACTION_PACKAGE_REMOVED);
		registerReceiver(mAppReceiver, filterAPP);
	}
	
	private void unRegister() {
		try {
			if (mTimeReceiver != null) {
				unregisterReceiver(mTimeReceiver);
			}

			if (mNetWorkChangeReceiver != null) {
				unregisterReceiver(mNetWorkChangeReceiver);
			}
			
			if (viewUpdateReceiver != null) {
				unregisterReceiver(viewUpdateReceiver);
			}
			
			if (mAppReceiver != null) {
				unregisterReceiver(mAppReceiver);
			}
		}catch(Exception e){
			
		}
	}
	
	private AppReceiver mAppReceiver;

	public class AppReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String packageName = intent.getDataString();
			packageName = packageName.split(":")[1];
			Item item;
			List<Item> items = Contanst.fd.findAllByWhere(Item.class, "pkg="+"'"+packageName.trim()+"'");
			if (intent.getAction()
					.equals("android.intent.action.PACKAGE_ADDED")) {
				if(Contanst.postConfirm.equals(packageName)){
					postData(packageName);
					Contanst.postConfirm = "";
				}
				
				for (int i = 0; i < items.size(); i++) {
					item = items.get(i);
					((IViewUpdate)current).UpdateViewByTag(item.getTag());
				}
			}else if(intent.getAction()
					.equals("android.intent.action.PACKAGE_REMOVED")){
				for (int i = 0; i < items.size(); i++) {
					item = items.get(i);
					((IViewUpdate)current).UpdateViewByTag(item.getTag());
				}
			}
			
		}
	}
	
	public void postData(String packageName) {
		// TODO Auto-generated method stub
		JSONObject jm = new JSONObject();
		try {
			jm.put("pkg", packageName);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
		}
		
		String url = "";
		try {
			url = Contanst.postData + URLEncoder.encode(DesHelper.encrypt(jm.toString(), Contanst.ENC_KEY), "utf8");
//			System.out.println("url:"+url);
			FinalHttp fh = new FinalHttp();
			fh.get(url, new AjaxCallBack<Object>(){});
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
		}
		
	}
	
	private boolean core_done,voole_done;
	NetWorkChangeReceiver mNetWorkChangeReceiver;
	public class NetWorkChangeReceiver extends BroadcastReceiver {
		private ConnectivityManager connectivityManager;
		private NetworkInfo info;

		@SuppressWarnings("deprecation")
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				connectivityManager = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo mNetworkInfo = connectivityManager.getActiveNetworkInfo();
				if(mNetworkInfo != null && mNetworkInfo.isAvailable()){
					switch (mNetworkInfo.getType()) {
						case  ConnectivityManager.TYPE_WIFI:
							((IViewUpdate)current).netStatuUpdate(ConnectivityManager.TYPE_WIFI);
							break;
						case  ConnectivityManager.TYPE_ETHERNET:
							((IViewUpdate)current).netStatuUpdate(ConnectivityManager.TYPE_ETHERNET);
							break;
					}
					
					if(current instanceof FirstFragment){
						if(!voole_done){
							
							synchronized (MainActivity.this) {
								conn= new VooleServiceConnection() ;
								Intent voole = new Intent() ;
								voole.setAction("com.voole.epg.cooperation.aidl") ;
								bindService(voole, conn , BIND_AUTO_CREATE) ;
							}
							
							voole_done = true;
						}
					}else{
						if(!core_done){
							
							synchronized (MainActivity.this) {
								MainActivity.this.startService(new Intent(MainActivity.this, CoreService.class));
							}
							
							core_done = true;
						}
					}
				}else{
					((IViewUpdate)current).netStatuUpdate(-1);
				}
			}  
		}
	}
	
	private TimeReceiver mTimeReceiver;
	public class TimeReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_TIME_TICK)) {
				((IViewUpdate)current).TimeUpdate(SystemUtils.getTime(MainActivity.this));
				if(!DateFormat.is24HourFormat(MainActivity.this)){
					((IViewUpdate)current).StatuUpdate(SystemUtils.getStatu());
				}else{
					((IViewUpdate)current).StatuUpdate("");
				}
			}
		}
	}
	
	ViewUpdateReceiver viewUpdateReceiver;
	public class ViewUpdateReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			System.out.println("ViewUpdateReceiver********MainActivity*************************************");
			if(Contanst.VIEWUPDATE.equals(intent.getAction())){
				List<Item> items = Contanst.fd.findAll(Item.class);
				for (int i = 0; i < items.size(); i++) {
					((IViewUpdate)current).UpdateViewByTag(items.get(i).getTag());
				}
				
			}
		}
	}
	
	
	public class appKill extends AsyncTask<String, Integer, JSONObject> {
		protected JSONObject doInBackground(String... params) {
			ProcessManage pm = new ProcessManage(MainActivity.this);
			return pm.killApp();
		}
	}

	@Override
	public void onContentMoveFocus(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if(hasFocus){
			getFocus(v);
			contentFocus.setVisibility(View.VISIBLE);
			moveFocus(v, contentFocus);
		}else{
			cancleFocus(v);
			contentFocus.setVisibility(View.GONE);
		}
	}
	
	public void getFocus(final View v) {
		v.bringToFront();
		
		ViewGroup viewGroup = (ViewGroup) v;
		final View focusTitle = viewGroup.getChildAt(3);
		focusTitle.setAlpha(0);
		focusTitle.setVisibility(View.VISIBLE);
		
		ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, View.SCALE_X, 1F,
				1.03F);
		ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, View.SCALE_Y, 1F,
				1.03F);
		AnimatorSet scale = new AnimatorSet();
		scale.setDuration(300);
		scale.setInterpolator(new AccelerateInterpolator());
		scale.playTogether(scaleX, scaleY);
		scale.start();

		scale.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				super.onAnimationEnd(animation);
				if (focusTitle != null) {
					ObjectAnimator titleY = ObjectAnimator.ofFloat(focusTitle,
							View.Y, focusTitle.getY()+focusTitle.getHeight(), focusTitle.getY());
					 ObjectAnimator titleA = ObjectAnimator
					 .ofFloat(focusTitle, View.ALPHA, 0F,1F);
					 AnimatorSet tran = new AnimatorSet();
					 tran.setDuration(200);
					 tran.setInterpolator(new AccelerateInterpolator());
					 tran.playTogether(titleY, titleA);
					 tran.start();
				}
			}

			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				super.onAnimationStart(animation);
			}

		});
	}

	public void cancleFocus(final View v) {

		ViewGroup viewGroup = (ViewGroup) v;
		
		final View focusTitle = viewGroup.getChildAt(3);		
		
		ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, View.SCALE_X, 1.03F,
				1F);
		ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, View.SCALE_Y, 1.03F,
				1F);
		AnimatorSet scale = new AnimatorSet();
		scale.setDuration(300);
		scale.setInterpolator(new AccelerateInterpolator());
		scale.playTogether(scaleX, scaleY);
		scale.start();
		scale.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				super.onAnimationEnd(animation);
				if (focusTitle != null) {
					focusTitle.setVisibility(View.INVISIBLE);
					v.postInvalidate();
				}
			}

			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				super.onAnimationStart(animation);
				
			}

		});
	}
	
	public void moveFocus(View v,final View iv) {
		// TODO Auto-generated method stub
//		int scale_x = (int)(v.getWidth()*0.03);
//		int scale_y = (int)(v.getHeight()*0.03);
//		int v_width = v.getWidth()+scale_x;
//		int v_height = v.getHeight()+scale_y;
		
		int v_width = v.getWidth()+2;
		int v_height = v.getHeight()+2;
		
		ViewGroup.LayoutParams params = iv.getLayoutParams();
		params.width = iv.getWidth();
		params.height = iv.getHeight();
		iv.setLayoutParams(params);
//		float x = v.getX() - scale_x/2 - 2;
//		float y = v.getTop() - scale_y/2;
		float x = v.getX();
		float y = v.getTop();
		
		if (R.id.first_1 == v.getId() || R.id.one_1 == v.getId()) {
			v_width = 442;
			v_height = 298;
			x = 82;
			y = 46;
		} else if (v.getWidth() == 0 || v.getHeight() == 0) {
			iv.setVisibility(View.INVISIBLE);
			return;
		}
		
		Size s1 = new Size(iv.getWidth(), iv.getHeight());
		Size s2 = new Size(v_width + 37 , v_height +37);
		ValueAnimator animWH = ValueAnimator.ofObject(new SizeEvaluator(iv), s1,
				s2);

		ObjectAnimator animX = ObjectAnimator
				.ofFloat(iv, "x", iv.getX(), x - 17);
		ObjectAnimator animY = ObjectAnimator
				.ofFloat(iv, "y", iv.getY(), y - 17);
		ObjectAnimator animA = ObjectAnimator.ofFloat(iv, "alpha", 0.8f, 1f);
		AnimatorSet animSetXY = new AnimatorSet();
		animSetXY.setDuration(500);
		animSetXY.setInterpolator(new AccelerateDecelerateInterpolator());
		animSetXY.playTogether(animWH, animA, animX, animY);
		animSetXY.start();
	}
	
}
