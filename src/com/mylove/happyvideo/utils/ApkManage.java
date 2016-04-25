package com.mylove.happyvideo.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;





import com.mylove.happyvideo.HappyVideoApplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.widget.Toast;

public class ApkManage {
	public static PackageInfo getAPKInfo(Context ctx, String apk, boolean type) {// 获取apk的信息
		PackageManager pm = ctx.getPackageManager();
		PackageInfo pakinfo = null;
		if (type) {
			try {
				pakinfo = pm.getPackageInfo(apk, PackageManager.GET_ACTIVITIES);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
			}
		} else {
			pakinfo = pm.getPackageArchiveInfo(apk,
					PackageManager.GET_UNINSTALLED_PACKAGES);
		}
		return pakinfo;
	}

	public static boolean openApk(Context context, String pkg) {
		try {
			PackageManager pm = context.getPackageManager();
			Intent intent = pm.getLaunchIntentForPackage(pkg);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
	}
	/** 
	 * 查询手机内非系统应用 
	 * @param context 
	 * @return 
	 */  
	public static List<PackageInfo> getAllApps(Context context) {  
		List<PackageInfo> apps = new ArrayList<PackageInfo>();  
	    PackageManager pManager = context.getPackageManager();  
	    //获取手机内所有应用  
	    List<PackageInfo> paklist = pManager.getInstalledPackages(0);  
	    for (int i = 0; i < paklist.size(); i++) {  
	        PackageInfo pak = (PackageInfo) paklist.get(i);  
	        //判断是否为非系统预装的应用程序  
	        if (((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0
	        		|| pak.applicationInfo.packageName.equals("com.voole.epg")) 
	        		&& !pak.applicationInfo.packageName.equals("com.android.rockchip")
	        		&& !pak.applicationInfo.packageName.equals("com.android.apkinstaller")) {  
	            // customs applications  
	            apps.add(pak);  
	        }  
	    }  
	    int n=0;
	    for(int i=0; i<Contanst.pkgs.length; i++){
	    	for(int j=0; j<apps.size(); j++){
	    		if(apps.get(j).applicationInfo.packageName.equals(Contanst.pkgs[i])){
	    			PackageInfo info = apps.get(n);
	    			apps.set(n, apps.get(j));
	    			apps.set(j, info);
	    			n++;
	    			break;
	    		}
	    	}
	    }
	    
	    return apps;  
	} 
	
	public static List<PackageInfo> getAllApps(Context ctx,String pkgs) {  
		List<PackageInfo> apps = new ArrayList<PackageInfo>();
		String pkg[] = pkgs.split(";");
		PackageManager pm = ctx.getPackageManager();
		for(int i=0; i<pkg.length; i++){
			PackageInfo pkgInfo = null;
			if(checkInstall(ctx, pkg[i])){
				try {
					pkgInfo = pm.getPackageInfo(pkg[i],
							PackageManager.GET_PERMISSIONS);
					apps.add(pkgInfo);
				} catch (NameNotFoundException e) {
				}
			}else{
				pkgInfo = new PackageInfo();
				pkgInfo.applicationInfo = new ApplicationInfo();
				pkgInfo.applicationInfo.packageName = "CustomApp";
				apps.add(pkgInfo);
			}
		}
		return apps;
	}
	
	public static boolean checkInstall(Context ctx, String apk) {// 检查是否安装
		// TODO Auto-generated method stub
		boolean install = false;
		PackageManager pm = ctx.getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(apk, 1);
			if (info != null && info.activities.length > 0) {
				install = true;
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
		}
		return install;
	}

	public static void install(Context ctx, String filename) {// 安装应用
		Uri uri = Uri.fromFile(new File(filename));
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		ctx.startActivity(intent);
	}

	public static void uninstall(Context ctx, String apk) {// 卸载应用
		Uri packageURI = Uri.parse("package:" + apk);// "package:com.demo.CanavaCancel"
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
		uninstallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		ctx.startActivity(uninstallIntent);
	}

	public static void execCommand(final String path) { // 静默安装
		new Thread() {
			public void run() {
				Process install = null;
				try {
					// 安装apk命令
					install = Runtime.getRuntime()
							.exec("pm install -r " + path);
					install.waitFor();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (install != null) {
						install.destroy();
					}
				}
			}
		}.start();
	}

	public static int installSlient(Context paramContext, String path) {
		if (!new File(path).exists())
			return -1;
		StringBuilder localStringBuilder = new StringBuilder();
		Process localProcess = null;
		BufferedReader localBufferedReader = null;
		try {
			chmod("777", path);
			localProcess = Runtime.getRuntime().exec(
					"pm install -r   " + path);
			localBufferedReader = new BufferedReader(new InputStreamReader(
					localProcess.getInputStream()));
			String str = localBufferedReader.readLine();
			if (str != null)
				localStringBuilder.append(str);
			if (localStringBuilder.toString().contains("Success")) {
				return 1;
			} else {
				return 0;
			}
		} catch (Exception localException) {
		}
		try {
			localBufferedReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		localProcess.destroy();
		return 0;
	}

	public static void chmod(String permission, String path) {
		try {
			String command = "chmod " + permission + " " + path;
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(command);
		} catch (IOException e) {
		}
	}
	
	
	 public static AppInfoData getApkFileInfo(Context ctx, String apkPath) 
	 {  
	     System.out.println(apkPath);  
	     File apkFile = new File(apkPath);  
	     if (!apkFile.exists() || !apkPath.toLowerCase().endsWith(".apk"))
	     {  
	         System.out.println("file path is not correct");  
	         return null;  
	     }  
	     
	     AppInfoData appInfoData;  
	     String PATH_PackageParser = "android.content.pm.PackageParser";  
	     String PATH_AssetManager = "android.content.res.AssetManager";  
	     try
	     {  
	         Class<?> pkgParserCls = Class.forName(PATH_PackageParser);  
	         Class<?>[] typeArgs = {String.class};  
	         Constructor<?> pkgParserCt = pkgParserCls.getConstructor(typeArgs);  
	         Object[] valueArgs = {apkPath};  
	         Object pkgParser = pkgParserCt.newInstance(valueArgs);  
	         
	         DisplayMetrics metrics = new DisplayMetrics();  
	         metrics.setToDefaults();  
	         typeArgs = new Class<?>[]{File.class,String.class,  
	         DisplayMetrics.class,int.class};  
	         Method pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod("parsePackage", typeArgs);  
	         
	         valueArgs=new Object[]{new File(apkPath),apkPath,metrics,0};  
	         
	         Object pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser, valueArgs);  
	         
	         if (pkgParserPkg==null)
	         {  
	             return null;  
	         }  
	         Field appInfoFld = pkgParserPkg.getClass().getDeclaredField("applicationInfo");  
	         
	         if (appInfoFld.get(pkgParserPkg)==null)
	         {  
	             return null;  
	         }  
	         ApplicationInfo info = (ApplicationInfo) appInfoFld.get(pkgParserPkg);     
	         
	         Class<?> assetMagCls = Class.forName(PATH_AssetManager);     
	         Object assetMag = assetMagCls.newInstance();  
	         typeArgs = new Class[1];  
	         typeArgs[0] = String.class;  
	         Method assetMag_addAssetPathMtd = assetMagCls.getDeclaredMethod("addAssetPath", typeArgs);  
	         valueArgs = new Object[1];  
	         valueArgs[0] = apkPath;  
	         assetMag_addAssetPathMtd.invoke(assetMag, valueArgs);  
	         
	         Resources res = ctx.getResources();  
	         typeArgs = new Class[3];  
	         typeArgs[0] = assetMag.getClass();  
	         typeArgs[1] = res.getDisplayMetrics().getClass();  
	         typeArgs[2] = res.getConfiguration().getClass();  
	         Constructor<Resources> resCt = Resources.class.getConstructor(typeArgs);  
	         valueArgs = new Object[3];  
	         valueArgs[0] = assetMag;  
	         valueArgs[1] = res.getDisplayMetrics();  
	         valueArgs[2] = res.getConfiguration(); 
	         
	         res = (Resources) resCt.newInstance(valueArgs);  
	         
	         appInfoData = new AppInfoData();  
	         if (info!=null)
	         {  
	             if (info.icon != 0) 
	             {
	                 Drawable icon = res.getDrawable(info.icon);
	                 appInfoData.setAppicon(icon);  
	             }  
	             if (info.labelRes != 0) 
	             {  
	                 String neme = (String) res.getText(info.labelRes);
	                 appInfoData.setAppname(neme);  
	             }else 
	             {  
	                 String apkName=apkFile.getName();  
	                 appInfoData.setAppname(apkName.substring(0,apkName.lastIndexOf(".")));  
	             }  
	             String pkgName = info.packageName;
	             appInfoData.setApppackage(pkgName);  
	         }
	         else 
	         {  
	             return null;  
	         }     
	         PackageManager pm = ctx.getPackageManager();  
	         PackageInfo packageInfo = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);  
	         if (packageInfo != null)
	         {  
	             appInfoData.setAppversion(packageInfo.versionName);
	             appInfoData.setAppversionCode(packageInfo.versionCode);
	         }  
	         return appInfoData;  
	     } catch (Exception e)
	     {   
	         e.printStackTrace();  
	     }  
	     return null;  
	 }

	public static List<ResolveInfo> getMore(Context context) {
		// TODO Auto-generated method stub
		List<ResolveInfo> infos = new ArrayList<ResolveInfo>();
		PackageManager pm = context.getPackageManager();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> apps = pm.queryIntentActivities(mainIntent,0);
		Collections.sort(apps, new ResolveInfo.DisplayNameComparator(
				pm));
		for (int i = 0; i < apps.size(); i++) {
			ResolveInfo info = apps.get(i);
			if(info.activityInfo.packageName.equals("com.mylove.happyvideo")||
					info.activityInfo.packageName.equals("com.android.development")||
					info.activityInfo.packageName.equals("com.rockchip.wfd")||
					info.activityInfo.packageName.equals("com.rockchip.mediacenter")||
					info.activityInfo.packageName.equals("com.mylove.settting")||
//					info.activityInfo.packageName.equals("com.bsw.update")||
					info.activityInfo.packageName.equals("com.android.apkinstaller")){
				continue;
			}
			
			if(info.activityInfo.packageName.equals("com.voole.epg")){
				if(infos.size() > 0){
					infos.add(infos.get(0));
					infos.set(0, info);
				}
				continue;
			}
			
			infos.add(info);
		}
		
		return infos;
	}

	public static void selectApp(int position, String packageName) {
		// TODO Auto-generated method stub
		String favorite = (String) SharedPreferencesUtils.getParam(HappyVideoApplication.getInstance(), Contanst.FAVORITE, Contanst.FAVORITE_CONFIG);
		String pkg[] = favorite.split(";");
		if(pkg.length > position){
			pkg[position] = packageName;
		}
		
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < pkg.length; i++) {
			sb.append(pkg[i]).append(";");
		}
		SharedPreferencesUtils.setParam(HappyVideoApplication.getInstance(), Contanst.FAVORITE, sb.toString());
	}
}
