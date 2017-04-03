package com.aibinong.tantan.ui.adapter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/12/6.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.yueaiapi.pojo.GiftEntity;
import com.bumptech.glide.Glide;
import com.fatalsignal.util.DeviceUtils;
import com.fatalsignal.util.Log;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserDetailGiftAdapter extends RecyclerView.Adapter<UserDetailGiftAdapter.GiftHolder> {
    ArrayList<GiftEntity> mGiftEntities;

    public void setGiftEntities(ArrayList<GiftEntity> giftEntities) {
        mGiftEntities = giftEntities;
    }

    @Override
    public GiftHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.abn_yueai_item_userdetail_gift, parent, false);
        GiftHolder giftHolder = new GiftHolder(contentView, parent);
        return giftHolder;
    }

    @Override
    public void onBindViewHolder(GiftHolder holder, int position) {
        holder.bindData(mGiftEntities.get(position));
    }

    @Override
    public int getItemCount() {
        return mGiftEntities == null ? 0 : mGiftEntities.size();
    }


    static class GiftHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_item_userdetail_gift)
        ImageView mIvItemUserdetailGift;
        @Bind(R.id.tv_item_userdetail_gift_name)
        TextView mTvItemUserdetailGiftName;
        private GiftEntity mGiftEntity;

        public GiftHolder(View itemView, ViewGroup parent) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ViewGroup.LayoutParams vlp = mIvItemUserdetailGift.getLayoutParams();
            vlp.width = (int) (DeviceUtils.getScreenWidth(itemView.getContext()) / 4.0f);
            Log.e("parent.getWidth()="+parent.getWidth());
            if (parent.getWidth() > 0) {
                vlp.width = (int) (parent.getWidth() / 4.0f);
            }
            vlp.height = vlp.width;
            mIvItemUserdetailGift.setLayoutParams(vlp);

        }

        public void bindData(GiftEntity giftEntity) {
            mGiftEntity = giftEntity;
            Glide.with(itemView.getContext()).load(mGiftEntity.img).into(mIvItemUserdetailGift);
            mTvItemUserdetailGiftName.setText(String.format("%s x%d", mGiftEntity.name, mGiftEntity.count));
        }

    }
}
