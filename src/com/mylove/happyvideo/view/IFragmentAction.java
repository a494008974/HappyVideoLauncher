package com.mylove.happyvideo.view;

import android.view.View;

public interface IFragmentAction {
	public void fragmentClick(View v,IViewUpdate update,int n);
	public boolean fragmentLongClick(View v,int n);
	public void onFragmentMoveFocus(View v,View focus,boolean hasFocus);
	public boolean onFragmentActionMenu(View v,IViewUpdate update,int n);
}
