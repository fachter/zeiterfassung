package com.fhws.zeiterfassung.utils;

import org.springframework.stereotype.Service;

@Service
public class LoggedInUserUtil {

    private final JwtUtil jwtUtil;

    public LoggedInUserUtil(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String getUsernameFromAuthorizationToken(String authorization) {
        String token = authorization.substring(7);
        return jwtUtil.extractUsername(token);
    }
}
