
package com.egg.biblioteca.servicios;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorio.EditorialRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EditorialService {
    @Autowired
    EditorialRepositorio editorialRepositorio;
    @Transactional
    public void crearEditorial(String nombre) throws MiException {
        validar(nombre);
        Editorial editorial = new Editorial();
        editorial.setNombre(nombre);
        editorialRepositorio.save(editorial);
    }
    public List<Editorial> buscarEditoriales(){
        List<Editorial> editoriales = new ArrayList();
        editoriales = editorialRepositorio.findAll();
        return editoriales;
    }
    @Transactional
    public void modificarEditorial(String nombre,String id) throws MiException{
        validar(nombre);
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if(respuesta.isPresent()){
            Editorial editorial = respuesta.get();
            editorial.setNombre(nombre);
            editorial.setId(id);
            editorialRepositorio.save(editorial);
        }
    }
    private void validar(String nombre) throws MiException{
        if(nombre==null || nombre.isEmpty()){
            throw new MiException("El nombre de la editorial no puede ser nulo o estar vacio.");
        }
    }
    @Transactional
    public Editorial getOne(String id){
        return editorialRepositorio.getOne(id);
    }
}
