package com.mylove.happyvideo.view;

import java.util.List;

import com.mylove.happyvideo.R;
import com.mylove.happyvideo.adapter.ScrollViewAdapter;
import com.mylove.happyvideo.bean.Item;
import com.mylove.happyvideo.utils.ApkManage;
import com.mylove.happyvideo.utils.Contanst;


import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SelectApp extends DialogFragment {
	
	IFavoriteUpdate update;
	int index;
	String title;
	
	public SelectApp(IFavoriteUpdate update,int position) {
		// TODO Auto-generated constructor stub
		this.index = position;
		this.update = update;
		this.setStyle(0, R.style.Transparent);
	}
	
	public SelectApp(IFavoriteUpdate update, int position, String title) {
		// TODO Auto-generated constructor stub
		this.index = position;
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
				ApkManage.selectApp(index,info.applicationInfo.packageName);
				dismiss();
				update.refresh(index);
			}
		});
		List<PackageInfo> infos = ApkManage.getAllApps(getActivity());
		ScrollViewAdapter mAdapter = new ScrollViewAdapter(getActivity(), infos,R.layout.scroll_view_item);
		hs.setAdapter(mAdapter);
		return v;
	}
	
}
