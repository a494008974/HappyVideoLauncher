package com.mylove.viewpagerindicator;

import com.mylove.happyvideo.R;
import com.mylove.happyvideo.view.MyViewPager;
import com.mylove.happyvideo.view.Size;
import com.mylove.happyvideo.view.TitleAction;
import com.zhy.autolayout.utils.AutoUtils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class PageViewIndicator extends LinearLayout implements
		OnPageChangeListener {
	
	private int count = 0;
	private BaseAdapter adapter;
	private MyViewPager mPager;
	
	private Paint mPaint;
	private int mTriangleWidth;
	private int mInitTranslationX;
	private float mTranslationX;

	private TitleAction mAction;

	public PageViewIndicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.parseColor("#ffff0000"));
		mPaint.setStyle(Style.FILL);
		mPaint.setPathEffect(new CornerPathEffect(3));
		
	}

	public PageViewIndicator(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public PageViewIndicator(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public void setAction(TitleAction mAction) {
		this.mAction = mAction;
	}

	public interface PageChangeListener {
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels);

		public void onPageSelected(int position);

		public void onPageScrollStateChanged(int state);
	}

	// 对外的ViewPager的回调接口
	private PageChangeListener onPageChangeListener;

	// 对外的ViewPager的回调接口的设置
	public void setOnPageChangeListener(PageChangeListener pageChangeListener) {
		this.onPageChangeListener = pageChangeListener;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);

	}

	public int getScreenWidth() {
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}

	public void setAdapter(BaseAdapter adapter) {
		if (adapter != null) {
			setViewWidth(0);
			count = adapter.getCount();
			mTriangleWidth = mWidth / count;
			mInitTranslationX = 0;
			this.adapter = adapter;
			initView();
		}
	}

	public ViewPager getPager() {
		return mPager;
	}

	public void setPager(MyViewPager mPager) {
		this.mPager = mPager;
		mPager.setOnPageChangeListener(this);
		this.mPager.setCurrentItem(0);
		highLightTextView(0);
	}

	public void setCurrentPosition(int position) {
		View v = getChildAt(position);
		v.requestFocus();
		this.mPager.setCurrentItem(position);
		highLightTextView(position);
	}

	private int mWidth;

	public void setViewWidth(int width) {
		if (width == 0) {
			mWidth = getScreenWidth();
		} else {
			mWidth = width;
		}

	}

	protected void scroll(int position, float offset) {
		// TODO Auto-generated method stub
		mTranslationX = getWidth() / count * (position + offset);
		invalidate();
	}

	private void initView() {
		// TODO Auto-generated method stub
		for (int i = 0; i < adapter.getCount(); i++) {
			final int j = i;
			View view = getView(adapter.getView(i, null, this));
			view.setFocusable(true);
			view.setClickable(true);
			view.setFocusableInTouchMode(true);
			view.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					mAction.onTitleMoveFocus(v, hasFocus);
					if (hasFocus) {
						mPager.setCurrentItem(j);
					}
				}
			});
			
			if(i == 0){
				view.setId(0x1000101);
				view.setNextFocusLeftId(0x1000111);
			}else if(i == adapter.getCount() - 1){
				view.setId(0x1000111);
				view.setNextFocusRightId(0x1000101);
			}
			
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mPager.setCurrentItem(j);
				}
			});
			addView(view);
		}
		resetTextViewColor();
	}

	protected void highLightTextView(int position) {
		View view = ((ViewGroup) getChildAt(position)).getChildAt(0);
		if (view instanceof TextView) {

			((TextView) view).setTextColor(Color.rgb(150, 255, 0));
			((TextView) view).setTextSize(getResources().getDimension(
					R.dimen.dimen_36_sp));
		}

	}

	private void resetTextViewColor() {
		for (int i = 0; i < getChildCount(); i++) {
			ViewGroup group = (ViewGroup) getChildAt(i);
			if (group instanceof LinearLayout) {
				View v = group.getChildAt(0);
				if (v instanceof TextView) {
					((TextView) v).setTextColor(getResources().getColor(
							R.color.white));
					((TextView) v).setTextSize(getResources().getDimension(
							R.dimen.dimen_36_sp));
				}
			}

		}
	}

	private View getView(View view) {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.width = mWidth / 2 / adapter.getCount();
		view.setLayoutParams(lp);
		view.setFocusable(true);
		view.setClickable(true);
		view.setFocusableInTouchMode(true);
		AutoUtils.autoSize(view);
		return view;
	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		resetTextViewColor();
		highLightTextView(position);
		if (onPageChangeListener != null) {
			onPageChangeListener.onPageSelected(position);
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// TODO Auto-generated method stub
//		scroll(position, positionOffset);
		if (onPageChangeListener != null) {
			onPageChangeListener.onPageScrolled(position, positionOffset,
					positionOffsetPixels);
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub
		if (onPageChangeListener != null) {
			onPageChangeListener.onPageScrollStateChanged(state);
		}
	}

}
