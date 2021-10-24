package com.flight.dto.response;
//TODO lombok can be used here, but as of now out of scope of POC
public class ErrorResponse {

    private String errorCode;
    private String messageEn;

    public ErrorResponse(String errorCode, String messageEn) {
        this.errorCode = errorCode;
        this.messageEn = messageEn;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessageEn() {
        return messageEn;
    }

    public void setMessageEn(String messageEn) {
        this.messageEn = messageEn;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "errorCode='" + errorCode + '\'' +
                ", messageEn='" + messageEn + '\'' +
                '}';
    }
}
