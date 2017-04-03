package com.aibinong.tantan.ui.adapter.viewholder;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/12/26.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.aibinong.tantan.R;

public abstract class BaseCommonVH<T> extends RecyclerView.ViewHolder {
    public Button btnDelete;
    public BaseCommonVH(View itemView) {
        super(itemView);
        btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
    }

    public abstract void bindData(T data, int dataType);

}