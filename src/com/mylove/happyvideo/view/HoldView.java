package com.mylove.happyvideo.view;

import com.mylove.happyvideo.R;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HoldView extends DialogFragment {
	
	private TextView tv;
	public HoldView() {
		// TODO Auto-generated constructor stub
		this.setCancelable(false);
		this.setStyle(0, R.style.Transparent);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.hold_view, null);
		return v;
	}
}
