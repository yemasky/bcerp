package core.jdbc.mysql;

import java.util.HashMap;

public class NeedEncrypt {
	public static final String _ENCRYPT = "encrypt";
	public String _UNIQUE_KEY = "unique_id";//m_unique_id 需要加密成unique_id的必须包含有 “unique_id”字样
	public static final String _ANONYMITY = "anonymity";
	private boolean isNeedEncrypt = false;
	private HashMap<String, String> needEncrypt;
	public boolean isNeedEncrypt() {
		return isNeedEncrypt;
	}
	public void setNeedEncrypt(boolean isNeedEncrypt) {
		this.isNeedEncrypt = isNeedEncrypt;
	}
	public HashMap<String, String> getNeedEncrypt() {
		return needEncrypt;
	}
	public void setNeedEncrypt(String key, String val) {
		if(this.needEncrypt == null) this.needEncrypt = new HashMap<String, String>();
		this.needEncrypt.put(key, val);
	}
	public String get_UNIQUE_KEY() {
		return _UNIQUE_KEY;
	}
	public void set_UNIQUE_KEY(String _UNIQUE_KEY) {
		this._UNIQUE_KEY = _UNIQUE_KEY;
	}
	
}
