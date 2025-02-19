package com.springboot.ezenbackend.controller;

import java.sql.Date;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.ezenbackend.util.CustomJWTException;
import com.springboot.ezenbackend.util.JWTUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
public class APIRefreshController {
    @RequestMapping("/api/member/refresh")
    public Map<String, Object> refresh(@RequestHeader("Authorization") String authHeader, String refreshToken) {
        if (refreshToken == null) {
            throw new CustomJWTException("NULL_REFRESH");
        }

        if (authHeader == null || authHeader.length() < 7) {
            throw new CustomJWTException("IWALID_STRING");
        }

        String accessToken = authHeader.substring(7);
        if (checkExpiredToken(accessToken) == false) {
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        }

        Map<String, Object> claims = JWTUtil.vaildateToken(refreshToken);

        log.info("refresh... claims: " + claims);

        String newAcessToken = JWTUtil.generateToken(claims, 10);

        String newRefreshToken = checkTime((Integer) claims.get("exp")) == true ? JWTUtil.generateToken(claims, 60 * 24)
                : refreshToken;

        return Map.of("accessToken", newAcessToken, "refreshToken", refreshToken);

    }

    private boolean checkTime(Integer exp) {
        Date expDate = new Date((long)exp * (1000));
        long gap = expDate.getTime() - System.currentTimeMillis();

        long leftMin = gap / (1000*60);

        return leftMin<60;
    }

    private boolean checkExpiredToken(String token) {
        try{
            JWTUtil.vaildateToken(token);

        }catch(CustomJWTException ex){
            if(ex.getMessage().equals("Expired")){
                return true;
            }
        }
        return false;
    }
}
