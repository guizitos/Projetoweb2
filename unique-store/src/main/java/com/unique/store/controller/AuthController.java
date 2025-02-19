package com.unique.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.unique.store.model.Usuario;
import com.unique.store.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String fazerLogin(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        Usuario usuarioAutenticado = usuarioService.autenticar(username, password);
        if (usuarioAutenticado != null) {
            session.setAttribute("usuarioEmail", usuarioAutenticado.getEmail()); // Armazena o e-mail na sessão
            session.setAttribute("usuarioNome", usuarioAutenticado.getNome()); // Armazena o nome na sessão
            model.addAttribute("usuarioEmail", usuarioAutenticado.getEmail()); // Adiciona o e-mail ao modelo
            model.addAttribute("usuarioNome", usuarioAutenticado.getNome()); // Adiciona o nome ao modelo
            return "redirect:/"; // Redireciona para a página inicial após o login
        } else {
            model.addAttribute("error", "Email ou senha inválidos!");
            return "login"; // Retorna para a página de login com uma mensagem de erro
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalida a sessão atual
        return "redirect:/login"; // Redireciona para a página de login
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "register";
    }

    @PostMapping("/register")
    public String registrarUsuario(Usuario usuario) {
        usuarioService.registrarUsuario(usuario);
        return "redirect:/login";
    }

    @GetMapping("/homePage")
    public String homePage() {
        return "index";  
    }
}
