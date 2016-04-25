package com.mylove.happyvideo.fragment;

import com.mylove.happyvideo.view.ContentAction;
import com.mylove.happyvideo.view.ContentMainAction;
import com.mylove.happyvideo.view.ContentView;
import com.mylove.happyvideo.view.PageTurn;

import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;

public class BaseFragment extends Fragment implements OnFocusChangeListener,
		OnClickListener,OnLongClickListener {
	private ViewGroup mViewGroup;
	private ContentAction mAction;
	private PageTurn pageTurn;
	private int pageTag;
	private ContentMainAction mainAction;

	public void setMainAction(ContentMainAction mainAction) {
		this.mainAction = mainAction;
	}

	public ContentMainAction getMainAction() {
		return mainAction;
	}


	public int getPageTag() {
		return pageTag;
	}

	public void setPageTag(int pageTag) {
		this.pageTag = pageTag;
	}

	public ViewGroup getViewGroup() {
		return mViewGroup;
	}

	public void setViewGroup(ViewGroup mViewGroup) {
		this.mViewGroup = mViewGroup;
	}

	public ContentAction getFocusMove() {
		return mAction;
	}

	public void setAction(ContentAction mAction) {
		this.mAction = mAction;
	}
	
	public ContentAction getAction() {
		return mAction;
	}

	public PageTurn getPageTurn() {
		return pageTurn;
	}

	public void setPageTurn(PageTurn pageTurn) {
		this.pageTurn = pageTurn;
	}

	public void init(ViewGroup mGroup,int n){
		setViewGroup(mGroup);
		initView(n);
		initListener();
	}

	public void initListener() {
		if (mViewGroup != null) {
			for (int i = 0; i < mViewGroup.getChildCount(); i++) {
				View v = mViewGroup.getChildAt(i);
				v.setOnFocusChangeListener(this);
				v.setOnClickListener(this);
				v.setOnLongClickListener(this);
			}
		}
	}
	
	public void initView(int n) {
		if (mViewGroup != null) {
			for (int i = 0; i < mViewGroup.getChildCount(); i++) {
				ContentView v = (ContentView) mViewGroup.getChildAt(i);
				if((i+1)/10 > 0){
					v.setTag(pageTag+""+(i+1));
				}else{
					v.setTag(pageTag+"0"+(i+1));
				}
				v.initView(n);
			}
		}
	}

	public void refreshView(String tag,int n) {
		if (mViewGroup != null) {
			for (int i = 0; i < mViewGroup.getChildCount(); i++) {
				ContentView v = (ContentView) mViewGroup.getChildAt(i);
				if(tag.equals(v.getTag())){
					v.initView(n);
				}
			}
		}
	}
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if (mAction != null) {
			mAction.onContentMoveFocus(v, hasFocus);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (mAction != null) {
			mAction.onContentClick(v);
		}
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		if (mAction != null) {
			return mAction.onContentLongClick(v);
		}
		return false;
	}
}
