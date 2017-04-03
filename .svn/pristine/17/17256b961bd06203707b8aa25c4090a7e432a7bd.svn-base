package com.aibinong.tantan.ui.widget.chat;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.pojo.chat.EaseEmojiconGroupEntity;
import com.aibinong.tantan.ui.widget.chat.emojicon.EaseEmojiconMenu;
import com.aibinong.tantan.ui.widget.chat.emojicon.EaseEmojiconMenuBase;
import com.aibinong.tantan.util.message.EaseDefaultEmojiconDatas;
import com.aibinong.tantan.util.message.EaseEmojicon;
import com.aibinong.tantan.util.message.EaseSmileUtils;
import com.aibinong.tantan.util.message.EmojiconExampleGroupData;
import com.aibinong.yueaiapi.pojo.GiftEntity;
import com.aibinong.yueaiapi.utils.ConfigUtil;
import com.fatalsignal.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/11.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

public class ChatInputMenu extends ChatPrimaryMenuBase implements View.OnClickListener, View.OnTouchListener, TextWatcher {


    @Bind(R.id.abn_yueai_extendMenu_chat_input_more)
    EaseChatExtendMenu mAbnYueaiExtendMenuChatInputMore;

    @Bind(R.id.ibtn_chat_input_primary_switchvoice)
    ImageButton mIbtnChatInputPrimarySwitchvoice;
    @Bind(R.id.ibtn_chat_input_primary_switchkeyboard)
    ImageButton mIbtnChatInputPrimarySwitchkeyboard;
    @Bind(R.id.edit_chat_input_primary_input)
    EditText mEditChatInputPrimaryInput;
    @Bind(R.id.btn_chat_input_primary_press_speak)
    Button mBtnChatInputPrimaryPressSpeak;
    @Bind(R.id.ibtn_chat_input_primary_switchface)
    ImageButton mIbtnChatInputPrimarySwitchface;
    @Bind(R.id.ibtn_chat_input_primary_switchmore)
    ImageButton mIbtnChatInputPrimarySwitchmore;
    @Bind(R.id.btn_chat_input_primary_send)
    Button mBtnChatInputPrimarySend;
    @Bind(R.id.abn_yueai_emoji_chat_input)
    EaseEmojiconMenu mAbnYueaiEmojiChatInput;
    @Bind(R.id.abn_yueai_chat_giftmenu)
    EaseChatGiftMenu mAbnYueaiChatGiftmenu;
    private ChatInputMenuListener mChatInputMenuListener;

    public ChatInputMenu(Context context) {
        super(context);
        initView();
    }


    public ChatInputMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ChatInputMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void setChatInputMenuListener(ChatInputMenuListener chatInputMenuListener) {
        mChatInputMenuListener = chatInputMenuListener;
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.abn_yueai_chat_input, this, true);
        ButterKnife.bind(this);

        mIbtnChatInputPrimarySwitchkeyboard.setOnClickListener(this);
        mIbtnChatInputPrimarySwitchvoice.setOnClickListener(this);
        mIbtnChatInputPrimarySwitchface.setOnClickListener(this);
        mIbtnChatInputPrimarySwitchmore.setOnClickListener(this);
        mBtnChatInputPrimarySend.setOnClickListener(this);
        mBtnChatInputPrimaryPressSpeak.setOnTouchListener(this);
        mEditChatInputPrimaryInput.addTextChangedListener(this);
        mEditChatInputPrimaryInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                String content = mEditChatInputPrimaryInput.getText().toString();
                if (!StringUtils.isEmpty(content)) {
                    if (actionId == EditorInfo.IME_ACTION_SEND
                            || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                        //发送
                        mBtnChatInputPrimarySend.performClick();
                        return true;
                    }
                }
                return false;
            }
        });

        mEditChatInputPrimaryInput.setOnClickListener(this);

        mBtnChatInputPrimaryPressSpeak.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mChatInputMenuListener != null) {
                    return mChatInputMenuListener.onPressToSpeakBtnTouch(v, event);
                }
                return false;
            }
        });

        // emojicon menu
        mAbnYueaiEmojiChatInput.setEmojiconMenuListener(new EaseEmojiconMenuBase.EaseEmojiconMenuListener() {

            @Override
            public void onExpressionClicked(EaseEmojicon emojicon) {
                if (emojicon.getType() != EaseEmojicon.Type.BIG_EXPRESSION) {
                    if (emojicon.getEmojiText() != null) {
                        onEmojiconInputEvent(EaseSmileUtils.getSmiledText(getContext(), emojicon.getEmojiText()));
                    }
                } else {
                    if (mChatInputMenuListener != null) {
                        mChatInputMenuListener.onBigExpressionClicked(emojicon);
                    }
                }
            }

            @Override
            public void onDeleteImageClicked() {
                onEmojiconDeleteEvent();
            }
        });
        switchKeyBoardMode();
    }

    public void init() {
        mAbnYueaiExtendMenuChatInputMore.init();
        ArrayList<EaseEmojiconGroupEntity> emojiconGroupList = null;
        if (emojiconGroupList == null) {
            emojiconGroupList = new ArrayList<EaseEmojiconGroupEntity>();
            emojiconGroupList.add(new EaseEmojiconGroupEntity(R.drawable.ee_1, Arrays.asList(EaseDefaultEmojiconDatas.getData())));
        }
        mAbnYueaiEmojiChatInput.init(emojiconGroupList);
        mAbnYueaiEmojiChatInput.addEmojiconGroup(EmojiconExampleGroupData.getData());
        mAbnYueaiChatGiftmenu.init(ConfigUtil.getInstance().getConfig().gifts);
    }

    public void registerExtendMenuItem(String name, int drawableRes, int itemId,
                                       EaseChatExtendMenu.EaseChatExtendMenuItemClickListener listener) {
        mAbnYueaiExtendMenuChatInputMore.registerMenuItem(name, drawableRes, itemId, listener);
    }


    public void registerExtendMenuItem(int nameRes, int drawableRes, int itemId,
                                       EaseChatExtendMenu.EaseChatExtendMenuItemClickListener listener) {
        mAbnYueaiExtendMenuChatInputMore.registerMenuItem(nameRes, drawableRes, itemId, listener);
    }

    private void switchKeyBoardMode() {
        boolean isTextEmpty = StringUtils.isEmpty(mEditChatInputPrimaryInput.getText().toString());
        mBtnChatInputPrimarySend.setVisibility(isTextEmpty ? INVISIBLE : VISIBLE);

        //键盘模式,隐藏按住说话，隐藏键盘按钮,如果没输入文字隐藏发送按钮
        mBtnChatInputPrimaryPressSpeak.setVisibility(INVISIBLE);
        mIbtnChatInputPrimarySwitchkeyboard.setVisibility(INVISIBLE);
        mIbtnChatInputPrimarySwitchface.setVisibility(VISIBLE);
        if (mBtnChatInputPrimarySend.getVisibility() == VISIBLE) {
            mIbtnChatInputPrimarySwitchmore.setVisibility(INVISIBLE);
        } else {
            mIbtnChatInputPrimarySwitchmore.setVisibility(VISIBLE);
        }

        mEditChatInputPrimaryInput.setVisibility(VISIBLE);
        mIbtnChatInputPrimarySwitchvoice.setVisibility(VISIBLE);

        closeExtendsMenu();
        closeEmojiMenu();
        closeGiftMenu();
    }

    private void switchVoiceMode() {


        //按住说话,隐藏输入框，隐藏表情按钮，隐藏发送按钮
        mIbtnChatInputPrimarySwitchvoice.setVisibility(INVISIBLE);
        mIbtnChatInputPrimarySwitchface.setVisibility(INVISIBLE);
        mBtnChatInputPrimarySend.setVisibility(INVISIBLE);
        mEditChatInputPrimaryInput.setVisibility(GONE);

        mBtnChatInputPrimaryPressSpeak.setVisibility(VISIBLE);
        mIbtnChatInputPrimarySwitchmore.setVisibility(VISIBLE);
        mIbtnChatInputPrimarySwitchkeyboard.setVisibility(VISIBLE);

        hideKeyboard();
        closeExtendsMenu();
        closeEmojiMenu();
        closeGiftMenu();
    }

    private void switchExtendsMenu() {
        hideKeyboard();
        closeEmojiMenu();
        closeGiftMenu();
        if (mAbnYueaiExtendMenuChatInputMore.getVisibility() == VISIBLE) {
            mAbnYueaiExtendMenuChatInputMore.setVisibility(GONE);
        } else {
            mAbnYueaiExtendMenuChatInputMore.setVisibility(VISIBLE);
        }
    }

    public void closeExtendsMenu() {
        mAbnYueaiExtendMenuChatInputMore.setVisibility(GONE);
    }

    public void openGiftMenu() {
        hideKeyboard();
        closeEmojiMenu();
        closeExtendsMenu();
        mAbnYueaiChatGiftmenu.setVisibility(VISIBLE);
    }

    public void setOwnedGifts(List<GiftEntity> gifts) {
        mAbnYueaiChatGiftmenu.setOwnedGifts(gifts);
    }

    public void closeGiftMenu() {
        mAbnYueaiChatGiftmenu.setVisibility(GONE);
    }

    public void switchEmojiMenu() {
        closeExtendsMenu();
        hideKeyboard();
        closeGiftMenu();
        if (mAbnYueaiEmojiChatInput.getVisibility() == VISIBLE) {
            closeEmojiMenu();
        } else {
            mAbnYueaiEmojiChatInput.setVisibility(VISIBLE);
        }
    }

    public void closeEmojiMenu() {
        mAbnYueaiEmojiChatInput.setVisibility(GONE);
    }

    public void setGiftMenuItemClickListener(EaseChatGiftMenu.GiftMenuItemClickListener giftMenuItemClickListener) {
        mAbnYueaiChatGiftmenu.setGiftMenuItemClickListener(giftMenuItemClickListener);
    }

    @Override
    public void onClick(View view) {
        if (view == mIbtnChatInputPrimarySwitchkeyboard) {
            switchKeyBoardMode();

        } else if (view == mIbtnChatInputPrimarySwitchvoice) {
            switchVoiceMode();
        } else if (view == mIbtnChatInputPrimarySwitchface) {
            //展开表情界面
            switchEmojiMenu();
        } else if (view == mIbtnChatInputPrimarySwitchmore) {
            //图片视频大表情等等
            switchExtendsMenu();
        } else if (view == mBtnChatInputPrimarySend) {
            //发送
            if (mChatInputMenuListener != null) {
                String s = mEditChatInputPrimaryInput.getText().toString();
                if (s != null && s.length() > 0) {
                    mChatInputMenuListener.onSendMessage(s);
                    mEditChatInputPrimaryInput.setText(null);
                }
            }
        } else if (view == mEditChatInputPrimaryInput) {
            closeExtendsMenu();
            closeEmojiMenu();
            closeGiftMenu();
        }
    }

    public boolean onBackPressed() {
        if (mAbnYueaiExtendMenuChatInputMore.getVisibility() == VISIBLE || mAbnYueaiEmojiChatInput.getVisibility() == VISIBLE || mAbnYueaiChatGiftmenu.getVisibility() == VISIBLE) {
            closeExtendsMenu();
            closeEmojiMenu();
            closeGiftMenu();
            return false;
        } else {
            return true;
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        boolean isTextEmpty = StringUtils.isEmpty(mEditChatInputPrimaryInput.getText().toString());
        mBtnChatInputPrimarySend.setVisibility(isTextEmpty ? INVISIBLE : VISIBLE);
        if (mBtnChatInputPrimarySend.getVisibility() == VISIBLE) {
            mIbtnChatInputPrimarySwitchmore.setVisibility(INVISIBLE);
        } else {
            mIbtnChatInputPrimarySwitchmore.setVisibility(VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


    /**
     * append emoji icon to editText
     *
     * @param emojiContent
     */
    public void onEmojiconInputEvent(CharSequence emojiContent) {
        mEditChatInputPrimaryInput.append(emojiContent);
    }

    /**
     * delete emojicon
     */
    public void onEmojiconDeleteEvent() {
        if (!TextUtils.isEmpty(mEditChatInputPrimaryInput.getText())) {
            KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
            mEditChatInputPrimaryInput.dispatchKeyEvent(event);
        }
    }

    @Override
    public void onExtendMenuContainerHide() {

    }

    @Override
    public void onTextInsert(CharSequence text) {

    }

    @Override
    public EditText getEditText() {
        return null;
    }


    public interface ChatInputMenuListener {
        /**
         * when send message button pressed
         *
         * @param content message content
         */
        void onSendMessage(String content);

        /**
         * when big icon pressed
         *
         * @param emojicon
         */
        void onBigExpressionClicked(EaseEmojicon emojicon);

        /**
         * when speak button is touched
         *
         * @param v
         * @param event
         * @return
         */
        boolean onPressToSpeakBtnTouch(View v, MotionEvent event);
    }
}
