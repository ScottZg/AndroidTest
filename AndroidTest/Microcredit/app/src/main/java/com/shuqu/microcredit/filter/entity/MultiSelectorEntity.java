package com.shuqu.microcredit.filter.entity;

/**
 * Created by wuxin on 16/7/26.
 */
public class MultiSelectorEntity {

    private String name;
    private Object obj;
    private boolean isChecked;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
