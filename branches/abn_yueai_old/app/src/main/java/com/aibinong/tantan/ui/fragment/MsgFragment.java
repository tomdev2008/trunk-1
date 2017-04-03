package com.aibinong.tantan.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.aibinong.tantan.R;
import com.aibinong.tantan.ui.adapter.message.MsgFragVpAdapter;
import com.fatalsignal.util.DeviceUtils;

import butterknife.Bind;
import butterknife.ButterKnife;


// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/10/18.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

public class MsgFragment extends Fragment implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {
    private static String INTENT_BUNDLE_KEY_MSG_CURRENT_TAB = "INTENT_BUNDLE_KEY_MSG_CURRENT_TAB";
    @Bind(R.id.rbtn_fragment_msg_pm)
    RadioButton mRbtnFragmentMsgPm;
    @Bind(R.id.rbtn_fragment_msg_pair)
    RadioButton mRbtnFragmentMsgPair;
    @Bind(R.id.radiogroup_fragment_msg_list)
    RadioGroup mRadiogroupFragmentMsgList;
    @Bind(R.id.viewpager_msg_content)
    ViewPager mViewpagerMsgContent;

    private View mContentView;
    private MsgFragVpAdapter mMsgFragVpAdapter;
    private int mCurrentTab;

    public static MsgFragment newInstance() {
        MsgFragment fragment = new MsgFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMsgFragVpAdapter = new MsgFragVpAdapter(getFragmentManager());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_msg, container, false);
        ButterKnife.bind(this, mContentView);
        mCurrentTab = savedInstanceState == null ? 0 : savedInstanceState.getInt(INTENT_BUNDLE_KEY_MSG_CURRENT_TAB);
        setUpAll(savedInstanceState);
        return mContentView;
    }

    private void setUpAll(Bundle savedInstanceState) {
        mRadiogroupFragmentMsgList.setOnCheckedChangeListener(this);

        /*中间列表*/
        mViewpagerMsgContent.setAdapter(mMsgFragVpAdapter);
        mViewpagerMsgContent.addOnPageChangeListener(this);


        mRbtnFragmentMsgPm.setChecked(true);

        LinearLayout.LayoutParams llp = (LinearLayout.LayoutParams) mRadiogroupFragmentMsgList.getLayoutParams();
        llp.topMargin+= DeviceUtils.getStatusBarHeight(getActivity());
        mRadiogroupFragmentMsgList.setLayoutParams(llp);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(INTENT_BUNDLE_KEY_MSG_CURRENT_TAB, mViewpagerMsgContent.getCurrentItem());
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            mRbtnFragmentMsgPm.setChecked(true);
        } else {
            mRbtnFragmentMsgPair.setChecked(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        if (checkedId == R.id.rbtn_fragment_msg_pm) {
            //私信列表
            mViewpagerMsgContent.setCurrentItem(0);
        } else {
            //配对列表
            mViewpagerMsgContent.setCurrentItem(1);
        }

    }
}
