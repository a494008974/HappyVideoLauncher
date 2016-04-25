package com.mylove.happyvideo.view;

import java.util.List;

import com.mylove.happyvideo.R;
import com.mylove.happyvideo.adapter.ScrollViewAdapter;
import com.mylove.happyvideo.bean.Item;
import com.mylove.happyvideo.utils.ApkManage;
import com.mylove.happyvideo.utils.Contanst;


import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CheckApp extends DialogFragment {
	
	IViewUpdate update;
	String tag;
	String title;
	
	public CheckApp(IViewUpdate update,String tag) {
		// TODO Auto-generated constructor stub
		this.tag = tag;
		this.update = update;
		this.setStyle(0, R.style.Transparent);
	}
	
	public CheckApp(IViewUpdate update, String tag, String title) {
		// TODO Auto-generated constructor stub
		this.tag = tag;
		this.update = update;
		this.title = title;
		this.setStyle(0, R.style.Transparent);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.select_app_main, null);

		TextView tv = (TextView) v.findViewById(R.id.select_app);
		tv.setAlpha( 0.2f);
		if(title != null && !"".equals(title)){
			tv.setText(title);
		}
		
		MyHorizontalScrollView hs = (MyHorizontalScrollView) v
				.findViewById(R.id.my_horizontal_scroller);
		hs.setOnItemClickListener(new MyHorizontalScrollView.OnItemClickListener() {
			@Override
			public void onItemClick(BaseAdapter parent, View view, int position) {
				// TODO Auto-generated method stub
				PackageInfo info = (PackageInfo) parent.getItem(position);
				Item item = new Item();
				item.setTag(tag);
				item.setPkg(info.applicationInfo.packageName);
				Contanst.fd.update(item,"tag=" + "'" + item.getTag() + "'");
				update.UpdateViewByTag(tag);
				dismiss();
			}
		});
		List<PackageInfo> infos = ApkManage.getAllApps(getActivity());
		ScrollViewAdapter mAdapter = new ScrollViewAdapter(getActivity(), infos,R.layout.scroll_view_item);
		hs.setAdapter(mAdapter);
		return v;
	}
	
}
