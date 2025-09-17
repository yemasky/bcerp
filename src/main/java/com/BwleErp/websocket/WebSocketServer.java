package com.BwleErp.websocket;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import com.base.controller.AbstractAction;
import com.base.type.CheckedStatus;
import com.base.util.EncryptUtiliy;

import core.util.Encrypt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/*import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;*/
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@Component
@ServerEndpoint("/websocket/xiaoqu/{action}") // 接口路径 ws://localhost:8087/webSocket?m_encrypt_id={m_encrypt_id};
public class WebSocketServer extends AbstractAction {
	
	// 与某个客户端的连接会话，需要通过它来给客户端发送数据
	//private Session session;
	/**
	 * 用户ID
	 */
	private int member_id;
	private String m_encrypt_id;//加密后member_id
	private String member_key;

	// concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
	// 虽然@Component默认是单例模式的，但SpringBoot还是会为每个WebSocket连接初始化一个bean，所以可以用一个静态set保存起来。
	// 注：底下WebSocket是当前类名
	private static CopyOnWriteArraySet<WebSocketServer> webSockets = new CopyOnWriteArraySet<>();
	// 用来存在线连接用户信息
	private static ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<String, Session>();
	
	@Override
	public CheckedStatus check(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		MDC.put("APP_NAME", "socket_info");
		this.status.setStatus(true);
		return this.status;
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void release(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void rollback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
	}

	/**
	 * 链接成功调用的方法
	 * , @PathParam(value = "m_encrypt_id") String m_encrypt_id
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam(value = "action") String action) {
		try {
			Map<String, List<String>> requestMap = session.getRequestParameterMap();
			String m_encrypt_id = requestMap.get("m_encrypt_id").get(0);
			logger.info("开始WebSocket，m_encrypt_id:" + m_encrypt_id);
			//this.session      = session;
			this.member_id    = EncryptUtiliy.instance().myIDDecrypt(m_encrypt_id);//我的ID 打开连接并解密
			this.m_encrypt_id = EncryptUtiliy.instance().intIDEncrypt(this.member_id);
			this.setMemberKey(this.member_id);
			//String key = Encrypt.md5Lower(this.member_id+"");
			if(sessionPool.containsKey(this.member_key)) {//移除舊鏈接
				try {
					//webSockets.remove(this);
					Session old_session = sessionPool.get(this.member_key);
					old_session.close();
					sessionPool.remove(this.member_key);
					logger.info("【websocket消息】连接断开，总数为:" + sessionPool.size());
				} catch (Exception e) {
					MDC.put("APP_NAME", "socket_error");
					logger.error(e.getMessage(), e);
				}
			}
			//webSockets.add(this);
			sessionPool.put(this.member_key, session);
			logger.info("【websocket消息】有新的连接，总数为:" + sessionPool.size() + ";最新:"+this.member_key);
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
			sessionPool.remove(this.member_key);
			logger.info("【websocket消息】连接断开，总数为:" + sessionPool.size());
		} catch (Exception e) {
			MDC.put("APP_NAME", "socket_error");
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 收到客户端消息后调用的方法
	 * @throws SQLException 
	 *
	 * @Param message
	 * @Param session
	 */
	@OnMessage
	public void onMessage(String message) throws SQLException {
		logger.info("【websocket消息】收到客户端消息:" + message);
		this.sendOneMessage(message);
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
		for (WebSocketServer webSocket : webSockets) {
			try {
				if (sessionPool.get(webSocket.member_key).isOpen()) {
					sessionPool.get(webSocket.member_key).getAsyncRemote().sendText(message);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("websocket错误，",e.getMessage());
			}
		}
	}

	// 此为单点消息
	public void sendOneMessage(String message) throws SQLException {
		
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
	
	private void setMemberKey(int member_id) {
		this.member_key = "k_" + member_id;
	}
	
	private String getToMemberKey(int to_member_id) {
		return "k_" + to_member_id;
	}
}
interface MessageType {
	String _USER = "user";
	String _SYS = "system";
	String _PROMPT = "prompt";//提醒
	String _TEXT = "text";
	String _IMG = "img";
	String _LeaveMsg = "LeaveMsg";
	String _GET_HMSG = "GetHistory";
}
class ToMessage {
	private String type = MessageType._TEXT;
	private String content;
	private String content_type;
	private String to_unique_id;
	private String date;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContent_type() {
		return content_type;
	}
	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}
	public String getTo_unique_id() {
		return to_unique_id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public void setTo_unique_id(String to_unique_id) {
		this.to_unique_id = to_unique_id;
	}

	
}

class LeaveMessage {
	private String type = MessageType._LeaveMsg;
	private List<ToMessage> toMessageList;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<ToMessage> getToMessageList() {
		return toMessageList;
	}
	public void setToMessageList(List<ToMessage> toMessageList) {
		this.toMessageList = toMessageList;
	}
}
