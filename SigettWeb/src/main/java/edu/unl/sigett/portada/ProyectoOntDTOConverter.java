/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.portada;

import edu.unl.sigett.webSemantica.dto.ProyectoOntDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author jorge-luis
 */
@FacesConverter("proyectoOntDTOConverter")
public class ProyectoOntDTOConverter implements Converter {

    private static List<ProyectoOntDTO> proyectoOntDTOs;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value.trim().equals("")) {
            return null;
        }
        if (ProyectoOntDTOConverter.proyectoOntDTOs != null) {
            for (ProyectoOntDTO proyectoOntDTO : ProyectoOntDTOConverter.proyectoOntDTOs) {
                if (proyectoOntDTO.getId() == Long.parseLong(value)) {
                    return proyectoOntDTO;
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
        if (((ProyectoOntDTO) value) != null) {
            return ((ProyectoOntDTO) value).getId().toString();
        }
        return "";
    }

    public static List<ProyectoOntDTO> getProyectoOntDTOs() {
        return proyectoOntDTOs;
    }

    public static void setProyectoOntDTOs(List<ProyectoOntDTO> proyectoOntDTOs) {
        ProyectoOntDTOConverter.proyectoOntDTOs = proyectoOntDTOs;
    }

}
