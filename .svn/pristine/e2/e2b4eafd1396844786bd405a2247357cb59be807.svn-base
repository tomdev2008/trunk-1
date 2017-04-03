//package com.aibinong.tantan.danmu;
//
//// _______________________________________________________________________________________________\
////|                                                                                               |
////| Created by yourfriendyang on 16/12/15.                                                                |
////| yourfriendyang@163.com                                                                        |
////|_______________________________________________________________________________________________|
//
//import android.graphics.Color;
//
//import com.aibinong.yueaiapi.pojo.DanMuEntity;
//import com.aibinong.yueaiapi.pojo.DanMusEntity;
//import com.aibinong.yueaiapi.pojo.GiftDanMuEntity;
//import com.aibinong.yueaiapi.pojo.PairDanMuEntity;
//import com.aibinong.yueaiapi.pojo.RechargeDanMuEntity;
//
//import master.flame.danmaku.danmaku.model.BaseDanmaku;
//import master.flame.danmaku.danmaku.model.IDanmakus;
//import master.flame.danmaku.danmaku.model.android.Danmakus;
//import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
//
//public class AbnDanMuParser extends BaseDanmakuParser {
//    DanMusEntity mDanMuEntities;
//
//    public AbnDanMuParser(DanMusEntity danMuEntities) {
//        mDanMuEntities = danMuEntities;
//    }
//
//    @Override
//    protected IDanmakus parse() {
//        if (mDanMuEntities != null) {
//            Danmakus danmakus = new Danmakus(Danmakus.ST_BY_TIME);
//            for (int i = 0; i < mDanMuEntities.list.size(); i++) {
//                DanMuEntity danMuEntity = mDanMuEntities.list.get(i);
//                BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL, mContext);
//                if (danMuEntity instanceof PairDanMuEntity) {
//                    PairDanMuEntity pairDanMuEntity = (PairDanMuEntity) danMuEntity;
//                    danmaku.text = String.format("%s和%s配对成功", pairDanMuEntity.data.get(0).nickname, pairDanMuEntity.data.get(1).nickname);
//                } else if (danMuEntity instanceof GiftDanMuEntity) {
//                    GiftDanMuEntity pairDanMuEntity = (GiftDanMuEntity) danMuEntity;
//                    danmaku.text = String.format("%s送了%s %s", pairDanMuEntity.data.user.nickname, pairDanMuEntity.data.target.nickname, pairDanMuEntity.data.gift.name);
//                } else if (danMuEntity instanceof RechargeDanMuEntity) {
//                    RechargeDanMuEntity pairDanMuEntity = (RechargeDanMuEntity) danMuEntity;
//                    danmaku.text = String.format("%s升级成为%s", pairDanMuEntity.data.user.nickname, pairDanMuEntity.data.member.name);
//                }
//                danmaku.setTimer(mTimer);
////                danmaku.padding = 5;
//                danmaku.priority = 1;  // 一定会显示, 一般用于本机发送的弹幕
//                danmaku.isLive = false;
//                danmaku.setTime((long) (mDanMuEntities.intervals * 1000L * (i) + 1200L));
//                danmaku.textSize = 25f * (getDisplayer().getDensity() - 0.6f);
//                danmaku.textColor = Color.RED;
////                danmaku.duration = new Duration(8 * 1000L);
//                danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
//                danmaku.setTag(danMuEntity);
//                danmakus.addItem(danmaku);
//            }
//            return danmakus;
//        }
//        return null;
//    }
//}
