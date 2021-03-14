package br.com.catalagoproduto.catalagoprotudo.configuration.handler;

import br.com.catalagoproduto.catalagoprotudo.configuration.exceptions.ProducNotFoundException;
import br.com.catalagoproduto.catalagoprotudo.configuration.exceptions.UnssuportedValueMinMaxException;
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

    @ResponseBody
    @ExceptionHandler(ProducNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ApiError produtctNotFoundHandler(ProducNotFoundException ex) {
        return new ApiError(404, ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(UnssuportedValueMinMaxException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ApiError unssuporteValueMinMaxException(UnssuportedValueMinMaxException ex) {
        return new ApiError(409, ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ApiError unssuporteValueMinMaxException(NumberFormatException ex) {
        return new ApiError(409, ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> details = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        return new ResponseEntity<>(new ApiError(400, details.toString()), HttpStatus.BAD_REQUEST);
    }

}
