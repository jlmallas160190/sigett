/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.finalizacion.controlador;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.finalizacion.managed.session.SessionTribunal;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.entity.AutorProyecto;
import edu.jlmallas.academico.entity.Docente;
import edu.unl.sigett.entity.EvaluacionTribunal;
import edu.unl.sigett.entity.MiembroTribunal;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.entity.Tribunal;
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
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import edu.unl.sigett.dao.AutorProyectoDao;
import org.jlmallas.seguridad.dao.LogDao;
import edu.unl.sigett.dao.MiembroFacadeLocal;
import edu.unl.sigett.dao.TribunalFacadeLocal;
import org.jlmallas.seguridad.dao.UsuarioDao;
import edu.jlmallas.academico.entity.EstudianteCarrera;
import edu.jlmallas.academico.enumeration.EstadoEstudianteCarreraEnum;
import edu.jlmallas.academico.dao.EstudianteCarreraDao;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "tribunalesDocente",
            pattern = "/buscarTribunalesDocente/",
            viewId = "/faces/pages/sigett/buscarTribunalesDocente.xhtml"
    )
})
public class AdministrarTribunales implements Serializable {

//    @Inject
//    private SessionTribunal sessionTribunal;
//    @Inject
//    private AdministrarEvaluacionesTribunal administrarEvaluacionesTribunal;
//    @Inject
//    private AdministrarMiembrosTribunal administrarMiembrosTribunal;
//    @Inject
//    private SessionUsuario sessionUsuario;
//    @Inject
//    private AdministrarActas administrarActas;
//
//    @EJB
//    private TribunalFacadeLocal tribunalFacadeLocal;
//    @EJB
//    private UsuarioDao usuarioFacadeLocal;
//    @EJB
//    private LogDao logFacadeLocal;
//    @EJB
//    private AutorProyectoDao autorProyectoFacadeLocal;
//    @EJB
//    private MiembroFacadeLocal miembroFacadeLocal;
//    @EJB
//    private EstudianteCarreraDao estudianteCarreraFacadeLocal;
//    private List<Tribunal> tribunales;
//    private List<Tribunal> tribunalesPorDocente;
//
//    private boolean renderedEditar;
//    private boolean renderedDlgEditar;
//    private boolean renderedNoEditar;
//    private boolean renderedEliminar;
//    private boolean renderedCrear;
//    private boolean renderedConsultaTribunal;
//    private boolean renderedBuscar;
//    private boolean renderedTribunal;
//    private boolean renderedViewTribunal;
//
//    private String criterioPorDocente;
//
//    public AdministrarTribunales() {
//    }
//
//    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
//    public void renderedCrear(Usuario usuario, Proyecto proyecto) {
////        FacesContext facesContext = FacesContext.getCurrentInstance();
////        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
////        if (comprobarAutoresAptosSustentacion(proyecto)) {
////            if (proyecto.getEstadoProyectoId().getId() == 4 || proyecto.getEstadoProyectoId().getId() == 5 || proyecto.getEstadoProyectoId().getId() == 6 || proyecto.getEstadoProyectoId().getId() == 7) {
////                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_tribunal");
////                if (tienePermiso == 1) {
////                    renderedCrear = true;
////                } else {
////                    renderedCrear = false;
////                }
////            } else {
////                renderedCrear = false;
////            }
////        } else {
////            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.autor_no_apto") + ". " + bundle.getString("lbl.msm_consulte"), "");
////            FacesContext.getCurrentInstance().addMessage(null, message);
////            renderedCrear = false;
////        }
//
//    }
//
//    public boolean comprobarTribunalesActivos(Proyecto proyecto) {
//        boolean var = false;
//        try {
//            for (Tribunal tribunal : tribunalFacadeLocal.buscarPorProyecto(proyecto.getId())) {
//                if (tribunal.getEsActivo()) {
//                    var = true;
//                    break;
//                }
//            }
//        } catch (Exception e) {
//        }
//        return var;
//    }
//
//    public void renderedBuscar(Usuario usuario, Proyecto proyecto) {
////        if (proyecto.getEstadoProyectoId().getId() == 4 || proyecto.getEstadoProyectoId().getId() == 5 || proyecto.getEstadoProyectoId().getId() == 6 || proyecto.getEstadoProyectoId().getId() == 7 || proyecto.getEstadoProyectoId().getId() == 8 || proyecto.getEstadoProyectoId().getId() == 9) {
////            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_tribunal");
////            if (tienePermiso == 1) {
////                renderedBuscar = true;
////            } else {
////                renderedBuscar = false;
////            }
////        } else {
////            renderedBuscar = false;
////        }
//    }
//
//    public void renderedEditar(Usuario usuario, Proyecto proyecto) {
////        if (proyecto.getEstadoProyectoId().getId() == 4 || proyecto.getEstadoProyectoId().getId() == 5 || proyecto.getEstadoProyectoId().getId() == 6 || proyecto.getEstadoProyectoId().getId() == 7) {
////            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_tribunal");
////            if (tienePermiso == 1) {
////                renderedEditar = true;
////                renderedNoEditar = false;
////            } else {
////                renderedNoEditar = true;
////                renderedEditar = false;
////            }
////        } else {
////            renderedNoEditar = true;
////            renderedEditar = false;
////        }
//    }
//
//    public void renderedEliminar(Usuario usuario, Proyecto proyecto) {
////        if (proyecto.getEstadoProyectoId().getId() == 4 || proyecto.getEstadoProyectoId().getId() == 5 || proyecto.getEstadoProyectoId().getId() == 6 || proyecto.getEstadoProyectoId().getId() == 7) {
////            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_tribunal");
////            if (tienePermiso == 1) {
////                renderedEliminar = true;
////            } else {
////                renderedEliminar = false;
////            }
////        } else {
////            renderedEliminar = false;
////        }
//    }
////</editor-fold>
//    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
//
//    public String crear(Usuario usuario, Proyecto proyecto) {
//        String navegacion = "";
////        FacesContext facesContext = FacesContext.getCurrentInstance();
////        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
////        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
////        try {
////            if (comprobarAutoresAptosSustentacion(proyecto)) {
////                if (comprobarTribunalesActivos(proyecto) == false) {
////                    if (proyecto.getEstadoProyectoId().getId() == 4 || proyecto.getEstadoProyectoId().getId() == 5 || proyecto.getEstadoProyectoId().getId() == 6 || proyecto.getEstadoProyectoId().getId() == 7) {
////                        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_tribunal");
////                        if (tienePermiso == 1) {
////                            sessionTribunal.setTribunal(new Tribunal());
////                            sessionTribunal.getTribunal().setProyectoId(proyecto);
////                            if (param.equals("crear-dlg")) {
////                                renderedDlgEditar = true;
////                                RequestContext.getCurrentInstance().execute("PF('dlgEditarTribunal').show()");
////                            }
////                        } else {
////                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
////                            FacesContext.getCurrentInstance().addMessage(null, message);
////                        }
////                    } else {
////                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
////                        FacesContext.getCurrentInstance().addMessage(null, message);
////                    }
////                } else {
////                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
////                    FacesContext.getCurrentInstance().addMessage(null, message);
////                }
////            } else {
////                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.autor_no_apto") + ". " + bundle.getString("lbl.msm_consulte"), "");
////                FacesContext.getCurrentInstance().addMessage(null, message);
////            }
////        } catch (Exception e) {
////        }
//        return navegacion;
//    }
//
//    public String editar(Usuario usuario, Tribunal tribunal, Proyecto proyecto) {
//        String navegacion = "";
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
//        try {
////            if (comprobarAutoresAptosSustentacion(proyecto)) {
////                if (proyecto.getEstadoProyectoId().getId() == 4 || proyecto.getEstadoProyectoId().getId() == 5 || proyecto.getEstadoProyectoId().getId() == 6 || proyecto.getEstadoProyectoId().getId() == 7) {
////                    int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_tribunal");
////                    if (tienePermiso == 1) {
////                        sessionTribunal.setTribunal(tribunal);
////                        administrarMiembrosTribunal.buscar(sessionTribunal.getTribunal(), usuario, "");
////                        if (param.equals("editar-dlg")) {
////                            renderedDlgEditar = true;
////                            RequestContext.getCurrentInstance().execute("PF('dlgEditarTribunal').show()");
////                        }
////                    } else {
////                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
////                        FacesContext.getCurrentInstance().addMessage(null, message);
////                    }
////                } else {
////                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
////                    FacesContext.getCurrentInstance().addMessage(null, message);
////                }
////            } else {
////                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.autor_no_apto") + ". " + bundle.getString("lbl.msm_consulte"), "");
////                FacesContext.getCurrentInstance().addMessage(null, message);
////            }
//        } catch (Exception e) {
//        }
//        return navegacion;
//    }
//
//    public String grabar(Tribunal tribunal, Proyecto proyecto, Usuario usuario) {
//        String navegacion = "";
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
//        try {
////            if (comprobarAutoresAptosSustentacion(proyecto)) {
////                tribunal.setProyectoId(proyecto);
////                List<EvaluacionTribunal> evaluacionTribunals = new ArrayList<>();
////                evaluacionTribunals.addAll(tribunal.getEvaluacionTribunalList());
////                tribunal.setEvaluacionTribunalList(new ArrayList<EvaluacionTribunal>());
////                List<Miembro> miembros = new ArrayList<>();
////                miembros.addAll(tribunal.getMiembroList());
////                tribunal.setMiembroList(new ArrayList<Miembro>());
////                if (tribunal.getId() == null) {
////                    if (proyecto.getEstadoProyectoId().getId() == 4 || proyecto.getEstadoProyectoId().getId() == 5 || proyecto.getEstadoProyectoId().getId() == 6 || proyecto.getEstadoProyectoId().getId() == 7) {
////                        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_tribunal");
////                        if (tienePermiso == 1) {
////                            tribunalFacadeLocal.create(tribunal);
////                            tribunal.setEvaluacionTribunalList(evaluacionTribunals);
////                            tribunal.setMiembroList(miembros);
////                            renderedCrear(usuario, proyecto);
////                            logFacadeLocal.create(logFacadeLocal.crearLog("Tribunal", tribunal.getId() + "", "CREAR", "|Descripcion " + tribunal.getDescripcion() + "|Es Activo= " + tribunal.getEsActivo() + "|Proyecto= " + tribunal.getProyectoId().getId(), usuario));
////                            if (param.equalsIgnoreCase("grabar-dlg")) {
////                                RequestContext.getCurrentInstance().execute("PF('dlgEditarTribunal').hide()");
////                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
////                                FacesContext.getCurrentInstance().addMessage(null, message);
////                                sessionTribunal.setTribunal(new Tribunal());
////                            } else {
////                                if (param.equalsIgnoreCase("grabar-editar-dlg")) {
////                                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
////                                    FacesContext.getCurrentInstance().addMessage(null, message);
////                                }
////                            }
////                            buscar(proyecto, usuario);
////                        } else {
////                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
////                            FacesContext.getCurrentInstance().addMessage(null, message);
////                        }
////                    } else {
////                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
////                        FacesContext.getCurrentInstance().addMessage(null, message);
////                    }
////                } else {
////                    if (proyecto.getEstadoProyectoId().getId() == 4 || proyecto.getEstadoProyectoId().getId() == 5 || proyecto.getEstadoProyectoId().getId() == 6 || proyecto.getEstadoProyectoId().getId() == 7) {
////                        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_tribunal");
////                        if (tienePermiso == 1) {
////                            tribunalFacadeLocal.edit(tribunal);
////                            tribunal.setEvaluacionTribunalList(evaluacionTribunals);
////                            tribunal.setMiembroList(miembros);
////                            logFacadeLocal.create(logFacadeLocal.crearLog("Tribunal", tribunal.getId() + "", "EDITAR", "|Descripcion " + tribunal.getDescripcion() + "|Es Activo= " + tribunal.getEsActivo() + "|Proyecto= " + tribunal.getProyectoId().getId(), usuario));
////                            if (param.equalsIgnoreCase("grabar-dlg")) {
////                                RequestContext.getCurrentInstance().execute("PF('dlgEditarTribunal').hide()");
////                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
////                                FacesContext.getCurrentInstance().addMessage(null, message);
////                                sessionTribunal.setTribunal(new Tribunal());
////                            } else {
////                                if (param.equalsIgnoreCase("grabar-editar-dlg")) {
////                                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
////                                    FacesContext.getCurrentInstance().addMessage(null, message);
////                                }
////                            }
////                            buscar(proyecto, usuario);
////                        } else {
////                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
////                            FacesContext.getCurrentInstance().addMessage(null, message);
////                        }
////                    } else {
////                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
////                        FacesContext.getCurrentInstance().addMessage(null, message);
////                    }
////                }
////            } else {
////                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.autor_no_apto") + ". " + bundle.getString("lbl.msm_consulte"), "");
////                FacesContext.getCurrentInstance().addMessage(null, message);
////            }
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return navegacion;
//    }
//
//    public boolean comprobarAutoresAptosSustentacion(Proyecto proyecto) {
//        boolean var = false;
//        try {
////            for (AutorProyecto autorProyecto : autorProyectoFacadeLocal.buscarPorProyecto(proyecto.getId())) {
////                EstudianteCarrera estudianteCarrera = estudianteCarreraFacadeLocal.find(autorProyecto.getAspiranteId().getId());
//////                if (autorProyecto.getEstadoAutorId().getId() != 10) {
//////                    if (estudianteCarrera.getEstadoId().getCodigo().equalsIgnoreCase(EstadoEstudianteCarreraEnum.EGRESADO.getTipo())) {
//////                        var = true;
//////                    } else {
//////                        var = false;
//////                        break;
//////                    }
//////                }
////            }
//        } catch (Exception e) {
//        }
//        return var;
//    }
//
//    public void remover(Tribunal tribunal, Usuario usuario, Proyecto proyecto) {
//        try {
////            FacesContext facesContext = FacesContext.getCurrentInstance();
////            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
////            if (proyecto.getEstadoProyectoId().getId() == 4 || proyecto.getEstadoProyectoId().getId() == 5 || proyecto.getEstadoProyectoId().getId() == 6 || proyecto.getEstadoProyectoId().getId() == 7) {
////                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_tribunal");
////                if (tienePermiso == 1) {
////                    tribunal.setEsActivo(false);
////                    tribunalFacadeLocal.edit(tribunal);
////                    logFacadeLocal.create(logFacadeLocal.crearLog("Tribunal", tribunal.getId() + "", "EDITAR", "|Descripcion " + tribunal.getDescripcion() + "|Es Activo= " + tribunal.getEsActivo() + "|Proyecto= " + tribunal.getProyectoId().getId(), usuario));
////                    buscar(proyecto, usuario);
////                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_eliminar"), "");
////                    FacesContext.getCurrentInstance().addMessage(null, message);
////                }
////            } else {
////                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_eliminar") + ". " + bundle.getString("lbl.msm_consulte"), "");
////                FacesContext.getCurrentInstance().addMessage(null, message);
////            }
//        } catch (Exception e) {
//        }
//    }
//
//    public void buscarPorDocente(Long docenteId, String criterio) {
//        try {
//            tribunalesPorDocente = new ArrayList<>();
//            for (MiembroTribunal miembro : miembroFacadeLocal.buscarPorDocente(docenteId)) {
//                if (miembro.getTribunalId().getProyectoId().getTemaActual().toLowerCase().contains(criterio.toLowerCase())) {
//                    tribunalesPorDocente.add(miembro.getTribunalId());
//                }
//            }
//        } catch (Exception e) {
//        }
//    }
//
//    public String viewBuscarTribunalesDocente(Docente docente) {
//        String navegacion = "";
//        try {
//            administrarEvaluacionesTribunal.renderedAceptarNota(docente);
//            administrarActas.setRenderedDlgActaPrivada(false);
//            navegacion = "pretty:tribunalesDocente";
//        } catch (Exception e) {
//        }
//        return navegacion;
//    }
//
//    public void buscar(Proyecto proyecto, Usuario usuario) {
//        try {
////            FacesContext facesContext = FacesContext.getCurrentInstance();
////            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
////            if (proyecto.getEstadoProyectoId().getId() == 4 || proyecto.getEstadoProyectoId().getId() == 5 || proyecto.getEstadoProyectoId().getId() == 6 || proyecto.getEstadoProyectoId().getId() == 7 || proyecto.getEstadoProyectoId().getId() == 8 || proyecto.getEstadoProyectoId().getId() == 9) {
////                this.tribunales = new ArrayList<>();
////                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_tribunal");
////                if (tienePermiso == 1) {
////                    tribunales = tribunalFacadeLocal.buscarPorProyecto(proyecto.getId());
////                    for (Tribunal tr : tribunales) {
////                        administrarMiembrosTribunal.buscar(tr, usuario, "");
////                        administrarEvaluacionesTribunal.buscar(tr, usuario);
////                    }
////                    administrarEvaluacionesTribunal.listadoSustentacionesPorUsuarioCarrera(sessionUsuario.getUsuario(), proyecto);
////                } else {
////                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
////                    FacesContext.getCurrentInstance().addMessage(null, message);
////                }
////            } else {
////                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
////                FacesContext.getCurrentInstance().addMessage(null, message);
////            }
//        } catch (Exception e) {
//        }
//    }
//
//    public void onTabChange(TabChangeEvent event) {
//        if (event.getTab().getId().equals("tabEvaluaciones")) {
//            administrarEvaluacionesTribunal.renderedCrear(sessionUsuario.getUsuario());
//            administrarEvaluacionesTribunal.renderedEditar(sessionUsuario.getUsuario());
//            administrarEvaluacionesTribunal.renderedEliminar(sessionUsuario.getUsuario());
//            administrarEvaluacionesTribunal.buscar(sessionTribunal.getTribunal(), sessionUsuario.getUsuario());
//            administrarMiembrosTribunal.setRenderedDlgDocentesDisponibles(false);
//            administrarMiembrosTribunal.setRenderedDlgEditar(false);
//            administrarMiembrosTribunal.renderedImprimirOficio(sessionUsuario.getUsuario(), sessionTribunal.getTribunal().getProyectoId());
//            administrarMiembrosTribunal.setRenderedDlgOficio(false);
//            administrarEvaluacionesTribunal.setRenderedDlgEditar(false);
//        } else {
//            if (event.getTab().getId().equals("tabMiembros")) {
//                administrarMiembrosTribunal.setRenderedDlgDocentesDisponibles(false);
//                administrarMiembrosTribunal.setRenderedDlgEditar(false);
//                administrarEvaluacionesTribunal.setRenderedDlgEditar(false);
//                administrarMiembrosTribunal.renderedImprimirOficio(sessionUsuario.getUsuario(), sessionTribunal.getTribunal().getProyectoId());
//                administrarMiembrosTribunal.setRenderedDlgOficio(false);
//                administrarMiembrosTribunal.renderedCrear(sessionUsuario.getUsuario(), sessionTribunal.getTribunal().getProyectoId());
//                administrarMiembrosTribunal.renderedEditar(sessionUsuario.getUsuario(), sessionTribunal.getTribunal().getProyectoId());
//                administrarMiembrosTribunal.renderedEliminar(sessionUsuario.getUsuario(), sessionTribunal.getTribunal().getProyectoId());
//                administrarMiembrosTribunal.buscar(sessionTribunal.getTribunal(), sessionUsuario.getUsuario(), administrarMiembrosTribunal.getCriterio());
//            }
//        }
//    }
//
////</editor-fold>
//    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
//    public String getCriterioPorDocente() {
//        return criterioPorDocente;
//    }
//
//    public void setCriterioPorDocente(String criterioPorDocente) {
//        this.criterioPorDocente = criterioPorDocente;
//    }
//
//    public List<Tribunal> getTribunalesPorDocente() {
//        return tribunalesPorDocente;
//    }
//
//    public void setTribunalesPorDocente(List<Tribunal> tribunalesPorDocente) {
//        this.tribunalesPorDocente = tribunalesPorDocente;
//    }
//
//    public boolean isRenderedViewTribunal() {
//        return renderedViewTribunal;
//    }
//
//    public void setRenderedViewTribunal(boolean renderedViewTribunal) {
//        this.renderedViewTribunal = renderedViewTribunal;
//    }
//
//    public SessionTribunal getSessionTribunal() {
//        return sessionTribunal;
//    }
//
//    public void setSessionTribunal(SessionTribunal sessionTribunal) {
//        this.sessionTribunal = sessionTribunal;
//    }
//
//    public boolean isRenderedNoEditar() {
//        return renderedNoEditar;
//    }
//
//    public void setRenderedNoEditar(boolean renderedNoEditar) {
//        this.renderedNoEditar = renderedNoEditar;
//    }
//
//    public boolean isRenderedEliminar() {
//        return renderedEliminar;
//    }
//
//    public void setRenderedEliminar(boolean renderedEliminar) {
//        this.renderedEliminar = renderedEliminar;
//    }
//
//    public boolean isRenderedCrear() {
//        return renderedCrear;
//    }
//
//    public void setRenderedCrear(boolean renderedCrear) {
//        this.renderedCrear = renderedCrear;
//    }
//
//    public List<Tribunal> getTribunales() {
//        return tribunales;
//    }
//
//    public void setTribunales(List<Tribunal> tribunales) {
//        this.tribunales = tribunales;
//    }
//
//    public boolean isRenderedEditar() {
//        return renderedEditar;
//    }
//
//    public void setRenderedEditar(boolean renderedEditar) {
//        this.renderedEditar = renderedEditar;
//    }
//
//    public boolean isRenderedBuscar() {
//        return renderedBuscar;
//    }
//
//    public void setRenderedBuscar(boolean renderedBuscar) {
//        this.renderedBuscar = renderedBuscar;
//    }
//
//    public boolean isRenderedDlgEditar() {
//        return renderedDlgEditar;
//    }
//
//    public void setRenderedDlgEditar(boolean renderedDlgEditar) {
//        this.renderedDlgEditar = renderedDlgEditar;
//    }
//
//    public boolean isRenderedTribunal() {
//        return renderedTribunal;
//    }
//
//    public void setRenderedTribunal(boolean renderedTribunal) {
//        this.renderedTribunal = renderedTribunal;
//    }
//
//    public AdministrarEvaluacionesTribunal getAdministrarEvaluacionesTribunal() {
//        return administrarEvaluacionesTribunal;
//    }
//
//    public void setAdministrarEvaluacionesTribunal(AdministrarEvaluacionesTribunal administrarEvaluacionesTribunal) {
//        this.administrarEvaluacionesTribunal = administrarEvaluacionesTribunal;
//    }
//
//    public SessionUsuario getSessionUsuario() {
//        return sessionUsuario;
//    }
//
//    public void setSessionUsuario(SessionUsuario sessionUsuario) {
//        this.sessionUsuario = sessionUsuario;
//    }
//
//    public AdministrarMiembrosTribunal getAdministrarMiembrosTribunal() {
//        return administrarMiembrosTribunal;
//    }
//
//    public void setAdministrarMiembrosTribunal(AdministrarMiembrosTribunal administrarMiembrosTribunal) {
//        this.administrarMiembrosTribunal = administrarMiembrosTribunal;
//    }
//
//    public boolean isRenderedConsultaTribunal() {
//        return renderedConsultaTribunal;
//    }
//
//    public void setRenderedConsultaTribunal(boolean renderedConsultaTribunal) {
//        this.renderedConsultaTribunal = renderedConsultaTribunal;
//    }
//
////</editor-fold>
}
