package com.example.shuangxiang.qdkelenqdforphone.bean;

/**
 * Created by Administrator on 2016/6/10 0010.
 */
public class LoginErrorInfo {
    /**
     * count : 7
     * error : 密码错误，您还有7次机会
     */

    private int count;
    private String error;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }




}
