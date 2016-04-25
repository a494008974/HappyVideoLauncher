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

public class FragmentThree extends BaseFragment implements OnKeyListener{
	
	public static String TAG = "FragmentThree";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_three, container, false);  
		
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
		System.out.println("TAG====================>"+tag);
		if(event.getAction() == KeyEvent.ACTION_DOWN){
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_LEFT:
				System.out.println("KEYCODE_DPAD_LEFT");
				switch (tag) {
				case 301:
					if(getPageTurn() != null){
						getPageTurn().turnPage(true, R.id.two_5);
					}
					break;
				case 306:
					if(getPageTurn() != null){
						getPageTurn().turnPage(true, R.id.two_10);
					}
					break;
				}
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				System.out.println("KEYCODE_DPAD_RIGHT");
				switch (tag) {
				case 304:
					if(getPageTurn() != null){
						getPageTurn().turnPage(true, R.id.four_1);
					}
					break;
				case 305:
					if(getPageTurn() != null){
						getPageTurn().turnPage(true, R.id.four_2);
					}
					break;
				case 310:
					if(getPageTurn() != null){
						getPageTurn().turnPage(true, R.id.four_7);
					}
					break;
				}
				break;
			case KeyEvent.KEYCODE_DPAD_UP:
				System.out.println("KEYCODE_DPAD_UP");
				switch(tag){
				case 301:
				case 302:
				case 303:
				case 304:
					if(getPageTurn() != null){
						getPageTurn().returnItemBar();
					}
				}
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				System.out.println("KEYCODE_DPAD_DOWN");
				switch (tag) {
				case 306:
				case 307:
				case 308:
				case 309:
				case 310:
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
