package com.libreria.controladores;

import com.libreria.entidades.Autor;
import com.libreria.servicios.AutorServicio;
import com.libreria.servicios.ErroresServicios.ErrorServicios;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class AutorControlador {

    @Autowired
    private AutorServicio autorServicio;

    @GetMapping("/autor")
    public String listaDeAutores(Model m) {
        List<Autor> autor = autorServicio.listaAutores();
        m.addAttribute("autor", autor);
        System.out.println(autor);
        return "/autor/index";
    }

    @GetMapping("/autor/crear")
    public String crearAutor() {
        return "/autor/crear";
    }

    @PostMapping("/autor/crear")
    public String crearAutor(HttpSession session, ModelMap modelo, @RequestParam String nombre) {
        try {
            autorServicio.crearAutor(nombre);
        } catch (ErrorServicios ex) {
            modelo.put("error", ex.getMessage());
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);
            return "/autor/crear";
        }
        session.setAttribute("msg", "El autor " + nombre + " fue creado correctamente!!!");
        return "redirect:/autor";
    }

    @GetMapping("/autor/editar/{id}")
    public String editarAutor(@PathVariable String id, Model m) {
        try {
            Autor autor = autorServicio.buscarAutor(id);
            m.addAttribute("autor", autor);
        } catch (ErrorServicios ex) {
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
       return "/autor/editar";
    }

    @PostMapping("/autor/editar")
    public String editarAutor(@RequestParam String id,@RequestParam String nombre,HttpSession session){
        try {
            autorServicio.modificarAutor(id, nombre);
        } catch (ErrorServicios ex) {
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);
            session.setAttribute("msj", ex.getMessage());
            return "redirect:/autor/editar/"+ id;
        }
        session.setAttribute("msg", "El autor fue modificado correctamente con el nombre: " + nombre); 
        return "redirect:/autor";
    }

    @GetMapping("/autor/alta/{id}")
    public String modificarElAlta(@PathVariable String id) {
        try {
            autorServicio.modificarAltaLibro(id);
        } catch (ErrorServicios ex) {
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "redirect:/autor";
    }
}
