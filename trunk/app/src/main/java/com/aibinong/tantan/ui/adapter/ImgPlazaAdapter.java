package com.aibinong.tantan.ui.adapter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/12/22.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aibinong.tantan.R;
import com.aibinong.yueaiapi.pojo.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ImgPlazaAdapter extends RecyclerView.Adapter<ImgPlazaAdapter.ImgPalzzaHolder> {
    private static final int VIEW_TYPE_NO_BIG = 0, VIEW_TYPE_LEFT_BIG = 1, VIEW_TYPE_RIGHT_BIG = 2, VIEW_TYPE_RIGHT_CENTERBIG = 3, VIEW_TYPE_LEFT_CENTERBIG = 4;

    private ArrayList<UserEntity> mUserEntities = new ArrayList<>();


    public ArrayList<UserEntity> getUserEntities() {
        return mUserEntities;
    }


    @Override
    public ImgPalzzaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LEFT_BIG) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.abn_yueai_item_imgplaza_leftbig, parent, false);
            ImgPalzzaHolder holder = new ImgPalzzaHolder(view);
            return holder;
        } else if (viewType == VIEW_TYPE_RIGHT_BIG) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.abn_yueai_item_imgplaza_rightbig, parent, false);
            ImgPalzzaHolder holder = new ImgPalzzaHolder(view);
            return holder;
        } else if (viewType == VIEW_TYPE_RIGHT_CENTERBIG) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.abn_yueai_item_imgplaza_right_centerbig, parent, false);
            ImgPalzzaHolder holder = new ImgPalzzaHolder(view);
            return holder;
        } else if (viewType == VIEW_TYPE_LEFT_CENTERBIG) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.abn_yueai_item_imgplaza_left_centerbig, parent, false);
            ImgPalzzaHolder holder = new ImgPalzzaHolder(view);
            return holder;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.abn_yueai_item_imgplaza_nobig, parent, false);
            ImgPalzzaHolder holder = new ImgPalzzaHolder(view);
            return holder;
        }
    }


    @Override
    public int getItemViewType(int position) {
        //3个一组，包含大图的就认为需要大图
        List<UserEntity> groupEntities = mUserEntities.subList(position * 3, Math.min((position + 1) * 3, mUserEntities.size() - 1));
        int bigIdx = -1;
        for (int i = 0; i < groupEntities.size(); i++) {
            UserEntity userEntity = groupEntities.get(i);
            if (userEntity.isVIP() || userEntity.memberLevel > 0) {
                bigIdx = i;
                break;
            }
        }


        if (bigIdx == 0) {
            return VIEW_TYPE_LEFT_BIG;
        } else if (bigIdx == 2) {
            return VIEW_TYPE_RIGHT_BIG;
        } else if (bigIdx == 1) {

            //根据历史来判断
            int currPos = position;
            while (currPos > 0) {
                currPos--;
                int viewType = getItemViewType(currPos);
                if (viewType == VIEW_TYPE_LEFT_BIG || viewType == VIEW_TYPE_LEFT_CENTERBIG) {
                    return VIEW_TYPE_RIGHT_CENTERBIG;
                }
            }
            return VIEW_TYPE_LEFT_CENTERBIG;
        }
        return VIEW_TYPE_NO_BIG;
    }

    @Override
    public void onBindViewHolder(ImgPalzzaHolder holder, int position) {
        int start = position * 3;
        int end = Math.min((position + 1) * 3, mUserEntities.size());
        List<UserEntity> groupEntities = mUserEntities.subList(start, end);
        holder.bindData(groupEntities);
    }

    @Override
    public int getItemCount() {
        int count = (int) Math.ceil(mUserEntities.size() / 3.0f);
        return count;
    }

    class ImgPalzzaHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.abn_yueai_imgplaza_item_img1)
        com.aibinong.tantan.ui.widget.ImgPlazaCard mAbnYueaiImgplazaItemImg1;
        @Bind(R.id.abn_yueai_imgplaza_item_img2)
        com.aibinong.tantan.ui.widget.ImgPlazaCard mAbnYueaiImgplazaItemImg2;
        @Bind(R.id.abn_yueai_imgplaza_item_img3)
        com.aibinong.tantan.ui.widget.ImgPlazaCard mAbnYueaiImgplazaItemImg3;

        public ImgPalzzaHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        void bindData(List<UserEntity> userEntities) {
            mAbnYueaiImgplazaItemImg1.setUserInfo(userEntities.get(0));
            if (userEntities.size() > 1) {
                mAbnYueaiImgplazaItemImg2.setUserInfo(userEntities.get(1));
                mAbnYueaiImgplazaItemImg2.setVisibility(View.VISIBLE);
            } else {
                mAbnYueaiImgplazaItemImg2.setVisibility(View.INVISIBLE);
            }
            if (userEntities.size() > 2) {
                mAbnYueaiImgplazaItemImg3.setUserInfo(userEntities.get(2));
                mAbnYueaiImgplazaItemImg3.setVisibility(View.VISIBLE);
            } else {
                mAbnYueaiImgplazaItemImg3.setVisibility(View.INVISIBLE);
            }
        }
    }
}
