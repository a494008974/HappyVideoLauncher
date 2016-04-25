package com.mylove.happyvideo.view;

import java.util.HashMap;
import java.util.Map;

import com.mylove.happyvideo.R;






import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MyHorizontalScrollView extends HorizontalScrollView implements
		View.OnClickListener, View.OnFocusChangeListener,View.OnKeyListener {
	public final static int SCROLL_DEFAULT = 100;
	ImageView focus;
	private Context mContext;
	private View view;
	private LinearLayout linear;
	private int currentPosition = 0;

	private Map<View, Integer> mViewPos = new HashMap<View, Integer>();
	
	private BaseAdapter mAdapter;
	
	public BaseAdapter getAdapter() {
		return mAdapter;
	}

	public void setAdapter(BaseAdapter mAdapter) {
		this.mAdapter = mAdapter;
		initView();
	}

	@Override
	protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
		// TODO Auto-generated method stub
		rect.left = -SCROLL_DEFAULT + rect.left;
		rect.right = SCROLL_DEFAULT + rect.right;
		return super.computeScrollDeltaToGetChildRectOnScreen(rect);
	}
	
	public interface OnItemClickListener {
		void onItemClick(BaseAdapter parent, View view, int position);
	}
	public interface OnItemKeyListener{
		public boolean onKey(View v, int keyCode, KeyEvent event, int position);
	}
	OnItemKeyListener itemKeyListener;
	OnItemClickListener itemClickListener;

	
	public void setOnItemKeyListener(OnItemKeyListener itemKeyListener) {
		this.itemKeyListener = itemKeyListener;
	}

	public void setOnItemClickListener(OnItemClickListener itemClickListener) {
		this.itemClickListener = itemClickListener;
	}

	public MyHorizontalScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		
		view = LayoutInflater.from(context).inflate(R.layout.scroll_horizontal,
				this);
		linear = (LinearLayout) view.findViewById(R.id.scroll_linear);
		focus = (ImageView) view.findViewById(R.id.focus_img);
	}

	public MyHorizontalScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public MyHorizontalScrollView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	
	public void initView() {
		linear.removeAllViews();
		for (int i = 0; i < mAdapter.getCount(); i++) {
			View v = mAdapter.getView(i, null, linear);
			v.setFocusable(true);
			v.setFocusableInTouchMode(true);
			v.setClickable(true);
			v.setOnKeyListener(this);
			v.setOnClickListener(this);
			v.setOnFocusChangeListener(this);
			linear.addView(v);
			mViewPos.put(v, i);
		}
	}
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(itemKeyListener != null){
			return itemKeyListener.onKey(v, keyCode, event, mViewPos.get(v));
		}
		return false;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(itemClickListener != null){
			itemClickListener.onItemClick(mAdapter, v, mViewPos.get(v));
		}
	}

	public void moveFocusView(int index){
		if(linear != null){
			View v = linear.getChildAt(index);
			if(v != null){
				request(v);
			}
		}
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if (hasFocus) {
			focus.setVisibility(View.VISIBLE);
			moveFocus(v);
		} else {
			focus.setVisibility(View.INVISIBLE);
		}
	}

	private void moveFocus(View v) {
		// TODO Auto-generated method stub
		int v_width = v.getWidth();
		int v_height = v.getHeight();
		ViewGroup.LayoutParams params = focus.getLayoutParams();
		params.width = v_width + 32;
		params.height = v_height + 32;
		focus.setLayoutParams(params);
		float x = v.getX();
		float y = v.getTop();
		
		ObjectAnimator animX = ObjectAnimator.ofFloat(focus, View.X, focus.getX(),
				x - 15);
		ObjectAnimator animY = ObjectAnimator.ofFloat(focus, View.Y, focus.getY(),
				y - 15);
		ObjectAnimator animA = ObjectAnimator.ofFloat(focus, View.ALPHA, 0.5f, 1f);
		AnimatorSet animSetXY = new AnimatorSet();
		animSetXY.setDuration(300);
		animSetXY.setInterpolator(new AccelerateDecelerateInterpolator());
		animSetXY.playTogether(animA, animX, animY);
		animSetXY.start();
	}
	
	public void request(final View focusView){
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
}
