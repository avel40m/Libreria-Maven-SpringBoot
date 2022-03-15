package com.libreria.servicios;

import com.libreria.entidades.Cliente;
import com.libreria.repositorio.ClienteRepositorio;
import com.libreria.servicios.ErroresServicios.ErrorServicios;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteServicio {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    public List<Cliente> listaCliente() {
        return clienteRepositorio.findAll();
    }

    @Transactional
    public void crearCliente(Cliente cliente) throws ErrorServicios {
        validar(cliente);

        cliente.setAlta(Boolean.TRUE);
        clienteRepositorio.save(cliente);
    }

    public Cliente buscarClientePorId(String id) throws ErrorServicios {
        Optional<Cliente> respuesta = clienteRepositorio.findById(id);

        if (respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw new ErrorServicios("No se pudo obtener el id");
        }
    }

    @Transactional
    public void editarCliente(Cliente cliente) throws ErrorServicios {
        validarEditarCliente(cliente);
        Optional<Cliente> respuesta = clienteRepositorio.findById(cliente.getId());
        if (respuesta.isPresent()) {
            cliente.setAlta(respuesta.get().getAlta());
            clienteRepositorio.save(cliente);
        } else {
            throw new ErrorServicios("El id no se encontro");
        }
    }

    @Transactional
    public void darAltaCliente(String id) throws ErrorServicios {
        Optional<Cliente> respuesta = clienteRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Cliente cliente = respuesta.get();
            if (cliente.getAlta() == true) {
                cliente.setAlta(Boolean.FALSE);
            } else {
                cliente.setAlta(Boolean.TRUE);
            }
            clienteRepositorio.save(cliente);
        } else {
        }
    }

    private void validar(Cliente cliente) throws ErrorServicios {
        if (cliente.getDocumento() == null || cliente.getDocumento().toString().length() != 8 || cliente.getDocumento().toString().isEmpty()) {
            throw new ErrorServicios("El documento no puede estar nulo  y debe tener 8 digitos");
        }
        if (clienteRepositorio.buscarPorDocumento(cliente.getDocumento()) != null) {
            throw new ErrorServicios("El D.N.I. " + cliente.getDocumento() + " está en la base de datos");
        }
        if (cliente.getNombre() == null || cliente.getNombre().isEmpty()) {
            throw new ErrorServicios("El nombre no puede estar nulo");
        }
        if (cliente.getApellido() == null || cliente.getApellido().isEmpty()) {
            throw new ErrorServicios("El apellido no puede estar nulo");
        }
        if (cliente.getTelefono() == null || cliente.getTelefono().length() < 6 || cliente.getTelefono().isEmpty()) {
            throw new ErrorServicios("El telefono no puede ser nulo y debe tener 6 caracteres como minimo");
        }
    }

    private void validarEditarCliente(Cliente cliente) throws ErrorServicios {
        if (cliente.getDocumento() == null || cliente.getDocumento().toString().length() != 8 || cliente.getDocumento().toString().isEmpty()) {
            throw new ErrorServicios("El documento no puede estar nulo  y debe tener 8 digitos");
        }
        if (cliente.getNombre() == null || cliente.getNombre().isEmpty()) {
            throw new ErrorServicios("El nombre no puede estar nulo");
        }
        if (cliente.getApellido() == null || cliente.getApellido().isEmpty()) {
            throw new ErrorServicios("El apellido no puede estar nulo");
        }
        if (cliente.getTelefono() == null || cliente.getTelefono().length() < 6 || cliente.getTelefono().isEmpty()) {
            throw new ErrorServicios("El telefono no puede ser nulo y debe tener 6 caracteres como minimo");
        }
    }
}
