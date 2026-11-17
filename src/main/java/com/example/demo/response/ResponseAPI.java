package com.example.demo.response;


public class ResponseAPI<T> {
	
	private String status;
    private T data;

    public ResponseAPI() {
    }

    public ResponseAPI(String status, T data) {
        this.status = status;
        this.data = data;
    }


    public static <T> ResponseAPI<T> success(T data) {
        return new ResponseAPI<T>("SUCCESS", data);
    }


    public static <T> ResponseAPI<T> error(String status, T data) {
        return new ResponseAPI<T>(status, data);
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    
    

}
