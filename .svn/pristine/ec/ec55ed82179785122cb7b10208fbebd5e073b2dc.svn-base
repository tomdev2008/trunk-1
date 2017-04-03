package com.aibinong.tantan.ui.adapter.message.viewholder;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/14.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aibinong.tantan.R;
import com.aibinong.tantan.ui.activity.FullScreenImageBrowserActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fatalsignal.util.DeviceUtils;
import com.fatalsignal.util.StringUtils;
import com.fatalsignal.view.RoundAngleImageView;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChatItemSelfImageHolder extends ChatItemBaseHolder implements View.OnClickListener {


    @Bind(R.id.tv_item_chat_self_image_content)
    RoundAngleImageView mTvItemChatSelfImageContent;
    private String mImageUrl;

    public ChatItemSelfImageHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void bindContentView(ViewGroup viewGroup) {
        LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.abn_yueai_item_chat_self_image, viewGroup, true);
        ButterKnife.bind(this, viewGroup);
        mTvItemChatSelfImageContent.setOnClickListener(this);
        mTvItemChatSelfImageContent.setOnLongClickListener(this);
    }

    @Override
    protected void onBindData(EMMessage msg) {
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
//            if (imageW > imageH) {
                //限宽
//                finalW = Math.min(imageW, maxW);
//                //也不能太小
//                finalW = Math.max(minW, finalW);
//                finalH = (int) (finalW * imageH / (imageW * 1.0f));
//            } else {
//                //限高
//                finalH = Math.min(imageH, maxW);
//                //也不能太小
//                finalH = Math.max(minW, finalH);
//                finalW = (int) (finalH * imageW / (imageH * 1.0f));
//            }
            ViewGroup.LayoutParams vlp = mTvItemChatSelfImageContent.getLayoutParams();
            vlp.width = finalW;
            vlp.height = finalH;
            mTvItemChatSelfImageContent.setLayoutParams(vlp);
            String imageUrl = txtBody.getLocalUrl();
            if (StringUtils.isEmpty(imageUrl) || !(new File(imageUrl).exists())) {
                imageUrl = txtBody.getThumbnailUrl();
            }
            if (StringUtils.isEmpty(imageUrl)) {
                imageUrl = txtBody.getRemoteUrl();
            }
            mImageUrl = imageUrl;
            Glide.with(itemView.getContext()).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(mTvItemChatSelfImageContent);


        }
    }



    @Override
    public void onClick(View view) {
        if (view == mTvItemChatSelfImageContent) {
            //全屏看图
            Intent intent = FullScreenImageBrowserActivity.launchIntent(itemView.getContext(), mImageUrl, false, true);
//            ActivityTransitionLauncher.with((Activity) view.getContext()).from(mTvItemChatSelfImageContent).launch(intent);
        }else{
            super.onClick(view);
        }
    }
}
