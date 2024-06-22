package com.kenshu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.kenshu.model.bean.UserBean;
import com.kenshu.model.dto.UserDto;

public class UserDao {
    private Connection connection;

    public UserDto findUserByLoginid(int loginid) {
        UserDto userDto = new UserDto();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM users WHERE loginid = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, loginid);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int retrievedLoginId = rs.getInt("loginid");
                String retrievedPassword = rs.getString("password");
                int retrievedAuthCode = rs.getInt("authcode");
                userDto.add(new UserBean(retrievedLoginId, retrievedPassword, retrievedAuthCode));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return userDto;
    }
}
