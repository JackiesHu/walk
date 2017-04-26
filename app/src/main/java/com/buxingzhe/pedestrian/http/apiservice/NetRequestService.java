package com.buxingzhe.pedestrian.http.apiservice;

import com.buxingzhe.pedestrian.bean.RequestResultInfo;
import com.buxingzhe.pedestrian.found.bean.Streets;
import com.buxingzhe.pedestrian.http.NetRequestParams;

import java.util.Map;
import java.util.function.DoubleUnaryOperator;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zhaishaoping on 27/03/2017.
 */

public interface NetRequestService {

    //用户登陆
    @FormUrlEncoded
    @POST(NetRequestParams.WALK_USER_LOGIN)
    Observable</* RequestResultInfo<UserLoginResultInfo> */ String> login(@FieldMap Map<String, String> loginMap);

    @FormUrlEncoded
    @POST(NetRequestParams.WALK_USER_LOGIN_PHONE)
    Observable</*  RequestResultInfo<UserLoginResultInfo> */ String> loginByPhone(@FieldMap Map<String, String> loginMap);

    //修改用户信息
    @FormUrlEncoded
    @POST(NetRequestParams.WALK_USER_MODIFY_USER_INFO)
    Observable</* RequestResultInfo<UserModifyInfo> */ String> modifyUserInfo(@FieldMap Map<String, String> modifyInfoMap);
    //修改用户信息
    @FormUrlEncoded
    @POST(NetRequestParams.WALK_USER_MODIFY_USER_INFO)
    Observable</* RequestResultInfo<UserModifyInfo> */ String> modifyUserInfo2(@FieldMap Map<String, String> modifyInfoMap);
    //增加用户步票，（社交网络分享步行记录后调用）
    @FormUrlEncoded
    @POST(NetRequestParams.WALK_USER_ADD_WALK_NUM)
    Observable</* RequestResultInfo<UserAddWalkNumInfo> */ String> addWalkNum(@FieldMap Map<String, String> walkNumMap);

    //查询用户信息
    @FormUrlEncoded
    @POST(NetRequestParams.WALK_USER_GET_INFO)
    Observable<String> getUserInfo(@Field("userId") String userId, @Field("token") String token);

    //用户注册
    @FormUrlEncoded
    @POST(NetRequestParams.WALK_USER_REGISTER)
    Observable</* RequestResultInfo<UserLoginResultInfo> */ String> register(@FieldMap Map<String, String> registerMap);

    //上传步行或者骑行记录
    @FormUrlEncoded
    @POST(NetRequestParams.WALK_UPLOAD_WALK)
    Observable</* RequestResultInfo<String> */ String> uploadWalkRecord(@FieldMap Map<String, String> registerMap);

    //获取步行或者骑行记录
    @FormUrlEncoded
    @POST(NetRequestParams.WALK_GET_WALK_RECORD)
    Observable</* RequestResultInfo<WalkRecordResultInfo> */ String> getWalkRecord(@Field("recordId") String recordId);


    //获取当天天气 // cityName: 城市名，不包含区，市等字符 不可空
    @FormUrlEncoded
    @POST(NetRequestParams.WALK_GET_CURRENT_WEATHER)
    Observable</* RequestResultInfo<WalkWeatherInfo> */ String> getCurrentWeather(@Field("cityName") String cityName);

    //获取历史天气  //  data: 日期 格式为20170401
    @FormUrlEncoded
    @POST(NetRequestParams.WALK_GET_HISTORY_WEATHER)
    Observable</* RequestResultInfo<WalkWeatherInfo> */ String> getHistoryWeather(@Field("cityName") String cityName, @Field("date") String date);

    //我的跑团
    @FormUrlEncoded
    @POST(NetRequestParams.WALK_MINE_RUN_TEAM)
    Observable</* RequestResultInfo<WalkWeatherInfo> */ String> getMineRunTeam(@FieldMap Map<String, String> runTeamMap);

    //查询圈子
    @FormUrlEncoded
    @POST(NetRequestParams.WALK_GET_WALK_RECORDS)
    Observable</* RequestResultInfo<WalkWeatherInfo> */ String> getWalkRecords(@Field("userId") String userId, @Field("pageNo") int pageNo, @Field("pageSize") int pageSize);

    //活动查询
    @FormUrlEncoded
    @POST(NetRequestParams.WALK_GET_ACTIVITYS)
    Observable</* RequestResultInfo<WalkWeatherInfo> */ String> getActivities(@Field("pageNo") int pageNo, @Field("pageSize") int pageSize);

    //查询该活动下的记录
    @FormUrlEncoded
    @POST(NetRequestParams.WALK_GET_RECORDSBYACT)
    Observable</* RequestResultInfo<WalkWeatherInfo> */ String> getWalkRecordsByActivity(@Field("userId") String userId, @Field("activity") String activity, @Field("pageNo") int pageNo, @Field("pageSize") int pageSize);


    //步行或骑行记录点赞
    @FormUrlEncoded
    @POST(NetRequestParams.WALK_LIKE_RECORD)
    Observable</* RequestResultInfo<WalkWeatherInfo> */ String> walkRecordLike(@Field("userId") String userId, @Field("token") String token, @Field("walkRecord") String walkRecord);

    //评论步行或骑行记录
    @FormUrlEncoded
    @POST(NetRequestParams.WALK_COMMENT_RECORD)
    Observable</* RequestResultInfo<WalkWeatherInfo> */ String> walkRecordComment(@Field("userId") String userId, @Field("token") String token, @Field("walkRecord") String walkRecord, @Field("star") String star, @Field("streetStar") Double streetStar, @Field("envirStar") Double envirStar, @Field("safeStar") Double safeStar, @Field("content") String content);

    //发布活动
//    @FormUrlEncoded
//    @POST(NetRequestParams.WALK_PUBLISH_ACTVITY)
//    Observable</* RequestResultInfo<WalkWeatherInfo> */ String> publishActivity(@Field("userId") String userId, @Field("token") String token,
//                                                                                @Field("title") String title, @Field("Long") String startTimestamp
//            , @Field("endTimestamp") Long endTimestamp, @Field("introduction") String introduction, @Field("bannerUrl") Data bannerUrl, @Field("publisher") String publisher);
    //附近街道
    @FormUrlEncoded
    @POST(NetRequestParams.STREETS_NEARBY_FOUND)
    Observable</* RequestResultInfo<WalkWeatherInfo> */ String> getStreets(@FieldMap Map<String, String> runTeamMap);

    @FormUrlEncoded
    @POST(NetRequestParams.REMARK_POINTS_NEARBY_FOUND)
    Observable</* RequestResultInfo<WalkWeatherInfo> */ String> getNearByPoints(@FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST(NetRequestParams.REMARK_POINTS_COMMENT_FOUND)
    Observable</* RequestResultInfo<WalkWeatherInfo> */ String> getPointComments(@FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST(NetRequestParams.ADD_POINT_COMMENT)
    Observable</* RequestResultInfo<WalkWeatherInfo> */ String> pointComment(Map<String, String> paramsMap);
}
