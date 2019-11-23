package com.e_waimai.business;

import com.e_waimai.dbo.CustomerDBO;
import com.e_waimai.net.ANetClient;
import com.e_waimai.net.IMessage;
import com.e_waimai.net.message.CusLoginMsg;
import com.e_waimai.net.message.ResLoginMsg;
import com.e_waimai.net.message.TextMsg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;

public class Customer extends ANetClient {
    protected static Logger logger = LogManager.getLogger("APP");
    private String phone;
    private int id;
    private CustomerDBO cdbo = new CustomerDBO();

    public Customer(Socket socket, String phone) {
        super(socket);
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
                    String code = loginMsg.getVcode();
                    if(code.equals("654321")){
                        send(new TextMsg("登录成功！"));
                        String info  = cdbo.getCustomerInfo(this.phone);
                        send(new TextMsg(info));
                        logger.debug("验证码正确");
/*                      setId(rdbo.getIdByPhone(loginMsg.getPhone()));*/
                    }else{
                        send(new TextMsg("登录失败！"));
                        logger.debug("验证码不匹配");
                    }
                    break;
                case IMessage.CS_REG:
                    logger.debug("服务器收到顾客的注册信息");
                    break;
                case IMessage.CS_ORDER_FOOD:
                    logger.debug("服务器收到商家的增加菜品信息");
                    break;
                default:
                    break;
            }
        }else{
            logger.debug("顾客收到意外消息！");
        }
    }
}
