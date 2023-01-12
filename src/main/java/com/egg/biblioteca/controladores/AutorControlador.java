package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.servicios.AutorService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/autor") //localhost:8080/autor -> ingreso automaticamente al componente de tipo controlador
public class AutorControlador {

    @Autowired
    private AutorService autorServicio;

    @GetMapping("/registrar") //localhost:8080/autor/registrar
    public String registrar() {
        return "autor_form.html";
    }
    
    //El parametro que llega a este metodo se llama de la misma manera que
    //el del input en el form.
    //Nos encontramos con que si dejamos solamente el nombre da un error. Esto se debe a que el metodo
    //crearAutor contiene otro metodo que valida los datos y arroja una excepcion -> tratamos la excepcion
    //Si todo sale bien, retorna al index, si algo sale mal, retorna al formulario. 
    @PostMapping("/registro")      
    public String registro(@RequestParam String nombre, ModelMap modelo) {      
        try {
            autorServicio.crearAutor(nombre);
            modelo.put("exito","El autor fue cargado correctamente");
        } catch (MiException ex) {
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error","El autor no fue cargado correctamente");
            return "autor_form.html";
        }
        return "index.html";
    }
    
    @GetMapping("/lista")
    public String listar(ModelMap modelo){
        List<Autor> autores = autorServicio.buscarAutores();
        modelo.addAttribute("autores",autores);
        return "autor_list.html";
    }
    /*Creamos los metodos necesarios para modificar los datos desde nuestra pagina. Para que el metodo "sepa"
    //qué autor queremos modificar añadimos la anotacion PathVariable. Esta indica que el valor determinado
    //va a viajar a través de un path (fragmento de url en la que se encuentra determiado).
    Le estamos diciendo a Spring que, en este caso, la variable id es una variable de Path y viajará en esa url
    Lo que buscamos es que cuando seleccionemos la accion modificar, tengamos los datos de un autor determinado
    Creamos el metodo getOne para traer un autor determinado mediante su id y luego lo asignamos en el modelo
    con la llave "autor". 
*/ 
    
    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo){
        modelo.put("autor", autorServicio.getOne(id));
        return "autor_modificar.html";
    }
    
    /*
    @PostMapping("/modificar")
    public String modificar(@PathVariable(required = false) String id, String nombre, ModelMap modelo){
        try {
            autorServicio.modificarAutor(nombre, id);
            return "redirect:../lista";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "autor_modificar.html";
        }
    }
*/
    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, String nombre, ModelMap modelo){
        try {
            autorServicio.modificarAutor(nombre, id);
            return "redirect:../lista";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "autor_modificar.html";
        }
    }
    }    
 
