package com.aibinong.tantan.ui.fragment;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/12/23.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.aibinong.tantan.R;
import com.aibinong.tantan.constant.Constant;
import com.aibinong.tantan.ui.activity.MainActivity;
import com.aibinong.tantan.ui.widget.WheelView;
import com.aibinong.yueaiapi.utils.ConfigUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GuideFragment3 extends Fragment implements View.OnClickListener, WheelView.OnSelectListener, CompoundButton.OnCheckedChangeListener {
    @Bind(R.id.iv_guide_image)
    ImageView mIvGuideImage;
    @Bind(R.id.loopview_guide3_age)
    WheelView mLoopviewGuide3Age;
    @Bind(R.id.btn_guide3_start)
    Button mBtnGuide3Start;
    @Bind(R.id.rbtn_guide3_male)
    RadioButton mRbtnGuide3Male;
    @Bind(R.id.rbtn_guide3_female)
    RadioButton mRbtnGuide3Female;
    private View mContentView;

    public static GuideFragment3 newInstance() {
        Bundle args = new Bundle();
        GuideFragment3 fragment = new GuideFragment3();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.abn_yueai_fragment_guide3, container, false);
        ButterKnife.bind(this, mContentView);
//        setupView(savedInstanceState);
        return mContentView;
    }

    protected void setupView(@Nullable Bundle savedInstanceState) {
        ArrayList<String> ageList = new ArrayList<>(Constant.maxShowAge);
        for (int i = Constant.minShowAge; i < Constant.maxShowAge; i++) {
            ageList.add(i + "");
        }
        ageList.add(Constant.maxShowAge + "+");
        mLoopviewGuide3Age.setData(ageList);

        int defaultAgeIdx = ConfigUtil.getInstance().getTempAge() - Constant.minShowAge;
        if (defaultAgeIdx < 0) {
            defaultAgeIdx = 0;
        } else if (defaultAgeIdx >= ageList.size()) {
            defaultAgeIdx = ageList.size() - 1;
        }
        mLoopviewGuide3Age.setDefault(defaultAgeIdx);
        mLoopviewGuide3Age.setOnSelectListener(this);

        mBtnGuide3Start.setOnClickListener(this);
        mRbtnGuide3Female.setOnCheckedChangeListener(this);
        mRbtnGuide3Male.setOnCheckedChangeListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnGuide3Start) {
            //结束当前页
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public void endSelect(int id, String text) {
        //选择
        ConfigUtil.getInstance().saveTempAge(Constant.minShowAge + id);
    }

    @Override
    public void selecting(int id, String text) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == mRbtnGuide3Female) {
            if (isChecked) {
                mRbtnGuide3Male.setChecked(false);
                ConfigUtil.getInstance().saveTempSex(0);
            }
        } else if (buttonView == mRbtnGuide3Male) {
            if (isChecked) {
                mRbtnGuide3Female.setChecked(false);
                ConfigUtil.getInstance().saveTempSex(1);
            }
        }
    }
}
