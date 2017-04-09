package com.buxingzhe.pedestrian.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * 解决ScrollView嵌套ViewPager出现的滑动冲突问题
 */
public class VerticalScrollView extends ScrollView {
    // 滑动距离及坐标
    private float xDistance, yDistance, xLast, yLast;

    private boolean isScrolledToTop = false; // 初始化的时候设置一下值
    private boolean isScrolledToBottom = false;

    public VerticalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public VerticalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VerticalScrollView(Context context) {
        super(context);
        init();
    }

    public void init() {
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                // 手指按下的时候，让其父类交出 onTouch权限
//                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;

                if (xDistance <= yDistance) {
                    return true;
                }

            case MotionEvent.ACTION_UP:
                // 手指松开的时候，让其父类重新获取 onTouch权限
//                getParent().getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }


    private IScrollChangedListener mScrollChangedListener;

    /**
     * 定义监听接口
     */
    public interface IScrollChangedListener {
        void onScrolledToBottom(View view);

        void onScrolledToTop(View view);
    }

    public void setScrollChangedListener(IScrollChangedListener scrollChangedListener) {
        mScrollChangedListener = scrollChangedListener;
    }

    public boolean isScrolledToTop() {
        return isScrolledToTop;
    }

    public void setScrolledToTop(boolean scrolledToTop) {
        isScrolledToTop = scrolledToTop;
    }


    public boolean isScrolledToBottom() {
        return isScrolledToBottom;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //滚动到顶部
        if (getScrollY() == 0) {    // 小心踩坑1: 这里不能是getScrollY() <= 0
            isScrolledToTop = true;
            isScrolledToBottom = false;

        } else if (getScrollY() + getHeight() - getPaddingTop() - getPaddingBottom() == getChildAt(0).getHeight()) {
            // 滚动到底部
            // 小心踩坑2: 这里不能是 >=
            // 小心踩坑3（可能忽视的细节2）：这里最容易忽视的就是ScrollView上下的padding　
            isScrolledToBottom = true;
            isScrolledToTop = false;
        } else {
            isScrolledToTop = false;
            isScrolledToBottom = false;
        }
        notifyScrollChangedListeners();
    }

    private void notifyScrollChangedListeners() {
        if (isScrolledToTop) {
            if (mScrollChangedListener != null) {
                mScrollChangedListener.onScrolledToTop(this);
            }
        } else if (isScrolledToBottom) {
            if (mScrollChangedListener != null) {
                mScrollChangedListener.onScrolledToBottom(this);
            }
        }
    }

    public void scrollToBottom(final View scroll, final View inner) {

        Handler mHandler = new Handler();

        mHandler.post(new Runnable() {
            public void run() {
                if (scroll == null || inner == null) {
                    return;
                }
                int offset = inner.getMeasuredHeight() - scroll.getHeight();
                if (offset < 0) {
                    offset = 0;
                }

                scroll.scrollTo(0, offset);
            }
        });
    }
}
