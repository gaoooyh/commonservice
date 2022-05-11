package com.tools.commonservice.util;

public class UserContextUtil {
    private static final ThreadLocal<String> userThreadLocal = new ThreadLocal<>();

     public static void setCurrentUser(String userId) {
         userThreadLocal.set(userId);
     }

     public static String getCurrentUser() {
         return userThreadLocal.get();
     }
}
