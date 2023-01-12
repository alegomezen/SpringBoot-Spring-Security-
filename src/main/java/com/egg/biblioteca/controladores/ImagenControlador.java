/*
 
 */
package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Usuario;
import com.egg.biblioteca.servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
Necesitamos que el navegador obtenga el contenido en bytes para interpretar el arreglo. 
El metodo nos devuelve la imagen de perfil que esta vinculada al usuario 
* En lugar de retornar un HTML retornamos una ResponseEntity de bytes que es la imagen del usuario 
 */
@Controller 
@RequestMapping("/imagen")
public class ImagenControlador {
    @Autowired
    UsuarioService usuarioServicio;
    @GetMapping("/perfil/{id}")
    public ResponseEntity<byte[]> imagenUsuario(@PathVariable String id){
        Usuario usuario = usuarioServicio.getOne(id);
        byte[] imagen = usuario.getImagen().getContenido(); //Necesitamos que el navegador descargue esto 
        HttpHeaders headers = new HttpHeaders(); //Lo que hacen las cabeceras es decirle al navegador que lo que estamos devolviendo es una imagen
        headers.setContentType(MediaType.IMAGE_JPEG); //Seteamos el tipo de contenido que recibira 
        
        return new ResponseEntity<>(imagen,headers,HttpStatus.OK); //Recibe la imagen en bytes, los headers y estadoHTTP
    }
}
