package space.sye.z.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Created by Syehunter on 15/12/29.
 */
public class FlipLoadingLayout extends FrameLayout implements PtrUIHandler {

    public FlipLoadingLayout(Context context) {
        super(context);
    }

    public FlipLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlipLoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {

    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {

    }
}
