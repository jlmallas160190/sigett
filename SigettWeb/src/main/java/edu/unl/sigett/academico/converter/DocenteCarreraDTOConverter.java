/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.academico.converter;

import edu.unl.sigett.academico.dto.DocenteCarreraDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author jorge-luis
 */
@FacesConverter("docenteCarreraDTOConverter")
public class DocenteCarreraDTOConverter implements Converter {

    private static List<DocenteCarreraDTO> docenteCarreraDTOs;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value.trim().equals("")) {
            return null;
        } else {
            if (DocenteCarreraDTOConverter.docenteCarreraDTOs != null) {
                for (DocenteCarreraDTO docenteCarreraDTO : DocenteCarreraDTOConverter.docenteCarreraDTOs) {
                    if (docenteCarreraDTO.getDocenteCarrera().getId() == Long.parseLong(value)) {
                        return docenteCarreraDTO;
                    }
                }
            }
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value==null){
            return "";
        }
        if (((DocenteCarreraDTO) value).getDocenteCarrera()!=null) {
            return ((DocenteCarreraDTO) value).getDocenteCarrera().getId().toString();
        }
        return "";
    }

    public static List<DocenteCarreraDTO> getDocenteCarreraDTOs() {
        return docenteCarreraDTOs;
    }

    public static void setDocenteCarreraDTOs(List<DocenteCarreraDTO> docenteCarreraDTOs) {
        DocenteCarreraDTOConverter.docenteCarreraDTOs = docenteCarreraDTOs;
    }

}
