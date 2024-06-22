package com.kenshu.service;

import com.kenshu.dao.UserDao;
import com.kenshu.model.bean.UserBean;
import com.kenshu.model.dto.UserDto;

public class LoginService {
    private UserDao userDao = new UserDao();

    public boolean login(String loginidStr, String password) {
        if (loginidStr == null || loginidStr.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("missing_parameters");
        }

        int loginid;
        try {
            loginid = Integer.parseInt(loginidStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("invalid_loginid");
        }

        UserDto storedUser = userDao.findUserByLoginid(loginid);

        if (storedUser != null && storedUser.size() > 0) {
            UserBean storedUserBean = storedUser.get(0);
            return password.equals(storedUserBean.getPassword());
        } else {
            return false;
        }
    }

    public UserDto getUserDto(String loginidStr) {
        int loginid = Integer.parseInt(loginidStr);
        return userDao.findUserByLoginid(loginid);
    }
}
