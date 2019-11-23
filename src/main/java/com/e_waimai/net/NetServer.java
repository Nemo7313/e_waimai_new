package com.e_waimai.net;

import com.e_waimai.business.Customer;
import com.e_waimai.business.Restaurant;
import com.e_waimai.dbo.RestaurantDBO;
import com.e_waimai.net.message.CusVcodeMsg;
import com.e_waimai.net.message.ResLoginMsg;
import com.e_waimai.net.message.TextMsg;
import com.e_waimai.net.message.VCodeMsg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NetServer implements Runnable {
    protected static Logger logger = LogManager.getLogger("APP");
    private int port;
    private ServerSocket server;
    private boolean loop = true;
    private static Random random = new Random(System.currentTimeMillis());
    private Map<Long, Socket> clients = new HashMap<>();
    private RestaurantDBO rtdbo = new RestaurantDBO();

    public NetServer(int port){
        this.port = port;
    }

    public boolean start(){
        try {
            this.server = new ServerSocket(port);
            logger.info("服务端Socket创建完成！");
        } catch (IOException e) {
            logger.fatal(e);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        logger.debug("等待客户端连接");
        do{
            try {
                final Socket c = server.accept();
                logger.info("客户端连接成功");

                //分配随机 session key 添加到客户端列表
                Long sessionId = random.nextLong();
                while(clients.containsKey(sessionId)){
                    sessionId = random.nextLong();
                }
                logger.debug("SessionID为："+sessionId);
                clients.put(sessionId, c);

                //创建客户端，发起线程
                ANetClient client = new ANetClient(c) {
                   @Override
                   public void onMessage(IMessage event) {
                       logger.debug("客户端线程收到消息");
                       int MsgType = event.getMsgType();
                       switch(MsgType){
                           case IMessage.TEXT_MSG:
                               TextMsg textMsg = (TextMsg)event;
                               String msg = textMsg.getMessage();
                               logger.info("客户端传来的消息是："+msg);
                               break;
                           case IMessage.REQ_VCODE:
                               VCodeMsg vMsg = (VCodeMsg)event;
                               String phoneNum = vMsg.getPhone();
                               logger.info("已向手机号："+phoneNum+"发送验证码123456");
                               Restaurant rn = new Restaurant(c,vMsg.getPhone());
                               Thread tk = new Thread(rn);
                               tk.setDaemon(true);
                               this.stop();//停止旧客户端
                               tk.start();
                               break;
                           case IMessage.REQ_VCODE_CUS:
                               CusVcodeMsg cusVcodeMsg = (CusVcodeMsg) event;
                               String phone = cusVcodeMsg.getPhone();
                               logger.info("已向手机号："+phone+"发送验证码654321");
                               Customer cus = new Customer(c,phone);
                               Thread tk2 = new Thread(cus);
                               tk2.setDaemon(true);
                               this.stop();//停止旧客户端
                               tk2.start();
                               break;
                           default:
                               logger.debug("暂不支持的消息类型");
                               break;
                       }
                   }
               };
               TextMsg textMsg = new TextMsg("欢迎登录"+sessionId);
               client.send(textMsg);
               Thread task = new Thread(client);
               task.setDaemon(true);
               task.start();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }while(this.loop);
    }

    public boolean stopServe(){
        logger.info("收到服务器关闭指令");
        // 停止接入新用户
        this.loop = false;
        try{
            Thread.currentThread().setDaemon(true);
            return true;
        }catch (Exception e){
            return false;
        }

    }
}
