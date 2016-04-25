package com.mylove.happyvideo.fragment;

import com.mylove.happyvideo.view.FirstFragment;
import com.mylove.happyvideo.view.HomeFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class FragmentFactory {
	public static Fragment getFragmentByTag(FragmentActivity pActivity,
			String pTag) {
		FragmentManager fm = pActivity.getSupportFragmentManager();
		Fragment fragment = fm.findFragmentByTag(pTag);
		if (fragment != null) {
			return fragment;
		} else {
			if (FirstFragment.TAG.equals(pTag)) {
				return new FirstFragment();
			} else if (HomeFragment.TAG.equals(pTag)) {
				return new HomeFragment();
			} else if (FragmentOne.TAG.equals(pTag)) {
				return new FragmentOne();
			} else if (FragmentTwo.TAG.equals(pTag)) {
				return new FragmentTwo();
			} else if (FragmentThree.TAG.equals(pTag)) {
				return new FragmentThree();
			} else if (FragmentFour.TAG.equals(pTag)) {
				return new FragmentFour();
			}else {
				return null;
			}
		}
	}
}
