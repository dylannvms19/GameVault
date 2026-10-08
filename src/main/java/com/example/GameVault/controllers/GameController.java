package com.example.GameVault.controllers;

import com.example.GameVault.models.Juego;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class GameController {

    // "Base de datos" simulada en memoria
    private static List<Juego> juegosDb = new ArrayList<>();
    private static long idCounter = 1;

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    static {
        juegosDb.add(new Juego(idCounter++, "The Legend of Zelda", "Aventura épica.", "zelda.jpg"));
    }

    // Página independiente para enseñar las notaciones de fragmentos
    @GetMapping("/fragments-demo")
    public String fragmentsDemo() {
        return "fragments-demo";
    }

    // Catálogo (usa el Layout maestro)
    @GetMapping({"/", "/juegos"})
    public String listarJuegos(Model model) {
        model.addAttribute("juegos", juegosDb);
        return "juegos";
    }

    // Formulario de subida
    @GetMapping("/juegos/nuevo")
    public String mostrarFormulario() {
        return "formulario";
    }

    // Lógica de guardado del archivo
    @PostMapping("/juegos")
    public String guardarJuego(@RequestParam("titulo") String titulo,
                               @RequestParam("descripcion") String descripcion,
                               @RequestParam("portada") MultipartFile portada) {

        String nombreArchivo = "default.png"; // Imagen por defecto si no suben nada

        // Verificamos si el archivo no está vacío
        if (!portada.isEmpty()) {
            try {
                // Crear la carpeta si no existe
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Nombre único para evitar sobrescribir archivos con el mismo nombre
                // (getFileName() evita rutas tipo ../../ en el nombre original)
                String original = Paths.get(portada.getOriginalFilename()).getFileName().toString();
                nombreArchivo = UUID.randomUUID().toString() + "_" + original;
                Path filePath = uploadPath.resolve(nombreArchivo);

                // Guardamos el archivo físicamente en disco
                Files.copy(portada.getInputStream(), filePath);

                System.out.println("Archivo guardado en: " + filePath.toAbsolutePath());

            } catch (IOException e) {
                e.printStackTrace();
                // En un proyecto real, manejaríamos esta excepción apropiadamente
            }
        }

        // Guardamos el juego en nuestra "base de datos"
        Juego nuevoJuego = new Juego(idCounter++, titulo, descripcion, nombreArchivo);
        juegosDb.add(nuevoJuego);

        // Patrón PRG (Post/Redirect/Get)
        return "redirect:/juegos";
    }
}
