package com.aibinong.tantan.util;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aibinong.tantan.ui.widget.LoadingFooter;
import com.cundong.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.fatalsignal.util.Log;

/**
 * Created by cundong on 2015/11/9.
 * <p/>
 * 分页展示数据时，RecyclerView的FooterView State 操作工具类
 * <p/>
 * RecyclerView一共有几种State：Normal/Loading/Error/TheEnd
 */
public class RecyclerViewStateUtils {
    public static void setFooterViewState(Activity instance, RecyclerView recyclerView, boolean hideFooter, LoadingFooter.State state, View.OnClickListener errorListener) {
        setFooterViewState(instance, recyclerView, hideFooter, state, errorListener, null);
    }

    /**
     * 设置headerAndFooterAdapter的FooterView State
     *
     * @param instance      context
     * @param recyclerView  recyclerView
     * @param state         FooterView State
     * @param errorListener FooterView处于Error状态时的点击事件
     */
    public static void setFooterViewState(Activity instance, RecyclerView recyclerView, boolean hideFooter, LoadingFooter.State state, View.OnClickListener errorListener, String str) {

        if (instance == null || instance.isFinishing()) {
            return;
        }

        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();

        if (outerAdapter == null || !(outerAdapter instanceof HeaderAndFooterRecyclerViewAdapter)) {
            return;
        }

        HeaderAndFooterRecyclerViewAdapter headerAndFooterAdapter = (HeaderAndFooterRecyclerViewAdapter) outerAdapter;

        LoadingFooter footerView;
        //只有一页的时候，就别加什么FooterView了
        if (hideFooter) {
            if (headerAndFooterAdapter.getFooterViewsCount() > 0) {
                footerView = (LoadingFooter) headerAndFooterAdapter.getFooterView();
                headerAndFooterAdapter.removeFooterView(footerView);
                Log.d("hide footerView");
            } else {
                Log.d("hide footerView but no");
            }
            return;
        }

        //已经有footerView了
        if (headerAndFooterAdapter.getFooterViewsCount() > 0) {
            footerView = (LoadingFooter) headerAndFooterAdapter.getFooterView();
            footerView.setState(state, str);
            Log.d("headerAndFooterAdapter.getFooterViewsCount() > 0" + " state = " + state);
            if (state == LoadingFooter.State.NetWorkError) {
                footerView.setOnClickListener(errorListener);
            }
//            recyclerView.scrollToPosition(headerAndFooterAdapter.getItemCount() - 1);
        } else {
            Log.d("headerAndFooterAdapter.getFooterViewsCount() <= 0" + " state = " + state);

            footerView = new LoadingFooter(instance);
            footerView.setState(state, str);

            if (state == LoadingFooter.State.NetWorkError) {
                footerView.setOnClickListener(errorListener);
            }

            headerAndFooterAdapter.addFooterView(footerView);
//            recyclerView.scrollToPosition(headerAndFooterAdapter.getItemCount() - 1);
        }
    }

    /**
     * 获取当前RecyclerView.FooterView的状态
     *
     * @param recyclerView
     */
    public static LoadingFooter.State getFooterViewState(RecyclerView recyclerView) {

        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();
        if (outerAdapter != null && outerAdapter instanceof HeaderAndFooterRecyclerViewAdapter) {
            if (((HeaderAndFooterRecyclerViewAdapter) outerAdapter).getFooterViewsCount() > 0) {
                LoadingFooter footerView = (LoadingFooter) ((HeaderAndFooterRecyclerViewAdapter) outerAdapter).getFooterView();
                return footerView.getState();
            }
        }

        return LoadingFooter.State.Normal;
    }

    /**
     * 设置当前RecyclerView.FooterView的状态
     *
     * @param recyclerView
     * @param state
     */
    public static void setFooterViewState(RecyclerView recyclerView, LoadingFooter.State state) {
        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();
        if (outerAdapter != null && outerAdapter instanceof HeaderAndFooterRecyclerViewAdapter) {
            if (((HeaderAndFooterRecyclerViewAdapter) outerAdapter).getFooterViewsCount() > 0) {
                LoadingFooter footerView = (LoadingFooter) ((HeaderAndFooterRecyclerViewAdapter) outerAdapter).getFooterView();
                footerView.setState(state, null);
            }
        }
    }


    public static void setHeaderViewState(Activity instance, RecyclerView recyclerView, boolean hideFooter, LoadingFooter.State state, View.OnClickListener errorListener, String str) {

        if (instance == null || instance.isFinishing()) {
            return;
        }

        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();

        if (outerAdapter == null || !(outerAdapter instanceof HeaderAndFooterRecyclerViewAdapter)) {
            return;
        }

        HeaderAndFooterRecyclerViewAdapter headerAndFooterAdapter = (HeaderAndFooterRecyclerViewAdapter) outerAdapter;

        LoadingFooter footerView;
        //只有一页的时候，就别加什么FooterView了
        if (hideFooter) {
            if (headerAndFooterAdapter.getHeaderViewsCount() > 0) {
                footerView = (LoadingFooter) headerAndFooterAdapter.getHeaderView();
                headerAndFooterAdapter.removeHeaderView(footerView);
                Log.d("hide footerView");
            } else {
                Log.d("hide footerView but no");
            }
            return;
        }

        //已经有footerView了
        if (headerAndFooterAdapter.getHeaderViewsCount() > 0) {
            footerView = (LoadingFooter) headerAndFooterAdapter.getHeaderView();
            footerView.setState(state, str);
            Log.d("headerAndFooterAdapter.getFooterViewsCount() > 0" + " state = " + state);
            if (state == LoadingFooter.State.NetWorkError) {
                footerView.setOnClickListener(errorListener);
            }
//            recyclerView.scrollToPosition(headerAndFooterAdapter.getItemCount() - 1);
        } else {
            Log.d("headerAndFooterAdapter.getFooterViewsCount() <= 0" + " state = " + state);

            footerView = new LoadingFooter(instance);
            footerView.setState(state, str);

            if (state == LoadingFooter.State.NetWorkError) {
                footerView.setOnClickListener(errorListener);
            }

            headerAndFooterAdapter.addHeaderView(footerView);
//            recyclerView.scrollToPosition(headerAndFooterAdapter.getItemCount() - 1);
        }
    }

    public static LoadingFooter.State getHeaderViewState(RecyclerView recyclerView) {

        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();
        if (outerAdapter != null && outerAdapter instanceof HeaderAndFooterRecyclerViewAdapter) {
            if (((HeaderAndFooterRecyclerViewAdapter) outerAdapter).getHeaderViewsCount() > 0) {
                LoadingFooter footerView = (LoadingFooter) ((HeaderAndFooterRecyclerViewAdapter) outerAdapter).getHeaderView();
                return footerView.getState();
            }
        }

        return LoadingFooter.State.Normal;
    }
}
