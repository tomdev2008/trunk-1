package com.aibinong.tantan.ui.widget.chat;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.yueaiapi.pojo.GiftEntity;
import com.bumptech.glide.Glide;
import com.fatalsignal.util.DeviceUtils;
import com.matrixxun.starry.badgetextview.MaterialBadgeTextView;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Extend menu when user want send image, voice clip, etc
 */
public class EaseChatGiftMenu extends FrameLayout {

    @Bind(R.id.viewpager_chat_gift_menu)
    ViewPager mViewpagerChatGiftMenu;
    @Bind(R.id.cpi_chat_extend_gift_indicator)
    CirclePageIndicator mCpiChatExtendGiftIndicator;
    private List<GiftEntity> itemModels;
    private HashMap<String, GiftEntity> mOwnedGiftsMap = new HashMap<>();
    private GiftMenuItemClickListener mGiftMenuItemClickListener;

    public EaseChatGiftMenu(Context context) {
        super(context);
        initView();
    }

    public EaseChatGiftMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public EaseChatGiftMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EaseChatGiftMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.abn_yueai_view_giftmenu, this, true);
        ButterKnife.bind(this);
        ViewGroup.LayoutParams vlp = mViewpagerChatGiftMenu.getLayoutParams();
        vlp.height = (int) ((DeviceUtils.getScreenWidth(getContext()) / 2.0f) + 48 * DeviceUtils.getScreenDensity(getContext()));
        mViewpagerChatGiftMenu.setLayoutParams(vlp);

        mViewpagerChatGiftMenu.setAdapter(new MenuPagerAdapter());
        mCpiChatExtendGiftIndicator.setViewPager(mViewpagerChatGiftMenu);
    }

    /**
     * init
     */
    public void init(ArrayList<GiftEntity> giftEntities) {
        itemModels = giftEntities;
        mViewpagerChatGiftMenu.getAdapter().notifyDataSetChanged();
    }

    public void setGiftMenuItemClickListener(GiftMenuItemClickListener giftMenuItemClickListener) {
        mGiftMenuItemClickListener = giftMenuItemClickListener;
    }

    public void setOwnedGifts(List<GiftEntity> gifts) {
        mOwnedGiftsMap.clear();
        if (gifts != null) {
            for (GiftEntity giftEntity : gifts) {
                mOwnedGiftsMap.put(giftEntity.id, giftEntity);
            }
        }
        mViewpagerChatGiftMenu.getAdapter().notifyDataSetChanged();
    }

    private class ItemAdapter extends ArrayAdapter<GiftEntity> {

        private Context context;

        public ItemAdapter(Context context, List<GiftEntity> objects) {
            super(context, 0, objects);
            this.context = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.abn_yueai_item_giftmenu, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            GiftEntity giftEntity = getItem(position);
            holder.bindData(giftEntity);

            return convertView;
        }


    }

    class ViewHolder implements OnClickListener {
        @Bind(R.id.iv_item_giftmenu_image)
        ImageView mIvItemGiftmenuImage;
        @Bind(R.id.tv_item_giftmenu_name)
        TextView mTvItemGiftmenuName;
        @Bind(R.id.badge_item_giftmenu_count)
        MaterialBadgeTextView mBadgeItemGiftmenuCount;
        private GiftEntity mGiftEntity;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            ViewGroup.LayoutParams vlp = mIvItemGiftmenuImage.getLayoutParams();
            vlp.height = (int) (DeviceUtils.getScreenWidth(getContext()) / 4.0f);
            vlp.width = vlp.height;
            mIvItemGiftmenuImage.setLayoutParams(vlp);
            view.setOnClickListener(this);
        }

        public void bindData(GiftEntity giftEntity) {
            mGiftEntity = giftEntity;
            Glide.with(getContext()).load(mGiftEntity.img).into(mIvItemGiftmenuImage);
            GiftEntity ownedGift = mOwnedGiftsMap.get(mGiftEntity.id);
            int ownedCount = 0;
            if (ownedGift != null) {
                ownedCount = ownedGift.count;
            }
            mBadgeItemGiftmenuCount.setBadgeCount(ownedCount, true, Integer.MAX_VALUE);
            mTvItemGiftmenuName.setText(mGiftEntity.name);
        }

        @Override
        public void onClick(View view) {
            GiftEntity ownedGift = mOwnedGiftsMap.get(mGiftEntity.id);
            if (ownedGift != null && ownedGift.count > 0) {
                if (mGiftMenuItemClickListener != null) {
                    mGiftMenuItemClickListener.onClick(mGiftEntity, view);
                }
                ownedGift.count--;
                bindData(mGiftEntity);
            } else {
                if (mGiftMenuItemClickListener != null) {
                    mGiftMenuItemClickListener.onBuyGift();
                }
            }

        }
    }

    private class MenuPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            int itemCount = (itemModels == null ? 0 : itemModels.size());
            return (int) Math.ceil(itemCount / 8.0f);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            GridView gridView = new GridView(getContext());
            gridView.setNumColumns(4);
            gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            gridView.setGravity(Gravity.CENTER_VERTICAL);

            ArrayList<GiftEntity> models = new ArrayList<>(4);
            for (int i = position * 8; i < Math.min(itemModels.size(), position * 8 + 8); i++) {
                final GiftEntity itemModel = itemModels.get(i);
                models.add(itemModel);
            }
            gridView.setAdapter(new ItemAdapter(getContext(), models));
            container.addView(gridView);
            return gridView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    public interface GiftMenuItemClickListener {
        void onClick(GiftEntity gift, View view);

        void onBuyGift();
    }


}
