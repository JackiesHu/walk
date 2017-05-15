package com.buxingzhe.pedestrian.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseActivity;
import com.buxingzhe.pedestrian.common.GlobalParams;
import com.buxingzhe.pedestrian.http.manager.NetRequestManager;
import com.buxingzhe.pedestrian.utils.FileConfig;
import com.buxingzhe.pedestrian.utils.PicUtil;
import com.buxingzhe.pedestrian.utils.SystemUtils;
import com.buxingzhe.pedestrian.widget.CircularImageView;
import com.buxingzhe.pedestrian.widget.TitleBarView;
import com.pizidea.imagepicker.FileUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;

/**
 * // 用户信息详情
 * Created by Administrator on 2016/4/7 0007.
 */
public class UserInfoActivity extends BaseActivity implements View.OnClickListener {
    String headUrl;
    @BindView(R.id.title_bar)
    TitleBarView titleBar;
    @BindView(R.id.sdv_userHead)
    CircularImageView sdvUserHead;
    @BindView(R.id.ll_userHead)
    LinearLayout llUserHead;
    @BindView(R.id.tv_userName)
    TextView tvUserName;
    @BindView(R.id.ll_userName)
    LinearLayout llUserName;
    @BindView(R.id.tv_userSex)
    View tvUserSex;
    @BindView(R.id.iv_userSex)
    ImageView ivUserSex;
    @BindView(R.id.ll_userSex)
    LinearLayout llUserSex;
    @BindView(R.id.tv_userAge)
    TextView tvUserAge;
    @BindView(R.id.ll_userAge)
    LinearLayout llUserAge;
    @BindView(R.id.ll_group)
    LinearLayout llGroup;
    @BindView(R.id.tv_userHeight)
    TextView tvUserHeight;
    @BindView(R.id.ll_userHeight)
    LinearLayout llUserHeight;
    @BindView(R.id.tv_userWeight)
    TextView tvUserWeight;
    @BindView(R.id.ll_userWeight)
    LinearLayout llUserWeight;
    private File uploadFile;
    private RequestBody requestBody;
    private String uploadFileName;
    private MultipartBody.Part photo;
    private String photoNew;
    private UserInfo userInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        vTitleBar = (TitleBarView) findViewById(R.id.title_bar);
        setTitle("个人资料");
        vTitleBar.setRight("完成");
        findViewById(R.id.ll_userHead).setOnClickListener(this);
        findViewById(R.id.ll_userName).setOnClickListener(this);
        findViewById(R.id.ll_userSex).setOnClickListener(this);
        findViewById(R.id.ll_userAge).setOnClickListener(this);
        findViewById(R.id.ll_userHeight).setOnClickListener(this);
        findViewById(R.id.ll_userWeight).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userInfo = new UserInfo();
        userInfo = userInfo.getUserInfo(this);
        initUser(userInfo);
    }


    @Override
    public void onRightListener(View v) {
        super.onRightListener(v);

        Map<String, RequestBody> params = new HashMap<>();
        params.put("nickName", RequestBody.create(MediaType.parse("multipart/form-data"), userInfo.getNickName()));
        params.put("gender ", RequestBody.create(MediaType.parse("multipart/form-data"), userInfo.getGender()));
        params.put("height ", RequestBody.create(MediaType.parse("multipart/form-data"), userInfo.getHeight()));
        params.put("weight", RequestBody.create(MediaType.parse("multipart/form-data"), userInfo.getWeight()));
        params.put("age", RequestBody.create(MediaType.parse("multipart/form-data"), userInfo.getAge()));
        params.put("userId", RequestBody.create(MediaType.parse("multipart/form-data"), GlobalParams.USER_ID));
        params.put("token", RequestBody.create(MediaType.parse("multipart/form-data"), GlobalParams.TOKEN));
        MultipartBody.Part filePart;
        if (uploadFile != null) {
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), uploadFile);

            filePart = MultipartBody.Part.createFormData("avatar", uploadFile.getName(), requestFile);

        } else {
            filePart = null;
        }


        NetRequestManager.getInstance().modifyUserInfo(params, filePart, new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                Toast.makeText(UserInfoActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(String str) {
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    int code = (int) jsonObject.get("code");

                    System.out.println("eeUserInfo--next" + "code" + code + jsonObject.get("content"));

                    JSONObject content = new JSONObject(jsonObject.get("content").toString());

                    UserInfo newUserInfo = new UserInfo();
                    newUserInfo.setNickName(content.get("nickName").toString());
                    newUserInfo.setAvatar(content.get("avatarUrl").toString());
                    newUserInfo.setGender(content.get("gender").toString());
                    newUserInfo.setHeight(content.get("height").toString());
                    newUserInfo.setWeight(content.get("weight").toString());
                    newUserInfo.setAge(content.get("age").toString());
                    newUserInfo.setCreateTimestamp(content.get("createTimestamp").toString());
                    newUserInfo.setId(content.get("id").toString());
                    newUserInfo.setToken(GlobalParams.TOKEN);
                    GlobalParams.USER_ID = content.get("id").toString();
                    initUser(newUserInfo);
                    newUserInfo.saveUserInfo(UserInfoActivity.this, newUserInfo);


                    Toast.makeText(UserInfoActivity.this, jsonObject.get("content") + "content", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
    }


    private void initUser(UserInfo userInfo) {

        // 头像
        if (!TextUtils.isEmpty(userInfo.getAvatar())) {
            Picasso.with(this).load(userInfo.getAvatar()).resize(SystemUtils.dip2px(this, 67.0f), SystemUtils.dip2px(this, 67.0f)).centerCrop()
                    .into((ImageView) findViewById(R.id.sdv_userHead));
        }
        // 昵称
        ((TextView) findViewById(R.id.tv_userName)).setText(userInfo.getNickName());
        // 性别

        if (userInfo.getGender().equals("1"))
            ((ImageView) findViewById(R.id.iv_userSex)).setImageResource(R.mipmap.personal_icon_f);
        else
            ((ImageView) findViewById(R.id.iv_userSex)).setImageResource(R.mipmap.personal_icon_m);
        // 年龄
        ((TextView) findViewById(R.id.tv_userAge)).setText(userInfo.getAge() + "岁");
        //
        if (userInfo.getHeight().equals(""))
            findViewById(R.id.tv_userHeight).setVisibility(View.INVISIBLE);
        else {
            findViewById(R.id.tv_userHeight).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.tv_userHeight)).setText(userInfo.getHeight() + "cm");
        }
        ((TextView) findViewById(R.id.tv_userWeight)).setText(userInfo.getWeight() + "kg");
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.putExtra(IntentKey.EDIT_TAG, view.getId());
        switch (view.getId()) {
            case R.id.ll_userHead:

                PicUtil.showPicDialog(this);
                break;
            case R.id.ll_userName:

                intent.setClass(getApplicationContext(), EditInfoActiviy.class);
                break;
            case R.id.ll_userSex:

                intent.setClass(getApplicationContext(), EditInfoActiviy.class);
                break;
            case R.id.ll_userAge:

                intent.setClass(getApplicationContext(), EditInfoActiviy.class);
                break;
            case R.id.ll_userHeight:

                intent.setClass(getApplicationContext(), EditInfoActiviy.class);
                break;
            case R.id.ll_userWeight:

                intent.setClass(getApplicationContext(), EditInfoActiviy.class);
                break;
        }
        if (intent.getComponent() != null)
            startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int arg1, Intent arg2) {
        super.onActivityResult(requestCode, arg1, arg2);
        //拍照0
        if (arg1 != -1) {
            return;
        }
        if (requestCode == PicUtil.CUT_PIC_CODE && arg2 != null) {
            String picName = System.currentTimeMillis() + ".jpg";
            String newFileName = FileConfig.IMAGE_UP_PATH + picName;

            try {
                FileUtil.compressBmpToFile(PicUtil.cutPicPath, newFileName);

                File f = new File(PicUtil.cutPicPath);
                uploadFile = f;

/*
                if (f.exists())
                    f.delete();*/
                PicUtil.cutPicPath = newFileName;
                uploadFileName = newFileName;

                if (!TextUtils.isEmpty(newFileName)) {
                    Glide
                            .with(UserInfoActivity.this)
                            .load(newFileName)
                            .override(SystemUtils.dip2px(this, 67.0f), SystemUtils.dip2px(this, 67.0f))
                            .centerCrop()
                            .into((ImageView) findViewById(R.id.sdv_userHead));

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PicUtil.TAKEPHOTO_PIC_CODE || requestCode == PicUtil.SELECT_PIC_CODE) {

            if (arg1 == RESULT_OK) {
                PicUtil.cutPhotoZoom(arg2, this);
            }
        }
    }
}
