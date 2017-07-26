package com.funoble.myarstation.protocol;

public class RequestUrlTools
{
/**
 *  获取SKEY的请求地址
 */
public final static String SKEY_URL="http://ptlogin2.qq.com/361everywhere";
//public final static String SKEY_URL="http://119.147.14.227:14000";
/**
 * 361项目的网址
 */
public final static String MAIN_URL="http://api.361everywhere.qq.com";
//public final static String MAIN_URL="http://124.115.12.45";
//public final static String MAIN_URL="http://10.6.208.188";

/** 点击按钮事件发送协议的地址 change by jason 07-13 */
public final static String RECORD_OPERATE_URL = "http://act.t.l.qq.com";

/**
 * 搜索列表请求地址
 */
public final static String search_store_URL="/sport/search";
/**
 * 支持者的请求地址
 */
public final static String SupportList_URL="/evaluate/sportSupportList";
public final static String  EvaluateList_URL="/evaluate/sportList";

/**
 * 顶场馆请求地址
 */
public final static String sport_up_URL="/evaluate/support";
/**
 * 最新场馆列表请求地址
 */
public final static String  new_store_URL="/list/new";
/**
 * 创建场馆请求地址
 */
public final static String  createsport_URL="/sport/create";
/**
 * 修改场馆的请求地址
 */
public final static String eitesport_URL="/sport/edit";
public final static String eitestar="/evaluate/star?sportId=1&star=1";
/**
 * 评论请求地址
 */
public final static String evaluatecreate_URL="/evaluate/create";
/**
 * 滚动公告
 */
public final static String feeds_URL="/sport/link";
/**
 * 我的场馆
 */
public final static String My_Sport_URL="/sport/mysport";
/**
 * 个人中心请求地址
 */
public final static String MyZone_URL="/evaluate/total";
/**
 * 我的足迹列表
 * 
 */
public final static String My_action_URL="/evaluate/userActionList";
/**
 * 我的评论列表
 */
public final static String My_evaluate_URL="/evaluate/userList";
/**
 * 我的支持列表
 */
public final static String My_Suppor_URL="/evaluate/userSupportList";
/**
 * 场馆详细
 */
public final static String Sport_detail_URL="/sport/detail";

/** 热词搜索 change by jason 06-27 */
public final static String Hotword_URL = "/sport/keyword";

/** 发微博 change by jason 06-28 */
public final static String Sendblog_URL = "/micoblog/sharetext";

/** 轮播的查看更多按钮的请求地址 change by jason 06-29*/
public final static String 	btn_new_store_URL="/list/newpage";
/** 最热列表 change by jason 06-30*/
public final static String 	best_hot_store_URL = "/list/hotpage";

/**
 * 推荐场地
 */
public final static String DaPengChe_Store_URL="/list/recommend";
/**
 * google 地理位置服务
 */
public final static String GOOGLE_GEO_SERVICE = "http://ditu.google.cn/maps/geo";
/**
 * 未回复反馈
 */
public final static String Suggest_URL = "/suggest/adminnewlist";

/**
 * 已回复反馈
 */
public final static String Reply_URL = "/suggest/adminreplylist";
/**
 * 提交反馈
 */
public final static String Create_Suggest_URL = "/suggest/create";
}
