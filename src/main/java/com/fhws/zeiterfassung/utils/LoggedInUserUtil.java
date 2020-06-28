package com.fhws.zeiterfassung.utils;

public class LoggedInUserUtil {

    private final JwtUtil jwtUtil;

    public LoggedInUserUtil(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public static String getUsernameFromAuthorizationToken(String authorization) {
        String token = authorization.substring(7);
        return jwtUtil.extractUsername(token);
    }
}
