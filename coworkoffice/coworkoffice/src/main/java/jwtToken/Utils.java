package jwtToken;

import jwtToken.jwtlib.JWTfun;

public class Utils {
    public static String getRole(){
        if(JWTfun.getUserType().contains("user")) return "user";
        else if(JWTfun.getUserType().contains("admin")) return "admin";
        else return "";
    }
    public static String getUserId(){
        String userIdRaw = JWTfun.getUserId();
        String userId = userIdRaw.substring(1,userIdRaw.length()-1);
        return userId;
    }
}
