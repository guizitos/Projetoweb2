package com.unique.store.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.unique.store.model.Produto;
import com.unique.store.service.CarrinhoService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/carrinho")
public class CarrinhoController {
    
    @Autowired
    private CarrinhoService carrinhoService;

    // Método existente para exibir a página do carrinho
    @GetMapping
    public String carrinho(Model model, HttpSession session) {
        String email = (String) session.getAttribute("usuarioEmail"); // Obtém o e-mail da sessão
        if (email == null) {
            return "redirect:/login"; // Redireciona para login se não estiver logado
        }
        String usuarioNome = (String) session.getAttribute("usuarioNome"); // Obtém o nome do usuário da sessão
        model.addAttribute("usuarioNome", usuarioNome); // Adiciona o nome do usuário ao modelo
        model.addAttribute("itensCarrinho", carrinhoService.getItensCarrinho(email));
        return "carrinho"; 
    }

    @PostMapping("/adicionar/{produtoId}")
    @ResponseBody
    public ResponseEntity<?> adicionarProdutoAoCarrinho(@PathVariable Long produtoId, HttpSession session) {
        String email = (String) session.getAttribute("usuarioEmail");
        if (email == null) {
            return ResponseEntity.status(401).body("{\"success\": false, \"message\": \"Usuário não logado\"}");
        }
        carrinhoService.adicionarProdutoAoCarrinho(produtoId, email);
        return ResponseEntity.ok("{\"success\": true, \"message\": \"Produto adicionado ao carrinho\"}");
    }
    
    // Método existente para remover um produto do carrinho
    @PostMapping("/remover/{produtoId}")
    public String removerProdutoDoCarrinho(@PathVariable Long produtoId, HttpSession session) {
        String email = (String) session.getAttribute("usuarioEmail");
        if (email == null) {
            return "redirect:/login";
        }
        carrinhoService.removerProdutoDoCarrinho(produtoId, email);
        return "redirect:/carrinho";
    }

    @GetMapping("/finalizar-compra")
    public String finalizarCompra(Model model, HttpSession session) {
        String email = (String) session.getAttribute("usuarioEmail");
        if (email == null) {
            return "redirect:/login";
        }
        return "checkout"; // Retorna a página de checkout
    }

    @PostMapping("/finalizar-compra")
    public String confirmarCompra(@RequestParam String nome, @RequestParam String endereco, 
                                  @RequestParam String cartao, @RequestParam String validade, 
                                  @RequestParam String cvv, HttpSession session, RedirectAttributes redirectAttributes) {
        String email = (String) session.getAttribute("usuarioEmail");
        if (email == null) {
            return "redirect:/login";
        }

        carrinhoService.limparCarrinho(email);
        redirectAttributes.addFlashAttribute("mensagem", "Compra realizada com sucesso!");

        return "redirect:/";
    }

    @GetMapping("/api")
    @ResponseBody // Indica que o retorno será JSON
    public ResponseEntity<List<Produto>> getCarrinhoApi(HttpSession session) {
        String email = (String) session.getAttribute("usuarioEmail");
        if (email == null) {
            return ResponseEntity.status(401).build(); // Retorna 401 se o usuário não estiver logado
        }
        List<Produto> itensCarrinho = carrinhoService.getItensCarrinho(email);
        return ResponseEntity.ok(itensCarrinho); // Retorna os itens do carrinho em formato JSON
    }
}
