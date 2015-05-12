/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.finalizacion.controlador;

import edu.unl.sigett.finalizacion.managed.session.SessionSugerenciaCalificacionMiembro;
import edu.unl.sigett.entity.CalificacionMiembro;
import edu.unl.sigett.entity.SugerenciaCalificacionMiembro;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import edu.unl.sigett.dao.SugerenciaCalificacionMiembroFacadeLocal;

/**
 *
 * @author jorge-luis
 */
@Named(value = "administrarSugerenciaCalificacionMiembro")
@SessionScoped
public class AdministrarSugerenciaCalificacionMiembro implements Serializable {

    @Inject
    private SessionSugerenciaCalificacionMiembro sessionSugerenciaCalificacionMiembro;

    @EJB
    private SugerenciaCalificacionMiembroFacadeLocal sugerenciaCalificacionMiembroFacadeLocal;

    private boolean renderedDlgEditar;
    private String criterio;

    private List<SugerenciaCalificacionMiembro> sugerenciaCalificacionMiembros;

    public AdministrarSugerenciaCalificacionMiembro() {
    }

    public String crear(CalificacionMiembro calificacionMiembro) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            sessionSugerenciaCalificacionMiembro.setSugerenciaCalificacionMiembro(new SugerenciaCalificacionMiembro());
            sessionSugerenciaCalificacionMiembro.getSugerenciaCalificacionMiembro().setCalificacionMiembroId(calificacionMiembro);
            sessionSugerenciaCalificacionMiembro.getSugerenciaCalificacionMiembro().setEsActivo(true);
            if (param.equals("crear-dlg")) {
                renderedDlgEditar = true;
                RequestContext.getCurrentInstance().execute("PF('dlgEditarSugerencia').show()");
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public String editar(SugerenciaCalificacionMiembro sugerenciaCalificacionMiembro) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            sessionSugerenciaCalificacionMiembro.setSugerenciaCalificacionMiembro(sugerenciaCalificacionMiembro);
            if (param.equals("editar-dlg")) {
                renderedDlgEditar = true;
                RequestContext.getCurrentInstance().execute("PF('dlgEditarSugerencia').show()");
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public String grabar(SugerenciaCalificacionMiembro sugerenciaCalificacionMiembro, CalificacionMiembro calificacionMiembro) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            if (sugerenciaCalificacionMiembro.getId() == null) {
                sugerenciaCalificacionMiembroFacadeLocal.create(sugerenciaCalificacionMiembro);
                buscar(calificacionMiembro, "");
                if (param.equalsIgnoreCase("grabar-dlg")) {
                    RequestContext.getCurrentInstance().execute("PF('dlgEditarSugerencia').hide()");
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    sessionSugerenciaCalificacionMiembro.setSugerenciaCalificacionMiembro(new SugerenciaCalificacionMiembro());
                } else {
                    if (param.equalsIgnoreCase("grabar-editar-dlg")) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                }
            } else {
                sugerenciaCalificacionMiembroFacadeLocal.edit(sugerenciaCalificacionMiembro);
                buscar(calificacionMiembro, "");
                if (param.equalsIgnoreCase("grabar-dlg")) {
                    RequestContext.getCurrentInstance().execute("PF('dlgEditarSugerencia').hide()");
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    sessionSugerenciaCalificacionMiembro.setSugerenciaCalificacionMiembro(new SugerenciaCalificacionMiembro());
                } else {
                    if (param.equalsIgnoreCase("grabar-editar-dlg")) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                }
            }
            if (param.equals("editar-dlg")) {
                renderedDlgEditar = true;
                RequestContext.getCurrentInstance().execute("PF('dlgEditarSugerencia').show()");
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public void remover(SugerenciaCalificacionMiembro sugerenciaCalificacionMiembro, CalificacionMiembro calificacionMiembro) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (sugerenciaCalificacionMiembro.getId() != null) {
                sugerenciaCalificacionMiembroFacadeLocal.remove(sugerenciaCalificacionMiembro);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_eliminar"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                buscar(calificacionMiembro, "");
            }
        } catch (Exception e) {
        }
    }

    public void buscar(CalificacionMiembro calificacionMiembro, String criterio) {
        try {
            this.sugerenciaCalificacionMiembros = new ArrayList<>();
            for (SugerenciaCalificacionMiembro sugerenciaCalificacionMiembro : sugerenciaCalificacionMiembroFacadeLocal.buscarPorCalificacioMiembro(calificacionMiembro.getId())) {
                if (sugerenciaCalificacionMiembro.getDescripcion().toLowerCase().contains(criterio.toLowerCase())) {
                    sugerenciaCalificacionMiembros.add(sugerenciaCalificacionMiembro);
                }
            }
        } catch (Exception e) {
        }
    }

    public void editar() {

    }

    public boolean isRenderedDlgEditar() {
        return renderedDlgEditar;
    }

    public void setRenderedDlgEditar(boolean renderedDlgEditar) {
        this.renderedDlgEditar = renderedDlgEditar;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public List<SugerenciaCalificacionMiembro> getSugerenciaCalificacionMiembros() {
        return sugerenciaCalificacionMiembros;
    }

    public void setSugerenciaCalificacionMiembros(List<SugerenciaCalificacionMiembro> sugerenciaCalificacionMiembros) {
        this.sugerenciaCalificacionMiembros = sugerenciaCalificacionMiembros;
    }

}
