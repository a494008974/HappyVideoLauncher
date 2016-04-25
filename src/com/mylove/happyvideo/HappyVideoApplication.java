package com.mylove.happyvideo;


import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalDb;

import com.mylove.happyvideo.base.BaseApplication;
import com.mylove.happyvideo.utils.Contanst;

public class HappyVideoApplication extends BaseApplication {
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		Contanst.fb = FinalBitmap.create(this);
		Contanst.fd = FinalDb.create(this);
		Contanst.path = getFilesDir().getPath() + "/";
	}
}
