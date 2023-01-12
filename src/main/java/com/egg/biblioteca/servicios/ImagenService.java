/*
Como primera medida crearemos un metodo que nos permia guardar una imagen. Este metodo recibe como parametro
un MultipartFile -> este tipo de dato es el tipo de archivo en el que se almacena la imagen.
Dentro del metodo validamos que el archivo no sea nulo, instanciamos una Imagen vacía y, con los metodos
del MulipartFile seteamos los atributos de la Imagen.
Ademas, añadimos un metodo para actualizar la imagen en caso de que así se requiera. 
Este será similar al de guardar, con la diferencia de que añadimos un objeto Optional que guardará el contenido
que nos trae el repositorio medinte el Id y luego setea los atributos a la imagen. 
 */
package com.egg.biblioteca.servicios;
import com.egg.biblioteca.entidades.Imagen;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorio.ImagenRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 */
@Service
public class ImagenService {

    @Autowired
    ImagenRepositorio imagenRepositorio;

    public Imagen guardar(MultipartFile archivo) throws MiException{
        if (archivo != null) {
            try {
                Imagen imagen = new Imagen();
                imagen.setMime(archivo.getContentType()); //Con este metodo del dato MultipartFile seteamos el tipo de dato de la imagen
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());
                return imagenRepositorio.save(imagen);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null; //Si todo sale bien, se persistirá la imagen en el repositorio y, si no, retornará 
        //una entidad nula. 
    }
    public Imagen actualizar(MultipartFile archivo, String idImagen) throws MiException{
        if (archivo != null) {
            try {
                Imagen imagen = new Imagen();
                if(idImagen!=null){
                   Optional<Imagen> respuesta = imagenRepositorio.findById(idImagen);
                   if(respuesta.isPresent()){
                       imagen = respuesta.get();
                   }
                }
                imagen.setMime(archivo.getContentType()); 
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());
                return imagenRepositorio.save(imagen);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
        
    }
    
}
