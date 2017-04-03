package com.aibinong.tantan.ui.behavior;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.fatalsignal.util.DeviceUtils;

/**
 * Created by sunger on 2015/12/15.
 */
public class ScrollingFabBehavior extends CoordinatorLayout.Behavior<View> {
    private int toolbarHeight;

    public ScrollingFabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.toolbarHeight = DeviceUtils.getStatusBarHeight(context);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View fab, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View fab, View dependency) {
        if (dependency instanceof AppBarLayout) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            int fabBottomMargin = lp.bottomMargin;
            int distanceToScroll = fab.getHeight() + fabBottomMargin;
            float ratio = (float) dependency.getY() / (float) toolbarHeight;
            fab.setTranslationY(-distanceToScroll * ratio);
        }
        return true;
    }
}
