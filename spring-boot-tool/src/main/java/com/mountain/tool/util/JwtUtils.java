package com.mountain.tool.util;

import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.Map;

public class JwtUtils {

    /**
     * 1、选择签名的算法
     * 2、生成签名的密钥
     * 3、构建Token信息
     * 4、利用算法和密钥生成Token
     *
     * @param id     唯一的id
     * @param claims Map
     */
    public static String createToken(String id, Map<String, Object> claims) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] secretBytes = DatatypeConverter.parseBase64Binary("JWT-TOKEN");
        Key signingKey = new SecretKeySpec(secretBytes, signatureAlgorithm.getJcaName());
        Date expire = new Date(System.currentTimeMillis() + 10 * 60 * 1000);
        JwtBuilder builder = Jwts.builder().setClaims(claims)
                .setId(id)
                .setIssuedAt(new Date())
                .setExpiration(expire)
                .signWith(signatureAlgorithm, signingKey);
        return builder.compact();
    }

    /**
     * 验证token是否正确
     *
     * @param token token
     * @return Claims
     */
    public static Claims parseToken(String token) throws Exception {
        return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary("JWT-TOKEN"))
                .parseClaimsJws(token).getBody();
    }

}
