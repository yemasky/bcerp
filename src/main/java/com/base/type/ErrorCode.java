package com.base.type;

public enum ErrorCode {
	// T TURE 的缩写 F FALSE的缩写
	__T_SUCCESS("100001", "请求数据成功"), __T_LOGIN("100002", "登錄成功"),__T_API("-1", "API_DATA"),
	//
	__F_SYS("000000", "系统错误"),
	//
	__F_NO_LOGIN("000001", "用户没有登录"), __F_NO_MATCH_MEMBER("000002", "登录失败,用户不匹配"),
	__F_WX_LOGIN("000003", "微信登录失败"),__F_MEMBER_HAVE_SAM_NAME("000004", "账号重复"),
	__F_NOT_INCREASE("000005", "不能给自己点"),__F_AUTH("000006", "认证失败"),__F_AUTH_EDU("000007", "学历认证失败，此身份已被人使用"),
	__F_OUT_STOCk("000008", "超出库存"),__F_LOGIN_OVERTIME("000009", "登录超时"),__F_NO_ENOUGH_CEEDITS("000010", "没有足够的金币"),
	__F_WRONG_PARAM("000011", "参数错误"),__F_OVER_PRICE("000012", "出价被超过"),__F_WX_INTERFACE("000013", "含有敏感词汇"),
	__F_NO_ACTIVITY("000014", "没有多余的字条"),
	;
	
	//

	private String error_code;
	private String describe;

	private ErrorCode(String error_code, String describe) {
		this.setError_code(error_code);
		this.setDescribe(describe);
	}

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

}
