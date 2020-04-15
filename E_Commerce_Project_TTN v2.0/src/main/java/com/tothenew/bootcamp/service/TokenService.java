package com.tothenew.bootcamp.service;

import com.tothenew.bootcamp.constants.JwtUtility;
import com.tothenew.bootcamp.entity.Token;
import com.tothenew.bootcamp.enums.StatusCode;
import com.tothenew.bootcamp.exceptionHandling.GiveMessageException;
import com.tothenew.bootcamp.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TokenService {

    @Autowired
    TokenRepository tokenRepository;


    public void storeTokenInDB(String jwt){
        System.out.println("yes yes came here");
        System.out.println(jwt);
        String email = JwtUtility.findUsernameFromToken(jwt);
        Token token1 = new Token();
        token1.setEmail(email);
        token1.setToken(jwt);
        tokenRepository.save(token1);

    }


    @Transactional
    public boolean consumeTokenInDB(String jwt){

        String email = JwtUtility.findUsernameFromToken(jwt);
        try {
            List<Token> tokens = tokenRepository.findTokensByEmail(email);
            for (Token token: tokens){
                if (jwt.equals(token.getToken())){
                    tokenRepository.deleteTokenWithId(token.getId());
                    return true;
                }

            }
            throw new GiveMessageException(Arrays.asList(StatusCode.TOKEN_INVALID.toString()));


        }
        catch (NullPointerException e){
            throw new GiveMessageException(Arrays.asList(StatusCode.TOKEN_INVALID.toString()));
        }

    }


}
