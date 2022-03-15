package com.libreria.servicios;

import com.libreria.repositorio.AutorRepositorio;
import java.util.List;
import org.springframework.stereotype.Service;
import com.libreria.entidades.Autor;
import com.libreria.servicios.ErroresServicios.ErrorServicios;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class AutorServicio {
    @Autowired
    private AutorRepositorio autorRepositorio;
    
    public List<Autor> listaAutores(){
        return autorRepositorio.findAll();
    }
    
    @Transactional
    public void crearAutor(String nombre) throws ErrorServicios{  
        
        validar(nombre);
        
        Autor autor = new Autor();
        autor.setNombre(nombre);
        autor.setAlta(true);
        
        autorRepositorio.save(autor);
    }
    
    @Transactional
    public void modificarAutor(String id, String nombre) throws ErrorServicios{
        validar(nombre);
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setNombre(nombre);
            autorRepositorio.save(autor);
        } else {
            throw new ErrorServicios("El id no de encontro en nuestra base de datos");
        }
        
    }
    
    @Transactional
    public Autor buscarAutor(String id) throws ErrorServicios{
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        
        if (respuesta.isPresent()) {
            Autor a =respuesta.get();
            return a;
        } else {
            throw new ErrorServicios("No se encontro el id");
        }
        
    }
    
    @Transactional
    public void modificarAltaLibro(String id) throws ErrorServicios{
        Optional<Autor> resultado = autorRepositorio.findById(id);
        
        if (resultado.isPresent()) {
            Autor autor = resultado.get();
            
            if (autor.getAlta() == true) {
                autor.setAlta(Boolean.FALSE);
            } else {
                autor.setAlta(Boolean.TRUE);
            }
            autorRepositorio.save(autor);
        }else{
            throw new ErrorServicios("No se encontro el id");
        }
        
    }
    
    private void validar(String nombre) throws ErrorServicios{
        if ("".equals(nombre) || nombre.isEmpty()) {
            throw new ErrorServicios("El nombre no puede quedar vacio");
        }        
        Autor autor = buscarPorNombre(nombre);
        if (autor != null) {
            throw new ErrorServicios("El nombre " + nombre + " ya existe en la base de datos");
        }
    }
    
    private Autor buscarPorNombre(String nombre){
        return autorRepositorio.buscarPorNombre(nombre);
    }
}
