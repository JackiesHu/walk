package com.buxingzhe.pedestrian.http;

/**
 * Created by zhaishaoping on 27/03/2017.
 */

public class NetRequestParams {

    //*************************** Server ****************************
    public static final String WALK_SERVER_HOST = "http://115.28.109.174:8282/"; // http://222.128.13.159:8585/walkman //http://115.28.109.174:8282/walkman/
//    public static final String WALK_SERVER_HOST = "http://pacers.yjxtech.com/api/";


    //*************************** User ****************************
    /** 手机号登陆 */
    public static final String WALK_USER_LOGIN_PHONE = "walkman/wmUser/mobileLogin";
    /** 用户登陆 */
    public static final String WALK_USER_LOGIN = "walkman/wmUser/login";
    /** 修改用户信息 */
    public static final String WALK_USER_MODIFY_USER_INFO = "walkman/wmUser/modifyUserInformationAndAvatarAndroid";
    /** 增加用户步票 */
    public static final String WALK_USER_ADD_WALK_NUM = "walkman/wmUser/addWalkMoneyAmount";
    /** 获取用户信息 */
    public static final String WALK_USER_GET_INFO = "walkman/wmUser/getUserInformation";
    /** 查询用户排名情况 */
    public static final String WALK_USER_GET_RANKS = "walkman/wmUser/getUserRanks";
    /** 用户注册 */
    public static final String WALK_USER_REGISTER = "walkman/wmUser/registe";


    //*************************** Walk ****************************
    /** 上传步行或者骑行记录 */
    public static final String WALK_UPLOAD_WALK = "walkman/wmWalkRecord/addWalkRecordAndroid";
    /** 获取步行或者骑行记录*/
    public static final String WALK_GET_WALK_RECORD = "walkman/wmWalkRecord/getWalkRecord";


    //*************************** Weather ****************************
    /** 获取今天天气信息 */
    public static final String WALK_GET_CURRENT_WEATHER = "walkman/wmWeather/getCurWeather";
    /** 获取最新发布活动 */
    public static final String WALK_GET_LATEST_ACTIVITY = "walkman/wmActivity/getLatestActivity";
    /** 获取历史天气信息 */
    public static final String WALK_GET_HISTORY_WEATHER = "walkman/wmWeather/getHistoryWeather";

    //*************************** mine ****************************
    /** 我的跑团*/
    public static final String WALK_MINE_RUN_TEAM = "walkman/wmActivity/getActivityByPublisher";
    /** 我的任务*/
    public static final String WALK_MY_TASK = "walkman/wmActivity/getActivityByUserId";


    //*************************** common ****************************
    /** 活动查询*/
    public static final String WALK_GET_ACTIVITYS = "walkman/wmActivity/getActivities";
    /** 查询圈子*/
    public static final String WALK_GET_WALK_RECORDS = "walkman/wmWalkRecord/getWalkRecords";
    public static final String WALK_GET_MY_WALK_RECORDS = "walkman/wmWalkRecord/getWalkRecordsByUserId";
    /** 查询该活动下的记录*/
    public static final String WALK_GET_RECORDSBYACT = "walkman/wmWalkRecord/getWalkRecordsByActivity";
    /** 步行或骑行记录点赞*/
    public static final String WALK_LIKE_RECORD = "walkman/wmWalkRecordLike/walkRecordLike";
    /** 评论步行或骑行记录*/
    public static final String WALK_COMMENT_RECORD = "walkman/wmWalkRecordComment/walkRecordComment";
    /** 发布活动*/
    public static final String PUBLISHACTIVITY = "walkman/wmActivity/publishActivityAndroid";


    //*************************** found ****************************
    /** 附近街道*/
    public static final String STREETS_NEARBY_FOUND = "walkman/wmStreet/nearbyStreets";
    /** 附近标记点*/
    public static final String REMARK_POINTS_NEARBY_FOUND = "walkman/wmRemarkPoint/nearby";
    /** 获取标记点评论*/
    public static final String REMARK_POINTS_COMMENT_FOUND = "walkman/wmRemarkPointComment/getRemarkPointComments";
    /** 评论标记点*/
    public static final String ADD_POINT_COMMENT = "walkman/wmRemarkPointComment/addRemarkPointComment";
    /** 发现推荐或吐槽*/
    public static final String FOUND_COMMENT = "walkman/wmRemarkPoint/remarkPoint";
    /** 查询标签*/
    public static final String QUERY_TAG = "walkman/wmTagType/getTags";
    /** 根据标签查询步行记录*/
    public static final String WALK_RECORD_BY_TAG = "walkman/wmWalkRecord/getWalkRecordsByTag";
    /** 根据标题查询步行记录*/
    public static final String WALK_RECORD_BY_TITLE = "walkman/wmWalkRecord/getWalkRecordsByTitle";
    /**
     * 根据时间段获取步行记录
     **/
    public static final String WALK_RECORD_BY_DAY = "walkman/wmWalkRecord/getDayWalkRecord";

}
