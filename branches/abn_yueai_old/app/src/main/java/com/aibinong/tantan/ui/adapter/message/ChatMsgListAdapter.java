package com.aibinong.tantan.ui.adapter.message;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/4.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.aibinong.tantan.constant.EMessageConstant;
import com.aibinong.tantan.presenter.message.ChatMsgListPresenter;
import com.aibinong.tantan.ui.adapter.message.viewholder.ChatItemBaseHolder;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

import static com.aibinong.tantan.ui.adapter.message.viewholder.ChatItemBaseHolder.VIEWTYPE_HIGHLIGHT_TEXT;
import static com.aibinong.tantan.ui.adapter.message.viewholder.ChatItemBaseHolder.VIEWTYPE_OPPOSITE_BIG_EXPRESSION;
import static com.aibinong.tantan.ui.adapter.message.viewholder.ChatItemBaseHolder.VIEWTYPE_OPPOSITE_GIFT;
import static com.aibinong.tantan.ui.adapter.message.viewholder.ChatItemBaseHolder.VIEWTYPE_OPPOSITE_IMAGE;
import static com.aibinong.tantan.ui.adapter.message.viewholder.ChatItemBaseHolder.VIEWTYPE_OPPOSITE_INFOCARD;
import static com.aibinong.tantan.ui.adapter.message.viewholder.ChatItemBaseHolder.VIEWTYPE_OPPOSITE_QUESTION;
import static com.aibinong.tantan.ui.adapter.message.viewholder.ChatItemBaseHolder.VIEWTYPE_OPPOSITE_TEXT;
import static com.aibinong.tantan.ui.adapter.message.viewholder.ChatItemBaseHolder.VIEWTYPE_OPPOSITE_VIDEO;
import static com.aibinong.tantan.ui.adapter.message.viewholder.ChatItemBaseHolder.VIEWTYPE_OPPOSITE_VOICE;
import static com.aibinong.tantan.ui.adapter.message.viewholder.ChatItemBaseHolder.VIEWTYPE_SELF_BIG_EXPRESSION;
import static com.aibinong.tantan.ui.adapter.message.viewholder.ChatItemBaseHolder.VIEWTYPE_SELF_GIFT;
import static com.aibinong.tantan.ui.adapter.message.viewholder.ChatItemBaseHolder.VIEWTYPE_SELF_IMAGE;
import static com.aibinong.tantan.ui.adapter.message.viewholder.ChatItemBaseHolder.VIEWTYPE_SELF_QUESTION;
import static com.aibinong.tantan.ui.adapter.message.viewholder.ChatItemBaseHolder.VIEWTYPE_SELF_TEXT;
import static com.aibinong.tantan.ui.adapter.message.viewholder.ChatItemBaseHolder.VIEWTYPE_SELF_VIDEO;
import static com.aibinong.tantan.ui.adapter.message.viewholder.ChatItemBaseHolder.VIEWTYPE_SELF_VOICE;

public class ChatMsgListAdapter extends RecyclerView.Adapter<ChatItemBaseHolder> {

    static class MapList {
        private List<EMMessage> mKVList;

        public MapList() {
            this(0);
        }

        public MapList(int capacity) {
            mKVList = new ArrayList<>(capacity);
        }

        public EMMessage get(int position) {
            return mKVList.get(position);
        }

        public int size() {
            return mKVList.size();
        }

        public void set(int idx, EMMessage msg) {
            mKVList.set(idx, msg);
        }

        public boolean add(EMMessage msg) {
            return mKVList.add(msg);
        }

        public void add(EMMessage msg, int idx) {
            mKVList.add(idx, msg);
        }

        public EMMessage remove(int idx) {
            return mKVList.remove(idx);
        }

        public void addAll(List<EMMessage> vList) {
            for (EMMessage v : vList) {
                mKVList.add(v);
            }
        }

        public void addFront(List<EMMessage> vList) {
            mKVList.addAll(0, vList);
        }

        public int indexOf(EMMessage msg) {
            for (int i = 0; i < mKVList.size(); i++) {
                EMMessage message = mKVList.get(i);
                if (message == msg) {
                    return i;
                } else if (message.getMsgId() == msg.getMsgId()) {
                    return i;
                }
            }
            return -1;
        }

        public int indexOf(String msgId) {
            for (int i = 0; i < mKVList.size(); i++) {
                EMMessage message = mKVList.get(i);
                if (message.getMsgId().equals(msgId)) {
                    return i;
                }
            }
            return -1;
        }

        public void clear() {
            mKVList.clear();
        }
    }


    private RecyclerView mRecyclerView;

    private MapList mEMMessageIndexMap = new MapList();
    private ChatMsgListPresenter mChatMsgListPresenter;
    private UserEntity mChatToUserEntity;

    public ChatMsgListAdapter(ChatMsgListPresenter presenter) {
        mChatMsgListPresenter = presenter;
    }

    public void setChatToUserEntity(UserEntity userEntity) {
        mChatToUserEntity = userEntity;
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public ChatItemBaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ChatItemBaseHolder holder = null;
        holder = ChatItemBaseHolder.newInstance(parent, viewType, mChatMsgListPresenter, mChatToUserEntity, this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ChatItemBaseHolder holder, int position) {
        holder.bindData(getItemAtPosition(position));
    }


    @Override
    public int getItemCount() {
        return mEMMessageIndexMap == null ? 0 : mEMMessageIndexMap.size();
    }

    public EMMessage getItemAtPosition(int position) {
        return mEMMessageIndexMap.get(position);
    }

    public EMMessage getPreItem(int position) {
        return getItemAtPosition(position - 1);
    }

    @Override
    public int getItemViewType(int position) {
        EMMessage emMessage = getItemAtPosition(position);
        if (emMessage.getType() == EMMessage.Type.TXT) {
            //系统消息，高亮提示
            if (emMessage.getIntAttribute(EMessageConstant.KEY_EXT_TYPE, EMessageConstant.EXT_TYPE_NORMAL) == EMessageConstant.EXT_TYPE_SYSTEM_MSG) {
                return VIEWTYPE_HIGHLIGHT_TEXT;
            }
        }
        if (emMessage.direct() == EMMessage.Direct.SEND) {
            //自己发出去的
            if (emMessage.getType() == EMMessage.Type.VOICE) {
                return VIEWTYPE_SELF_VOICE;
            } else if (emMessage.getType() == EMMessage.Type.IMAGE) {
                return VIEWTYPE_SELF_IMAGE;
            } else if (emMessage.getType() == EMMessage.Type.VIDEO) {
                return VIEWTYPE_SELF_VIDEO;
            } else {
                if (emMessage.getType() == EMMessage.Type.TXT) {
                    if (emMessage.getIntAttribute(EMessageConstant.KEY_EXT_TYPE, EMessageConstant.EXT_TYPE_NORMAL) == EMessageConstant.EXT_TYPE_QUESTION) {
                        return VIEWTYPE_SELF_QUESTION;
                    } else if (emMessage.getIntAttribute(EMessageConstant.KEY_EXT_TYPE, EMessageConstant.EXT_TYPE_NORMAL) == EMessageConstant.EXT_TYPE_GIFT) {
                        return VIEWTYPE_SELF_GIFT;
                    } else if (emMessage.getBooleanAttribute(EMessageConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)) {
                        return VIEWTYPE_SELF_BIG_EXPRESSION;
                    }
                }
                return VIEWTYPE_SELF_TEXT;
            }
        } else {
            //收到的消息
            if (emMessage.getType() == EMMessage.Type.VOICE) {
                return VIEWTYPE_OPPOSITE_VOICE;
            } else if (emMessage.getType() == EMMessage.Type.IMAGE) {
                return VIEWTYPE_OPPOSITE_IMAGE;
            } else if (emMessage.getType() == EMMessage.Type.VIDEO) {
                return VIEWTYPE_OPPOSITE_VIDEO;
            } else {
                if (emMessage.getType() == EMMessage.Type.TXT) {
                    int extType = emMessage.getIntAttribute(EMessageConstant.KEY_EXT_TYPE, EMessageConstant.EXT_TYPE_NORMAL);
                    if (extType == EMessageConstant.EXT_TYPE_QUESTION) {
                        return VIEWTYPE_OPPOSITE_QUESTION;
                    } else if (extType == EMessageConstant.EXT_TYPE_IMAGECARD) {
                        return VIEWTYPE_OPPOSITE_INFOCARD;
                    } else if (extType == EMessageConstant.EXT_TYPE_GIFT) {
                        return VIEWTYPE_OPPOSITE_GIFT;
                    } else if (emMessage.getBooleanAttribute(EMessageConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)) {
                        return VIEWTYPE_OPPOSITE_BIG_EXPRESSION;
                    }
                }
                return VIEWTYPE_OPPOSITE_TEXT;
            }
        }
    }

    public void scrollToEnd(final boolean smooth) {
        if (getItemCount() > 0 && mRecyclerView != null) {
            mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (smooth) {
                        mRecyclerView.smoothScrollToPosition(getItemCount());
                    } else {
                        mRecyclerView.scrollToPosition(getItemCount());
                    }
                }
            }, 200);
        }

//        if (getItemCount() > 0 && mRecyclerView != null) {
//            mRecyclerView.post(new Runnable() {
//                @Override
//                public void run() {
//                    if (smooth) {
//                        mRecyclerView.smoothScrollToPosition(0);
//                    } else {
//                        mRecyclerView.scrollToPosition(0);
//                    }
//                }
//            });
//        }
    }

    public void scrollToStart(final boolean smooth) {
        if (getItemCount() > 0 && mRecyclerView != null) {
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    if (smooth) {
                        mRecyclerView.smoothScrollToPosition(getItemCount() - 1);
                    } else {
                        mRecyclerView.scrollToPosition(getItemCount() - 1);
                    }
                }
            });
        }
    }


    public void onMessageReceived(EMMessage message) {
        //新消息附加在队列最后面

        int oldSize = getItemCount();
        mEMMessageIndexMap.add(message);
        notifyItemInserted(oldSize);
        scrollToEnd(true);

    }


    public void onLoadAllMsg(List<EMMessage> msgs) {
        if (msgs == null || msgs.size() <= 0) {
            return;
        }
        mEMMessageIndexMap.clear();
        mEMMessageIndexMap.addFront(msgs);
        notifyDataSetChanged();
        scrollToEnd(false);

        for (int i = 0; i < mEMMessageIndexMap.size(); i++) {
            Log.e("msg:", mEMMessageIndexMap.get(i).getBody().toString());
        }
    }


    public void onLoadMoreMsg(List<EMMessage> msgs) {
        if (msgs == null || msgs.size() <= 0) {
            return;
        }
        int oldPos = mEMMessageIndexMap.size();
        mEMMessageIndexMap.addFront(msgs);
        notifyItemRangeInserted(0, msgs.size());
        /*int lodPos = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        notifyDataSetChanged();
        mRecyclerView.scrollToPosition(lodPos);*/

//        mRecyclerView.scrollToPosition(oriPos + msgs.size());
    }


    public void onEMmsgSend(EMMessage msg) {
        //新消息附加在队列最后面
        if (msg != null) {
            int oldSize = getItemCount();
            mEMMessageIndexMap.add(msg);
            notifyItemInserted(oldSize);
//            notifyDataSetChanged();
            scrollToEnd(true);


        }
        if (getItemCount() <= 3) {
            ((LinearLayoutManager) mRecyclerView.getLayoutManager()).setStackFromEnd(false);
        } else {
            ((LinearLayoutManager) mRecyclerView.getLayoutManager()).setStackFromEnd(true);
        }
    }


    public void onEmmsgSendErr(EMMessage msg, int code, String info) {
        int msgIdx = getItemCount() - mEMMessageIndexMap.indexOf(msg) - 1;
        /*if (msgIdx >= 0) {
//            notifyItemChanged(msgIdx);
            RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForLayoutPosition(msgIdx);
            if (holder != null) {
                onBindViewHolder((ChatItemBaseHolder) holder, msgIdx);
            } else {
                notifyItemChanged(msgIdx);
            }
        } else {
        }*/
        notifyDataSetChanged();
//        Log.e("onEmmsgSendErr", "msgIdx=" + msgIdx);
        scrollToEnd(true);

    }


    public void onEmmsgSended(EMMessage msg) {
        final int msgIdx = getItemCount() - mEMMessageIndexMap.indexOf(msg) - 1;

//        final int msgIdx = mEMMessageIndexMap.indexOf(msg);
        /*if (msgIdx >= 0) {
            RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForLayoutPosition(msgIdx);
            if (holder != null) {
                onBindViewHolder((ChatItemBaseHolder) holder, msgIdx);
            } else {
                notifyItemChanged(msgIdx);
            }
            Log.i("onEmmsgSended", "msgIdx=" + msgIdx + ",holder=" + holder);
        } else {
        }*/

        notifyDataSetChanged();
        scrollToEnd(true);
    }


    public void onDeleteMessage(String msgId) {
        int msgIdx = mEMMessageIndexMap.indexOf(msgId);
        if (msgIdx >= 0 && msgIdx < getItemCount()) {
            mEMMessageIndexMap.remove(msgIdx);
            notifyItemRemoved(msgIdx);
        }
    }


    public void onRevocationMsgSuccess(EMMessage msg, EMMessage notifyMsg) {
        //撤回消息成功
        int msgIdx = mEMMessageIndexMap.indexOf(msg);
        if (msgIdx >= 0 && msgIdx < getItemCount()) {
            mEMMessageIndexMap.remove(msgIdx);
            notifyItemRemoved(msgIdx);
            mEMMessageIndexMap.add(notifyMsg, msgIdx);
            notifyItemInserted(msgIdx);

        }

    }


    public void onOppositeRevokeMsg(String msgId) {
        //撤回消息成功
        int msgIdx = mEMMessageIndexMap.indexOf(msgId);
        if (msgIdx >= 0 && msgIdx < getItemCount()) {
            EMMessage newMsg = EMClient.getInstance().chatManager().getMessage(msgId);
            if (newMsg != null) {
                mEMMessageIndexMap.set(msgIdx, newMsg);
            }
            notifyItemChanged(msgIdx);
        }
    }


    public String getLastMsgId() {
        String lastId = null;
        if (getItemCount() > 0) {
            EMMessage lastMsg = getItemAtPosition(0);
            lastId = lastMsg.getMsgId();
            Log.i("lastMsg=", lastMsg.getBody().toString());
        }
        return lastId;
    }
}
