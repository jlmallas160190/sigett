/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.documentoProyecto;

import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.service.ItemService;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author jorge-luis
 */
@FacesConverter("catalogoDocumentoConverter")
public class CatalogoDocumentoConverter implements Converter {

    @EJB
    ItemService itemService;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return itemService.buscarPorId(Long.valueOf(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Item item=((Item) value);
        return item.getNombre();
    }

}
