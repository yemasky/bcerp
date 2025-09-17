package com.base.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.base.config.Config;

import core.util.Encrypt;
import core.util.Utiliy;

public class EncryptUtiliy {
	private static EncryptUtiliy encryptUtiliy = null;

	public static EncryptUtiliy instance() {
		if (encryptUtiliy == null)
			encryptUtiliy = new EncryptUtiliy();
		return encryptUtiliy;
	}

	public String intIDEncrypt(int intId) {
		return Encrypt.base64DESEncrypt(intId + "---" + Utiliy.instance().getTodayDate(), Config.encryptKey);
	}

	public int getIntIDDecrypt(String intEncrypt) {
		String[] decrypt = this.dateDecrypt(intEncrypt);
		if (decrypt != null && decrypt.length > 0)
			return Integer.parseInt(decrypt[0]);
		return 0;

	}

	public String getDecryptDate(String strEncrypt) {
		String[] memberDecrypt = this.dateDecrypt(strEncrypt);
		if (strEncrypt != null && memberDecrypt.length > 0)
			return memberDecrypt[1];
		return null;
	}

	private String[] dateDecrypt(String strEncrypt) {
		if (strEncrypt == null || strEncrypt.trim().equals(""))
			return null;
		return Encrypt.base64DESDecrypt(strEncrypt, Config.encryptKey).split("---");
	}
	
	public String myIDEncrypt(int intId) throws Exception {
		return Encrypt.strAESEncrypt(intId + "---" + Utiliy.instance().getTodayDate(), Config.myEncryptKey);
	}
	
	public int myIDDecrypt(String intEncrypt) throws Exception {
		String[] memberDecrypt = this.myDateDecrypt(intEncrypt);
		if (memberDecrypt != null && memberDecrypt.length > 0) {
			int id = Integer.parseInt(memberDecrypt[0]);
			//if(id == 1 && !Utiliy.instance().getTodayDate().substring(0, 10).equals(memberDecrypt[1].substring(0, 10))) return -1;//登录超时
			return id;
		}
		return 0;

	}
	
	private String[] myDateDecrypt(String strEncrypt) throws Exception {
		if (strEncrypt == null || strEncrypt.trim().equals(""))
			return null;
		return Encrypt.strAESDecrypt(strEncrypt, Config.myEncryptKey).split("---");
	}
	
	public String getType(Object obj) {
		/**
		 * 1. 通过反射获取传来参数的JavaClass对象 2. 获取到JavaClass对象的类型名称 3. 将参数的类型名称返回
		 */
		return obj.getClass().getSimpleName();
	}

	public String getUniqueId(int id) {
		return Encrypt.md5Lower(Encrypt.md5Lower(id+Config.uniqueKey));
	}
	
	public String getOrderNumber(int id) {
		Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");//
        String dateStr = format.format(date);
        String number = id+"";
        int len = number.length();
        if(len < 8) {
        	if(len<7) {
        		number = number +"0"+ (10000000*Math.random()+"").substring(0, 7-len);
        	} else {
        		number = number + "0";
        	}
        } else {
        	number = number.substring(0, 8);
        }
        number = dateStr + number;
		return number;
	}
}
