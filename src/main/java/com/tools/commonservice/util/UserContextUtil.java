package com.tools.commonservice.util;

import com.tools.commonservice.exception.ApiException;
import com.tools.commonservice.exception.Constants;

public class UserContextUtil {
    private static final ThreadLocal<String> userThreadLocal = new ThreadLocal<>();

    private static final ThreadLocal<ApiException> errorThreadLocal = new ThreadLocal<>();

     public static void setCurrentUser(String userId) {
         userThreadLocal.set(userId);
     }

     public static String getCurrentUser() {
         return userThreadLocal.get();
     }

    public static void setCurrentError(ApiException ex) {
        errorThreadLocal.set(ex);
        throw new ApiException(Constants.ERROR_NOT_LOGIN);
    }

    public static void cleanThreadLocal() {
         userThreadLocal.remove();
         errorThreadLocal.remove();
    }

    public static ApiException getCurrentError() {
        return errorThreadLocal.get();
    }
}
