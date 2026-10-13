package com.example.GameVault.repository;

import com.example.GameVault.models.Juego;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JuegoRepository extends JpaRepository <Juego, Long> {
    //Metodos gratis: insert(save) update, delete, find

    List<Juego> findByTituloContainingIgnoreCase(String titulo);

    List<Juego> findByPrecioLessThan(Double precio);



}
