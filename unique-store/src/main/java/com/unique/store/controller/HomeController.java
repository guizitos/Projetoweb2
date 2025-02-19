package com.unique.store.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.unique.store.model.Produto;
import com.unique.store.service.ProdutoService;

@Controller
public class HomeController {
    @Autowired
    private ProdutoService produtoService;

    @GetMapping("/")
    public String home(Model model) {
        List<Produto> produtos = produtoService.findAll();
        model.addAttribute("produtos", produtos);
        return "index";
    }

    @GetMapping("/produtos")
    public String produtos(Model model) {
        List<Produto> produtos = produtoService.findAll();
        model.addAttribute("produtos", produtos);
        return "produtos";
    }
}
