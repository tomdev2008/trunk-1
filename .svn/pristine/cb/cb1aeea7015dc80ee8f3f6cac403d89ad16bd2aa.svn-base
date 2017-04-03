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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.hyphenate.util.DensityUtil;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Extend menu when user want send image, voice clip, etc
 */
public class EaseChatExtendMenu extends FrameLayout {

    @Bind(R.id.viewpager_chat_extend_menu)
    ViewPager mViewpagerChatExtendMenu;
    @Bind(R.id.cpi_chat_extend_menu_indicator)
    CirclePageIndicator mCpiChatExtendMenuIndicator;
    private List<ChatMenuItemModel> itemModels = new ArrayList<ChatMenuItemModel>();

    public EaseChatExtendMenu(Context context) {
        super(context);
        initView();
    }

    public EaseChatExtendMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public EaseChatExtendMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EaseChatExtendMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.abn_yueai_view_chatextendmenu, this, true);
        ButterKnife.bind(this);

    }

    /**
     * init
     */
    public void init() {
        mViewpagerChatExtendMenu.setAdapter(new MenuPagerAdapter());
        mCpiChatExtendMenuIndicator.setViewPager(mViewpagerChatExtendMenu);
    }

    /**
     * register menu item
     *
     * @param name        item name
     * @param drawableRes background of item
     * @param itemId      id
     * @param listener    on click event of item
     */
    public void registerMenuItem(String name, int drawableRes, int itemId, EaseChatExtendMenuItemClickListener listener) {
        ChatMenuItemModel item = new ChatMenuItemModel();
        item.name = name;
        item.image = drawableRes;
        item.id = itemId;
        item.clickListener = listener;
        itemModels.add(item);

    }

    /**
     * register menu item
     *
     * @param nameRes     resource id of item name
     * @param drawableRes background of item
     * @param itemId      id
     * @param listener    on click event of item
     */
    public void registerMenuItem(int nameRes, int drawableRes, int itemId, EaseChatExtendMenuItemClickListener listener) {
        registerMenuItem(getContext().getString(nameRes), drawableRes, itemId, listener);
    }


    private class ItemAdapter extends ArrayAdapter<ChatMenuItemModel> {

        private Context context;

        public ItemAdapter(Context context, List<ChatMenuItemModel> objects) {
            super(context, 0, objects);
            this.context = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ChatMenuItem menuItem = null;
            if (convertView == null) {
                convertView = new ChatMenuItem(context);
            }
            menuItem = (ChatMenuItem) convertView;
            menuItem.setImage(getItem(position).image);
            menuItem.setText(getItem(position).name);
            menuItem.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (getItem(position).clickListener != null) {
                        getItem(position).clickListener.onClick(getItem(position).id, v);
                    }
                }
            });
            return convertView;
        }


    }

    private class MenuPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            int itemCount = itemModels == null ? 0 : itemModels.size();
            return (int) Math.ceil(itemCount / 4.0f);
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
            gridView.setVerticalSpacing(DensityUtil.dip2px(getContext(), 8));
            ArrayList<ChatMenuItemModel> models = new ArrayList<>(4);
            for (int i = position * 4; i < Math.min(itemModels.size(), position * 4 + 4); i++) {
                final ChatMenuItemModel itemModel = itemModels.get(i);
                models.add(itemModel);
            }
            gridView.setAdapter(new ItemAdapter(getContext(), models));
            container.addView(gridView);
            return gridView;
        }
    }

    public interface EaseChatExtendMenuItemClickListener {
        void onClick(int itemId, View view);
    }


    class ChatMenuItemModel {
        String name;
        int image;
        int id;
        EaseChatExtendMenuItemClickListener clickListener;
    }

    class ChatMenuItem extends LinearLayout {
        private ImageView imageView;
        private TextView textView;

        public ChatMenuItem(Context context, AttributeSet attrs, int defStyle) {
            this(context, attrs);
        }

        public ChatMenuItem(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(context, attrs);
        }

        public ChatMenuItem(Context context) {
            super(context);
            init(context, null);
        }

        private void init(Context context, AttributeSet attrs) {
            LayoutInflater.from(context).inflate(R.layout.abn_yueai_item_chat_extends_menu, this);
            imageView = (ImageView) findViewById(R.id.image);
            textView = (TextView) findViewById(R.id.text);
        }

        public void setImage(int resid) {
            imageView.setBackgroundResource(resid);
        }

        public void setText(int resid) {
            textView.setText(resid);
        }

        public void setText(String text) {
            textView.setText(text);
        }
    }
}
