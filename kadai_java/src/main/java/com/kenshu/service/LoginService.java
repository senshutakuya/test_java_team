package com.kenshu.service;

import com.kenshu.dao.UserDao;
import com.kenshu.model.bean.UserBean;

public class LoginService {
    private static UserDao userDao = new UserDao();

    public UserBean login(String loginid, String password) {
        // データベースからユーザーを取得
        return userDao.getUserByLoginIdAndPassword(loginid, password);
    }

    public static String userAdd(String name, String loginid, String password, String authcodeStr) {
        StringBuilder validationMessage = new StringBuilder();
        int authcode;
        
        try {
//          authcodeをintがたに変換  
            authcode = Integer.parseInt(authcodeStr);
        }catch(NumberFormatException e){
        	return "エラーが発生しました暫く待ってからやり直してください";
        }


        // バリデーションチェックを行い、エラーメッセージを取得
        boolean validation_flag = userAddValidateCheck(name, loginid, password, authcode, validationMessage);

        if (validation_flag) {
            try {
                userDao.newUserAdd(name, loginid, password, authcode);
                System.out.println("一応通った？");
                return "ユーザの追加に成功しました";
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("エラー出てるわ");
                return "ユーザの追加に失敗しました";
            }
        } else {
        	System.out.println("validation_flagが"+validation_flag);
            return validationMessage.toString();
        }
    }

    private static boolean userAddValidateCheck(String name, String loginid, String password, int authcode, StringBuilder errorMessage) {
        boolean isValid = true;
        
        if (name == null || name.length() > 20) {
            errorMessage.append("・名前は20文字以内にしてください\n");
            isValid = false;
        }
        if (loginid == null || !loginid.matches("^[a-zA-Z0-9$_]{8,24}$")) {
            errorMessage.append("・ユーザIDは半角英数字(記号は$と_のみ)で、8文字以上24文字以内にしてください\n");
            isValid = false;
        }
        if (password == null || !password.matches("^[a-zA-Z0-9$_]{8,24}$")) {
            errorMessage.append("・パスワードは半角英数字(記号は$と_のみ)で、8文字以上24文字以内にしてください\n");
            isValid = false;
        }
        if (!(authcode == 1 || authcode == 2)) {
        	errorMessage.append("販売管理者か注文管理者を選択してください");
        	isValid = false;
        }

        if (!isValid) {
            errorMessage.insert(0, "入力に誤りがあります:\n");
        }
        
        System.out.println("入力値のチェックname:"+name+"loginid"+loginid+"password"+password+"authcode"+authcode+"errorMessage"+errorMessage);
        System.out.println("debug isValidの値は" + isValid);
        return isValid;
    }
	
	
}
