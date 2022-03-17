package com.libreria.servicios;

import com.libreria.entidades.Cliente;
import com.libreria.entidades.Libro;
import com.libreria.entidades.Prestamo;
import com.libreria.repositorio.ClienteRepositorio;
import com.libreria.repositorio.LibroRepositorio;
import com.libreria.repositorio.PrestamoRepositorio;
import com.libreria.servicios.ErroresServicios.ErrorServicios;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrestamoServicio {

    @Autowired
    private PrestamoRepositorio prestamoRepositorio;
    @Autowired
    private LibroRepositorio libroRepositorio;
    @Autowired
    private ClienteRepositorio clienteRepositorio;

    public List<Prestamo> listaPrestamos() {
        return prestamoRepositorio.findAll();
    }

    public List<Libro> listaDeLibro() {
        return libroRepositorio.seleccionarLibroAlta();
    }

    public List<Cliente> listaDeCliente() {
        return clienteRepositorio.seleccionarClienteAlta();
    }

    @Transactional
    public void crearPrestamo(String cliente, String libro, String fechaDevolucion) throws ErrorServicios {

        validarPrestamo(cliente, libro, fechaDevolucion);
        prestamoLibro(libro);

        Prestamo prestamo = new Prestamo();
        prestamo.setFechaPrestamo(new Date());
        prestamo.setFechaDevolucion(convertirStringEnDate(fechaDevolucion));
        prestamo.setCliente(clienteRepositorio.buscarPorId(cliente));
        prestamo.setLibro(libroRepositorio.buscarPorId(libro));
        prestamo.setAlta(Boolean.TRUE);

        prestamoRepositorio.save(prestamo);

    }

    private Date convertirStringEnDate(String fecha) {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaDate = null;
        try {
            fechaDate = formato.parse(fecha);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return fechaDate;
    }

    private void validarPrestamo(String cliente, String libro, String fechaDevolucion) throws ErrorServicios {
        if ("".equals(fechaDevolucion) || fechaDevolucion.isEmpty()) {
            throw new ErrorServicios("La fecha de devolucion no podrá ser nula");
        }
        if (cliente.isEmpty()) {
            throw new ErrorServicios("Seleccione un cliente");
        }
        if (libro.isEmpty()) {
            throw new ErrorServicios("Seleccionar un libro");
        }
        if (prestamoRepositorio.controlarPrestamo(cliente, libro) != null) {
            throw new ErrorServicios("El cliente está figurado como prestado el libro");
        }
    }

    @Transactional
    private void prestamoLibro(String libro) throws ErrorServicios {
        Optional<Libro> respuesta = libroRepositorio.findById(libro);

        if (respuesta.isPresent()) {
            Libro l = respuesta.get();
            if (l.getEjemplares() < l.getEjemplaresPrestados()+ 1) {
                throw new ErrorServicios("El libro " + l.getTitulo() + " no tiene más ejemplares para prestar");
            }
            if ((l.getEjemplaresReastantes()- 1) < 0) {
                throw new ErrorServicios("El libro " + l.getTitulo() + " no tiene ejemplares para prestar");
            }
            l.setEjemplaresPrestados(l.getEjemplaresPrestados() + 1);
            l.setEjemplaresReastantes(l.getEjemplaresReastantes() - 1);

            libroRepositorio.save(l);

        } else {
            throw new ErrorServicios("El id del libro no se encontro");
        }
    }
    @Transactional
    public void devulucionLibro(String id) throws ErrorServicios{
        Optional<Prestamo> respuesta = prestamoRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Prestamo p = respuesta.get();
            descontarLibro(p.getLibro().getId());
            p.setAlta(Boolean.FALSE);
            prestamoRepositorio.save(p);
            
        }else{
            throw new ErrorServicios("No se encontro el id");
        }
     }
    
    private void descontarLibro(String id) throws ErrorServicios{
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro l = respuesta.get();
            l.setEjemplaresPrestados(l.getEjemplaresPrestados() - 1);
            l.setEjemplaresReastantes(l.getEjemplaresReastantes() + 1);
            libroRepositorio.save(l);
        } else {
            throw new ErrorServicios("No se encontro el id");
        }
    }
    @Transactional
    public void eliminarPrestamo(String id) throws ErrorServicios{
        Optional<Prestamo> respuesta = prestamoRepositorio.findById(id);
        if (respuesta.isPresent()) {
            prestamoRepositorio.deleteById(id);
        } else {
            throw new ErrorServicios("El id no se encontro");
        }
    }
    
    public List<Prestamo> seleccionarLibroPrestamoCliente(String id) throws ErrorServicios{
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        
        if (respuesta.isPresent()) {
            return prestamoRepositorio.seleccionarLibroPrestamoCliente(respuesta.get().getId());
        } else {
            throw new ErrorServicios("El id de libro es incorrecto");
        }
    }
}
