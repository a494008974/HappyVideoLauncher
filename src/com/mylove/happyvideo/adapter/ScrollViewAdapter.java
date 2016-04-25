package com.mylove.happyvideo.adapter;

import java.util.List;

import com.mylove.happyvideo.R;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;


public class ScrollViewAdapter extends CommonAdapter<PackageInfo> {
	Context context;
	PackageManager pm;
	public ScrollViewAdapter(Context context,
			List<PackageInfo> infos, int itemLayoutId) {
		super(context, infos, itemLayoutId);
		// TODO Auto-generated constructor stub
		this.context = context;
		pm = context.getPackageManager();
	}

	@Override
	public void convert(ViewHolder helper, PackageInfo info) {
		// TODO Auto-generated method stub
		if(!info.applicationInfo.packageName.equals("CustomApp")){
			helper.setImageDrawable(R.id.item_img, pm.getApplicationIcon(info.applicationInfo));
			helper.setText(R.id.item_name, pm.getApplicationLabel(info.applicationInfo).toString());
		}else{
			helper.setImageResource(R.id.item_img, R.drawable.add_common_icon);
			helper.setText(R.id.item_name, R.string.add);
		}
	}

}
