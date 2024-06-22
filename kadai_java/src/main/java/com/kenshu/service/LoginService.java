package com.kenshu.service;

import com.kenshu.dao.UserDao;
import com.kenshu.model.bean.UserBean;

public class LoginService {
    private UserDao userDao = new UserDao();

    public UserBean login(int loginid, String password) {
        // データベースからユーザーを取得
        return userDao.getUserByLoginIdAndPassword(loginid, password);
    }
}
