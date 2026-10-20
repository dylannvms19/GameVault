package com.example.GameVault.service;

import com.example.GameVault.models.Usuario;
import com.example.GameVault.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    UsuarioRepository usuarioRepository;

    // Simulamos al usuario "logueado": siempre es el usuario con ID 1
    public Usuario obtenerUsuarioLogueado(){
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(1L);

        if(usuarioOpt.isPresent()){
            return usuarioOpt.get();
        }else{
            Usuario usuario = new Usuario();
            usuario.setNombre("Itson");
            usuario.setEmail("itson@potros.itson.edu.mx");
            usuario.setContrasenia("221163");

            return usuarioRepository.save(usuario);
        }
    }
}