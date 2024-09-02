package com.kenshu.model.bean;

public class UserBean {
    private String loginid;
    private String password;
    private int authcode;

    public UserBean(String loginid, String password, int authcode) {
        this.loginid = loginid;
        this.password = password;
        this.authcode = authcode;
    }

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAuthcode() {
        return authcode;
    }

    public void setAuthcode(int authcode) {
        this.authcode = authcode;
    }
}
