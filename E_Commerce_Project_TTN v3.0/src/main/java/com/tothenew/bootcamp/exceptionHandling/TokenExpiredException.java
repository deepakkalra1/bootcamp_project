package com.tothenew.bootcamp.exceptionHandling;

import com.tothenew.bootcamp.enums.StatusCode;
import com.tothenew.bootcamp.pojo.CommonResponseVO;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

public class TokenExpiredException extends RuntimeException {



    public CommonResponseVO getErrorResponse(){
        return new CommonResponseVO(
                Arrays.asList(StatusCode.TOKEN_EXPIRED.toString())
        );
    }
    public HttpStatus getHttpStatus(){
        return HttpStatus.UNAUTHORIZED;
    }

}
