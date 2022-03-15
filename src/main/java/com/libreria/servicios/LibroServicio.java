package com.libreria.servicios;

import com.libreria.entidades.Libro;
import com.libreria.entidades.Autor;
import com.libreria.entidades.Editorial;
import com.libreria.repositorio.AutorRepositorio;
import com.libreria.repositorio.EditorialRepositorio;
import com.libreria.repositorio.LibroRepositorio;
import com.libreria.repositorio.PrestamoRepositorio;
import com.libreria.servicios.ErroresServicios.ErrorServicios;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LibroServicio {

    @Autowired
    private LibroRepositorio libroRepositorio;
    @Autowired
    private AutorRepositorio autorRepositorio;
    @Autowired
    private EditorialRepositorio editorialRepositorio;

    public List<Libro> listarLibro() {
        return libroRepositorio.findAll();
    }

    public List<Autor> traerAutoresAlta() {
        return autorRepositorio.seleccionarAutoresAlta();
    }

    public List<Editorial> traerEditorialAlta() {
        return editorialRepositorio.seleccionatEditorialAlta();
    }
    @Transactional
    public void crearAutor(Libro libro) throws ErrorServicios{
        validar(libro);
        libro.setEjemplaresPrestados(libro.getEjemplares());
        libro.setEjemplaresReastantes(0);
        libro.setAlta(Boolean.TRUE);
        
        libroRepositorio.save(libro);
    }
    @Transactional
    public void altaDeLibro(String id) throws ErrorServicios{
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            if (libro.getAlta() == true) {
                libro.setAlta(Boolean.FALSE);
            } else {
                libro.setAlta(Boolean.TRUE);
            }
            libroRepositorio.save(libro);
        } else {
            throw new ErrorServicios("No se encontro el id");
        }
    }
    @Transactional
    public void editarLibro(Libro libro) throws ErrorServicios{
        validar(libro);
        Optional<Libro> respuesta = libroRepositorio.findById(libro.getId());
        if (respuesta.isPresent()) {
            libro.setEjemplaresReastantes(libro.getEjemplares());
            libro.setEjemplaresPrestados(respuesta.get().getEjemplaresPrestados());
            libro.setAlta(respuesta.get().getAlta());            
            libroRepositorio.save(libro);
        } else {
            throw new ErrorServicios("El id no fue encontrado");
        }
    }
    
    public Libro buscarPorId(String id) throws ErrorServicios{
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw  new ErrorServicios("No se encontro el id");
        }
    }

    private void validar(Libro l) throws ErrorServicios {
        if(l.getIsbn() == null || l.getIsbn() < 0){
            throw new ErrorServicios("El ISBN no debe ser nulo y no debe ser negativo");
        }        
        if (libroRepositorio.buscarPorISBN(l.getIsbn()) != null) {
            throw new ErrorServicios("El numero " + l.getIsbn() + " existe en la base de datos");
        }
        if ("".equals(l.getTitulo()) || l.getTitulo() == null) {
            throw new ErrorServicios("El titulo no puede quedar vacio");
        }
        if (libroRepositorio.buscarPorNombre(l.getTitulo()) != null) {
            throw new ErrorServicios("El titulo del libro existe en la base de datos");
        }
        if (l.getEjemplares() == null || (l.getEjemplares().toString().length() > 2 && l.getEjemplares().toString().length() < 5)) {
            throw new ErrorServicios("El ejemplar no debe ser nulo y además los digitos deben estar entre 3 y 4");
        }
        if (l.getAutor() == null) {
            throw new ErrorServicios("Seleccionar un autor");
        }
        if (l.getEditorial() == null) {
            throw new ErrorServicios("Seleccionar una editorial");
        }
    }
    
}
