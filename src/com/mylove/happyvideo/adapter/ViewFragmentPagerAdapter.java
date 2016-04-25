package com.mylove.happyvideo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.mylove.happyvideo.fragment.BaseFragment;

/**
 * Created by Administrator on 2015/3/13 0013.
 */
public class ViewFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<? extends Fragment> fragments;
    public ViewFragmentPagerAdapter(FragmentManager fm,List<BaseFragment> fragments2) {
        super(fm);
        this.fragments = fragments2 != null ? fragments2 : new ArrayList<Fragment>();
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
