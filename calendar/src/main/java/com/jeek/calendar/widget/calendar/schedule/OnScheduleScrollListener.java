package com.jeek.calendar.widget.calendar.schedule;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class OnScheduleScrollListener extends GestureDetector.SimpleOnGestureListener {

    private CalendarLayout mScheduleLayout;

    public OnScheduleScrollListener(CalendarLayout scheduleLayout) {
        mScheduleLayout = scheduleLayout;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        mScheduleLayout.onCalendarScroll(distanceY);
        return true;
    }
}
