package com.nj.websystem.rest;

import java.util.ArrayList;
import java.util.List;

public class QueryResponseData {

    List<String> data = new ArrayList<>();

    public QueryResponseData() {
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
