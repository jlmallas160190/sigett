/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.finalizacion.controlador;

import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.dao.PersonaDao;
import edu.unl.sigett.finalizacion.managed.session.SessionInformeProyecto;
import edu.unl.sigett.reportes.AdministrarReportes;
import edu.unl.sigett.entity.AutorProyecto;
import edu.jlmallas.academico.entity.Carrera;
import edu.unl.sigett.entity.CatalogoInformeProyecto;
import edu.unl.sigett.entity.DirectorProyecto;
import edu.unl.sigett.entity.InformeProyecto;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.entity.ProyectoCarreraOferta;
import org.jlmallas.seguridad.entity.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.context.RequestContext;
import edu.unl.sigett.dao.AutorProyectoDao;
import edu.jlmallas.academico.service.CarreraService;
import edu.unl.sigett.dao.CatalogoInformeProyectoFacadeLocal;
import edu.unl.sigett.dao.ConfiguracionGeneralDao;
import edu.unl.sigett.dao.DirectorProyectoFacadeLocal;
import edu.unl.sigett.dao.InformeProyectoFacadeLocal;
import org.jlmallas.seguridad.dao.LogDao;
import edu.unl.sigett.dao.ProyectoDao;
import org.jlmallas.seguridad.dao.UsuarioDao;
import edu.jlmallas.academico.entity.Docente;
import edu.jlmallas.academico.entity.EstudianteCarrera;
import edu.jlmallas.academico.dao.DocenteCarreraDao;
import edu.jlmallas.academico.dao.EstudianteCarreraDao;
import edu.unl.sigett.enumeration.EstadoAutorEnum;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class AdministrarInformesProyecto implements Serializable {

    @Inject
    private SessionInformeProyecto sessionInformeProyecto;

    @EJB
    private UsuarioDao usuarioFacadeLocal;
    @EJB
    private CatalogoInformeProyectoFacadeLocal catalogoInformeProyectoFacadeLocal;
    @EJB
    private InformeProyectoFacadeLocal informeProyectoFacadeLocal;
    @EJB
    private LogDao logFacadeLocal;
    @EJB
    private AutorProyectoDao autorProyectoFacadeLocal;
    @EJB
    private ProyectoDao proyectoFacadeLocal;
    @EJB
    private CarreraService carreraFacadeLocal;
    @EJB
    private DirectorProyectoFacadeLocal directorProyectoFacadeLocal;
    @EJB
    private ConfiguracionGeneralDao configuracionGeneralFacadeLocal;
    @EJB
    private EstudianteCarreraDao estudianteCarreraFacadeLocal;
    @EJB
    private PersonaDao personaFacadeLocal;
    @EJB
    private DocenteCarreraDao docenteCarreraFacadeLocal;

    private List<InformeProyecto> informeProyectos;

    private String catalogoInformeProyecto;
    private String criterio;
    private Integer carreraId;
    private Long dpId;
    private String lenguaje;

    private boolean renderedEditar;
    private boolean renderedNoEditar;
    private boolean renderedEliminar;
    private boolean renderedCrear;
    private boolean renderedDlgEditar;
    private boolean renderedDlgCertificado;

    public AdministrarInformesProyecto() {
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
    public void renderedCrear(Usuario usuario, Proyecto proyecto) {
//        if (proyecto.getEstadoProyectoId().getId() == 3 || proyecto.getEstadoProyectoId().getId() == 4) {
//            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_informe_proyecto");
//            if (tienePermiso == 1) {
//                renderedCrear = true;
//            } else {
//                renderedCrear = false;
//            }
//        } else {
//            renderedCrear = false;
//        }
    }

    public void renderedEditar(Usuario usuario, Proyecto proyecto) {
//        if (proyecto.getEstadoProyectoId().getId() == 3 || proyecto.getEstadoProyectoId().getId() == 4) {
//            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_informe_proyecto");
//            if (tienePermiso == 1) {
//                renderedEditar = true;
//                renderedNoEditar = false;
//            } else {
//                renderedNoEditar = true;
//                renderedEditar = false;
//            }
//        } else {
//            renderedNoEditar = true;
//            renderedEditar = false;
//        }
    }

    public void renderedEliminar(Usuario usuario, Proyecto proyecto) {
//        if (proyecto.getEstadoProyectoId().getId() == 3 || proyecto.getEstadoProyectoId().getId() == 4) {
//            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_informe_proyecto");
//            if (tienePermiso == 1) {
//                renderedEliminar = true;
//            } else {
//                renderedEliminar = false;
//            }
//        } else {
//            renderedEliminar = false;
//        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">

    public String crear(Usuario usuario, Proyecto proyecto) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
//            if (proyecto.getEstadoProyectoId().getId() == 3 || proyecto.getEstadoProyectoId().getId() == 4) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_informe_proyecto");
//                if (tienePermiso == 1) {
//                    sessionInformeProyecto.setInformeProyecto(new InformeProyecto());
//                    if (param.equals("crear")) {
//                        navegacion = "editarInformeProyecto?faces-redirect=true";
//                    } else {
//                        if (param.equals("crear-dlg")) {
//                            renderedDlgEditar = true;
//                            RequestContext.getCurrentInstance().execute("PF('dlgEditarInformeProyecto').show()");
//                        }
//                    }
//                } else {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear" + ". " + bundle.getString("lbl.msm_consulte")), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
//            }

        } catch (Exception e) {
        }
        return navegacion;
    }

    public void imprimirCertificacion(DirectorProyecto directorProyecto) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            lenguaje = bundle.getLocale().getLanguage();
            dpId = directorProyecto.getId();
            for (ProyectoCarreraOferta pco : directorProyecto.getProyectoId().getProyectoCarreraOfertaList()) {
                if (pco.getEsActivo()) {
                    carreraId = pco.getCarreraId();
                    break;
                }
            }
            renderedDlgCertificado = true;
            RequestContext.getCurrentInstance().execute("PF('dlgCertificacion').show()");
        } catch (Exception e) {
        }
    }

    public void download() {
        try {
            Map datosReporte = new HashMap();
            AdministrarReportes reportes = new AdministrarReportes();
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
            String path = request.getRealPath("/");
            String pathSetting = request.getRealPath("/settings.txt");
            Carrera carrera = carreraFacadeLocal.find(carreraId);
            DirectorProyecto directorProyecto = directorProyectoFacadeLocal.find(dpId);
            Docente docente = docenteCarreraFacadeLocal.find(directorProyecto.getId()).getDocenteId();
            Persona datosDirector = personaFacadeLocal.find(docente.getId());
            Calendar fechaActual1 = Calendar.getInstance();
            String fechaFormateada1 = configuracionGeneralFacadeLocal.dateFormat(fechaActual1.getTime());
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            datosReporte.put("tituloDirector", datosDirector.getNombres().toUpperCase() + " " + datosDirector.getApellidos().toUpperCase());
            datosReporte.put("razon", bundle.getString("lbl.razon_certificacion_director"));
            datosReporte.put("autores", getAutores(autorProyectoFacadeLocal.buscarPorProyecto(directorProyecto.getProyectoId().getId())));
            reportes.certificacionDirector("docx", fechaFormateada1, response, datosReporte, carrera, path, pathSetting);
        } catch (Exception e) {

        }
    }

    private String getAutores(List<AutorProyecto> autorProyectos) {
        String datosAutores = "";
        int cont = 0;
        try {
            for (AutorProyecto autorProyecto : autorProyectos) {
//                if (!autorProyecto.getEstadoAutorId().getCodigo().equalsIgnoreCase(EstadoAutorEnum.ABANDONADO.getTipo())) {
//                    EstudianteCarrera estudianteCarrera = estudianteCarreraFacadeLocal.find(autorProyecto.getAspiranteId().getId());
//                    Persona datosAutor = personaFacadeLocal.find(estudianteCarrera.getEstudianteId().getId());
//                    if (cont == 0) {
////                        datosAutores += "" + estudianteCarrera.getEstadoId().getNombre() + " " + datosAutor.getNombres().toUpperCase() + " " + datosAutor.getApellidos().toUpperCase() + "";
//                        cont++;
//                    } else {
////                        datosAutores += ", " + estudianteCarrera.getEstadoId().getNombre() + " " + datosAutor.getNombres().toUpperCase() + " " + datosAutor.getApellidos().toUpperCase();
//                        cont++;
//                    }
//                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return datosAutores;
    }

    public String editar(Usuario usuario, InformeProyecto informeProyecto, Proyecto proyecto) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
//            if (proyecto.getEstadoProyectoId().getId() == 3 || proyecto.getEstadoProyectoId().getId() == 4) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_informe_proyecto");
//                if (tienePermiso == 1) {
//                    sessionInformeProyecto.setInformeProyecto(informeProyecto);
//                    catalogoInformeProyecto = informeProyecto.getCatalogoInformeProyectoId().toString();
//                    if (param.equals("editar")) {
//                        navegacion = "editarInformeProyecto?faces-redirect=true";
//                    } else {
//                        if (param.equals("editar-dlg")) {
//                            renderedDlgEditar = true;
//                            RequestContext.getCurrentInstance().execute("PF('dlgEditarInformeProyecto').show()");
//                        }
//                    }
//                } else {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar" + ". " + bundle.getString("lbl.msm_consulte")), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
//            }

        } catch (Exception e) {
        }
        return navegacion;
    }

    public void seleccionaCatalogoInforme(InformeProyecto informeProyecto) {
        int pos = catalogoInformeProyecto.indexOf(":");
        CatalogoInformeProyecto cp = catalogoInformeProyectoFacadeLocal.find(Integer.parseInt(catalogoInformeProyecto.substring(0, pos)));
        if (cp != null) {
            informeProyecto.setCatalogoInformeProyectoId(cp);
        }
    }

    public String grabar(InformeProyecto informe, Proyecto proyecto, Usuario usuario) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
//            Calendar fechaActual = Calendar.getInstance();
//            seleccionaCatalogoInforme(informe);
//            informe.setProyectoId(proyecto);
//            /*SI tipo de informe es autorizacion de sustentacion privada*/
//            if (informe.getCatalogoInformeProyectoId().getId() == 1) {
//                EstadoProyecto estadoProyecto = estadoProyectoFacadeLocal.find(4);
//                proyecto.setEstadoProyectoId(estadoProyecto);
//            }
//            if (informe.getId() == null) {
//                if (proyecto.getEstadoProyectoId().getId() == 3 || proyecto.getEstadoProyectoId().getId() == 4) {
//                    int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_informe_proyecto");
//                    if (tienePermiso == 1) {
//                        informe.setFecha(fechaActual.getTime());
//                        informeProyectoFacadeLocal.create(informe);
//                        logFacadeLocal.create(logFacadeLocal.crearLog("InformeProyecto", informe.getId() + "", "CREAR", "|Observacion= " + informe.getObservacion() + "|Catalogo= " + informe.getCatalogoInformeProyectoId() + "|Proyecto= " + informe.getProyectoId().getId(), usuario));
//                        proyectoFacadeLocal.edit(proyecto);
//                        actualizaEstadoAutores(proyecto);
//                        if (param.equalsIgnoreCase("grabar-dlg")) {
//                            RequestContext.getCurrentInstance().execute("PF('dlgEditarInformeProyecto').hide()");
//                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
//                            FacesContext.getCurrentInstance().addMessage(null, message);
//                            sessionInformeProyecto.setInformeProyecto(new InformeProyecto());
//                        } else {
//                            if (param.equalsIgnoreCase("grabar-editar-dlg")) {
//                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
//                                FacesContext.getCurrentInstance().addMessage(null, message);
//                            }
//                        }
//                        buscar("", proyecto, usuario);
//
//                    } else {
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                    }
//                } else {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
//            } else {
//                if (proyecto.getEstadoProyectoId().getId() == 3 || proyecto.getEstadoProyectoId().getId() == 4) {
//                    int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_informe_proyecto");
//                    if (tienePermiso == 1) {
//                        informe.setFecha(fechaActual.getTime());
//                        informeProyectoFacadeLocal.create(informe);
//                        logFacadeLocal.create(logFacadeLocal.crearLog("InformeProyecto", informe.getId() + "", "EDITAR", "|Observacion= " + informe.getObservacion() + "|Catalogo= " + informe.getCatalogoInformeProyectoId() + "|Proyecto= " + informe.getProyectoId().getId(), usuario));
//                        proyectoFacadeLocal.edit(proyecto);
//                        actualizaEstadoAutores(proyecto);
//                        if (param.equalsIgnoreCase("grabar-dlg")) {
//                            RequestContext.getCurrentInstance().execute("PF('dlgEditarInformeProyecto').hide()");
//                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
//                            FacesContext.getCurrentInstance().addMessage(null, message);
//                            sessionInformeProyecto.setInformeProyecto(new InformeProyecto());
//                        } else {
//                            if (param.equalsIgnoreCase("grabar-editar-dlg")) {
//                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
//                                FacesContext.getCurrentInstance().addMessage(null, message);
//                            }
//                        }
//                        buscar("", proyecto, usuario);
//                    } else {
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                    }
//                } else {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
//            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return navegacion;
    }

    public void actualizaEstadoAutores(Proyecto proyecto) {
        try {
//            EstadoAutor ea = estadoAutorFacadeLocal.find(4);
//            EstadoAutor eas = estadoAutorFacadeLocal.find(3);
//            for (AutorProyecto autorProyecto : autorProyectoFacadeLocal.buscarPorProyecto(proyecto.getId())) {
//                if (autorProyecto.getEstadoAutorId().getId() != 10) {
//                    if (proyecto.getEstadoProyectoId().getId() != 10) {
//                        if (ea != null) {
//                            autorProyecto.setEstadoAutorId(ea);
//                            autorProyectoFacadeLocal.edit(autorProyecto);
//                        }
//                    } else {
//                        if (ea != null) {
//                            autorProyecto.setEstadoAutorId(eas);
//                            autorProyectoFacadeLocal.edit(autorProyecto);
//                        }
//                    }
//                }
//            }
        } catch (Exception e) {
        }
    }

    public void buscar(String criterio, Proyecto proyecto, Usuario usuario) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            this.informeProyectos = new ArrayList<>();
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_informe_proyecto");
            if (tienePermiso == 1) {
                for (InformeProyecto informeProyecto : informeProyectoFacadeLocal.buscarPorProyecto(proyecto.getId())) {
                    if (informeProyecto.getCatalogoInformeProyectoId().getNombre().toUpperCase().contains(criterio.toUpperCase())) {
                        informeProyectos.add(informeProyecto);
                    }
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
    }

    public void remover(InformeProyecto informe, Usuario usuario, Proyecto proyecto) {
        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            if (proyecto.getEstadoProyectoId().getId() == 3 || proyecto.getEstadoProyectoId().getId() == 4) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_informe_proyecto");
//                if (tienePermiso == 1) {
//                    logFacadeLocal.create(logFacadeLocal.crearLog("InformeProyecto", informe.getId() + "", "ELIMINAR", "|Observacion= " + informe.getObservacion() + "|Catalogo= " + informe.getCatalogoInformeProyectoId() + "|Proyecto= " + informe.getProyectoId().getId(), usuario));
//                    informeProyectoFacadeLocal.remove(informe);
//                    if (existeInformeAutorizacionSp(proyecto) == false) {
//                        EstadoProyecto estadoProyecto = estadoProyectoFacadeLocal.find(3);
//                        proyecto.setEstadoProyectoId(estadoProyecto);
//                        proyectoFacadeLocal.edit(proyecto);
//                        actualizaEstadoAutores(proyecto);
//                    }
//                    buscar("", proyecto, usuario);
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_eliminar"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                } else {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_eliminar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
//            } else {
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_eliminar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
        } catch (Exception e) {
        }
    }

    public boolean existeInformeAutorizacionSp(Proyecto proyecto) {
        boolean var = false;
        for (InformeProyecto informeProyecto : informeProyectoFacadeLocal.buscarPorProyecto(proyecto.getId())) {
            if (informeProyecto.getCatalogoInformeProyectoId().getId() == 1) {
                var = true;
                break;
            }
        }
        return var;
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
    public String getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
    }

    public Integer getCarreraId() {
        return carreraId;
    }

    public void setCarreraId(Integer carreraId) {
        this.carreraId = carreraId;
    }

    public Long getDpId() {
        return dpId;
    }

    public void setDpId(Long dpId) {
        this.dpId = dpId;
    }

    public boolean isRenderedDlgCertificado() {
        return renderedDlgCertificado;
    }

    public void setRenderedDlgCertificado(boolean renderedDlgCertificado) {
        this.renderedDlgCertificado = renderedDlgCertificado;
    }

    public boolean isRenderedDlgEditar() {
        return renderedDlgEditar;
    }

    public void setRenderedDlgEditar(boolean renderedDlgEditar) {
        this.renderedDlgEditar = renderedDlgEditar;
    }

    public SessionInformeProyecto getSessionInformeProyecto() {
        return sessionInformeProyecto;
    }

    public void setSessionInformeProyecto(SessionInformeProyecto sessionInformeProyecto) {
        this.sessionInformeProyecto = sessionInformeProyecto;
    }

    public String getCatalogoInformeProyecto() {
        return catalogoInformeProyecto;
    }

    public void setCatalogoInformeProyecto(String catalogoInformeProyecto) {
        this.catalogoInformeProyecto = catalogoInformeProyecto;
    }

    public List<InformeProyecto> getInformeProyectos() {
        return informeProyectos;
    }

    public void setInformeProyectos(List<InformeProyecto> informeProyectos) {
        this.informeProyectos = informeProyectos;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public boolean isRenderedEditar() {
        return renderedEditar;
    }

    public void setRenderedEditar(boolean renderedEditar) {
        this.renderedEditar = renderedEditar;
    }

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

    public boolean isRenderedEliminar() {
        return renderedEliminar;
    }

    public void setRenderedEliminar(boolean renderedEliminar) {
        this.renderedEliminar = renderedEliminar;
    }

    public boolean isRenderedCrear() {
        return renderedCrear;
    }

    public void setRenderedCrear(boolean renderedCrear) {
        this.renderedCrear = renderedCrear;
    }
//</editor-fold>
}
