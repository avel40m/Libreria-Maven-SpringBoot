package com.libreria.repositorio;

import com.libreria.entidades.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepositorio extends JpaRepository<Prestamo, String>{
    
    @Query("SELECT p FROM Prestamo p WHERE p.cliente.id = :clienteid AND p.libro.id = :libroid")
    public Prestamo controlarPrestamo(@Param("clienteid") String clienteid,@Param("libroid") String libroid);
}
