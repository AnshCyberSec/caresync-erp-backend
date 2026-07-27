package com.caresync.erp.common.constants;

public class AppConstants {

    private AppConstants(){

    }
    public static final String JWT_SECRET =
            "hospital-management-secret-key-very-secure";

    public static final long JWT_EXPIRATION_TIME =
            24 * 60 * 60 * 1000; // 24 hours

    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

}
