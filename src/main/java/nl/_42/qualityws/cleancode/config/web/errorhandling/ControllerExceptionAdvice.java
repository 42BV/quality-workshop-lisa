package nl._42.qualityws.cleancode.config.web.errorhandling;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Advice for handling exceptions in controllers.
 */
@RestControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResult handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return handleBindingError(ex.getBindingResult());
    }
   
    @ExceptionHandler({ BindException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResult handleMethodArgumentNotValid(BindException ex) {
        return handleBindingError(ex.getBindingResult());
    }
    
    @ExceptionHandler({ Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleAllOther(Exception ex) {
        return ex.getMessage();
    }
    
    private ErrorResult handleBindingError(BindingResult result) {
        List<GlobalErrorBody> globalErrors = result.getGlobalErrors()
                .stream()
                .map(GlobalErrorBody::new)
                .collect(Collectors.toList());

        List<FieldErrorBody> fieldErrors = result.getFieldErrors()
                .stream()
                .map(FieldErrorBody::new)
                .sorted(Comparator.comparing(FieldErrorBody::getField))
                .collect(Collectors.toList());

        return new ErrorResult(globalErrors, fieldErrors);
    }

}