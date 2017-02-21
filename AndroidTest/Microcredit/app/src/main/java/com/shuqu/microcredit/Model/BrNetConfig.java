package com.shuqu.microcredit.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wuxin on 16/6/21.
 */
public class BrNetConfig implements Serializable {

    /**
     * code : 0
     * message : Success.
     * hosts : ["dymapi.100credit.com","dym.100credit.com"]
     */
    private List<String> hosts;

    public List<String> getHosts() {
        return hosts;
    }

    public void setHosts(List<String> hosts) {
        this.hosts = hosts;
    }

}
