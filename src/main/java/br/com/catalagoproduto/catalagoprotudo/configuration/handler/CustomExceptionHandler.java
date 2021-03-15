package br.com.catalagoproduto.catalagoprotudo.configuration.handler;

import br.com.catalagoproduto.catalagoprotudo.configuration.exceptions.ProducNotFoundException;
import br.com.catalagoproduto.catalagoprotudo.configuration.exceptions.UnssuportedValueMinMaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(ProducNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected void produtctNotFoundHandler(ProducNotFoundException ex) {
        log.error(ex.getMessage());
        /*
          No body response
          */
    }

    @ResponseBody
    @ExceptionHandler(UnssuportedValueMinMaxException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ApiError unssuporteValueMinMaxException(UnssuportedValueMinMaxException ex) {
        log.error(ex.getMessage());
        return new ApiError(409, ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ApiError unssuporteValueMinMaxException(NumberFormatException ex) {
        log.error(ex.getMessage());
        return new ApiError(409, ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ex.getMessage());
        List<String> details = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        return new ResponseEntity<>(new ApiError(400, details.toString()), HttpStatus.BAD_REQUEST);
    }

}
