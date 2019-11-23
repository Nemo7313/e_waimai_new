package com.e_waimai.net;


public interface IMsgHandler {
    /**
     * 响应消息
     * @param event 收到的消息
     */
    void onMessage(IMessage event);
}
