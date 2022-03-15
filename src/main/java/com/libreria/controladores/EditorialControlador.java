package com.libreria.controladores;

import com.libreria.servicios.EditorialServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.libreria.entidades.Editorial;
import com.libreria.servicios.ErroresServicios.ErrorServicios;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class EditorialControlador {
    
    @Autowired
    private EditorialServicio editorialServicio;
    
    @GetMapping("/editorial")
    public String listarEditoriales(Model m){
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        m.addAttribute("editorial", editoriales);
        System.out.println(editoriales);
        return "/editorial/index";
    }
    
    
    @GetMapping("/editorial/crear")
    public String crearEditorial(){
        return "/editorial/crear";
    }
    
    @PostMapping("/editorial/crear")
    public String crearEditorial(@RequestParam String nombre,ModelMap map, HttpSession session){
        try {
            editorialServicio.crearEditorial(nombre);
        } catch (ErrorServicios ex) {
            Logger.getLogger(EditorialControlador.class.getName()).log(Level.SEVERE, null, ex);
            map.addAttribute("error",ex.getMessage());
            return "/editorial/crear";
        }
        session.setAttribute("msg","La editorial " + nombre + " fue creada correctamente");
        return "redirect:/editorial";
    }
    
    @GetMapping("/editorial/alta/{id}")
    public String darDeAlta(@PathVariable String id){
        try {
            editorialServicio.darAltaEditorial(id);
        } catch (ErrorServicios ex) {
            Logger.getLogger(EditorialControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "redirect:/editorial";
    }
    
    @GetMapping("/editorial/editar/{id}")
    public String editarEditorial(@PathVariable String id,Model m){
        try {
            Editorial e = editorialServicio.buscarEditorialId(id);
            m.addAttribute("editorial", e);
        } catch (ErrorServicios ex) {
            Logger.getLogger(EditorialControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "/editorial/editar";
    }
    
    @PostMapping("/editorial/editar")
    public String editarEditorial(@RequestParam String id,@RequestParam String nombre, HttpSession session){
        try {
            editorialServicio.editarEditorial(id, nombre);
        } catch (ErrorServicios ex) {
            Logger.getLogger(EditorialControlador.class.getName()).log(Level.SEVERE, null, ex);
            session.setAttribute("msj", ex.getMessage());
            return "redirect:/editorial/editar/" + id;
        }
        session.setAttribute("msg","La editorial fue modificada por el nombre: " + nombre);
        return "redirect:/editorial";
    }
}
