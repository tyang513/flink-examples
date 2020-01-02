package com.talkingdata.flink.entity;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author tao.yang
 * @date 2019-12-31
 */
public class TraceLog {

    private String id;

    private String traceId;

    private String kind;

    private String name;

    private long timestamp;

    private int duration;

    private LocalEndpoint localEndpoint;

    private Tags tags;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalEndpoint getLocalEndpoint() {
        return localEndpoint;
    }

    public void setLocalEndpoint(LocalEndpoint localEndpoint) {
        this.localEndpoint = localEndpoint;
    }

    public Tags getTags() {
        return tags;
    }

    public void setTags(Tags tags) {
        this.tags = tags;
    }

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String text = "{\"traceId\":\"9cfc679d5a27afbd\",\"id\":\"9cfc679d5a27afbd\",\"kind\":\"SERVER\",\"name\":\"gw-point\",\"timestamp\":1577774655854833,\"duration\":84,\"localEndpoint\":{\"serviceName\":\"gateway\",\"ipv4\":\"172.20.33.8\"},\"tags\":{\"http.path\":\"/data/wjtest/t9501\"}}";

        TraceLog log = objectMapper.readValue(text, TraceLog.class);
        System.out.println(log.getId());

    }

}
