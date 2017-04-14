package com.buxingzhe.lib.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Loading Dialog
 * Created by zhaishaoping on 03/04/2017.
 */
public class AwesomeDialogUtil {

    private AwesomeDialog mDialog = null;
    private String mContent = "";

    private AwesomeDialogUtil() {
    }

    public static AwesomeDialogUtil getInstance() {
        return AwesomeDialogUtil.SingleHolder.sInstance;
    }

    private static class SingleHolder {
        private static final AwesomeDialogUtil sInstance = new AwesomeDialogUtil();
    }

    public AwesomeDialog create(Context context) {
        if (mDialog == null) {
            mDialog = new AwesomeDialog(context);
        }
        return mDialog;
    }

    public AwesomeDialog create(Context context,String content) {
        if (mDialog == null) {
            mContent = content;
            mDialog = new AwesomeDialog(context);
        }
        return mDialog;
    }

    public class AwesomeDialog extends Dialog {

        private TextView mTextView;

        public AwesomeDialog(Context context) {
            super(context, context.getResources().getIdentifier("loadingDialogStyle", "style", context.getPackageName()));
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(getContext().getResources().getIdentifier("dialog_loading", "layout", getContext().getPackageName()));

            setCanceledOnTouchOutside(false);
            setCancelable(false);

            mTextView = (TextView) findViewById(getContext().getResources().getIdentifier("dialog_content", "id", getContext().getPackageName()));
            mTextView.setText(mContent);

        }

        /**
         * 设置内容 //TODO BUG:这个方法还暂时未能设置成功
         *
         * @param message
         */
        public AwesomeDialog setMessage(String message) {
            if (mTextView != null) {
                mTextView.setText(message);
            }
            return this;
        }

        /**
         * 设置内容 //TODO BUG:这个方法还暂时未能设置成功
         *
         * @param msgID
         */
        public AwesomeDialog setMessage(int msgID) {
            if (mTextView != null) {
                mTextView.setText(getContext().getResources().getString(msgID));
            }
            return this;
        }

        public void showDialog() {
            if (!isShowing()) {
                show();
            }
        }

        public void hideDialog() {
            if (isShowing()) {
                hide();
            }
        }

        public void dismissDialog() {
            dismiss();
        }

    }
}



