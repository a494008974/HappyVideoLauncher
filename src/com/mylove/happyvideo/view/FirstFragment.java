package com.mylove.happyvideo.view;

import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.List;

import com.mylove.happyvideo.R;
import com.mylove.happyvideo.base.BaseActivity;
import com.mylove.happyvideo.bean.Item;
import com.mylove.happyvideo.fragment.BaseFragment;
import com.mylove.happyvideo.utils.AnimUtils;
import com.mylove.happyvideo.utils.Contanst;
import com.mylove.happyvideo.utils.SystemUtils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FirstFragment extends BaseFragment implements ContentAction,
		IViewUpdate, OnKeyListener {
	
	public static String TAG = "FirstFragment";
	
	private ImageView img_content_focus;
	private ImageView netStatu,setStatu;
	private LinearLayout net,set;
	
	private TextView timeTextView, statuTextView;
	
	private View view,currentView,statuView;
	private ViewGroup mGroup;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.first_main, container, false);

		img_content_focus = (ImageView) view.findViewById(R.id.content_focus);
		set = (LinearLayout) view.findViewById(R.id.set);
		net = (LinearLayout) view.findViewById(R.id.net);
		
		statuView = view.findViewById(R.id.status_back);
		netStatu = (ImageView) view.findViewById(R.id.net_statu);
		timeTextView = (TextView) view.findViewById(R.id.time);
		statuTextView = (TextView) view.findViewById(R.id.time_statu);
		setStatu = (ImageView) view.findViewById(R.id.set_statu);
		
		timeTextView.setText(SystemUtils.getTime(getActivity()));
		if (!DateFormat.is24HourFormat(getActivity())) {
			statuTextView.setText(SystemUtils.getStatu());
		} else {
			statuTextView.setVisibility(View.INVISIBLE);
		}
		
		set.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus){
					statuView.setVisibility(View.VISIBLE);
					set.getChildAt(1).setVisibility(View.VISIBLE);
				}else{
					set.getChildAt(1).setVisibility(View.GONE);
					statuView.setVisibility(View.INVISIBLE);
				}
			}
		});
        
        net.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus){
					statuView.setVisibility(View.VISIBLE);
					net.getChildAt(1).setVisibility(View.VISIBLE);
				}else{
					net.getChildAt(1).setVisibility(View.GONE);
					statuView.setVisibility(View.INVISIBLE);
				}
			}
		});
        set.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_DPAD_DOWN:
						if(currentView != null){
							currentView.requestFocus();
						}
						return true;
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
						break;
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
					switch (keyCode) {
					case KeyEvent.KEYCODE_DPAD_DOWN:
						if(currentView != null){
							currentView.requestFocus();
						}
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
						break;
					}
				}
				return false;
			}
		});
		
		mGroup = (ViewGroup) view.findViewById(R.id.first_content);
		init(mGroup,Contanst.FIRST);

		if (mGroup != null) {
			for (int i = 0; i < mGroup.getChildCount(); i++) {
				View v = mGroup.getChildAt(i);
				v.setOnKeyListener(this);
				
			}
			mGroup.getChildAt(0).requestFocus();
		}
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	
	@Override
	public void onContentMoveFocus(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		((IFragmentAction) getActivity()).onFragmentMoveFocus(v,
				img_content_focus, hasFocus);
		
		if(hasFocus){
			currentView = v;
		}
		
	}

	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	@Override
	public boolean onContentLongClick(View v) {
		// TODO Auto-generated method stub
		return ((IFragmentAction) getActivity()).fragmentLongClick(v,Contanst.FIRST);
	}

	@Override
	public void onContentClick(View v) {
		((IFragmentAction) getActivity()).fragmentClick(v, this,Contanst.FIRST);
	}

	@Override
	public void UpdateViewByTag(String tag) {
		refreshView(tag,Contanst.FIRST);
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
	public boolean actionMenu(View v) {
		// TODO Auto-generated method stub
		return ((IFragmentAction) getActivity()).onFragmentActionMenu(v,this,Contanst.FIRST);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		int tag = Integer.parseInt((String) v.getTag());
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			System.out.println("KEYCODE_DPAD_DOWN FIRST : " + System.currentTimeMillis());
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_DOWN:
				switch (tag) {
				case 805:
				case 806:
				case 807:
				case 808:
				case 809:
					if(getAction() != null){
						getAction().onDownKeyClick();
					}
					return true;
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
	public static boolean flag = true;
	@Override
	public void onDownKeyClick() {
		// TODO Auto-generated method stub
		if(flag){
			if(!FavoriteApp.getInstance().isVisible()){
				FavoriteApp.getInstance().show(getFragmentManager(), "FAVORITEAPP");
			}
			flag = false;
		}
	}

	@Override
	public void onKeyUpClick() {
		// TODO Auto-generated method stub
		flag = true;
	}
}
