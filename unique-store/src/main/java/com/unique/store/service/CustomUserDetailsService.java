package com.unique.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.unique.store.model.Usuario;
import com.unique.store.repository.UsuarioRepository;
import java.util.Optional;

@Service
public class CustomUserDetailsService  {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
}
