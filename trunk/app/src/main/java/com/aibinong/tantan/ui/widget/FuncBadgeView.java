package com.aibinong.tantan.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.fatalsignal.util.StringUtils;

/**
 * Created by yourfriendyang on 16/3/25.
 */
public class FuncBadgeView extends FrameLayout {


    private View mTv_widget_func_badge_point;
    private TextView mTv_widget_func_badge_badge;

    // End Of Content View Elements

    private void bindViews() {

        mTv_widget_func_badge_point = (View) findViewById(R.id.tv_widget_func_badge_point);
        mTv_widget_func_badge_badge = (TextView) findViewById(R.id.tv_widget_func_badge_badge);
    }


    public FuncBadgeView(Context context) {
        super(context);
        initView();
    }

    public FuncBadgeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public FuncBadgeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FuncBadgeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.wieget_badge_view, this, true);
        bindViews();
    }

    public void setPoint(boolean show) {
        if (show) {
            mTv_widget_func_badge_badge.setVisibility(INVISIBLE);
            mTv_widget_func_badge_point.setVisibility(VISIBLE);
        } else {
            mTv_widget_func_badge_point.setVisibility(INVISIBLE);
        }
    }

    public void setBadge(int count) {
        if (count <= 0) {
            mTv_widget_func_badge_badge.setVisibility(View.INVISIBLE);
        } else {
            mTv_widget_func_badge_point.setVisibility(INVISIBLE);

            mTv_widget_func_badge_badge.setVisibility(View.VISIBLE);
            mTv_widget_func_badge_badge.setText(count > 99 ? "99+" : String.format(
                    "%d", count));
        }
    }

    public void setBadge(String count) {
        if (StringUtils.isEmpty(count)) {
            mTv_widget_func_badge_badge.setVisibility(View.INVISIBLE);
        } else {
            mTv_widget_func_badge_point.setVisibility(INVISIBLE);

            mTv_widget_func_badge_badge.setVisibility(View.VISIBLE);
            mTv_widget_func_badge_badge.setText(count.length() > 2 ? "..." : count);
        }
    }
}
