package com.mylove.happyvideo.view;

import java.util.List;

import com.mylove.happyvideo.R;
import com.mylove.happyvideo.bean.Item;
import com.mylove.happyvideo.fragment.BaseFragment;
import com.mylove.happyvideo.utils.Contanst;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.utils.ImageUtils;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/3/13 0013.
 */
public class ContentView extends RelativeLayout {

	Context mContext;
	Item item;

	private ImageView imageView;
	private TextView textView,tv;
	private ImageView icon;
	private ImageView focusView;

	public ContentView(Context context) {
		this(context, null);
	}

	public ContentView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ContentView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		this.mContext = context;
		this.setGravity(Gravity.CENTER);
		this.setBackgroundResource(R.drawable.contentview);
		
		RelativeLayout.LayoutParams p1 = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		imageView = new ImageView(context);
		imageView.setScaleType(ScaleType.FIT_XY);
		addView(imageView, p1);

		int size = (int) context.getResources().getDimension(
				R.dimen.dimen_80_dip);
		RelativeLayout.LayoutParams p2 = new RelativeLayout.LayoutParams(size,
				size);
		p2.addRule(RelativeLayout.CENTER_HORIZONTAL);
		p2.setMargins(0, 20, 0, 0);
		icon = new ImageView(context);
		icon.setScaleType(ScaleType.FIT_CENTER);
		addView(icon, p2);

		RelativeLayout.LayoutParams p3 = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		p3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		
		tv = new TextView(context);
		tv.setGravity(Gravity.CENTER);
		tv.setTextColor(Color.WHITE);
		tv.setPadding(0, 8, 0, 8);
		tv.setSingleLine(true);
		tv.setTextSize(context.getResources().getDimension(
				R.dimen.dimen_18_dip));
		addView(tv, p3);
		
		textView = new TextView(context);
		textView.setGravity(Gravity.CENTER);
		textView.setTextColor(Color.WHITE);
		textView.setPadding(0, 8, 0, 8);
		textView.setSingleLine(true);
		textView.setAlpha(0);
		textView.setTextSize(context.getResources().getDimension(
				R.dimen.dimen_18_dip));
		textView.setBackgroundColor(Color.argb(255, 1, 90, 252));
		addView(textView, p3);

		focusView = new ImageView(context);
		focusView.setScaleType(ScaleType.FIT_XY);
		addView(focusView, p1);
	}

	public void initView(int n) {
		String tag = (String) getTag();
		if(tag == null){
			tag = "";
		}
		
		List<Item> current = Contanst.fd.findAllByWhere(Item.class, "tag="
				+ "'" + tag.trim() + "'");
		if (current != null && current.size() > 0) {
			item = current.get(0);
		}

		if (item != null) {
			tv.setVisibility(INVISIBLE);
			PackageManager pm = mContext.getPackageManager();
			PackageInfo pkgInfo = null;
			try {
				pkgInfo = pm.getPackageInfo(item.getPkg(),
						PackageManager.GET_PERMISSIONS);
			} catch (NameNotFoundException e) {
			}

			if ("1".equals(item.getIslock().trim())) {
				// 网络获取
				if (!"".equals(item.getImgurl().trim()) 
					&& item.getImgurl() != null
					&& "1".equals(item.getIshold().trim())) {
					
					icon.setImageBitmap(null);
					
					if(n == Contanst.FIRST){
//						Contanst.fb.display(imageView,item.getImgurl().trim());
						try {
							int k = Integer.parseInt(item.getImgurl().trim());
							imageView.setBackgroundResource(Contanst.vooleChannel[k]);
						} catch (Exception e) {
							// TODO: handle exception
						}
					}else{
						Contanst.fb.display(imageView,Contanst.downmain + item.getImgurl().trim());
					}
					
					
				} else {
					imageView.setImageBitmap(null);
					if (item.getIcon() != null) {
						Contanst.fb.display(icon,Contanst.downmain + item.getIcon().trim());
					} else {
						icon.setImageResource(R.drawable.add_common_icon);
					}
				}
				if (item.getTitle() != null) {
					textView.setText(item.getTitle());
					if("".equals(item.getTitle().trim())){
						textView.setVisibility(INVISIBLE);
					}
				}
				
			} else {
				imageView.setImageBitmap(null);
				if (pkgInfo != null) {
					icon.setImageDrawable(pm.getApplicationIcon(pkgInfo.applicationInfo));
					tv.setText(pm
							.getApplicationLabel(pkgInfo.applicationInfo));
					textView.setText(pm
							.getApplicationLabel(pkgInfo.applicationInfo));
				}else{
					icon.setImageResource(R.drawable.add_common_icon);
					tv.setText(getResources().getString(
							R.string.add));
					textView.setText(getResources().getString(
							R.string.add));
				}
				tv.setVisibility(VISIBLE);
				textView.setVisibility(VISIBLE);
			}
		}else{
			icon.setImageResource(R.drawable.add_common_icon);
			textView.setText(getResources().getString(
					R.string.add));
			textView.setVisibility(VISIBLE);
			tv.setText(getResources().getString(
					R.string.add));
			tv.setVisibility(VISIBLE);
		}
		
		switch (n) {
		case Contanst.FIRST:
			if("809".equals(tag.trim())){
				imageView.setBackgroundResource(R.drawable.voole_9);
				icon.setImageBitmap(null);
				tv.setVisibility(INVISIBLE);
				textView.setText(getResources().getString(
						R.string.more));
				textView.setVisibility(VISIBLE);
			}
			break;
		case Contanst.SECOND:
			if("411".equals(tag.trim())){
				imageView.setBackgroundResource(R.drawable.voole_9);
				icon.setImageBitmap(null);
				tv.setVisibility(INVISIBLE);
				textView.setText(getResources().getString(
						R.string.more));
				textView.setVisibility(VISIBLE);
			}
			break;
		}
		imageView.setVisibility(VISIBLE);
		icon.setVisibility(VISIBLE);
	}

	public void focusDoAnim() {
		
	}

	public void unFocusDoAnim() {
		
	}
}
