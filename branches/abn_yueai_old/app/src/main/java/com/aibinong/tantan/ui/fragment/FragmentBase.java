package com.aibinong.tantan.ui.fragment;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/4.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.aibinong.tantan.presenter.PresenterBase;
import com.aibinong.tantan.util.DialogUtil;
import com.fatalsignal.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class FragmentBase extends Fragment implements View.OnClickListener {
    protected Handler mDelayLoadHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDelayLoadHandler = new Handler();
    }

    public void postDelayLoad(final Runnable runnable) {
        getActivity().getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                mDelayLoadHandler.post(runnable);
            }
        });
    }

    protected abstract void setupView(@Nullable Bundle savedInstanceState);

    @Override
    public void onClick(View view) {

    }

    protected void showErrDialog(Throwable e) {
        DialogUtil.showDialog(getActivity(), e.getMessage(), true);
    }

    protected void showErrToast(Throwable e) {
        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    public void startActivity(Class<?> intentClass) {
        Intent intent = new Intent(getActivity(), intentClass);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        Field[] mFileds = this.getClass().getDeclaredFields();
        Field[] mFields_public = this.getClass().getFields();

        handlerFields(mFileds);
        handlerFields(mFields_public);
        super.onDestroyView();
    }

    private void handlerFields(Field[] fields) {
        if (fields != null && fields.length > 0) {
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                Object object = null;
                try {
                    object = field.get(this);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                //取消所有的下拉刷新
               /* if (PtrFrameLayout.class.isAssignableFrom(field.getType())) {
                    try {
                        Method method = PtrFrameLayout.class.getMethod("refreshComplete");
                        field.setAccessible(true);
                        Object obj = field.get(this);
                        method.invoke(obj);
                        Log.d(field.getName() + "", "invoke PtrFrameLayout.refreshComplete()");
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } else */
                if (object != null && PresenterBase.class.isInstance(object)) {
                    //PresenterBase.class.isAssignableFrom(field.getType())
                    try {
                        Method method = object.getClass().getMethod("onDestoryView");
                        method.invoke(object);
                        Log.d(field.getName() + "", "invoke PresenterBase.onDestoryView()");
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    InputMethodManager inputManager;

    public void hideKeyboard() {
        if (inputManager == null) {
            inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
