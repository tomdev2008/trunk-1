package com.aibinong.tantan.ui.widget.chat.emojicon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aibinong.tantan.R;
import com.hyphenate.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class EaseEmojiconIndicatorView extends LinearLayout {

    private Context context;

    private List<ImageView> dotViews;

    private int dotHeight = 12;

    public EaseEmojiconIndicatorView(Context context, AttributeSet attrs, int defStyle) {
        this(context, null);
    }

    public EaseEmojiconIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EaseEmojiconIndicatorView(Context context) {
        this(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        dotHeight = DensityUtil.dip2px(context, dotHeight);
        setGravity(Gravity.CENTER_HORIZONTAL);
    }

    public void init(int count) {
        dotViews = new ArrayList<ImageView>();
        for (int i = 0; i < count; i++) {
            RelativeLayout rl = new RelativeLayout(context);
            LayoutParams params = new LayoutParams(dotHeight, dotHeight);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            ImageView imageView = new ImageView(context);

            if (i == 0) {
                imageView.setImageResource(R.drawable.abn_yueai_shape_dot_indicator_sel);
                rl.addView(imageView, layoutParams);
            } else {
                imageView.setImageResource(R.drawable.abn_yueai_shape_dot_indicator);
                rl.addView(imageView, layoutParams);
            }
            this.addView(rl, params);
            dotViews.add(imageView);
        }
    }

    public void updateIndicator(int count) {
        if (dotViews == null) {
            return;
        }
        for (int i = 0; i < dotViews.size(); i++) {
            if (i >= count) {
                dotViews.get(i).setVisibility(GONE);
                ((View) dotViews.get(i).getParent()).setVisibility(GONE);
            } else {
                dotViews.get(i).setVisibility(VISIBLE);
                ((View) dotViews.get(i).getParent()).setVisibility(VISIBLE);
            }
        }
        if (count > dotViews.size()) {
            int diff = count - dotViews.size();
            for (int i = 0; i < diff; i++) {
                RelativeLayout rl = new RelativeLayout(context);
                LayoutParams params = new LayoutParams(dotHeight, dotHeight);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(R.drawable.abn_yueai_shape_dot_indicator);
                rl.addView(imageView, layoutParams);
                rl.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                this.addView(rl, params);
                dotViews.add(imageView);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }

    public void selectTo(int position) {
        for (ImageView iv : dotViews) {
            iv.setImageResource(R.drawable.abn_yueai_shape_dot_indicator);
        }
        dotViews.get(position).setImageResource(R.drawable.abn_yueai_shape_dot_indicator_sel);
    }


    public void selectTo(int startPosition, int targetPostion) {
        ImageView startView = dotViews.get(startPosition);
        ImageView targetView = dotViews.get(targetPostion);
        startView.setImageResource(R.drawable.abn_yueai_shape_dot_indicator);
        targetView.setImageResource(R.drawable.abn_yueai_shape_dot_indicator_sel);
    }

}   
