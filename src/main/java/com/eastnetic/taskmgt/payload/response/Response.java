package com.eastnetic.taskmgt.payload.response;

public class Response {

    String code, message;
    String successCode = "0000";
    String successMessage = "Success";

    public Response(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public Response() {
        this.code = this.successCode;
        this.message = this.successMessage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
