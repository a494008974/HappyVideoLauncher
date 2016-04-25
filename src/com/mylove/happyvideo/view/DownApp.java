package com.mylove.happyvideo.view;

import java.io.File;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;

import cn.trinea.android.common.util.FileUtils;

import com.mylove.happyvideo.R;
import com.mylove.happyvideo.bean.Item;
import com.mylove.happyvideo.utils.ApkManage;
import com.mylove.happyvideo.utils.Contanst;

import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DownApp extends DialogFragment {
	Item item;
	private String url;
	
	private ImageView icon;
	private TextView title,detail;
	private ProgressBar bar;
	private Button btn;
	HttpHandler httpHandler;
	private String fileName;
	private enum State {
		NONE, SETUP, SETUPING, RUN
	};
	private State mState = State.SETUP;
	public DownApp(Item item) {
		// TODO Auto-generated constructor stub
		this.item = item;
		if ("1".equals(item.getIslink())) {
			this.url = item.getLink();
		} else {
			this.url = Contanst.downmain + item.getUrl();
		}
		this.setStyle(0, R.style.Transparent);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.down_layout, null);
		
		icon = (ImageView) v.findViewById(R.id.app_icon);
		Contanst.fb.display(icon, Contanst.downmain+item.getIcon());
		title = (TextView) v.findViewById(R.id.app_title);
		title.setText(item.getTitle());
		
		detail = (TextView) v.findViewById(R.id.app_detail);
		bar = (ProgressBar) v.findViewById(R.id.app_down_progressBar);
		btn = (Button) v.findViewById(R.id.app_setup);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub
				if (mState == State.SETUP) {
					mState = State.SETUPING;
					bar.setMax(100);
					btn.setBackgroundResource(R.drawable.bting_selector);
					
					fileName = "tmp.apk";
					
					FinalHttp fh = new FinalHttp();
					AjaxCallBack ajaxCallBack = new AjaxCallBack<File>() {

						@Override
						public void onLoading(long count, long current) {
							// TODO Auto-generated method stub
							super.onLoading(count, current);
							System.out.println(current+"/"+count);
							float num = (float) current / (float) count;
							int result = (int) (num * 100);
							if(result <= 100){
								bar.setProgress(result);
								detail.setText(result + "%");
							}
						}

						@Override
						public void onSuccess(File t) {
							// TODO Auto-generated method stub
							super.onSuccess(t);
							boolean result = false;
							System.out.println("file:"+t.getPath());
							if (FileUtils.isFileExist(t.getPath())) {
								Contanst.postConfirm = item.getPkg();
								
								ApkManage.chmod("777", t.getPath());
								ApkManage.install(getActivity(), t.getPath());
								dismiss();
							}
							if (result) {
								mState = State.RUN;
								btn.setBackgroundResource(R.drawable.yun_selector);
							}
						}
						
					};
					
					ajaxCallBack.progress(true, 10);
					httpHandler = fh.download(url, Contanst.path + fileName, false, ajaxCallBack);
				} else if (mState == State.RUN) {
					dismiss();
					Intent intent = null;
					try {
						String pkg = item.getPkg();
						if (ApkManage.checkInstall(getActivity(), pkg)) {
							intent = getActivity().getPackageManager()
									.getLaunchIntentForPackage(pkg);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							getActivity().startActivity(intent);
						}
					} catch (Exception e) {
						v.setBackgroundResource(R.drawable.bt_selector);
					}
				}

			}
		});
		
		return v;
	}
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		super.onDismiss(dialog);
		if(httpHandler != null){
			System.out.println("httpHandler.stop()");
			httpHandler.stop();
		}
	}
}
