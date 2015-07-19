/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.evaluacionTribunal;

import edu.unl.sigett.entity.RangoNota;
import edu.unl.sigett.service.RangoNotaService;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author jorge-luis
 */
@FacesConverter("rangoNotaConverter")
public class RangoNotaConverter implements Converter {

    @EJB(lookup = "java:global/SigettService/RangoNotaServiceImplement!edu.unl.sigett.service.RangoNotaService")
    private RangoNotaService rangoNotaService;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        RangoNota rangoNotaBuscar = new RangoNota();
        rangoNotaBuscar.setCodigo(value);
        return this.rangoNotaService.buscarPorCodigo(rangoNotaBuscar);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        RangoNota rangoNota = ((RangoNota) value);
        if (rangoNota == null) {
            return null;
        }
        return rangoNota.getCodigo();
    }

}
