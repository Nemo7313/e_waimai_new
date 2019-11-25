package com.e_waimai;

import com.e_waimai.net.ANetClient;
import com.e_waimai.net.IMessage;
import com.e_waimai.net.message.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CustomerClient {
    private static Logger logger = LogManager.getLogger("APP");
    private static Scanner sc = new Scanner(System.in);
    static final int VCODE = 1;
    static final int LOGIN = 2;
    static final int REGISTER = 3;
    static final int LOGOUT = 4;
    static final int ORDER = 10;
    static final int SELECTORDER = 20;
    static final int BACK = 30;
    private static boolean loop = true;
    private static boolean loop2 = true;
    private static boolean isFlag = false; // 判断用户是否有权限进行点餐
    private static Map<Long,Integer> bills = new HashMap<>();

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
                    case IMessage.BOOLEAN_MSG:
                        logger.debug("状态消息");
                        IsLoginMsg isLoginMsg = (IsLoginMsg)event;
                        isFlag = isLoginMsg.isFlag();
                        logger.debug("顾客端可以进入点餐功能啦！");
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
            if(isFlag){
                do {
                    logger.debug("进入二级菜单！");
                    showMenu2();
                    int t2 = sc.nextInt();
                    String enter2 = sc.nextLine();
                    switch (t2) {
                        case ORDER:
                            logger.debug("可以开始点餐了o(*￣▽￣*)ブ");
                            customer.send(new CusOrderFoodMsg());
                            System.out.println("请选择以下餐厅的编号");
                            int ResNo = sc.nextInt();
                            String enter3 = sc.nextLine();
                            logger.debug("点餐信息信息已发送");
                            customer.send(new FoodListMsg(ResNo));
                            System.out.println("请选择以下菜品，输入0结束");
                            while(true){
                                String food = sc.nextLine();
                                long foodId = Long.parseLong(food);
                                if(foodId==0){
                                    break;
                                }
                                if(bills.containsKey(foodId)){
                                    Integer mount = bills.get(foodId);
                                    mount = mount+1;
                                    bills.put(foodId, mount++);
                                }else {
                                    bills.put(foodId, 1);
                                }

                            }
                            System.out.println("OK,您已点餐完毕，正在帮您计算价格");
                            customer.send(new AddPriceMsg(bills));
                            String choose = sc.nextLine();
                            break;
                        case SELECTORDER:
                            break;
                        case BACK:
                            isFlag = false;
                            loop2 = false;
                            break;
                    }
                }while(loop2);
            }



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
                    System.out.print("请输入姓名：");
                    String name = sc.nextLine();
                    System.out.print("请输入地址：");
                    String addr = sc.nextLine();
                    CusRegisterMsg cusRegisterMsg = new CusRegisterMsg(name, addr);
                    customer.send(cusRegisterMsg);
                    logger.debug("用户注册信息已发送");
                    break;
                case LOGOUT:
                    loop = false;
                    break;
                default:
                    break;
            }

        }while(loop);

        customer.stop();
        logger.info("顾客客户端正确退出");
    }


    static void showMenu(){
        System.out.println("======顾客测试客户端======");
        System.out.println("1，请求验证码");
        System.out.println("2，请求登录");
        System.out.println("3，请求注册");
        System.out.println("4，退出");
        System.out.println("=========================");
    }


    static void showMenu2(){
        System.out.println("======顾客客户端已登录======");
        System.out.println("10，点餐");
        System.out.println("20，查询订单");
        System.out.println("30，返回");
        System.out.println("============================");
    }

}
