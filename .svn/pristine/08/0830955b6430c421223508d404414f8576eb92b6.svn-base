package com.aibinong.tantan.ui.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.aibinong.tantan.R;
import com.aibinong.tantan.presenter.AlbumEditPresenter;
import com.bumptech.glide.Glide;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.fatalsignal.util.StringUtils;

import java.io.File;

/**
 * Created by xmuSistone on 2016/5/23.
 */
public class DraggableItemView extends FrameLayout implements AlbumEditPresenter.UploadCallback {
    public interface OnItemClickListener {
        void onItemClick(DraggableItemView itemView, int status);
    }

    public static final int STATUS_LEFT_TOP = 0;
    public static final int STATUS_RIGHT_TOP = 1;
    public static final int STATUS_RIGHT_MIDDLE = 2;
    public static final int STATUS_RIGHT_BOTTOM = 3;
    public static final int STATUS_MIDDLE_BOTTOM = 4;
    public static final int STATUS_LEFT_BOTTOM = 5;

    public static final int SCALE_LEVEL_1 = 1; // 最大状态，缩放比例是100%
    public static final int SCALE_LEVEL_2 = 2; // 中间状态，缩放比例scaleRate
    public static final int SCALE_LEVEL_3 = 3; // 最小状态，缩放比例是smallerRate

    private ImageView mIv_album_img_image;
    private android.support.v4.widget.ContentLoadingProgressBar mCpb_album_img_progress;
    private ImageView mIv_album_img_uploadfailed;
    private ImageView mIv_album_img_add;
    private View mDrag_item_mask_view;

    private int status;
    private float scaleRate = 0.5f;
    private float smallerRate = scaleRate * 0.9f;
    private Spring springX, springY;
    private ObjectAnimator scaleAnimator;
    private boolean hasSetCurrentSpringValue = false;
    private DraggableSquareView parentView;
    private SpringConfig springConfigCommon = SpringConfig.fromOrigamiTensionAndFriction(140, 7);
    private int moveDstX = Integer.MIN_VALUE, moveDstY = Integer.MIN_VALUE;
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public DraggableItemView(Context context) {
        this(context, null);
    }

    public DraggableItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DraggableItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.abn_yueai_album_imageview, this);
        mIv_album_img_image = (ImageView) findViewById(R.id.iv_album_img_image);
        mCpb_album_img_progress = (android.support.v4.widget.ContentLoadingProgressBar) findViewById(R.id.cpb_album_img_progress);
        mIv_album_img_uploadfailed = (ImageView) findViewById(R.id.iv_album_img_uploadfailed);
        mIv_album_img_add = (ImageView) findViewById(R.id.iv_album_img_add);
        mDrag_item_mask_view = (View) findViewById(R.id.drag_item_mask_view);

        mCpb_album_img_progress.hide();
//
//        dialogListener = new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (v.getId() == R.id.pick_image) {
//                    // 从相册选择图片
//                    pickImage();
//                } else {
//                    // 删除
//                    imagePath = null;
//                    imageView.setImageBitmap(null);
//                    addView.setVisibility(View.VISIBLE);
//                    parentView.onDedeleteImage(DraggableItemView.this);
//                }
//            }
//        };

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!hasSetCurrentSpringValue) {
                    adjustImageView();
                    hasSetCurrentSpringValue = true;
                }
            }
        });

        mDrag_item_mask_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(DraggableItemView.this, status);
                }
            }
        });

        initSpring();
    }


    /**
     * 初始化Spring相关
     */
    private void initSpring() {
        SpringSystem mSpringSystem = SpringSystem.create();
        springX = mSpringSystem.createSpring();
        springY = mSpringSystem.createSpring();

        springX.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                int xPos = (int) spring.getCurrentValue();
                setScreenX(xPos);
            }
        });

        springY.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                int yPos = (int) spring.getCurrentValue();
                setScreenY(yPos);
            }
        });

        springX.setSpringConfig(springConfigCommon);
        springY.setSpringConfig(springConfigCommon);
    }

    /**
     * 调整ImageView的宽度和高度各为FrameLayout的一半
     */
    private void adjustImageView() {
        if (status != STATUS_LEFT_TOP) {
            mIv_album_img_image.setScaleX(scaleRate);
            mIv_album_img_image.setScaleY(scaleRate);

            mDrag_item_mask_view.setScaleX(scaleRate);
            mDrag_item_mask_view.setScaleY(scaleRate);
        }

        setCurrentSpringPos(getLeft(), getTop());
    }

    public void setScaleRate(float scaleRate) {
        this.scaleRate = scaleRate;
        this.smallerRate = scaleRate * 0.9f;
    }

    /**
     * 从一个状态切换到另一个状态
     */
    public void switchPosition(int toStatus) {
        if (this.status == toStatus) {
            throw new RuntimeException("程序错乱");
        }

        if (toStatus == STATUS_LEFT_TOP) {
            scaleSize(SCALE_LEVEL_1);
        } else if (this.status == STATUS_LEFT_TOP) {
            scaleSize(SCALE_LEVEL_2);
        }

        this.status = toStatus;
        Point point = parentView.getOriginViewPos(status);
        this.moveDstX = point.x;
        this.moveDstY = point.y;
        animTo(moveDstX, moveDstY);
    }

    public void animTo(int xPos, int yPos) {
        springX.setEndValue(xPos);
        springY.setEndValue(yPos);
    }

    /**
     * 设置缩放大小
     */
    public void scaleSize(int scaleLevel) {
        float rate = scaleRate;
        if (scaleLevel == SCALE_LEVEL_1) {
            rate = 1.0f;
        } else if (scaleLevel == SCALE_LEVEL_3) {
            rate = smallerRate;
        }

        if (scaleAnimator != null && scaleAnimator.isRunning()) {
            scaleAnimator.cancel();
        }

        scaleAnimator = ObjectAnimator
                .ofFloat(this, "custScale", mIv_album_img_image.getScaleX(), rate)
                .setDuration(200);
        scaleAnimator.setInterpolator(new DecelerateInterpolator());
        scaleAnimator.start();
    }

    public void saveAnchorInfo(int downX, int downY) {
        int halfSide = getMeasuredWidth() / 2;
        moveDstX = downX - halfSide;
        moveDstY = downY - halfSide;
    }

    /**
     * 真正开始动画
     */
    public void startAnchorAnimation() {
        if (moveDstX == Integer.MIN_VALUE || moveDstX == Integer.MIN_VALUE) {
            return;
        }

        springX.setOvershootClampingEnabled(true);
        springY.setOvershootClampingEnabled(true);
        animTo(moveDstX, moveDstY);
        scaleSize(DraggableItemView.SCALE_LEVEL_3);
    }

    public void setScreenX(int screenX) {
        this.offsetLeftAndRight(screenX - getLeft());
    }

    public void setScreenY(int screenY) {
        this.offsetTopAndBottom(screenY - getTop());
    }

    public int computeDraggingX(int dx) {
        this.moveDstX += dx;
        return this.moveDstX;
    }

    public int computeDraggingY(int dy) {
        this.moveDstY += dy;
        return this.moveDstY;
    }

    /**
     * 设置当前spring位置
     */
    private void setCurrentSpringPos(int xPos, int yPos) {
        springX.setCurrentValue(xPos);
        springY.setCurrentValue(yPos);
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setParentView(DraggableSquareView parentView) {
        this.parentView = parentView;
    }

    public void onDragRelease() {
        if (status == DraggableItemView.STATUS_LEFT_TOP) {
            scaleSize(DraggableItemView.SCALE_LEVEL_1);
        } else {
            scaleSize(DraggableItemView.SCALE_LEVEL_2);
        }

        springX.setOvershootClampingEnabled(false);
        springY.setOvershootClampingEnabled(false);
        springX.setSpringConfig(springConfigCommon);
        springY.setSpringConfig(springConfigCommon);

        Point point = parentView.getOriginViewPos(status);
        setCurrentSpringPos(getLeft(), getTop());
        this.moveDstX = point.x;
        this.moveDstY = point.y;
        animTo(moveDstX, moveDstY);
    }


    // 以下两个get、set方法是为自定义的属性动画CustScale服务，不能删
    public void setCustScale(float scale) {
        mIv_album_img_image.setScaleX(scale);
        mIv_album_img_image.setScaleY(scale);

        mDrag_item_mask_view.setScaleX(scale);
        mDrag_item_mask_view.setScaleY(scale);
    }

    public float getCustScale() {
        return mIv_album_img_image.getScaleX();
    }

    public void updateEndSpringX(int dx) {
        springX.setEndValue(springX.getEndValue() + dx);
    }

    public void updateEndSpringY(int dy) {
        springY.setEndValue(springY.getEndValue() + dy);
    }

    public boolean isDraggable() {
        return !isEmpty();
    }


    private String mImageUrl;
    private File mLocalFile;
    private float mProgress = 0;
    private boolean mUploadError = false;
    private boolean mUploadComplete = false;

    public boolean isUploadError() {
        return mUploadError;
    }

    public boolean isUploadComplete() {
        return mUploadComplete;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    private void bindData() {

        if (!StringUtils.isEmpty(mImageUrl)) {
            //上传完了
            if (mLocalFile != null) {
                Glide.with(getContext()).load(mLocalFile).into(mIv_album_img_image);
            } else {
                Glide.with(getContext()).load(mImageUrl).into(mIv_album_img_image);
            }
            mIv_album_img_uploadfailed.setVisibility(INVISIBLE);
            mCpb_album_img_progress.hide();
            mIv_album_img_add.setVisibility(INVISIBLE);
        } else if (mLocalFile != null) {
            //上传中或者上传失败
            Glide.with(getContext()).load(mLocalFile).into(mIv_album_img_image);
            mIv_album_img_add.setVisibility(INVISIBLE);
            if (mUploadError) {
                mIv_album_img_uploadfailed.setVisibility(VISIBLE);
                mCpb_album_img_progress.hide();
            } else {
                mIv_album_img_uploadfailed.setVisibility(INVISIBLE);
                mCpb_album_img_progress.show();
                mCpb_album_img_progress.setProgress((int) (mProgress * 100));
            }
        } else {
            //没有图
            mIv_album_img_image.setImageResource(0);
            mCpb_album_img_progress.hide();
            mIv_album_img_add.setVisibility(VISIBLE);
            mIv_album_img_uploadfailed.setVisibility(INVISIBLE);
        }
    }

    public void bindImage(String imageUrl) {
        mLocalFile = null;
        mImageUrl = imageUrl;
        mUploadError = false;
        bindData();
    }

    public void bindFile(File source) {
        mLocalFile = source;
        mImageUrl = null;
        bindData();
    }

    public File getLocalFile() {
        return mLocalFile;
    }

    public boolean isEmpty() {
        if (mLocalFile == null && mImageUrl == null) {
            return true;
        }
        return false;
    }

    public void clearImage() {
        mLocalFile = null;
        mImageUrl = null;
        bindData();
    }

    @Override
    public void onProgress(File file, long readedSize, long totalSize) {
        mProgress = readedSize / (totalSize * 1.0f);
        if (mProgress < 0) {
            mProgress = 0;
        }
        if (mProgress >= 100) {
            mProgress = 99.99f;
        }
        mUploadError = false;
        mIv_album_img_uploadfailed.setVisibility(INVISIBLE);
        mCpb_album_img_progress.show();
        mCpb_album_img_progress.setProgress((int) (mProgress * 100));
    }

    @Override
    public void onError(Throwable e) {
        mUploadError = true;
        bindData();
    }

    @Override
    public void onComplete(String url) {
        mUploadComplete = true;
        mUploadError = false;
        mImageUrl = url;
        bindData();
    }
}

