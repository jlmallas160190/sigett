/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.calificacionMiembro;

import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.service.ItemService;
import edu.unl.sigett.docenteUsuario.DocenteUsuarioDM;
import edu.unl.sigett.entity.CalificacionMiembro;
import edu.unl.sigett.entity.CalificacionParametro;
import edu.unl.sigett.entity.MiembroTribunal;
import edu.unl.sigett.entity.ParametroCatalogoEvaluacion;
import edu.unl.sigett.enumeration.EstadoProyectoEnum;
import edu.unl.sigett.evaluacionTribunal.SessionEvaluacionTribunal;
import edu.unl.sigett.service.CalificacionMiembroService;
import edu.unl.sigett.service.CalificacionParametroService;
import edu.unl.sigett.service.EvaluacionTribunalService;
import edu.unl.sigett.service.MiembroTribunalService;
import edu.unl.sigett.service.ParametroCatalogoEvaluacionService;
import edu.unl.sigett.util.CabeceraController;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.jlmallas.secure.Secure;
import org.primefaces.context.RequestContext;

/**
 *
 * @author jorge-luis
 */
@Named(value = "calificacionMiembroController")
@SessionScoped
public class CalificacionMiembroController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private SessionCalificacionMiembro sessionCalificacionMiembro;
    @Inject
    private SessionEvaluacionTribunal sessionEvaluacionTribunal;
    @Inject
    private CabeceraController cabeceraController;
    @Inject
    private DocenteUsuarioDM docenteUsuarioDM;
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB(lookup = "java:global/SigettService/CalificacionMiembroServiceImplement!edu.unl.sigett.service.CalificacionMiembroService")
    private CalificacionMiembroService calificacionMiembroService;
    @EJB(lookup = "java:global/SigettService/MiembroTribunalServiceImplement!edu.unl.sigett.service.MiembroTribunalService")
    private MiembroTribunalService miembroTribunalService;
    @EJB(lookup = "java:global/ComunService/ItemServiceImplement!com.jlmallas.comun.service.ItemService")
    private ItemService itemService;
    @EJB(lookup = "java:global/SigettService/ParametroCatalogoEvaluacionServiceImplement!edu.unl.sigett.service.ParametroCatalogoEvaluacionService")
    private ParametroCatalogoEvaluacionService parametroCatalogoEvaluacionService;
    @EJB(lookup = "java:global/SigettService/CalificacionParametroServiceImplement!edu.unl.sigett.service.CalificacionParametroService")
    private CalificacionParametroService calificacionParametroService;
    @EJB(lookup = "java:global/SigettService/EvaluacionTribunalServiceImplement!edu.unl.sigett.service.EvaluacionTribunalService")
    private EvaluacionTribunalService evaluacionTribunalService;
//</editor-fold>
    private static final Logger LOG = Logger.getLogger(CalificacionMiembroController.class.getName());

    public CalificacionMiembroController() {
    }

    public void preRenderView() {
        renderedEditar();
        buscar();
    }

    //<editor-fold defaultstate="collapsed" desc="CRUD">
    private void buscar() {
        try {
            sessionCalificacionMiembro.getCalificacionMiembros().clear();
            CalificacionMiembro calificacionMiembroBuscar = new CalificacionMiembro();
            calificacionMiembroBuscar.setEvaluacionTribunalId(sessionEvaluacionTribunal.getEvaluacionTribunal());
            List<CalificacionMiembro> calificacionMiembros = calificacionMiembroService.buscar(calificacionMiembroBuscar);
            for (CalificacionMiembro calificacionMiembro : calificacionMiembros) {
                calificacionMiembro.setEditar(Boolean.FALSE);
                String miembroId = cabeceraController.getSecureService().decrypt(
                        new Secure(cabeceraController.getConfiguracionGeneralUtil().getSecureKey(), calificacionMiembro.getMiembroId()));
                MiembroTribunal miembroTribunal = miembroTribunalService.buscarPorId(new MiembroTribunal(Long.parseLong(miembroId)));
                if (miembroTribunal.getDocenteId().equals(docenteUsuarioDM.getDocenteUsuarioDTO().getDocente().getId())) {
                    calificacionMiembro.setEditar(Boolean.TRUE);
                }
                if (!sessionCalificacionMiembro.getCalificacionMiembros().contains(calificacionMiembro)) {
                    sessionCalificacionMiembro.getCalificacionMiembros().add(calificacionMiembro);
                }
            }
        } catch (NumberFormatException e) {
            LOG.warning(e.getMessage());
        }

    }

    public void editar(CalificacionMiembro calificacionMiembro) {
        if (!sessionCalificacionMiembro.getRenderedEditar()) {
            return;
        }
        sessionCalificacionMiembro.setRenderedCrud(Boolean.TRUE);
        sessionCalificacionMiembro.setCalificacionMiembro(calificacionMiembro);
        buscarCalificacionParametros();
        RequestContext.getCurrentInstance().execute("PF('dlgCrudCalificacionMiembro').show()");
    }

    private void buscarCalificacionParametros() {
        sessionCalificacionMiembro.getCalificacionParametros().clear();
        ParametroCatalogoEvaluacion parametroCatalogoEvaluacionBuscar = new ParametroCatalogoEvaluacion();
        parametroCatalogoEvaluacionBuscar.setCatalogoEvaluacionId(sessionEvaluacionTribunal.getEvaluacionTribunal().getCatalogoEvaluacionId());
        List<ParametroCatalogoEvaluacion> parametroCatalogoEvaluacions = parametroCatalogoEvaluacionService.buscar(parametroCatalogoEvaluacionBuscar);
        for (ParametroCatalogoEvaluacion parametroCatalogoEvaluacion : parametroCatalogoEvaluacions) {
            CalificacionParametro calificacionParametroBuscar = new CalificacionParametro();
            calificacionParametroBuscar.setCalificacionMiembroId(sessionCalificacionMiembro.getCalificacionMiembro());
            calificacionParametroBuscar.setParametroCatEvId(parametroCatalogoEvaluacion);
            List<CalificacionParametro> calificacionParametros = calificacionParametroService.buscar(calificacionParametroBuscar);
            CalificacionParametro calificacionParametro = !calificacionParametros.isEmpty() ? calificacionParametros.get(0) : null;
            if (calificacionParametro != null) {
                sessionCalificacionMiembro.getCalificacionParametros().add(calificacionParametro);
                continue;
            }
            if (!parametroCatalogoEvaluacion.getParametroId().getEsActivo()) {
                continue;
            }
            calificacionParametro = new CalificacionParametro(null, BigDecimal.ZERO, parametroCatalogoEvaluacion, sessionCalificacionMiembro.getCalificacionMiembro());
            sessionCalificacionMiembro.getCalificacionParametros().add(calificacionParametro);
            calificacionParametroService.guardar(calificacionParametro);
        }
    }

    private void actualizarNotaEvaluacionTribunal() {
        Double nota = 0.0;
        CalificacionMiembro calificacionMiembroBuscar = new CalificacionMiembro();
        calificacionMiembroBuscar.setEvaluacionTribunalId(sessionEvaluacionTribunal.getEvaluacionTribunal());
        List<CalificacionMiembro> calificacionMiembros = calificacionMiembroService.buscar(calificacionMiembroBuscar);
        for (CalificacionMiembro calificacionMiembro : calificacionMiembros) {
            nota += calificacionMiembro.getNota().doubleValue();
        }
        nota = nota / sessionCalificacionMiembro.getCalificacionMiembros().size();
        sessionEvaluacionTribunal.getEvaluacionTribunal().setNota(new BigDecimal(nota));

        evaluacionTribunalService.actualizar(sessionEvaluacionTribunal.getEvaluacionTribunal());
    }

    public void grabar() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("tipo-grabado");
        if (!sessionCalificacionMiembro.getRenderedEditar()) {
            return;
        }
        calificacionMiembroService.actualizar(sessionCalificacionMiembro.getCalificacionMiembro());
        actualizarCalificacionParametros();
        actualizarNotaEvaluacionTribunal();
        cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("grabar"), "");
        if (param.equals("grabar")) {
            cancelarEdicion();
        }
    }

    public void calculaNota() {
        try {
            Double nota = 0.0;
            for (CalificacionParametro cp : sessionCalificacionMiembro.getCalificacionParametros()) {
                nota += cp.getNota().doubleValue();
            }
            nota = nota / sessionCalificacionMiembro.getCalificacionParametros().size();
            sessionCalificacionMiembro.getCalificacionMiembro().setNota(new BigDecimal(nota));
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    private void actualizarCalificacionParametros() {
        for (CalificacionParametro calificacionParametro : sessionCalificacionMiembro.getCalificacionParametros()) {
            calificacionParametroService.actualizar(calificacionParametro);
        }
    }

    public void cancelarEdicion() {
        sessionCalificacionMiembro.setRenderedCrud(Boolean.FALSE);
        sessionCalificacionMiembro.setCalificacionMiembro(new CalificacionMiembro());
        RequestContext.getCurrentInstance().execute("PF('dlgCrudCalificacionMiembro').hide()");
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="RENDERED">
    private void renderedEditar() {
        sessionCalificacionMiembro.setRenderedEditar(Boolean.FALSE);
        Item estadoActualProyecto = itemService.buscarPorId(sessionEvaluacionTribunal.getEvaluacionTribunal().getTribunalId().getProyectoId().getEstadoProyectoId());
        if (!(estadoActualProyecto.getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
                || estadoActualProyecto.getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo()))
                || estadoActualProyecto.getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
                || estadoActualProyecto.getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
            return;
        }
        Calendar fechaActual = Calendar.getInstance();
        if (!((fechaActual.getTime().after(sessionEvaluacionTribunal.getEvaluacionTribunal().getFechaInicio())
                || fechaActual.getTime().equals(sessionEvaluacionTribunal.getEvaluacionTribunal().getFechaInicio()))
                && fechaActual.getTime().before(sessionEvaluacionTribunal.getEvaluacionTribunal().getFechaPlazo())
                || fechaActual.getTime().equals(sessionEvaluacionTribunal.getEvaluacionTribunal().getFechaPlazo()))) {
            return;
        }
        sessionCalificacionMiembro.setRenderedEditar(Boolean.TRUE);
    }
    //</editor-fold>
}
