package com.puti.education.netFrame.response;


public class ResponseInfo {

	public boolean status;
	public String error;
	public DataInfo data;
	public int code;
	
    public ResponseInfo()
    {
    	super();
    }
    public ResponseInfo(String str) {
    }
	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public DataInfo getData() {
		return data;
	}

	public void setData(DataInfo data) {
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
