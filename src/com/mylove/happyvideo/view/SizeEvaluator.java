package com.mylove.happyvideo.view;

import android.animation.TypeEvaluator;
import android.view.View;
import android.view.ViewGroup;

public class SizeEvaluator implements TypeEvaluator<Size> {
	View iv;
	public SizeEvaluator(View iv) {
		// TODO Auto-generated constructor stub
		this.iv = iv;
	}
	@Override
	public Size evaluate(float fraction, Size startValue, Size endValue) {
		Size startSize = (Size) startValue;
		Size endSize = (Size) endValue;
		float width = startSize.getWidth() + fraction
				* (endSize.getWidth() - startSize.getWidth());
		float height = startSize.getHeight() + fraction
				* (endSize.getHeight() - startSize.getHeight());
		ViewGroup.LayoutParams params = iv.getLayoutParams();
		params.width = (int) width;
		params.height = (int) height;
		iv.setLayoutParams(params);
		Size size = new Size((int) width, (int) height);
		return size;
	}
}