package com.mouse.world.http;

public class RequestTaskGet extends RequestTask {

    public RequestTaskGet(RequestTaskDelegate delegate) {
        super(delegate);
        setRequestType("GET");
    }

    @Override
    protected void onPostExecute(String result) {
        _delegate.onTaskGETCompleted(result, this);
    }

    @Override
    protected void setRequestType(String requestType) {
        _requestType = requestType;
    }
}
