package hos.srm.srmbusiness.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import hos.srm.srmbusiness.model.BaseResponse;
@RestControllerAdvice

public class GlobalExceptionHandler {
    @ExceptionHandler(ErrorException.class)

    public BaseResponse handleErrorException(ErrorException e){
        return BaseResponse.error(e.getCode(),e.getMsg());
    }
}
