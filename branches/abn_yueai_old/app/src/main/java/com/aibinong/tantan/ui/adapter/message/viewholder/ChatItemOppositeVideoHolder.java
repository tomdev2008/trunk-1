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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.ui.activity.message.EaseShowVideoActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fatalsignal.util.DeviceUtils;
import com.fatalsignal.util.Log;
import com.fatalsignal.util.StringUtils;
import com.fatalsignal.view.RoundAngleImageView;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVideoMessageBody;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChatItemOppositeVideoHolder extends ChatItemBaseHolder {


    @Bind(R.id.tv_item_chat_self_video_content)
    RoundAngleImageView mTvItemChatSelfVideoContent;
    @Bind(R.id.ibtn_item_chat_self_video_load)
    ImageView mIbtnItemChatSelfVideoLoad;
    @Bind(R.id.tv_item_chat_self_video_load)
    TextView mTvItemChatSelfVideoLoad;
    @Bind(R.id.ll_item_chat_self_video_load)
    LinearLayout mLlItemChatSelfVideoLoad;
    private EMVideoMessageBody mVideoBody;

    public ChatItemOppositeVideoHolder(View itemView) {
        super(itemView);

    }

    @Override
    protected void bindContentView(ViewGroup viewGroup) {
        LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.abn_yueai_item_chat_self_video, viewGroup, true);
        ButterKnife.bind(this, viewGroup);
    }

    @Override
    protected void onBindData(EMMessage msg) {
        if (msg.getBody() instanceof EMVideoMessageBody) {
            mVideoBody = (EMVideoMessageBody) mEMMessage.getBody();

            int maxW = (int) ((DeviceUtils.getScreenWidth(itemView.getContext()) * 2) / 3.0f);

            ViewGroup.LayoutParams vlp = mTvItemChatSelfVideoContent.getLayoutParams();
            vlp.width = maxW;
            vlp.height = maxW;
            mTvItemChatSelfVideoContent.setLayoutParams(vlp);
            String imageUrl = mVideoBody.getThumbnailUrl();
            if (StringUtils.isEmpty(imageUrl)) {
                imageUrl = mVideoBody.getLocalThumb();
            }
            if (StringUtils.isEmpty(imageUrl)) {
                imageUrl = mVideoBody.getLocalUrl();
            }
            if (StringUtils.isEmpty(imageUrl)) {
                imageUrl = mVideoBody.getRemoteUrl();
            }
            Log.i("ChatItemOppositeVideoHolder", "pos=" + getAdapterPosition() + "imageUrl=" + imageUrl);
            Glide.with(itemView.getContext()).load(imageUrl).into(new SimpleTarget<GlideDrawable>(maxW, maxW) {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    mTvItemChatSelfVideoContent.setImageDrawable(resource);
                }
            });

        }
    }

    @Override
    protected void onContentClick() {
        Intent intent = new Intent(itemView.getContext(), EaseShowVideoActivity.class);
        intent.putExtra("localpath", mVideoBody.getLocalUrl());
        intent.putExtra("secret", mVideoBody.getSecret());
        intent.putExtra("remotepath", mVideoBody.getRemoteUrl());
        itemView.getContext().startActivity(intent);
        markMsgAsReaded();
    }
}
