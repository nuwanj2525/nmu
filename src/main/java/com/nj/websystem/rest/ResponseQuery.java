package com.nj.websystem.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResponseQuery {

    private List<QueryResponseData> response = new ArrayList<>();
    private List<String> responseHeader = new ArrayList<>();
    private List<Map<String, Object>> resultMap = new ArrayList<>();
    private boolean success;
    private String exeception;
    private int recCount;

    public List<QueryResponseData> getResponse() {
        return response;
    }

    public void setResponse(List<QueryResponseData> response) {
        this.response = response;
    }

    public List<String> getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(List<String> responseHeader) {
        this.responseHeader = responseHeader;
    }

    public List<Map<String, Object>> getResultMap() {
        return resultMap;
    }

    public void setResultMap(List<Map<String, Object>> resultMap) {
        this.resultMap = resultMap;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getExeception() {
        return exeception;
    }

    public void setExeception(String exeception) {
        this.exeception = exeception;
    }

    public int getRecCount() {
        return recCount;
    }

    public void setRecCount(int recCount) {
        this.recCount = recCount;
    }
}
