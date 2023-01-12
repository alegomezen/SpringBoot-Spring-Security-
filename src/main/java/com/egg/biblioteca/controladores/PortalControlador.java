/*
 Los controladores son los componentes del proyecto que conectan las interfaces de usuario en HTML con la capa
 de servicios que contiene la logica del negocio.
 Añadimos las anotaciones Controller y RequestMapping para avisarle al framework que es de tipo controller y 
 configurar la url que va a "escuchar" al controlador. En este caso indicamos que el controlador va a escuchar
 desde la barra "/". Cada vez que en el localhost pongamos "/ejemplo" el controlador se activa. 
 El metodo index se anota como GetMapping de HTTP que mapea la url cuando se ingresa la barra. 
 De esta manera configuramos que cuando se ingrese a la "url/" se ejecute el cuerpo del metodo. 
 */
package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Usuario;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.servicios.UsuarioService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    public UsuarioService usuarioServicio;

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/registrar")
    public String registrar() {
        return "registro.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, @RequestParam String email,
            @RequestParam String password, @RequestParam String password2, ModelMap modelo, MultipartFile archivo) {
        try {
            usuarioServicio.registrar(archivo, nombre, email, password, password2);
            modelo.put("exito", "Usuario registrado con exito!");
            return "index.html";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "registro.html";
        }
    }

    //Como el login puede o no generar un error vamos a agregar un RequestParam=false y recibirá como parametros
    //un String error y un ModelMap para mostrar un mensaje  
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "El usuario o la contraseña son invalidos.");
        }
        return "login.html";
    }

    /*
     Lo que hacemos con esta anotacion es veriricar que la persona que esta utilizando la pagina tenga
     un rol activo. De esta manera evitamos que los usuarios puedan acceder al inicio sin estar logueados
     Modificamos algunas cosas para hacer que el administrdor ingrese al dashboard directamente
     Agregamos un parametro HttpS y creamos un Usuario que contiene los datos de la sesion. 
     Esto da un error de tipo, por lo que lo casteamos. 
     Entonces, cada vez que un administrador se loguee, se lo redireccionara a la url correspondiente
     */
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session) {
        Usuario logeado = (Usuario) session.getAttribute("usuariosession");
        if (logeado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard"; //De esta manera redireccionamos al dashboard si el usuario es un administrador
        }
        return "inicio.html";
    }

    /*Este GetMapping recupera los datos del usuario a traves de la session. 
      Se crea un usuario y se le setean los datos del usuario logueado. 
      Con el modelo ponemos esos datos para que se autocomplete el formulario por si no se requiera cambiar 
      alguno. No se requiere poner el id ya que la session lo trae. 
     */
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/perfilM")
    public String perfilModificar(ModelMap modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.put("usuario", usuario);
        return "usuario_modificar.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/perfilM/{id}")
    public String actualizar(HttpSession session, @RequestParam("imagen") MultipartFile imagen, @PathVariable String id, @RequestParam String nombre, @RequestParam String email,
            @RequestParam String password, @RequestParam String password2, ModelMap modelo) {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            modelo.put("password", password);
            modelo.put("password2", password2);
            modelo.put("imagen", imagen);
            usuarioServicio.actualizar(imagen, usuario.getId(), nombre, email, password, password2);
            modelo.put("exito", "Usuario actualizado correctamente!");
            return "inicio.html";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            return "usuario_modificar.html";
        }
    }

}
