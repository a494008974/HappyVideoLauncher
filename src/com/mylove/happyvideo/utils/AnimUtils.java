package com.mylove.happyvideo.utils;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import com.mylove.happyvideo.R;
import com.mylove.happyvideo.view.Size;
import com.mylove.happyvideo.view.SizeEvaluator;

public class AnimUtils {

	public static void request(final View focusView) {
		final ViewTreeObserver vto = focusView.getViewTreeObserver();
		vto.addOnDrawListener(new ViewTreeObserver.OnDrawListener() {

			@Override
			public void onDraw() {
				if (focusView.getWidth() != 0) {
					focusView.requestFocus();
					vto.removeOnDrawListener(this);
				}
			}
		});
	}

	public static boolean openApk(Context context, String pkg) {
		try {
			PackageManager pm = context.getPackageManager();
			Intent intent = pm.getLaunchIntentForPackage(pkg);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
	}

	public static void getFocus(final View v) {
		v.bringToFront();
		
		ViewGroup viewGroup = (ViewGroup) v;
		final View focusTitle = viewGroup.getChildAt(3);
		focusTitle.setAlpha(0);
		focusTitle.setVisibility(View.VISIBLE);
		
		ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, View.SCALE_X, 1F,
				1.03F);
		ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, View.SCALE_Y, 1F,
				1.03F);
		AnimatorSet scale = new AnimatorSet();
		scale.setDuration(200);
		scale.setInterpolator(new AccelerateInterpolator());
		scale.playTogether(scaleX, scaleY);
		scale.start();

		scale.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				super.onAnimationEnd(animation);
				if (focusTitle != null) {
					ObjectAnimator titleY = ObjectAnimator.ofFloat(focusTitle,
							View.Y, focusTitle.getY()+focusTitle.getHeight(), focusTitle.getY());
					 ObjectAnimator titleA = ObjectAnimator
					 .ofFloat(focusTitle, View.ALPHA, 0F,1F);
					 AnimatorSet tran = new AnimatorSet();
					 tran.setDuration(300);
					 tran.setInterpolator(new AccelerateInterpolator());
					 tran.playTogether(titleY, titleA);
					 tran.start();
				}
			}

			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				super.onAnimationStart(animation);
			}

		});
	}

	public static void cancleFocus(final View v) {

		ViewGroup viewGroup = (ViewGroup) v;
		
		final View focusTitle = viewGroup.getChildAt(3);		
		
		ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, View.SCALE_X, 1.03F,
				1F);
		ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, View.SCALE_Y, 1.03F,
				1F);
		AnimatorSet scale = new AnimatorSet();
		scale.setDuration(200);
		scale.setInterpolator(new AccelerateInterpolator());
		scale.playTogether(scaleX, scaleY);
		scale.start();
		scale.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				super.onAnimationEnd(animation);
				if (focusTitle != null) {
					focusTitle.setVisibility(View.INVISIBLE);
					v.postInvalidate();
				}
			}

			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				super.onAnimationStart(animation);
				
			}

		});
	}

	public static void moveFocus(View v, final View iv) {
		// TODO Auto-generated method stub
		int scale_x = (int) (v.getWidth() * 0.03);
		int scale_y = (int) (v.getHeight() * 0.03);
		int v_width = v.getWidth() + scale_x;
		int v_height = v.getHeight() + scale_y;

		ViewGroup.LayoutParams params = iv.getLayoutParams();
		params.width = iv.getWidth();
		params.height = iv.getHeight();
		iv.setLayoutParams(params);
		float x = v.getX() - scale_x / 2 - 2;
		float y = v.getTop() - scale_y / 2;

		System.out.println("ZHOU =====>  width:" + v_width + " height:"
				+ v_height + " x:" + x + " y:" + y);
		if (R.id.first_1 == v.getId() || R.id.one_1 == v.getId()) {
			v_width = 442;
			v_height = 298;
			x = 82;
			y = 46;
		} else if (v.getWidth() == 0 || v.getHeight() == 0) {
			iv.setVisibility(View.INVISIBLE);
			return;
		}

		Size s1 = new Size(iv.getWidth(), iv.getHeight());
		Size s2 = new Size(v_width + 37, v_height + 37);
		ValueAnimator animWH = ValueAnimator.ofObject(new SizeEvaluator(iv),
				s1, s2);

		ObjectAnimator animX = ObjectAnimator.ofFloat(iv, View.X, iv.getX(),
				x - 17);
		ObjectAnimator animY = ObjectAnimator.ofFloat(iv, View.Y, iv.getY(),
				y - 17);
		ObjectAnimator animA = ObjectAnimator.ofFloat(iv, "alpha", 0.8f, 1f);
		AnimatorSet animSetXY = new AnimatorSet();
		animSetXY.setDuration(300);
		animSetXY.setInterpolator(new AccelerateInterpolator());
		animSetXY.playTogether(animWH, animA, animX, animY);
		animSetXY.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				super.onAnimationEnd(animation);
				// if(!visable){
				// iv.setVisibility(View.VISIBLE);
				// visable = true;
				// }
			}

			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				super.onAnimationStart(animation);
				// if(visable){
				// iv.setVisibility(View.VISIBLE);
				// }
			}

		});
		animSetXY.start();
	}
}
