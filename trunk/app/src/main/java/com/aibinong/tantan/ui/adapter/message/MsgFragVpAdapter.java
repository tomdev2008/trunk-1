package com.aibinong.tantan.ui.adapter.message;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/10/26.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|


import android.app.Fragment;
import android.app.FragmentManager;

import com.aibinong.tantan.ui.fragment.message.PMListFragment;
import com.aibinong.tantan.ui.fragment.message.FollowListFragment;

public class MsgFragVpAdapter extends android.support.v13.app.FragmentStatePagerAdapter {
    private Fragment mPairListFragment, mPMLFragment;

    public MsgFragVpAdapter(FragmentManager fm) {
        super(fm);
        mPairListFragment = FollowListFragment.newInstance();
        mPMLFragment = PMListFragment.newInstance();
    }

    @Override
    public Fragment getItem(int position) {
        return position == 0 ? mPMLFragment : mPairListFragment;
    }


    @Override
    public int getCount() {
        return 2;
    }
}
