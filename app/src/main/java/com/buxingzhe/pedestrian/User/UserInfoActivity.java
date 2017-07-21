package com.buxingzhe.pedestrian.User;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseActivity;
import com.buxingzhe.pedestrian.activity.MainActivity;
import com.buxingzhe.pedestrian.application.PDApplication;
import com.buxingzhe.pedestrian.common.GlobalParams;
import com.buxingzhe.pedestrian.http.manager.NetRequestManager;
import com.buxingzhe.pedestrian.utils.CropUtils;
import com.buxingzhe.pedestrian.utils.FileConfig;
import com.buxingzhe.pedestrian.utils.PicUtil;
import com.buxingzhe.pedestrian.utils.SelectPicPopupWindow;
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
    private SelectPicPopupWindow popupWindown;
    private static final int REQUEST_CODE_ALBUM = 0;
    private static final int REQUEST_CODE_TAKE_PHOTO = 1;
    private static final int REQUEST_CODE_CROUP_PHOTO = 2;
    private Uri uri;
    private File file; //take photo
    private String fileSrc;//choose album
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        ButterKnife.bind(this);
        initView();
        initPopuWindow();

        initFile();

    }

    private void initFile() {
        file = new File(FileConfig.IMAGE_UP_PATH, "user-avatar.jpg");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            uri = Uri.fromFile(file);
        } else {
            //通过FileProvider创建一个content类型的Uri(android 7.0需要)
            uri = FileProvider.getUriForFile(PDApplication.getApp(), FileConfig.IMAGE_UP_PATH, file);
        }
    }

    private void initPopuWindow() {
        popupWindown = new SelectPicPopupWindow();
        popupWindown.setOnclick(itemsOnclickListener);
        popupWindown.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setParams(1.0f);
            }
        });
        popupWindown.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindown.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
    private void setParams(float f) {
        WindowManager.LayoutParams params =UserInfoActivity.this.getWindow().getAttributes();
        params.alpha = f;
        params.dimAmount = f;
        UserInfoActivity.this.getWindow().setAttributes(params);
        UserInfoActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

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

                //PicUtil.showPicDialog(this);
                popupWindown.showAtLocation(llUserHead, Gravity.CENTER | Gravity.BOTTOM, 0, 0);
                setParams(0.4f);
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
    protected void onBack() {
        super.onBack();
       // finish();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*默认为-1，为0表示用户取消操作，则直接返回*/
        if (resultCode != -1) {
            return;
        }
        if (requestCode == REQUEST_CODE_ALBUM && data != null&&resultCode== RESULT_OK) {
            Uri newUri;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                //将相册的图片的路径转成uri
                newUri = Uri.parse("file:///" + CropUtils.getPath(UserInfoActivity.this, data.getData()));
            } else {
                newUri = data.getData();
            }
            if (newUri != null) {
                cropImageUri(newUri);

            } else {
                Toast.makeText(UserInfoActivity.this, "没有得到相册图片",Toast.LENGTH_SHORT).show();
            }
            uploadAvatarFromAlbum(data);

        } else if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
            startPhotoZoom(uri);
        } else if (requestCode == REQUEST_CODE_CROUP_PHOTO) {

            upDatePic();

        }

    }

    private void upDatePic() {
        final File cover = FileUtil.getSmallBitmap(UserInfoActivity.this, file.getPath());

        uploadFile=cover;
        Glide
                .with(UserInfoActivity.this)
                .load(cover.getPath())
                .override(SystemUtils.dip2px(this, 67.0f), SystemUtils.dip2px(this, 67.0f))
                .centerCrop()
                .into((ImageView) findViewById(R.id.sdv_userHead));


    }


    private View.OnClickListener itemsOnclickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            popupWindown.dismiss();
            switch (v.getId()) {
                case R.id.register_set_camera:
                    uploadAvatarFromPhotoRequest();
                    break;
                case R.id.register_take_photo:
                    //takeSetPhotos();
                    uploadAvatarFromAlbumRequest();
                    break;
                case R.id.register_set_cancle:
                    popupWindown.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    private void uploadAvatarFromAlbumRequest() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_ALBUM);
    }

    public void uploadAvatarFromPhotoRequest() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
    }


    /**
     * 调用系统相册后进行裁剪图片
     *
     * @param orgUri
     */
    public void cropImageUri(Uri orgUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(orgUri, "image/*");
        intent.putExtra("crop", "true");// crop=true 有这句才能出来最后的裁剪页面.
        intent.putExtra("aspectX", 1);// 这两项为裁剪框的比例.
        intent.putExtra("aspectY", 1);// x:y=1:1
        intent.putExtra("outputX",100);//图片输出大小
        intent.putExtra("outputY", 100);
        intent.putExtra("output", Uri.fromFile(file));
        intent.putExtra("outputFormat", "JPEG");// 返回格式
        startActivityForResult(intent, REQUEST_CODE_CROUP_PHOTO);
    }

    /**
     * 裁剪图片
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("crop", "true");// crop=true 有这句才能出来最后的裁剪页面.
        intent.putExtra("aspectX", 1);// 这两项为裁剪框的比例.
        intent.putExtra("aspectY", 1);// x:y=1:1
        intent.putExtra("outputX", 100);//图片输出大小
        intent.putExtra("outputY", 100);
        intent.putExtra("output", Uri.fromFile(file));
        intent.putExtra("outputFormat", "JPEG");// 返回格式
        startActivityForResult(intent, REQUEST_CODE_CROUP_PHOTO);
    }


    private void uploadAvatarFromAlbum(Intent data) {
        Cursor cursor =UserInfoActivity.this.getContentResolver().query(data.getData(), null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            fileSrc = cursor.getString(idx);
            cursor.close();
        }

    }


}
