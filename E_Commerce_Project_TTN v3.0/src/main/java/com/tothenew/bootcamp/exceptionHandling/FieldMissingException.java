package com.tothenew.bootcamp.exceptionHandling;

import com.tothenew.bootcamp.pojo.CommonResponseVO;
import com.tothenew.bootcamp.enums.StatusCode;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

public class FieldMissingException extends RuntimeException {


    public CommonResponseVO getErrorResponse(){
        CommonResponseVO commonResponseVO = new CommonResponseVO();
        commonResponseVO.setName(this.getClass().getSimpleName());
        HashMap<Integer,String> hashMap = new HashMap<>();
        hashMap.put(0, StatusCode.FIELD_MISSING.toString());
        commonResponseVO.setMessage(hashMap);
        return commonResponseVO;
    }

    public HttpStatus getHttpStatus(){
        return HttpStatus.BAD_REQUEST;
    }
}
