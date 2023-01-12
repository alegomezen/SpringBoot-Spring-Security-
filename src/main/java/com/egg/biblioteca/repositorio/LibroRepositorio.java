package com.egg.biblioteca.repositorio;

import com.egg.biblioteca.entidades.Libro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/*
Vemos algo nuevo. Una anotación llamada Repository -> los repositorios van a hacer de nexo entre nuestras
entidades y la base de datos a la que estemos conectados. 
Los repositorios son interfaces que se encargan de crear, buscar, modificar o eliminar objetos de nuestro dominio
de la base de datos.
Creamos una interfaz llamada LibroRepositorio que se extiende o, hereda, de la JpaRepository.
En este caso debemos señalar que esta JpaRepository va a manejar la entidad Libro, cuya clave primaria
Es del tipo Long.
Con el repositorio correctamente armado podemos usar los metodos intrinsecos brindados por JpaRepository. 

 */
@Repository
public interface LibroRepositorio extends JpaRepository<Libro, Long> {

    //Si bien podemos utilizar uno de los metodos precargados, veamos cómo sería crear una consulta para 
    //buscar un libro por un atributo que no es su primary key. 
    //Debemos generar la anotacion Query y escribir la consulta SQL correspondiente. Lo importante es 
    //agregar la anotacion Param para indicar que es un atributo del libro denominado de esa manera.
    //Entonces, el primer "titulo" hace referencia a lo anterior y, el segundo, al que pasamos para la busqueda
     @Query("SELECT l FROM Libro l WHERE l.titulo = :titulo")
     public Libro buscarPorLibro(@Param("titulo") String titulo);
    //Analogamente, creamos un metodo para buscar los libros que sean de un determinado autor. 
    //Como pueden haber numerosos libros de un solo autor, declaramos que debe devolver una lista de Libros
    @Query("SELECT l FROM Libro l WHERE l.autor.nombre = :nombre")
    public List<Libro> buscarPorAutor(@Param("nombre") String nombre);
    
    
}
