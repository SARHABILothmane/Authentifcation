package com.sarhabil.demo.exception;

import lombok.*;

/**
 * <h2>BaseResponse</h2>
 *
 * @author aek
 */

public class ApiResponse {

    private Object data;
    private String message;
    private boolean error = true;

    
    public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public ApiResponse(Object data, String message){
        this.data = data;
        this.message = message;
    }

	public ApiResponse(Object data, String message, boolean error) {
		super();
		this.data = data;
		this.message = message;
		this.error = error;
	}
    
}
