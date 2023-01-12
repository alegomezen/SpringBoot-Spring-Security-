/*
 En esta clase definiremos las funciones que podrán realizar los usuarios de tipo admin. 
 Como primera medida debemos aplicar seguridad a toda la clase, con el fin de que ningun usuario que no sea 
 un administrador no pueda siquiera ingresar a la url "/admin". Para ello nos movemos a la clase de seguridad
 y añadimos un nuevo antMatcher
 */
package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Usuario;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.servicios.UsuarioService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class AdminControlador {
    @Autowired UsuarioService usuarioServicio;
    @GetMapping("/dashboard") //Este nombre se usa normalmente en los paneles de administradores
    public String panelAdministrativo(){
        return "panel.html";
    }
    @GetMapping("/usuarios") 
    public String listar(ModelMap modelo){
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        modelo.addAttribute("usuarios",usuarios);
        return "usuario_list";
    }
    @GetMapping("/modificar/usuario/{id}")
    public String actualizar(MultipartFile archivo,@PathVariable String idUsuario, String nombre, String email, String password, String password2) throws MiException{
        usuarioServicio.actualizar(archivo, idUsuario, nombre, email, password, password2);
        return "redirect:/admin/usuarios";
    }
    @GetMapping("/modificarRol/{id}")
    public String cambiarRol(@PathVariable String id){
        usuarioServicio.cambiarRol(id);
        return "redirect:/admin/usuarios";
    }
}
