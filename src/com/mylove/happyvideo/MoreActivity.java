package com.mylove.happyvideo;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;

import com.mylove.happyvideo.adapter.MoreAdapter;
import com.mylove.happyvideo.base.BaseActivity;
import com.mylove.happyvideo.utils.ApkManage;

public class MoreActivity extends BaseActivity {
	
	GridView mGridView;
	MoreAdapter mAdapter;
	private AppReceiver mAppReceiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_layout);
		mGridView = (GridView) findViewById(R.id.bsw_all_apps);
		
		mAdapter = new MoreAdapter(this, ApkManage.getMore(this), R.layout.item_info);
		mGridView.setAdapter(mAdapter);
		
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,long parent) {
				// TODO Auto-generated method stub
				ResolveInfo item = (ResolveInfo) adapter.getItemAtPosition(position);
				openApk(item.activityInfo.packageName);
			}
		});
		
		mGridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view, int position,long parent) {
				// TODO Auto-generated method stub
				ResolveInfo item = (ResolveInfo) adapter.getItemAtPosition(position);
				Uri packageURI = Uri.parse("package:" + item.activityInfo.packageName);
				Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,
						packageURI);
				MoreActivity.this.startActivity(uninstallIntent);
				return true;
			}
		});
		
		mAppReceiver = new AppReceiver();
		IntentFilter filterAPP = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
		filterAPP.addDataScheme("package");
		filterAPP.addAction(Intent.ACTION_PACKAGE_REMOVED);
		registerReceiver(mAppReceiver, filterAPP);
	}
	
	public class AppReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String packageName = intent.getDataString();
			packageName = packageName.split(":")[1];
			if (intent.getAction()
					.equals("android.intent.action.PACKAGE_ADDED")) {
				
			} else if (intent.getAction().equals(
					"android.intent.action.PACKAGE_REMOVED")) {
				
			}
			
			mAdapter.setData(ApkManage.getMore(MoreActivity.this));
			mAdapter.notifyDataSetChanged();
			
		}
	}
}
