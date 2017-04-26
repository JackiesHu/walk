package com.buxingzhe.pedestrian.User;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseActivity;
import com.buxingzhe.pedestrian.bean.user.User;
import com.buxingzhe.pedestrian.widget.TitleBarView;

import static android.text.InputType.TYPE_TEXT_VARIATION_NORMAL;

/**
 * Created by jackie on 2017/4/24.
 */

public class EditInfoActiviy extends BaseActivity implements View.OnClickListener {
    private EditText et_editInfo_content;
    private RadioButton rbtn_editSex_m, rbtn_editSex_f;
    private User user = new User();
    private String sex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editinfo);
        findViewById(R.id.title_bar);
        initView();
    }
    private void initView() {
        vTitleBar = (TitleBarView) findViewById(R.id.title_bar);
        vTitleBar.setRight("完成");

        findViewById(R.id.ll_edit_sex).setVisibility(View.GONE);


        findViewById(R.id.ll_edit_sex_m).setOnClickListener(this);
        findViewById(R.id.ll_edit_sex_f).setOnClickListener(this);

        et_editInfo_content = (EditText) findViewById(R.id.et_editInfo_content);
        rbtn_editSex_m = (RadioButton) findViewById(R.id.rbtn_editSex_m);
        rbtn_editSex_f = (RadioButton) findViewById(R.id.rbtn_editSex_f);

        TextView tv_editInfo_unit = (TextView) findViewById(R.id.tv_editInfo_unit);
        String title = "";
        String digits = "";
        int minlines = 1;
        int maxLength = Integer.MAX_VALUE;
        int inputType = InputType.TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_NORMAL;
        switch (getIntent().getIntExtra(IntentKey.EDIT_TAG, R.id.ll_userName)) {
            case R.id.ll_userName:

                title = "昵称";
                digits = "";
                maxLength = 100;
                tv_editInfo_unit.setVisibility(View.GONE);
                et_editInfo_content.setText(user.getNickname());
                break;
            case R.id.ll_userSex:

                title = "性别";
                findViewById(R.id.ll_edit_info).setVisibility(View.GONE);
                findViewById(R.id.ll_edit_sex).setVisibility(View.VISIBLE);
                sex = user.getSex();
                if (sex != null && sex.equals("f")){
                    rbtn_editSex_m.setChecked(false);
                    rbtn_editSex_f.setChecked(true);
                }else {
                    rbtn_editSex_m.setChecked(true);
                    rbtn_editSex_f.setChecked(false);
                }
                break;
            case R.id.ll_userAge:

                title = "年龄";
                digits = "0123456789";
                maxLength = 3;
                inputType = InputType.TYPE_CLASS_NUMBER;
                tv_editInfo_unit.setText("岁");
                et_editInfo_content.setText(user.getAge());
                break;
            case R.id.ll_userHeight:

                title = "身高";
                digits = "0123456789";
                maxLength = 3;
                inputType = InputType.TYPE_CLASS_NUMBER;
                tv_editInfo_unit.setText("cm");
                et_editInfo_content.setText(user.getHeight());
                break;
            case R.id.ll_userWeight:

                title = "体重";
                digits = "0123456789";
                maxLength = 3;
                inputType = InputType.TYPE_CLASS_NUMBER;
                tv_editInfo_unit.setText("kg");
                et_editInfo_content.setText(user.getWeight());
                break;
          /*  case R.id.lv_personal:  // 验证邀请码

                title = "验证邀请";
                digits = "0123456789";
                maxLength = 6;
                inputType = InputType.TYPE_CLASS_NUMBER;
                tv_editInfo_unit.setVisibility(View.GONE);
                et_editInfo_content.setHint("请输入验证码");
                break;
            case R.id.tv_add:  // 跑团加入验证邀

                title = "验证信息";
                digits = "";
                minlines = 6;
                setRightTitle("发送");
                tv_editInfo_unit.setVisibility(View.GONE);
                et_editInfo_content.setHint("对方需要填写验证信息");
                break;*/
        }
        setTitle(title);
        // 移动光标
        et_editInfo_content.setSelection(et_editInfo_content.getText().length());
        // 设置输入文本限制
        et_editInfo_content.setKeyListener(DigitsKeyListener.getInstance(digits));
        // 设置软键盘类型
        et_editInfo_content.setInputType(inputType);
        // 限制输入字符长度
        et_editInfo_content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        // 设置输入框最低行数
        et_editInfo_content.setMinLines(minlines);
    }

    @Override
    public void onClick(View view) {

    }
}
