/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.proyecto;

import edu.unl.sigett.entity.Proyecto;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author jorge-luis
 */
@FacesConverter("proyectoDTOConverter")
public class ProyectoConverter implements Converter {

    private static List<Proyecto> proyectos;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value.trim().equals("")) {
            return null;
        }
        if (ProyectoConverter.proyectos != null) {
            for (Proyecto proyecto : ProyectoConverter.proyectos) {
                if (proyecto.getId() == Long.parseLong(value)) {
                    return proyecto;
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (((Proyecto) value) != null) {
            return ((Proyecto) value).getId().toString();
        }
        return "";
    }

    public static List<Proyecto> getProyectos() {
        return proyectos;
    }

    public static void setProyectos(List<Proyecto> proyectos) {
        ProyectoConverter.proyectos = proyectos;
    }

}
