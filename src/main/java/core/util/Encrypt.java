package core.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.Base64;
import java.util.UUID;

import javax.crypto.Cipher;
//import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * MD5等工具
 * 
 * @author CooC
 *
 */
public class Encrypt {

	private static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F' };

	private static char hexDigitsLower[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	private static SecureRandom secureRandom;
	private static final String ALGORITHM_MD5 = "md5";
	private static final String ALGORITHM_DES = "des";
	private static final String ALGORITHM_RSA = "rsa";
	private static final String ALGORITHM_MD5_RSA = "MD5withRSA";
	private static final String ALGORITHM_SHA = "sha";
	private static final String ALGORITHM_MAC = "HmacMD5";
	private static final String ALGORITHM_AES = "AES/ECB/PKCS5Padding";

	private static KeyPair keyPair;

	static {
		secureRandom = new SecureRandom();
		try {
			// 创建密钥对KeyPair
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM_RSA);
			// 密钥长度推荐为1024位
			keyPairGenerator.initialize(1024);
			keyPair = keyPairGenerator.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public static Encrypt instance() {
		return new Encrypt();
	}

	/**
	 * 产生一个不带'-'的UUID字符串
	 * 
	 * @return
	 */
	public static String getRandomUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 对字符串进行MD5摘要，字符编码为UTF-8，md5后的字符串是大写。
	 * 
	 * @param text 要摘要的字符串
	 * @return
	 */
	public static String md5Upper(String text) {
		return md5(text, false);
	}

	/**
	 * 对字符串进行MD5摘要，字符编码为UTF-8，md5后的字符串是小写。
	 * 
	 * @param text 要摘要的字符串
	 * @return
	 */
	public static String md5Lower(String text) {
		return md5(text, true);
	}

	/**
	 * 对byte[] 进行md5摘要，md5后的字符串是大写。
	 * 
	 * @param data
	 * @return
	 */
	public static String md5Upper(byte[] data) {
		return md5(data, false);
	}

	/**
	 * 对byte[] 进行md5摘要，md5后的字符串是小写。
	 * 
	 * @param data
	 * @return
	 */
	public static String md5Lower(byte[] data) {
		return md5(data, true);
	}

	/**
	 * 生成随机的numBytes位字符串。
	 * 
	 * @param numBytes 字符串长度。
	 */
	public static String generateRandomString(int numBytes) {
		if (numBytes < 1) {
			throw new RuntimeException("字节数参数必需是正整数");
		}

		byte[] bytes = new byte[numBytes];
		secureRandom.nextBytes(bytes);
		return encodeHexString(bytes);
	}

	/**
	 * 
	 * @param salt      盐值
	 * @param plainText 明文
	 * @return md5(md5(plainText)+salt)
	 */
	public static String entryptText(String plainText, String salt) {
		String ret = md5Lower(plainText);
		if (salt != null) {
			ret += salt.trim();
		}
		return md5Lower(ret);
	}

	/**
	 * 
	 * @param text
	 * @param toLowerCase 是否用小写字母。
	 * @return
	 */
	private static String md5(String text, final boolean toLowerCase) {
		if (text == null || text.trim().length() < 1) {
			return "";
		}
		try {
			return md5(text.getBytes("UTF-8"), toLowerCase);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private static String md5(byte[] source, final boolean toLowerCase) {
		if (source == null || source.length == 0) {
			return null;
		}
		return md5(source, toLowerCase ? hexDigitsLower : hexDigits);
	}

	private static String md5(byte[] source, final char[] hexDigits) {
		String s = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
			// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
			// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
				// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
				// >>> 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			s = new String(str); // 换后的结果转换为字符串

		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	private static String encodeHexString(final byte[] data) {
		return new String(encodeHex(data, true));
	}

	private static char[] encodeHex(final byte[] data, final boolean toLowerCase) {
		return encodeHex(data, toLowerCase ? hexDigitsLower : hexDigits);
	}

	private static char[] encodeHex(final byte[] data, final char[] toDigits) {
		final int l = data.length;
		final char[] out = new char[l << 1];
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
			out[j++] = toDigits[0x0F & data[i]];
		}
		return out;
	}

	/**
	 * MD5简单加密
	 *
	 * @param content 加密内容
	 * @return String
	 */
	public static String md5Encrypt(final String content) {

		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance(ALGORITHM_MD5);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		// md5.update(text.getBytes());
		// digest()最后返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
		// BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
		BigInteger digest = new BigInteger(md5.digest(content.getBytes()));
		// 32位
		return digest.toString(16);
	}

	/**
	 * base64加密 因为+、*、|、\等符号在正则表达示中有相应的不同意义，所以在使用时要进行转义处理
	 * 
	 * @param content 待加密内容
	 * @return byte[]
	 */
	public static byte[] base64Encrypt(final String content) {
		return Base64.getEncoder().encode(content.getBytes());
	}

	public String base64EncryptString(final String content) {
		return new String(Base64.getEncoder().encode(content.getBytes())).replaceAll("\\+", "_");
	}

	/*
	 * base64DESEncrypt
	 */
	public static String base64DESEncrypt(final String content, final String key) {
		return new String(Base64.getEncoder().encode(DESEncrypt(content, key))).replaceAll("\\+", "_");
	}

	/**
	 * base64解密
	 *
	 * @param encoderContent 已加密内容
	 * @return byte[]
	 */
	public static byte[] base64Decrypt(final byte[] encoderContent) {
		return Base64.getDecoder().decode(encoderContent);
	}

	public static String base64DecryptString(final String base64EncryptString) {
		return new String(Base64.getDecoder().decode(base64EncryptString.replaceAll("_", "+").getBytes()));
	}

	/*
	 * base64DESDecrypt
	 */
	public static String base64DESDecrypt(final String base64EncryptString, final String key) {
		return new String(
				DESDecrypt(Base64.getDecoder().decode(base64EncryptString.replaceAll("_", "+").getBytes()), key));
	}

	/**
	 * 根据key生成秘钥
	 *
	 * @param key 给定key,要求key至少长度为8个字符
	 * @return SecretKey
	 */
	public static SecretKey getSecretKey(final String key) {
		try {
			DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
			SecretKeyFactory instance = SecretKeyFactory.getInstance(ALGORITHM_DES);
			SecretKey secretKey = instance.generateSecret(desKeySpec);
			return secretKey;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * DES加密
	 *
	 * @param key     秘钥key
	 * @param content 待加密内容
	 * @return byte[] DES对称加密/解密 要求key至少长度为8个字符
	 */
	public static byte[] DESEncrypt(final String content, final String key) {
		return processCipher(content.getBytes(), getSecretKey(key), Cipher.ENCRYPT_MODE, ALGORITHM_DES);
	}

	/**
	 * DES解密 DES对称加密/解密 要求key至少长度为8个字符
	 * 
	 * @param key            秘钥key
	 * @param encoderContent 已加密内容
	 * @return byte[]
	 */
	public static byte[] DESDecrypt(final byte[] encoderContent, final String key) {
		return processCipher(encoderContent, getSecretKey(key), Cipher.DECRYPT_MODE, ALGORITHM_DES);
	}

	/**
	 * 加密/解密处理
	 *
	 * @param processData 待处理的数据
	 * @param key         提供的密钥
	 * @param opsMode     工作模式
	 * @param algorithm   使用的算法
	 * @return byte[]
	 */
	private static byte[] processCipher(final byte[] processData, final Key key, final int opsMode,
			final String algorithm) {
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			// 初始化
			cipher.init(opsMode, key, secureRandom);
			return cipher.doFinal(processData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取私钥，用于RSA非对称加密.
	 *
	 * @return PrivateKey
	 */
	public static PrivateKey getPrivateKey() {
		return keyPair.getPrivate();
	}

	/**
	 * 获取公钥，用于RSA非对称加密.
	 *
	 * @return PublicKey
	 */
	public static PublicKey getPublicKey() {
		return keyPair.getPublic();
	}

	/**
	 * 获取数字签名，用于RSA非对称加密.
	 *
	 * @return byte[]
	 */
	public static byte[] getSignature(final byte[] encoderContent) {
		try {
			Signature signature = Signature.getInstance(ALGORITHM_MD5_RSA);
			signature.initSign(keyPair.getPrivate());
			signature.update(encoderContent);
			return signature.sign();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 验证数字签名，用于RSA非对称加密.
	 *
	 * @return byte[]
	 */
	public static boolean verifySignature(final byte[] encoderContent, final byte[] signContent) {
		try {
			Signature signature = Signature.getInstance(ALGORITHM_MD5_RSA);
			signature.initVerify(keyPair.getPublic());
			signature.update(encoderContent);
			return signature.verify(signContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}

	/**
	 * RSA加密
	 *
	 * @param content 待加密内容
	 * @return byte[]
	 */
	public static byte[] RSAEncrypt(final String content) {
		return processCipher(content.getBytes(), keyPair.getPrivate(), Cipher.ENCRYPT_MODE, ALGORITHM_RSA);
	}

	/**
	 * RSA解密
	 *
	 * @param encoderContent 已加密内容
	 * @return byte[]
	 */
	public static byte[] RSADecrypt(final byte[] encoderContent) {
		return processCipher(encoderContent, keyPair.getPublic(), Cipher.DECRYPT_MODE, ALGORITHM_RSA);
	}

	/**
	 * SHA加密
	 *
	 * @param content 待加密内容
	 * @return String
	 */
	public String SHAEncrypt(final String content) {
		try {
			MessageDigest sha = MessageDigest.getInstance(ALGORITHM_SHA);
			byte[] sha_byte = sha.digest(content.getBytes());
			StringBuffer hexValue = new StringBuffer();
			for (byte b : sha_byte) {
				// 将其中的每个字节转成十六进制字符串：byte类型的数据最高位是符号位，通过和0xff进行与操作，转换为int类型的正整数。
				String toHexString = Integer.toHexString(b & 0xff);
				hexValue.append(toHexString.length() == 1 ? "0" + toHexString : toHexString);
			}
			return hexValue.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * HMAC加密
	 *
	 * @param key     给定秘钥key
	 * @param content 待加密内容
	 * @return String
	 */
	public static byte[] HMACEncrypt(final String key, final String content) {
		try {
			SecretKey secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM_MAC);
			Mac mac = Mac.getInstance(secretKey.getAlgorithm());
			// 初始化 MAC
			mac.init(secretKey);
			return mac.doFinal(content.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * AES加密为base 64 code
	 *
	 * @param content    待加密的内容
	 * @param encryptKey 加密密钥
	 * @return 加密后的base 64 code
	 */
	public static String strAESEncrypt(String content, String encryptKey) throws Exception {
		return parseByte2HexStr(AESEncrypt(content, encryptKey));
		//return new String(Base64.getEncoder().encode(AESEncrypt(content, encryptKey))).replaceAll("\\+", "_");
	}

	/**
	 * 将base 64 code AES解密
	 *
	 * @param encryptStr 待解密的base 64 code
	 * @param decryptKey 解密密钥
	 * @return 解密后的string
	 */
	public static String strAESDecrypt(String encryptStr, String decryptKey) throws Exception {
		return new String(AESDecrypt(parseHexStr2Byte(encryptStr), decryptKey));
		//return AESDecrypt(Base64.getDecoder().decode(encryptStr.replaceAll("_", "+").getBytes("utf-8")), decryptKey);
	}

	/**
	 * AES加密
	 * 
	 * @param content    待加密的内容
	 * @param encryptKey 加密密钥
	 * @return 加密后的byte[]
	 */
	private static byte[] AESEncrypt(String content, String encryptKey) throws Exception {
		/*KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128);
		Cipher cipher = Cipher.getInstance(ALGORITHM_AES);
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));

		return cipher.doFinal(content.getBytes("utf-8"));*/
		
		byte[] raw = encryptKey.getBytes("utf-8");
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance(ALGORITHM_AES);//"算法/模式/补码方式""AES/ECB/PKCS5Padding";
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(content.getBytes("utf-8"));
		return encrypted;
	}

	/**
	 * AES解密
	 *
	 * @param encryptBytes 待解密的byte[]
	 * @param decryptKey   解密密钥
	 * @return 解密后的String
	 */
	private static String AESDecrypt(byte[] encryptBytes, String decryptKey) throws Exception {
		/*KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128);

		Cipher cipher = Cipher.getInstance(ALGORITHM_AES);
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
		byte[] decryptBytes = cipher.doFinal(encryptBytes);

		return new String(decryptBytes);*/
		byte[] raw = decryptKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance(ALGORITHM_AES);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] original = cipher.doFinal(encryptBytes);
        String originalString = new String(original,"utf-8");
        return originalString;
	}

	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		hexStr = hexStr.trim();
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

}
