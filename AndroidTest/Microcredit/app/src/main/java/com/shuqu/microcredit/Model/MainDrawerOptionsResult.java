package com.shuqu.microcredit.Model;

import java.util.List;

/**
 * Created by wuxin on 16/9/11.
 */
public class MainDrawerOptionsResult {
    private int code = -1;
    private String message;
    private List<MainDrawerOptionItem> result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<MainDrawerOptionItem> getResult() {
        return result;
    }

    public void setResult(List<MainDrawerOptionItem> result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return code == 0;
    }
}
