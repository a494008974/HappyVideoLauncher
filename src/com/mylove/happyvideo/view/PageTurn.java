package com.mylove.happyvideo.view;

import android.view.View;

public interface PageTurn {
	void turnPage(boolean next, int focusIndex);
	void turnPage(int index,int focusIndex);
	void returnItemBar();
}