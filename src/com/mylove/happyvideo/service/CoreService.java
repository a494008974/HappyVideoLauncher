package com.mylove.happyvideo.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import net.tsz.afinal.FinalDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.trinea.android.common.entity.HttpResponse;
import cn.trinea.android.common.util.HttpUtils;

import com.mylove.happyvideo.bean.Item;
import com.mylove.happyvideo.bean.Title;
import com.mylove.happyvideo.utils.Contanst;
import com.mylove.happyvideo.utils.DesHelper;
import com.mylove.happyvideo.utils.NetTools;
import com.mylove.happyvideo.utils.SystemUtils;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CoreService extends IntentService {

	List<Item> current;
	List<Title> titleCurrent;
	boolean done = false;

	public CoreService() {
		super("CoreService");

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("CoreService .................................");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		try {
			String data = NetTools.get(getUrl());
			String result = DesHelper.decrypt(data,Contanst.DES_KEY);
			System.out.println("result:"+result);
			JSONObject json = new JSONObject(result);
			if (json.getInt("status") == 1) {
				String info = json.getString("info");
				if (!info.equals("") || !info.equals("null")) {
					JSONObject model = new JSONObject(info);
					
					/*
					JSONArray titles = model.getJSONObject("data").getJSONArray(
							"cls");
					for (int i = 0; i < titles.length(); i++) {
						JSONObject jo = titles.getJSONObject(i);
						Title title = new Title();
						title.setScreen(jo.getString("screen"));
						title.setTitle(jo.getString("title"));
						
						titleCurrent = Contanst.fd.findAllByWhere(Title.class, "screen="
								+ "'" + title.getScreen() + "'");
						if (titleCurrent != null && titleCurrent.size() > 0) {
							Contanst.fd.update(title,"screen=" + "'" + title.getScreen() + "'");
						}else{
							Contanst.fd.save(title);
						}
					}
					*/
					JSONArray list = model.getJSONObject("data").getJSONArray(
							"info");
					for (int i = 0; i < list.length(); i++) {
						JSONObject jo = list.getJSONObject(i);
						Item item = new Item();
						item.setImgurl(jo.getString("img_url"));
						item.setTitle(jo.getString("title"));
						item.setIshold(jo.getString("is_hold"));
						item.setIslock(jo.getString("is_lock"));
						item.setTag(jo.getString("tag"));
						item.setIcon(jo.getString("ico"));
						item.setAct(jo.getString("act"));
						item.setPkg(jo.getString("pkg"));
						item.setUrl(jo.getString("url"));
						item.setWay(jo.getString("way"));
						item.setWayval(jo.getString("way_val"));
						item.setIslink(jo.getString("is_link"));
						item.setLink(jo.getString("link"));
						
						current = Contanst.fd.findAllByWhere(Item.class, "tag="
								+ "'" + item.getTag() + "'");
						
						if ("1".equals(item.getIslock().trim())) {
							if (current != null && current.size() > 0) {
								Contanst.fd.update(item,"tag=" + "'" + item.getTag() + "'");
							} else {
								Contanst.fd.save(item);
							}
						}else{
							if (current != null && current.size() > 0) {
								if (current.get(0).getPkg().equals(item.getPkg())) { // 判断是否是后台推的apk,是 ----清除数据
									item.setPkg("");
									item.setAct("");
									item.setIshold("");
									Contanst.fd.update(item,"tag=" + "'" + item.getTag() + "'");
								}
							}else{
								item.setPkg("");
								item.setAct("");
								item.setIshold("");
								Contanst.fd.save(item);
							}
						}
					}
					// 发送广播
					if(!done){
						Intent viewUpdate = new Intent(Contanst.VIEWUPDATE);
						sendBroadcast(viewUpdate);
						done = true;
					}

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}

	private String getUrl() {
		String firmware = SystemUtils.getProp("ro.product.firmware");
		String path = "";
		try {
			path = firmware.substring(0, firmware.indexOf("_"));
		} catch (Exception e) {
			// TODO: handle exception
			path = "BSWSD";
		}
		JSONObject jm = new JSONObject();
		try {
			jm.put("model", android.os.Build.MODEL);
			jm.put("path", path);
			jm.put("tem_index", Contanst.tem_index);
			jm.put("serial", SystemUtils.getProp("debug.product.serial"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
		}

		System.out.println("params:"+jm.toString());
		String data = DesHelper.encrypt(jm.toString(), Contanst.ENC_KEY);
		String url = "";
		
		try {
			url = Contanst.domain + URLEncoder.encode(data, "utf8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
		}
		System.out.println("url:"+url);
		return url;
	}

}
