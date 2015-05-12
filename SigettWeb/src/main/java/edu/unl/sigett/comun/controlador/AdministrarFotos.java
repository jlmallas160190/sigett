/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.comun.controlador;

import edu.unl.sigett.academico.managed.session.SessionDocente;
import edu.unl.sigett.academico.managed.session.SessionDocenteCarrera;
import edu.unl.sigett.academico.managed.session.SessionEstudiante;
import edu.unl.sigett.academico.managed.session.SessionEstudianteCarrera;
import edu.unl.sigett.comun.managed.session.SessionFoto;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import com.jlmallas.comun.entity.Foto;
import com.jlmallas.comun.entity.Persona;
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
import org.primefaces.event.FileUploadEvent;
import com.jlmallas.comun.dao.FotoFacadeLocal;
import org.jlmallas.seguridad.dao.UsuarioDao;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class AdministrarFotos implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionDocente sessionDocente;
    @Inject
    private SessionEstudianteCarrera sessionEstudianteCarrera;
    @Inject
    private SessionFoto sessionFoto;
    @Inject
    private SessionEstudiante sessionEstudiante;
    @EJB
    private FotoFacadeLocal fotoFacadeLocal;
    private String paginaActual;
    @EJB
    private UsuarioDao usuarioFacadeLocal;
    @Inject
    private SessionDocenteCarrera sessionDocenteCarrera;

    public AdministrarFotos() {
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
    public boolean renderedCrear() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_foto");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }

    public boolean renderedEditar() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_foto");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }

    public boolean renderedSeleccionar(String pagina) {
        boolean var = false;
        if (pagina.equalsIgnoreCase("/editarDocente.xhtml")) {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "select_foto_perfil_docente");
            if (tienePermiso == 1) {
                var = true;
            }
        } else {
            if (pagina.equalsIgnoreCase("/editarEstudiante.xhtml") || pagina.equalsIgnoreCase("/editarEstudianteCarrera.xhtml")) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "select_foto_perfil_estudiante");
                if (tienePermiso == 1) {
                    var = true;
                }
            } else {
                if (pagina.equalsIgnoreCase("/editarDocenteCarrera.xhtml")) {
                    int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "select_foto_perfil_docente");
                    if (tienePermiso == 1) {
                        var = true;
                    }
                }
            }
        }
        return var;
    }

    public boolean renderedRemover() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "eliminar_foto");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">   
    public void actualizaEstadoFotos(Persona p, Foto foto) {
        for (Foto f : p.getFotoList()) {
            if (f != foto) {
                f.setEsActual(false);
                if (f.getId() != null) {
                    fotoFacadeLocal.edit(f);
                }
            }
        }
    }

    public List<Foto> buscarPorPersona(Persona persona) {
        List<Foto> fotos = new ArrayList<>();
        try {
            for (Foto foto : fotoFacadeLocal.buscarPorPersona(persona.getId())) {
                fotos.add(foto);
            }
        } catch (Exception e) {
        }
        return fotos;
    }

    public void crear(String pagina) {
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_foto");
            if (tienePermiso == 1) {
                paginaActual = pagina;
                sessionFoto.setFoto(new Foto());
                RequestContext.getCurrentInstance().execute("PF('dlgFoto').show()");
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No tiene permisos para crear Foto. COnsulte con el Administrador del Sistema.", "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void editar(Foto foto, String pagina) {
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_foto");
            if (tienePermiso == 1) {
                paginaActual = pagina;
                sessionFoto.setFoto(foto);
                RequestContext.getCurrentInstance().execute("PF('dlgFoto').show()");
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No tiene permisos para editar Foto. Consulte con el Administrador del Sistema.", "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void grabarFoto(Foto foto) {
        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            if (foto.getId() == null) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_foto");
//                if (tienePermiso == 1) {
//                    foto.setEsActual(false);
//                    if (paginaActual.equalsIgnoreCase("/editarDocente.xhtml")) {
//                        sessionDocente.getPersona().getFotoList().add(foto);
//                        foto.setPersonaId(sessionDocente.getPersona());
//                    } else {
//                        if (paginaActual.equalsIgnoreCase("/editarEstudiante.xhtml")) {
//                            sessionEstudiante.getPersona().getFotoList().add(foto);
//                            foto.setPersonaId(sessionEstudiante.getPersona());
//                        } else {
//                            if (paginaActual.equalsIgnoreCase("/editarDocenteCarrera.xhtml")) {
//                                sessionDocenteCarrera.getPersona().getFotoList().add(foto);
//                                foto.setPersonaId(sessionDocenteCarrera.getPersona());
//                            } else {
//                                if (paginaActual.equalsIgnoreCase("/editarEstudianteCarrera.xhtml")) {
//                                    sessionEstudianteCarrera.getPersona().getFotoList().add(foto);
//                                    foto.setPersonaId(sessionEstudianteCarrera.getPersona());
//                                }
//                            }
//                        }
//                    }
//                    fotoFacadeLocal.create(foto);
//                    RequestContext.getCurrentInstance().execute("PF('dlgFoto').hide()");
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.foto") + " " + bundle.getString("lbl.msm_grabar"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                } else {
//                    if (tienePermiso == 2) {
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm.permiso_denegado_crear") + " ." + bundle.getString("lbl.msm_consulte"), "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                    }
//                }
//            } else {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_foto");
//                if (tienePermiso == 1) {
//                    foto.setEsActual(false);
//                    fotoFacadeLocal.edit(foto);
//                    RequestContext.getCurrentInstance().execute("PF('dlgFoto').hide()");
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.foto") + " " + bundle.getString("lbl.msm_editar"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                } else {
//                    if (tienePermiso == 2) {
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + " ." + bundle.getString("lbl.msm_consulte"), "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                    }
//                }
//            }

        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            String tipoArchivo = event.getFile().getContentType();
            this.sessionFoto.getFoto().setTamanio(event.getFile().getSize());
            sessionFoto.getFoto().setTipoArchivo(tipoArchivo);
            sessionFoto.getFoto().setFoto(event.getFile().getContents());
            grabarFoto(sessionFoto.getFoto());
        } catch (Exception e) {
        }
    }

    public String seleccionarFoto(Foto foto, String pagina) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (pagina.equalsIgnoreCase("/editarDocente.xhtml")) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "select_foto_perfil_docente");
                if (tienePermiso == 1) {
                    actualizaEstadoFotos(foto.getPersonaId(), foto);
                    foto.setEsActual(true);
                    fotoFacadeLocal.edit(foto);
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.foto") + " " + bundle.getString("lbl.seleccionada"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    navegacion = "";
                } else {
                    if (tienePermiso == 2) {
                        navegacion = "";
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + " ." + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    } else {
                        navegacion = "pretty:login";
                    }
                }
            } else {
                if (pagina.equalsIgnoreCase("/editarEstudiante.xhtml")) {
                    int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "select_foto_perfil_estudiante");
                    if (tienePermiso == 1) {
                        actualizaEstadoFotos(foto.getPersonaId(), foto);
                        foto.setEsActual(true);
                        fotoFacadeLocal.edit(foto);
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.foto") + " " + bundle.getString("lbl.seleccionada"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                        navegacion = "";
                    } else {
                        if (tienePermiso == 2) {
                            navegacion = "";
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + " ." + bundle.getString("lbl.msm_consulte"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            navegacion = "pretty:login";
                        }
                    }
                } else {
                    if (pagina.equalsIgnoreCase("/editarDocenteCarrera.xhtml")) {
                        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "select_foto_perfil_docente");
                        if (tienePermiso == 1) {
                            actualizaEstadoFotos(foto.getPersonaId(), foto);
                            foto.setEsActual(true);
                            fotoFacadeLocal.edit(foto);
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.foto") + " " + bundle.getString("lbl.seleccionada"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                            navegacion = "";
                        } else {
                            if (tienePermiso == 2) {
                                navegacion = "";
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + " ." + bundle.getString("lbl.msm_consulte"), "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                            } else {
                                navegacion = "pretty:login";
                            }
                        }
                    } else {
                        if (pagina.equalsIgnoreCase("/editarEstudianteCarrera.xhtml")) {
                            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "select_foto_perfil_docente");
                            if (tienePermiso == 1) {
                                actualizaEstadoFotos(foto.getPersonaId(), foto);
                                foto.setEsActual(true);
                                fotoFacadeLocal.edit(foto);
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.foto") + " " + bundle.getString("lbl.seleccionada"), "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                                navegacion = "";
                            } else {
                                if (tienePermiso == 2) {
                                    navegacion = "";
                                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + " ." + bundle.getString("lbl.msm_consulte"), "");
                                    FacesContext.getCurrentInstance().addMessage(null, message);
                                } else {
                                    navegacion = "pretty:login";
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return navegacion;
    }

    public void removerFoto(Foto foto, String pagina) {
        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "eliminar_foto");
//            if (tienePermiso == 1) {
//                if (foto.getId() != null) {
//                    if (pagina.equalsIgnoreCase("/editarDocente.xhtml")) {
//                        sessionDocente.getPersona().getFotoList().remove(foto);
//                        fotoFacadeLocal.remove(foto);
//                    } else {
//                        if (pagina.equalsIgnoreCase("/editarEstudiante.xhtml")) {
//                            sessionEstudiante.getPersona().getFotoList().remove(foto);
//                            fotoFacadeLocal.remove(foto);
//                        } else {
//                            if (pagina.equalsIgnoreCase("/editarDocenteCarrera.xhtml")) {
//                                sessionDocenteCarrera.getPersona().getFotoList().remove(foto);
//                                fotoFacadeLocal.remove(foto);
//                            } else {
//                                if (pagina.equalsIgnoreCase("/editarDocenteCarrera.xhtml")) {
//                                    sessionEstudianteCarrera.getPersona().getFotoList().remove(foto);
//                                    fotoFacadeLocal.remove(foto);
//                                }
//                            }
//                        }
//                    }
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.foto") + " " + bundle.getString("lbl.msm_eliminar"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
//            } else {
//                if (tienePermiso == 2) {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_eliminar") + " ." + bundle.getString("lbl.msm_consulte"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
//            }
        } catch (Exception e) {
        }

    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
    public SessionEstudianteCarrera getSessionEstudianteCarrera() {
        return sessionEstudianteCarrera;
    }

    public void setSessionEstudianteCarrera(SessionEstudianteCarrera sessionEstudianteCarrera) {
        this.sessionEstudianteCarrera = sessionEstudianteCarrera;
    }

    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public SessionDocente getSessionDocente() {
        return sessionDocente;
    }

    public void setSessionDocente(SessionDocente sessionDocente) {
        this.sessionDocente = sessionDocente;
    }

    public SessionFoto getSessionFoto() {
        return sessionFoto;
    }

    public void setSessionFoto(SessionFoto sessionFoto) {
        this.sessionFoto = sessionFoto;
    }

    public SessionEstudiante getSessionEstudiante() {
        return sessionEstudiante;
    }

    public void setSessionEstudiante(SessionEstudiante sessionEstudiante) {
        this.sessionEstudiante = sessionEstudiante;
    }

    public SessionDocenteCarrera getSessionDocenteCarrera() {
        return sessionDocenteCarrera;
    }

    public void setSessionDocenteCarrera(SessionDocenteCarrera sessionDocenteCarrera) {
        this.sessionDocenteCarrera = sessionDocenteCarrera;
    }

//</editor-fold>
}
