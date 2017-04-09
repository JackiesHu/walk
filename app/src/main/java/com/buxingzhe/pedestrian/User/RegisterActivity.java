package com.buxingzhe.pedestrian.User;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.buxingzhe.lib.util.Log;
import com.buxingzhe.lib.util.StringUtil;
import com.buxingzhe.lib.widget.AwesomeDialogUtil;
import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseActivity;
import com.buxingzhe.pedestrian.bean.user.UserLoginResultInfo;
import com.buxingzhe.pedestrian.http.manager.NetRequestManager;
import com.buxingzhe.pedestrian.utils.EnterActUtils;
import com.buxingzhe.pedestrian.utils.JsonParseUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.Unbinder;
import rx.Subscriber;


/**
 * Created by zhaishaoping on 01/04/2017.
 */
public class RegisterActivity extends BaseActivity{

    //昵称
    @BindView(R.id.register_et_nickname) EditText mNickName;
    //密码
    @BindView(R.id.register_et_pwd) EditText mPassword;
    //手机号
    @BindView(R.id.register_et_phone) EditText mPhone;
    //显示密码
    @BindView(R.id.register_iv_pwd_show) ImageView mIVPwdShow;
    //删除手机号按钮
    @BindView(R.id.register_iv_phone_del) ImageView mIVPhoneDel;
    //删除昵称按钮
    @BindView(R.id.register_iv_nickname_del) ImageView mIVNickNameDel;

    //是否显示密码标记. true: 显示密码 false: 隐藏密码
    private boolean isShowPwd = false;

    Unbinder bind = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//无标题
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//无状态栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_layout);
        bind = ButterKnife.bind(this);

        registerListener();
    }


    private void registerListener() {
        watchEditText(mNickName,mIVNickNameDel);
        watchEditText(mPhone,mIVPhoneDel);

        watchEditTextOnFocus(mNickName);
        watchEditTextOnFocus(mPhone);
    }

    //删除昵称按钮事件
    @OnClick(R.id.register_iv_nickname_del) void delNickName(View view){
        Log.i(view.toString());
        if (mNickName != null) {
            //TODO 想要实现单个字符删除
            String nickNameStr = mNickName.getText().toString();
            if (!"".equals(nickNameStr) && nickNameStr != null && nickNameStr.length() > 1) {
                mNickName.setText(nickNameStr.substring(0, nickNameStr.length() - 1));
                mNickName.setSelection(nickNameStr.length() - 1);
            } else {
                mNickName.setText("");
            }
        }
    }

    //长按删除昵称按钮事件
    @OnLongClick(R.id.register_iv_nickname_del) boolean delNickNameByLong(){
        if (mNickName != null) {
            //实现长按全部字符删除
            mNickName.setText("");
            return true;
        }
        return false;
    }

    //删除手机号按钮事件
    @OnClick(R.id.register_iv_phone_del) void delPhone(){
        if (mPhone != null) {
            //TODO 想要实现单个字符删除
            String phoneStr = mPhone.getText().toString();
            if (!"".equals(phoneStr) && phoneStr != null && phoneStr.length() > 1) {
                mPhone.setText(phoneStr.substring(0, phoneStr.length() - 1));
                mPhone.setSelection(phoneStr.length() - 1);
            } else {
                mPhone.setText("");
            }
        }
    }

    //长按删除手机号按钮事件
    @OnLongClick(R.id.register_iv_phone_del) boolean delPhoneByLong(){
        if (mPhone != null) {
            //实现长按全部字符删除
            mPhone.setText("");
            return true;
        }
        return false;
    }

    //显示密码按钮事件
    @OnClick(R.id.register_iv_pwd_show) void showPwd(){
        if (mPassword != null) {
            if (isShowPwd) {
                //显示密码
                mIVPwdShow.setImageResource(R.mipmap.login_ic_eye_close);
                mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else {
                //隐藏密码
                mIVPwdShow.setImageResource(R.mipmap.login_ic_eye_open);
                mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            isShowPwd = !isShowPwd;
            //EditText设置setText()后，光标会自动跑到第一个字符之前。
            mPassword.setSelection(mPassword.getText().length());


        }
    }

    //注册
    @OnClick(R.id.register_btn_register) void register(){
        String phoneStr = mPhone.getText().toString();
        String passwordStr = mPassword.getText().toString();
        String nickNameStr = mNickName.getText().toString();

        boolean isOK = checkInputInfo(phoneStr,passwordStr,nickNameStr);
        if(!isOK){
            return;
        }

        Map<String, String> registerParams = new HashMap<String, String>();
        registerParams.put("password", passwordStr);
        registerParams.put("mobile", phoneStr);
        registerParams.put("nickName", phoneStr);

        register(registerParams);
    }

    //返回登陆页面
    @OnClick(R.id.register_login_back) void backLogin(){
        finish();
    }

    private void register(Map<String, String> params) {
        AwesomeDialogUtil.getInstance().create(this,"正在注册...").showDialog();
        mSubscription = NetRequestManager.getInstance().register(params, /* new Subscriber<RequestResultInfo<UserLoginResultInfo>>() {
            @Override
            public void onCompleted() {
                AwesomeDialogUtil.getInstance().create(RegisterActivity.this).dismissDialog();
                Log.i("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                AwesomeDialogUtil.getInstance().create(RegisterActivity.this).dismissDialog();
                Log.i("onError " + e.getMessage());
            }

            @Override
            public void onNext(RequestResultInfo<UserLoginResultInfo> userLoginResultInfoRequestResultInfo) {
                Log.i("onNext");
                AwesomeDialogUtil.getInstance().create(RegisterActivity.this).dismissDialog();
                if (userLoginResultInfoRequestResultInfo != null ) {
                    UserLoginResultInfo content = userLoginResultInfoRequestResultInfo.getContent();
                    if(content != null){
                        //TODO 保存数据
                        Log.i(content.toString());
                        //TODO 跳转Main
                        EnterActUtils.enterMainActivity(RegisterActivity.this);
                        finish();
                    }
                }
            }
        } */ new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                AwesomeDialogUtil.getInstance().create(RegisterActivity.this).dismissDialog();
                Log.i("onError " + e.getMessage());
            }

            @Override
            public void onNext(String jsonStr) {
                Log.i("onNext");
                AwesomeDialogUtil.getInstance().create(RegisterActivity.this).dismissDialog();

                // 由于服务端的返回数据格式不固定，因此这里采用手动解析
                Object[] datas = JsonParseUtil.getInstance().parseJson(jsonStr, UserLoginResultInfo.class);
                if ((Integer) datas[0] == 0) {
                    Log.i(datas[1].toString());

                    UserLoginResultInfo resultInfo = (UserLoginResultInfo) datas[1];
                    if (resultInfo != null) {
                        //TODO 保存数据

                        //TODO 跳转Main
                        EnterActUtils.enterMainActivity(RegisterActivity.this);
                        finish();
                    }

                } else {
                    Toast.makeText(RegisterActivity.this, datas[2].toString(), Toast.LENGTH_SHORT).show();
                }
            }


        });
    }


    /**
     * 检查输入的内容是否合法
     * @param phoneStr
     * @param passwordStr
     * @param nickNameStr
     */
    private boolean checkInputInfo(String phoneStr, String passwordStr, String nickNameStr) {

        if(TextUtils.isEmpty(nickNameStr)){
            Toast.makeText(this, "昵称不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(phoneStr)){
            Toast.makeText(this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(passwordStr)){
            Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        //判断输入的手机号是否合法 "^1(3|4|5|7|8)\\d{9}$"
        if(!StringUtil.checkPhone(phoneStr)){
            Toast.makeText(this, "请输入正确的手机号码！", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    /**
     * 根据EditText中内容的变化动态显示 删除按钮
     * @param editText
     * @param imageView
     */
    private void watchEditText(EditText editText, final ImageView imageView) {
        if (editText!=null){
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String content = s.toString().trim();
                    if (!TextUtils.isEmpty(content)){
                        imageView.setVisibility(View.VISIBLE);
                    }else{
                        imageView.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }

    /**
     * 根据EditText焦点得失的变化动态显示 删除按钮
     * @param editText
     */
    private void watchEditTextOnFocus(EditText editText) {
        if (editText!=null){
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        // 此处为失去焦点时的处理内容
                        if(mIVPhoneDel!=null){
                            mIVPhoneDel.setVisibility(View.INVISIBLE);
                        }

                        if(mIVNickNameDel!=null){
                            mIVNickNameDel.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isShowPwd = false;
        if (bind!=null){
            bind.unbind();
        }
    }

}
