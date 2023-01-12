/*
 Los servicios son las clases que llevan adelante la logica del negocio. 
 Deben realizar las tareas que cumplan con las necesidades del cliente.
 A tener en cuenta -> los datos que debe ingresar el usuario para "crear" un libro se harán mediante un formulario
 Llegan por un formulario y lo que debemos hacer es transformarlos en una entidad de nuestro sistema 
 */
package com.egg.biblioteca.servicios;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.entidades.Libro;
import com.egg.biblioteca.repositorio.AutorRepositorio;
import com.egg.biblioteca.repositorio.EditorialRepositorio;
import com.egg.biblioteca.repositorio.LibroRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.egg.biblioteca.excepciones.MiException;

@Service
public class LibroService {

    @Autowired
    private LibroRepositorio libroRepositorio;
    //Con lo anterior le indicamos al servidor que esta variable debe ser inicializada por el, no por nosotros
    //Esto se conoce como INYECCION DE DEPENDENCIAS
    //De la misma manera podemos crear una para Autor y Editorial
    @Autowired
    private AutorRepositorio autorRepositorio;
    @Autowired
    private EditorialRepositorio editorialRepositorio;

    /*
     Lo que hacemos con esta anotacion es declarar que si se lleva a cabo el metodo
     sin lanzar excepciones se realizará un commit y se aplican los cambios. 
     si lanza una excepción se produce un rollback y no se aplican cambios en la Base de Datos
     Todos aquellos metodos que realicen un cambio en la base de datos deben anotarse como Transactional. 
     En cambio, los que realizan busquedas o consultan no la alteran, por lo que no es necesario anotarlos.
     Agregamos las excepciones para evitar datos nulos o errores de tipo. Lo correcto es crear nuestras excepciones
     para diferenciar los errores de sistema de los de la logica de negocio. 
     */
    @Transactional
    public void crearLibro(Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial) throws MiException {
        validar(isbn, titulo, idAutor, idEditorial, ejemplares);
        Autor autor = autorRepositorio.findById(idAutor).get();
        Editorial editorial = editorialRepositorio.findById(idEditorial).get();
        //Usamos un metodo propio de JpaRepositorio, creamos un autor y será igual a lo que me retorne
        //el llamado al repositorio. El metodo get devuelve el autor correspondiente a ese id 
        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setEjemplares(ejemplares);
        libro.setAlta(new Date());
        libro.setAutor(autor); //Le seteamos el autor que encontro el metodo de arriba 
        libro.setEditorial(editorial);
        libroRepositorio.save(libro); //llamamos a un metodo de la JPArepository llamado save, que recibe como
        //parametro a una entidad, en nuestro caso, el libro.                                     
    }

    public List<Libro> buscarLibros() {
        List<Libro> libros = new ArrayList();
        libros = libroRepositorio.findAll();
        return libros;
    }

    /*
    Optional es un objeto contenedor que puede o no contener un valor No nulo. Si el valor 
    Esta presente devuelve true, sino, false. 
    Lo usamos para verificar si los id estan presentes en alguno de los objetos, ya que, puede ocurrir que
    no exista alguno con esos atributos 
    Lo que hacemos es verificar si existen esos objetos y, si es así, rellenamos los objetos vacios con 
    el contenido de los Optional y luego los pasamos a los setter del libro a modificar. 
    Notar que el alta del libro no se modifica, ocurrió en una fecha y queda así. 
     */
    public void modificarLibro(Long isbn, String titulo, String idAutor, String idEditorial, Integer ejemplares) throws MiException {
        validar(isbn, titulo, idAutor, idEditorial, ejemplares);
        Optional<Libro> respuesta = libroRepositorio.findById(isbn);
        Optional<Autor> respuestaAutor = autorRepositorio.findById(idAutor);
        Optional<Editorial> respuestaEditorial = editorialRepositorio.findById(idEditorial);
        Autor autor = new Autor();
        Editorial editorial = new Editorial();
        if (respuestaAutor.isPresent()) {
            autor = respuestaAutor.get();
        }
        if (respuestaEditorial.isPresent()) {
            editorial = respuestaEditorial.get();
        }
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setTitulo(titulo);
            libro.setAutor(autor);
            libro.setEditorial(editorial);
            libro.setEjemplares(ejemplares);
            libroRepositorio.save(libro);
        }
    }

    private void validar(Long isbn, String titulo, String idAutor, String idEditorial, Integer ejemplares) throws MiException {
        if (isbn == null) {
            throw new MiException("El isbn no puede ser nulo.");
        }
        if (titulo.isEmpty() || titulo == null) {
            throw new MiException("El titulo del libro no puede ser nulo o esyar vacio.");
        }
        if (ejemplares == 0 || ejemplares == null) {
            throw new MiException("No pueden haber 0 ejemplares de un libro");
        }
        if (idAutor.isEmpty() || idAutor == null) {
            throw new MiException("El id del autor no puede ser nulo o estar vacio");
        }
        if (idEditorial.isEmpty() || idEditorial == null) {
            throw new MiException("El id de la editorial no puede ser nulo o estar vacio");
        }
    }
}
