/*
 
 */
package com.egg.biblioteca.controladores;

import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Este controlador implementa ErrorController que es una interfaz del framework
 * de Spring Esta vez trabajaremos de una manera distinta, declararemos el
 * RequestMapping a la altura del metodo y no de la clase. Estamos ordenando al
 * metodo que ingrese todo recurso que sea /error. Sea get o post. Primero
 * recuperamos el codigo de error que viene del servidor y, con base en eso,
 * establecemos un mensaje particular para ese codigo y redireccionamos o
 * abrimos una vista especifica. Un ModelAndView trabaja de manera similar a un
 * ModelMap pero nos devuelve un modelo y la vista.
 *
 * @author agome
 */
@Controller
public class ErroresControlador implements ErrorController {

    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {
        ModelAndView errorPage = new ModelAndView("error");
        String errorMsg = "";
        int httpErrorCode = getErrorCode(httpRequest);
        switch (httpErrorCode) {
            case 400: {
                errorMsg = "El recurso solicitado no existe.";
                break;
            }
            case 403: {
                errorMsg = "No tiene permisos para acceder a este recurso.";
                break;
            }
            case 401: {
                errorMsg = "No se encuentra autorizado.";
                break;
            }
            case 404: {
                errorMsg = "El recurso solicitado no fue encontrado.";
                break;
            }
            case 500: {
                errorMsg = "Ocurrió un error interno";
                break;
            }
        }
        errorPage.addObject("codigo", httpErrorCode);
        errorPage.addObject("mensaje", errorMsg);
        return errorPage;
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
    }

    public String getErrorPath() {
        return "/error.html";
    }
}
