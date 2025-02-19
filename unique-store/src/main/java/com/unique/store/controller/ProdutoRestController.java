package com.unique.store.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unique.store.model.Produto;
import com.unique.store.service.ProdutoService;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoRestController {
     @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public List<Produto> listarProdutos() {
        return produtoService.findAll();
    }
}
