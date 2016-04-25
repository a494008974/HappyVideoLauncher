package com.mylove.happyvideo.adapter;

import java.util.ArrayList;
import java.util.List;

import com.zhy.autolayout.utils.AutoUtils;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class CommonAdapter<T> extends BaseAdapter {
	protected LayoutInflater mInflater;
	protected Context mContext;
	protected List<T> mDatas;
	protected final int mItemLayoutId;

	public CommonAdapter(Context context, List<T> mDatas, int itemLayoutId) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.mDatas = mDatas == null ? new ArrayList<T>() : new ArrayList<T>(mDatas);
		this.mItemLayoutId = itemLayoutId;
	}

	public void setData(List<T> mDatas){
		this.mDatas = mDatas;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		// TODO Auto-generated method stub
		if (position >= mDatas.size()) return null;
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder viewHolder = getViewHolder(position, convertView,
				parent);
		convert(viewHolder, getItem(position));
		AutoUtils.autoSize(viewHolder.getConvertView());
		return viewHolder.getConvertView();
	}

	public abstract void convert(ViewHolder helper, T item);

	private ViewHolder getViewHolder(int position, View convertView,
			ViewGroup parent) {
		return ViewHolder.get(mContext, convertView, parent, mItemLayoutId,
				position);
	}

	public Context getContext(){
		return mContext;
	}
}
