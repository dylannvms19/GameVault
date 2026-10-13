package com.example.GameVault.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Genera Getters, Setters, toString, etc. (Gracias a Lombok)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Juegos")
public class Juego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(length = 1000)
    private String descripcion;

    @Column(name = "portada")
    private String portadaUrl;

    private Double precio;
}

