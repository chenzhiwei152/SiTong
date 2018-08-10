package com.jyall.bbzf.api.scheduler


/**
 * 接口地址
 */
object APIAddressConstants {


    /**
     * 获取基础地址，此地址是最主要的接口地址
     */
    const val baseUrl: String = "api.bibizf.com"
    //    const val baseUrl: String = "10.10.38.88:8080"
//        const val baseUrl: String = "192.168.3.44:8080"
//        const val baseUrl: String = "10.10.38.89"//测试
//        const val baseUrl: String = "10.10.39.89"//开发
//        const val baseUrl: String = "10.10.39.63"//新开发
//    const val baseUrl: String = "118.144.87.69:8080"
    val APP_HOST = "http://$baseUrl/"//
    const val USER_API: String = "api"
    private val M_BASE_URL = "http://m.bibizf.com/"
    /**
     * 我的收益，活动声明
     */
    val URL_TOPIC = M_BASE_URL + "xieyi/10090.html"
    /**
     * 协议
     */
    val URL_XIEYI = M_BASE_URL + "xieyi/10087.html"
    /**
     * 房源详情，法律与免责声明
     */
    val RENT_URL = M_BASE_URL + "xieyi/10084.html"

    //    登陆
    const val USER_LOGIN: String = "$USER_API/basic/register.do"

    /*
        * 上传文件
        * */
    const val UPLOAD_FILES = "$USER_API/base64Upload.do"
    /*
        * 版本升级
        * */
    const val CHECK_VERSION = "$USER_API/basic/getAppVersion.do"
    /*
        * 获取首页列表
        * */
    const val GET_HOME_LIST = "$USER_API/basic/getIndexRecommendHouseList.do"
    /*
        * 获取首页广告位
        * */
    const val GET_HOME_AD_LIST = "$USER_API/basic/getAdvertising.do"

    /*
        * 获取区域
        * */
    const val GET_AREA = "$USER_API/basic/getCityDataByParentId.do"
    /*
        * 获取城市列表
        * */
    const val GET_CITY_LIST = "$USER_API/basic/getOpenCityData.do"

    /**
     * 获取字典数据
     */
    const val GET_DICTIONARY = "$USER_API/basic/getCodeDataByPid.do"

    /**
     * 联想搜索
     */
    const val GET_SEARCHDATA = "$USER_API/basic/getLikeNameData.do"

    /**
     * 获取商圈列表
     */
    const val GET_AREA_TRADE_DATA = "$USER_API/basic/getAreaTradeData.do"

    /**
     * 获取地铁列表
     */
    const val GET_SUBWAY_DATA = "$USER_API/basic/getMetroData.do"
    /*
   * 全局配置
   * */
    const val GET_CONFIG_GLOABLE = "$USER_API/basic/getSysConfigure.do"

    /**
     * 我的收益
     */
    const val GET_INCOME = "$USER_API/getMyMoney.do"

    /**
     *  拉新活动
     */
    const val GET_INVITE_LIST = "$USER_API/"

    /**
     * 我的预约
     *
     */
    const val GET_RESERVATION = "$USER_API/getMyBespeak.do"
    /**
     * 预约详情
     *
     */
    const val GET_RESERVATION_DETAIL = "$USER_API/getBespeakInfo.do"
    /**
     * 预约---  评价详情
     *
     */
    const val GET_RESERVATION_COMMENT_DETAIL = "$USER_API/getEvaluate.do"
    /**
     * 预约---  提交评价
     *
     */
    const val COMMIT_RESERVATION_DETAIL = "$USER_API/userCommentBespeak.do"

    /**
     *  提现
     */
    const val USER_PRESENT = "$USER_API/userPresent.do"

    /**
     *  提现记录 收益记录
     */
    const val GET_MONEY_INOUT = "$USER_API/getMyMoneyInout.do"

    /**
     * 获取房源详情
     * */
    const val GET_HOUSE_INFODATA = "$USER_API/basic/getHouseInfoData.do"

    /**
     * 小区详情
     * */
    const val GET_VILLAGE_INFODATA = "$USER_API/basic/getVillageInfoData.do"

    /**
     * 经纪人拉黑用户列表
     * */
    const val GET_BROKER_BLACKLIST = "$USER_API/getBrokerBlackList.do"

    /**
     * 用户拉黑经纪人列表
     * */
    const val GET_USER_BLACKLIST = "$USER_API/getUserBlackList.do"


    /**
     * 用户解除黑名单
     * */
    const val USER_REMOVE_BLACKLIST = "$USER_API/userRemoveBlackList.do"

    /**
     * 经纪人解除黑名单
     * */
    const val BROKER_REMOVE_BLACKLIST = "$USER_API/brokerRemoveBlackList.do"

    /**
     * 提交预约数据
     * */
    const val USER_BESPEAK_EDIT = "$USER_API/userBespeakEdit.do"
    /**
     * 修改预约数据
     * */
    const val UPDATE_USER_BESPEAK = "$USER_API/updateUserBespeakEdit.do"

    /**
     * 小区收藏/取消
     * */
    const val USER__VILLAGE_COLLECT = "$USER_API/userVillageCollect.do"

    /**
     * 房源收藏/取消
     * */
    const val USER_HOUSE_COLLECT = "$USER_API/userHouseCollect.do"


    /**
     * 根据小区/商圈获取房源数据
     * */
    const val GET_HOUSE_BYTRADE_VILLAGE = "$USER_API/basic/getHouseListByTradeVillage.do"

    /**
     *  线下看房
     */
    const val GET_MY_BEASPAK = "$USER_API/getMyBespeak.do"
    /**
     *  取消线下看房
     */
    const val USER_BEASPAK_STATUS = "$USER_API/userBespeakStatus.do"

    /**
     *  查看经纪人
     */
    const val GET_HOUSE_BROKER = "$USER_API/getHouseBroker.do"

    /**
     *  我的客户
     */
    const val GET_MY_CUSTOMER = "$USER_API/getMyCustomer.do"

    /**
     *  我认领的房源
     */
    const val GET_MY_BROKERHOUSE_LIST = "$USER_API/getMyBrokerHouseList.do"

    /**
     *  我发布的房源
     */
    const val GET_MYHOUSE_LIST = "$USER_API/getMyHouseList.do"

    /**
     *  我认领的小区
     */
    const val GET_MY_RECEIVE_VILLAGE = "$USER_API/getMyReceiveVillage.do"

    /**
     *  我认领的商圈
     */
    const val GET_MY_RECEIVE_TRADE = "$USER_API/getMyReceiveTrade.do"

    /**
     *  认领房源
     */
    const val BROKER_RECEIVE_HOUSE = "$USER_API/brokerReceiveHouse.do"

    /**
     * 浏览记录-房源/小区
     */
    const val GET_MY_BROWSE_HOUSE = "$USER_API/getMyBrowseHouse.do"

    /**
     * 收藏的房源
     */
    const val GET_MY_COLLENT_HOUSE = "$USER_API/getMyCollentHouse.do"

    /**
     * 收藏的小区
     */
    const val GET_MY_COLLENT_VILLAGE = "$USER_API/getMyCollentVillage.do"
    /**
     * 比一比列表
     */
    const val GET_MY_RECOR_HOUSE = "$USER_API/getMyRecordHouse.do"
    /**
     * 比一比详情
     */
    const val GET_MY_RECOR_RECORD = "$USER_API/getMyBespeakRecord.do"
    /**
     * 反馈
     */
    const val USER_COMMIT_FEEDBACK = "$USER_API/basic/userConsultInfo.do"

    /**
     * 客户意向度
     */
    const val BROKER_PINGJI_USER = "$USER_API/brokerRatingBespeak.do"
    /**
     * 举报
     */
    const val USER_REPORT_BROKER = "$USER_API/userReportBroker.do"

    /*---------------------------------------------登录注册相关start-------------------------*/
    /**
     *  发送短信验证码
     */
    const val SEND_SHORT_MESSAGE = "$USER_API/basic/sendShortMessage.do"
    /**
     *  校验短信验证码
     */
    const val CHECK_SHORT_MESSAGE = "$USER_API/basic/checkShortMessage.do"


    /*---------------------------------------------登录注册相关end-------------------------*/


    /**
     *  系统消息列表
     */
    const val GET_SYSTEM_MESSAGE = "$USER_API/getSystemMessage.do"

    /**
     *  接单记录
     */
    const val ORDER_LIST = "$USER_API/getHouseByUserBroker.do"

    /**
     *  认领商圈/小区
     */
    const val CLAIM_TRADE_VILLAGE = "$USER_API/bokerTradeVillage.do"

    /**
     *  发起视频聊天记录接口
     */
    const val SAVE_VIDEO_CHANNEL_ID = "$USER_API/saveVideoChannelid.do"


    /**
     *  标识IM聊天数据
     */
    const val INIT_USER_IM_INFO = "$USER_API/initUserImInfo.do"

    /**
     *  判断是否拉黑或被拉黑
     */
    const val IS_BLACK_LIST = "$USER_API/isBlackList.do"

    /**
     *  用户/经纪人加入黑名单（完成）
     */
    const val ADD_BLACK_LIST = "$USER_API/setSpecialRelation.do"

    /**
     *  获取 经纪人抢单列表
     */
    const val GET_GRAB_LIST = "$USER_API/brokerGetBespeakList.do"

    /**
     *  获取 客户/经纪人 预约单带看列表
     */
    const val GET_DAI_KAN_LIST = "$USER_API/getBespeakDaikan.do"
    /**
     *  经纪人抢单
     */
    const val BROKER_GRAB = "$USER_API/brokerGetBespeak.do"

    /**
     *  经纪人不抢单
     */
    const val BROKER_IGNORE = "$USER_API/brokerNoBespeak.do"

    /**
     *  设置系统消息已读
     */
    const val UPDATE_MSG_READED = "$USER_API/updateMsgReadStatus.do"

    /**
     *  获取用户所有未读系统消息数量
     */
    const val SYS_MESSAGE_UNREAD_COUNT = "$USER_API/getUserWaitMsgCount.do"

    /**
     *  获取小区列表
     */
    const val GET_VILLAGE_LIST = "$USER_API/basic/getVillageInfoList.do"

    /**
     *  根据名称搜索公司
     */
    const val SEARCH_COMPANY = "$USER_API/basic/getCompanyLikeName.do"
    /**
     *  上传图片
     */
    const val UPLOAD_BASE64 = "$USER_API/base64Upload.do"
    /**
     *  更新用户信息
     */
    const val UPDATE_USER_INFO = "$USER_API/updateUserInfo.do"
    /**
     *  获取用户信息
     */
    const val GET_USER_INFO = "$USER_API/getUserInfo.do"
    /**
     * 更新笔记本
     */
    const val UPDATE_NOTE_BOOK = "$USER_API/updateMyBespeakRecord.do"
    /**
     * 笔记本详情
     */
    const val GET_NOTE_BOOK = "$USER_API/getMyBespeakRecord.do"
    /**
     *  获取小区
     */
    const val GET_HOUSE_LIST = "$USER_API/basic/getHouseList.do"

    /**
     *  保存devicePush
     */
    const val SAVE_DEVICE_PUSH = "$USER_API/saveDevicePush.do"

    /**
     *  保存devicePush
     */
    const val LOGOUT = "$USER_API/untieDeviceId.do"
    /**
     *  首页弹屏
     */
    const val INDEX_AD = "$USER_API/basic/getScreenInfo.do"

}
