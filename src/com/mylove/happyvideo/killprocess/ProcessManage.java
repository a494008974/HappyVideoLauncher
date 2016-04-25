package com.mylove.happyvideo.killprocess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
//import android.os.Process;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class ProcessManage {
	private final String TAG = "ProcessManage";
	private Context ct;
	private ActivityManager am;
	private ArrayList<DetailProceess> avaiAppInfo;
	private PackagesInfo packsInfo;
	public ProcessManage(Context ctx){
		ct = ctx;
		am = (ActivityManager) ct.getSystemService(Context.ACTIVITY_SERVICE);
		packsInfo = new PackagesInfo(ct);
		getRunningProcess();
	}
	public String getCurrentPname(){//获取当前activity
		ActivityManager manager = (ActivityManager) ct.getSystemService(Context.ACTIVITY_SERVICE);  
		List<RunningTaskInfo> runningTasks = manager.getRunningTasks(1);
		RunningTaskInfo cinfo = runningTasks.get(0);
		ComponentName component = cinfo.topActivity;
		return component.getPackageName();
	}
	public void getRunningProcess() {
		List<RunningAppProcessInfo> allRunning = am.getRunningAppProcesses();
		System.out.println("allRunning.size():::::"+allRunning.size());
		avaiAppInfo = new ArrayList<DetailProceess>();
		for (RunningAppProcessInfo info : allRunning) {
			String processName = info.processName;
			System.out.println("processName:"+processName);
			if (	   "system".equals(processName)
					|| "com.android.phone".equals(processName)
					|| "android.process.acore".equals(processName)
//					|| "android.process.media".equals(processName)
					||  ct.getPackageName().equals(processName)
					|| "com.mylove.settting".equals(processName)
					|| "com.mylove.update".equals(processName)
					|| "com.voole.epg".equals(processName)
					|| "com.sohu.inputmethod.sogoupad".equals(processName)
					|| "com.ce3g.android.v5im".equals(processName)
					|| "com.android.inputmethod.latin".equals(processName)
					|| "com.android.inputmethod.pinyin".equals(processName)
					) {
				continue;
			}
			final DetailProceess dp = new DetailProceess(ct, info);
			dp.fetchApplicationInfo(packsInfo);
			dp.getPackageInfo();
			if(dp.isGoodProcess()){
				avaiAppInfo.add(dp);	
			}
		}
	}
	public JSONObject killApp(){
		int count = avaiAppInfo.size();
		System.out.println("avaiAppInfo.size():::"+avaiAppInfo.size());
		int num = 0;
		long quantity = 0;
		for(int i = 0; i<count; i++){
			num++;
			quantity = quantity + avaiAppInfo.get(i).getMemory();
			System.out.println("####################################killBackgroundProcesses:"+avaiAppInfo.get(i).getProcessName()+"PID::"+avaiAppInfo.get(i).getRuninfo().pid);
			am.forceStopPackage(avaiAppInfo.get(i).getPackageInfo().packageName);
		}
		JSONObject jo = new JSONObject();
		try {
			jo.put("quantity", quantity+"");
			jo.put("num", num+"");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jo;
	}
	
	private void ShellForceStopAPK(String pkgName){
		Process sh = null;
		try {
			final String Command = "am force-stop "+pkgName+ "\n";
			sh = Runtime.getRuntime().exec(Command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			sh.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
