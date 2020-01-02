package com.talkingdata.flink.entity;

/**
 * @author tao.yang
 * @date 2019-12-31
 */
public class LocalEndpoint {

    private String serviceName;

    private String ipv4;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getIpv4() {
        return ipv4;
    }

    public void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }
}
