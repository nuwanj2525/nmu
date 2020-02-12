package com.nj.websystem.rest;

import java.util.List;
import java.util.Map;

public class CommonRest {

    Map<String, List> propListMap;
    Map<String, String> prop;
    List propList;

    public Map<String, List> getPropListMap() {
        return propListMap;
    }

    public void setPropListMap(Map<String, List> propListMap) {
        this.propListMap = propListMap;
    }

    public Map<String, String> getProp() {
        return prop;
    }

    public void setProp(Map<String, String> prop) {
        this.prop = prop;
    }

    public List getPropList() {
        return propList;
    }

    public void setPropList(List propList) {
        this.propList = propList;
    }
}
