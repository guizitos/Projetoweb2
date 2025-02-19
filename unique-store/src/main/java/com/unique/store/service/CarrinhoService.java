package com.unique.store.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unique.store.model.Carrinho;
import com.unique.store.model.Produto;
import com.unique.store.model.Usuario;
import com.unique.store.repository.CarrinhoRepository;

@Service
public class CarrinhoService {
    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProdutoService produtoService;

    public Carrinho adicionarProdutoAoCarrinho(Long produtoId, String email) {
        Usuario usuario = usuarioService.findByEmail(email);
        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        Carrinho carrinho = carrinhoRepository.findByUsuarioId(usuario.getId());
        if (carrinho == null) {
            carrinho = new Carrinho();
            carrinho.setUsuario(usuario);
        }

        Produto produto = produtoService.findById(produtoId);
        if (produto == null) {
            throw new RuntimeException("Produto não encontrado");
        }

        if (!carrinho.getProdutos().contains(produto)) {
            carrinho.getProdutos().add(produto);
        }

        return carrinhoRepository.save(carrinho);
    }

    public Carrinho removerProdutoDoCarrinho(Long produtoId, String email) {
        Usuario usuario = usuarioService.findByEmail(email);
        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        Carrinho carrinho = carrinhoRepository.findByUsuarioId(usuario.getId());
        if (carrinho == null) {
            throw new RuntimeException("Carrinho não encontrado");
        }

        Produto produto = produtoService.findById(produtoId);
        if (produto == null) {
            throw new RuntimeException("Produto não encontrado");
        }

        if (carrinho.getProdutos().contains(produto)) {
            carrinho.getProdutos().remove(produto);
        }

        return carrinhoRepository.save(carrinho);
    }

    public List<Produto> getItensCarrinho(String email) {
        Usuario usuario = usuarioService.findByEmail(email);
        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        Carrinho carrinho = carrinhoRepository.findByUsuarioId(usuario.getId());
        if (carrinho != null) {
            return carrinho.getProdutos();
        }
        return Collections.emptyList(); // Retorna uma lista vazia se o carrinho não existir
    }

    public void limparCarrinho(String email) {
        Usuario usuario = usuarioService.findByEmail(email);
        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        Carrinho carrinho = carrinhoRepository.findByUsuarioId(usuario.getId());
        if (carrinho != null) {
            carrinho.getProdutos().clear();
            carrinhoRepository.save(carrinho);
        }
    }
}
