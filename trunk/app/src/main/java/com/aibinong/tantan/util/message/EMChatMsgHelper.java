package com.aibinong.tantan.util.message;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/14.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.aibinong.tantan.R;
import com.aibinong.tantan.constant.EMessageConstant;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class EMChatMsgHelper {
    public static String getRingDuring(String path) {
        String duration = null;
        android.media.MediaMetadataRetriever mmr = new android.media.MediaMetadataRetriever();

        try {
            if (path != null) {
                mmr.setDataSource(path);
            }

            duration = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mmr.release();
        }
        return duration;
    }

    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null)
            return null;

        Object obj = beanClass.newInstance();

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }

            field.setAccessible(true);
            field.set(obj, map.get(field.getName()));
        }

        return obj;
    }

    public static Map<String, Object> objectToMap(Object obj) throws Exception {
        if (obj == null) {
            return null;
        }

        Map<String, Object> map = new HashMap<String, Object>();

        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }

        return map;
    }


    private static final String TAG = "CommonUtils";

    /**
     * check if network avalable
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable() && mNetworkInfo.isConnected();
            }
        }

        return false;
    }

    /**
     * check if sdcard exist
     *
     * @return
     */
    public static boolean isSdcardExist() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    public static EMMessage createExpressionMessage(String toChatUsername, String expressioName, String identityCode) {
        EMMessage message = EMMessage.createTxtSendMessage("[" + expressioName + "]", toChatUsername);
        if (identityCode != null) {
            message.setAttribute(EMessageConstant.MESSAGE_ATTR_EXPRESSION_ID, identityCode);
        }
        message.setAttribute(EMessageConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, true);
        return message;
    }

    /**
     * Get digest according message type and content
     *
     * @param message
     * @param context
     * @return
     */
    public static String getMessageDigest(EMMessage message, Context context) {
        String digest = "";
        switch (message.getType()) {
            case LOCATION:
                if (message.direct() == EMMessage.Direct.RECEIVE) {
                    digest = getString(context, R.string.location_recv);
                    digest = String.format(digest, message.getFrom());
                    return digest;
                } else {
                    digest = getString(context, R.string.location_prefix);
                }
                break;
            case IMAGE:
                digest = getString(context, R.string.picture);
                break;
            case VOICE:
                digest = getString(context, R.string.voice_prefix);
                break;
            case VIDEO:
                digest = getString(context, R.string.video);
                break;
            case TXT:
                EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
                if (message.getBooleanAttribute(EMessageConstant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    digest = getString(context, R.string.voice_call) + txtBody.getMessage();
                } else if (message.getBooleanAttribute(EMessageConstant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                    digest = getString(context, R.string.video_call) + txtBody.getMessage();
                } else if (message.getBooleanAttribute(EMessageConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)) {
                    if (!TextUtils.isEmpty(txtBody.getMessage())) {
                        digest = txtBody.getMessage();
                    } else {
                        digest = getString(context, R.string.dynamic_expression);
                    }
                } else {
                    digest = txtBody.getMessage();
                }
                break;
            case FILE:
                digest = getString(context, R.string.file);
                break;
            case CMD:
                if (message.getIntAttribute(EMessageConstant.CMD_EXT_TYPE, 0) == EMessageConstant.CMD_EXT_TYPE_REVOCATION_MSG) {
                    //撤回消息
                    digest = getString(context, R.string.opposite_revoke_msg);
                }
                break;
            default:
                EMLog.e(TAG, "error, unknow type");
                return "";
        }

        return digest;
    }

    static String getString(Context context, int resId) {
        return context.getResources().getString(resId);
    }

    public static void markReadedAndListened(EMMessage mEMMessage) {
        if (mEMMessage.direct() == EMMessage.Direct.RECEIVE) {
            EMClient.getInstance().chatManager().getConversation(mEMMessage.getFrom()).markMessageAsRead(mEMMessage.getMsgId());
            if (!mEMMessage.isAcked() && mEMMessage.getChatType() == EMMessage.ChatType.Chat) {
                // 告知对方已读这条消息
                try {
                    EMClient.getInstance().chatManager().ackMessageRead(mEMMessage.getFrom(), mEMMessage.getMsgId());
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
            if (!mEMMessage.isListened()) {
                mEMMessage.setListened(true);
                EMClient.getInstance().chatManager().setMessageListened(mEMMessage);
            }
        }
    }
}
