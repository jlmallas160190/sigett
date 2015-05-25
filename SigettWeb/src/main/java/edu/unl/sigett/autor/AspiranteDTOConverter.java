package edu.unl.sigett.autor;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import edu.unl.sigett.academico.converter.DocenteCarreraDTOConverter;
import edu.unl.sigett.academico.dto.DocenteCarreraDTO;
import edu.unl.sigett.autor.dto.AspiranteDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author jorge-luis
 */
@FacesConverter("aspiranteDTOConverter")
public class AspiranteDTOConverter implements Converter {

    private static List<AspiranteDTO> aspirantesDTO;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value.trim().equals("")) {
            return null;
        } else {
            if (AspiranteDTOConverter.aspirantesDTO != null) {
                for (AspiranteDTO aspiranteDTO : AspiranteDTOConverter.aspirantesDTO) {
                    if (aspiranteDTO.getAspirante().getId() == Long.parseLong(value)) {
                        return aspiranteDTO;
                    }
                }
            }
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (((AspiranteDTO) value).getAspirante() != null) {
            return ((AspiranteDTO) value).getAspirante().getId().toString();
        }
        return "";
    }

    public static List<AspiranteDTO> getAspirantesDTO() {
        return aspirantesDTO;
    }

    public static void setAspirantesDTO(List<AspiranteDTO> aspirantesDTO) {
        AspiranteDTOConverter.aspirantesDTO = aspirantesDTO;
    }

}
