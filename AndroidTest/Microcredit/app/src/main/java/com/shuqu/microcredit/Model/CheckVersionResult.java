package com.shuqu.microcredit.Model;

/**
 * Created by wuxin on 16/6/29.
 */
public class CheckVersionResult {

    private String name;
    private String versionShort;
    private String build;
    private String update_url;

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getUpdate_url() {
        return update_url;
    }

    public void setUpdate_url(String update_url) {
        this.update_url = update_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersionShort() {
        return versionShort;
    }

    public void setVersionShort(String versionShort) {
        this.versionShort = versionShort;
    }
}
