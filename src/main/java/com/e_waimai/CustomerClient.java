package com.e_waimai;

import com.e_waimai.net.ANetClient;
import com.e_waimai.net.IMessage;
import com.e_waimai.net.message.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class CustomerClient {
    private static Logger logger = LogManager.getLogger("APP");
    private static Scanner sc = new Scanner(System.in);
    static final int VCODE = 1;
    static final int LOGIN = 2;
    static final int REGISTER = 3;
    static final int ORDER = 4;
    static final int SELECTORDER = 5;
    static final int LOGOUT = 6;
    private static boolean flag = true;

    public static void main(String[] args) {
        logger.debug("顾客客户端准备启动");
        ANetClient customer = new ANetClient("127.0.0.1",9999) {
            @Override
            public void onMessage(IMessage event) {
                int t = event.getMsgType();
                switch(t){
                    case IMessage.TEXT_MSG:
                        logger.debug("文本消息");
                        TextMsg textMsg = (TextMsg)event;
                        System.out.println(textMsg.getMessage());
                        break;
                }
            }
        };

        logger.debug("顾客客户端Socket创建完成！");
        Thread task = new Thread(customer);
        task.setDaemon(true);
        task.start();
        logger.debug("顾客客户端消息接收线程启动");

        String phone = null;
        do{
            showMenu();
            int t = sc.nextInt();
            String enter = sc.nextLine();
            switch (t){
                case VCODE:
                    System.out.print("请输入手机号：");
                    phone = sc.nextLine();
                    CusVcodeMsg cusVcodeMsg = new CusVcodeMsg(phone);
                    customer.send(cusVcodeMsg);
                    logger.debug("电话号码已发送，等待验证码！");
                    break;
                case LOGIN:
                    System.out.print("请输入验证码：");
                    String code = sc.nextLine();
                    CusLoginMsg cusLoginMsg = new CusLoginMsg(phone, code);
                    customer.send(cusLoginMsg);
                    logger.debug("验证码向服务器提交，等待验证！");
                    break;
                case REGISTER:

                    logger.debug("用户注册信息已发送");
                    break;
                case ORDER:
                    logger.debug("点餐信息信息已发送");
                    break;
                case SELECTORDER:
                    break;
                case LOGOUT:
                    flag = false;
                    break;
                default:
                    break;
            }
        }while(flag);
        customer.stop();
        logger.info("顾客客户端正确退出");
    }


    static void showMenu(){
        System.out.println("======顾客测试客户端======");
        System.out.println("1，请求验证码");
        System.out.println("2，请求登录");
        System.out.println("3，请求注册(需要先登录)");
        System.out.println("4，点餐(需要先登录)");
        System.out.println("5，查询订单(需要先登录)");
        System.out.println("6，退出");
        System.out.println("=========================");
    }
}
