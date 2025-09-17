package com.Example.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//import com.base.websocket.WebSocketServer;

@Component
public class WebSocketTask {
	@Scheduled(fixedRate = 10000)
    public void doTask() {
        try {
            System.out.println("发送消息");
            //WebSocketServer.sendInfo("9999999");
        } catch (Exception e) {
            e.printStackTrace();//"系统出错:{}", e
        }
 
    }
}
