package com.example.my_mvc_project.utils;

import com.example.my_mvc_project.exceptions.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    @Value(value = "${secure.key}")
    private String secureKey;

    public String encode(String data){
        Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 60*24);
        return Jwts.builder()
                .setIssuer("localhost:8080")
                .setIssuedAt(new Date())
                .setSubject(data)
                .setExpiration(expiration)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String decode(String encodedData){
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(encodedData)
                    .getBody().getSubject();
        }catch (Exception e){
            throw new UnauthorizedException("Registratsiyadan o'ting");
        }
    }
    private Key key(){
        byte[] decode = Decoders.BASE64.decode(secureKey);
        return Keys.hmacShaKeyFor(decode);
    }
}
