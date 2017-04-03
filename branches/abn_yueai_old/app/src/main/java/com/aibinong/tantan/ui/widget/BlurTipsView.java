package com.aibinong.tantan.ui.widget;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/12/12.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.aibinong.tantan.R;
import com.gigamole.library.PulseView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BlurTipsView extends FrameLayout {
    @Bind(R.id.pulseView_blurtips_dot)
    PulseView mPulseViewBlurtipsDot;
    @Bind(R.id.iv_blurtips_blurtips)
    ImageView mIvBlurtipsBlurtips;

    public BlurTipsView(Context context) {
        super(context);
        init();
    }

    public BlurTipsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public BlurTipsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BlurTipsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.abn_yueai_view_blurtips, this, true);
        ButterKnife.bind(this);

    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility != VISIBLE) {
            mPulseViewBlurtipsDot.finishPulse();
        } else {
            mPulseViewBlurtipsDot.startPulse();
        }
    }
}
