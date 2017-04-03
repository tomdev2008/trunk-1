package com.aibinong.tantan.ui.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.aibinong.tantan.presenter.MineFragPresenter;
import com.aibinong.tantan.ui.fragment.ImgPlazaFragment;
import com.aibinong.tantan.ui.fragment.MineFragment;
import com.aibinong.tantan.ui.fragment.MsgFragment;
import com.aibinong.tantan.ui.fragment.RecommendFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hubin on 2017/3/31.
 */

public class ViewPagerFragmentAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mList = new ArrayList<Fragment>();
    public ViewPagerFragmentAdapter(FragmentManager fm , ArrayList<Fragment> list) {
        super(fm);
        this.mList = list;
    }



    @Override
    public Fragment getItem(int position) {
        Fragment page = null;
        if (mList.size() > position) {
            page = mList.get(position);
            if (page != null) {
                return page;
            }
        }
        while (position>=mList.size()) {
            mList.add(null);
        }
        switch (position%4) {
            case 0:
                page = ImgPlazaFragment.newInstance();
                mList.set(position, page);
                break;
            case 1:
                page = MsgFragment.newInstance();
                mList.set(position, page);
                break;
            case 2:
                page = RecommendFragment.newInstance();
                mList.set(position, page);
                break;
            case 3:
                page = MineFragment.newInstance();
                mList.set(position, page);
                break;
            default:
                break;
        }
        return page;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }
}
