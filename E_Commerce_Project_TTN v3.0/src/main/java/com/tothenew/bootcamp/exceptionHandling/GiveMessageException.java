package com.tothenew.bootcamp.exceptionHandling;

import com.tothenew.bootcamp.pojo.CommonResponseVO;
import org.springframework.http.HttpStatus;

import java.util.List;

public class GiveMessageException extends RuntimeException {
   private List<String> messageList;
    private List<String> descriptionList;
    private int triggeredConstructor=0;

    public GiveMessageException(List<String> message){
        this.messageList=message;
        triggeredConstructor=1;
    }

    public GiveMessageException(List<String> message,List<String> description){
        this.messageList=message;
        this.descriptionList=description;
        triggeredConstructor=2;
    }

    //------------------------------------------------------------------------------------------------------------->
    /***
     *
     * @return commonErrorResponse
     * @description
     * ->returns obj containing the msg and desciption
     */
    public CommonResponseVO getErrorResponse(){
        CommonResponseVO commonResponseVO;

        if (triggeredConstructor==1){
           commonResponseVO = new CommonResponseVO(messageList);
            commonResponseVO.setName(this.getClass().getSimpleName());
            return commonResponseVO;
        }
        else if (triggeredConstructor==2){
            commonResponseVO= new CommonResponseVO(messageList,descriptionList);
            commonResponseVO.setName(this.getClass().getSimpleName());
            return commonResponseVO;
        }
    return commonResponseVO=new CommonResponseVO(null);
    }

    public HttpStatus getHttpStatus(){
        return HttpStatus.BAD_REQUEST;
    }
}
