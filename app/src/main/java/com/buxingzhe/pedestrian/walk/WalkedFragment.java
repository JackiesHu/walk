package com.buxingzhe.pedestrian.walk;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseFragment;
import com.buxingzhe.pedestrian.widget.MaterialSpinnerLayout;
import com.buxingzhe.pedestrian.widget.VerticalScrollView;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jeek.calendar.widget.calendar.OnCalendarClickListener;
import com.jeek.calendar.widget.calendar.schedule.CalendarLayout;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by quanjing on 2017/2/23.
 */
public class WalkedFragment extends BaseFragment {
    private TextView mCurrentSelectedDate;
    private LineChartView mLineChartView;

    private MaterialSpinner mMaterialSpinner;
    private MaterialSpinnerLayout mMaterialSpinnerLayout;

    private CalendarLayout mCalendarLayout;
    private VerticalScrollView mScrollView;

    private String[] WALK_SPINNER_DATA;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_walk, container, false);
        mContext = getContext();
        findId(view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onClick();
        setData();
    }

    private void findId(View view) {
        mCurrentSelectedDate = (TextView) view.findViewById(R.id.currentSelectedDate);
        mLineChartView = (LineChartView) view.findViewById(R.id.linechartview);
        mMaterialSpinner = (MaterialSpinner) view.findViewById(R.id.walk_spinner);
        mMaterialSpinnerLayout = (MaterialSpinnerLayout) view.findViewById(R.id.walk_spinner_layout);
        mScrollView = (VerticalScrollView) view.findViewById(R.id.scrollView);
        mCalendarLayout = (CalendarLayout) view.findViewById(R.id.calendar_layout);

    }

    private void onClick() {
        mLineChartView.setOnValueTouchListener(new MineLineChartOnValueSelectListener());
        mMaterialSpinner.setOnItemSelectedListener(new MineOnItemSelectedListener());
        mCalendarLayout.setOnCalendarClickListener(new MineCalendarClickListener());
    }

    private void setData() {
        mCalendarLayout.setScrollView(mScrollView);
        setSpinnerData();
        setChartData();
    }

    /**
     * 初始化Spinner
     */
    private void setSpinnerData() {
        WALK_SPINNER_DATA = getActivity().getResources().getStringArray(R.array.walk_spinner);
        mMaterialSpinner.setItems(WALK_SPINNER_DATA);
    }

    /**
     * 初始化折线图
     */
    private void setChartData() {

        String[] XData = {"2017年3月", "2月", "1月", "2016年12月", "11月", "10月", "9月", "8月", "7月", "6月", "5月"};
        int[] YData = {1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000, 11000, 12000,};
//        String[] YData = {"5000","10000","15000","20000"};
        int[] score = {5200, 4000, 7000, 0, 2000, 3000, 10000, 12000, 15000};


        //1. 获取XY轴的标注
        List<AxisValue> mAxisXValue = new ArrayList<AxisValue>();
        for (int i = 0; i < XData.length; i++) {
            mAxisXValue.add(new AxisValue(i).setLabel(XData[i]));
        }
        List<AxisValue> mAxisYValue = new ArrayList<AxisValue>();
        for (int i = 0; i < YData.length; i++) {
            mAxisYValue.add(new AxisValue(i).setLabel(YData[i] + ""));
        }
        //2. 获取坐标点
        List<PointValue> mPointValue = new ArrayList<PointValue>();
        for (int i = 0; i < score.length; i++) {
            mPointValue.add(new PointValue(i, score[i]));
        }

        //3. 折线的属性
        // 创建折线并设置折线的颜色
        Line line = new Line(mPointValue).setColor(Color.parseColor("#00AE66"));
        //折线图上的每个数据点的形状。节点图形样式 DIAMOND菱形、SQUARE方形、CIRCLE圆形
        line.setShape(ValueShape.CIRCLE);
        //折线是否平滑，即是曲线还是折线。false：折线，true：曲线
        line.setCubic(false);
        //是否填充去信的面积
        line.setFilled(false);
        //曲线的数据坐标是否加上备注
        line.setHasLabels(true);
        // 隐藏数据，触摸可以显示。（设置了这个属性后，那么 setHasLabels()方法就会无效）
        line.setHasLabelsOnlyForSelected(false);
        //是否用线显示。false: 只有点显示，没有曲线
        line.setHasLines(true);
        //是否显示原点。false：没有原点只有点显示 (每个数据点都是个大的圆点)
        line.setHasPoints(true);
        //设置折线的颜色
        //line.setColor(Color.BLACK);
        //设置折线的宽度
        //line.setStrokeWidth(2);
        //设置节点颜色
        //line.setPointColor(Color.BLACK);
        //设置节点半径
        //line.setPointRadius(3);

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        LineChartData lineChartData = new LineChartData();
        lineChartData.setLines(lines);


        //4. 坐标轴
        Axis axisX = new Axis();//X轴
        //X坐标轴字体是 斜的显示还是直的显示， true：斜的显示
        axisX.setHasTiltedLabels(false);
        //X坐标轴字体颜色
        axisX.setTextColor(Color.parseColor("#D7D7D7"));
        //X坐标轴字体大小
        axisX.setTextSize(13);
        //X坐标轴 表格名称
        //axisX.setName("日期");
        //最多几个X轴坐标，就是说：缩放让X轴上显示数据的个数（7<= x <= mAxisXValues.length）
        axisX.setMaxLabelChars(8);
        //填充X轴坐标上 原点的值
        axisX.setValues(mAxisXValue);
        //X轴网格线
        axisX.setHasLines(false);
        //X轴的颜色
        axisX.setLineColor(Color.parseColor("#DDDDDD"));
        //X轴显示的数值在外部显示
        axisX.setInside(false);


        //Y轴
        Axis axisY = new Axis();
        //Y轴标注
        //axisY.setName("步数");
        //Y轴字体大小
        axisY.setTextSize(13);
        //Y轴字体颜色
        axisY.setTextColor(Color.parseColor("#CCCCCC"));
        //Y轴网格线
        axisY.setHasLines(true);
        //Y轴分割线（就是坐标轴是否显示）
        axisY.setHasSeparationLine(false);
        //填充Y轴坐标上 原点的值
        axisY.setValues(mAxisYValue);
        //Y轴数值自动生成 (需要在setValues()方法之后调用才起作用)
        axisY.setAutoGenerated(true);
        //Y轴显示的数值在内部显示
        axisY.setInside(true);


        //X轴坐标显示在底部
        lineChartData.setAxisXBottom(axisX);
        //Y轴坐标显示在左边
        lineChartData.setAxisYLeft(axisY);


        //5.设置LineChartData属性
        //数据背景颜色
        lineChartData.setValueLabelBackgroundColor(Color.WHITE);
        //数据背景颜色和节点颜色是否一致
        //lineChartData.setValueLabelBackgroundAuto(true);
        //是否有数据背景颜色
        lineChartData.setValueLabelBackgroundEnabled(true);


        //6.设置行为属性，支持缩放／滑动以及平移
        //LineChartView 与 用户是否可交互
        mLineChartView.setInteractive(true);
        //缩放的方向
        mLineChartView.setZoomType(ZoomType.HORIZONTAL);
        //最大缩放比例
        mLineChartView.setMaxZoom(4.0f);
        mLineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        mLineChartView.setLineChartData(lineChartData);
        mLineChartView.setVisibility(View.VISIBLE);


        //7.X轴固定显示数据项
        Viewport viewport = new Viewport(mLineChartView.getMaximumViewport());
        viewport.left = 0; //代表显示图表起始x轴坐标，0表示第一个
        viewport.right = 6; //代表显示图表结束x轴坐标，6表示第7个
        mLineChartView.setCurrentViewport(viewport);


        //8.如何设置以Y轴最小值0开始 显示折线？

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }


    private class MineLineChartOnValueSelectListener implements LineChartOnValueSelectListener {
        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            Toast.makeText(mContext, value.getY() + "", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {

        }
    }

    private class MineOnItemSelectedListener implements MaterialSpinner.OnItemSelectedListener<String> {
        @Override
        public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
            Toast.makeText(mContext, item, Toast.LENGTH_SHORT).show();
            mMaterialSpinnerLayout.setArrow(false);
        }
    }

    private class MineCalendarClickListener implements OnCalendarClickListener {
        @Override
        public void onClickDate(int year, int month, int day) {
            mCurrentSelectedDate.setText(mCalendarLayout.getCurrrentSelectedDate());
        }
    }
}
