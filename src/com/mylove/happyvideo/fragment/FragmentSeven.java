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

public class FragmentSeven extends BaseFragment implements OnKeyListener{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_seven, container, false);  
		
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
		if(event.getAction() == KeyEvent.ACTION_DOWN){
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_LEFT:
				System.out.println("KEYCODE_DPAD_LEFT");
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				System.out.println("KEYCODE_DPAD_RIGHT");
				break;
			case KeyEvent.KEYCODE_DPAD_UP:
				System.out.println("KEYCODE_DPAD_UP");
				switch(tag){
				case 701:
				case 704:
				case 707:
					getPageTurn().returnItemBar();
				}
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				System.out.println("KEYCODE_DPAD_DOWN");
				break;
			}
		}
		return false;
	}
}
