package com.libreria.controladores;

import com.libreria.entidades.Libro;
import com.libreria.servicios.ErroresServicios.ErrorServicios;
import com.libreria.servicios.LibroServicio;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class LibroControlador {

    @Autowired
    private LibroServicio libroServicio;

    @GetMapping("/libro")
    public String listarLibros(Model m) {
        m.addAttribute("libros", libroServicio.listarLibro());
        return "/libro/index";
    }

    @GetMapping("/libro/crear")
    public String crearLibro(Model m) {
        m.addAttribute("autores", libroServicio.traerAutoresAlta());
        m.addAttribute("editoriales", libroServicio.traerEditorialAlta());
        return "/libro/crear";
    }

    @PostMapping("/libro/crear")
    public String crearLibro(@ModelAttribute Libro l, ModelMap model, HttpSession session) {
        try {
            libroServicio.crearAutor(l);
        } catch (ErrorServicios ex) {
            Logger.getLogger(LibroControlador.class.getName()).log(Level.SEVERE, null, ex);
            model.addAttribute("error", ex.getMessage());
            return "/libro/crear";
        }
        session.setAttribute("msg", "El libro fue creado correctamente!!!");
        return "redirect:/libro";
    }

    @GetMapping("/libro/editar/{id}")
    public String editarLibro(@PathVariable String id, Model m) {
        try {
            m.addAttribute("autores", libroServicio.traerAutoresAlta());
            m.addAttribute("editoriales", libroServicio.traerEditorialAlta());
            m.addAttribute("libro", libroServicio.buscarPorId(id));
        } catch (ErrorServicios ex) {
            Logger.getLogger(LibroControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "/libro/editar";
    }
    
    @PostMapping("/libro/editar")
    public String editarLibro(@ModelAttribute Libro libro, HttpSession session){
        try {
            libroServicio.editarLibro(libro);
        } catch (ErrorServicios ex) {
            Logger.getLogger(LibroControlador.class.getName()).log(Level.SEVERE, null, ex);
            session.setAttribute("msj", ex.getMessage());
            return "redirect:/libro/editar/" + libro.getId();
        }
        session.setAttribute("msg", "El libro " + libro.getTitulo() + " fue editado correcctamente");
        return "redirect:/libro";
    }

    @GetMapping("/libro/alta/{id}")
    public String altaLibro(@PathVariable String id) {
        try {
            libroServicio.altaDeLibro(id);
        } catch (ErrorServicios ex) {
            Logger.getLogger(LibroControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "redirect:/libro";
    }

    @GetMapping("/libro/detalles/{id}")
    public String verDetallesLibros(@PathVariable String id, Model m) {

        try {
            m.addAttribute("libro", libroServicio.buscarPorId(id));
        } catch (ErrorServicios ex) {
            Logger.getLogger(LibroControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "/libro/detalles";
    }

}
