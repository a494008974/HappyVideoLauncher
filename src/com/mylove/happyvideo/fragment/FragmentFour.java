package com.mylove.happyvideo.fragment;

import com.mylove.happyvideo.R;
import com.mylove.happyvideo.utils.Contanst;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnKeyListener;

public class FragmentFour extends BaseFragment implements OnKeyListener{
	public static String TAG = "FragmentFour";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_four, container, false);  
		
		ViewGroup mGroup = (ViewGroup)view;
		init(mGroup,Contanst.SECOND);
		
		if(mGroup != null){
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
		int tag = Integer.parseInt((String)v.getTag());
		System.out.println("TAG====================>"+tag+"      v.getId ==>"+v.getId());
		if(event.getAction() == KeyEvent.ACTION_DOWN){
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_LEFT:
				System.out.println("KEYCODE_DPAD_LEFT");
				switch (tag) {
				case 401:
					getPageTurn().turnPage(true, R.id.three_4);
					break;
				case 402:
					getPageTurn().turnPage(true, R.id.three_5);
					break;
				case 407:
					getPageTurn().turnPage(true, R.id.three_10);
					break;
				}
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				System.out.println("KEYCODE_DPAD_RIGHT");
				switch (tag) {
				case 405:
				case 406:
					if(getPageTurn() != null){
						getPageTurn().turnPage(0, R.id.one_1);
					}
					return true;
				case 411:
					if(getPageTurn() != null){
						getPageTurn().turnPage(0, R.id.one_7);
					}
					return true;
				}
				break;
			case KeyEvent.KEYCODE_DPAD_UP:
				System.out.println("KEYCODE_DPAD_UP");
				switch(tag){
				case 401:
				case 403:
				case 404:
				case 405:
					getPageTurn().returnItemBar();
				}
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				System.out.println("KEYCODE_DPAD_DOWN");
				switch (tag) {
				case 407:
				case 408:
				case 409:
				case 410:
				case 411:
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
