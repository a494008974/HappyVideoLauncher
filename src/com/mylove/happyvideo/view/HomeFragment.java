package com.mylove.happyvideo.view;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import com.mylove.happyvideo.MainActivity;
import com.mylove.happyvideo.R;
import com.mylove.happyvideo.adapter.TitleAdapter;
import com.mylove.happyvideo.adapter.ViewFragmentPagerAdapter;
import com.mylove.happyvideo.fragment.BaseFragment;
import com.mylove.happyvideo.fragment.FragmentFactory;
import com.mylove.happyvideo.fragment.FragmentFour;
import com.mylove.happyvideo.fragment.FragmentOne;
import com.mylove.happyvideo.fragment.FragmentThree;
import com.mylove.happyvideo.fragment.FragmentTwo;
import com.mylove.happyvideo.utils.AnimUtils;
import com.mylove.happyvideo.utils.Contanst;
import com.mylove.happyvideo.utils.SystemUtils;
import com.mylove.viewpagerindicator.PageViewIndicator;
import com.mylove.viewpagerindicator.PageViewIndicator.PageChangeListener;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeFragment extends Fragment implements TitleAction,ContentAction,PageTurn,PageChangeListener,IViewUpdate{
	
	public static String TAG = "HomeFragment";
	private MyViewPager mViewPager;
	
	private ViewFragmentPagerAdapter mFragmentPagerAdapter;
	private PageViewIndicator mPageIndicator;
	private List<BaseFragment> fragments;
	private TitleAdapter mTitleAdapter;
	
	private ImageView img_title_focus;
//	private ImageView img_content_focus;
	private boolean visable = true;
	
	private ImageView netStatu,setStatu;
	private LinearLayout net,set;
	private TextView timeTextView,statuTextView;
	private Handler mHandler = new Handler();
	
	private View statuView;
	
	PackageManager pm;
	
	public static boolean flag = true;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.home_main, container, false);  
		
		mViewPager = (MyViewPager) view.findViewById(R.id.viewpager);
		mPageIndicator = (PageViewIndicator) view.findViewById(R.id.indicator);
		img_title_focus = (ImageView) view.findViewById(R.id.title_focus);
//		img_content_focus = (ImageView) view.findViewById(R.id.content_focus);
		
		set = (LinearLayout) view.findViewById(R.id.set);
		net = (LinearLayout) view.findViewById(R.id.net);
		statuView = view.findViewById(R.id.status_back);
		
		netStatu = (ImageView) view.findViewById(R.id.net_statu);
		timeTextView = (TextView) view.findViewById(R.id.time);
		statuTextView = (TextView) view.findViewById(R.id.time_statu);
		setStatu = (ImageView) view.findViewById(R.id.set_statu);
		
		set.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus){
//					img_content_focus.setVisibility(View.GONE);
					img_title_focus.setVisibility(View.GONE);
					statuView.setVisibility(View.VISIBLE);
					set.getChildAt(1).setVisibility(View.VISIBLE);
				}else{
					set.getChildAt(1).setVisibility(View.GONE);
				}
			}
		});
        
        net.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus){
//					img_content_focus.setVisibility(View.GONE);
					img_title_focus.setVisibility(View.GONE);
					statuView.setVisibility(View.VISIBLE);
					net.getChildAt(1).setVisibility(View.VISIBLE);
				}else{
					net.getChildAt(1).setVisibility(View.GONE);
				}
			}
		});
        set.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					System.out.println("keyCode ==> "+keyCode+"HomeFragment");
					switch (keyCode) {
					case KeyEvent.KEYCODE_DPAD_LEFT:
						return true;
					case KeyEvent.KEYCODE_DPAD_CENTER:
					case KeyEvent.KEYCODE_ENTER:
						try {
							Intent intent = new Intent();
							intent.setClassName("com.mylove.settting","com.mylove.settting.MainActivity");
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							getActivity().startActivity(intent);
//							overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
							return true;
						} catch (Exception e) {
							// TODO Auto-generated catch block
						}
						AnimUtils.openApk(getActivity(),"com.android.settings");
						return true;
					case KeyEvent.KEYCODE_DPAD_DOWN:
						System.out.println("setStatu KEYCODE_DPAD_DOWN");
						returnItemBar();
						return true;
					}
				}
				return false;
			}
		});
		
		net.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					System.out.println("keyCode ==> "+keyCode+"HomeFragment");
					switch (keyCode) {
					case KeyEvent.KEYCODE_DPAD_RIGHT:
						return true;
					case KeyEvent.KEYCODE_DPAD_CENTER:
					case KeyEvent.KEYCODE_ENTER:
						if(netFlag == 0){
							try {
								Intent intent = new Intent();
								intent.setClassName("com.mylove.settting","com.mylove.settting.ui.WifiSettingActivity");
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								getActivity().startActivity(intent);
//								overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
								return true;
							} catch (Exception e) {
								// TODO Auto-generated catch block
							}
						}else{
							try {
								Intent intent = new Intent();
								intent.setClassName("com.mylove.settting","com.mylove.settting.ui.EthernetActivity");
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								getActivity().startActivity(intent);
//								overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
								return true;
							} catch (Exception e) {
								// TODO Auto-generated catch block
							}
						}
						AnimUtils.openApk(getActivity(),"com.android.settings");
					case KeyEvent.KEYCODE_DPAD_DOWN:
						System.out.println("setStatu KEYCODE_DPAD_DOWN");
						returnItemBar();
						return true;
					}
				}
				return false;
			}
		});
		
		timeTextView.setText(SystemUtils.getTime(getActivity()));
		if (!DateFormat.is24HourFormat(getActivity())) {
			statuTextView.setText(SystemUtils.getStatu());
		} else {
			statuTextView.setVisibility(View.INVISIBLE);
		}
		
		initView();
		
		if(mViewPager != null){
			mViewPager.requestFocus();
		}
		
		return view;
	}


	private void initView() {
		// TODO Auto-generated method stub
		initFragments();
		
//		try {
//			Field mScroller;
//			mScroller = ViewPager.class.getDeclaredField("mScroller");
//			mScroller.setAccessible(true);
//			Interpolator sInterpolator = new AccelerateDecelerateInterpolator();
//			FixedScroller scroller = new FixedScroller(mViewPager.getContext(),
//					sInterpolator);
//			mScroller.set(mViewPager, scroller);
//		} catch (Exception e) {
//		}
		
		mViewPager.setOffscreenPageLimit(3);
		mFragmentPagerAdapter = new ViewFragmentPagerAdapter(getChildFragmentManager(), fragments);
		mViewPager.setAdapter(mFragmentPagerAdapter);
		
		List<String> titleDatas;
//		List<Title> titles = Contanst.fd.findAll(Title.class,"screen");
//		if(titles!=null && titles.size() >= 3){
//			titleDatas = new ArrayList<String>();
//			for(int i=0; i<3; i++){
//				titleDatas.add(titles.get(i).getTitle());
//			}
//		}else{
//			titleDatas = Arrays.asList(Contanst.titles);
//		}
		
		titleDatas = Arrays.asList(Contanst.titles);
		
		mTitleAdapter = new TitleAdapter(getActivity(), titleDatas, R.layout.title_item);
		mPageIndicator.setAction(this);
		
		mPageIndicator.setAdapter(mTitleAdapter);
		mPageIndicator.setOnPageChangeListener(this);
		mPageIndicator.setPager(mViewPager);
		
	}
	
	private void initFragments(){
		
		int count = getFragmentManager().getBackStackEntryCount();
		Log.e("tag", String.valueOf(count));
		
		BaseFragment[] baseFragments = new BaseFragment[4];
		baseFragments[0] = (BaseFragment) FragmentFactory.getFragmentByTag(getActivity(), FragmentOne.TAG);
		baseFragments[1] = (BaseFragment) FragmentFactory.getFragmentByTag(getActivity(), FragmentTwo.TAG);
		baseFragments[2] = (BaseFragment) FragmentFactory.getFragmentByTag(getActivity(), FragmentThree.TAG);
		baseFragments[3] = (BaseFragment) FragmentFactory.getFragmentByTag(getActivity(), FragmentFour.TAG);
		
		for (int i = 0; i < baseFragments.length; i++) {
			baseFragments[i].setAction(this);
			baseFragments[i].setPageTurn(this);
			baseFragments[i].setPageTag(i+1);
		}
		fragments = Arrays.asList(baseFragments);
	}
	@Override
	public void onTitleClick(View v) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onTitleMoveFocus(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		
		if(hasFocus){
//			img_content_focus.setVisibility(View.GONE);
			statuView.setVisibility(View.INVISIBLE);
			moveFocus(v, img_title_focus);
		}else{
			img_title_focus.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onContentMoveFocus(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		((IFragmentAction) getActivity()).onFragmentMoveFocus(v,
				null, hasFocus);
		
		if(hasFocus){
			mHandler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					set.setFocusable(false);
					set.setFocusableInTouchMode(false);
					set.setClickable(false);
					
					net.setFocusable(false);
					net.setFocusableInTouchMode(false);
					net.setClickable(false);
				}
			});
		}
	}

	public void getFocus(final View v) {
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

	public void cancleFocus(final View v) {

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
	
	public void moveFocus(View v,final View iv) {
		// TODO Auto-generated method stub
		int scale_x = (int)(v.getWidth()*0.03);
		int scale_y = (int)(v.getHeight()*0.03);
		int v_width = v.getWidth()+scale_x;
		int v_height = v.getHeight()+scale_y;
		
		ViewGroup.LayoutParams params = iv.getLayoutParams();
		params.width = iv.getWidth();
		params.height = iv.getHeight();
		iv.setLayoutParams(params);
		float x = v.getX() - scale_x/2 - 2;
		float y = v.getTop() - scale_y/2;
		
		if (R.id.one_1 == v.getId()) {
			v_width = 442;
			v_height = 298;
			x = 82;
			y = 46;
		} else if (v.getWidth() == 0 || v.getHeight() == 0) {
			iv.setVisibility(View.INVISIBLE);
			return;
		}
		
		Size s1 = new Size(iv.getWidth(), iv.getHeight());
		Size s2 = new Size(v_width + 37 , v_height +37);
		ValueAnimator animWH = ValueAnimator.ofObject(new SizeEvaluator(iv), s1,
				s2);

		ObjectAnimator animX = ObjectAnimator
				.ofFloat(iv, "x", iv.getX(), x - 17);
		ObjectAnimator animY = ObjectAnimator
				.ofFloat(iv, "y", iv.getY(), y - 17);
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
				if(!visable){
					iv.setVisibility(View.VISIBLE);
					visable = true;
				}
			}

			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				super.onAnimationStart(animation);
				if(visable){
					iv.setVisibility(View.VISIBLE);
				}
			}
			
		});
		animSetXY.start();
	}
	
	@Override
	public boolean onContentLongClick(View v) {
		// TODO Auto-generated method stub
		return ((IFragmentAction)getActivity()).fragmentLongClick(v,Contanst.SECOND);
	}
	
	@Override
	public void onContentClick(View v) {
		// TODO Auto-generated method stub
		((IFragmentAction) getActivity()).fragmentClick(v, this,Contanst.SECOND);
	}

	@Override
	public boolean actionMenu(View v) {
		// TODO Auto-generated method stub
		return ((IFragmentAction)getActivity()).onFragmentActionMenu(v, this,Contanst.SECOND);
	}
	
	@Override
	public void turnPage(boolean next, int focusIndex) {
		// TODO Auto-generated method stub
		View v = mViewPager.findViewById(focusIndex);
		if(v != null){
			v.requestFocus();
		}
	}
	public void turnPage(int index,int focusIndex) {
		// TODO Auto-generated method stub
		mPageIndicator.setCurrentPosition(index);
		View v = mViewPager.findViewById(focusIndex);
		if(v != null){
			v.requestFocus();
			System.out.println("View id ==>"+v.getId());
		}else{
			System.out.println("View is null ......................");
		}
	}

	@Override
	public void returnItemBar() {
		// TODO Auto-generated method stub
		visable = false;
		mPageIndicator.setCurrentPosition(mViewPager.getCurrentItem());

		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				set.setFocusable(true);
				set.setFocusableInTouchMode(true);
				set.setClickable(true);
				
				net.setFocusable(true);
				net.setFocusableInTouchMode(true);
				net.setClickable(true);
			}
		});

	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub
		
	}
	
	public void UpdateViewByTag(String tag){
		int location = Integer.parseInt(tag.substring(0, 1)) - 1;
		if (fragments != null && fragments.size() > location) {
			fragments.get(location).refreshView(tag,Contanst.SECOND);
		}
	}
	

	@Override
	public void TimeUpdate(String time) {
		// TODO Auto-generated method stub
		timeTextView.setText(time);
	}
	@Override
	public void StatuUpdate(String statu) {
		// TODO Auto-generated method stub
		statuTextView.setText(statu);
	}
	private int netFlag;
	@Override
	public void netStatuUpdate(int statu) {
		// TODO Auto-generated method stub
		switch (statu) {
		case ConnectivityManager.TYPE_WIFI:
			netStatu.setImageResource(R.drawable.wlan);
			netFlag = 0;
			break;
		case ConnectivityManager.TYPE_ETHERNET:
			netStatu.setImageResource(R.drawable.eth);
			netFlag = 1;
			break;
		default:
			netStatu.setImageResource(R.drawable.un_eth);
			break;
		}
	}

	
	@Override
	public void onDownKeyClick() {
		// TODO Auto-generated method stub
		if(flag){
			if(!FavoriteApp.getInstance().isVisible()){
				FavoriteApp.getInstance().show(getFragmentManager(), "FAVORITEAPP");
			}
			flag = false;
		}
		
		System.out.println("home ....................... down click .....................");
	}


	@Override
	public void onKeyUpClick() {
		// TODO Auto-generated method stub
		flag = true;
	}


	
}
