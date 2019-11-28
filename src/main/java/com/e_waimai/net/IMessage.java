package com.e_waimai.net;

import java.io.Serializable;

public interface IMessage {
    int TEXT_MSG = 0; //普通文字消息
    int BOOLEAN_MSG = 10; //标定状态消息
    int REQ_VCODE = 1000;//请求注册或登录验证码,仅限商家
    int REQ_VCODE_CUS = 1100;//请求注册或登录验证码,仅限顾客

    int REQ_LOGIN_RS = 2000;//商家客户端请求登陆
    int RS_REG = 2010; //商家注册
    int RS_UPDATE = 2011;//商家修改信息

    int RS_ADD_FOOD = 2020;//添加菜品
    int RS_REC_ORDER = 2500;//商家接到订单
    int RS_DATA = 2510;//服务器下发商家数据给商家客户端

    int REQ_LOGIN_CS = 3000;//顾客端登录
    int CS_REG = 3010;//顾客注册
    int CS_ORDER_FOOD = 3020; //顾客开始点餐
    int Food_List = 3021;
    int ADD_PRICE = 3022;
    int CREATE_NEW_ORDER = 3033;

    int getMsgType();
}
