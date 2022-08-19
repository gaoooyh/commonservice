package com.tools.commonservice.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tools.commonservice.data.constant.ConstantsKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;

public class JWTUtil {
    static Logger log = LoggerFactory.getLogger(JWTUtil.class);
    private static final String KEY = "PSLCYKOWQWIEYCUSOEOMCEGALPOEA";

    private static final String DEFAULT_AGENT = "DEFAULT_AGENT";

    public static String generateToken(Long userId) {
        return generateToken(userId.toString(), DEFAULT_AGENT);
    }

    public static String generateToken(String userId) {
        return generateToken(userId, DEFAULT_AGENT);
    }


    public static String generateToken(String userId, String agent) {
        return JWT.create()
                .withClaim(ConstantsKey.USER_ID,userId)
                .withClaim(ConstantsKey.CREATE_AT, System.currentTimeMillis())
                .withClaim(ConstantsKey.AGENT, agent)
                .withExpiresAt(new Date(Long.MAX_VALUE))
                .sign(Algorithm.HMAC256(KEY));

    }

    public static boolean verify(String token) {
        try {
            JWT.require(Algorithm.HMAC256(KEY)).build().verify(token);
            return true;
        } catch (JWTDecodeException ex) {
            log.error(ex.getMessage(), ex);
        }
        return false;
    }


    public static String getUserId(String token) {
        try {
            DecodedJWT decoded = JWT.require(Algorithm.HMAC256(KEY)).build().verify(token);
            return decoded.getClaim("userId").asString();
        } catch (JWTDecodeException ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    public static String getKey(String token, String key) {
        try {
            DecodedJWT decoded = JWT.require(Algorithm.HMAC256(KEY)).build().verify(token);
            Claim claim = decoded.getClaim(key);
            if(claim != null) {
                return claim.asString();
            }
        } catch (JWTDecodeException ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    public static boolean compareAgent(String agent, String token) {
        try {
            DecodedJWT decoded = JWT.require(Algorithm.HMAC256(KEY)).build().verify(token);
            return decoded.getClaim(ConstantsKey.AGENT).asString().equals(agent);
        } catch (JWTDecodeException ex) {
            log.error(ex.getMessage(), ex);
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(generateToken("gaoooyh"));
        System.out.println(getUserId("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjkyMjMzNzIwMzY4NTQ3NzUsInVzZXJJZCI6Imdhb29veWgiLCJjcmVhdGVBdCI6MTY0ODcwODU3Mjc5NX0.Xb29ZOVmBKMtxVlUIhqk0j-sBLZ26hi9CMiqaBci_Pw"));
    }
}
