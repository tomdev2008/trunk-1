/**
 *
 */
package com.aibinong.tantan.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.ColorDrawable;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.aibinong.tantan.R;
import com.fatalsignal.inter.CommonLoadListener;
import com.fatalsignal.util.DeviceUtils;

import java.lang.ref.WeakReference;
import java.util.Calendar;

/**
 * @author 杨升迁(yourfriendyang@163.com)
 *         <p>
 *         下午7:59:55 2015年5月14日
 */
public class DialogUtil {
    /**
     * @author 杨升迁(yourfriendyang@163.com)
     *         <p>
     *         下午2:16:32 2015年6月4日
     */
    public interface SimpleOnCompleteListener {
        public void onComplete(Object extra);

        public void onFailed(Object extra);
    }

    static SparseArray<WeakReference<Dialog>> dialogArr = new SparseArray<WeakReference<Dialog>>();

    /**
     * 显示日期选择对话框
     *
     * @param context
     */
    public static void showDatePicker(final Activity context,
                                      OnDateSetListener dateListener) {
        // TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        Calendar calendar = Calendar.getInstance();
        showDatePicker(context, dateListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        // DatePickerDialog dialog = new DatePickerDialog(context, dateListener,
        // calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
        // calendar.get(Calendar.DAY_OF_MONTH));
        // dialog.show();
    }

    public static void showDatePicker(final Activity context,
                                      OnDateSetListener callBack, int year, int monthOfYear,
                                      int dayOfMonth) {
        DatePickerDialog dialog = new DatePickerDialog(context, callBack, year,
                monthOfYear, dayOfMonth) {
            @Override
            protected void onStop() {
                // TODO: 2015/11/26 狗曰的
            }
        };
        dialog.show();
    }

    public static void showTimePicker(Context context,
                                      OnTimeSetListener callBack, boolean is24HourView) {
        Calendar calendar = Calendar.getInstance();
        showTimePicker(context, callBack, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), is24HourView);
    }

    /**
     * @param context
     * @param listener  最终结果以时间戳的形式返回
     * @param defaultTS 默认时间,传小于等于0表示用当前时间
     */
    public static void showDateTimePicker(final Activity context,
                                          long defaultTS, final CommonLoadListener<Long, Long> listener,
                                          final boolean is24HourView) {
        if (defaultTS <= 0) {
            defaultTS = System.currentTimeMillis();
        }
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(defaultTS);

        showDatePicker(
                context,
                new OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, final int year,
                                          final int monthOfYear, final int dayOfMonth) {
                        // 选好年月日后再选时间
                        showTimePicker(context, new OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view,
                                                  int hourOfDay, int minute) {
                                // 全选完了
                                Calendar cal = Calendar.getInstance();
                                cal.set(year, monthOfYear, dayOfMonth,
                                        hourOfDay, minute);
                                listener.onSuccess(cal.getTimeInMillis());
                            }
                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar
                                .get(Calendar.MINUTE), is24HourView);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
    }

    public static void showTimePicker(Context context,
                                      OnTimeSetListener callBack, int hourOfDay, int minute,
                                      boolean is24HourView) {
        TimePickerDialog dialog = new TimePickerDialog(context, callBack,
                hourOfDay, minute, is24HourView) {
            @Override
            protected void onStop() {
                // TODO: 2015/11/26 狗曰的
            }
        };
        dialog.show();
    }

    public static Dialog showIndeternimateDialog(Activity activity, String msg, boolean showCancel, final OnClickListener listener) {
        ProgressDialog indeternimateDialog = null;
        try {
            hideDialog(activity);
            indeternimateDialog = new ProgressDialog(activity);
            ((ProgressDialog) indeternimateDialog).setIndeterminate(true);
            dialogArr.put(activity.hashCode(), new WeakReference<Dialog>(
                    indeternimateDialog));

            if (msg == null) {
                msg = activity.getResources().getString(R.string.abn_yueai_waitting);
            }
            if (showCancel) {
                indeternimateDialog.setButton(Dialog.BUTTON_NEGATIVE, activity.getResources()
                                .getString(R.string.abn_yueai_cancel),
                        new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (listener != null) {
                                    listener.onClick(dialog, which);
                                }
                            }
                        });
            }
            ((ProgressDialog) indeternimateDialog).setMessage(msg);
            indeternimateDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return indeternimateDialog;
    }

    public static Dialog showIndeternimateDialog(Activity activity, String msg) {
        return showIndeternimateDialog(activity, msg, false, null);
    }

    public static Dialog showDialog(Activity activity, String title,
                                    boolean needBtn) {
        return showDialog(activity, null, title, needBtn);
    }

    public static Dialog showDialog(Activity activity, String title,
                                    String msg, boolean needBtn) {
        AlertDialog dialog = null;
        try {
            hideDialog(activity);
            dialog = new AlertDialog.Builder(activity).create();
            dialogArr.put(activity.hashCode(), new WeakReference<Dialog>(dialog));
            if (msg == null) {
                msg = "";
            }
            if (needBtn) {
                dialog.setButton(Dialog.BUTTON_POSITIVE, activity.getResources()
                                .getString(R.string.abn_yueai_confirm),
                        new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
            }
            dialog.setTitle(title);
            dialog.setMessage(msg);

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }

    public static Dialog showExistingDialog(Activity activity, Dialog dialog) {
        hideDialog(activity);
        dialogArr.put(activity.hashCode(), new WeakReference<Dialog>(dialog));
        dialog.show();
        return dialog;
    }

    public static void hideDialog(Activity activity) {
        if (activity == null) {
            return;
        }
        WeakReference<Dialog> indeternimateDialogRef = dialogArr.get(activity
                .hashCode());
        if (indeternimateDialogRef != null) {
            Dialog indeternimateDialog = indeternimateDialogRef.get();
            if (indeternimateDialog != null) {
                indeternimateDialog.dismiss();
                dialogArr.remove(activity.hashCode());
            }
        }

    }


    public static void showSelectPopupWindow(Context context,
                                             String[] btnTitles, final View.OnClickListener listener, View parent) {
        View popContent = LayoutInflater.from(context).inflate(
                R.layout.item_select_popwindow, null);
        final PopupWindow pop = new PopupWindow(popContent,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout ll_popup = (LinearLayout) popContent
                .findViewById(R.id.ll_popup);
        for (int i = 0; i < btnTitles.length; i++) {
            TextView rbtn_i = new TextView(context);
            rbtn_i.setPadding(0,
                    (int) (8 * DeviceUtils.getScreenDensity(context)), 0,
                    (int) (8 * DeviceUtils.getScreenDensity(context)));
            // rbtn_i.setBackgroundDrawable(context.getResources().getDrawable(
            // R.drawable.bt_nobgd));
//            rbtn_i.setBackgroundResource(R.drawable.selector_common_btn_white_bg);
            rbtn_i.setGravity(Gravity.CENTER);
            rbtn_i.setTextColor(context.getResources().getColor(
                    R.color.abn_yueai_text_black));
            rbtn_i.setText(btnTitles[i]);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    (int) (48 * DeviceUtils.getScreenDensity(context)));
            rbtn_i.setLayoutParams(llp);
            rbtn_i.setTag(i + "");
            rbtn_i.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.onClick(v);
                    pop.dismiss();
                }
            });
            ll_popup.addView(rbtn_i);
        }
        popContent.startAnimation(AnimationUtils.loadAnimation(context,
                android.R.anim.fade_in));
        ll_popup.startAnimation(AnimationUtils.loadAnimation(context,
                R.anim.abc_slide_in_bottom));

        pop.setBackgroundDrawable(new ColorDrawable(context.getResources()
                .getColor(R.color.color_common_translucent_black_20)));
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.showAtLocation(parent, Gravity.NO_GRAVITY, 0, 10);
    }
}
