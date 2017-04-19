package com.buxingzhe.pedestrian.widget;

/**
 * Created by 姜炳轩 on 2016/6/28.
 */

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

public class TextParser {
    private List<TextBean> textList;

    public TextParser() {
        textList = new LinkedList<TextBean>();
    }


    public TextParser append(String text, int size, int color) {
        if (text == null) {
            return this;
        }
        TextBean bean = new TextBean();
        bean.text = text;
        bean.size = size;
        bean.color = color;
        textList.add(bean);

        return this;
    }

    public TextParser append(String text, int size, int color,
                             View.OnClickListener onClickListener) {
        if (text == null) {
            return this;
        }
        TextBean bean = new TextBean();
        bean.text = text;
        bean.size = size;
        bean.color = color;
        bean.onClickListener = onClickListener;
        textList.add(bean);

        return this;
    }
    public TextParser append(String text, int size, int color,int style) {
        if (text == null) {
            return this;
        }
        TextBean bean = new TextBean();
        bean.text = text;
        bean.size = size;
        bean.color = color;
        bean.style=style;
        textList.add(bean);

        return this;
    }


    public void parse(TextView textView) {
        StringBuilder sBuilder = new StringBuilder();
        for (TextBean bean : textList) {
            sBuilder.append(bean.text);
        }

        SpannableStringBuilder style = new SpannableStringBuilder(sBuilder);
        int position = 0;
        for (TextBean bean : textList) {
            if (bean.onClickListener != null) {

                style.setSpan(new MyClickableSpan(bean.onClickListener),
                        position,
                        position + bean.text.length(),
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }

            style.setSpan(new ForegroundColorSpan(bean.color), position,
                    position + bean.text.length(),
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

            style.setSpan(new AbsoluteSizeSpan(bean.size,true), position, position
                    + bean.text.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

            style.setSpan(new StyleSpan(bean.style),position, position
                    + bean.text.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

            position += bean.text.length();
        }

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(style);
    }


    private class TextBean {
        public View.OnClickListener onClickListener;
        public String text;
        public int size;
        public int color;
        public int style;
    }

    private static class MyClickableSpan extends ClickableSpan {
        private View.OnClickListener mOnClickListener;

        public MyClickableSpan(View.OnClickListener onClickListener) {
            mOnClickListener = onClickListener;
        }

        @Override
        public void onClick(View widget) {
            if (mOnClickListener != null) {
                mOnClickListener.onClick(widget);
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
        }

    }

}
