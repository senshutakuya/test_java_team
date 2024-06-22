package com.kenshu.model.dto;

import java.util.ArrayList;

import com.kenshu.model.bean.UserBean;

public class UserDto {
    private ArrayList<UserBean> userList;

    public UserDto() {
        this.userList = new ArrayList<>();
    }

    public void add(UserBean userBean) {
        userList.add(userBean);
    }

    public UserBean get(int i) {
        return userList.get(i);
    }

    public int size() {
        return userList.size();
    }
}
