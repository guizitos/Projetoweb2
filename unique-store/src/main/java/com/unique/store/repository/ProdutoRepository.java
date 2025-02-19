package com.unique.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unique.store.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
