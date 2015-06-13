/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguimiento.controlador;

import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.dao.PersonaDao;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.finalizacion.controlador.AdministrarInformesProyecto;
import edu.unl.sigett.seguimiento.session.SessionProyectosDirector;
import edu.unl.sigett.entity.DirectorProyecto;
import edu.jlmallas.academico.entity.Docente;
import edu.jlmallas.academico.entity.DocenteCarrera;
import edu.jlmallas.academico.entity.OfertaAcademica;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.entity.ProyectoCarreraOferta;
import org.jlmallas.seguridad.entity.Usuario;
import java.io.Serializable;
import java.text.SimpleDateFormat;
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
import edu.unl.sigett.dao.ActividadFacadeLocal;
import edu.unl.sigett.dao.ConfiguracionGeneralDao;
import edu.unl.sigett.dao.DirectorProyectoDao;
import edu.unl.sigett.dao.ProyectoOfertaCarreraDao;
import org.jlmallas.seguridad.dao.UsuarioDao;
import edu.jlmallas.academico.service.OfertaAcademicaService;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "directorProyectos",
            pattern = "/directorProyectos/",
            viewId = "/faces/pages/sigett/buscarDirectorProyectos.xhtml"
    ),
    @URLMapping(
            id = "editarDirectorProyecto",
            pattern = "/editarDirectorProyecto/#{sessionProyectosDirector.directorProyecto.id}",
            viewId = "/faces/pages/sigett/editarDirectorProyecto.xhtml"
    )
})
public class AdministrarProyectosDirector implements Serializable {

    @Inject
    private SessionProyectosDirector sessionProyectosDirector;
    @Inject
    private AdministrarActividades administrarActividades;
    @Inject
    private AdministrarRevisiones administrarRevisiones;
    @Inject
    private AdministrarInformesProyecto administrarInformesProyecto;
    @EJB
    private UsuarioDao usuarioFacadeLocal;
    @EJB
    private DirectorProyectoDao directorProyectoFacadeLocal;
    @EJB
    private ActividadFacadeLocal actividadFacadeLocal;
    @EJB
    private ConfiguracionGeneralDao configuracionGeneralFacadeLocal;
    @EJB
    private ProyectoOfertaCarreraDao proyectoCarreraOfertaFacadeLocal;
    @EJB
    private OfertaAcademicaService ofertaAcademicaFacadeLocal;
    @EJB
    private PersonaDao personaFacadeLocal;

    private List<DirectorProyecto> directorProyectos;
    private List<OfertaAcademica> ofertasAcademicas;
    private boolean renderedEditar;
    private boolean renderedNoEditar;
    private boolean renderedImprimirAutorizacionSp;

    private String criterio;
    private int intervalo;

    public AdministrarProyectosDirector() {
    }

    @PostConstruct
    public void init() {
        this.directorProyectos = new ArrayList<>();
        this.ofertasAcademicas = new ArrayList<>();
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public void notificacionesProyectos(Docente docente) {
        try {
            this.directorProyectos = new ArrayList<>();
            List<String> valores = new ArrayList<>();
            Map parametros = new HashMap();
            parametros.put("filtro", "3");
            parametros.put("docenteId", docente.getId());
//            ProyectoResource proyectoResource = new ProyectoResource(configuracionGeneralFacadeLocal.find(17).getValor());
//            valores = proyectoResource.buscarPorDocente(parametros);
            for (String valor : valores) {
                DirectorProyecto dp = directorProyectoFacadeLocal.find(Long.parseLong(valor));
                if (dp != null && !this.directorProyectos.contains(dp)) {
                    this.directorProyectos.add(dp);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void buscar(String filtro, Docente docente) {
        try {
            this.directorProyectos = new ArrayList<>();
            List<String> valores = new ArrayList<>();
            Map parametros = new HashMap();
            parametros.put("filtro", filtro);
            parametros.put("docenteId", docente.getId());
//            ProyectoResource proyectoResource = new ProyectoResource(configuracionGeneralFacadeLocal.find(17).getValor());
//            valores = proyectoResource.buscarPorDocente(parametros);
            for (String valor : valores) {
                DirectorProyecto dp = directorProyectoFacadeLocal.find(Long.parseLong(valor));
                if (dp != null && !this.directorProyectos.contains(dp)) {
                    this.directorProyectos.add(dp);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
    }

    public void buscarPorPeriodos(OfertaAcademica ofertaAcademica) {
        try {
//            if (!ofertasAcademicas.contains(ofertaAcademica)) {
//                if (ofertaAcademica.isEsSeleccionado()) {
//                    this.ofertasAcademicas.add(ofertaAcademica);
//                }
//            }
            for (OfertaAcademica of : ofertasAcademicas) {
                SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
                String fechaCreate = formatoFecha.format(of.getFechaInicio());
//                buscar(fechaCreate, sessionDocenteUsuario.getDocente());
            }
        } catch (Exception e) {
        }
    }

    public void onTabChange(TabChangeEvent event) {
        if (sessionProyectosDirector.getDirectorProyecto().getProyectoId() != null) {
            switch (event.getTab().getId()) {
                case "autores":
//                    administrarAutoresProyecto.buscarAutoresDesdeDirectorProyecto("", sessionProyectosDirector.getDirectorProyecto().getProyectoId());
                    administrarInformesProyecto.setRenderedDlgEditar(false);
                    administrarInformesProyecto.setRenderedDlgCertificado(false);
                    break;
                case "prorrogas":
//                    administrarProrrogas.setRenderedDlgOficio(false);
//                    administrarProrrogas.buscar(sessionProyectosDirector.getDirectorProyecto().getProyectoId().getCronograma(), sessionDocenteUsuario.getUsuario(), "");
//                    administrarProrrogas.renderedCrear(sessionDocenteUsuario.getUsuario(), sessionProyectosDirector.getDirectorProyecto().getProyectoId());
//                    administrarProrrogas.renderedBuscar(sessionDocenteUsuario.getUsuario(), sessionProyectosDirector.getDirectorProyecto().getProyectoId());
//                    administrarProrrogas.renderedEditar(sessionDocenteUsuario.getUsuario(), sessionProyectosDirector.getDirectorProyecto().getProyectoId());
//                    administrarProrrogas.renderedEliminar(sessionDocenteUsuario.getUsuario(), sessionProyectosDirector.getDirectorProyecto().getProyectoId());
//                    administrarProrrogas.renderedImprimirOficio(sessionDocenteUsuario.getUsuario());
//                    administrarProrrogas.renderedAceptar(sessionDocenteUsuario.getUsuario(), sessionProyectosDirector.getDirectorProyecto().getProyectoId());
//                    administrarProrrogas.setRenderedDlgOficio(false);
//                    administrarProrrogas.setRenderedDlgEditar(false);
                    administrarInformesProyecto.setRenderedDlgEditar(false);
                    administrarInformesProyecto.setRenderedDlgCertificado(false);
                    break;
                case "actividades":
//                    administrarActividades.buscarPorDirector("", sessionDocenteUsuario.getUsuario(), sessionProyectosDirector.getDirectorProyecto().getProyectoId());
                    administrarInformesProyecto.setRenderedDlgEditar(false);
                    administrarInformesProyecto.setRenderedDlgCertificado(false);
                    break;
                case "inicio":
                    administrarInformesProyecto.setRenderedDlgEditar(false);
//                    administrarCronograma.calculaAvanceFaltanteCronograma(sessionProyectosDirector.getDirectorProyecto().getProyectoId().getCronograma(),
//                            actividadFacadeLocal.buscarPorProyecto(sessionProyectosDirector.getDirectorProyecto().getProyectoId().getCronograma().getId()),
//                            sessionDocenteUsuario.getUsuario());
                    administrarInformesProyecto.setRenderedDlgCertificado(false);
                case "informes":
//                    administrarInformesProyecto.buscar("", sessionProyectosDirector.getDirectorProyecto().getProyectoId(), sessionDocenteUsuario.getUsuario());
//                    administrarInformesProyecto.renderedCrear(sessionDocenteUsuario.getUsuario(), sessionProyectosDirector.getDirectorProyecto().getProyectoId());
//                    administrarInformesProyecto.renderedEditar(sessionDocenteUsuario.getUsuario(), sessionProyectosDirector.getDirectorProyecto().getProyectoId());
//                    administrarInformesProyecto.renderedEliminar(sessionDocenteUsuario.getUsuario(), sessionProyectosDirector.getDirectorProyecto().getProyectoId());
            }
        }
    }

    public void editarPeriodo(OfertaAcademica ofertaAcademica) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            String fechaCreate = formatoFecha.format(ofertaAcademica.getFechaInicio());
//            buscar(fechaCreate, sessionDocenteUsuario.getDocente());
        } catch (Exception e) {
        }
    }

    public String viewBuscarDirectoresProyecto(Usuario usuario) {
        String navegacion = "";
        try {
            navegacion = "pretty:directorProyectos";
            renderedEditar(usuario);
        } catch (Exception e) {
        }
        return navegacion;
    }

    public List<OfertaAcademica> listadoPeriodosAcademicos(Docente docente) {
        List<OfertaAcademica> ofertaAcademicas = new ArrayList<>();
        try {
            for (DocenteCarrera docenteCarrera : docente.getDocenteCarreraList()) {
                for (ProyectoCarreraOferta pco : proyectoCarreraOfertaFacadeLocal.buscarPorCarrera(docenteCarrera.getCarreraId().getId())) {
                    OfertaAcademica ofertaAcademica = ofertaAcademicaFacadeLocal.find(pco.getOfertaAcademicaId());
                    if (!ofertaAcademicas.contains(ofertaAcademica)) {
                        ofertaAcademicas.add(ofertaAcademica);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return ofertaAcademicas;
    }

    public int countProyectosPeriodoCarrera(Docente docente, OfertaAcademica of) {
        int count = 0;
        try {
            Persona p = personaFacadeLocal.find(docente.getId());
//            count = count + directorProyectoFacadeLocal.buscarPorDocenteOferta(p.getNumeroIdentificacion(), of.getId()).size();
        } catch (Exception e) {
            System.out.println(e);
        }
        return count;
    }

    public String editarDirectorProyecto(DirectorProyecto directorProyecto, Usuario usuario, Proyecto proyecto) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_director_proyecto");
            if (tienePermiso == 1) {
                sessionProyectosDirector.setDirectorProyecto(directorProyecto);
                /*---------------------------------------------------------Autores-----------------------------------------------------------*/
//                administrarAutoresProyecto.buscarAutoresDesdeDirectorProyecto("", sessionProyectosDirector.getDirectorProyecto().getProyectoId());
                /*---------------------------------------------Prorrogas------------------------------------------------------------------*/
//                administrarProrrogas.setRenderedDlgOficio(false);
//                administrarProrrogas.buscar(sessionProyectosDirector.getDirectorProyecto().getProyectoId().getCronograma(), sessionDocenteUsuario.getUsuario(), criterio);
//                administrarProrrogas.renderedCrear(sessionDocenteUsuario.getUsuario(), sessionProyectosDirector.getDirectorProyecto().getProyectoId());
//                administrarProrrogas.renderedBuscar(sessionDocenteUsuario.getUsuario(), sessionProyectosDirector.getDirectorProyecto().getProyectoId());
//                administrarProrrogas.renderedEditar(sessionDocenteUsuario.getUsuario(), sessionProyectosDirector.getDirectorProyecto().getProyectoId());
//                administrarProrrogas.renderedEliminar(sessionDocenteUsuario.getUsuario(), sessionProyectosDirector.getDirectorProyecto().getProyectoId());
//                administrarProrrogas.renderedImprimirOficio(sessionDocenteUsuario.getUsuario());
//                administrarProrrogas.renderedAceptar(sessionDocenteUsuario.getUsuario(), sessionProyectosDirector.getDirectorProyecto().getProyectoId());
//                administrarProrrogas.setRenderedDlgOficio(false);
//                administrarProrrogas.setRenderedDlgEditar(false);
                /*--------------------------------------------------------------Actividades----------------------------------------------------------------*/
//                administrarActividades.buscarPorDirector("", sessionDocenteUsuario.getUsuario(), sessionProyectosDirector.getDirectorProyecto().getProyectoId());
//                /*--------------------------------------------------------------Revisiones------------------------------------------------------------------------*/
//                administrarRevisiones.renderedCrear(sessionDocenteUsuario.getUsuario());
//                administrarRevisiones.renderedEditar(sessionDocenteUsuario.getUsuario());
//                administrarRevisiones.renderedEliminar(sessionDocenteUsuario.getUsuario());
//                intervalo = administrarConfiguraciones.intervaloActualizaciones();
                navegacion = "pretty:editarDirectorProyecto";
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

    public void renderedEditar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_director_proyecto");
        if (tienePermiso == 1) {
            renderedEditar = true;
            renderedNoEditar = false;
        } else {
            renderedEditar = false;
            renderedNoEditar = true;
        }
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
    public List<OfertaAcademica> getOfertasAcademicas() {
        return ofertasAcademicas;
    }

    public void setOfertasAcademicas(List<OfertaAcademica> ofertasAcademicas) {
        this.ofertasAcademicas = ofertasAcademicas;
    }

    public boolean isRenderedImprimirAutorizacionSp() {
        return renderedImprimirAutorizacionSp;
    }

    public void setRenderedImprimirAutorizacionSp(boolean renderedImprimirAutorizacionSp) {
        this.renderedImprimirAutorizacionSp = renderedImprimirAutorizacionSp;
    }

    public int getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(int intervalo) {
        this.intervalo = intervalo;
    }

    public boolean isRenderedEditar() {
        return renderedEditar;
    }

    public void setRenderedEditar(boolean renderedEditar) {
        this.renderedEditar = renderedEditar;
    }

    public SessionProyectosDirector getSessionProyectosDirector() {
        return sessionProyectosDirector;
    }

    public void setSessionProyectosDirector(SessionProyectosDirector sessionProyectosDirector) {
        this.sessionProyectosDirector = sessionProyectosDirector;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

   

    public List<DirectorProyecto> getDirectorProyectos() {
        return directorProyectos;
    }

    public void setDirectorProyectos(List<DirectorProyecto> directorProyectos) {
        this.directorProyectos = directorProyectos;
    }

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

//    public AutorProyectoPostulacionController getAdministrarAutoresProyecto() {
//        return administrarAutoresProyecto;
//    }
//
//    public void setAdministrarAutoresProyecto(AutorProyectoPostulacionController administrarAutoresProyecto) {
//        this.administrarAutoresProyecto = administrarAutoresProyecto;
//    }

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

    public AdministrarInformesProyecto getAdministrarInformesProyecto() {
        return administrarInformesProyecto;
    }

    public void setAdministrarInformesProyecto(AdministrarInformesProyecto administrarInformesProyecto) {
        this.administrarInformesProyecto = administrarInformesProyecto;
    }

  
//</editor-fold>
}
