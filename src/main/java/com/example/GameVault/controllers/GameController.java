package com.example.GameVault.controllers;

import com.example.GameVault.models.Juego;
import com.example.GameVault.models.Usuario;
import com.example.GameVault.service.JuegoService;
import com.example.GameVault.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class GameController {

    @Autowired
    private JuegoService juegoService;

    @Autowired
    private UsuarioService usuarioService;

    // Página independiente para enseñar las notaciones de fragmentos
    @GetMapping("/fragments-demo")
    public String fragmentsDemo() {
        return "fragments-demo";
    }

    // Catálogo (usa el Layout maestro)
    @GetMapping({"/", "/juegos"})
    public String listarJuegos(@RequestParam(name = "buscar", required = false) String buscar,
                               Model model) {
        if (buscar != null && !buscar.isEmpty()) {
            model.addAttribute("juegos", juegoService.buscarPorTitulo(buscar));
            model.addAttribute("busquedaActual", buscar); // Para mantener el texto en la barra
        } else {
            model.addAttribute("juegos", juegoService.listarTodos());
        }
        return "juegos";
    }

    // Panel del usuario: solo los juegos de su bóveda
    @GetMapping("/mis-juegos")
    public String misJuegos(Model model) {
        Usuario logueado = usuarioService.obtenerUsuarioLogueado();
        model.addAttribute("usuario", logueado);
        model.addAttribute("juegos", juegoService.listarPorUsuario(logueado.getId()));
        return "mis-juegos";
    }

    // Formulario de subida
    @GetMapping("/juegos/nuevo")
    public String mostrarFormulario() {
        return "formulario";
    }

    // Guardado del juego
    @PostMapping("/juegos")
    public String guardarJuego(@RequestParam("titulo") String titulo,
                               @RequestParam("descripcion") String descripcion,
                               @RequestParam("precio") Double precio,
                               @RequestParam("categoria") String categoria,
                               @RequestParam("portada") MultipartFile portada) {

        Juego nuevoJuego = new Juego();
        nuevoJuego.setTitulo(titulo);
        nuevoJuego.setDescripcion(descripcion);
        nuevoJuego.setPrecio(precio);
        nuevoJuego.setCategoria(categoria);
        // El juego pertenece al usuario que lo sube
        nuevoJuego.setUsuario(usuarioService.obtenerUsuarioLogueado());

        juegoService.guardarJuego(nuevoJuego, portada);

        // Patrón PRG (Post/Redirect/Get)
        return "redirect:/mis-juegos";
    }

    // Vista de edición (reutiliza el formulario)
    @GetMapping("/juegos/editar/{id}")
    public String editarJuego(@PathVariable Long id, Model model) {
        Juego juego = juegoService.obtenerJuegoPorId(id);
        Usuario logueado = usuarioService.obtenerUsuarioLogueado();

        // Si el juego existe y el dueño coincide, mostramos el formulario
        if (juego != null && juego.getUsuario() != null
                && juego.getUsuario().getId().equals(logueado.getId())) {
            model.addAttribute("juego", juego);
            return "formulario";
        }

        // Si intenta editar un juego ajeno, lo devolvemos a su panel
        return "redirect:/mis-juegos";
    }

    // Procesa los cambios de edición
    @PostMapping("/juegos/editar/{id}")
    public String actualizarJuego(@PathVariable Long id,
                                  @RequestParam("titulo") String titulo,
                                  @RequestParam("descripcion") String descripcion,
                                  @RequestParam("precio") Double precio,
                                  @RequestParam("categoria") String categoria,
                                  @RequestParam(value = "portada", required = false) MultipartFile portada) {

        Juego juegoExistente = juegoService.obtenerJuegoPorId(id);
        Usuario logueado = usuarioService.obtenerUsuarioLogueado();

        // Verificamos propiedad nuevamente antes de modificar
        if (juegoExistente != null && juegoExistente.getUsuario() != null
                && juegoExistente.getUsuario().getId().equals(logueado.getId())) {
            juegoExistente.setTitulo(titulo);
            juegoExistente.setDescripcion(descripcion);
            juegoExistente.setPrecio(precio);
            juegoExistente.setCategoria(categoria);

            juegoService.guardarJuego(juegoExistente, portada);
        }

        return "redirect:/mis-juegos";
    }

    // Eliminar (POST por seguridad)
    @PostMapping("/juegos/eliminar/{id}")
    public String eliminarJuego(@PathVariable Long id) {
        Juego juego = juegoService.obtenerJuegoPorId(id);
        Usuario logueado = usuarioService.obtenerUsuarioLogueado();

        // Solo procedemos a borrar si el usuario logueado es el dueño
        if (juego != null && juego.getUsuario() != null
                && juego.getUsuario().getId().equals(logueado.getId())) {
            juegoService.eliminarJuego(id);
        }

        return "redirect:/mis-juegos";
    }
}