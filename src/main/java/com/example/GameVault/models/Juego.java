package com.example.GameVault.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Genera Getters, Setters, toString, etc. (Gracias a Lombok)
@NoArgsConstructor
@AllArgsConstructor
public class Juego {

    private Long id;
    private String titulo;
    private String descripcion;

    // Almacenaremos el nombre del archivo o la URL de la imagen
    private String portadaUrl;
}

