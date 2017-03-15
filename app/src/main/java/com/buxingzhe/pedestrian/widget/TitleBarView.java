package com.buxingzhe.pedestrian.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.utils.SystemUtils;


/**
 * Created by Administrator on 2016/5/19.
 */
public class TitleBarView extends FrameLayout {
    private LayoutInflater inflater;
    private RelativeLayout containerLayout;
    private TextView vLeft;
    private TextView vTitle;
    private ImageView vImg;
    private TextView vRight;
    private ImageView vImgLeft;
    private ImageView vImgRight;
    private ImageView vTitleImage;
    private ImageView vRightMark;
    private RelativeLayout vStatusbar;
    public void setTitleBarLinstener(TitleBarLinstener titleBarLinstener) {
        this.titleBarLinstener = titleBarLinstener;
    }
    private TitleBarLinstener titleBarLinstener;
    public TitleBarView(Context context) {
        super(context);
        construct();
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        construct();
    }

    public TitleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        construct();
    }
    private void construct()
    {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        containerLayout = (RelativeLayout) inflater.inflate(R.layout.actionbar_layout, this, false);
        addView(containerLayout);
        findId();
    }
    private void findId() {
        vStatusbar = (RelativeLayout) containerLayout.findViewById(R.id.status_bar);
        vLeft = (TextView)containerLayout.findViewById(R.id.left_text);
        vTitle = (TextView)containerLayout.findViewById(R.id.caption_text);
        vTitleImage = (ImageView)containerLayout.findViewById(R.id.caption_image);
        vRight = (TextView) containerLayout.findViewById(R.id.right_text);
        vImgLeft = (ImageView)containerLayout.findViewById(R.id.left_img);
        vImgRight = (ImageView)containerLayout.findViewById(R.id.right_image);
        vRightMark = (ImageView) containerLayout.findViewById(R.id.caption_right_mark);
        setLeftList();
        setRightList();
        setImgLeftList();
        setImgRightList();

        hideStatusbar();
        showLeftImag();
        hiddeRight();
        hideRightImg();
        hideRightMark();


    }
    public void visibleTitleImage(){
        vTitleImage.setVisibility(VISIBLE);
    }
    public void invisibleTitleImage(){
        vTitleImage.setVisibility(GONE);
    }
    public void visibleTitle(){
        vTitle.setVisibility(VISIBLE);
    }
    public void invisibleTitle(){
        vTitle.setVisibility(GONE);
    }
    public void showTitleImage(){
        visibleTitleImage();
        invisibleTitle();
    }
    public void showTitleTitle(){
        invisibleTitleImage();
        visibleTitle();
    }
    public void setvTitle(String title){
        vTitle.setText(title);
        showTitleTitle();
    }
    public void setvTitleImage(int RId){
        vTitleImage.setImageResource(RId);
        showTitleImage();
    }
    public void setvImg(int id){
        vImg.setImageResource(id);
    }
    public void setLeft(String title){
        vLeft.setText(title);
        vLeft.setVisibility(View.VISIBLE);
        vImgLeft.setVisibility(GONE);
    }
    public void setRight(String title){
        vRight.setText(title);
        vRight.setVisibility(View.VISIBLE);
        vImgRight.setVisibility(GONE);
    }
    public void setRightIcon(int id){
        vImgRight.setImageResource(id);
    }
    public void showLeftImag(){
        vLeft.setVisibility(GONE);
        vImgLeft.setVisibility(VISIBLE);
    }
    public void hideStatusbar() {
        if (vStatusbar != null){
            vStatusbar.setVisibility(GONE);
        }
    }
    public void hideLeft(){
        vLeft.setVisibility(GONE);
        vImgLeft.setVisibility(GONE);
    }
    public void showLeftTitle(){
        vImgLeft.setVisibility(GONE);
        vLeft.setVisibility(VISIBLE);
    }
    public void setLeftList(){
        vLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleBarLinstener != null) {
                    titleBarLinstener.onLeftTitleListener(v);
                }
            }
        });
    }
    public void setImgLeftOnclick(OnClickListener onclick){
        vImgLeft.setOnClickListener(onclick);
    }
    public void setRightList(){
        vRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleBarLinstener != null) {
                    titleBarLinstener.onRightListener(v);
                }
            }
        });
    }
    public void setImgLeftList(){
        vImgLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleBarLinstener != null){
                    titleBarLinstener.onLeftImgListener(v);
                }
            }
        });
    }
    public void setImgLeftList(OnClickListener onClickListener){
        vImgLeft.setOnClickListener(onClickListener);
    }
    public void setImgRightList(){
        vImgRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleBarLinstener != null) {
                    titleBarLinstener.onRightImageListener(v);
                }
            }
        });
    }
    public void setImgRightList(OnClickListener onClickListener){
        vImgRight.setOnClickListener(onClickListener);
    }
    public void showRight(){
        vRight.setVisibility(VISIBLE);
        setRightMarkLayout(R.id.right_text);
    }
    public void hiddeRight(){
        vRight.setVisibility(GONE);
    }
    public void showRightImg(){
        vImgRight.setVisibility(VISIBLE);
        setRightMarkLayout(R.id.right_image);
    }
    public void hideRightImg(){
        vImgRight.setVisibility(GONE);
    }
    public void hideRightMark(){
        vRightMark.setVisibility(GONE);
    }
    public void showRightMark(){
        vRightMark.setVisibility(VISIBLE);
    }
    private void setRightMarkLayout(int id){
        int paramsHeight = SystemUtils.dip2px(getContext(),8);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(paramsHeight,paramsHeight);
        layoutParams.addRule(RelativeLayout.RIGHT_OF,id);
        layoutParams.addRule(RelativeLayout.ABOVE,id);
//        layoutParams.addRule(RelativeLayout.ALIGN_TOP,id);
        vRightMark.setLayoutParams(layoutParams);
    }
    public void hideTitlebar(){
        containerLayout.setVisibility(GONE);
    }
    public void showTitlebar(){
        containerLayout.setVisibility(VISIBLE);
    }
}
