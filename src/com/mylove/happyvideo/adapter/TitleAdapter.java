package com.mylove.happyvideo.adapter;

import java.util.List;

import com.mylove.happyvideo.R;

import android.content.Context;

public class TitleAdapter extends CommonAdapter<String> {

	public TitleAdapter(Context context, List<String> mDatas, int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		// TODO Auto-generated constructor stub
	}

	public void setData(List<String> mDatas){
		super.setData(mDatas);
	}
	@Override
	public void convert(ViewHolder helper, String item) {
		// TODO Auto-generated method stub
		helper.setText(R.id.title, item);
	}

}
