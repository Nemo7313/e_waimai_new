package com.e_waimai.business;

import com.e_waimai.dbo.CustomerDBO;
import com.e_waimai.net.ANetClient;
import com.e_waimai.net.IMessage;
import com.e_waimai.net.message.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;

public class Customer extends ANetClient {
    protected static Logger logger = LogManager.getLogger("APP");
    private String phone;
    private long id;
    private CustomerDBO cdbo = new CustomerDBO();

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
                    }else{
                        send(new TextMsg("注册失败，请重新尝试"));
                    }
                    break;
                case IMessage.CS_ORDER_FOOD:
                    logger.debug("服务器收到顾客的点餐消息");
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
