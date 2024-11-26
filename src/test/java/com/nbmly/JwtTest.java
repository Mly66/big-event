package com.nbmly;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {

    @Test
    public void testGen(){
        Map<String,Object> claims = new HashMap<>();
        claims.put("id",1);
        claims.put("username","张三");
        //生成jwt代码
        String token = JWT.create()
                .withClaim("user",claims)//添加载荷
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60*12))//过期时间
                .sign(Algorithm.HMAC256("nbmly"));//制定算法，配置密钥

        System.out.println(token);
    }
    @Test
    public void testParse(){
        String token ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
                ".eyJ1c2VyIjp7ImlkIjoxLCJ1c2VybmFtZSI6IuW8oOS4iSJ9LCJleHAiOjE3MjEyNjgzMjl9" +
                ".95qsYPDRHxVkRcHZs00NDZxYKOovFV-7b6CMneB47_4";
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("nbmly")).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);//验证token，生成解析后对象
        Map<String, Claim> claims = decodedJWT.getClaims();
        System.out.println(claims.get("user"));
        //头部载荷部分不可修改
        //私人密钥不可修改
        //在token有效期内验证

    }
}
