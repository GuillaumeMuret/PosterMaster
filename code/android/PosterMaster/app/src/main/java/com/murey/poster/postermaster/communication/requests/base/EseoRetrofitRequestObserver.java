package com.murey.poster.postermaster.communication.requests.base;

public interface EseoRetrofitRequestObserver {
    /**
     * This method is called by OperationExecuterQueue class only. It must not called by any Request implementor. Method
     * is called to send event to specific request observer telling that request has been completed. Method is called in
     * case of any exception (un-handled or business) Method not called If request is cancelled.
     *
     * @param requestId
     * @param eseoRetrofitResponse
     */
    void handleRequestFinished(Object requestId, EseoRetrofitResponse eseoRetrofitResponse);
    void requestCanceled(Object requestId, Throwable error);
}
