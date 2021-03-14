package br.com.catalagoproduto.catalagoprotudo.configuration.exceptions;

import java.math.BigDecimal;

public class UnssuportedValueMinMaxException extends RuntimeException {

    public UnssuportedValueMinMaxException(BigDecimal minValue, BigDecimal maxValue) {
        super("The minimum value(" + minValue + ") cannot be greater than the maximum value (" + maxValue + ")");
    }

}
