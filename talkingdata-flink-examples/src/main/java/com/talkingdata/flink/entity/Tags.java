package com.talkingdata.flink.entity;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonAlias;

/**
 * @author tao.yang
 * @date 2019-12-31
 */
public class Tags {

    @JsonAlias("http.path")
    private String httpPath;

    public String getHttpPath() {
        return httpPath;
    }

    public void setHttpPath(String httpPath) {
        this.httpPath = httpPath;
    }
}
