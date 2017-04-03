package com.aibinong.tantan.ui.widget;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/10/27.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.aibinong.tantan.R;

public class MainFragArcBg extends View {
    Drawable gradientDrawable;

    public MainFragArcBg(Context context) {
        super(context);
        setup();
    }

    public MainFragArcBg(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public MainFragArcBg(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MainFragArcBg(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setup();
    }

    private void setup() {
        gradientDrawable = getResources().getDrawable(R.drawable.abn_yueai_gradient_vertical_red);
    }

    private Paint paint, paint2;

    @Override
    public void draw(Canvas canvas) {
        if (getWidth() > 0 && getHeight() > 0) {
            Bitmap bmp = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas2 = new Canvas(bmp);
            super.draw(canvas2);


            Path path = new Path();
            path.moveTo(0, getHeight());
            path.quadTo(getWidth() / 2.0f, getHeight() *2/ 3.0f, getWidth(), getHeight() *2/ 3.0f);
            path.moveTo(getWidth(),0);
            path.moveTo(0,0);
            path.close();

            if (paint == null) {
                paint = new Paint();
                paint.setAntiAlias(true);
                paint.setColor(Color.WHITE);
                paint.setAntiAlias(true);
//                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

            }

            if (paint2 == null) {
                paint2 = new Paint();
            }

            canvas2.drawPath(path, paint);
            canvas.drawBitmap(bmp, 0, 0, paint2);
        } else {
            super.draw(canvas);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
//        canvas.translate(0, getMeasuredHeight());
        gradientDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
        gradientDrawable.draw(canvas);
        canvas.restore();

    }

}
