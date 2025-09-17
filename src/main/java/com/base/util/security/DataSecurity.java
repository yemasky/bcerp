package com.base.util.security;

import java.io.UnsupportedEncodingException;
import java.security.KeyPair;

public class DataSecurity {
	private KeyPair keyPair;
	private static final String KEY_FILE = "config/ca.key";//"E:\\workspace\\liaoyou.xyz\\logs\\CAPath\\ca.key";
	private DataSignaturer dataSignaturer;

	public DataSecurity() {
		try {
			//this.getClass().getClassLoader().getResourceAsStream("config/jdbc.mysql.properties");
			//this.keyPair = KeyPairUtil.loadKeyPair(getClass().getResourceAsStream(KEY_FILE));
			this.keyPair = KeyPairUtil.loadKeyPair(this.getClass().getClassLoader().getResourceAsStream(KEY_FILE));
			this.dataSignaturer = new DataSignaturer(this.keyPair.getPublic(), this.keyPair.getPrivate());
		} catch (RuntimeException e) {
			System.out.println("没有找到KeyPair文件["+this.getClass().getClassLoader().toString() + KEY_FILE+"]!");
		}
	}

	/**
	 * 验证数字签名
	 * 
	 * @param data
	 * @param signs
	 * @return
	 */
	public boolean verifySign(String data, String signs) {
		if ((data == null) || (signs == null)) {
			System.out.println("参数为Null");
		}
		boolean verifyOk = false;
		try {
			verifyOk = this.dataSignaturer.verifySign(data.getBytes("UTF-8"), StringHelper.decryptBASE64(signs));
		} catch (RuntimeException e) {
			System.out.println("fail!data=" + data + ", sign=" + signs + ", exception:" + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			System.out.println("不支持UTF-8字符集");
		} catch (Exception e) {
			System.out.println("Exception:" + e.getMessage());
		}
		if (!verifyOk) {
			System.out.println("fail!data=" + data + ", sign=" + signs + ", verifyOk=false!");
		}
		return verifyOk;
	}

	/**
	 * 生成数字签名
	 * 
	 * @param data
	 * @return
	 */
	public String sign(String data) {
		if (data == null) {
			System.out.println("参数为Null");
		}
		String sign = null;
		try {
			sign = StringHelper.encryptBASE64(this.dataSignaturer.sign(data.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			System.out.println("不支持UTF-8字符集");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return sign;
	}
}
