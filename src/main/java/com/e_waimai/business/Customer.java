package com.e_waimai.business;

import com.e_waimai.dbo.CustomerDBO;
import com.e_waimai.net.ANetClient;
import com.e_waimai.net.IMessage;
import com.e_waimai.net.message.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Customer extends ANetClient {
    protected static Logger logger = LogManager.getLogger("APP");
    private String phone;
    private long id; // 顾客自己的Id
    private CustomerDBO cdbo = new CustomerDBO();
    private Platform platform  = new Platform();

    private int newOrderRestaurantId; //点餐传递过来的餐馆Id
    private  Map<Long,Integer> newOrderBills = new HashMap<>();
    private double newOrderDeliCost = 0.0;
    private double newOrderTotalCost = 0.0;

    public Customer(Socket socket, String phone) {
        super(socket);
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void onMessage(IMessage event) {
        int msgType = event.getMsgType();
        if(msgType >= 3000 && msgType < 4000){
            logger.debug("顾客客户端消息处理线程收到消息：");
            switch(msgType){
                case IMessage.REQ_LOGIN_CS:
                    logger.debug("服务器收到顾客的登录信息");
                    CusLoginMsg loginMsg = (CusLoginMsg)event;
                    if(getIdByPhone(loginMsg.getPhone()) != -1){
                        String code = loginMsg.getVcode();
                        if(code.equals("654321")){
                            send(new TextMsg("登录成功！,请输入666刷新菜单！"));
                            String info  = cdbo.getCustomerInfo(this.phone);

                            //发送已登录状态的消息
                            boolean login = true;
                            IsLoginMsg isLoginMsg = new IsLoginMsg(login);
                            send(isLoginMsg);
                            logger.debug("顾客端可看到新菜单");
                            //发送顾客的信息
                            send(new TextMsg(info));

                            logger.debug("验证码正确");
                            setId(cdbo.getIdByPhone(loginMsg.getPhone()));
                        }else{
                            send(new TextMsg("登录失败！验证码不正确"));
                            logger.debug("验证码不匹配");
                        }
                    }else{
                        send(new TextMsg("请先使用这个手机号进行注册"));
                        logger.debug("手机号在数据库里不存在");
                    }

                    break;
                case IMessage.CS_REG:
                    logger.debug("服务器收到顾客的注册信息");
                    CusRegisterMsg cusRegisterMsg = (CusRegisterMsg)event;
                    if(register(cusRegisterMsg)){
                        logger.debug("用户注册成功");

                        setId(getIdByPhone(phone));
                        //发送已登录状态的消息
                        boolean login = true;
                        IsLoginMsg isLoginMsg = new IsLoginMsg(login);
                        send(isLoginMsg);
                        send(new TextMsg("您已注册成功，输入666刷新菜单，可以进行点餐啦！"));
                    }else{
                        send(new TextMsg("注册失败，请重新尝试"));
                    }
                    break;
                case IMessage.CS_ORDER_FOOD:
                    logger.debug("服务器收到顾客的点餐消息");
                    String RestList = platform.restaurantDBO.getRestaurantList();
                    send(new TextMsg(RestList));
                    logger.debug("服务器已发送餐馆列表信息！");
                    break;
                case IMessage.Food_List:
                    logger.debug("开始展示菜单");
                    FoodListMsg foodListMsg = (FoodListMsg)event;
                    newOrderRestaurantId = foodListMsg.getRestaurantNumber();
                    String FoodList = platform.restaurantDBO.getFoodList(newOrderRestaurantId);
                    send(new TextMsg(FoodList));
                    logger.debug("已向服务端发送菜单信息");
                    break;
                case IMessage.ADD_PRICE:
                    logger.debug("服务器收到计算菜品总价的信息！");
                    AddPriceMsg addPriceMsg = (AddPriceMsg)event;
                    newOrderBills = addPriceMsg.getBills();
                    double totalCost = 0.0;
                    for(long fooId: newOrderBills.keySet()){
                        double price = platform.restaurantDBO.getFoodPrice(fooId);
                        totalCost += price * newOrderBills.get(fooId);
                    }
                    String billsCost = "您点的菜品总消费额为"+totalCost+"元，另需要支付配送费3元，" +
                            "支付请输入 yes， 不支付请选择 no";
                    send(new TextMsg(billsCost));
                    newOrderDeliCost = totalCost;
                    newOrderTotalCost = totalCost + 3.0;
                    break;
                case IMessage.CREATE_NEW_ORDER:
                    logger.debug("开始准备下单.....");
                    boolean add = platform.platformDBO.createNewOrderAndGetId(id, newOrderRestaurantId, newOrderDeliCost,
                            newOrderTotalCost, newOrderBills);
                    logger.debug("菜品总价为："+newOrderDeliCost+",加上配送费之后的总价为："+newOrderTotalCost);
                    if(add){
                        send(new TextMsg("您已下单成功，等待配送"));
                    }else{
                        send(new TextMsg("您下单失败，请重新下单"));
                    }
                    break;
                default:
                    break;
            }
        }else{
            logger.debug("顾客收到意外消息！");
        }
    }

    boolean register (CusRegisterMsg RegMsg){
        logger.info("顾客名为："+RegMsg.getName()+"，顾客地址为："+ RegMsg.getAddress()+"，顾客电话为："+getPhone());
        return cdbo.register(getPhone(), RegMsg.getName(), RegMsg.getAddress());
    }

    long getIdByPhone(String phone){
        return cdbo.getIdByPhone(phone);
    }
}
