package com.BwleErp.websocket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/*import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;*/

import org.openqa.selenium.WebDriver;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import com.base.controller.AbstractAction;
import com.base.type.CheckedStatus;

import core.util.Encrypt;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@Component
@ServerEndpoint("/websocket/taobao/{action}") // 
public class WebSocketTaobaoCrawlerServer extends AbstractAction {
	// concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
	// 虽然@Component默认是单例模式的，但SpringBoot还是会为每个WebSocket连接初始化一个bean，所以可以用一个静态set保存起来。
	// 注：底下WebSocket是当前类名
	private static CopyOnWriteArraySet<WebSocketTaobaoCrawlerServer> webSockets = new CopyOnWriteArraySet<>();
	// 用来存在线连接用户信息
	private static ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<String, Session>();
	// 用来存浏览器
	private static ConcurrentHashMap<String, WebDriver> driverPool = new ConcurrentHashMap<String, WebDriver>();
	//
	private String mobile;
	private static HashMap<String, String> captchaHash = new HashMap<>();
	@Override
	public CheckedStatus check(HttpServletRequest request, HttpServletResponse response) {
		
		MDC.put("APP_NAME", "socket_info");
		this.status.setStatus(true);
		return this.status;
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
	}

	@Override
	public void release(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
	}

	@Override
	public void rollback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
	}

	/**
	 * 链接成功调用的方法
	 * , @PathParam(value = "mobile") String mobile
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam(value = "action") String action) {
		try {
			
			Map<String, List<String>> requestMap = session.getRequestParameterMap();
			String mobile = requestMap.get("mobile").get(0);
			logger.info("开始WebSocket，mobile:" + mobile);
			//this.session      = session;
			this.mobile    = mobile;//
			//String key = Encrypt.md5Lower(this.member_id+"");
			if(sessionPool.containsKey(mobile)) {//移除舊鏈接
				try {
					//webSockets.remove(this);
					Session old_session = sessionPool.get(mobile);
					old_session.close();
					sessionPool.remove(mobile);
					logger.info("【websocket消息】连接断开，总数为:" + sessionPool.size());
				} catch (Exception e) {
					MDC.put("APP_NAME", "socket_error");
					logger.error(e.getMessage(), e);
				}
			}
			//webSockets.add(this);
			sessionPool.put(mobile, session);
			logger.info("【websocket消息】有新的连接，总数为:" + sessionPool.size() + ";最新:"+mobile);
		} catch (Exception e) {
			MDC.put("APP_NAME", "socket_error");
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 链接关闭调用的方法
	 */
	@OnClose
	public void onClose() {
		try {
			//webSockets.remove(this);
			sessionPool.remove(this.mobile);
			logger.info("【websocket消息】连接断开，总数为:" + sessionPool.size());
		} catch (Exception e) {
			MDC.put("APP_NAME", "socket_error");
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 收到客户端消息后调用的方法
	 * @throws Exception 
	 *
	 * @Param message
	 * @Param session
	 */
	@OnMessage
	public void onMessage(String message) throws Exception {
		if(message.equals("heartbeat")) {
			logger.info("【websocket消息】收到客户端心跳消息:" + message);
			return;
		}
		logger.info("【websocket消息】收到客户端消息:" + message);
		String[] excuteList = message.split("\\|");
		if(excuteList.length < 2) {
			logger.info("toMember_message.length < 2.");
			return;
		}
		String action = excuteList[0];
		switch (action) {
		case "captcha":
			if(excuteList.length != 3) break;
			String mobile = excuteList[1];
			String captcha = excuteList[2];
			if(sessionPool.containsKey(mobile)) {
				captchaHash.put(mobile, captcha);
			}
			break;
		default:
			break;
		}
	}
	
	public String getCaptcha(String mobile) {
		if(captchaHash.containsKey(mobile)) return captchaHash.get(mobile);
		return "";
	}
	
	public void setCaptcha(String mobile) {
		if(captchaHash.containsKey(mobile)) captchaHash.put(mobile, "");
	}

	/**
	 * 发送错误时的处理
	 * 
	 * @Param session
	 * @Param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		logger.error("用户错误,原因:" + error.getMessage());
		//error.printStackTrace();
	}

	// 此为广播消息
	public void sendAllMessage(String message) {
		logger.info("【websocket消息】广播消息:" + message);
		for (WebSocketTaobaoCrawlerServer webSocket : webSockets) {
			try {
				if (sessionPool.get(webSocket.mobile).isOpen()) {
					sessionPool.get(webSocket.mobile).getAsyncRemote().sendText(message);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("websocket错误，",e.getMessage());
			}
		}
	}

	// 此为发送单点消息
	public void sendOneMessage(String message, String mobile) {
		if(message == null || message.equals("")) return;
		logger.info("【websocket消息】发送消息:" + message);
		Session toSession = sessionPool.get(mobile);
		toSession.getAsyncRemote().sendText(message);
	}
 
	// 此为单点消息(多人)
	public void sendMoreMessage(String[] to_member_ids, String message) {
		for (String to_member_id : to_member_ids) {
			Session session = sessionPool.get(Encrypt.md5Lower(to_member_id));
			if (session != null && session.isOpen()) {
				try {
					logger.info("【websocket消息】 单点消息:" + message);
					session.getAsyncRemote().sendText(message);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static WebDriver getDriver(String mobile) {
		if(driverPool.contains(mobile)) {
			System.out.println("getDriver=========================>"+mobile);
			return driverPool.get(mobile);
		}
		return null;
	}
	
	public void setDriver(String mobile, WebDriver driver) {
		driverPool.put(mobile, driver);
	}
}



