package space.sye.z.library.widget;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import space.sye.z.library.R;
import space.sye.z.library.manager.RecyclerMode;

/**
 * Created by Syehunter on 2015/11/22.
 */
public class RotateLoadingLayout extends RefreshLoadingLayout {

    private RelativeLayout mRootView;
    private TextView mRefreshText;
    private TextView mRefreshTime;
    private ImageView mImage;

    private String mRefreshing;
    private String mLoading;
    private String mComplete;
    private String mLastUpdateTime;
    private Matrix mImageMatrix;
    private RotateAnimation mRotateAnimation;

    private Drawable imageDrawable;
    private LayoutParams layoutParams;

    private boolean mUseIntrinsicAnimation;

    public RotateLoadingLayout(Context context, RecyclerMode mode) {
        super(context, mode);

    }

    @Override
    protected void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.loadinglayout, this, false);
        mRootView = (RelativeLayout) view.findViewById(R.id.fl_root);
        mRefreshText = (TextView) view.findViewById(R.id.tv_refresh);
        mRefreshTime = (TextView) view.findViewById(R.id.tv_refresh_time);
        mImage = (ImageView) view.findViewById(R.id.iv_image);

        layoutParams = (LayoutParams) mRootView.getLayoutParams();

        mRefreshing = mContext.getResources().getString(R.string.refreshing);
        mLoading = mContext.getResources().getString(R.string.loading);
        mComplete = mContext.getResources().getString(R.string.complete);
        mLastUpdateTime = getLastTime();
        if (!TextUtils.isEmpty(mLastUpdateTime)) {
            mRefreshTime.setText(mLastUpdateTime);
        }

        imageDrawable = mContext.getResources().getDrawable(R.mipmap.default_ptr_rotate);
        mImage.setScaleType(ImageView.ScaleType.MATRIX);
        mImageMatrix = new Matrix();
        mImage.setImageMatrix(mImageMatrix);

        mRotateAnimation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
        mRotateAnimation.setDuration(ROTATION_ANIMATION_DURATION);
        mRotateAnimation.setRepeatCount(Animation.INFINITE);
        mRotateAnimation.setRepeatMode(Animation.RESTART);


        addView(view);
    }

    @Override
    protected void onRefreshImpl() {
        mImage.setImageDrawable(imageDrawable);
        if (null != mImage.getAnimation()){
            mImage.clearAnimation();
        }
        mImage.startAnimation(mRotateAnimation);

        if (RecyclerMode.BOTH == mode || RecyclerMode.TOP == mode) {
            if (null != mRefreshText) {
                mRefreshText.setText(mRefreshing);
            }
            if (null != mRefreshTime) {
                if (TextUtils.isEmpty(mLastUpdateTime)) {
                    mRefreshTime.setVisibility(View.GONE);
                } else {
                    mRefreshTime.setVisibility(View.VISIBLE);
                }
            }
        } else {
            if (null != mRefreshText) {
                mRefreshText.setText(mLoading);
            }
        }
    }

    @Override
    protected void onResetImpl() {
        if (null != mRefreshText) {
            mRefreshText.setText(mComplete);
        }
        mImage.setImageDrawable(getResources().getDrawable(R.mipmap.refresh_complete));
        mImage.setVisibility(View.VISIBLE);

        mImage.clearAnimation();
        resetImageRotation();

        mRefreshTime.setVisibility(GONE);
    }

    public final void setHeight(int height) {
        layoutParams.height = height;
        requestLayout();
    }

    public final void setWidth(int width) {
        layoutParams.width = width;
        requestLayout();
    }

    public final int getContentSize() {
        return mRootView.getHeight();
    }

    public void setLayoutPadding(int left, int top, int right, int bottom) {
        layoutParams.setMargins(left, top, right, bottom);
        setLayoutParams(layoutParams);
    }

    private void resetImageRotation() {
        if (null != mImageMatrix) {
            mImageMatrix.reset();
            mImage.setImageMatrix(mImageMatrix);
        }
    }

    private String getLastTime() {
        SharedPreferences sp = mContext.getSharedPreferences("RefreshRecycleView", Activity.MODE_PRIVATE);
        String lastUpdateTime = sp.getString("LastUpdateTime", "");
        return lastUpdateTime;
    }
}
