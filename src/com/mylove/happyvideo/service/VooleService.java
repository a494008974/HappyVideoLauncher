package com.mylove.happyvideo.service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mylove.happyvideo.bean.Item;
import com.mylove.happyvideo.parser.DesktopParser;
import com.mylove.happyvideo.utils.Contanst;
import com.mylove.happyvideo.utils.NetTools;

import android.app.IntentService;
import android.content.Intent;

public class VooleService extends IntentService {

	boolean done = false;
	String url = "";
	List<Item> current;
	
	public VooleService() {
		super("VooleService");

	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("VooleService .................................");
		url = intent.getStringExtra("url");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		if(url != null && !"".equals(url)){
			String result = NetTools.get(url);
			DesktopParser desktopParser = new DesktopParser(result);
			String desktop = desktopParser.getDesktop();
			if(desktop != null){
				desktop = desktop.replaceAll("oemid=(\\d*)", "oemid=100124");
				
				// 频道 json
				String channelUrl = "";
				
				String channelReuslt = NetTools.get(desktop);
				try {
					JSONObject channelJson = new JSONObject(channelReuslt);
					if("1".equals(channelJson.getString("status"))){
						channelUrl = channelJson.getJSONObject("data").getJSONArray("interface").getJSONObject(0).getString("url");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
				}
				
				String channel = "";
				if(!"".equals(channelUrl)){
					try {
						channelReuslt = NetTools.get(channelUrl);
						JSONObject channelJson = new JSONObject(channelReuslt);
						if("1".equals(channelJson.getString("status"))){
							channel = channelJson.getJSONArray("data").getJSONObject(0).getString("sourceurl");
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
					}
				}
				
				if(!"".equals(channel)){
					try {
						channelReuslt = NetTools.get(channel);
						JSONObject channelJson = new JSONObject(channelReuslt);
						if("1".equals(channelJson.getString("status"))){
							JSONArray jarry = channelJson.getJSONArray("data");
							int n = Math.min(jarry.length(), Contanst.vooleTag.length);
							for(int i=0; i<n; i++){
								JSONObject jo = jarry.getJSONObject(i);
								Item item = new Item();
								item.setImgurl(i+"");
//								item.setImgurl(jo.getJSONArray("imglist").getString(0));
								item.setTitle(jo.getString("filmname"));
								item.setIshold("1");
								item.setIslock("1");
								item.setTag(Contanst.vooleTag[i]);
								item.setIcon("");
								item.setAct(jo.getString("activity"));
								item.setPkg(jo.getString("activity"));
								item.setUrl("");
								item.setWay("1");
								
								Intent wayIntent = new Intent();
								wayIntent.setAction(jo.getString("parameter"));
								wayIntent.putExtra("id", jo.getString("sourceurl"));
								wayIntent.putExtra("jumpType", "1");
								
								item.setWayval(wayIntent.toUri(0));
								item.setIslink("");
								item.setLink("");
								
								current = Contanst.fd.findAllByWhere(Item.class, "tag="
										+ "'" + item.getTag() + "'");
								
								if ("1".equals(item.getIslock().trim())) {
									if (current != null && current.size() > 0) {
										Contanst.fd.update(item,"tag=" + "'" + item.getTag() + "'");
									} else {
										Contanst.fd.save(item);
									}
								}
							}
							
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
					}
				}
				
			}
			
		}
		
		if(!done){
			Intent viewUpdate = new Intent(Contanst.VIEWUPDATE);
			sendBroadcast(viewUpdate);
			done = true;
		}
	}


}























