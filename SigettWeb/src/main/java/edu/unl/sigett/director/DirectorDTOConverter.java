/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.unl.sigett.director;

import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author jorge-luis
 */
@FacesConverter("directorDTOConverter")
public class DirectorDTOConverter implements Converter{
private static List<DirectorDTO> directoresDTO;
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
         if (value.trim().equals("")) {
            return null;
        } else {
            if (DirectorDTOConverter.directoresDTO != null) {
                for (DirectorDTO directorDTO : DirectorDTOConverter.directoresDTO) {
                    if (directorDTO.getDocenteCarrera().getId() == Long.parseLong(value)) {
                        return directorDTO;
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
        if (((DirectorDTO) value).getDocenteCarrera()!=null) {
            return ((DirectorDTO) value).getDocenteCarrera().getId().toString();
        }
        return "";
    }

    public static List<DirectorDTO> getDirectoresDTO() {
        return directoresDTO;
    }

    public static void setDirectoresDTO(List<DirectorDTO> directoresDTO) {
        DirectorDTOConverter.directoresDTO = directoresDTO;
    }
    
}
