package com.tothenew.bootcamp.exceptionHandling;

import com.tothenew.bootcamp.pojo.CommonResponseVO;
import com.tothenew.bootcamp.enums.StatusCode;
import io.jsonwebtoken.MalformedJwtException;
import org.hibernate.StaleObjectStateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;
import java.util.Arrays;
import java.util.HashMap;

@ControllerAdvice
public class ExceptionsHandling {



    //------------------------------------------------------------------------------------------------------------>
    /***
     *
     * @param e
     * @return ResponseEntity,  BAD_REQUEST
     * @description: handles the exceptions which will be raised due to constraints on fields of the entity
     * when only try to save it in database
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity cons(ConstraintViolationException e) {
        int[] i =new int[]{1};
        CommonResponseVO common_responseVO = new CommonResponseVO();
        common_responseVO.setName(e.getClass().getSimpleName());

        HashMap<Integer, String> error_reasons = new HashMap<Integer, String>();


        e.getConstraintViolations().forEach(ex -> {

            error_reasons.put(i[0]++, ex.getMessage());

        });


        common_responseVO.setMessage(error_reasons);
        ResponseEntity responseEntity = new ResponseEntity(common_responseVO, HttpStatus.BAD_REQUEST);

        return responseEntity;
    }





    //------------------------------------------------------------------------------------------------------------>
    /***
     *
     * @param e
     * @return  BAD_REQUEST
     * @description: handle the exceptions which are raised due to @valid in method argument
     * which is checking for constraints that are applied to field of class in which data is being receiving in
     * argument
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class )
    public ResponseEntity cons(MethodArgumentNotValidException e) {

        CommonResponseVO common_responseVO = new CommonResponseVO();
        common_responseVO.setName(e.getClass().getSimpleName());
        HashMap<Integer, String> error_reasons = new HashMap<Integer, String>();

        error_reasons.put(0, StatusCode.FIELD_MISSING.toString());
        common_responseVO.setMessage(error_reasons);
        return new ResponseEntity(common_responseVO, HttpStatus.BAD_REQUEST);
    }





    //------------------------------------------------------------------------------------------------------------>
    @ExceptionHandler(value = UserAlreadyRegisteredException.class)
    public ResponseEntity userAlreadyRegisteredExceptionHandling(UserAlreadyRegisteredException e){
        return new ResponseEntity(e.getErrorResponse(),e.getHttpStatus());
    }





    //------------------------------------------------------------------------------------------------------------>
    @ExceptionHandler(value = FieldMissingException.class)
    public ResponseEntity FieldMissingExceptionHandling(FieldMissingException e){
        return new ResponseEntity(e.getErrorResponse(),e.getHttpStatus());
    }





    //------------------------------------------------------------------------------------------------------------>
    @ExceptionHandler(value = UserNotRegisteredException.class)
    public ResponseEntity UserNotExistExceptionHandling(UserNotRegisteredException e){
        return new ResponseEntity(e.getErrorResponse(),e.getHttpStatus());
    }



    //------------------------------------------------------------------------------------------------------------>
    @ExceptionHandler(value = GiveMessageException.class)
    public ResponseEntity giveMsgException(GiveMessageException e){
        return new ResponseEntity(e.getErrorResponse(),e.getHttpStatus());
    }



    //------------------------------------------------------------------------------------------------------------>
    @ExceptionHandler(value = TokenExpiredException.class)
    public ResponseEntity tokenExpire(TokenExpiredException e){
        return new ResponseEntity(e.getErrorResponse(),e.getHttpStatus());
    }





    //------------------------------------------------------------------------------------------------------------->
    @ExceptionHandler(value = MalformedJwtException.class)
    public ResponseEntity malformedJwtToken(MalformedJwtException e){
        CommonResponseVO commonResponseVO = new CommonResponseVO(Arrays.asList(StatusCode.FAILED.toString())
                                                ,Arrays.asList(e.getLocalizedMessage().toString())
                                                     );
        return new ResponseEntity(commonResponseVO,HttpStatus.UNAUTHORIZED);
    }





    //------------------------------------------------------------------------------------------------------------>
    /*
     *when two or more people try to update record at same time...and 1st update the record..
     * that row version no will be updated and when 2nd person try to update it.. it will throw exception
     */
    @ExceptionHandler(value = StaleObjectStateException.class)
    public ResponseEntity lockingDBexception(StaleObjectStateException e){
        CommonResponseVO commonResponseVO = new CommonResponseVO(Arrays.asList(StatusCode.FAILED.toString())
                ,Arrays.asList(e.getLocalizedMessage().toString())
        );
        return new ResponseEntity(commonResponseVO,HttpStatus.UNAUTHORIZED);
    }



    @ExceptionHandler(value = UnexpectedTypeException.class)
    public ResponseEntity UnexpectedValidationException(UnexpectedTypeException e){
        CommonResponseVO commonResponseVO = new CommonResponseVO(Arrays.asList(StatusCode.FAILED.toString())
                ,Arrays.asList(e.getLocalizedMessage().toString())
        );
        return new ResponseEntity(commonResponseVO,HttpStatus.BAD_REQUEST);
    }
}
