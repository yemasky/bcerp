package com.base.type;

public class CheckedStatus {
	private boolean status = true;
	private ErrorCode error;
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public ErrorCode getError() {
		return error;
	}
	public void setError(ErrorCode error) {
		this.error = error;
	}
}

