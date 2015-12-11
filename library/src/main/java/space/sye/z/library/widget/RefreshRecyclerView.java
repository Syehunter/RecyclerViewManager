package space.sye.z.library.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import space.sye.z.library.R;
import space.sye.z.library.listener.LoadMoreRecyclerListener;
import space.sye.z.library.listener.OnBothRefreshListener;
import space.sye.z.library.listener.OnLoadMoreListener;
import space.sye.z.library.listener.OnPullDownListener;
import space.sye.z.library.manager.RecyclerMode;

/**
 * Created by Syehunter on 2015/11/22.
 */
public class RefreshRecyclerView extends PtrFrameLayout {

    private Context mContext;
    private RecyclerView recyclerView;
    private PtrFrameLayout.LayoutParams params;
    private LoadMoreRecyclerListener mOnScrollListener;
    private RecyclerMode mode;
    private View mHeaderView;

    public RefreshRecyclerView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        init();
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        recyclerView = new RecyclerView(mContext);
        params = new PtrFrameLayout.LayoutParams(
                PtrFrameLayout.LayoutParams.MATCH_PARENT, PtrFrameLayout.LayoutParams.MATCH_PARENT);
        recyclerView.setLayoutParams(params);
        addView(recyclerView);

        setResistance(1.7f);
        setRatioOfHeaderHeightToRefresh(1.2f);
        setDurationToClose(200);
        setDurationToCloseHeader(1000);
        setKeepHeaderWhenRefresh(true);
        setPullToRefresh(false);
        //ViewPager滑动冲突
        disableWhenHorizontalMove(true);
    }

    public void setAdapter(RecyclerView.Adapter adapter){
        if (null == adapter){
            throw new NullPointerException("adapter cannot be null");
        }
        recyclerView.setAdapter(adapter);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager){
        recyclerView.setLayoutManager(layoutManager);
    }

    public void setItemAnimator(RecyclerView.ItemAnimator itemAnimator){
        if (null == itemAnimator){
            return;
        }
        recyclerView.setItemAnimator(itemAnimator);
    }

    public void setMode(RecyclerMode mode){
        this.mode = mode;
        if (RecyclerMode.NONE == mode || RecyclerMode.BOTTOM == mode){

            setEnabled(false);
        } else {
            setEnabled(true);
        }

        if(null != mOnScrollListener){
            mOnScrollListener.setMode(mode);
        }
    }

    public void addOnScrollListener(RecyclerView.OnScrollListener listener){
        if (null == listener){
            return;
        }
        if (listener instanceof LoadMoreRecyclerListener){
            mOnScrollListener = (LoadMoreRecyclerListener) listener;
            recyclerView.addOnScrollListener(mOnScrollListener);
        } else {
            recyclerView.addOnScrollListener(listener);
        }
    }

    public RecyclerView.LayoutManager getLayoutManager(){
        return recyclerView.getLayoutManager();
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor){
        if (null == decor){
            return;
        }
        recyclerView.addItemDecoration(decor);
    }

    public void setOnBothRefreshListener(final OnBothRefreshListener listener){
        if (RecyclerMode.NONE == mode || null == listener){
            return;
        }

        if (RecyclerMode.BOTH == mode || RecyclerMode.TOP == mode){
            //当前允许下拉刷新
            if (null == mHeaderView){
                //用户未设置过下拉刷新头
                setHeaderView(new RotateLoadingLayout(mContext, RecyclerMode.TOP));
            }

            setPtrHandler(new PtrHandler() {
                @Override
                public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                }

                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    listener.onPullDown();
                }
            });
        }

        if (RecyclerMode.BOTH == mode || RecyclerMode.BOTTOM == mode){
            if (null != mOnScrollListener){
                mOnScrollListener.setOnBothRefreshListener(listener);
            }
        }
    }

    public void setOnPullDownListener(final OnPullDownListener listener){
        if (RecyclerMode.NONE == mode || null == listener){
            return;
        }

        if (RecyclerMode.BOTH == mode || RecyclerMode.TOP == mode){
            //当前允许下拉刷新
            if (null == mHeaderView){
                //用户未设置过下拉刷新头
                setHeaderView(new RotateLoadingLayout(mContext, RecyclerMode.TOP));
            }
            setPtrHandler(new PtrHandler() {

                @Override
                public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                }

                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    listener.onPullDown();
                }
            });
        }
    }

    public void setOnLoadMoreListener(final OnLoadMoreListener listener){
        if (RecyclerMode.NONE == mode || null == listener){
            return;
        }

        if (RecyclerMode.BOTH == mode || RecyclerMode.BOTTOM == mode){
            if (null != mOnScrollListener){
                mOnScrollListener.setOnLoadMoreListener(listener);
            }
        }
    }

    public void setRefreshHeader(View headerView){
        mHeaderView = headerView;
        setHeaderView(mHeaderView);
    }

}
