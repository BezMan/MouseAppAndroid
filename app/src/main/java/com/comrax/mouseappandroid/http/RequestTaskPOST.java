package com.mouse.world.http;

public class RequestTaskPOST extends RequestTask {

    public RequestTaskPOST(RequestTaskDelegate delegate) {
        super(delegate);
        setRequestType("POST");
    }

    protected void onPostExecute(String result) {
        _delegate.onTaskPOSTCompleted(result, this);
    }

    @Override
    protected void setRequestType(String requestType) {
        _requestType = requestType;
    }
}

