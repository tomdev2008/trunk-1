package com.aibinong.tantan.ui.adapter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/10/19.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aibinong.tantan.R;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class PersonDetailGalleryAdapter extends PagerAdapter {
    private List<String> mImgList;
    private boolean mBlur;

    public PersonDetailGalleryAdapter(boolean blur) {
        mBlur = blur;
    }

    public void setmImgList(List<String> mImgList) {
        this.mImgList = mImgList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mImgList == null ? 0 : mImgList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_pager_person_detail_gallery, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_item_pager_person_detail_gallery);
        String imgUrl = mImgList.get(position);
        if (mBlur) {
            Glide.with(container.getContext()).load(imgUrl).bitmapTransform(new BlurTransformation(container.getContext(), 25)).into(imageView);
        } else {
            Glide.with(container.getContext()).load(imgUrl).into(imageView);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public static class DetailImageHolderView implements Holder<String> {
        private View mContentView;
        private ImageView imageView;

        public DetailImageHolderView() {

        }

        @Override
        public View createView(Context context) {
            mContentView = LayoutInflater.from(context).inflate(R.layout.item_pager_person_detail_gallery, null, false);
            imageView = (ImageView) mContentView.findViewById(R.id.iv_item_pager_person_detail_gallery);

            return mContentView;
        }

        @Override
        public void UpdateUI(Context context, final int position, String data) {
            Glide.with(context).load(data).asBitmap().into(imageView);
         /*   if (mBlur) {
                Glide.with(context).load(data).bitmapTransform(new BlurTransformation(context, 25)).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
            } else */{
                Glide.with(context).load(data).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
            }
        }
    }

}
