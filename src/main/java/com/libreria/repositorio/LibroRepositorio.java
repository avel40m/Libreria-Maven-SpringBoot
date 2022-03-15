package com.libreria.repositorio;

import com.libreria.entidades.Libro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, String> {
    @Query("SELECT l FROM Libro l WHERE l.id LIKE :id")
    public Libro buscarPorId(@Param("id") String id);
    
    @Query("SELECT l FROM Libro l WHERE l.isbn = :isbn")
    public Libro buscarPorISBN(@Param("isbn") Long isbn);
    
    @Query("SELECT l FROM Libro l WHERE l.titulo = :titulo")
    public Libro buscarPorNombre(@Param("titulo") String titulo);
    
    @Query("SELECT l FROM Libro l WHERE l.alta = true")
    public List<Libro> seleccionarLibroAlta();
}
