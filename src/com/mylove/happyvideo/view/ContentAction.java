package com.mylove.happyvideo.view;

import android.view.View;

/**
 * Created by Administrator on 2015/6/2 0002.
 */
public interface ContentAction {
    public void onContentMoveFocus(View v, boolean hasFocus);
    public void onContentClick(View v);
    public boolean onContentLongClick(View v);
    boolean actionMenu(View v);
    public void onDownKeyClick();
    public void onKeyUpClick();
}
