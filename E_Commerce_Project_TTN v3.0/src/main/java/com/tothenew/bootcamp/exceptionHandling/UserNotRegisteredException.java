package com.tothenew.bootcamp.exceptionHandling;

import com.tothenew.bootcamp.pojo.CommonResponseVO;
import com.tothenew.bootcamp.enums.StatusCode;
import org.springframework.http.HttpStatus;

import java.util.Arrays;


public class UserNotRegisteredException extends RuntimeException {



    //--------------------------------------------------------------------------------------------------------->
    public String getMessage(){
        return StatusCode.NOT_REGISTERED.toString();
    }

    //--------------------------------------------------------------------------------------------------------->

    public CommonResponseVO getErrorResponse(){

        CommonResponseVO commonResponseVO = new CommonResponseVO(
                Arrays.asList(StatusCode.NOT_REGISTERED.toString())
        );
        commonResponseVO.setName(this.getClass().getSimpleName());
        return commonResponseVO;
    }

    public HttpStatus getHttpStatus(){
        return HttpStatus.NOT_FOUND;
    }
}
