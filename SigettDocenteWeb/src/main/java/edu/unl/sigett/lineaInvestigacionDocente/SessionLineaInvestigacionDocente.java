/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.lineaInvestigacionDocente;

import edu.unl.sigett.entity.LineaInvestigacion;
import edu.unl.sigett.entity.LineaInvestigacionDocente;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.model.DualListModel;

/**
 *
 * @author jorge-luis
 */
@Named(value = "sessionLineaInvestigacionDocente")
@SessionScoped
public class SessionLineaInvestigacionDocente implements Serializable {

    private DualListModel<LineaInvestigacion> lineasInvestigacionDualList;
    private List<LineaInvestigacionDocente> lineaInvestigacionDocentesRemovidos;

    public SessionLineaInvestigacionDocente() {
        this.lineaInvestigacionDocentesRemovidos = new ArrayList<>();
    }

    public DualListModel<LineaInvestigacion> getLineasInvestigacionDualList() {
        return lineasInvestigacionDualList;
    }

    public void setLineasInvestigacionDualList(DualListModel<LineaInvestigacion> lineasInvestigacionDualList) {
        this.lineasInvestigacionDualList = lineasInvestigacionDualList;
    }

    public List<LineaInvestigacionDocente> getLineaInvestigacionDocentesRemovidos() {
        return lineaInvestigacionDocentesRemovidos;
    }

    public void setLineaInvestigacionDocentesRemovidos(List<LineaInvestigacionDocente> lineaInvestigacionDocentesRemovidos) {
        this.lineaInvestigacionDocentesRemovidos = lineaInvestigacionDocentesRemovidos;
    }

}
