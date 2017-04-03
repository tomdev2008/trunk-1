//package com.aibinong.tantan.danmu;
//
//// _______________________________________________________________________________________________\
////|                                                                                               |
////| Created by yourfriendyang on 16/12/16.                                                                |
////| yourfriendyang@163.com                                                                        |
////|_______________________________________________________________________________________________|
//
//import android.graphics.Canvas;
//import android.view.View;
//
//import master.flame.danmaku.danmaku.model.android.AndroidDisplayer;
//import master.flame.danmaku.danmaku.model.android.ViewCacheStuffer;
//
//public class AbnDanMuViewHolder extends ViewCacheStuffer.ViewHolder {
//    public AbnDanMuViewHolder(View itemView) {
//        super(itemView);
//    }
//
//    public void measure(int widthMeasureSpec, int heightMeasureSpec) {
//        this.itemView.measure(widthMeasureSpec, heightMeasureSpec);
//    }
//
//    public int getMeasureWidth() {
//        return this.itemView.getMeasuredWidth();
//    }
//
//    public int getMeasureHeight() {
//        return this.itemView.getMeasuredHeight();
//    }
//
//    public void layout(int l, int t, int r, int b) {
//        this.itemView.layout(l, t, r, b);
//    }
//
//    public void draw(Canvas canvas, AndroidDisplayer.DisplayerConfig displayerConfig) {
//        this.itemView.draw(canvas);
//    }
//}
