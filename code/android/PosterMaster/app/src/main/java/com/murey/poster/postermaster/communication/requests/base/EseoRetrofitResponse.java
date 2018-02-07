package com.murey.poster.postermaster.communication.requests.base;

import com.murey.poster.postermaster.communication.message.WebMessage;
import com.murey.poster.postermaster.communication.requests.EseoBaseRequest;

public class EseoRetrofitResponse extends Throwable {

    public static final int FAILURE_CODE = -2;

    private int code;
    private String message;
    private WebMessage webMessage;
    private EseoBaseRequest eseoBaseRequest;


    public EseoRetrofitResponse(EseoBaseRequest eseoBaseRequest, int code, String message) {
        this.eseoBaseRequest = eseoBaseRequest;
        this.code = code;
        this.message = message;
        this.webMessage=null;
    }

    public EseoRetrofitResponse(EseoBaseRequest eseoBaseRequest, Throwable throwable) {
        super(throwable);
        this.eseoBaseRequest = eseoBaseRequest;
        this.code=-2;
        this.webMessage=null;
    }

    public EseoRetrofitResponse(EseoBaseRequest eseoBaseRequest, int code, String message, WebMessage webMessage) {
        this.eseoBaseRequest = eseoBaseRequest;
        this.code = code;
        this.message = message;
        this.webMessage = webMessage;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public WebMessage getWebMessage() {
        return webMessage;
    }

    public void setWebMessage(WebMessage webMessage) {
        this.webMessage = webMessage;
    }

    public EseoBaseRequest getEseoBaseRequest() {
        return eseoBaseRequest;
    }

    public void setEseoBaseRequest(EseoBaseRequest eseoBaseRequest) {
        this.eseoBaseRequest = eseoBaseRequest;
    }
}
