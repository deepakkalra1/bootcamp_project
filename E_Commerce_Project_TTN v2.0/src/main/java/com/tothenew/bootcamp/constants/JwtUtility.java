package com.tothenew.bootcamp.constants;

import com.tothenew.bootcamp.exceptionHandling.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class JwtUtility {



    //---------------------------------------------------------------------------------------------------------------->>
    /***
     * @param minutes
     * @param subject
     * @param username
     * @return token
     * it takes -> minutes for token to expire, subject for token, username(email) for token
     */
    public static String createJWT(int minutes,String subject,String username,String role){

       String token= Jwts.builder()
               .signWith(SignatureAlgorithm.HS256,"tothenew".getBytes())
               .claim("issuer","tothenew")
               .claim("subject",subject)
               .claim("user_name",username)
               .claim("role",role)
               .claim("expiry",new Date().getTime()+ (minutes*1000*60))
               .compact();


        return token;
    }





    //---------------------------------------------------------------------------------------------------------------->>
    /***
     * @param token
     * @param subject
     * @return boolean
     * this method takes token, subject (to match with.. which has been putted in token)
     * and match expiry time.. usernmae and subject.. after parsing token
     * then return true else false
     */
    public static boolean verifyJwtToken(String token,String subject,String username,String role){
        Jws<Claims> jws = Jwts.parser().setSigningKey("tothenew".getBytes()).parseClaimsJws(token);

        //if subject and username matches then allow to go in
        if (    jws.getBody().get("subject").equals(subject) &&
                jws.getBody().get("user_name").equals(username) &&
                jws.getBody().get("role").equals(role)

            )
        {
            //if token time has been time out ... then exception tokenExpired..
            if (Long.parseLong(jws.getBody().get("expiry").toString())   >= new Date().getTime()){
                return true;
            }
            else {
                throw new TokenExpiredException();
            }
        }
return false;
    }






    //---------------------------------------------------------------------------------------------------------------->
    public static String whatIsRoleInToken(String token){
        Jws<Claims> jws= Jwts.parser().setSigningKey("tothenew".getBytes()).parseClaimsJws(token);
       return jws.getBody().get("role").toString();
    }





    //----------------------------------------------------------------------------------------------------------->
    public static String findUsernameFromToken(String token){
        Jws<Claims> claimsJws= Jwts.parser().setSigningKey("tothenew".getBytes()).parseClaimsJws(token);
        return (String) claimsJws.getBody().get("user_name");
    }



}











