package com.tothenew.bootcamp.constants;

import com.tothenew.bootcamp.enums.UsersType;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class UrlLink {


    public static String getActivationUrlWithTokenInParam(int timeForToken,String emailForToken,String roleForToken){
        String jwt = JwtUtility.createJWT(timeForToken, "activation", emailForToken, roleForToken);
        String userActivationLink = ServletUriComponentsBuilder.fromCurrentRequest().replacePath("user/activation?token={jwt}").buildAndExpand(jwt).toUriString();
        return userActivationLink;
    }

}
