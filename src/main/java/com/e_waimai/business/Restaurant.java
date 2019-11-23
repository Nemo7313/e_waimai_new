package com.e_waimai.business;

import com.e_waimai.dbo.RestaurantDBO;
import com.e_waimai.net.ANetClient;
import com.e_waimai.net.IMessage;
import com.e_waimai.net.message.ResAddFoodMsg;
import com.e_waimai.net.message.ResLoginMsg;
import com.e_waimai.net.message.ResRegisterMsg;
import com.e_waimai.net.message.TextMsg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;

public class Restaurant extends ANetClient {
    protected static Logger logger = LogManager.getLogger("APP");
    private String phone;
    private int id;
    private RestaurantDBO rdbo;

    public Restaurant(Socket socket,String phone) {
        super(socket);
        this.phone = phone;
        rdbo = new RestaurantDBO();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public void onMessage(IMessage event) {
        int MsgType = event.getMsgType();
        if(MsgType >= 2000 && MsgType < 3000){
            logger.debug("商家消息处理线程收到消息：");
            switch(MsgType){
                case IMessage.REQ_LOGIN_RS:
                    logger.debug("服务器收到商家的登录信息");
                    ResLoginMsg loginMsg = (ResLoginMsg)event;
                    String code = loginMsg.getVcode();
                    if(code.equals("123456")){
                        send(new TextMsg("登录成功！"));
                        String info = rdbo.getRestaurantInfo(loginMsg.getPhone());
                        send(new TextMsg(info));
                        logger.debug("验证码正确");
                        setId(rdbo.getIdByPhone(loginMsg.getPhone()));
                    }else{
                        send(new TextMsg("登录失败！"));
                        logger.debug("验证码不匹配");
                    }
                    break;
                case IMessage.RS_REG:
                    logger.debug("服务器收到商家的注册信息");
                    ResRegisterMsg registerMsg = (ResRegisterMsg)event;
                    if(register(registerMsg)){
                        logger.debug("商家注册成功");
                        setId(getIdByPhone());
                        send(new TextMsg("注册成功，您的餐馆ID为"+getId()));
                    }else{
                        logger.debug("商家注册失败");
                    }
                    break;
                case IMessage.RS_ADD_FOOD:
                    logger.debug("服务器收到商家的增加菜品信息");
                    ResAddFoodMsg addFoodMsg = (ResAddFoodMsg)event;
                    if(rdbo.addfood(getId(), addFoodMsg.getName(), addFoodMsg.getPrice(), addFoodMsg.getIntro())){
                        send(new TextMsg("添加菜品成功"));
                    }else{
                        send(new TextMsg("添加菜品失败"));
                    }
                    break;
                case IMessage.RS_REC_ORDER:
                    break;
                case IMessage.RS_DATA:
                    break;
                case IMessage.RS_UPDATE:
                    break;
                default:
                    break;
            }
        }else{
            logger.debug("商家收到意外消息！");
        }
    }

    boolean register (ResRegisterMsg RegMsg){
        logger.info("商家店名为："+RegMsg.getName()+"，商家地址为："+ RegMsg.getAddress()+"，" +
                "商家简介为："+ RegMsg.getIntroduction()+"，商家电话为："+getPhone());
        return rdbo.register(getPhone(), RegMsg.getName(), RegMsg.getAddress(), RegMsg.getIntroduction() );
    }

    int getIdByPhone(){
        return rdbo.getIdByPhone(this.phone);
    }

    String getRestaurantInfo(){
        return rdbo.getRestaurantInfo(this.phone);
    }

}
