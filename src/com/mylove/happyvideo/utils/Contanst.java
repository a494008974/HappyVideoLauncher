package com.mylove.happyvideo.utils;

import com.mylove.happyvideo.R;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalDb;

/**
 * Created by Administrator on 2015/11/25 0025.
 */
public class Contanst {
	
	public static String[] preinstall = { "/system/etc/property/app",
		"/system/rkpreinstall", "/system/preinstall" };
	
	public static final String preinstall_key1 = "9999";
	public static final String preinstall_key2 = "114477";
	public static final String preinstall_key3 = "654321";
	public static final String preinstall_key4 = "123456";
	
	
    public static final String CORESERVICE = "CoreServiceInit";
    public static final String FIRSTRUN = "FIRSTRUN";
    public static final String FIRSTCONFIG = "FIRSTCONFIG";
    public static final String INSTALLED = "INSTALLED";
    
    public static final String FAVORITE = "FAVORITE";
    public static final String FAVORITE_CONFIG = " ; ; ; ; ; ;";
    
    public static final int FIRST = 0x12;
	public static final int SECOND = 0x13;
    
    public static String[] titles = {"推荐","影视","教育","应用"};
    
    public static String[] pkgs = {"com.voole.epg","com.ktcp.video","com.voole.magictv","com.xiaojie.tv"};
    //voole 数据源
    public static String[] vooleTag = {"801","802","803","804","805","806","807","808","809"};
    public static int[] vooleChannel = {R.drawable.voole_1,R.drawable.voole_2,R.drawable.voole_3,
								    	R.drawable.voole_4,R.drawable.voole_5,R.drawable.voole_6,
								    	R.drawable.voole_7,R.drawable.voole_8,R.drawable.voole_9};
    //数据获取源
    public static String domain = "http://mng.on-best.com/index.php/win8/getContentVer1?data=";
    public static String postData = "http://mng.on-best.com/index.php/win8/postData?data=";
    public static String postConfirm = "";
    
	public final static String ENC_KEY = "win81688";
	public final static String DES_KEY = "win8fafa";
	public final static String tem_index = "happy_video_index";
	public final static String downmain = "http://down.on-best.com/";
	
	
	//界面更新广播
	public static final String VIEWUPDATE = "com.mylove.happyvideo.VIEWUPDATE";
	
	public static FinalBitmap fb;
	public static FinalDb fd;
	
	public static String path;
}
