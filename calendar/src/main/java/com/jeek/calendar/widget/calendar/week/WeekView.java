package com.jeek.calendar.widget.calendar.week;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.jeek.calendar.library.R;

import org.joda.time.DateTime;

import java.util.Calendar;


public class WeekView extends View {

    private static final int NUM_COLUMNS = 7;
    private Paint mPaint;
    private int mNormalDayColor;
    /** 所选日期文字颜色*/
    private int mSelectDayTextColor;
    /** 所选日期背景颜色*/
    private int mSelectBGColor;
    /** 所选当前日期背景颜色*/
    private int mSelectBGTodayColor;
    /** 所选当前日期文字颜色*/
    private int mCurrentDayTextColor;

    private int mCurrYear, mCurrMonth, mCurrDay;
    private int mSelYear, mSelMonth, mSelDay;
    private int mColumnSize, mRowSize, mSelectCircleSize;
    /** 日期文字大小*/
    private int mDayTextSize;
    private DateTime mStartDate;
    private DisplayMetrics mDisplayMetrics;
    private OnWeekClickListener mOnWeekClickListener;
    private GestureDetector mGestureDetector;

    public WeekView(Context context, DateTime dateTime) {
        this(context, null, dateTime);
    }

    public WeekView(Context context, TypedArray array, DateTime dateTime) {
        this(context, array, null, dateTime);
    }

    public WeekView(Context context, TypedArray array, AttributeSet attrs, DateTime dateTime) {
        this(context, array, attrs, 0, dateTime);
    }

    public WeekView(Context context, TypedArray array, AttributeSet attrs, int defStyleAttr, DateTime dateTime) {
        super(context, attrs, defStyleAttr);
        initAttrs(array, dateTime);
        initPaint();
        initWeek();
        initGestureDetector();
    }


    private void initAttrs(TypedArray array, DateTime dateTime) {
        if (array != null) {
            mSelectDayTextColor = array.getColor(R.styleable.WeekCalendarView_week_selected_text_color, Color.parseColor("#FFFFFF"));
            mSelectBGColor = array.getColor(R.styleable.WeekCalendarView_week_selected_circle_color, Color.parseColor("#00AE66"));

            mCurrentDayTextColor = array.getColor(R.styleable.WeekCalendarView_week_today_text_color, Color.parseColor("#FF8594"));
            mSelectBGTodayColor = array.getColor(R.styleable.WeekCalendarView_week_selected_circle_today_color, Color.parseColor("#FF8594"));

            mNormalDayColor = array.getColor(R.styleable.WeekCalendarView_week_normal_text_color, Color.parseColor("#232323"));

            mDayTextSize = array.getInteger(R.styleable.WeekCalendarView_week_day_text_size, 15);

        } else {
            mSelectDayTextColor = Color.parseColor("#FFFFFF");
            mSelectBGColor = Color.parseColor("#00AE66");
            mSelectBGTodayColor = Color.parseColor("#FF8594");
            mNormalDayColor = Color.parseColor("#232323");
            mCurrentDayTextColor = Color.parseColor("#FF8594");
            mDayTextSize = 13;
        }
        mStartDate = dateTime;
    }

    private void initPaint() {
        mDisplayMetrics = getResources().getDisplayMetrics();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mDayTextSize * mDisplayMetrics.scaledDensity);

    }

    private void initWeek() {
        Calendar calendar = Calendar.getInstance();
        mCurrYear = calendar.get(Calendar.YEAR);
        mCurrMonth = calendar.get(Calendar.MONTH);
        mCurrDay = calendar.get(Calendar.DATE);
        DateTime endDate = mStartDate.plusDays(7);
        if (mStartDate.getMillis() <= System.currentTimeMillis() && endDate.getMillis() > System.currentTimeMillis()) {
            setSelectYearMonth(mStartDate.getYear(), mStartDate.getMonthOfYear() - 1, mCurrDay);
        } else {
            setSelectYearMonth(mStartDate.getYear(), mStartDate.getMonthOfYear() - 1, mStartDate.getDayOfMonth());
        }
    }

    private void initGestureDetector() {
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                doClickAction((int) e.getX(), (int) e.getY());
                return true;
            }
        });
    }

    public void setSelectYearMonth(int year, int month, int day) {
        mSelYear = year;
        mSelMonth = month;
        mSelDay = day;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = mDisplayMetrics.densityDpi * 200;
        }
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = mDisplayMetrics.densityDpi * 300;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initSize();
        drawThisWeek(canvas);
    }


    private void initSize() {
        mColumnSize = getWidth() / NUM_COLUMNS;
        mRowSize = getHeight();
//        mSelectCircleSize = (int) (mColumnSize / 3.2);
//        while (mSelectCircleSize > mRowSize / 2) {
//            mSelectCircleSize = (int) (mSelectCircleSize / 1.3);
//        }
        mSelectCircleSize = (int) (15 * getResources().getDisplayMetrics().density);
    }

    private void drawThisWeek(Canvas canvas) {
        for (int i = 0; i < 7; i++) {
            DateTime date = mStartDate.plusDays(i);
            int day = date.getDayOfMonth();
            String dayString = String.valueOf(day);
            int startX = (int) (mColumnSize * i + (mColumnSize - mPaint.measureText(dayString)) / 2);
            int startY = (int) (mRowSize / 2 - (mPaint.ascent() + mPaint.descent()) / 2);
            if (day == mSelDay) {
                int startRecX = mColumnSize * i;
                int endRecX = startRecX + mColumnSize;
                if (date.getYear() == mCurrYear && date.getMonthOfYear() - 1 == mCurrMonth && day == mCurrDay) {
                    mPaint.setColor(mSelectBGTodayColor);
                } else {
                    mPaint.setColor(mSelectBGColor);
                }
                canvas.drawCircle((startRecX + endRecX) / 2, mRowSize / 2, mSelectCircleSize, mPaint);
            }
            if (day == mSelDay) {
                mPaint.setColor(mSelectDayTextColor);
            } else if (date.getYear() == mCurrYear && date.getMonthOfYear() - 1 == mCurrMonth && day == mCurrDay && day != mSelDay && mCurrYear == mSelYear) {
                mPaint.setColor(mCurrentDayTextColor);
            } else {
                if(i == 0 || i == 6){
                    // 周六日灰色字体
                    mPaint.setColor(Color.parseColor("#AAAAAA"));
                }else{
                    mPaint.setColor(mNormalDayColor);
                }
            }
            canvas.drawText(dayString, startX, startY, mPaint);
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    private void doClickAction(int x, int y) {
        if (y > getHeight())
            return;
        int column = x / mColumnSize;
        column = Math.min(column, 6);
        DateTime date = mStartDate.plusDays(column);
        clickThisWeek(date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth());
    }

    public void clickThisWeek(int year, int month, int day) {
        if (mOnWeekClickListener != null) {
            mOnWeekClickListener.onClickDate(year, month, day);
        }
        setSelectYearMonth(year, month, day);
        invalidate();
    }

    public void setOnWeekClickListener(OnWeekClickListener onWeekClickListener) {
        mOnWeekClickListener = onWeekClickListener;
    }

    /**
     * 获取当前选择年
     *
     * @return
     */
    public int getSelectYear() {
        return mSelYear;
    }

    /**
     * 获取当前选择月
     *
     * @return
     */
    public int getSelectMonth() {
        return mSelMonth;
    }

    /**
     * 获取当前选择日
     *
     * @return
     */
    public int getSelectDay() {
        return this.mSelDay;
    }


}
