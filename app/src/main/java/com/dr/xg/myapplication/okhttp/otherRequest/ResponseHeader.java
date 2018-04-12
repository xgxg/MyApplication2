package com.dr.xg.myapplication.okhttp.otherRequest;

import java.io.Serializable;

public class ResponseHeader implements Serializable {

	private int status;
	private String message;

	public ResponseHeader() {
		super();
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess(){
 		return status == 1000;
	}


}
