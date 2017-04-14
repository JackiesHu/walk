package com.buxingzhe.pedestrian.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.buxingzhe.pedestrian.R;


public class ProgersssDialog extends Dialog {
    private ProgressBar progressBar1;
    private TextView progress_dialog_txt;
        
    public ProgersssDialog(Context context) {
        super(context, R.style.progress_dialog);
        setCancelable(false);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view =inflater.inflate(R.layout.progress_dialog, null);
        progressBar1 = (ProgressBar) view.findViewById(R.id.progressBar1);
        progress_dialog_txt = (TextView) view.findViewById(R.id.progress_dialog_txt);
        setContentView(view);
    }

    /**
     * 设置显示进度框
     * @param text 文本内容
     * @param isShowProgressbar 是否显示进度条
     */
    public void showInView(String text, boolean isShowProgressbar)
    {
        if (!isShowProgressbar)
        {
            progressBar1.setVisibility(View.GONE);
        }
        else
        {
            progressBar1.setVisibility(View.VISIBLE);
        }
        progress_dialog_txt.setText(text);
        show();
    }

}