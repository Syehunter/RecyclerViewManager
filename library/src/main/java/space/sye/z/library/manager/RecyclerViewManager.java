/**
 * @(#) z.sye.space.refreshrecyclerview.Manager 2015/11/19;
 * <p/>
 * Copyright (c), 2009 深圳孔方兄金融信息服务有限公司（Shenzhen kfxiong
 * Financial Information Service Co. Ltd.）
 * <p/>
 * 著作权人保留一切权利，任何使用需经授权。
 */
package space.sye.z.library.manager;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import space.sye.z.library.adapter.RefreshRecyclerViewAdapter;

/**
 * Created by Syehunter on 2015/11/19.
 */
public class RecyclerViewManager {

    private static final RecyclerViewManager mInstance = new RecyclerViewManager();

    private static RefreshRecyclerAdapterManager refreshRecyclerAdapterManager;

    private RecyclerViewManager(){

    }

    public static RecyclerViewManager getInstance(){
        return mInstance;
    }

    public static RefreshRecyclerAdapterManager with(
            RecyclerView.Adapter adapter, RecyclerView.LayoutManager layoutManager) {
        if (null == adapter) {
            throw new NullPointerException("Couldn't resolve a null object reference of RecyclerView.Adapter");
        }
        if (null == layoutManager) {
            throw new NullPointerException("Couldn't resolve a null object reference of RecyclerView.LayoutManager");
        }
        return getRefreshRecyclerAdapterManager(adapter, layoutManager);
    }

    private static RefreshRecyclerAdapterManager getRefreshRecyclerAdapterManager(
            RecyclerView.Adapter adapter, RecyclerView.LayoutManager layoutManager) {
        refreshRecyclerAdapterManager = new RefreshRecyclerAdapterManager(adapter, layoutManager);
        return refreshRecyclerAdapterManager;
    }

    public static void setMode(RecyclerMode mode){
        if (null == refreshRecyclerAdapterManager) {
            throw new RuntimeException("adapter has not been inited");
        }
        refreshRecyclerAdapterManager.setMode(mode);
    }

    public static void setLayoutManager(RecyclerView.LayoutManager layoutManager){
        if (null == refreshRecyclerAdapterManager) {
            throw new RuntimeException("adapter has not been inited");
        }
        if (layoutManager instanceof GridLayoutManager){
            //如果是header或footer，设置其充满整列
            ((GridLayoutManager)layoutManager).setSpanSizeLookup(
                    new HeaderSapnSizeLookUp(refreshRecyclerAdapterManager.getAdapter(),
                            ((GridLayoutManager) layoutManager).getSpanCount()));
        }
        refreshRecyclerAdapterManager.getAdapter().putLayoutManager(layoutManager);
        refreshRecyclerAdapterManager.getRecyclerView().setLayoutManager(layoutManager);
    }

    /**
     * Replace RecyclerView.ViewHolder.getLayoutPosition() with this
     *
     * @param recyclerView
     * @param holder
     * @return
     */
    public static int getLayoutPosition(RecyclerView recyclerView, RecyclerView.ViewHolder holder) {
        if (null != recyclerView && null != recyclerView.getAdapter() && null != holder) {
            RecyclerView.Adapter mAdapter = recyclerView.getAdapter();
            if (mAdapter instanceof RefreshRecyclerViewAdapter) {
                int headersCount = ((RefreshRecyclerViewAdapter) mAdapter).getHeadersCount();
                if (headersCount > 0) {
                    return holder.getLayoutPosition() - headersCount;
                }
            }
            return holder.getLayoutPosition();
        } else if (null == recyclerView) {
            throw new NullPointerException("RefreshRecyclerView cannot be null");
        } else if (null == recyclerView.getAdapter()) {
            throw new NullPointerException("RecyclerViewAdapter cannot be null");
        } else {
            throw new NullPointerException("RecyclerView.ViewHolde cannot be null");
        }
    }

    /**
     * Replace RecyclerView.ViewHolder.getLayoutPosition() with this
     *
     * @param recyclerView
     * @param holder
     * @return
     */
    public static int getAdapterPosition(RecyclerView recyclerView, RecyclerView.ViewHolder holder) {
        if (null != recyclerView && null != recyclerView.getAdapter() && null != holder) {
            RecyclerView.Adapter mAdapter = recyclerView.getAdapter();
            if (mAdapter instanceof RefreshRecyclerViewAdapter) {
                int headersCount = ((RefreshRecyclerViewAdapter) mAdapter).getHeadersCount();
                if (headersCount > 0) {
                    return holder.getAdapterPosition() - headersCount;
                }
            }
            return holder.getAdapterPosition();
        } else if (null == recyclerView) {
            throw new NullPointerException("RefreshRecyclerView cannot be null");
        } else if (null == recyclerView.getAdapter()) {
            throw new NullPointerException("RecyclerViewAdapter cannot be null");
        } else {
            throw new NullPointerException("RecyclerView.ViewHolder cannot be null");
        }
    }

}
