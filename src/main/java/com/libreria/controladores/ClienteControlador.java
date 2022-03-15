package com.libreria.controladores;

import com.libreria.entidades.Cliente;
import com.libreria.servicios.ClienteServicio;
import com.libreria.servicios.ErroresServicios.ErrorServicios;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ClienteControlador {

    @Autowired
    private ClienteServicio clienteServicio;

    @GetMapping("/cliente")
    public String listaClientes(ModelMap model) {
        model.addAttribute("clientes", clienteServicio.listaCliente());
        return "/cliente/index";
    }

    @GetMapping("/cliente/crear")
    public String crearCliente() {
        return "/cliente/crear";
    }

    @PostMapping("/cliente/crear")
    public String crearCliente(@ModelAttribute Cliente cliente, ModelMap model, HttpSession session) {
        try {
            clienteServicio.crearCliente(cliente);
        } catch (ErrorServicios ex) {
            Logger.getLogger(ClienteControlador.class.getName()).log(Level.SEVERE, null, ex);
            model.addAttribute("error", ex.getMessage());
            return "/cliente/crear";
        }
        session.setAttribute("msg", "El cliente " + cliente.getApellido() + " fue creado correctamente!!!");
        return "redirect:/cliente";
    }

    @GetMapping("/cliente/alta/{id}")
    public String darAltaCliente(@PathVariable String id) {
        try {
            clienteServicio.darAltaCliente(id);
        } catch (ErrorServicios ex) {
            Logger.getLogger(ClienteControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "redirect:/cliente";
    }

    @GetMapping("/cliente/editar/{id}")
    public String editarCliente(@PathVariable String id, ModelMap m) {
        try {
            m.addAttribute("cliente", clienteServicio.buscarClientePorId(id));
        } catch (ErrorServicios ex) {
            Logger.getLogger(ClienteControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "/cliente/editar";
    }

    @PostMapping("/cliente/editar")
    public String editarCliente(@ModelAttribute Cliente cliente, HttpSession session) {
        try {
            clienteServicio.editarCliente(cliente);
        } catch (ErrorServicios ex) {
            Logger.getLogger(ClienteControlador.class.getName()).log(Level.SEVERE, null, ex);
            session.setAttribute("msj", ex.getMessage());
            return "redirect:/cliente/editar/" + cliente.getId();
        }
        session.setAttribute("msg", "El cliente " + cliente.getApellido() + " fue modificado correctamente!!!");
        return "redirect:/cliente";
    }
}
