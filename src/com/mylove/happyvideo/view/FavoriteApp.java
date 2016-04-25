package com.mylove.happyvideo.view;

import java.util.List;

import com.mylove.happyvideo.HappyVideoApplication;
import com.mylove.happyvideo.R;
import com.mylove.happyvideo.adapter.ScrollViewAdapter;
import com.mylove.happyvideo.bean.Item;
import com.mylove.happyvideo.utils.ApkManage;
import com.mylove.happyvideo.utils.Contanst;
import com.mylove.happyvideo.utils.SharedPreferencesUtils;
import com.mylove.happyvideo.view.MyHorizontalScrollView.OnItemKeyListener;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FavoriteApp extends DialogFragment {
	MyHorizontalScrollView hs;
	ScrollViewAdapter mAdapter;
	
	private static class FavoriteApponHolder {
		private static final FavoriteApp INSTANCE = new FavoriteApp();
	}

	private FavoriteApp() {
		this.setStyle(0, R.style.DownTransparent);
	}

	public static final FavoriteApp getInstance() {
		return FavoriteApponHolder.INSTANCE;
	}

	private IFavoriteUpdate favoriteUpdate = new IFavoriteUpdate() {
		
		@Override
		public void refresh(int index) {
			// TODO Auto-generated method stub
			String favorite = (String) SharedPreferencesUtils.getParam(HappyVideoApplication.getInstance(), Contanst.FAVORITE, Contanst.FAVORITE_CONFIG);
			List<PackageInfo> infos = ApkManage.getAllApps(getActivity(),favorite);
			mAdapter.setData(infos);
			hs.setAdapter(mAdapter);
			hs.moveFocusView(index);
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.scroll_horizontal_main, null);

		TextView tv = (TextView) v.findViewById(R.id.select_app);
		tv.setAlpha( 0.2f);
		tv.setText(R.string.fav_menu);
		tv.setVisibility(View.VISIBLE);

		ImageView img = (ImageView) v.findViewById(R.id.up_anim);
		img.setVisibility(View.VISIBLE);

		hs = (MyHorizontalScrollView) v.findViewById(R.id.my_horizontal_scroller);
		hs.setOnItemClickListener(new MyHorizontalScrollView.OnItemClickListener() {
			@Override
			public void onItemClick(BaseAdapter parent, View view, int position) {
				// TODO Auto-generated method stub
				PackageInfo info = (PackageInfo) parent.getItem(position);
				if(!info.applicationInfo.packageName.equals("CustomApp")){
					ApkManage.openApk(getActivity(),
							info.applicationInfo.packageName);
				}else{
					new SelectApp(favoriteUpdate,position).show(getFragmentManager(), "SELECT");
				}
			}
		});

		
		
		hs.setOnItemKeyListener(new OnItemKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event, int position) {
				// TODO Auto-generated method stub
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_DPAD_UP:
						System.out
								.println("MyHorizontalScrollView KeyEvent.KEYCODE_DPAD_UP ...................");
						dismiss();
						break;
					case KeyEvent.KEYCODE_MENU:
						System.out
								.println("MyHorizontalScrollView KeyEvent.KEYCODE_MENU ...................");
						new SelectApp(favoriteUpdate,position).show(getFragmentManager(), "SELECT");
						break;
					}
				}
				return false;
			}
		});
//		List<PackageInfo> infos = ApkManage.getAllApps(getActivity());
		
		String favorite = (String) SharedPreferencesUtils.getParam(HappyVideoApplication.getInstance(), Contanst.FAVORITE, Contanst.FAVORITE_CONFIG);
		List<PackageInfo> infos = ApkManage.getAllApps(getActivity(),favorite);
		mAdapter = new ScrollViewAdapter(getActivity(),
				infos, R.layout.scroll_view_item);
		hs.setAdapter(mAdapter);

		return v;
	}

}
