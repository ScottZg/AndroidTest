package com.zhanggui.recyclerview;

/**
 * Created by zhanggui on 2017/2/20.
 */

public class Fruit {
    public String getName() {
        return name;
    }

    public int getIamgeId() {
        return iamgeId;
    }

    private String name;
    private  int iamgeId;

    public Fruit(String name,int imageId) {
        this.name = name;
        this.iamgeId = imageId;
    }


}
