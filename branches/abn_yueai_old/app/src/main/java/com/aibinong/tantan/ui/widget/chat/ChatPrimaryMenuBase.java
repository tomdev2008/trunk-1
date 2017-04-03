package com.aibinong.tantan.ui.widget.chat;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

public abstract class ChatPrimaryMenuBase extends LinearLayout {
    protected Activity activity;
    protected InputMethodManager inputManager;

    public ChatPrimaryMenuBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public ChatPrimaryMenuBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChatPrimaryMenuBase(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.activity = (Activity) context;
        inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }


    /**
     * emoji icon input event
     *
     * @param emojiContent
     */
    public abstract void onEmojiconInputEvent(CharSequence emojiContent);

    /**
     * emoji icon delete event
     */
    public abstract void onEmojiconDeleteEvent();

    /**
     * hide extend menu
     */
    public abstract void onExtendMenuContainerHide();


    /**
     * insert text
     *
     * @param text
     */
    public abstract void onTextInsert(CharSequence text);

    public abstract EditText getEditText();

    /**
     * hide keyboard
     */
    public void hideKeyboard() {
        if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (activity.getCurrentFocus() != null)
                inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


}
