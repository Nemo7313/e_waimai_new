package com.e_waimai;

import com.e_waimai.net.ANetClient;
import com.e_waimai.net.IMessage;
import com.e_waimai.net.message.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;
import java.util.Scanner;

public class RestaurantClient {
    private static Logger logger = LogManager.getLogger("APP");
    private static Scanner sc = new Scanner(System.in);
    static final int VCODE = 1;
    static final int LOGIN = 2;
    static final int REGISTER = 3;
    static final int ADDFOOD = 4;
    static final int SELECTORDER = 5;
    static final int LOGOUT = 6;
    private static boolean flag = true;

    public static void main(String[] args) {
        logger.debug("餐馆客户端准备启动");
        ANetClient client = new ANetClient("127.0.0.1", 9999) {
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

        logger.debug("客户端Socket创建完成！");
        Thread task = new Thread(client);
        task.setDaemon(true);
        task.start();
        logger.debug("消息接收线程启动");

        String phone = null;
        do{
            showMenu();
            int t = sc.nextInt();
            String enter = sc.nextLine();
            switch (t){
                case VCODE:
                    System.out.print("请输入手机号：");
                    phone = sc.nextLine();
                    VCodeMsg vMsg = new VCodeMsg(phone);
                    client.send(vMsg);
                    logger.debug("电话号码已发送，等待验证码！");
                    break;
                case LOGIN:
                    System.out.print("请输入验证码：");
                    String code = sc.nextLine();
                    ResLoginMsg rlMsg = new ResLoginMsg(phone, code);
                    client.send(rlMsg);
                    logger.debug("验证码向服务器提交，等待验证！");
                    break;
                case REGISTER:
                    System.out.print("请输入店名：");
                    String name = sc.nextLine();
                    System.out.print("请输入地址：");
                    String addr = sc.nextLine();
                    System.out.print("请输入简介：");
                    String intro = sc.nextLine();
                    ResRegisterMsg registerMsg = new ResRegisterMsg(name, addr, intro);
                    client.send(registerMsg);
                    logger.debug("商家注册信息已发送");
                    break;
                case ADDFOOD:
                    System.out.print("请输入菜名：");
                    String foodName = sc.nextLine();
                    System.out.print("请输入价格：");
                    String price = sc.nextLine();
                    double foodPrice = Double.parseDouble(price);
                    System.out.print("请输入简介：");
                    String foodIntro = sc.nextLine();
                    ResAddFoodMsg addFoodMsg = new ResAddFoodMsg(foodName, foodPrice, foodIntro);
                    client.send(addFoodMsg);
                    logger.debug("增加菜品信息已发送");
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
        client.stop();
        logger.info("客户端正确退出");
    }
    static void showMenu(){
        System.out.println("======商家测试客户端======");
        System.out.println("1，请求验证码");
        System.out.println("2，请求登录");
        System.out.println("3，请求注册(需要先登录)");
        System.out.println("4，增加菜品(需要先登录)");
        System.out.println("5，查询订单(需要先登录)");
        System.out.println("6，退出");
        System.out.println("=========================");
    }
}
