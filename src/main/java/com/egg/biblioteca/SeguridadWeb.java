/*
Comenzamos con el tema Seguridad Web. 

 */
package com.egg.biblioteca;

import com.egg.biblioteca.servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 Una vez añadidas las clases pertinentes, hacemos extender a la clase de la WebSecurityConfigurerAdapter
 * para utilizar sus metodos. Creamos uno del tipo void llamado configure, que recibe como parametro
 * un objeto del tipo HttpSecurity y, dentro del proceso, autorizamos los requerimientos para visualizar
 * cualquier tipo de archivo que queramos. En este caso los css, js, img, etc. Con el metodo permitAll
 * de esta manera, cualquier usuario, sea admin o una persona cualquiera, puede ver estos contenidos. 
 * @author agome
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SeguridadWeb extends WebSecurityConfigurerAdapter{
    @Autowired
    public UsuarioService usuarioServicio;
    //Con el siguiente metodo vamos a configurar el manejador de seguridad que tiene Spring Security
    //y vamos a decirle cual es el servicio que tiene que usar para autenticar un usuario
    //Una vez que el usuario sea autenticado le pasamos el encriptador de contraseñas 
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(usuarioServicio)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
    /*
    Primero teniamos solamente la autorizacion de visualizacion sin la necesidad de estar logeado hasta permitAll()
    Ahora vamos a añadir nuevos requerimentos en el metodo configure.
    Como no podemos inicializar un usuario como admin lo que hacemos es cambiarlo desde la base de datos. 
    */
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.
                authorizeHttpRequests()
                .antMatchers("/admin/*").hasRole("ADMIN") //De esta manera denegamos el acceso desde el comienzo de la clase AdminControlador para todo aquel que no sea admin
                .antMatchers("/css/*","/js/*","/img/*","/**").
                permitAll()
                .and().formLogin()
                      .loginPage("/login")                //Desde donde se registraran los usuarios 
                      .loginProcessingUrl("/logincheck")  //validacion del registro
                      .usernameParameter("email")         //en nuestro caso es el email
                      .passwordParameter("password")
                      .defaultSuccessUrl("/inicio")       //Si no se produce ningun error, por defecto, se va a dirigir a esta url
                      .permitAll()
                .and().logout()                           //Tambien vamos a configurar la salida de nuestro sistema
                              .logoutUrl("/logout")
                              .logoutSuccessUrl("/")      //Volvemos al index por lo que solo ponemos la barra 
                              .permitAll()
                .and().csrf()
                .disable();
     }
}
