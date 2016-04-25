package com.mylove.happyvideo.fragment;

import com.mylove.happyvideo.R;
import com.mylove.happyvideo.utils.Contanst;
import com.mylove.happyvideo.view.HomeFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnKeyListener;
import android.widget.RelativeLayout;

public class FragmentOne extends BaseFragment implements OnKeyListener {

	public static String TAG = "FragmentOne";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		System.out.println("onCreateView ................... FragmentOne");
		View view = inflater.inflate(R.layout.fragment_one, container, false);

		ViewGroup mGroup = (ViewGroup) view;
		init(mGroup,Contanst.SECOND);

		if (mGroup != null) {
			for (int i = 0; i < mGroup.getChildCount(); i++) {
				View v = mGroup.getChildAt(i);
				v.setOnKeyListener(this);
			}
		}

		return view;
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		int tag = Integer.parseInt((String) v.getTag());
		System.out.println("TAG====================>"+tag+"      v.getId ==>"+v.getId());
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			System.out.println("KEYCODE_DPAD_DOWN HOME : " + System.currentTimeMillis());
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_LEFT:
				System.out.println("KEYCODE_DPAD_LEFT");
				switch (tag) {
				case 101:
					getPageTurn().turnPage(3, R.id.four_5);
					return true;
				case 107:
					getPageTurn().turnPage(3, R.id.four_11);
					return true;
				}
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				System.out.println("KEYCODE_DPAD_RIGHT");
				
				switch (tag) {
				case 105:
					if(getPageTurn() != null){
						getPageTurn().turnPage(true, R.id.two_1);
					}
					break;
				case 106:
					if(getPageTurn() != null){
						getPageTurn().turnPage(true, R.id.two_2);
					}
					break;
				case 111:
					if(getPageTurn() != null){
						getPageTurn().turnPage(true, R.id.two_6);
					}
					break;
				}
				break;
			case KeyEvent.KEYCODE_DPAD_UP:
				System.out.println("KEYCODE_DPAD_UP");
				switch (tag) {
				case 101:
				case 102:
				case 103:
				case 105:
					if(getPageTurn() != null){
						getPageTurn().returnItemBar();
					}
				}
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				System.out.println("KEYCODE_DPAD_DOWN");
				switch (tag) {
				case 107:
				case 108:
				case 109:
				case 110:
				case 111:
					if(getAction() != null){
						getAction().onDownKeyClick();
					}
				}
				break;
			case KeyEvent.KEYCODE_MENU:
				if(getAction() != null){
					return getAction().actionMenu(v);
				}
			}
		}else if(event.getAction() == KeyEvent.ACTION_UP){
			if(getAction() != null){
				getAction().onKeyUpClick();
			}
		}
		return false;
	}
}
