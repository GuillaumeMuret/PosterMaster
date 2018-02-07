package com.murey.poster.postermaster.communication.requests.base;

import android.content.Context;
import android.content.DialogInterface;

import java.util.ArrayList;

public class EseoRetrofitRequestQueue {
    ArrayList<RequestQueueItem> requestsQueue = new ArrayList<>();
    ArrayList<RequestQueueListener> requestsListeners = new ArrayList<>();
    RequestQueueItem currentItem;

    Object queueId;
    boolean showDialog;
    Context context;

    public EseoRetrofitRequestQueue(Object queueId) {
        this.queueId = queueId;
    }

    public EseoRetrofitRequestQueue(Object queueId, boolean showDialog, Context context) {
        this.queueId = queueId;
        this.showDialog = showDialog;
        this.context = context;
    }

    public void addRequest(EseoRetrofitBaseOperation request, boolean continueIfFailed){
        requestsQueue.add(new RequestQueueItem(request, continueIfFailed));
    }

    public void addObserver(RequestQueueListener observer){
        requestsListeners.add(observer);
    }

    public void startRequests(){
        startNextRequest();
    }

    private void startNextRequest() {
        if(requestsQueue.size() < 1) return;
        currentItem = requestsQueue.remove(0);

        currentItem.request.addRequestObserver(observer);
        currentItem.request.startRequest();
    }

    public void cancel(){
        if(currentItem != null)
            currentItem.request.cancel();
        for(RequestQueueItem itr:requestsQueue)
            itr.request.cancel();
    }

    EseoRetrofitRequestObserver observer = new EseoRetrofitRequestObserver() {
        @Override
        public void handleRequestFinished(Object requestId, EseoRetrofitResponse eseoRetrofitResponse) {

            if(eseoRetrofitResponse.getCode()!=-1 || currentItem.continueIfFailed){
                if(requestsQueue.size() < 1)
                    for(RequestQueueListener itr : requestsListeners)
                        itr.onAllRequestsFinished(queueId);
                else
                    startNextRequest();
            }
        }

        @Override
        public void requestCanceled(Object requestId, Throwable error) {

        }
    };

    private static class RequestQueueItem{
        EseoRetrofitBaseOperation request;
        boolean continueIfFailed;
        String errorMessage;
        DialogInterface.OnClickListener onCancelListener;

        public RequestQueueItem(EseoRetrofitBaseOperation request, boolean continueIfFailed) {
            this.request = request;
            this.continueIfFailed = continueIfFailed;
        }
    }

    public interface RequestQueueListener{
        void onAllRequestsFinished(Object QueueId);
        void onRequestFinished(Object requestId, Throwable error, Object resultObject, int requestIndex);
        void requestCanceled(Object requestId, Throwable error);
    }
}
