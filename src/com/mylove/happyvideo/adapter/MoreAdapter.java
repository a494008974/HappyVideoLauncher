package com.mylove.happyvideo.adapter;

import java.util.List;

import com.mylove.happyvideo.R;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class MoreAdapter extends CommonAdapter<ResolveInfo> {

	Context mContext;
	public MoreAdapter(Context context, List<ResolveInfo> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public void setData(List<ResolveInfo> mDatas){
		super.setData(mDatas);
	}
	
	@Override
	public void convert(ViewHolder helper, ResolveInfo item) {
		// TODO Auto-generated method stub
		PackageManager pm = mContext.getPackageManager();
		helper.setImageDrawable(R.id.app_img, item.loadIcon(pm));
		helper.setText(R.id.app_title, (String)item.loadLabel(pm));
	}

}
