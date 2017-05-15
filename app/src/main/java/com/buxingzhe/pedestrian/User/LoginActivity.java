package com.buxingzhe.pedestrian.User;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buxingzhe.lib.util.Log;
import com.buxingzhe.lib.util.StringUtil;
import com.buxingzhe.lib.widget.AwesomeDialogUtil;
import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseActivity;
import com.buxingzhe.pedestrian.bean.user.UserLoginResultInfo;
import com.buxingzhe.pedestrian.common.GlobalParams;
import com.buxingzhe.pedestrian.http.manager.NetRequestManager;
import com.buxingzhe.pedestrian.utils.EnterActUtils;
import com.buxingzhe.pedestrian.utils.JsonParseUtil;
import com.buxingzhe.pedestrian.utils.SharedPreferencesUtil;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

/**
 * 登陆页
 * Created by zhaishaoping on 26/03/2017.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener {
    //删除phone
    private ImageView mIVPhoneDel;
    //密码显示
    private ImageView mIVPwdShow;
    //微信快捷登陆
    private ImageView mIVWechat;
    //输入的手机号
    private EditText mPhone;
    //输入的密码
    private EditText mPassword;
    //登陆
    private Button mLogin;
    //快速注册
    private TextView mRegister;
    //是否显示密码标记. true: 显示密码 false: 隐藏密码
    private boolean isShowPwd = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//无标题
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//无状态栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);
        findId();
        registerListener();

        //自动登陆
       GlobalParams.TOKEN = SharedPreferencesUtil.getInstance().getSharedPreferences(getApplicationContext())
                .getString("token", "");
       GlobalParams.USER_ID = SharedPreferencesUtil.getInstance().getSharedPreferences(getApplicationContext())
                .getString("uid", "");
       GlobalParams.LOGIN_TYPE = SharedPreferencesUtil.getInstance().getSharedPreferences(getApplicationContext())
                .getInt("loginType", 0);
        if (!TextUtils.isEmpty(GlobalParams.TOKEN)){
            login(null,GlobalParams.LOGIN_TYPE);
        }

        checkPermission();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0x101);
            }
        }
    }

    private void registerListener() {
        mIVWechat.setOnClickListener(this);
        mIVPhoneDel.setOnClickListener(this);
        mIVPwdShow.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);

        mIVPhoneDel.setOnLongClickListener(this);

        watchEditText(mPhone, mIVPhoneDel);
        watchEditTextOnFocus(mPhone);

    }

    private void findId() {
        mIVWechat = (ImageView) findViewById(R.id.login_iv_wechat);
        mIVPhoneDel = (ImageView) findViewById(R.id.login_iv_phone_del);
        mIVPwdShow = (ImageView) findViewById(R.id.login_iv_pwd_show);
        mLogin = (Button) findViewById(R.id.login_btn_login);
        mRegister = (TextView) findViewById(R.id.login_register);
        mPassword = (EditText) findViewById(R.id.login_et_pwd);
        mPhone = (EditText) findViewById(R.id.login_et_phone);
    }

    @Override
    public boolean onLongClick(View v) {
        if (mPhone != null) {
            //实现长按全部字符删除
            mPhone.setText("");
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_iv_wechat://微信登陆
                UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.WEIXIN, umAuthListener);
                break;
            case R.id.login_btn_login://手机号登陆
                String phoneStr1 = mPhone.getText().toString();
                String passwordStr1 = mPassword.getText().toString();

                boolean isOK = checkInputInfo(phoneStr1, passwordStr1);
                if (!isOK) {
                    return;
                }

                Map<String, String> loginParams = new HashMap<String, String>();
                loginParams.put("password", passwordStr1);
                loginParams.put("mobile", phoneStr1);

                login(loginParams, 1);
                break;
            case R.id.login_register://快速注册
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                EnterActUtils.startAct(LoginActivity.this, intent);
                break;
            case R.id.login_iv_phone_del://删除输入的手机号
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
                break;
            case R.id.login_iv_pwd_show://显示pwd
                if (mPassword != null) {
                    if (isShowPwd) {
                        //隐藏密码
                        mIVPwdShow.setImageResource(R.mipmap.login_ic_eye_close);
                        mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    } else {
                        //显示密码
                        mIVPwdShow.setImageResource(R.mipmap.login_ic_eye_open);
                        mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                    isShowPwd = !isShowPwd;
                    //EditText设置setText()后，光标会自动跑到第一个字符之前。
                    mPassword.setSelection(mPassword.getText().length());


                }
                break;
        }
    }

    /**
     * 检查输入的内容是否合法
     *
     * @param phoneStr
     * @param passwordStr
     */
    private boolean checkInputInfo(String phoneStr, String passwordStr) {

        if (TextUtils.isEmpty(phoneStr)) {
            Toast.makeText(LoginActivity.this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(passwordStr)) {
            Toast.makeText(LoginActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        //判断输入的手机号是否合法 "^1(3|4|5|7|8)\\d{9}$"
        if (!StringUtil.checkPhone(phoneStr)) {
            Toast.makeText(LoginActivity.this, "请输入正确的手机号码！", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText(getApplicationContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();
            //授权成功
            Log.i(data.toString());

            String uid = null;//uid : 不可空
            String openid = null;
            String name = null;//用户名 : 不可空
            String iconurl = null;//头像URL : 不可空
            String gender = "0";//性别 0,未知 1，男 2,女
            String type = "0";//“0”:qq登陆 ， “1”：微信登陆，不填为qq登陆

            if (data != null && data.size() > 0) {
                openid = data.get("openid");
                name = data.get("name");
                iconurl = data.get("iconurl");
                gender = data.get("gender");

            }

            switch (platform) {
                case QQ:
                    type = "0";
                    uid = data.get("uid");
                    break;
                case WEIXIN:
                    type = "1";
                    uid = data.get("unionid");
                    break;

            }

            Map<String, String> loginParams = new HashMap<String, String>();
            loginParams.put("uid", uid);
            loginParams.put("openid", openid);
            loginParams.put("name", name);
            loginParams.put("iconurl", iconurl);
            loginParams.put("gender", gender);
            loginParams.put("type", type);

            login(loginParams, 2);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Log.e(t.getMessage());
            Toast.makeText(getApplicationContext(), "认证失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(getApplicationContext(), "认证取消", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 登陆服务器
     *
     * @param loginParams 参数封装的map
     * @param loginType   1：手机登陆，2：普通登陆  （设置此参数是因为 请求url不同）
     */
    private void login(Map<String, String> loginParams, final int loginType) {
        //TODO 访问服务端

        //show dialog
      //  AwesomeDialogUtil.getInstance().create(LoginActivity.this, "正在登陆...").showDialog();

        try{
            AwesomeDialogUtil.getInstance().create(LoginActivity.this, "正在登陆...").showDialog();
        }catch(Exception e){

        }


        Subscriber mSubscriber = new Subscriber<String>() {

            @Override
            public void onCompleted() {
                Log.i("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i("onError: " + e.getMessage());
                AwesomeDialogUtil.getInstance().create(LoginActivity.this).dismissDialog();

            }

            @Override
            public void onNext(String jsonStr) {
                Log.i(jsonStr);
               // AwesomeDialogUtil.getInstance().create(LoginActivity.this).dismissDialog();

                // 由于服务端的返回数据格式不固定，因此这里采用手动解析
                Object[] datas = JsonParseUtil.getInstance().parseJson(jsonStr, UserLoginResultInfo.class);
                if ((Integer) datas[0] == 0) {
                    Log.i(datas[1].toString());

                    UserLoginResultInfo resultInfo = (UserLoginResultInfo) datas[1];
                    if (resultInfo != null) {
                        //TODO 保存数据
                        UserInfo userInfo = new UserInfo();
                        userInfo.formatUser(resultInfo);
                        userInfo.saveUserInfo(mContext,userInfo);

                        GlobalParams.TOKEN = resultInfo.getToken();
                        GlobalParams.USER_ID = resultInfo.getId();
                        GlobalParams.mUserLoginResultInfo = resultInfo;

                        //TODO 跳转Main
                        EnterActUtils.enterMainActivity(LoginActivity.this);
                        finish();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, datas[2].toString(), Toast.LENGTH_SHORT).show();
                }
            }
        };

        if (loginType == 1) {
            //手机登陆
            mSubscription = NetRequestManager.getInstance().loginByPhone(loginParams, mSubscriber);
        } else if (loginType == 2) {
            //普通登陆
            mSubscription = NetRequestManager.getInstance().login(loginParams, mSubscriber);
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 根据EditText中内容的变化动态显示 删除按钮
     *
     * @param editText
     * @param imageView
     */
    private void watchEditText(EditText editText, final ImageView imageView) {
        if (editText != null) {
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
                    if (!TextUtils.isEmpty(content)) {
                        imageView.setVisibility(View.VISIBLE);
                    } else {
                        imageView.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }

    /**
     * 根据EditText焦点得失的变化动态显示 删除按钮
     *
     * @param editText
     */
    private void watchEditTextOnFocus(EditText editText) {
        if (editText != null) {
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        // 此处为失去焦点时的处理内容
                        if (mIVPhoneDel != null) {
                            mIVPhoneDel.setVisibility(View.INVISIBLE);
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
    }
}
