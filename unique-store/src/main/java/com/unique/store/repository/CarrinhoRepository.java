package com.unique.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unique.store.model.Carrinho;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Long>{
    Carrinho findByUsuarioId(Long usuarioId);
}
