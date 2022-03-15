package com.libreria.servicios;

import com.libreria.entidades.Editorial;
import com.libreria.repositorio.EditorialRepositorio;
import com.libreria.servicios.ErroresServicios.ErrorServicios;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EditorialServicio {
    @Autowired
    private EditorialRepositorio editorialRepositorio;
    
    public List<Editorial> listarEditoriales(){
        return editorialRepositorio.findAll();
    }
    
    @Transactional
    public void crearEditorial(String nombre) throws ErrorServicios{
        validar(nombre);
        Editorial editorial = new Editorial();
        editorial.setNombre(nombre);
        editorial.setAlta(Boolean.TRUE);
        
        editorialRepositorio.save(editorial);
    }
    
    private void validar(String nombre) throws ErrorServicios{
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicios("El nombre de la editorial no puede ser vacio");
        }
        Editorial editorial = editorialRepositorio.buscarPorNombre(nombre);
        if (editorial != null) {
            throw new ErrorServicios("El nombre " + nombre + " existe en nuestra base");
        }
    }
    
    @Transactional
    public void darAltaEditorial(String id) throws ErrorServicios{
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            
            if (editorial.getAlta()) {
                editorial.setAlta(Boolean.FALSE);
            } else {
                editorial.setAlta(Boolean.TRUE);
            }
            editorialRepositorio.save(editorial);
        } else {
            throw new ErrorServicios("El id no fue encontrado");
        }
    }
    
    public void editarEditorial(String id,String nombre) throws ErrorServicios{
        validar(nombre);
        
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setNombre(nombre);
            editorialRepositorio.save(editorial);
        } else {
            throw new ErrorServicios("No se encontro el id");
        }
        
    }
    
    public Editorial buscarEditorialId(String id) throws ErrorServicios{
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw new ErrorServicios("El id no fue encontrado");
        }
    }
}
