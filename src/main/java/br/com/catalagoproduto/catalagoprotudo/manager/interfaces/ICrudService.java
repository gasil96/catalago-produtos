package br.com.catalagoproduto.catalagoprotudo.manager.interfaces;

import java.util.List;
import java.util.Optional;

public interface ICrudService<Z> {

    Z save(Z genericClass);

    Z update(Z genericClass, Long id);

    Optional<Z> findById(Long id);

    List<Z> findAll();

    void deleById(Long id);

}
