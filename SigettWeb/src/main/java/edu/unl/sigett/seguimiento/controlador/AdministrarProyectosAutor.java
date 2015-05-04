/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguimiento.controlador;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.adjudicacion.controlador.AdministrarDirectoresProyecto;
import edu.unl.sigett.postulacion.controlador.AdministrarCronograma;
import edu.unl.sigett.postulacion.controlador.AdministrarDocumentosProyecto;
import edu.unl.sigett.seguimiento.session.SessionProyectosAutor;
import edu.unl.sigett.comun.controlador.AdministrarConfiguraciones;
import edu.unl.sigett.seguridad.managed.session.SessionEstudianteUsuario;
import edu.unl.sigett.entity.AutorProyecto;
import edu.jlmallas.academico.entity.Estudiante;
import org.jlmallas.seguridad.entity.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.TabChangeEvent;
import edu.unl.sigett.session.ActividadFacadeLocal;
import edu.unl.sigett.session.AutorProyectoFacadeLocal;
import org.jlmallas.seguridad.dao.UsuarioDao;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "autorProyectos",
            pattern = "/autorProyectos/",
            viewId = "/faces/pages/sigett/buscarProyectosAutor.xhtml"
    ),
    @URLMapping(
            id = "editarAutorProyecto",
            pattern = "/editarAutorProyecto/#{sessionProyectosAutor.autorProyecto.id}",
            viewId = "/faces/pages/sigett/editarProyectoAutor.xhtml"
    )
})
public class AdministrarProyectosAutor implements Serializable {

    @Inject
    private SessionProyectosAutor sessionProyectosAutor;
    @Inject
    private SessionEstudianteUsuario sessionEstudianteUsuario;
    @Inject
    private AdministrarDocumentosProyecto administrarDocumentosProyecto;
    @Inject
    private AdministrarActividades administrarActividades;
    @Inject
    private AdministrarRevisiones administrarRevisiones;
    @Inject
    private AdministrarDirectoresProyecto administrarDirectoresProyecto;
    @Inject
    private AdministrarConfiguraciones administrarConfiguraciones;
    @Inject
    private AdministrarCronograma administrarCronograma;

    @EJB
    private AutorProyectoFacadeLocal autorProyectoFacadeLocal;
    @EJB
    private UsuarioDao usuarioFacadeLocal;
    @EJB
    private ActividadFacadeLocal actividadFacadeLocal;

    private boolean renderedNoEditar;

    private int intervalo;
    private String criterio;

    private List<AutorProyecto> autorProyectos;

    public AdministrarProyectosAutor() {
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public void buscar(Usuario usuario, Estudiante estudiante, String criterio) {
        this.autorProyectos = new ArrayList<>();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_autor_proyecto");
            if (tienePermiso == 1) {
                for (AutorProyecto autorProyecto : autorProyectoFacadeLocal.buscarPorEstudiante(estudiante.getId())) {
                    if (autorProyecto.getEstadoAutorId().getId() != 4) {
                        if (autorProyecto.getProyectoId().getTemaActual().toLowerCase().contains(criterio.toLowerCase())) {
                            autorProyectos.add(autorProyecto);
                        }
                    }
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
    }

    public void onTabChange(TabChangeEvent event) {
        switch (event.getTab().getId()) {
            case "documentos":
                administrarDocumentosProyecto.buscarPorAutor(sessionProyectosAutor.getAutorProyecto().getProyectoId());
                administrarDocumentosProyecto.renderedCrear(sessionEstudianteUsuario.getUsuario());
                administrarDocumentosProyecto.renderedEditar(sessionEstudianteUsuario.getUsuario());
                administrarDocumentosProyecto.renderedEliminar(sessionEstudianteUsuario.getUsuario());
                break;
            case "directores":
                administrarDirectoresProyecto.historialDirectoresProyecto("", sessionProyectosAutor.getAutorProyecto().getProyectoId());
                break;
            case "actividades":
                administrarActividades.renderedBuscar(sessionEstudianteUsuario.getUsuario(), sessionProyectosAutor.getAutorProyecto().getProyectoId());
                administrarActividades.buscarPorAutorProyecto("", sessionEstudianteUsuario.getUsuario(), sessionProyectosAutor.getAutorProyecto().getProyectoId().getCronograma());
                administrarActividades.renderedEditar(sessionEstudianteUsuario.getUsuario(), sessionProyectosAutor.getAutorProyecto().getProyectoId());
                administrarActividades.renderedCrear(sessionEstudianteUsuario.getUsuario(), sessionProyectosAutor.getAutorProyecto().getProyectoId());
                administrarActividades.setRenderedEditar(false);
                break;
            case "inicio":
                administrarCronograma.calculaAvanceFaltanteCronograma(sessionProyectosAutor.getAutorProyecto().getProyectoId().getCronograma(),
                        actividadFacadeLocal.buscarPorProyecto(sessionProyectosAutor.getAutorProyecto().getProyectoId().getCronograma().getId()), 
                        sessionEstudianteUsuario.getUsuario());
        }
    }

    public String editar(AutorProyecto autorProyecto, Usuario usuario) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_autor_proyecto");
            if (tienePermiso == 1) {
                sessionProyectosAutor.setAutorProyecto(autorProyecto);
                intervalo = administrarConfiguraciones.intervaloActualizaciones();
                /*----------------------------------------Documentos-------------------------------------------------*/
                administrarDocumentosProyecto.buscarPorAutor(sessionProyectosAutor.getAutorProyecto().getProyectoId());
                administrarDocumentosProyecto.renderedCrear(sessionEstudianteUsuario.getUsuario());
                administrarDocumentosProyecto.renderedEditar(sessionEstudianteUsuario.getUsuario());
                administrarDocumentosProyecto.renderedEliminar(sessionEstudianteUsuario.getUsuario());
                /*------------------------------------------------Directores--------------------------------------------*/
                administrarDirectoresProyecto.historialDirectoresProyecto("", autorProyecto.getProyectoId());
                /*---------------------------------------------------Actividades---------------------------------------------------*/
                administrarActividades.buscarPorAutorProyecto("", sessionEstudianteUsuario.getUsuario(), sessionProyectosAutor.getAutorProyecto().getProyectoId().getCronograma());
                administrarActividades.renderedEditar(sessionEstudianteUsuario.getUsuario(), sessionProyectosAutor.getAutorProyecto().getProyectoId());
                administrarActividades.renderedCrear(sessionEstudianteUsuario.getUsuario(), sessionProyectosAutor.getAutorProyecto().getProyectoId());
                administrarActividades.setRenderedEditar(false);
                navegacion = "pretty:editarAutorProyecto";
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
        return navegacion;
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">

    public boolean renderedEditar(Usuario usuario) {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_autor_proyecto");
        if (tienePermiso == 1) {
            var = true;
            renderedNoEditar = false;
        } else {
            var = false;
            renderedNoEditar = true;
        }
        return var;
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
    public int getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(int intervalo) {
        this.intervalo = intervalo;
    }

    public SessionProyectosAutor getSessionProyectosAutor() {
        return sessionProyectosAutor;
    }

    public void setSessionProyectosAutor(SessionProyectosAutor sessionProyectosAutor) {
        this.sessionProyectosAutor = sessionProyectosAutor;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

    public AdministrarDocumentosProyecto getAdministrarDocumentosProyecto() {
        return administrarDocumentosProyecto;
    }

    public void setAdministrarDocumentosProyecto(AdministrarDocumentosProyecto administrarDocumentosProyecto) {
        this.administrarDocumentosProyecto = administrarDocumentosProyecto;
    }

    public List<AutorProyecto> getAutorProyectos() {
        return autorProyectos;
    }

    public void setAutorProyectos(List<AutorProyecto> autorProyectos) {
        this.autorProyectos = autorProyectos;
    }

    public AdministrarActividades getAdministrarActividades() {
        return administrarActividades;
    }

    public void setAdministrarActividades(AdministrarActividades administrarActividades) {
        this.administrarActividades = administrarActividades;
    }

    public AdministrarRevisiones getAdministrarRevisiones() {
        return administrarRevisiones;
    }

    public void setAdministrarRevisiones(AdministrarRevisiones administrarRevisiones) {
        this.administrarRevisiones = administrarRevisiones;
    }

    public SessionEstudianteUsuario getSessionEstudianteUsuario() {
        return sessionEstudianteUsuario;
    }

    public void setSessionEstudianteUsuario(SessionEstudianteUsuario sessionEstudianteUsuario) {
        this.sessionEstudianteUsuario = sessionEstudianteUsuario;
    }
//</editor-fold>
}
