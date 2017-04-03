package com.aibinong.tantan.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.aibinong.tantan.R;
import com.aibinong.yueaiapi.pojo.PayTypeEntity;
import com.bumptech.glide.Glide;
import com.fatalsignal.util.StringUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yourfriendyang on 2016/4/22.
 */
public class CrowdfundingPayTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PayTypeEntity> mPayTypes;
    private float mRmbPrice;
    private PayTypeEntity mCurrentPayType;

    public PayTypeEntity getCurrentPayType() {
        return mCurrentPayType;
    }

    public void setPayTypes(List<PayTypeEntity> types) {
        mPayTypes = types;
        if (mCurrentPayType == null && mPayTypes != null && mPayTypes.size() > 0) {
            mCurrentPayType = mPayTypes.get(0);
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.abn_yueai_item_paytype, parent, false);
        PayTypeHolder holder = new PayTypeHolder(convertView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PayTypeEntity payType = mPayTypes.get(position);
        ((PayTypeHolder) holder).bindData(payType, position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        int count = getPayTypeCount();
        return count;
    }

    public int getPayTypeCount() {
        return mPayTypes == null ? 0 : mPayTypes.size();
    }


    public void setRmbPrice(float rmbPrice) {
        mRmbPrice = rmbPrice;
        notifyDataSetChanged();
    }


    class PayTypeHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
        @Bind(R.id.iv_item_selectpay_icon)
        ImageView mIvItemSelectpayIcon;
        @Bind(R.id.tv_item_selectpay_name)
        TextView mTvItemSelectpayName;
        @Bind(R.id.tv_item_selectpay_totalbill)
        TextView mTvItemSelectpayTotalbill;
        @Bind(R.id.ll_item_selectpay_name)
        LinearLayout mLlItemSelectpayName;
        @Bind(R.id.rbtn_item_selectpay_check)
        RadioButton mRbtnItemSelectpayCheck;
        @Bind(R.id.tv_item_selectpay_needamount)
        TextView mTvItemSelectpayNeedamount;
        private PayTypeEntity mPayType;

        public PayTypeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);


        }


        public void bindData(PayTypeEntity payType, int pos) {
            mPayType = payType;

            mTvItemSelectpayName.setText(mPayType.payName);
            Glide.with(itemView.getContext()).load(mPayType.logo).placeholder(null).into(mIvItemSelectpayIcon);

            mTvItemSelectpayName.setVisibility(View.VISIBLE);
            mTvItemSelectpayTotalbill.setVisibility(View.GONE);

            {
                if (!StringUtils.isEmpty(mPayType.payTitle)) {
                    mTvItemSelectpayTotalbill.setVisibility(View.VISIBLE);
                    mTvItemSelectpayTotalbill.setText(mPayType.payTitle);
                }
                //其它
                mTvItemSelectpayNeedamount.setText(String.format("￥%.2f", mRmbPrice));
            }


            mRbtnItemSelectpayCheck.setOnCheckedChangeListener(null);
            if (mPayType.equals(mCurrentPayType)) {
                mRbtnItemSelectpayCheck.setChecked(true);
            } else

            {
                mRbtnItemSelectpayCheck.setChecked(false);
            }

            mRbtnItemSelectpayCheck.setOnCheckedChangeListener(this);
        }

        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param buttonView The compound button view whose state has changed.
         * @param isChecked  The new checked state of buttonView.
         */
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                mCurrentPayType = mPayType;
            }
            notifyDataSetChanged();
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            mRbtnItemSelectpayCheck.performClick();
        }

    }

}
