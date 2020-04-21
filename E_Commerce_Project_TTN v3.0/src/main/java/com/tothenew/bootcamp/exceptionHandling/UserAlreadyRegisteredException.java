package com.tothenew.bootcamp.exceptionHandling;

import com.tothenew.bootcamp.pojo.CommonResponseVO;
import com.tothenew.bootcamp.enums.StatusCode;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

public class UserAlreadyRegisteredException extends RuntimeException {


    //--------------------------------------------------------------------------------------------------------------->
    public String getMessage(){
        return StatusCode.ALREADY_REGISTERED.toString();
    }





    //--------------------------------------------------------------------------------------------------------------->
    /***
     *
     * @return commonErrorResponse
     * @description
     * ->returns obj containing the msg
     */
    public CommonResponseVO getErrorResponse(){
        CommonResponseVO commonResponseVO = new CommonResponseVO();
        HashMap<Integer,String> hashMap = new HashMap<Integer, String>();
        hashMap.put(0,getMessage());
        commonResponseVO.setMessage(hashMap);
        commonResponseVO.setName(this.getClass().getSimpleName());
        return commonResponseVO;
    }

    public HttpStatus getHttpStatus(){
        return HttpStatus.UNAUTHORIZED;
    }
}
