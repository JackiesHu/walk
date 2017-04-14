package com.buxingzhe.pedestrian.found.tag;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseActivity;
import com.buxingzhe.pedestrian.utils.EnterActUtils;
import com.buxingzhe.pedestrian.utils.ProgressUtils;
import com.buxingzhe.pedestrian.widget.TitleBarView;

/**
 * Created by quanjing on 2017/2/27.
 */
public class TagCreateActivity extends BaseActivity {
    private EditText vCreateTag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createtag);
        findId();
        setTitle();
    }
    private void findId() {
        vTitleBar = (TitleBarView) findViewById(R.id.createTag_title_bar);
        vCreateTag = (EditText) findViewById(R.id.createTag_ed);
    }
    private void setTitle() {
        setTitle("添加标签");
        setRightTitle("完成");
    }
    @Override
    public void onRightListener(View v) {
        String tag = vCreateTag.getText().toString();
        boolean checkResult = checkTag(tag);
        if (checkResult){
            Intent intent = new Intent();
            intent.putExtra("createTag",tag);
            setResult(RESULT_OK, intent);
            EnterActUtils.finishActivity(mActivity);
        }

    }
    private boolean checkTag(String tag){
        if (TextUtils.isEmpty(tag)){
            ProgressUtils.showDialog(mActivity,"标签不能为空",2);
            return false;
        }
        if (tag.length() > 20){
            ProgressUtils.showDialog(mActivity,"标签不能超过20个字",2);
            return false;
        }
        return true;
    }
}
