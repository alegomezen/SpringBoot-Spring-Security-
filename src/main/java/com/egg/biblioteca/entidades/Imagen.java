/*
 Vamos a desarrollar la entidad Imagen para poder guardar las fotos de perfil de nuestros usuarios. 
 El atributo mime es un formato String que asigna el formato del archivo de la imagen. 
 Generamos un arreglo de bytes que será la forma en que se guardará la imagen. Agregamos la anotacion 
 @Lob para informarle a Spring que este archivo puede ser pesado y manejar muchos bytes. Ademas añanimos
 la anotacion @Basic para definir que el tipo de carga será "perezosa" o "lazy". Esto es con el fin de
 que este arreglo de bytes se cargue solo cuando lo pidamos, haciendo que las querys sean mas livianas.
 Solo se cargará si hacemos un get. 
 */
package com.egg.biblioteca.entidades;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import org.hibernate.annotations.GenericGenerator;

/**
 
 */
@Entity
public class Imagen {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid", strategy="uuid2")
    private String id;
    private String mime;
    private String nombre;
    @Lob @Basic(fetch = FetchType.LAZY)
    private byte[] contenido; 

    public Imagen() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public byte[] getContenido() {
        return contenido;
    }

    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }
    
}
