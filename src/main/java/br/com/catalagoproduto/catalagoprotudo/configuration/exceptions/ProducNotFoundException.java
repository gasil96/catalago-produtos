package br.com.catalagoproduto.catalagoprotudo.configuration.exceptions;

public class ProducNotFoundException extends RuntimeException {

    public ProducNotFoundException(Long id) {
        super("Could not find product " + id);
    }

}
