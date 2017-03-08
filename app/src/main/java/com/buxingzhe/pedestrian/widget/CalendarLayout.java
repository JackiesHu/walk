package com.buxingzhe.pedestrian.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import com.buxingzhe.lib.util.Log;
import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.utils.SystemUtils;
import com.jeek.calendar.widget.calendar.CalendarUtils;
import com.jeek.calendar.widget.calendar.OnCalendarClickListener;
import com.jeek.calendar.widget.calendar.month.MonthCalendarView;
import com.jeek.calendar.widget.calendar.month.MonthView;
import com.jeek.calendar.widget.calendar.week.WeekCalendarView;
import com.jeek.calendar.widget.calendar.week.WeekView;

import java.util.Calendar;


public class CalendarLayout extends LinearLayout {

    private MonthCalendarView mMonthCalendarView;
    private WeekCalendarView mWeekCalendarView;
    private LinearLayout contentView;

    private int mTouchSlop;
    private int swipHeight;// contentView可以移动的距离
    private int translatDirection = 0;// calendarView可以移动的距离

    private float downY;
    private float mLastY;
    private boolean mIsScrolling = false;

    private Context mContext;

    private int mCurrentSelectYear;
    private int mCurrentSelectMonth;
    private int mCurrentSelectDay;
    private String mCurrrentSelectedDate;

    /**
     * 设置当前选择的位置
     *
     * @param selectPosition
     */
    public void setSelectPosition(int selectPosition) {
        int line = (selectPosition + 6) % 6;
        translatDirection = (line - 1) * SystemUtils.dip2px(mContext, 48);
    }

    public CalendarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setOrientation(LinearLayout.VERTICAL);
        mContext = context;
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMonthCalendarView = (MonthCalendarView) findViewById(R.id.month_calendar);
        mWeekCalendarView = (WeekCalendarView) findViewById(R.id.week_calendar);
        contentView = (LinearLayout) findViewById(R.id.walk_content);

        mMonthCalendarView.setOnCalendarClickListener(mMonthCalendarClickListener);
        mWeekCalendarView.setOnCalendarClickListener(mWeekCalendarClickListener);
        VerticalScrollView mVerticalScrollView = (VerticalScrollView) contentView.getChildAt(1);
        mVerticalScrollView.setScrollChangedListener(new MineScrollViewChangedListener());

        initData();
    }

    private void initData() {
        Calendar todayCalendar = mMonthCalendarView.getTodayCalendar();
        mCurrentSelectYear = todayCalendar.get(Calendar.YEAR);
        mCurrentSelectMonth = todayCalendar.get(Calendar.MONTH);
        mCurrentSelectDay = todayCalendar.get(Calendar.DATE);
        resetCurrentSelectDate(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay);
        translatDirection = (getPosition(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay) - 1) * SystemUtils.dip2px(mContext, 48);
    }


    private OnCalendarClickListener mMonthCalendarClickListener = new OnCalendarClickListener() {
        @Override
        public void onClickDate(int year, int month, int day) {
            mWeekCalendarView.setOnCalendarClickListener(null);
            int weeks = CalendarUtils.getWeeksAgo(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay, year, month, day);
            resetCurrentSelectDate(year, month, day);
            if (weeks != 0) {
                int position = mWeekCalendarView.getCurrentItem() + weeks;
                mWeekCalendarView.setCurrentItem(position, false);
            }
            resetWeekView();
            mWeekCalendarView.setOnCalendarClickListener(mWeekCalendarClickListener);
        }
    };

    private void resetWeekView() {
        WeekView weekView = mWeekCalendarView.getCurrentWeekView();
        if (weekView != null) {
            weekView.setSelectYearMonth(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay);
            weekView.invalidate();
        } else {
            WeekView newWeekView = mWeekCalendarView.getWeekAdapter().instanceWeekView(mWeekCalendarView.getCurrentItem());
            newWeekView.setSelectYearMonth(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay);
            newWeekView.invalidate();
        }
        if (mOnCalendarClickListener != null) {
            mOnCalendarClickListener.onClickDate(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay);
        }
    }

    private OnCalendarClickListener mWeekCalendarClickListener = new OnCalendarClickListener() {
        @Override
        public void onClickDate(int year, int month, int day) {
            mMonthCalendarView.setOnCalendarClickListener(null);
            int months = CalendarUtils.getMonthsAgo(mCurrentSelectYear, mCurrentSelectMonth, year, month);
            resetCurrentSelectDate(year, month, day);
            if (months != 0) {
                int position = mMonthCalendarView.getCurrentItem() + months;
                mMonthCalendarView.setCurrentItem(position, false);
            }
            resetMonthView();
            mMonthCalendarView.setOnCalendarClickListener(mMonthCalendarClickListener);
        }
    };

    private void resetMonthView() {
        MonthView monthView = mMonthCalendarView.getCurrentMonthView();
        if (monthView != null) {
            monthView.setSelectYearMonth(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay);
            monthView.invalidate();
        }
        if (mOnCalendarClickListener != null) {
            mOnCalendarClickListener.onClickDate(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightSpec = MeasureSpec.makeMeasureSpec(
                getMeasuredHeight() - SystemUtils.dip2px(mContext, 48),
                MeasureSpec.EXACTLY);
        contentView.measure(widthMeasureSpec, heightSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        swipHeight = mMonthCalendarView.getMeasuredHeight() - SystemUtils.dip2px(mContext, 48);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mIsScrolling) {
            return true;
        }
        final int action = ev.getAction();
        float y = ev.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastY = downY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mLastY;
//                Log.i("dy= "+dy);
                if (Math.abs(dy) > mTouchSlop) {
                    if ((dy > 0 && contentView.getTranslationY() <= 0)
                            || (dy < 0 && contentView.getTranslationY() >= -swipHeight)) {
                        mLastY = y;
//                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastY = downY = y;
                return true;
            case MotionEvent.ACTION_MOVE:
                mIsScrolling = true;
                float dy = y - mLastY;
                // 向下滑动
                if (dy > 0 && contentView.getTranslationY() + dy >= 0) {
                    contentView.setTranslationY(0);
                    translationSwipView();
                    return true;
                }

                //向上滑动
                if (dy < 0 && contentView.getTranslationY() + dy <= -swipHeight) {
                    contentView.setTranslationY(-swipHeight);
                    translationSwipView();
                    return true;
                }
                // 滑动中
                contentView.setTranslationY(contentView.getTranslationY() + dy);
                translationSwipView();
                mLastY = y;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mIsScrolling = false;
//                if (contentView.getTranslationY() == 0
//                        || contentView.getTranslationY() == swipHeight) {
//                    break;
//                }
                if (event.getY() - downY > 0) {
                    animateShow();
                } else {
                    animateHide();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 移动日历视图
     */
    private void translationSwipView() {
        float percent = contentView.getTranslationY() * 1.0f / swipHeight;
        mMonthCalendarView.setTranslationY(translatDirection * percent);
        mMonthCalendarView.setVisibility(View.VISIBLE);
        mWeekCalendarView.setVisibility(View.INVISIBLE);
    }

    /**
     * 显示日历
     */
    private void animateShow() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(contentView,
                "translationY", contentView.getTranslationY(), 0f);
        objectAnimator.setDuration(150);
        objectAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (Float) animation.getAnimatedValue();
                float percent = currentValue * 1.0f / swipHeight;
                mMonthCalendarView.setTranslationY(translatDirection * percent);
            }
        });
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mMonthCalendarView.setVisibility(View.VISIBLE);
                mWeekCalendarView.setVisibility(View.INVISIBLE);
            }

        });
        objectAnimator.start();
    }

    /**
     * 隐藏日历
     */
    private void animateHide() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(contentView,
                "translationY", contentView.getTranslationY(), -swipHeight);
        objectAnimator.setDuration(150);
        objectAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (Float) animation.getAnimatedValue();
                float percent = currentValue * 1.0f / swipHeight;
                mMonthCalendarView.setTranslationY(translatDirection * percent);
            }
        });
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mMonthCalendarView.setVisibility(View.INVISIBLE);
                mWeekCalendarView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        objectAnimator.start();
    }

    private OnCalendarClickListener mOnCalendarClickListener;

    public void setOnCalendarClickListener(OnCalendarClickListener onCalendarClickListener) {
        mOnCalendarClickListener = onCalendarClickListener;
    }

    private OnCalendarClickListener mCalendarViewClickListener = new OnCalendarClickListener() {
        @Override
        public void onClickDate(int year, int month, int day) {
            resetCurrentSelectDate(year, month, day);

            if (mOnCalendarClickListener != null) {
                mOnCalendarClickListener.onClickDate(year, month, day);
            }
        }
    };

    private void resetCurrentSelectDate(int year, int month, int day) {
        mCurrentSelectYear = year;
        mCurrentSelectMonth = month;
        mCurrentSelectDay = day;
        mCurrrentSelectedDate = (mCurrentSelectYear + "年" + (mCurrentSelectMonth + 1) + "月" + mCurrentSelectDay + "日 " + CalendarUtils.getWeek(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay));
    }

    public String getCurrrentSelectedDate() {
        return mCurrrentSelectedDate;
    }

    public void skipTodayCalendar() {
        mMonthCalendarView.setTodayToView();
    }

    /**
     * 获取当前选择日期所在日历表中的行数
     *
     * @return
     */
    public int getPosition(int year, int month, int day) {
        return CalendarUtils.getWeekRow(year, month, day) + 1;
    }

    private class MineScrollViewChangedListener implements VerticalScrollView.IScrollChangedListener {

        @Override
        public void onScrolledToBottom(View view) {
//            view.getParent().getParent().requestDisallowInterceptTouchEvent(true);
        }

        @Override
        public void onScrolledToTop(View view) {
//            view.getParent().getParent().requestDisallowInterceptTouchEvent(false);
            Log.i(view.getParent().getParent().toString());
        }
    }
}
