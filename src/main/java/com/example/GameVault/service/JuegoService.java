package com.example.GameVault.service;

import com.example.GameVault.models.Juego;
import com.example.GameVault.repository.JuegoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class JuegoService {

    @Autowired
    private JuegoRepository juegoRepository;

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    public List<Juego> listarTodos() {
        return juegoRepository.findAll();
    }

    public List<Juego> buscarPorTitulo(String palabra) {
        if (palabra == null || palabra.trim().isEmpty()) {
            return listarTodos();
        }
        return juegoRepository.findByTituloContainingIgnoreCase(palabra);
    }

    public Juego guardarJuego(Juego juego, MultipartFile portada) {
        String nombreArchivo = "default.png"; // Imagen por defecto si no suben nada

        if (portada != null && !portada.isEmpty()) {
            try {
                // Crear la carpeta si no existe
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Nombre único para evitar sobrescribir archivos
                String original = Paths.get(portada.getOriginalFilename()).getFileName().toString();
                nombreArchivo = UUID.randomUUID().toString() + "_" + original;
                Path filePath = uploadPath.resolve(nombreArchivo);

                // Guardamos el archivo físicamente en disco
                Files.copy(portada.getInputStream(), filePath);

                System.out.println("Archivo guardado en: " + filePath.toAbsolutePath());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        juego.setPortadaUrl(nombreArchivo);
        return juegoRepository.save(juego);
    }
}
