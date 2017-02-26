package com.buxingzhe.pedestrian.listen;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;

/**
 * Created by Administrator on 2016/6/20.
 */
public class SwpipeListViewOnScrollListener implements AbsListView.OnScrollListener {
    private SwipeRefreshLayout mSwipeView;
    private AbsListView.OnScrollListener mOnScrollListener;

    public SwpipeListViewOnScrollListener(SwipeRefreshLayout swipeView) {
        mSwipeView = swipeView;
    }
    public SwpipeListViewOnScrollListener(SwipeRefreshLayout swipeView, AbsListView.OnScrollListener onScrollListener) {
        mSwipeView = swipeView;
        mOnScrollListener = onScrollListener;
    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        View firstView = absListView.getChildAt(firstVisibleItem);

        // 当firstVisibleItem是第0位。如果firstView==null说明列表为空，需要刷新;或者top==0说明已经到达列表顶部, 也需要刷新
        if (firstVisibleItem == 0 && (firstView == null || firstView.getTop() == 0)) {
            mSwipeView.setEnabled(true);
        } else {
            mSwipeView.setEnabled(false);
        }
        if (null != mOnScrollListener) {
            mOnScrollListener.onScroll(absListView, firstVisibleItem,
                    visibleItemCount, totalItemCount);
        }
    }
}
