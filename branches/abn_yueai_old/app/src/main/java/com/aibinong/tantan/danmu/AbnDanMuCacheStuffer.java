//package com.aibinong.tantan.danmu;
//
//// _______________________________________________________________________________________________\
////|                                                                                               |
////| Created by yourfriendyang on 16/12/16.                                                                |
////| yourfriendyang@163.com                                                                        |
////|_______________________________________________________________________________________________|
//
//import android.content.Context;
//import android.text.TextPaint;
//import android.view.LayoutInflater;
//import android.view.View;
//
//import com.aibinong.tantan.R;
//import com.aibinong.yueaiapi.pojo.DanMuEntity;
//
//import master.flame.danmaku.danmaku.model.BaseDanmaku;
//import master.flame.danmaku.danmaku.model.android.AndroidDisplayer;
//import master.flame.danmaku.danmaku.model.android.ViewCacheStuffer;
//
//public class AbnDanMuCacheStuffer extends ViewCacheStuffer<AbnDanMuViewHolder> {
//    private Context mContext;
//
//    public AbnDanMuCacheStuffer(Context context) {
//        mContext = context;
//    }
//
//    @Override
//    public AbnDanMuViewHolder onCreateViewHolder(int viewType) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.abn_yueai_view_danmu_pair, null);
//        AbnDanMuViewHolder holder = new AbnDanMuViewHolder(view);
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(int viewType, AbnDanMuViewHolder viewHolder, BaseDanmaku danmaku, AndroidDisplayer.DisplayerConfig displayerConfig, TextPaint paint) {
//
//    }
//
//    @Override
//    public int getItemViewType(int position, BaseDanmaku danmaku) {
//        DanMuEntity danMuEntity = (DanMuEntity) danmaku.tag;
//        return danMuEntity.type;
//    }
//}
