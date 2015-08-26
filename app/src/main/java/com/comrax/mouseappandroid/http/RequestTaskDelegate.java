package com.mouse.world.http;

public interface RequestTaskDelegate {
    void onTaskPOSTCompleted(String result, RequestTask task);
    void onTaskGETCompleted(String result, RequestTask task);
}
