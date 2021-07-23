package com.allinfinance.dev.ccs.content;

public  class AosContent {
    public static  final String  LEVEL_0="0";//根目录
    public static  final String  LEVEL_1="1";//一级菜单
    public static  final String  LEVEL_2="2";//二级菜单
    public static  final String  MENU_BTN="3";//菜单按钮

    // 此状态为权限控制框架需要，业务无需更改
    public static  final String  ACCOUNT_OK="1"; //账户正常
    public static  final String  ACCOUNT_EXP="0";// 账户异常


    public static final String AOS_TOKEN="token";
    public static final String AOS_REFRESH_TOKEN="refreshToken";


    public static final String NOT_BIND="0";
    public static final String IS_BIND="1";

    public static final String ALLINFINANCE_ORG="000000000000";

    public static final String FIRST_PASS="0";
    public static final String NOT_FIRST_PASS="1";

    public static final String IS_AVAILABLE_TRUE="Y";//有效
    public static final String IS_AVAILABLE_FALSE="N";//无效

    public static final String MENU_ID_ROOT="001";//根节点初始化值

    //操作类型
    public static final String QUERY = "query";
    public static final String DELETE = "delete";
    public static final String UPDATE = "update";
    public static final String INSERT = "insert";
    public static final String DOWNLOAD = "download";
    public static final String UPLOAD = "upload";
    public static final String OTHER = "other";

}
