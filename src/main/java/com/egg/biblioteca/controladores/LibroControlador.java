package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.entidades.Libro;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.servicios.AutorService;
import com.egg.biblioteca.servicios.EditorialService;
import com.egg.biblioteca.servicios.LibroService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/libro")
public class LibroControlador {

    @Autowired
    private LibroService libroService;
    @Autowired
    private AutorService autorService;
    @Autowired
    private EditorialService editorialService;

    //Agregamos al registro la posibilidad de listar autores y editoriales. Con dos ModelMap devolvemos
    //las listas generadas por los metodos buscar y los añadimos a la llave String correspondiente. 
    @GetMapping("/registrar")
    public String registrar(ModelMap modelo) {
        List<Autor> autores = autorService.buscarAutores();
        List<Editorial> editoriales = editorialService.buscarEditoriales();
        modelo.addAttribute("autores", autores);
        modelo.addAttribute("editoriales", editoriales);
        return "libro_form.html";
    }

    /*
    En este caso, agregamos a los parametros numericos una sentencia required=fasle para que, en caso de 
    que sea nulo, de igual manera "ingrese" al controlador. Agregamos algo nuevo, un parametro de la clase
    ModelMap-> los modelos en Spring sirven para que insertemos en este modelo toda la informacion que queremos
    mostrar por pantalla o que queramos utilizar en la interfaz del usuario. En este caso lo utilizamos para
    inyectar el mensaje de error. El metodo put() de esta clase funciona con una llave valor.
    En caso de exito mostramos un mensaje y, en caso de errores, lo mostramos en la excepcion 
     */
    @PostMapping("/registro")
    public String registro(@RequestParam(required = false) Long isbn, @RequestParam String titulo,
            @RequestParam(required = false) Integer ejemplares, @RequestParam String idAutor,
            @RequestParam String idEditorial, ModelMap modelo) {
        try {
            libroService.crearLibro(isbn, titulo, ejemplares, idAutor, idEditorial);
            modelo.put("exito", "El libro fue cargado correctamente");
            System.out.println("asdadsdas");
        } catch (MiException ex) {
            List<Autor> autores = autorService.buscarAutores();
            List<Editorial> editoriales = editorialService.buscarEditoriales();
            modelo.addAttribute("autores", autores);
            modelo.addAttribute("editoriales", editoriales);
            modelo.put("error", ex.getMessage());            
            return "libro_form.html"; //volvemos a cargar el formulario. Agregamos lo que tenemos en 
                                      //  "/registrar" para que, en caso de errores, podamos volver a acceder
                                      // a los datos que tenemos cargados.
        }
        return "index.html";
    }
    @GetMapping("/lista")
    public String listar(ModelMap modelo){
        List<Libro> libros = libroService.buscarLibros();
        modelo.addAttribute("libros",libros);
        return "libro_list.html";
    }
}
