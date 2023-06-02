package com.baloot.IE.JWT.utility;

import com.baloot.IE.FIlter.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

public class JwtUtils {
    private static final String SECRET_KEY = "baloot";
    private static final String ISSUER = "Baloot.com";

    private static Date expirationDate() {
        long curTime = System.currentTimeMillis();
        long EXPIRE_PERIOD = 24 * 60 * 60 * 1000;
        return new Date(curTime + (EXPIRE_PERIOD));
    }

    public static String createJWT(String username) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder();
        builder.setIssuer(ISSUER);
        builder.setIssuedAt(new Date(System.currentTimeMillis()));
        builder.setExpiration(expirationDate());
        builder.setAudience(username);
        builder.signWith(signatureAlgorithm, signingKey);

        return builder.compact();
    }

    public static String verifyJWT(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(jwt).getBody();
        if(claims.getIssuedAt() == null ||
                claims.getExpiration()== null ||
                claims.getIssuer() == null)
            throw new UnauthorizedException("Authorization invalid");
        if (claims.getExpiration().getTime() < System.currentTimeMillis())
            return null;
        return claims.getIssuer();
    }
}
