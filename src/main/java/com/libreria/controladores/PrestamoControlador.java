package com.libreria.controladores;

import com.libreria.servicios.ErroresServicios.ErrorServicios;
import com.libreria.servicios.PrestamoServicio;
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
public class PrestamoControlador {

    @Autowired
    private PrestamoServicio prestamoServicio;

    @GetMapping("/prestamo")
    public String listaPrestamo(Model m) {
        m.addAttribute("prestamos", prestamoServicio.listaPrestamos());
        return "/prestamo/index";
    }

    @GetMapping("/prestamo/crear")
    public String crearPrestamo(ModelMap m) {
        m.addAttribute("libros", prestamoServicio.listaDeLibro());
        m.addAttribute("clientes", prestamoServicio.listaDeCliente());
        return "/prestamo/crear";
    }

    @PostMapping("/prestamo/crear")
    public String crearPrestamo(@RequestParam String cliente, @RequestParam String libro, @RequestParam String fechaDevolucion, ModelMap m, HttpSession session) {
        try {
            prestamoServicio.crearPrestamo(cliente, libro, fechaDevolucion);
        } catch (ErrorServicios ex) {
            Logger.getLogger(PrestamoControlador.class.getName()).log(Level.SEVERE, null, ex);
            m.put("error", ex.getMessage());
            return "/prestamo/crear";
        }
        session.setAttribute("msg", "El libro fue prestado correctamente");
        return "redirect:/prestamo";
    }

    @GetMapping("/prestamo/devolucion/{id}")
    public String controlDevolucion(@PathVariable String id, HttpSession session) {
        try {
            prestamoServicio.devulucionLibro(id);
        } catch (ErrorServicios ex) {
            Logger.getLogger(PrestamoControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        session.setAttribute("msg", "El libro fue devuelto");
        return "redirect:/prestamo";
    }

    @GetMapping("/prestamo/eliminar/{id}")
    public String eliminarPrestamo(@PathVariable String id, HttpSession session) {
        try {
            prestamoServicio.eliminarPrestamo(id);
        } catch (ErrorServicios ex) {
            Logger.getLogger(PrestamoControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        session.setAttribute("msg", "El prestamo se elimino");
        return "redirect:/prestamo";
    }
}
