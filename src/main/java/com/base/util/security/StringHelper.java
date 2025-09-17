package com.base.util.security;

import java.util.Base64;

public class StringHelper {
	/**
	 * BASE64Encoder 加密
	 * 
	 * @param data 要加密的数据
	 * @return 加密后的字符串
	 */
	public static String encryptBASE64(byte[] data) {
		return new String(Base64.getEncoder().encode(data));
	}

	/**
	 * BASE64Decoder 解密
	 * 
	 * @param data 要解密的字符串
	 * @return 解密后的byte[]
	 * @throws Exception
	 */
	public static byte[] decryptBASE64(String data) throws Exception {
		return Base64.getDecoder().decode(data.getBytes());
	}
}