package com.jeek.calendar.widget.calendar.schedule;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.jeek.calendar.library.R;
import com.jeek.calendar.widget.calendar.CalendarUtils;
import com.jeek.calendar.widget.calendar.OnCalendarClickListener;
import com.jeek.calendar.widget.calendar.month.MonthCalendarView;
import com.jeek.calendar.widget.calendar.month.MonthView;
import com.jeek.calendar.widget.calendar.week.WeekCalendarView;
import com.jeek.calendar.widget.calendar.week.WeekView;

import java.util.Calendar;

/**
 * 日历控件布局
 */
public class CalendarLayout extends FrameLayout {

    private final int DEFAULT_MONTH = 0;
    private final int DEFAULT_WEEK = 1;

    private MonthCalendarView mMonthCalendar;
    private WeekCalendarView mWeekCalendar;
    //    private RelativeLayout rlMonthCalendar;
//    private RelativeLayout rlScheduleList;
    private ScrollView mScrollView; //TODO ScrollView  ScheduleRecyclerView

    private int mCurrentSelectYear;
    private int mCurrentSelectMonth;
    private int mCurrentSelectDay;
    private int mRowSize;
    private int mMinDistance;
    private int mAutoScrollDistance;
    private int mDefaultView;
    private float mDownPosition[] = new float[2];
    private boolean mIsScrolling = false;

    private ScheduleState mState;
    private OnCalendarClickListener mOnCalendarClickListener;
    private GestureDetector mGestureDetector;

    private String mCurrrentSelectedDate;

    public CalendarLayout(Context context) {
        this(context, null);
    }

    public CalendarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context.obtainStyledAttributes(attrs, R.styleable.CalendarLayout));
        initDate();
        initGestureDetector();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CalendarLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(context.obtainStyledAttributes(attrs, R.styleable.CalendarLayout));
        initDate();
        initGestureDetector();
    }

    private void initAttrs(TypedArray array) {
        mDefaultView = array.getInt(R.styleable.CalendarLayout_default_view, DEFAULT_MONTH);
        mState = ScheduleState.OPEN;
        mRowSize = getResources().getDimensionPixelSize(R.dimen.week_calendar_height);
        mMinDistance = getResources().getDimensionPixelSize(R.dimen.calendar_min_distance);
        mAutoScrollDistance = getResources().getDimensionPixelSize(R.dimen.auto_scroll_distance);
    }

    private void initGestureDetector() {
        mGestureDetector = new GestureDetector(getContext(), new OnScheduleScrollListener(this));
    }

    private void initDate() {
        Calendar calendar = Calendar.getInstance();
        resetCurrentSelectDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMonthCalendar = (MonthCalendarView) findViewById(R.id.month_calendar);
        mWeekCalendar = (WeekCalendarView) findViewById(R.id.week_calendar);
//        rlMonthCalendar = (RelativeLayout) findViewById(R.id.rlMonthCalendar);
//        rlScheduleList = (RelativeLayout) findViewById(R.id.rlScheduleList);
//        rvScheduleList = (ScrollView) findViewById(R.id.rvScheduleList);


    }

    public void setScrollView(ScrollView scrollView) {
        mScrollView = scrollView;
        bindingMonthAndWeekCalendar();
    }

    private void bindingMonthAndWeekCalendar() {
        mMonthCalendar.setOnCalendarClickListener(mMonthCalendarClickListener);
        mWeekCalendar.setOnCalendarClickListener(mWeekCalendarClickListener);
        // 初始化视图
        if (mDefaultView == DEFAULT_MONTH) {
            mWeekCalendar.setVisibility(INVISIBLE);
            mState = ScheduleState.OPEN;
        } else if (mDefaultView == DEFAULT_WEEK) {
            mWeekCalendar.setVisibility(VISIBLE);
            mState = ScheduleState.CLOSE;
            Calendar calendar = Calendar.getInstance();
            int row = CalendarUtils.getWeekRow(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            mMonthCalendar.setY(-row * mRowSize);
            mScrollView.setY(mScrollView.getY() - 6 * mRowSize); // TODO  6 --> 5
        }
    }

    private void resetCurrentSelectDate(int year, int month, int day) {
        mCurrentSelectYear = year;
        mCurrentSelectMonth = month;
        mCurrentSelectDay = day;
        mCurrrentSelectedDate = (mCurrentSelectYear + "年" + mCurrentSelectMonth + "月" + mCurrentSelectDay + "日 " + CalendarUtils.getWeek(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay));
    }

    public String getCurrrentSelectedDate() {
        return mCurrrentSelectedDate;
    }

    private OnCalendarClickListener mMonthCalendarClickListener = new OnCalendarClickListener() {
        @Override
        public void onClickDate(int year, int month, int day) {
            mWeekCalendar.setOnCalendarClickListener(null);
            int weeks = CalendarUtils.getWeeksAgo(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay, year, month, day);
            resetCurrentSelectDate(year, month, day);
            if (weeks != 0) {
                int position = mWeekCalendar.getCurrentItem() + weeks;
                mWeekCalendar.setCurrentItem(position, false);
            }
            resetWeekView();
            mWeekCalendar.setOnCalendarClickListener(mWeekCalendarClickListener);
        }
    };

    private void resetWeekView() {
        WeekView weekView = mWeekCalendar.getCurrentWeekView();
        if (weekView != null) {
            weekView.setSelectYearMonth(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay);
            weekView.invalidate();
        } else {
            WeekView newWeekView = mWeekCalendar.getWeekAdapter().instanceWeekView(mWeekCalendar.getCurrentItem());
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
            mMonthCalendar.setOnCalendarClickListener(null);
            int months = CalendarUtils.getMonthsAgo(mCurrentSelectYear, mCurrentSelectMonth, year, month);
            resetCurrentSelectDate(year, month, day);
            if (months != 0) {
                int position = mMonthCalendar.getCurrentItem() + months;
                mMonthCalendar.setCurrentItem(position, false);
            }
            resetMonthView();
            mMonthCalendar.setOnCalendarClickListener(mMonthCalendarClickListener);
        }
    };

    private void resetMonthView() {
        MonthView monthView = mMonthCalendar.getCurrentMonthView();
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
        int height = MeasureSpec.getSize(heightMeasureSpec);
        resetViewHeight(mScrollView, height - mRowSize);
        resetViewHeight(this, height);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void resetViewHeight(View view, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams.height != height) {
            layoutParams.height = height;
            view.setLayoutParams(layoutParams);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mDownPosition[0] = ev.getRawX();
                mDownPosition[1] = ev.getRawY();
                mGestureDetector.onTouchEvent(ev);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mIsScrolling) {
            return true;
        }
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                float x = ev.getRawX();
                float y = ev.getRawY();
                float distanceX = Math.abs(x - mDownPosition[0]);
                float distanceY = Math.abs(y - mDownPosition[1]);
                if (distanceY > mMinDistance && distanceY > distanceX * 2.0f) {
                    return (y > mDownPosition[1] && isRecyclerViewTouch()) || (y < mDownPosition[1] && mState == ScheduleState.OPEN);
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private boolean isRecyclerViewTouch() {
        return mState == ScheduleState.CLOSE && (mScrollView.getChildCount() == 0 || mScrollView.getScrollY() == 0);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mDownPosition[0] = event.getRawX();
                mDownPosition[1] = event.getRawY();
                resetCalendarPosition();
                return true;
            case MotionEvent.ACTION_MOVE:
                transferEvent(event);
                mIsScrolling = true;
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                transferEvent(event);
                changeCalendarState();
                resetScrollingState();
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void transferEvent(MotionEvent event) {
        if (mState == ScheduleState.CLOSE) {
            mMonthCalendar.setVisibility(VISIBLE);
            mWeekCalendar.setVisibility(INVISIBLE);
        }
        mGestureDetector.onTouchEvent(event);
    }

    private void changeCalendarState() {
        if (mScrollView.getY() > mRowSize * 2 &&
                mScrollView.getY() < mMonthCalendar.getHeight() - mRowSize) { // 位于中间
            ScheduleAnimation animation = new ScheduleAnimation(this, mState, mAutoScrollDistance);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    changeState();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mScrollView.startAnimation(animation);
        } else if (mScrollView.getY() <= mRowSize * 2) { // 位于顶部
            ScheduleAnimation animation = new ScheduleAnimation(this, ScheduleState.OPEN, mAutoScrollDistance);
            animation.setDuration(50);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mState == ScheduleState.OPEN) {
                        changeState();
                    } else {
                        resetCalendar();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mScrollView.startAnimation(animation);
        } else {
            ScheduleAnimation animation = new ScheduleAnimation(this, ScheduleState.CLOSE, mAutoScrollDistance);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mState == ScheduleState.CLOSE) {
                        mState = ScheduleState.OPEN;
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mScrollView.startAnimation(animation);
        }
    }

    private void resetCalendarPosition() {
        if (mState == ScheduleState.OPEN) {
            mMonthCalendar.setY(0);
            mScrollView.setY(mMonthCalendar.getHeight());
        } else {
            mMonthCalendar.setY(-CalendarUtils.getWeekRow(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay) * mRowSize);
            mScrollView.setY(mRowSize);
        }
    }

    private void resetCalendar() {
        if (mState == ScheduleState.OPEN) {
            mMonthCalendar.setVisibility(VISIBLE);
            mWeekCalendar.setVisibility(INVISIBLE);
        } else {
            mMonthCalendar.setVisibility(INVISIBLE);
            mWeekCalendar.setVisibility(VISIBLE);
        }
    }

    private void changeState() {
        if (mState == ScheduleState.OPEN) {
            mState = ScheduleState.CLOSE;
            mMonthCalendar.setVisibility(INVISIBLE);
            mWeekCalendar.setVisibility(VISIBLE);
            mMonthCalendar.setY((1 - mMonthCalendar.getCurrentMonthView().getWeekRow()) * mRowSize);
        } else {
            mState = ScheduleState.OPEN;
            mMonthCalendar.setVisibility(VISIBLE);
            mWeekCalendar.setVisibility(INVISIBLE);
            mMonthCalendar.setY(0);
        }
    }

    private void resetScrollingState() {
        mDownPosition[0] = 0;
        mDownPosition[1] = 0;
        mIsScrolling = false;
    }

    protected void onCalendarScroll(float distanceY) {
        MonthView monthView = mMonthCalendar.getCurrentMonthView();
        distanceY = Math.min(distanceY, mAutoScrollDistance);
        float calendarDistanceY = distanceY / 5.0f;
        int row = monthView.getWeekRow() - 1;
        int calendarTop = -row * mRowSize;
        int scheduleTop = mRowSize;
        float calendarY = mMonthCalendar.getY() - calendarDistanceY * row;
        calendarY = Math.min(calendarY, 0);
        calendarY = Math.max(calendarY, calendarTop);
        mMonthCalendar.setY(calendarY);
        float scheduleY = mScrollView.getY() - distanceY;
        scheduleY = Math.min(scheduleY, mMonthCalendar.getHeight());
        scheduleY = Math.max(scheduleY, scheduleTop);
        mScrollView.setY(scheduleY);
    }

    public void setOnCalendarClickListener(OnCalendarClickListener onCalendarClickListener) {
        mOnCalendarClickListener = onCalendarClickListener;
    }

    public ScrollView getSchedulerRecyclerView() {
        return mScrollView;
    }

    public MonthCalendarView getMonthCalendar() {
        return mMonthCalendar;
    }

    public WeekCalendarView getWeekCalendar() {
        return mWeekCalendar;
    }

    public int getCurrentSelectYear() {
        return mCurrentSelectYear;
    }

    public int getCurrentSelectMonth() {
        return mCurrentSelectMonth;
    }

    public int getCurrentSelectDay() {
        return mCurrentSelectDay;
    }
}
