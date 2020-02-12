package com.nj.websystem.rest;

public class HttpResponse {

    private Object response;
    private boolean success;
    private String exception;
    private long recCount;

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public long getRecCount() {
        return recCount;
    }

    public void setRecCount(long recCount) {
        this.recCount = recCount;
    }
}
