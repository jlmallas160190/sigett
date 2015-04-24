/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.finalizacion.controlador;

import com.jlmallas.api.date.DateResource;
import edu.unl.sigett.finalizacion.managed.session.SessionPlazoEvaluacionTribunal;
import edu.unl.sigett.seguridad.managed.session.SessionDocenteUsuario;
import edu.unl.sigett.entity.EvaluacionTribunal;
import edu.unl.sigett.entity.PlazoEvaluacionTribunal;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import edu.unl.sigett.session.ConfiguracionGeneralFacadeLocal;
import edu.unl.sigett.session.EvaluacionTribunalFacadeLocal;
import edu.unl.sigett.session.PlazoEvaluacionTribunalFacadeLocal;

/**
 *
 * @author jorge-luis
 */
@Named(value = "administrarPlazoTribunal")
@SessionScoped
public class AdministrarPlazoTribunal implements Serializable {

    @Inject
    private SessionPlazoEvaluacionTribunal sessionPlazoEvaluacionTribunal;
    @Inject
    private SessionDocenteUsuario sessionDocenteUsuario;
    @Inject
    private AdministrarTribunales administrarTribunales;

    @EJB
    private PlazoEvaluacionTribunalFacadeLocal plazoEvaluacionTribunalFacadeLocal;
    @EJB
    private EvaluacionTribunalFacadeLocal evaluacionTribunalFacadeLocal;
    @EJB
    private ConfiguracionGeneralFacadeLocal configuracionGeneralFacadeLocal;

    private boolean renderedEditarDlg;

    public AdministrarPlazoTribunal() {
    }

    public void crear(EvaluacionTribunal evaluacionTribunal) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            sessionPlazoEvaluacionTribunal.setPlazoEvaluacionTribunal(new PlazoEvaluacionTribunal());
            sessionPlazoEvaluacionTribunal.getPlazoEvaluacionTribunal().setEvaluacionTribunalId(evaluacionTribunal);
            sessionPlazoEvaluacionTribunal.getPlazoEvaluacionTribunal().setFechaInicial(evaluacionTribunal.getFechaFin());
            if (param.equalsIgnoreCase("crear-dlg")) {
                this.renderedEditarDlg = true;
                RequestContext.getCurrentInstance().execute("PF('dlgEditarPlazo').show()");
            }
        } catch (Exception e) {
        }
    }

    public void editar(PlazoEvaluacionTribunal plazoEvaluacionTribunal) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            sessionPlazoEvaluacionTribunal.setPlazoEvaluacionTribunal(plazoEvaluacionTribunal);
            if (param.equalsIgnoreCase("editar-dlg")) {
                this.renderedEditarDlg = true;
                RequestContext.getCurrentInstance().execute("PF('dlgEditarPlazo').show()");
            }
        } catch (Exception e) {
        }
    }

    public void grabar(PlazoEvaluacionTribunal plazoEvaluacionTribunal) {
        try {
            int plazoMaximo = Integer.parseInt(configuracionGeneralFacadeLocal.find((int) 10).getValor());
            DateResource calculo = new DateResource();
            int duracionDias = 0;
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            duracionDias = calculo.calculaDuracionEnDias(plazoEvaluacionTribunal.getEvaluacionTribunalId().getFechaInicio(), plazoEvaluacionTribunal.getFechaPlazo(), 2);
            if (plazoMaximo >= duracionDias) {
                if (plazoEvaluacionTribunal.getId() == null) {
                    plazoEvaluacionTribunalFacadeLocal.create(plazoEvaluacionTribunal);
                    plazoEvaluacionTribunal.getEvaluacionTribunalId().setFechaPlazo(plazoEvaluacionTribunal.getFechaPlazo());
                    evaluacionTribunalFacadeLocal.edit(plazoEvaluacionTribunal.getEvaluacionTribunalId());
                    administrarTribunales.buscarPorDocente(sessionDocenteUsuario.getDocenteUsuario().getDocenteId(), "");
                    if (param.equalsIgnoreCase("grabar-dlg")) {
                        RequestContext.getCurrentInstance().execute("PF('dlgEditarPlazo').hide()");
                        sessionPlazoEvaluacionTribunal.setPlazoEvaluacionTribunal(new PlazoEvaluacionTribunal());
                    } else {
                        if (param.equalsIgnoreCase("grabar-editar-dlg")) {
                            RequestContext.getCurrentInstance().execute("PF('dlgEditarPlazo').hide()");
                        }
                    }
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                } else {
                    plazoEvaluacionTribunalFacadeLocal.edit(plazoEvaluacionTribunal);
                    plazoEvaluacionTribunal.getEvaluacionTribunalId().setFechaPlazo(plazoEvaluacionTribunal.getFechaPlazo());
                    evaluacionTribunalFacadeLocal.edit(plazoEvaluacionTribunal.getEvaluacionTribunalId());
                    administrarTribunales.buscarPorDocente(sessionDocenteUsuario.getDocenteUsuario().getDocenteId(), "");
                    if (param.equalsIgnoreCase("grabar-dlg")) {
                        RequestContext.getCurrentInstance().execute("PF('dlgEditarPlazoEvaluacionTribunal').hide()");
                        sessionPlazoEvaluacionTribunal.setPlazoEvaluacionTribunal(new PlazoEvaluacionTribunal());
                    } else {
                        if (param.equalsIgnoreCase("grabar-editar-dlg")) {
                            RequestContext.getCurrentInstance().execute("PF('dlgEditarPlazoEvaluacionTribunal').hide()");
                        }
                    }
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.plazo_cambios_privada_excede") + ".", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
    }

    public boolean isRenderedEditarDlg() {
        return renderedEditarDlg;
    }

    public void setRenderedEditarDlg(boolean renderedEditarDlg) {
        this.renderedEditarDlg = renderedEditarDlg;
    }

}
