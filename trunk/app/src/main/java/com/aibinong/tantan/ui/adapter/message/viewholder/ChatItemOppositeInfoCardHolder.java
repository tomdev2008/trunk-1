package com.aibinong.tantan.ui.adapter.message.viewholder;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/14.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.app.Activity;
import android.content.Intent;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.constant.EMessageConstant;
import com.aibinong.tantan.constant.IntentExtraKey;
import com.aibinong.tantan.pojo.chat.HightLightEntity;
import com.aibinong.tantan.ui.activity.UserDetailActivity;
import com.aibinong.tantan.util.message.EaseSmileUtils;
import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.utils.UserUtil;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.fatalsignal.util.StringUtils;
import com.fatalsignal.util.TimeUtil;
import com.fatalsignal.view.RoundAngleImageView;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.kogitune.activity_transition.ActivityTransitionLauncher;

import org.json.JSONArray;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChatItemOppositeInfoCardHolder extends ChatItemBaseHolder {

    @Bind(R.id.tv_item_chat_opposite_infocard_image)
    RoundAngleImageView mTvItemChatOppositeInfocardImage;
    @Bind(R.id.tv_item_chat_opposite_infocard_name)
    TextView mTvItemChatOppositeInfocardName;
    @Bind(R.id.tv_item_chat_opposite_infocard_content)
    TextView mTvItemChatOppositeInfocardContent;
    @Bind(R.id.iv_item_infocard_sex)
    ImageView mIvItemInfocardSex;
    @Bind(R.id.tv_item_infocard_age)
    TextView mTvItemInfocardAge;
    @Bind(R.id.tv_item_infocard_location)
    TextView mTvItemInfocardLocation;
    @Bind(R.id.tv_item_infocard_activeTime)
    TextView mTvItemInfocardActiveTime;
    @Bind(R.id.ll_item_infocard_activetime)
    LinearLayout mLlItemInfocardActivetime;
    @Bind(R.id.ll_item_infocard_age)
    LinearLayout mLlItemInfocardAge;


    public ChatItemOppositeInfoCardHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void bindContentView(ViewGroup viewGroup) {
        LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.abn_yueai_item_chat_opposite_infocard, viewGroup, true);
        ButterKnife.bind(this, viewGroup);

    }

    @Override
    protected void onBindData(EMMessage msg) {

        //图像
        String imageUrl = null;
        try {
            imageUrl = msg.getStringAttribute(EMessageConstant.KEY_EXT_imgUrl);
            DrawableTypeRequest<String> imgReq = Glide.with(itemView.getContext()).load(imageUrl);
            //是否要模糊
            /*if (mSelfUserEntity.memberLevel <= 0 && mChatToUserEntity != null && mChatToUserEntity.atomization == 1) {
                imgReq.bitmapTransform(new BlurTransformation(itemView.getContext(), 25));
            }*/
            imgReq.into(mTvItemChatOppositeInfocardImage);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        mTvItemChatOppositeInfocardName.setText(mChatToUserEntity.nickname);

        if (!StringUtils.isEmpty(mChatToUserEntity.distance)) {
            mTvItemInfocardLocation.setVisibility(View.VISIBLE);
            mTvItemInfocardLocation.setText(String.format("%s,", mChatToUserEntity.distance));
        } else {
            mTvItemInfocardLocation.setVisibility(View.GONE);
        }

        mTvItemInfocardActiveTime.setText(String.format("%s活跃", TimeUtil.getRelaStrStamp(mChatToUserEntity.activeTime)));
        mTvItemInfocardAge.setText(mChatToUserEntity.age + "岁");
        if (mChatToUserEntity.sex == UserEntity.SEX_FEMALE) {
            mIvItemInfocardSex.setImageResource(R.mipmap.abn_yueai_ic_detail_female);
        } else {
            mIvItemInfocardSex.setImageResource(R.mipmap.abn_yueai_ic_detail_male);
        }

        if (msg.getBody() instanceof EMTextMessageBody) {
            EMTextMessageBody txtBody = (EMTextMessageBody) mEMMessage.getBody();
            CharSequence msgBody = EaseSmileUtils.getSmiledText(itemView.getContext(), txtBody.getMessage());
            //看看是否需要高亮
            try {
                JSONArray highLightArr = msg.getJSONArrayAttribute(EMessageConstant.KEY_EXT_highLight);
                ArrayList<HightLightEntity> highLightEntitys = ApiHelper.getInstance().getGson().fromJson(highLightArr.toString(), new TypeToken<ArrayList<HightLightEntity>>() {
                }.getType());
                msgBody = EaseSmileUtils.getHighLightText(itemView.getContext(), msgBody, highLightEntitys, this);
                mTvItemChatOppositeInfocardContent.setMovementMethod(LinkMovementMethod.getInstance());
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
            mTvItemChatOppositeInfocardContent.setText(msgBody);
        } else {
            mTvItemChatOppositeInfocardContent.setText(R.string.abn_yueai_chat_unknow_msgtype);
        }


        if (UserUtil.isHelper(mChatToUserEntity)) {
            mLlItemInfocardAge.setVisibility(View.GONE);
            mLlItemInfocardActivetime.setVisibility(View.GONE);
        } else {
            mLlItemInfocardAge.setVisibility(View.VISIBLE);
            mLlItemInfocardActivetime.setVisibility(View.VISIBLE);
        }
        markMsgAsReaded();
    }

    @Override
    protected void onContentClick() {
        //进入用户资料页
        Intent intent = new Intent(itemView.getContext(), UserDetailActivity.class);
        intent.putExtra(IntentExtraKey.INTENT_EXTRA_KEY_USERINFO, mChatToUserEntity);
//        intent.putExtra(UserDetailActivity.INTENT_EXTRA_KEY_IMAGE_WH_RATIO, mIvItemPairCardImage.getWidth() / (mIvItemPairCardImage.getHeight() * 1.0f));
        ActivityTransitionLauncher.with((Activity) itemView.getContext()).from(mTvItemChatOppositeInfocardImage).launch(intent);
    }
}
