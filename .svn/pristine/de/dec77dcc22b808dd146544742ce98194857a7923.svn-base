package com.aibinong.tantan.ui.adapter.message.viewholder;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/14.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aibinong.tantan.R;
import com.aibinong.tantan.constant.Constant;
import com.aibinong.tantan.ui.activity.CommonWebActivity;
import com.aibinong.tantan.ui.activity.FullScreenImageBrowserActivity;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fatalsignal.util.DeviceUtils;
import com.fatalsignal.util.StringUtils;
import com.fatalsignal.view.RoundAngleImageView;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.kogitune.activity_transition.ActivityTransitionLauncher;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class ChatItemOppositeImageHolder extends ChatItemBaseHolder implements View.OnClickListener {

    @Bind(R.id.tv_item_chat_opposite_image_content)
    RoundAngleImageView mTvItemChatOppositeImageContent;
    @Bind(R.id.ll_item_chat_opposite_image_blurtips)
    LinearLayout mLlItemChatOppositeImageBlurtips;
    private String mImageUrl;
    private boolean mImageBlur;

    public ChatItemOppositeImageHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void bindContentView(ViewGroup viewGroup) {
        LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.abn_yueai_item_chat_opposite_image, viewGroup, true);
        ButterKnife.bind(this, viewGroup);
        mLlItemChatOppositeImageBlurtips.setOnClickListener(this);
        mTvItemChatOppositeImageContent.setOnClickListener(this);
        mTvItemChatOppositeImageContent.setOnLongClickListener(this);
    }

    @Override
    protected void onBindData(EMMessage msg) {
        mImageBlur = false;
        if (msg.getBody() instanceof EMImageMessageBody) {
            EMImageMessageBody txtBody = (EMImageMessageBody) mEMMessage.getBody();
            int imageW = (int) (txtBody.getWidth() * DeviceUtils.getScreenDensity(itemView.getContext()));
            int imageH = (int) (txtBody.getHeight() * DeviceUtils.getScreenDensity(itemView.getContext()));
            int maxW = (int) ((DeviceUtils.getScreenWidth(itemView.getContext()) * 2) / 3.0f);
            int minW = (int) ((DeviceUtils.getScreenWidth(itemView.getContext()) * 1.0) / 3.0f);
            if (imageW <= 0 || imageH <= 0) {
                imageW = minW;
                imageH = minW;
            }
            int finalW = (int) (210*DeviceUtils.getScreenDensity(itemView.getContext()));
            int finalH = (int) (finalW/((1.0f*imageW)/imageH));
            //设置宽高，最宽或者最高不超过屏宽的2/3
           /* if (imageW > imageH) {
                //限宽
                finalW = Math.min(imageW, maxW);
                //也不能太小
                finalW = Math.max(minW, finalW);
                finalH = (int) (finalW * imageH / (imageW * 1.0f));
            } else {
                //限高
                finalH = Math.min(imageH, maxW);
                //也不能太小
                finalH = Math.max(minW, finalH);
                finalW = (int) (finalH * imageW / (imageH * 1.0f));
            }*/
            ViewGroup.LayoutParams vlp = mTvItemChatOppositeImageContent.getLayoutParams();
            vlp.width = finalW;
            vlp.height = finalH;
            mTvItemChatOppositeImageContent.setLayoutParams(vlp);
            String imageUrl = txtBody.getLocalUrl();
            if (StringUtils.isEmpty(imageUrl) || !(new File(imageUrl).exists())) {
                imageUrl = txtBody.getThumbnailUrl();
            }
            if (StringUtils.isEmpty(imageUrl)) {
                imageUrl = txtBody.getRemoteUrl();
            }
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) mLlItemChatOppositeImageBlurtips.getLayoutParams();
            rlp.leftMargin = (int) (finalW / 2.0f);
            mLlItemChatOppositeImageBlurtips.setLayoutParams(rlp);
            mImageUrl = imageUrl;
            DrawableTypeRequest<String> imgReq = Glide.with(itemView.getContext()).load(imageUrl);
            //是否要模糊
            if (mSelfUserEntity.memberLevel <= 0 && mChatToUserEntity != null && mChatToUserEntity.messageLevel > 0) {
                imgReq.bitmapTransform(new BlurTransformation(itemView.getContext(), 25));
                mLlItemChatOppositeImageBlurtips.setVisibility(View.VISIBLE);
                mImageBlur = true;
            } else {
                mLlItemChatOppositeImageBlurtips.setVisibility(View.INVISIBLE);
            }
            imgReq.diskCacheStrategy(DiskCacheStrategy.SOURCE).into(mTvItemChatOppositeImageContent);


        }
        markMsgAsReaded();
    }

    @Override
    public void onClick(View view) {
        if (view == mLlItemChatOppositeImageBlurtips) {
            //开通会员
            CommonWebActivity.launchIntent(itemView.getContext(), Constant._sUrl_vip_buy);
        } else if (view == mTvItemChatOppositeImageContent) {
            //全屏看图
            Intent intent = FullScreenImageBrowserActivity.launchIntent(itemView.getContext(), mImageUrl, mImageBlur, false);
            ActivityTransitionLauncher.with((Activity) view.getContext()).from(mTvItemChatOppositeImageContent).launch(intent);
        }else{
            super.onClick(view);
        }
    }
}
